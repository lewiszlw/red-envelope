package lewiszlw.redenvelope.service;

import lewiszlw.redenvelope.constant.Constants;
import lewiszlw.redenvelope.constant.ExistentStatus;
import lewiszlw.redenvelope.converter.RedEnvelopeConverter;
import lewiszlw.redenvelope.entity.EnvelopeDetailEntity;
import lewiszlw.redenvelope.entity.EnvelopeGrabberEntity;
import lewiszlw.redenvelope.mapper.RedEnvelopeDetailMapper;
import lewiszlw.redenvelope.mapper.RedEnvelopeGrabberMapper;
import lewiszlw.redenvelope.model.GrabbingResult;
import lewiszlw.redenvelope.model.mq.GrabbingMessage;
import lewiszlw.redenvelope.model.redis.EnvelopeRedisModel;
import lewiszlw.redenvelope.model.redis.GrabbingDetail;
import lewiszlw.redenvelope.model.req.CreateEnvelopeReq;
import lewiszlw.redenvelope.mq.RedEnvelopeGrabbingProducer;
import lewiszlw.redenvelope.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-20
 */
@Service
@Slf4j
public class RedEnvelopeService {

    @Autowired
    private RedEnvelopeDetailMapper redEnvelopeDetailMapper;

    @Autowired
    private RedEnvelopeGrabberMapper redEnvelopeGrabberMapper;

    @Autowired
    private RedEnvelopeRedisService redEnvelopeRedisService;

    @Autowired
    private RedEnvelopeGrabbingProducer redEnvelopeGrabbingProducer;

    @Autowired
    private DistributedLock distributedLock;

    /**
     * 发红包
     */
    public Integer insertRedEnvelope(CreateEnvelopeReq req) {
        // 插入数据库
        EnvelopeDetailEntity envelopeDetailEntity = RedEnvelopeConverter.convertToEnvelopeDetailEntity(req);
        envelopeDetailEntity.setRemainMoney(envelopeDetailEntity.getAmount());
        envelopeDetailEntity.setRemainSize(envelopeDetailEntity.getSize());
        redEnvelopeDetailMapper.insertOne(envelopeDetailEntity);
        // 存入redis
        redEnvelopeRedisService.setAfterCreate(envelopeDetailEntity);
        return envelopeDetailEntity.getId();
    }

    /**
     * 抢红包
     */
    public GrabbingResult grabRedEnvelope(Integer envelopeId, String grabber) {
        // 查redis
        EnvelopeRedisModel envelopeRedisModel = redEnvelopeRedisService.get(envelopeId);
        if (envelopeRedisModel == null) {
            // 缓存为空, 防止缓存穿透、缓存击穿
            while (true) {
                envelopeRedisModel = redEnvelopeRedisService.get(envelopeId);
                if (envelopeRedisModel == null) {
                    // 尝试加互斥锁
                    if (redEnvelopeRedisService.lock("lock-" + envelopeId)) {
                        // 加锁成功，从DB load数据到缓存
                        loadDataToRedis(envelopeId);
                        // 释放锁
                        redEnvelopeRedisService.unlock("lock-" + envelopeId);
                    } else {
                        // 加锁失败，循环查缓存并尝试加锁
                        continue;
                    }
                } else {
                    // 数据已从DB load到缓存
                    break;
                }
            }
        }
        if (ExistentStatus.NON_EXISTENT.equals(envelopeRedisModel.getExistentStatus())) {
            // 红包不存在，防止缓存穿透
            return GrabbingResult.createFailGrabbingResult();
        }
        GrabbingDetail grabbingDetail = CommonUtils.find(envelopeRedisModel.getGrabbingDetails(), gd -> Objects.equals(gd.getGrabber(), grabber));
        if (grabbingDetail == null
                && envelopeRedisModel.getRemainMoney() > 0
                && envelopeRedisModel.getRemainSize() > 0) {
            // 红包有余份，且该用户未抢到，进行抢红包流程
            return doGrab(envelopeId, grabber);
        } else {
            // 红包过期已抢完或该用户已抢到
            return grabbingDetail == null ? GrabbingResult.createFailGrabbingResult()
                                            : GrabbingResult.createSuccessGrabbingResult(grabbingDetail.getAmount());
        }
    }

    /**
     * 处理mq消息
     * @param grabbingMessages
     */
    public void handleGrabbingMessage(List<GrabbingMessage> grabbingMessages) {
        if (CollectionUtils.isEmpty(grabbingMessages)) {
            return;
        }
        for (GrabbingMessage grabbingMessage : grabbingMessages) {
            // 利用mybatis来实现数据库排他锁（for update），感觉不够优雅，故采用分布式锁
            // 加锁
            distributedLock.lock(grabbingMessage.getEnvelopeId());

            EnvelopeDetailEntity envelopeDetailEntity = redEnvelopeDetailMapper.selectOne(grabbingMessage.getEnvelopeId());
            if (envelopeDetailEntity == null) {
                log.error("处理mq消息：查询红包不存在, grabbingMessage: {}", JsonUtils.toJson(grabbingMessage));
                // 释放锁
                distributedLock.unlock(grabbingMessage.getEnvelopeId());
                continue;
            }
            // 查看该用户是否已经抢到红包
            List<EnvelopeGrabberEntity> envelopeGrabberEntities = redEnvelopeGrabberMapper.selectByEnvelopeId(grabbingMessage.getEnvelopeId());
            EnvelopeGrabberEntity existEnvelopeGrabberEntity = CommonUtils.find(envelopeGrabberEntities,
                    (envelopeGrabberEntity) -> Objects.equals(envelopeGrabberEntity.getGrabber(), grabbingMessage.getGrabber()));

            if (envelopeDetailEntity.getRemainMoney() > 0
                    && envelopeDetailEntity.getRemainSize() > 0
                    && existEnvelopeGrabberEntity == null) {
                // 红包有余份，且该用户未抢到红包，事务内扣减红包金额
                allocateAndWriteDB(envelopeDetailEntity, grabbingMessage.getGrabber());
            }
            // 更新缓存
            EnvelopeDetailEntity currentEnvelopeDetailEntity = redEnvelopeDetailMapper
                    .selectOne(grabbingMessage.getEnvelopeId());
            List<EnvelopeGrabberEntity> currentEnvelopeGrabberEntities = redEnvelopeGrabberMapper
                    .selectByEnvelopeId(grabbingMessage.getEnvelopeId());
            redEnvelopeRedisService.delAndSet(currentEnvelopeDetailEntity, currentEnvelopeGrabberEntities);
            // 释放锁
            distributedLock.unlock(grabbingMessage.getEnvelopeId());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void allocateAndWriteDB(EnvelopeDetailEntity envelopeDetailEntity, String grabber) {
        Integer remainSize = envelopeDetailEntity.getRemainSize();
        Integer remainMoney = envelopeDetailEntity.getRemainMoney();

        // 分配金额
        Integer allocateMoney = AllocationUtils.allocateV2(remainMoney, remainSize);

        envelopeDetailEntity.setRemainMoney(remainMoney - allocateMoney);
        envelopeDetailEntity.setRemainSize(remainSize - 1);

        // 防止查询后，数据被其他线程更新，导致金额不是当前金额，即需解决不可重复读
        redEnvelopeDetailMapper.updateOne(envelopeDetailEntity);

        redEnvelopeGrabberMapper.insertOne(new EnvelopeGrabberEntity()
                .setEnvelopeId(envelopeDetailEntity.getId())
                .setGrabber(grabber)
                .setMoney(allocateMoney)
        );
        log.info("分配金额, envelopeDetailEntity: {}, grabber: {}, allocatedMoney: {}",
                JsonUtils.toJson(envelopeDetailEntity), grabber, allocateMoney);
    }

    private GrabbingResult doGrab(Integer envelopeId, String grabber) {
        // 发送mq消息
        redEnvelopeGrabbingProducer.send(JsonUtils.toJson(
                new GrabbingMessage().setEnvelopeId(envelopeId).setGrabber(grabber)));
        // 返回正在抢红包中，客户端轮询查看是否抢红包结果
        return GrabbingResult.createGrabbingGrabbingResult();
    }

    /**
     * 加载数据到缓存
     * @param envelopeId 红包id
     */
    private void loadDataToRedis(Integer envelopeId) {
        EnvelopeDetailEntity envelopeDetailEntity = redEnvelopeDetailMapper.selectOne(envelopeId);
        if (envelopeDetailEntity == null) {
            // 加载一个标记为不存在的envelopeId红包到redis，防止缓存穿透，过期时间设置为1小时
            redEnvelopeRedisService.set(new EnvelopeRedisModel()
                    .setEnvelopeId(envelopeId).setExistentStatus(ExistentStatus.NON_EXISTENT), 1 * 3600);
            return;
        }
        List<EnvelopeGrabberEntity> envelopeGrabberEntities = redEnvelopeGrabberMapper
                .selectByEnvelopeId(envelopeId);

        EnvelopeRedisModel envelopeRedisModel = RedEnvelopeConverter
                .convertToEnvelopeRedisModel(envelopeDetailEntity, envelopeGrabberEntities);
        redEnvelopeRedisService.set(envelopeRedisModel, Constants.ENVELOPE_EXPIRE_TIME);
    }
}

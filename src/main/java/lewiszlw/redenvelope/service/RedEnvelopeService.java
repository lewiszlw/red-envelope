package lewiszlw.redenvelope.service;

import lewiszlw.redenvelope.constant.EnvelopeStatus;
import lewiszlw.redenvelope.converter.RedEnvelopeConverter;
import lewiszlw.redenvelope.entity.EnvelopeDetailEntity;
import lewiszlw.redenvelope.mapper.RedEnvelopeDetailMapper;
import lewiszlw.redenvelope.model.GrabbingResult;
import lewiszlw.redenvelope.model.mq.GrabbingMessage;
import lewiszlw.redenvelope.model.redis.EnvelopeRedisModel;
import lewiszlw.redenvelope.model.redis.GrabbingDetail;
import lewiszlw.redenvelope.model.req.CreateEnvelopeReq;
import lewiszlw.redenvelope.mq.RedEnvelopeGrabbingProducer;
import lewiszlw.redenvelope.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-20
 */
@Service
public class RedEnvelopeService {

    @Autowired
    private RedEnvelopeDetailMapper redEnvelopeDetailMapper;

    @Autowired
    private RedEnvelopeRedisService redEnvelopeRedisService;

    @Autowired
    private RedEnvelopeGrabbingProducer redEnvelopeGrabbingProducer;

    /**
     * 发红包
     */
    public void insertRedEnvelope(CreateEnvelopeReq req) {
        // 插入数据库
        EnvelopeDetailEntity envelopeDetailEntity = RedEnvelopeConverter.convertToEnvelopeDetailEntity(req);
        envelopeDetailEntity.setStatus(EnvelopeStatus.Unexpired);
        envelopeDetailEntity.setRemainMoney(envelopeDetailEntity.getAmount());
        envelopeDetailEntity.setRemainSize(envelopeDetailEntity.getSize());
        redEnvelopeDetailMapper.insertOne(envelopeDetailEntity);
        // 存入redis
        redEnvelopeRedisService.set(envelopeDetailEntity);
    }

    /**
     * 抢红包
     */
    public GrabbingResult grabRedEnvelope(Integer envelopeId, String grabber) {
        // 查redis
        EnvelopeRedisModel envelopeRedisModel = redEnvelopeRedisService.get(envelopeId);
        if (envelopeRedisModel == null) {
            // 缓存为空, 防止缓存穿透、缓存击穿、缓存雪崩
            while (true) {
                envelopeRedisModel = redEnvelopeRedisService.get(envelopeId);
                if (envelopeRedisModel == null) {
                    // 尝试加互斥锁
                    if (redEnvelopeRedisService.lock("lock-" + envelopeId)) {
                        // 加锁成功，从DB load数据到缓存
                        // TODO
                    } else {
                        // 加锁失败，循环查缓存并尝试加锁
                        continue;
                    }
                } else {
                    // 数据已从DB load到缓存
                    break;
                }
            }
            // TODO

        }
        if (EnvelopeStatus.Unexpired.equals(envelopeRedisModel.getStatus()) && envelopeRedisModel.getRemainSize() > 0) {
            // 抢红包
            return doGrab(envelopeRedisModel, grabber);
        } else {
            // 红包过期或已抢完
            GrabbingDetail grabbingDetail = findGrabbingDetail(envelopeRedisModel.getGrabbingDetails(), grabber);
            if (grabbingDetail == null) {
                return GrabbingResult.createFailGrabbingResult();
            } else {
                return GrabbingResult.createSuccessGrabbingResult(grabbingDetail.getAmount());
            }
        }
    }

    private GrabbingDetail findGrabbingDetail(List<GrabbingDetail> grabbingDetails, String grabber) {
        if (CollectionUtils.isEmpty(grabbingDetails)) {
            return null;
        }
        for (GrabbingDetail grabbingDetail : grabbingDetails) {
            if (Objects.equals(grabbingDetail.getGrabber(), grabber)) {
                return grabbingDetail;
            }
        }
        return null;
    }

    private GrabbingResult doGrab(EnvelopeRedisModel envelopeRedisModel, String grabber) {
        // 发送mq消息
        redEnvelopeGrabbingProducer.send(JsonUtils.toJson(
                new GrabbingMessage().setEnvelopeRedisModel(envelopeRedisModel).setGrabber(grabber)));
        // 返回正在抢红包中，客户端轮询查看是否抢红包结果
        return GrabbingResult.createGrabbingGrabbingResult();
    }
}

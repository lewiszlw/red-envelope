package lewiszlw.redenvelope.service;

import lewiszlw.redenvelope.constant.Constants;
import lewiszlw.redenvelope.converter.RedEnvelopeConverter;
import lewiszlw.redenvelope.entity.EnvelopeDetailEntity;
import lewiszlw.redenvelope.entity.EnvelopeGrabberEntity;
import lewiszlw.redenvelope.model.redis.EnvelopeRedisModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-20
 */
@Service
public class RedEnvelopeRedisService {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    private final static int LOCK_REDIS_TIMEOUT = 20;

    public void setAfterCreate(EnvelopeDetailEntity envelopeDetailEntity) {
        EnvelopeRedisModel envelopeRedisModel = RedEnvelopeConverter.convertToEnvelopeRedisModel(envelopeDetailEntity);
        redisTemplate.opsForValue().set(envelopeDetailEntity.getId().toString(),
                envelopeRedisModel, Constants.ENVELOPE_EXPIRE_TIME, TimeUnit.SECONDS);
    }

    public void delAndSet(EnvelopeDetailEntity envelopeDetailEntity, List<EnvelopeGrabberEntity> envelopeGrabberEntities) {
        // 先删除
        redisTemplate.delete(envelopeDetailEntity.getId().toString());
        // 再set
        EnvelopeRedisModel envelopeRedisModel = RedEnvelopeConverter.convertToEnvelopeRedisModel(envelopeDetailEntity, envelopeGrabberEntities);
        redisTemplate.opsForValue().set(envelopeDetailEntity.getId().toString(), envelopeRedisModel);
    }

    public void set(EnvelopeRedisModel envelopeRedisModel, int timeout) {
        redisTemplate.opsForValue().set(envelopeRedisModel.getEnvelopeId().toString(),
                envelopeRedisModel, timeout, TimeUnit.SECONDS);
    }

    public EnvelopeRedisModel get(Integer envelopeId) {
        return (EnvelopeRedisModel) redisTemplate.opsForValue().get(envelopeId.toString());
    }

    public void del(Integer envelopeId) {
        redisTemplate.delete(envelopeId.toString());
    }

    public boolean lock(String lockId) {
        Boolean lockResult = redisTemplate.opsForValue().setIfAbsent(lockId, lockId, LOCK_REDIS_TIMEOUT, TimeUnit.SECONDS);
        return lockResult != null && lockResult;
    }

    public void unlock(String lockId) {
        redisTemplate.delete(lockId);
    }
}

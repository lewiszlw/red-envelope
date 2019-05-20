package lewiszlw.redenvelope.service;

import lewiszlw.redenvelope.constant.Constants;
import lewiszlw.redenvelope.converter.RedEnvelopeConverter;
import lewiszlw.redenvelope.entity.EnvelopeDetailEntity;
import lewiszlw.redenvelope.exception.LockTimeOutException;
import lewiszlw.redenvelope.model.redis.EnvelopeRedisModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

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

    private final static int LOCK_REDIS_TIMEOUT = 10;

    public void set(EnvelopeDetailEntity envelopeDetailEntity) {
        EnvelopeRedisModel envelopeRedisModel = RedEnvelopeConverter.convertToEnvelopeRedisModel(envelopeDetailEntity);
        redisTemplate.opsForValue().set(envelopeDetailEntity.getId(),
                envelopeRedisModel, Constants.ENVELOPE_EXPIRE_TIME, TimeUnit.SECONDS);
    }

    public EnvelopeRedisModel get(Integer envelopeId) {
        return (EnvelopeRedisModel) redisTemplate.opsForValue().get(envelopeId);
    }

    public boolean lock(String lockId) {
        Boolean lockResult = redisTemplate.opsForValue().setIfAbsent(lockId, lockId, LOCK_REDIS_TIMEOUT, TimeUnit.SECONDS);
        return lockResult != null && lockResult;
    }

    public void unlock(String lockId) {
        redisTemplate.delete(lockId);
    }
}

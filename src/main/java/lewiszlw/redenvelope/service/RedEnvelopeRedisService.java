package lewiszlw.redenvelope.service;

import lewiszlw.redenvelope.entity.EnvelopeDetailEntity;
import lewiszlw.redenvelope.model.req.CreateEnvelopeReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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

    public void insertRedEnvelope(EnvelopeDetailEntity envelopeDetailEntity) {

    }
}

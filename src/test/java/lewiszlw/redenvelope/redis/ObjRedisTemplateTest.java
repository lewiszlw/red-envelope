package lewiszlw.redenvelope.redis;

import lewiszlw.redenvelope.entity.EnvelopeDetailEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-20
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ObjRedisTemplateTest {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Test
    public void testSetAndGet() {
        redisTemplate.opsForValue().set("test-key", new EnvelopeDetailEntity().setAmount(1000l).setSize(10), 3, TimeUnit.SECONDS);
        EnvelopeDetailEntity envelopeDetailEntity = (EnvelopeDetailEntity) redisTemplate.opsForValue().get("test-key");
        Assert.assertTrue(envelopeDetailEntity.getAmount() == 1000l);
        Assert.assertTrue(envelopeDetailEntity.getSize() == 10);
    }
}

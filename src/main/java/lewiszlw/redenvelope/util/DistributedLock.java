package lewiszlw.redenvelope.util;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-22
 */
@Slf4j
@Component
public class DistributedLock {

    // 分布式锁的过期时间（秒）
    private final static int DIS_LOCK_TIMEOUT = 1 * 3600;
    private final static String LOCK_VALUE = "LOCK_VALUE";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void lock(Integer envelopeId) {
        String lockId = genLockId(envelopeId);
        Boolean lockResult = null;
        try {
            lockResult = stringRedisTemplate.opsForValue().setIfAbsent(lockId, LOCK_VALUE, DIS_LOCK_TIMEOUT, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        while (null == lockResult || !lockResult) {
            // 自旋
            // TODO 锁膨胀
            lockResult = stringRedisTemplate.opsForValue().setIfAbsent(lockId, LOCK_VALUE, DIS_LOCK_TIMEOUT, TimeUnit.SECONDS);
        }
        log.info("加锁成功 lockId: {}", lockId);
    }

    public void unlock(Integer envelopeId) {
        String lockId = genLockId(envelopeId);
        Preconditions.checkArgument(!StringUtils.isEmpty(lockId), "lockId is empty");
        stringRedisTemplate.delete(lockId);
        log.info("释放锁成功 lockId: {}", lockId);
    }

    public String genLockId(Integer envelopeId) {
        Preconditions.checkArgument(envelopeId != null, "envelopeId is null");
        return "LOCK-" + envelopeId.toString();
    }
}

package lewiszlw.redenvelope.util;

import com.google.common.base.Preconditions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-21
 */
public class DisLockUtils {

    private final static StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();

    // 分布式锁的过期时间（秒）
    private final static int DIS_LOCK_TIMEOUT = 1 * 3600;
    private final static String LOCK_VALUE = "LOCK_VALUE";

    public static void lock(Integer envelopeId) {
        String lockId = genLockId(envelopeId);
        Boolean lockResult = stringRedisTemplate.opsForValue().setIfAbsent(lockId, LOCK_VALUE, DIS_LOCK_TIMEOUT, TimeUnit.SECONDS);
        while (null == lockResult || !lockResult) {
            // 自旋
            // TODO 锁膨胀
            lockResult = stringRedisTemplate.opsForValue().setIfAbsent(lockId, LOCK_VALUE, DIS_LOCK_TIMEOUT, TimeUnit.SECONDS);
        }
    }

    public static void unlock(Integer envelopeId) {
        String lockId = genLockId(envelopeId);
        Preconditions.checkArgument(!StringUtils.isEmpty(lockId), "lockId is empty");
        stringRedisTemplate.delete(lockId);
    }

    public static String genLockId(Integer envelopeId) {
        Preconditions.checkArgument(envelopeId != null, "envelopeId is null");
        return "LOCK-" + envelopeId.toString();
    }
}

package lewiszlw.redenvelope.aop;

import com.google.common.util.concurrent.RateLimiter;
import lewiszlw.redenvelope.annotation.RateLimit;
import lewiszlw.redenvelope.model.WebResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-21
 */
@Aspect
@Component
public class RateLimitAspect implements InitializingBean {

    @Value("${ratelimit.permitsPerSecond}")
    private Double permitsPerSecond;

    private RateLimiter rateLimiter;

    @Override
    public void afterPropertiesSet() throws Exception {
        rateLimiter = RateLimiter.create(permitsPerSecond);
    }

    @Pointcut("@annotation(lewiszlw.redenvelope.annotation.RateLimit)")
    public void rateLimitPoingcut() {}

    @Around("rateLimitPoingcut()&&@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint pjp, RateLimit rateLimit) throws Throwable {
        Object proceedResult;
        boolean permit = this.rateLimiter.tryAcquire(1, TimeUnit.SECONDS);
        if (permit) {
            proceedResult = pjp.proceed();
        } else {
            proceedResult = WebResponse.createFailWebResponse("系统繁忙，请稍后再试");
        }
        return proceedResult;
    }

}

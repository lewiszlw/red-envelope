package lewiszlw.redenvelope.model.mq;

import lewiszlw.redenvelope.model.redis.EnvelopeRedisModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-20
 */
@Data
@Accessors(chain = true)
public class GrabbingMessage {

    private EnvelopeRedisModel envelopeRedisModel;

    private String grabber;
}

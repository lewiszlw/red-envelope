package lewiszlw.redenvelope.model.redis;

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
public class GrabbingDetail {

    /**
     * 抢到红包者
     */
    private String grabber;

    /**
     * 抢到红包金额
     */
    private Integer amount;
}

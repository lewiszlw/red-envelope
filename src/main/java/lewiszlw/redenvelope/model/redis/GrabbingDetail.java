package lewiszlw.redenvelope.model.redis;

import lombok.Data;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-20
 */
@Data
public class GrabbingDetail {

    /**
     * 抢到红包者
     */
    private String grabber;

    /**
     * 抢到红包金额
     */
    private Long amount;
}

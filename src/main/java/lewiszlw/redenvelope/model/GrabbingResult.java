package lewiszlw.redenvelope.model;

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
public class GrabbingResult {

    enum GrabbingStatus {
        SUCCESS, FAILURE, GRABBING
    }

    /**
     * 抢红包状态
     */
    private GrabbingStatus status;

    /**
     * 抢到红包金额
     * 单位：分
     */
    private Integer amount;

    public static GrabbingResult createSuccessGrabbingResult(int amount) {
        return new GrabbingResult().setStatus(GrabbingStatus.SUCCESS).setAmount(amount);
    }

    public static GrabbingResult createFailGrabbingResult() {
        return new GrabbingResult().setStatus(GrabbingStatus.FAILURE);
    }

    public static GrabbingResult createGrabbingGrabbingResult() {
        return new GrabbingResult().setStatus(GrabbingStatus.GRABBING);
    }
}

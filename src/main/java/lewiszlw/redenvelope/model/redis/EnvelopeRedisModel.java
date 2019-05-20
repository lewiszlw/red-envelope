package lewiszlw.redenvelope.model.redis;

import lewiszlw.redenvelope.constant.EnvelopeType;
import lewiszlw.redenvelope.entity.EnvelopeDetailEntity;
import lewiszlw.redenvelope.entity.EnvelopeGrabberEntity;
import lombok.Data;

import java.util.List;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-20
 */
@Data
public class EnvelopeRedisModel {
    /**
     * 红包id
     */
    private Integer envelopeId;

    /**
     * 发红包者
     */
    private String user;

    /**
     * 红包类型
     */
    private EnvelopeType type;

    /**
     * 红包总额
     */
    private Long amount;

    /**
     * 红包份数
     */
    private Integer size;

    /**
     * 红包余额
     */
    private Long remainMoney;

    /**
     * 红包余下份数
     */
    private Integer remainSize;

    /**
     * 红包抢到者情况
     */
    private List<GrabbingDetail> grabbingDetails;
}

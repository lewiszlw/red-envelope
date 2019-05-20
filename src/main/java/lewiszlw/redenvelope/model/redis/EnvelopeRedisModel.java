package lewiszlw.redenvelope.model.redis;

import lewiszlw.redenvelope.constant.EnvelopeStatus;
import lewiszlw.redenvelope.constant.EnvelopeType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-20
 */
@Data
@Accessors(chain = true)
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
    private Integer amount;

    /**
     * 红包份数
     */
    private Integer size;

    /**
     * 红包余额
     */
    private Integer remainMoney;

    /**
     * 红包余下份数
     */
    private Integer remainSize;

    /**
     * 红包切分列表
     * 红包预先分配好每个红包大小，每分配出去一个，remove一个
     */
    private List<Integer> allocations;

    /**
     * 红包抢到情况
     */
    private List<GrabbingDetail> grabbingDetails;

    /**
     * 红包是否过期
     */
    private EnvelopeStatus status;
}

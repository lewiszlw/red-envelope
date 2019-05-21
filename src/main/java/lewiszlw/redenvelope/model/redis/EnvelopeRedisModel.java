package lewiszlw.redenvelope.model.redis;

import lewiszlw.redenvelope.constant.EnvelopeType;
import lewiszlw.redenvelope.constant.ExistentStatus;
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
     * 数据是否存在
     * @see ExistentStatus#NON_EXISTENT 红包不存在，用于防止缓存穿透
     */
    private ExistentStatus existentStatus;
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
     * 红包抢到情况
     */
    private List<GrabbingDetail> grabbingDetails;
}

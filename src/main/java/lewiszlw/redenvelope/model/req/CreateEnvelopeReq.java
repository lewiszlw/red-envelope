package lewiszlw.redenvelope.model.req;

import lewiszlw.redenvelope.constant.EnvelopeType;
import lombok.Data;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-20
 */
@Data
public class CreateEnvelopeReq {

    /**
     * 发红包者
     */
    private String user;

    /**
     * 红包类型
     */
    private EnvelopeType type;

    /**
     * 红包金额（单位: 分）
     */
    private Integer amount;

    /**
     * 红包份数
     */
    private Integer size;
}

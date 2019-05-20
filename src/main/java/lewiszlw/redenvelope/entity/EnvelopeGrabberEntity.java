package lewiszlw.redenvelope.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-20
 */
@Data
@Accessors(chain = true)
public class EnvelopeGrabberEntity {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 红包id
     * 即envelope_detail主键
     */
    private Integer envelopeId;

    /**
     * 抢到用户
     */
    private String grabber;

    /**
     * 抢到金额
     */
    private Integer money;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间
     */
    private Date updatedTime;
}

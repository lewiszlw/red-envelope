package lewiszlw.redenvelope.entity;

import lewiszlw.redenvelope.constant.EnvelopeType;
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
public class EnvelopeDetailEntity {

    /**
     * 主键
     */
    private Integer id;

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
    private Long remainSize;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间
     */
    private Date updatedTime;
}

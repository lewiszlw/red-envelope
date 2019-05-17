package lewiszlw.redenvelope.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-29
 */
@Data
@Accessors(chain = true)
public class RedEnvelope {

    private BigDecimal remainMoney;

    private int remainSize;
}

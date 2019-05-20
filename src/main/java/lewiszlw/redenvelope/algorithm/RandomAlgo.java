package lewiszlw.redenvelope.algorithm;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-29
 */
public class RandomAlgo {

    private static Random random = new Random();
    private static BigDecimal min = new BigDecimal(0.01).setScale(2, BigDecimal.ROUND_DOWN);

    /**
     * 算法来源
     * https://www.zhihu.com/question/22625187/answer/85530416
     */
    public static BigDecimal randomAlgo(RedEnvelope redEnvelope) {
        if (redEnvelope.getRemainSize() == 0) {
            return new BigDecimal(0);
        }
        if (redEnvelope.getRemainMoney().compareTo(min.multiply(new BigDecimal(redEnvelope.getRemainSize()))) == -1) {
            throw new IllegalStateException(String.format("红包金额 %.2f 不够 %d 个人分",
                    redEnvelope.getRemainMoney().setScale(2, BigDecimal.ROUND_DOWN).doubleValue(),
                    redEnvelope.getRemainSize()));
        }
        if (redEnvelope.getRemainSize() == 1) {
            redEnvelope.setRemainSize(0);
            BigDecimal remainMoney = redEnvelope.getRemainMoney().setScale(2, BigDecimal.ROUND_DOWN);
            redEnvelope.setRemainMoney(new BigDecimal(0));
            return remainMoney;
        }
        BigDecimal max = redEnvelope.getRemainMoney()
                .multiply(new BigDecimal(2))
                .divide(new BigDecimal(redEnvelope.getRemainSize()), 2, BigDecimal.ROUND_DOWN);
        BigDecimal money = max.multiply(new BigDecimal(random.nextDouble()));
        if (money.compareTo(min) == -1) {
            // money小于min
            money = min;
        }
        money = money.setScale(2, BigDecimal.ROUND_DOWN);
        redEnvelope.setRemainSize(redEnvelope.getRemainSize() - 1);
        redEnvelope.setRemainMoney(redEnvelope.getRemainMoney().subtract(money));
        return money;
    }

    /**
     * 算法来源
     * https://www.bilibili.com/video/av19582887/
     *
     * @param redEnvelope
     * @return
     */
    public static int randomAlgoV2(RedEnvelope redEnvelope) {
        return 0;
    }
}

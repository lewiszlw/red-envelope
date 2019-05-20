package lewiszlw.redenvelope.util;

import lewiszlw.redenvelope.constant.Constants;

import java.math.BigDecimal;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-20
 */
public class MoneyUtils {

    /**
     * 前端价格转换为DB存储格式
     * @param money 单位：元
     * @return 单位：分
     */
    public static long feToDB(double money) {
        return BigDecimal.valueOf(money).multiply(BigDecimal.valueOf(Constants.MONEY_SCALE_NUMBER)).longValue();
    }

    /**
     * DB存储格式转换为前端价格
     * @param money 单位：分
     * @return 单位：元
     */
    public static double dbToFE(long money) {
        return BigDecimal.valueOf(money).divide(
                BigDecimal.valueOf(Constants.MONEY_SCALE_NUMBER), 2, BigDecimal.ROUND_DOWN
        ).doubleValue();
    }
}

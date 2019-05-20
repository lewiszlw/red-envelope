package lewiszlw.redenvelope.util;

import lewiszlw.redenvelope.constant.Constants;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-20
 */
@Slf4j
public class AllocationUtils {

    public static Random random = new Random();

    public static List<Integer> allocate(int amount, int size) {
        if (amount <= 0 || size <= 0) {
            throw new IllegalArgumentException();
        }
        List<Integer> allocationResult = new ArrayList<>(size);
        // 红包剩余总额
        int remainAmount = amount;
        for (int i = 0; i < size; i++) {
            if (i == size) {
                // 最后一人，把剩余钱都分配给他
                allocationResult.add(remainAmount);
                break;
            }
            // 需给后面至少每人预留最小红包金额
            int amountForGrabbing = remainAmount - Constants.ENVELOPE_MIN_GRAB_AMOUNT * (size - i - 1);
            // 当前若平均分配，每人分得金额，使用int好随机，红包金额暂时设置不超过200元
            int averageAmount = BigDecimal.valueOf(amountForGrabbing).divide(BigDecimal.valueOf(size - i), 2, BigDecimal.ROUND_DOWN).intValue();
            // 本地分配0到两份平均金额的随机值（均匀分布）
            int allocation;
            if (averageAmount <= 0) {
                allocation = Constants.ENVELOPE_MIN_GRAB_AMOUNT;
            } else {
                allocation = random.nextInt(averageAmount * 2);
                if (allocation < Constants.ENVELOPE_MIN_GRAB_AMOUNT) {
                    // 如果本次分配小于红包最小分配金额，则分配最小分配金额
                    allocation = Constants.ENVELOPE_MIN_GRAB_AMOUNT;
                }
                if (allocation > amountForGrabbing) {
                    // 如果本次分配超过了本次最大分配金额
                    allocation = amountForGrabbing;
                }
            }
            allocationResult.add(allocation);
            remainAmount -= allocation;
        }
        // 验证分配正确性
        Integer allocationAmount = 0;
        for (Integer allocation : allocationResult) {
            assert allocation >= Constants.ENVELOPE_MIN_GRAB_AMOUNT;
            allocationAmount += allocation;
        }
        assert allocationAmount <= amount;
        assert allocationResult.size() == size;
        return allocationResult;
    }
}

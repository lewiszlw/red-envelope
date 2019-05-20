package lewiszlw.redenvelope.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Random;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-20
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AllocationUtilsTest {

    @Test
    @Repeat(500)
    public void testAllocate() {
        Random random = new Random();
        // 红包金额, 非0
        int amount = (random.nextInt(200 * 100));
        while (amount == 0) {
            amount = (random.nextInt(1000));
        }
        System.out.println("红包总金额: " + amount + " 分");
        // 人数，非0
        int personNum = random.nextInt(200 * 100);
        while (personNum == 0) {
            personNum = random.nextInt(200 * 100);
        }
        System.out.println("分配人数: " + personNum);

        if (amount < personNum) {
            System.out.println("金额太少，无法分配");
        } else {
            List<Integer> allocate = AllocationUtils.allocate(amount, personNum);
            System.out.println("分配详情：" + allocate);
        }
    }
}

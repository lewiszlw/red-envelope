package lewiszlw.redenvelope.algorithm;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-29
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RandomAlgoTest {

    Random random = new Random();

    @Test
    @Repeat(10000)
    public void testRandomAlgo1() {
        // 红包金额, 非0
        int amount = (random.nextInt(1000));
        while (amount == 0) {
            amount = (random.nextInt(1000));
        }
        System.out.println("红包总金额: " + amount);
        // 人数，非0
        int personNum = random.nextInt(200);
        while (personNum == 0) {
            personNum = random.nextInt(200);
        }
        System.out.println("分配人数: " + personNum);
        if (amount < personNum * 0.01) {
            System.out.println("金额太少，无法分配");
        } else {
            RedEnvelope redEnvelope = new RedEnvelope().setRemainMoney(new BigDecimal(amount)).setRemainSize(personNum);
            List<BigDecimal> envelops = new ArrayList<>();
            for (int i = 0; i < personNum; i++) {
                BigDecimal envelop = RandomAlgo.randomAlgo(redEnvelope);
                envelops.add(envelop);
                System.out.println("第 " + (i + 1) + " 个红包: " + envelop.doubleValue());
            }
            BigDecimal assignAmount = new BigDecimal(0);
            for (int i = 0; i < envelops.size(); i++) {
                Assert.assertTrue(envelops.get(i).doubleValue() >= 0.01);
                assignAmount = assignAmount.add(envelops.get(i));
            }
            System.out.println("分配红包实际总金额: " + assignAmount.doubleValue());
            Assert.assertTrue(assignAmount.doubleValue() <= amount);
        }
    }

}

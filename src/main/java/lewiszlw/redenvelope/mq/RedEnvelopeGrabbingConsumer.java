package lewiszlw.redenvelope.mq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-17
 */
@Component
public class RedEnvelopeGrabbingConsumer implements InitializingBean {

    @Value("${rocketmq.namesrv.addr}")
    private String namesrvAddr;

    DefaultMQPushConsumer consumer;

    private String topic = "red-envelope-grabbing";
    private String consumerGroup = "red-envelope-grabbing-consumer-group";

    @Override
    public void afterPropertiesSet() throws Exception {
        consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.subscribe(topic, "*");
        consumer.registerMessageListener(new RedEnvelopeGrabbingMessageListener());
        consumer.start();
    }

    class RedEnvelopeGrabbingMessageListener implements MessageListenerConcurrently {
        @Override
        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
            for (MessageExt messageExt: list) {
                // TODO 消费消息
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
    }
}

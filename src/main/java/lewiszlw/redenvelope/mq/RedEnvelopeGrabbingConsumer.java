package lewiszlw.redenvelope.mq;

import lewiszlw.redenvelope.model.mq.GrabbingMessage;
import lewiszlw.redenvelope.service.RedEnvelopeService;
import lewiszlw.redenvelope.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-17
 */
@Component
@Slf4j
public class RedEnvelopeGrabbingConsumer implements InitializingBean {

    @Value("${rocketmq.namesrv.addr}")
    private String namesrvAddr;

    DefaultMQPushConsumer consumer;

    private String topic = "red-envelope-grabbing";
    private String consumerGroup = "red-envelope-grabbing-consumer-group";

    @Autowired
    private RedEnvelopeService redEnvelopeService;

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
            log.info("RedEnvelopeGrabbingConsumer 收到消息：{}", JsonUtils.toJson(list));
            try {
                List<GrabbingMessage> grabbingMessages = new ArrayList<>();
                for (MessageExt messageExt: list) {
                    grabbingMessages.add(JsonUtils.fromJson(messageExt.getBody(), GrabbingMessage.class));
                }
                redEnvelopeService.handleGrabbingMessage(grabbingMessages);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            } catch (Throwable t) {
                log.error("RedEnvelopeGrabbingConsumer 消费消息异常, 消息: {}", JsonUtils.toJson(list), t);
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        }
    }
}

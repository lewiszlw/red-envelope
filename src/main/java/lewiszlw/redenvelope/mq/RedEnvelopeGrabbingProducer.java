package lewiszlw.redenvelope.mq;

import lewiszlw.redenvelope.exception.MQProducerException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-05-17
 */
@Component
public class RedEnvelopeGrabbingProducer implements InitializingBean, DisposableBean {

    @Value("${rocketmq.namesrv.addr}")
    private String namesrvAddr;

    DefaultMQProducer defaultMQProducer;

    private final String topic = "red-envelope-grabbing";
    private final String producerGroup = "red-envelope-grabbing-producer-group";

    @Override
    public void afterPropertiesSet() throws Exception {
        defaultMQProducer = new DefaultMQProducer(producerGroup);
        defaultMQProducer.setNamesrvAddr(namesrvAddr);
        defaultMQProducer.start();
    }

    @Override
    public void destroy() throws Exception {
        defaultMQProducer.shutdown();
    }

    public SendResult send(String message) {
        try {
            Message msg = new Message(topic, message.getBytes(RemotingHelper.DEFAULT_CHARSET));
            return defaultMQProducer.send(msg);
        } catch (Exception e) {
            throw new MQProducerException("发送消息异常", e.getCause());
        }
    }
}

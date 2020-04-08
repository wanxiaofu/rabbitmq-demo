package rabbit.consumer.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rabbit.consumer.receiver.RabbitMessageReceiver;

/**
 * <p>
 * 消息接收确认机制配置
 * </p>
 *
 * @author wanxf
 * @date 2020/04/03
 */
@Configuration
public class MessageListenerConfig {

    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;

    @Autowired
    private DirectRabbitConfig directRabbitConfig;

    @Autowired
    private RabbitMessageReceiver rabbitMessageReceiver;

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer() {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(cachingConnectionFactory);
        simpleMessageListenerContainer.setConcurrentConsumers(5);
        simpleMessageListenerContainer.setMaxConcurrentConsumers(10);
        //手动ack
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //配置需要监听的队列
        simpleMessageListenerContainer.setQueues(directRabbitConfig.testDirectQueue());
        //配置监听器
        simpleMessageListenerContainer.setMessageListener(rabbitMessageReceiver);
        return simpleMessageListenerContainer;
    }

}

package rabbit.provider.config;

import org.springframework.amqp.core.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 *
 * </p>
 *
 * @author wanxf
 * @date 2020/04/02
 */
@Configuration
public class DirectRabbitConfig {

    /**
     * queue
     */
    private static final String TEST_DIRECT_QUEUE = "TestDirectQueue";

    /**
     * exchange
     */
    private static final String TEST_DIRECT_EXCHANGE = "TestDirectExchange";

    /**
     * routing-key
     */
    private static final String TEST_DIRECT_ROUTING = "TestDirectRouting";



    @Bean
    public Queue testDirectQueue() {
        return new Queue(TEST_DIRECT_QUEUE, true);
    }

    @Bean
    public DirectExchange testDirectExchange() {
        return new DirectExchange(TEST_DIRECT_EXCHANGE);
    }

    @Bean
    public Binding directBinding(){
        return BindingBuilder.bind(testDirectQueue()).to(testDirectExchange()).with(TEST_DIRECT_ROUTING);
    }

}

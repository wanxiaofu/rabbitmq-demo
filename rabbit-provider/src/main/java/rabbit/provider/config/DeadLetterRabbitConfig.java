package rabbit.provider.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 死信队列demo
 * <p>
 * RabbitMQ Queue中Arguments参数详解
 * x-message-ttl
 * <p>
 * 消息在队列中过期的时间 ms
 * <p>
 * 消息直接在列队头部，定期扫描然后移除
 * <p>
 * expiration
 * <p>
 * 消息在消费的时候判定是不是过期了然后在删除
 * <p>
 * x-expires
 * <p>
 * 队列在多长时间没有被使用后删除
 * <p>
 * x-max-length 
 * <p>
 * 加入queue中消息的条数。先进先出原则，超过10条后面的消息会顶替前面的消息。
 * <p>
 * x-max-length-bytes 
 * <p>
 * 加入queue中消息的容积。
 * <p>
 * x-dead-letter-routing-key 
 * <p>
 * x-dead-letter-exchange 
 * <p>
 * 'x-dead-letter-exchange': exchange, # 延迟结束后指向交换机（死信收容交换机）
 * <p>
 * 'x-dead-letter-routing-key': queue,  # 延迟结束后指向队列（死信收容队列），可直接设置queue name也可
 * <p>
 * x-max-priority 
 * <p>
 * 列队的优先级该参数会造成额外的CPU消耗。
 *
 * </p>
 *
 * @author wanxf
 * @date 2020/04/07
 */
@Configuration
public class DeadLetterRabbitConfig {

    /**
     * 普通任务队列
     *
     * @return Queue
     */
    @Bean
    public Queue taskQueue() {
        Map<String, Object> arguments = new HashMap<>();
        //消息过期时间
        arguments.put("x-message-ttl", 60 * 1000);
        //绑定死信交换机
        arguments.put("x-dead-letter-exchange", "dead-letter-directExchange");
        //死信队列也可能有多条，指定某一条
        arguments.put("x-dead-letter-routing-key", "dead-letter-routing-key");
        return new Queue("task-queue", true, false, false, arguments);
    }

    /**
     * 死信交换机
     *
     * @return deadLetterDirectExchange
     */
    @Bean
    public DirectExchange deadLetterDirectExchange() {
        return new DirectExchange("dead-letter-directExchange");
    }

    /**
     * 任务交换机
     *
     * @return taskDirectExchange
     */
    @Bean
    public DirectExchange taskDirectExchange() {
        return new DirectExchange("task-directExchange");
    }

    /**
     * 死信队列
     *
     * @return deadLetterQueue
     */
    @Bean
    public Queue deadLetterQueue() {
        return new Queue("dead-letter-queue");
    }

    /**
     * 任务队列绑定交换机
     *
     * @return taskBinding
     */
    @Bean
    public Binding taskBinding() {
        return BindingBuilder.bind(taskQueue()).to(taskDirectExchange()).with("task-routing-key");
    }

    /**
     * 死信队列绑定死信交换机
     *
     * @return deadLetterBinding
     */
    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterDirectExchange()).with("dead-letter-routing-key");
    }

}

package rabbit.consumer.receiver;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author wanxf
 * @date 2020/04/02
 */
@Component
public class RabbitMessageReceiver {

    @RabbitListener(queues = {"TestDirectQueue"})
    public void consumeMessage(Map<String,Object> map){
        System.out.println("map = " + map.toString());
    }
    @RabbitListener(queues = {"topic.man"})
    public void consumeMessage1(Map<String,Object> map){
        System.out.println("topic.man map = " + map.toString());
    }

    @RabbitListener(queues = {"topic.woman"})
    public void consumeMessage2(Map<String,Object> map){
        System.out.println("topic.* map = " + map.toString());
    }

    @RabbitListener(queues = {"fanout.A"})
    public void consumeMessage3(Map<String,Object> map){
        System.out.println("fanout.A map = " + map.toString());
    }

    @RabbitListener(queues = {"fanout.B"})
    public void consumeMessage4(Map<String,Object> map){
        System.out.println("fanout.B map = " + map.toString());
    }

    @RabbitListener(queues = {"fanout.C"})
    public void consumeMessage5(Map<String,Object> map){
        System.out.println("fanout.C map = " + map.toString());
    }
}

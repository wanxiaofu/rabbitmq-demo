package rabbit.consumer.receiver;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
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
public class RabbitMessageReceiver implements ChannelAwareMessageListener {

    /*@RabbitListener(queues = {"TestDirectQueue"})
    public void consumeMessage(Map<String, Object> map) {
        System.out.println("map = " + map.toString());
    }*/

    @RabbitListener(queues = {"topic.man"})
    public void consumeMessage1(Map<String, Object> map) {
        System.out.println("topic.man map = " + map.toString());
    }

    @RabbitListener(queues = {"topic.woman"})
    public void consumeMessage2(Map<String, Object> map) {
        System.out.println("topic.* map = " + map.toString());
    }

    @RabbitListener(queues = {"fanout.A"})
    public void consumeMessage3(Map<String, Object> map) {
        System.out.println("fanout.A map = " + map.toString());
    }

    @RabbitListener(queues = {"fanout.B"})
    public void consumeMessage4(Map<String, Object> map) {
        System.out.println("fanout.B map = " + map.toString());
    }

    @RabbitListener(queues = {"fanout.C"})
    public void consumeMessage5(Map<String, Object> map) {
        System.out.println("fanout.C map = " + map.toString());
    }

    /**
     * 自动ACK：消息一旦被接收，消费者自动发送ACK 弊端：不能确认消息是否被消费
     * 手动ACK：消息接收后，不会发送ACK，需要手动调用 可以确认消息被消费
     * <p>
     * 新手理解：这两ACK要怎么选择呢？这需要看消息的重要性：
     * 如果消息不太重要，丢失也没有影响，那么自动ACK会比较方便
     * 如果消息非常重要，不容丢失。那么最好在消费完成后手动ACK，否则接收消息后就自动ACK，RabbitMQ就会把消息从队列中删除。如果此时消费者宕机，那么消息就丢失了。
     * <p>
     * 手动确认 ， 这个比较关键，也是我们配置接收消息确认机制时，多数选择的模式。
     * 消费者收到消息后，手动调用basic.ack/basic.nack/basic.reject后，RabbitMQ收到这些消息后，才认为本次投递成功。
     * basic.ack用于肯定确认 
     * basic.nack用于否定确认（注意：这是AMQP 0-9-1的RabbitMQ扩展） 
     * basic.reject用于否定确认，但与basic.nack相比有一个限制:一次只能拒绝单条消息 
     * 消费者端以上的3个方法都表示消息已经被正确投递，但是basic.ack表示消息已经被正确处理，
     * 但是basic.nack,basic.reject表示没有被正确处理，但是RabbitMQ中仍然需要删除这条消息。 
     *
     * @param message
     * @param channel
     * @throws Exception
     */
    @RabbitListener(queues = {"TestDirectQueue"})
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            //因为传递消息的时候用的map传递,所以将Map从Message内取出需要做些处理
            String messageStr = message.toString();
            //可以点进Message里面看源码,单引号直接的数据就是我们的map消息数据
            String[] strings = messageStr.split("'");
            String s = strings[1].trim();
//            System.out.println("s = " + s);
            HashMap<String, String> msgMap = parseStr(s);
            String messageId = msgMap.get("messageId");
            String messageData = msgMap.get("messageData");
            String createTime = msgMap.get("createTime");
            System.out.println("messageId:" + messageId + "  messageData:" + messageData + "  createTime:" + createTime);
            //消息处理成功，手动ack，肯定确认
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            //出现异常，否定确认
            channel.basicReject(deliveryTag, false);
            e.printStackTrace();
        }


    }


    private HashMap<String, String> parseStr(String str) {
        String substring = str.substring(1, str.length() - 1);
        String[] strings = substring.split(",");
        HashMap<String, String> hashMap = new HashMap<>();
        for (String s : strings) {
            String key = s.split("=")[0].trim();
            String value = s.split("=")[1];
            hashMap.put(key, value);
        }
        return hashMap;
    }

    /*@RabbitListener(queues = "queue_demo4")
    public void process(Message message, @Headers Map<String, Object> headers, Channel channel) throws IOException {

        // 获取消息Id
        String messageId = message.getMessageProperties().getMessageId();
        String msg = new String(message.getBody(), "UTF-8");
        System.out.println("邮件消费者获取生产者消息msg:"+msg+",消息id"+messageId);

        JSONObject jsonObject = JSONObject.parseObject(msg);
        Integer timestamp = jsonObject.getInteger("timestamp");

        try {
            int result  = 1/timestamp;
            System.out.println("result"+result);
            //手动ack
            Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
            // 手动签收
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            //拒绝消费消息（丢失消息） 给死信队列,第三个参数 false 表示不会重回队列
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }

    }*/

    /*@RabbitListener(queues = {"task-queue"})
    public void process(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            System.out.println("deliveryTag = " + deliveryTag);
            System.out.println("queueName:" + message.getMessageProperties().getConsumerQueue());
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            String messageId = message.getMessageProperties().getMessageId();
            System.out.println("邮件消费者获取生产者消息msg:" + msg + ",消息id" + messageId);
            JSONObject object = JSONObject.parseObject(msg);
            Integer createTime = object.getInteger("createTime");
            System.out.println("createTime = " + createTime);
            //手动ack
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            channel.basicReject(deliveryTag, false);
            e.printStackTrace();
        }


    }*/

    @RabbitListener(queues = {"dead-letter-queue"})
    public void deadLetterProcess(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        System.out.println("deliveryTag = " + deliveryTag);
        try {
            System.out.println("queueName:" + message.getMessageProperties().getConsumerQueue());
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            String messageId = message.getMessageProperties().getMessageId();
            System.out.println("邮件消费者获取生产者消息msg:" + msg + ",消息id" + messageId);
            JSONObject object = JSONObject.parseObject(msg);
            String createTime = object.getString("createTime");
            System.out.println("createTime = " + createTime);
            //手动ack
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            e.printStackTrace();
        }


    }
}

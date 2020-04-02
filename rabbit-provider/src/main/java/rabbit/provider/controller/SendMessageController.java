package rabbit.provider.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author : wanxf
 * @date : 2020/4/2
 **/
@RestController
public class SendMessageController {

    /**
     * exchange
     */
    private static final String TEST_DIRECT_EXCHANGE = "TestDirectExchange";

    /**
     * routing-key
     */
    private static final String TEST_DIRECT_ROUTING = "TestDirectRouting";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendDirectMessage")
    public String sendDirectMessage() {
        String messageId = UUID.randomUUID().toString();
        String messageData = "test message,hello";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        HashMap<String, Object> map = new HashMap<>(3);
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        rabbitTemplate.convertAndSend(TEST_DIRECT_EXCHANGE, TEST_DIRECT_ROUTING, map);
        return "ok";
    }


}
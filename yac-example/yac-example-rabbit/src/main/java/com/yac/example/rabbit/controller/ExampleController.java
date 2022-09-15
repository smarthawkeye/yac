package com.yac.example.rabbit.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author xiaoya
 * @qq 278729535
 * @link https://github.com/an0701/ya-java
 * @date : 2022/5/13 16:03
 * @since 0.1.0
 */
@RestController
public class ExampleController {
    @Resource
    RabbitTemplate template;

    @RequestMapping("/test")
    public String test(){

        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "test message, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String,Object> map=new HashMap<>();
        map.put("messageId",messageId);
        map.put("messageData",messageData);
        map.put("createTime",createTime);
        //将消息携带绑定键值：payload 发送到交换机ex_yac
        template.convertAndSend("ex_yac", "payload", map);

        return "success";
    }
}

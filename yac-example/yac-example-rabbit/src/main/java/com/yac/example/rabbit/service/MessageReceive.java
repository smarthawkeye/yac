package com.yac.example.rabbit.service;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName MessageReceive
 * @Author xiaoya1
 * @Link https://github.com/an0701/ya-java
 * @Date 2022/8/24 18:07
 * @Version V0.1.0
 */
@Component
@RabbitListener(queues = "q_yac")//监听的队列名称 TestDirectQueue
public class MessageReceive {

    @RabbitHandler
    public void process(Map testMessage){
        System.out.println("testMessage = " + testMessage.toString());
    }
}

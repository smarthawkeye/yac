package com.yac.example.rabbit.sevice;

import io.github.smarthawkeye.core.mqtt.core.MqttTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @ClassName MqttSubcript
 * @Author xiaoya1
 * @Link https://github.com/an0701/ya-java
 * @Date 2022/8/24 18:00
 * @Version V0.1.0
 */
@Component
public class MqttSubcript {
    @Resource
    MqttTemplate template;

    @PostConstruct
    public void subcript(){
        template.subscribe("/yac_id/1",new MessageListener());
    }
}

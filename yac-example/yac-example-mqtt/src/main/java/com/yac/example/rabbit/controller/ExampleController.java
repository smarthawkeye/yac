package com.yac.example.rabbit.controller;

import io.github.smarthawkeye.core.mqtt.core.MqttTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

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
    MqttTemplate template;

    @RequestMapping("/test")
    public String test(){
        template.publish(0,false,"yac_id","test");

        return "success";
    }
}

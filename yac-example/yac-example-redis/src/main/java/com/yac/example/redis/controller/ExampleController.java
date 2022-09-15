package com.yac.example.redis.controller;


import io.github.smarthawkeye.core.redis.core.RedisService;
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
    RedisService redisService;

    @RequestMapping("/test")
    public String test(){
        redisService.set("yac","yac hello");
        String msg = (String) redisService.get("yac");

        System.out.println(msg);
        return "success";
    }
}

package com.yac.example.oss.controller;

import com.amazonaws.services.s3.model.Bucket;
import io.github.smarthawkeye.core.oss.core.OssTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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
    OssTemplate template;

    @RequestMapping("/test")
    public String test(){
        List<Bucket> allBuckets = template.getAllBuckets();
        System.out.println(allBuckets);
        return "success";
    }
}

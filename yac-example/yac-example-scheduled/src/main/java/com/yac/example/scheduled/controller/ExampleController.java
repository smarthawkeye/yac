package com.yac.example.scheduled.controller;


import io.github.smarthawkeye.core.job.core.ScheduledTaskService;
import io.github.smarthawkeye.core.job.core.YaScheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
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
    ScheduledTaskService taskService;

    @RequestMapping("/test")
    public String test() {
        List<YaScheduled> ls = new ArrayList<>();
        //执行一次
        YaScheduled scheduled = new YaScheduled();
        scheduled.setJobId(1);
        scheduled.setType(1);
        scheduled.setJobName("job1");
        scheduled.setTaskKey("scheduledTaskJob01");
        long timeMillis = System.currentTimeMillis();
        scheduled.setStartTime(new Date(timeMillis + 10000));
        //    ls.add(scheduled);
// 2.动态创建指定cron;

        YaScheduled scheduled2 = new YaScheduled();
        scheduled2.setJobId(2);
        scheduled2.setType(2);
        scheduled2.setJobName("job2");
        scheduled2.setTaskKey("scheduledTaskJob02");
        scheduled2.setCron("0/5 * * * * ?");
        //ls.add(scheduled2);
        // 3.指定间隔时间执行，间隔时间为前一次执行开始到下次任务开始时间;
        YaScheduled scheduled3 = new YaScheduled();
        scheduled3.setJobId(3);
        scheduled3.setType(3);
        scheduled3.setJobName("job3");
        scheduled3.setTaskKey("scheduledTaskJob02");
        scheduled3.setPeriod(5000L);
       //      ls.add(scheduled3);
        // 4.指定间隔时间执行一次任务，间隔时间为前一次任务完成到下一次开始时间
        YaScheduled scheduled4 = new YaScheduled();
        scheduled4.setJobId(4);
        scheduled4.setType(4);
        scheduled4.setJobName("job4");
        scheduled4.setTaskKey("scheduledTaskJob01");
        scheduled4.setPeriod(5000L);
        ls.add(scheduled4);

        taskService.initAllTask(ls);
        return "success";
    }
}

package io.github.smarthawkeye.core.job.config;

import io.github.smarthawkeye.core.job.core.ScheduledTaskService;
import io.github.smarthawkeye.core.job.core.ScheduledTaskServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @ClassName ScheduledTaskConfig
 * @Description 创建定时任务线程池,初始化任务Map
 * @Author xiaoya1
 * @Link https://github.com/an0701/ya-java
 * @Date 2022/8/25 17:28
 * @Version V0.1.0
 */
@Import(cn.hutool.extra.spring.SpringUtil.class)
@Slf4j
@Configuration
public class ScheduledTaskConfig {
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        log.info("创建定时任务调度线程池 start");
        threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(20);
        threadPoolTaskScheduler.setThreadNamePrefix("taskExecutor-");
        //用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean，这样这些异步任务的销毁就会先于Redis线程池的销毁。
        threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        //该方法用来设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住。
        threadPoolTaskScheduler.setAwaitTerminationSeconds(60);
        log.info("创建定时任务调度线程池 end");
        return threadPoolTaskScheduler;
    }
    @Bean
    @DependsOn("threadPoolTaskScheduler")
    public ScheduledTaskService scheduledTaskService(){
        return new ScheduledTaskServiceImpl(threadPoolTaskScheduler);
    }

}

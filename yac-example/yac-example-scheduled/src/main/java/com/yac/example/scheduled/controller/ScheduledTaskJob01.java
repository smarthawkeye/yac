package com.yac.example.scheduled.controller;


import io.github.smarthawkeye.core.job.core.ScheduledTaskJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ClassName ScheduledTaskJob01
 * @Description 测试类01
 * @Author lv617
 * @Date 2020/9/8 10:46
 * @Version 1.0
 */
@Slf4j
@Service
public class ScheduledTaskJob01 extends ScheduledTaskJob {
    private String param;
    @Override
    public void run() {
        // TODO 要处理的业务逻辑
        System.out.println("param = " + param);
        log.info("ScheduledTask => 01  run  当前线程名称 {} ", Thread.currentThread().getName());
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setParam(String param) {
        this.param = param;
    }
}

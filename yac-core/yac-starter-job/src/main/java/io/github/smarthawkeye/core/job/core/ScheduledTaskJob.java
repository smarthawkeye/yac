package io.github.smarthawkeye.core.job.core;


/**
 * @ClassName ScheduledTaskJob
 * @Description 创建调度任务公共父接口
 * @Author xiaoya1
 * @Link https://github.com/an0701/ya-java
 * @Date 2022/8/25 17:28
 * @Version V0.1.0
 */
public abstract class ScheduledTaskJob implements Runnable {
    public abstract void setParam(String param);
}

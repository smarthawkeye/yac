package io.github.smarthawkeye.core.job.core;

import java.util.List;

/**
 * @ClassName ScheduledTaskService
 * @Description 定时任务接口
 * @Author xiaoya1
 * @Link https://github.com/an0701/ya-java
 * @Date 2022/8/25 17:28
 * @Version V0.1.0
 */
public interface ScheduledTaskService {
    /**
     * 根据任务key 启动任务
     */
    Boolean start(YaScheduled scheduled);

    /**
     * 根据任务key 停止任务
     */
    Boolean stop(Integer jobId);

    /**
     * 根据任务key 重启任务
     */
    Boolean restart(YaScheduled scheduled);

    /**
     * 初始化  ==> 启动所有正常状态的任务
     */
    void initAllTask(List<YaScheduled> scheduleds);
}

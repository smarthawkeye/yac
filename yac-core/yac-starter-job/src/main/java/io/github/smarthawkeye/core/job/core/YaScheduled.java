package io.github.smarthawkeye.core.job.core;

import cn.hutool.core.date.DateTime;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @ClassName YaScheduled
 * @Description 描述
 * @Author xiaoya - https://github.com/an0701/ya-java
 * @Date 2022/8/25 17:33
 * @Version V0.1.0
 */
@Accessors(chain = true)
@Data
public class YaScheduled {
    /**
     * scheduled.jobId
     */
    private Integer jobId;

    /**
     * scheduled.task_key
     * 任务key值（使用bean名称）
     */
    private String taskKey;

    /**
     * scheduled.jobGroup
     * 任务分组
     */
    private String jobGroup;
    /**
     * scheduled.jobName
     * 任务名称
     */
    private String jobName;

    /**
     * scheduled.cron
     * 任务表达式
     */
    private String cron;
    /**
     *
     */
    private Long period;
    /**
     * scheduled.type
     * 定时执行类型(1.指定时间执行一次; 2.动态创建指定cron; 3.指定间隔时间执行，间隔时间为前一次执行开始到下次任务开始时间; 4.指定间隔时间执行一次任务，间隔时间为前一次任务完成到下一次开始时间)
     */
    private Integer type;
    /**
     * scheduled.status
     * 状态(0.禁用; 1.启用)
     */
    private Integer status;
    /**
     * scheduled.description
     * 描述
     */
    private String description;
    /**
     * scheduled.execTime
     * 任务执行时间
     */
    private Date startTime;
    /**
     * scheduled.create_time
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * scheduled.update_time
     * 更新时间
     */
    private LocalDateTime updateTime;
}
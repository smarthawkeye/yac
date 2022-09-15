package io.github.smarthawkeye.core.job.core;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName ScheduledTaskServiceImpl
 * @Description 定时任务实现
 * @Author xiaoya1
 * @Link https://github.com/an0701/ya-java
 * @Date 2022/8/25 17:28
 * @Version V0.1.0
 */
@Slf4j
@Service
public class ScheduledTaskServiceImpl implements ScheduledTaskService {
    /**
     * 定时任务线程池
     */
    ThreadPoolTaskScheduler threadPoolTaskScheduler;

    /**
     * 可重入锁
     */
    private ReentrantLock lock = new ReentrantLock();

    public ScheduledTaskServiceImpl(ThreadPoolTaskScheduler threadPoolTaskScheduler){
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
    }
    /**
     * 存放已经启动的任务map
     */
    private Map<Integer, ScheduledFuture> scheduledFutureMap = new ConcurrentHashMap<>();

    /**
     * 描述: 根据任务key 启动任务
     *
     * @param scheduled
     * @return java.lang.Boolean
     */
    @Override
    public Boolean start(YaScheduled scheduled) {
        log.info(">>>>>> 启动任务 {} 开始 >>>>>>", scheduled.getJobId());
        //添加锁放一个线程启动，防止多人启动多次
        lock.lock();
        try {
            log.info(">>>>>> 添加任务启动锁完毕");
            //校验是否已经启动
            if (this.isStart(scheduled.getJobId())) {
                log.info(">>>>>> 当前任务已经启动，无需重复启动！");
                return false;
            }
            //启动任务
            this.doStartTask(scheduled);
        } finally {// 释放锁
            lock.unlock();
            log.info(">>>>>> 释放任务启动锁完毕");
        }
        log.info(">>>>>> 启动任务 {} 结束 >>>>>>", scheduled.getJobId());
        return true;
    }

    /**
     * 描述: 查询定时任务配置参数
     *
     * @param jobId
     * @return com.yihaocard.main.module.scheduled.model.Scheduled
     */
//    private YaScheduled getByTaskKey(Integer jobId) {
//        ScheduledExample scheduledExample = new ScheduledExample();
//        scheduledExample.createCriteria()
//                .andStatusEqualTo(ScheduledStatus.DISABLE.getCode())
//                .andTaskKeyEqualTo(String.valueOf(jobId));
//        List<YaScheduled> scheduleds = scheduledMapper.selectByExample(scheduledExample);
//        if(scheduleds == null || scheduleds.size() < 1) {
//            return null;
//        }
//        return scheduleds.get(0);
//    }

    /**
     * 描述: 根据 key 停止任务
     *
     * @param jobId
     * @return java.lang.Boolean
     */
    @Override
    public Boolean stop(Integer jobId) {
        log.info(">>>>>> 进入停止任务 {}  >>>>>>", jobId);
        //当前任务实例是否存在
        boolean taskStartFlag = scheduledFutureMap.containsKey(jobId);
        log.info(">>>>>> 当前任务实例是否存在 {}", taskStartFlag);
        if (taskStartFlag) {
            //获取任务实例
            ScheduledFuture scheduledFuture = scheduledFutureMap.get(jobId);
            //关闭实例
            boolean cancel = scheduledFuture.cancel(true);
            log.info("cancel:{}", cancel);
            //删除关闭的任务实例
            scheduledFutureMap.remove(jobId);
        }
        log.info(">>>>>> 结束停止任务 {}  >>>>>>", jobId);
        return taskStartFlag;
    }

    /**
     * 描述: 根据任务key 重启任务
     *
     * @param scheduled
     * @return java.lang.Boolean
     */
    @Override
    public Boolean restart(YaScheduled scheduled) {
        log.info(">>>>>> 进入重启任务 {}  >>>>>>", scheduled.getJobId());
        //先停止
        this.stop(scheduled.getJobId());
        //再启动
        return this.start(scheduled);
    }

    /**
     * 初始化  ==> 启动所有正常状态的任务
     */
    @Override
    public void initAllTask(List<YaScheduled> scheduleds) {
        ScheduledExample scheduledExample = new ScheduledExample();
        scheduledExample.createCriteria()
                .andStatusEqualTo(ScheduledStatus.DISABLE.getCode());
        log.info("初始化  ==> 启动所有正常状态的任务开始 ！size={}", scheduleds == null ? 0 : scheduleds.size());
        if (scheduleds == null || scheduleds.size() < 1) {
            return;
        }
        for (YaScheduled scheduled : scheduleds) {
            //校验是否已经启动
            if (this.isStart(scheduled.getJobId())) {
                // 重启任务
                this.restart(scheduled);
            } else {
                // 启动任务
                this.doStartTask(scheduled);
            }
        }
        log.info("初始化  ==> 启动所有正常状态的任务结束 ！");
    }

    /**
     * 执行启动任务
     */
    private void doStartTask(YaScheduled scheduled) {
        if (scheduled == null) {
            return;
        }
        //任务key
        String taskKey = scheduled.getTaskKey();
        //定时表达式
        String taskCron = scheduled.getCron();
        //获取需要定时调度的接口
//        ScheduledTaskJob scheduledTaskJob = (ScheduledTaskJob) SpringContext.getBean(taskKey);
        ScheduledTaskJob scheduledTaskJob = (ScheduledTaskJob) SpringUtil.getBean(taskKey);
        log.info(">>>>>> 任务 [ {} ] ,cron={}", scheduled.getJobName(), taskCron);
        //定时执行类型
        // 1.指定时间执行一次;
        // 2.动态创建指定cron;
        // 3.指定间隔时间执行，间隔时间为前一次执行开始到下次任务开始时间;
        // 4.指定间隔时间执行一次任务，间隔时间为前一次任务完成到下一次开始时间
        ScheduledFuture scheduledFuture = null;
        switch(scheduled.getType()){
            case 1:
                scheduledFuture = threadPoolTaskScheduler.schedule(scheduledTaskJob,scheduled.getStartTime());
                break;
            case 2:
                scheduledFuture = threadPoolTaskScheduler.schedule(scheduledTaskJob, (TriggerContext triggerContext) -> new CronTrigger(taskCron).nextExecutionTime(triggerContext));
                break;
            case 3:
                scheduledFuture = threadPoolTaskScheduler.scheduleAtFixedRate(scheduledTaskJob,scheduled.getPeriod());
                break;
            case 4:
                scheduledFuture = threadPoolTaskScheduler.scheduleWithFixedDelay(scheduledTaskJob,scheduled.getPeriod());
                break;
        }
        if(null != scheduledFuture) {
            //将启动的任务放入 map
            scheduledFutureMap.put(scheduled.getJobId(), scheduledFuture);
        }
    }

    /**
     * 任务是否已经启动
     */
    private Boolean isStart(Integer jobId) {
        //校验是否已经启动
        if (scheduledFutureMap.containsKey(jobId)) {
            if (!scheduledFutureMap.get(jobId).isCancelled()) {
                return true;
            }
        }
        return false;
    }

}
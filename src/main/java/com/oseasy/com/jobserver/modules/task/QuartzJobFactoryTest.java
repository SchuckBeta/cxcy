package com.oseasy.com.jobserver.modules.task;

import org.apache.log4j.Logger;
import org.quartz.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 参考  http://blog.csdn.net/QXC1281/article/details/68924140
 */
public class QuartzJobFactoryTest implements Job {
    public final Logger log = Logger.getLogger(this.getClass());

    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("测试定时任务  11111111111111111111");
    }


    public static void cronSchedulerTest() throws SchedulerException {
        SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
        // 获取一个调度器
        Scheduler sched = schedFact.getScheduler();
        //		Class clazz = TaskScheduleJob.CONCURRENT_IS.equals(job.getIsConcurrent()) ? QuartzJobFactory.class : QuartzJobFactoryDisallowConcurrentExecution.class;
        JobDetail job = JobBuilder.newJob(QuartzJobFactoryTest.class).withIdentity("job1", "group1").build();
        // 每两秒执行
        CronTrigger trigger = TriggerBuilder.newTrigger().
                withIdentity("trigger1", "group1").
                withSchedule(CronScheduleBuilder.cronSchedule("/2 * * * * ?")
                ).build();
        sched.scheduleJob(job, trigger);
        // 调度启动
        sched.start();
    }

    public static void simpleSchedulerTest() throws SchedulerException {
// 获取一个调度工厂
        SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

        // 获取一个调度器
        Scheduler sched = schedFact.getScheduler();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        JobDetail job = JobBuilder.newJob(QuartzJobFactoryTest.class).withIdentity("job1", "group1").build();
        // 在当前时间15秒后运行
        Date startTime = DateBuilder.nextGivenSecondDate(new Date(), 15);
        // 创建一个SimpleTrigger实例，指定该Trigger在Scheduler中所属组及名称。
        // 接着设置调度的时间规则.当前时间15秒后运行，每10秒运行一次，共运行2次
        SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")
                .startAt(startTime).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).withRepeatCount(2))
                .build();
        sched.scheduleJob(job, trigger);
        // 调度启动
        sched.start();
    }

    //DailyTime调度器建造者
    public static void dailyTimeSchedulerTest() throws SchedulerException {
        SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
        // 获取一个调度器
        Scheduler sched = schedFact.getScheduler();
        JobDetail job = JobBuilder.newJob(QuartzJobFactoryTest.class).withIdentity("job1", "group1").build();
        // 每两秒执行
        DailyTimeIntervalTrigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").withSchedule(
                DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule().withInterval(2, DateBuilder.IntervalUnit.SECOND)
        ).build();
        sched.scheduleJob(job, trigger);
        // 调度启动
        sched.start();
    }


    public static void main(String[] args) throws SchedulerException {
//        QuartzJobFactoryTest.dailyTimeSchedulerTest();

        QuartzJobFactoryTest.simpleSchedulerTest();

    }
}

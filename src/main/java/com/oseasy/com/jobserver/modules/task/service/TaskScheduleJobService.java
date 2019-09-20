package com.oseasy.com.jobserver.modules.task.service;

import com.oseasy.com.jobserver.modules.task.QuartzJobFactory;
import com.oseasy.com.jobserver.modules.task.QuartzJobFactoryDisallowConcurrentExecution;
import com.oseasy.com.jobserver.modules.task.QuartzJobParamFactory;
import com.oseasy.com.jobserver.modules.task.dao.TaskScheduleJobDao;
import com.oseasy.com.jobserver.modules.task.entity.TaskScheduleJob;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Description: 计划任务管理
 */
@Service
@Transactional(readOnly = false)
public class TaskScheduleJobService {
    public final Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private TaskScheduleJobDao scheduleJobMapper;


    /**
     * 从数据库中取 区别于getAllJob
     *
     * @return
     */
    public List<TaskScheduleJob> getAllTask() {
        return scheduleJobMapper.getAll();
    }

    /**
     * 添加到数据库中 区别于addJob
     */
    public void addTask(TaskScheduleJob job) {
        job.setCreateTime(new Date());
        scheduleJobMapper.insertSelective(job);
    }

    /**
     * 从数据库中查询job
     */
    public TaskScheduleJob getTaskById(Long jobId) {
        return scheduleJobMapper.selectByPrimaryKey(jobId);
    }

    /**
     * 更改任务状态
     *
     * @throws SchedulerException
     */
    public void changeStatus(Long jobId, String cmd) throws SchedulerException {
        TaskScheduleJob job = getTaskById(jobId);
        if (job == null) {
            return;
        }
        if ("stop".equals(cmd)) {
            deleteJob(job);
            job.setJobStatus(TaskScheduleJob.STATUS_NOT_RUNNING);
        } else if ("start".equals(cmd)) {
            job.setJobStatus(TaskScheduleJob.STATUS_RUNNING);
            addJob(job);
        }
        scheduleJobMapper.updateByPrimaryKeySelective(job);
    }


    /**
     * 更改任务 cron表达式
     *
     * @throws SchedulerException
     */
    public void updateCron(Long jobId, String cron) throws SchedulerException {
        TaskScheduleJob job = getTaskById(jobId);
        if (job == null) {
            return;
        }
        job.setCronExpression(cron);
        if (TaskScheduleJob.STATUS_RUNNING.equals(job.getJobStatus())) {
            updateJobCron(job);
        }
        scheduleJobMapper.updateByPrimaryKeySelective(job);

    }

    /**
     * 添加任务
     *
     * @throws SchedulerException
     */
    public void addJob(TaskScheduleJob job) throws SchedulerException {
        if (job == null || !TaskScheduleJob.STATUS_RUNNING.equals(job.getJobStatus())) {
            return;
        }

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        log.debug(scheduler + ".......................................................................................add");
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
       //表达式触发器
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

        // 不存在，创建一个
        if (null == trigger) {
            Class clazz = TaskScheduleJob.CONCURRENT_IS.equals(job.getIsConcurrent()) ? QuartzJobFactory.class : QuartzJobFactoryDisallowConcurrentExecution.class;
            // 通过过JobDetail封装SimpleJob，同时指定Job在Scheduler中所属组及名称，
            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(job.getJobName(), job.getJobGroup()).build();

            jobDetail.getJobDataMap().put("scheduleJob", job);
            //Cron调度器建造者
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());//根据cron表达式建造
            // 创建一个SimpleTrigger实例，指定该Trigger在Scheduler中所属组及名称。
            trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
        } else {
            // Trigger已存在，那么更新相应的定时设置
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());

            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey) // withIdentity 根据name和默认的group(即"DEFAULT_GROUP")创建trigger的key
                    .withSchedule(scheduleBuilder)//调度器
                    .build();
            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        }
    }

    /**
    * 添加一次性审核人、任务
    *
    * @throws SchedulerException
    */
    public void addJobByOther(TaskScheduleJob job) throws SchedulerException {
        if (job == null ) {
           return;
        }
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        log.debug(scheduler + ".......................................................................................add");
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
      //表达式触发器
        SimpleTrigger trigger = (SimpleTrigger) scheduler.getTrigger(triggerKey);

       // 不存在，创建一个
        int afterMin= job.getAftertMin()>0?job.getAftertMin():0;

        if (null == trigger) {
            Class clazz = TaskScheduleJob.CONCURRENT_IS.equals(job.getIsConcurrent()) ? QuartzJobParamFactory.class : QuartzJobParamFactory.class;
           // 通过过JobDetail封装SimpleJob，同时指定Job在Scheduler中所属组及名称，
            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(job.getJobName(), job.getJobGroup()).build();
            // 在当前时间afterMin分后运行
            Date startTime = DateUtils.addMinutes(new Date(), afterMin) ;

                    //DateBuilder.nextGivenMinuteDate(new Date(), afterMin);
            jobDetail.getJobDataMap().put("scheduleJob", job);
            // 创建一个SimpleTrigger实例，指定该Trigger在Scheduler中所属组及名称。
            trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).startAt(startTime)
                   .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                   .build();
            scheduler.scheduleJob(jobDetail, trigger);
        } else {
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey) // withIdentity 根据name和默认的group(即"DEFAULT_GROUP")创建trigger的key
                   .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(afterMin*60).withRepeatCount(1))//调度器
                   .build();
           // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        }
   }

    @PostConstruct
    public void init() throws Exception {
// TODO 集群时有问题
     /*   Scheduler scheduler = schedulerFactoryBean.getScheduler();

        // 这里获取任务信息数据
        List<TaskScheduleJob> jobList = scheduleJobMapper.getAll();

        for (TaskScheduleJob job : jobList) {
            addJob(job);
        }*/
    }


    /**
     * 获取所有计划中的任务列表
     *
     * @return
     * @throws SchedulerException
     */
    public List<TaskScheduleJob> getAllJob() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        List<TaskScheduleJob> jobList = new ArrayList<TaskScheduleJob>();
        for (JobKey jobKey : jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggers) {
                TaskScheduleJob job = new TaskScheduleJob();
                job.setJobName(jobKey.getName());
                job.setJobGroup(jobKey.getGroup());
                job.setDescription("触发器:" + trigger.getKey());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                job.setJobStatus(triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    job.setCronExpression(cronExpression);
                }
                jobList.add(job);
            }
        }
        return jobList;
    }

    /**
     * 所有正在运行的job
     *
     * @return
     * @throws SchedulerException
     */
    public List<TaskScheduleJob> getRunningJob() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
        List<TaskScheduleJob> jobList = new ArrayList<TaskScheduleJob>(executingJobs.size());
        for (JobExecutionContext executingJob : executingJobs) {
            TaskScheduleJob job = new TaskScheduleJob();
            JobDetail jobDetail = executingJob.getJobDetail();
            JobKey jobKey = jobDetail.getKey();
            Trigger trigger = executingJob.getTrigger();
            job.setJobName(jobKey.getName());
            job.setJobGroup(jobKey.getGroup());
            job.setDescription("触发器:" + trigger.getKey());
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            job.setJobStatus(triggerState.name());
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                String cronExpression = cronTrigger.getCronExpression();
                job.setCronExpression(cronExpression);
            }
            jobList.add(job);
        }
        return jobList;
    }

    /**
     * 暂停一个job
     *
     * @param scheduleJob
     * @throws SchedulerException
     */
    public void pauseJob(TaskScheduleJob scheduleJob) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复一个job
     *
     * @param scheduleJob
     * @throws SchedulerException
     */
    public void resumeJob(TaskScheduleJob scheduleJob) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.resumeJob(jobKey);
    }

    /**
     * 删除一个job
     *
     * @param scheduleJob
     * @throws SchedulerException
     */
    public void deleteJob(TaskScheduleJob scheduleJob) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.deleteJob(jobKey);

    }

    /**
     * 删除一个job 同时删除数据库
     *
     * @throws SchedulerException
     */
    public void delete(Long jobId) throws SchedulerException {
        TaskScheduleJob job = getTaskById(jobId);
        if (job == null) {
            return;
        }
        pauseJob(job);////先停掉任务
        job.setJobStatus(TaskScheduleJob.STATUS_NOT_RUNNING);
        deleteJob(job);
        scheduleJobMapper.deleteByPrimaryKey(jobId);
    }


    /**
     * 立即执行job   只会运行一次
     *
     * @param scheduleJob
     * @throws SchedulerException
     */
    public void runAJobNow(TaskScheduleJob scheduleJob) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.triggerJob(jobKey);
    }

    /**
     * 更新job时间表达式
     *
     * @param scheduleJob
     * @throws SchedulerException
     */
    public void updateJobCron(TaskScheduleJob scheduleJob) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());

        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
      // 表达式调度构建器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
        //按新的cronExpression表达式构建一个新的trigger
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
     //按新的trigger重新设置job执行
        scheduler.rescheduleJob(triggerKey, trigger);
    }

    public void run()  {
        log.debug(" run.............service........................." + (new Date()));
    }
}

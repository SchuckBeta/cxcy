package com.oseasy.com.jobserver.modules.task;

import com.oseasy.com.jobserver.modules.task.entity.TaskScheduleJob;
import com.oseasy.com.jobserver.modules.task.support.spring.SpringUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class TaskUtils {
    public final static Logger log = Logger.getLogger(TaskUtils.class);

    /**
     * 通过反射调用scheduleJob中定义的方法
     *
     * @param scheduleJob
     */
    public static void invokMethod(TaskScheduleJob scheduleJob) {
        Object object = null;
        Class clazz = null;
        if (StringUtils.isNotBlank(scheduleJob.getSpringId())) {
            object = SpringUtils.getBean(scheduleJob.getSpringId());
//			OaNotifyDao oaNotifyDao = SpringContextHolder.getBean(OaNotifyDao.class);
        } else if (StringUtils.isNotBlank(scheduleJob.getBeanClass())) {
            try {
                clazz = Class.forName(scheduleJob.getBeanClass());
                object = clazz.newInstance();
            } catch (Exception e) {
                log.error(e);
            }
        }
        if (object == null) {
            log.error("任务名称 = [" + scheduleJob.getJobName() + "]---------------未启动成功，请检查是否配置正确！！！");
            return;
        }
        clazz = object.getClass();
        Method method = null;
        try {
            method = clazz.getDeclaredMethod(scheduleJob.getMethodName());
        } catch (NoSuchMethodException e) {
            log.error("任务名称 = [" + scheduleJob.getJobName() + "]---------------未启动成功，方法名设置错误！！！");
        } catch (SecurityException e) {
            log.error(e);
        }
        if (method != null) {
            try {
                method.invoke(object);
            } catch (IllegalAccessException e) {
                log.error(e);
            } catch (IllegalArgumentException e) {
                log.error(e);
            } catch (InvocationTargetException e) {
                log.error(e);
            }
        }
        log.info("任务名称 = [" + scheduleJob.getJobName() + "]----------启动成功");
    }

    /**
       * 通过反射调用scheduleJob中定义的方法
       *
       * @param scheduleJob
       */
      public static void invokParamMethod(TaskScheduleJob scheduleJob) {
          Object object = null;
          Class clazz = null;
          String param= "";
          if (StringUtils.isNotBlank(scheduleJob.getSpringId())) {
              object = SpringUtils.getBean(scheduleJob.getSpringId());
  //			OaNotifyDao oaNotifyDao = SpringContextHolder.getBean(OaNotifyDao.class);
          } else if (StringUtils.isNotBlank(scheduleJob.getBeanClass())) {
              try {
                  clazz = Class.forName(scheduleJob.getBeanClass());
                  object = clazz.newInstance();
              } catch (Exception e) {
                  log.error(e);
              }
          }
          if (object == null) {
              log.error("任务名称 = [" + scheduleJob.getJobName() + "]---------------未启动成功，请检查是否配置正确！！！");
              return;
          }
          clazz = object.getClass();
          Method method = null;
          try {
              method = clazz.getDeclaredMethod(scheduleJob.getMethodName(),TaskScheduleJob.class);
          } catch (NoSuchMethodException e) {
              log.error("任务名称 = [" + scheduleJob.getJobName() + "]---------------未启动成功，方法名设置错误！！！");
          } catch (SecurityException e) {
              log.error(e);
          }
          if (method != null) {
              try {
                  method.invoke(object,scheduleJob);
              } catch (IllegalAccessException e) {
                  log.error(e);
                  e.printStackTrace();
              } catch (IllegalArgumentException e) {
                  log.error(e);
                  e.printStackTrace();
              } catch (InvocationTargetException e) {
                  log.error(e);
                  e.printStackTrace();
              }
          }
          log.info("任务名称 = [" + scheduleJob.getJobName() + "]----------启动成功");
      }

}

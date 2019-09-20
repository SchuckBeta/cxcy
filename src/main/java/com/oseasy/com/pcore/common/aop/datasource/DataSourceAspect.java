package com.oseasy.com.pcore.common.aop.datasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 数据源切换
 */
@Aspect
@Component
public class DataSourceAspect /* implements Ordered */{
    private static Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);

    @Pointcut("execution (* com.oseasy.com.pcore.modules.sys.service.*.*(..))")
    public void readAspect() {
    }

    @Before("readAspect()")
    public void before(JoinPoint point) {
        Object target = point.getTarget();
        Class<?> clazz = target.getClass();
        Method[] methods = clazz.getMethods();
        DataSource annotation = null;
        for (Method method : methods) {
            if (point.getSignature().getName().equals(method.getName())) {
                annotation = method.getAnnotation(DataSource.class);
               if (annotation == null) {
                    annotation = point.getTarget().getClass().getAnnotation(DataSource.class);
                    if (annotation == null) {
                        return;
                    }
                }else  if(method.isAnnotationPresent(DataSource.class)){
                   DataSource data =method.getAnnotation(DataSource.class);
                   // 数据源放到当前线程中
                   DynamicDataSource.setDataSource(data.value());
                   logger.info("用户选择数据库库类型：" + DynamicDataSource.getDataSource());
                }else{
                   return;
               }

            }
        }
    }

    @After(value = "readAspect()")
    public void afterOpt() {
//        DynamicDataSource.clearDataSource();
//        logger.info("切回默认数据库:"+ DynamicDataSource.getDataSource());
    }


  /*  @Override
    public int getOrder() {
        return 2;
    }*/
}

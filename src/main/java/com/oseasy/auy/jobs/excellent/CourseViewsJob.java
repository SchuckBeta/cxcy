package com.oseasy.auy.jobs.excellent;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oseasy.com.jobserver.jobs.AbstractJobDetail;
import com.oseasy.pro.modules.interactive.service.SysViewsService;

@Service("courseViewsJob")
public class CourseViewsJob extends AbstractJobDetail{
	public final static Logger logger = Logger.getLogger(CourseViewsJob.class);
	@Autowired
	private SysViewsService sysViewsService;
	@Override
	public void doWork() {
		try {
			 sysViewsService.handleCourseViews();
		} catch (Exception e) {
			logger.error("处理名师讲堂浏览队列任务出错",e);
		}
	}
}

package com.oseasy.auy.jobs.excellent;

import com.oseasy.com.jobserver.jobs.AbstractJobDetail;
import com.oseasy.pro.modules.interactive.service.SysLikesService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("courseLikesJob")
public class CourseLikesJob extends AbstractJobDetail{
	public final static Logger logger = Logger.getLogger(CourseLikesJob.class);
	@Autowired
	private SysLikesService sysLikesService;

	@Override
	public void doWork() {
		try {
			 sysLikesService.handleCourseLikes();
		} catch (Exception e) {
			logger.error("处理名师讲堂评论队列任务出错",e);
		}
	}
}

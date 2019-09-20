package com.oseasy.auy.jobs.excellent;

import com.oseasy.com.jobserver.jobs.AbstractJobDetail;
import com.oseasy.pro.modules.interactive.service.SysCommentService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("courseCommentJob")
public class CourseCommentJob extends AbstractJobDetail{
	public final static Logger logger = Logger.getLogger(CourseCommentJob.class);
	@Autowired
	private SysCommentService sysCommentService;
	@Override
	public void doWork() {
		try {
			sysCommentService.handleCourseComment();
		} catch (Exception e) {
			logger.error("处理名师讲堂评论队列任务出错",e);
		}
	}
}

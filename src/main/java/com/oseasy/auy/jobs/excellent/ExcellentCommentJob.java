package com.oseasy.auy.jobs.excellent;

import com.oseasy.com.jobserver.jobs.AbstractJobDetail;
import com.oseasy.pro.modules.interactive.service.SysCommentService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("excellentCommentJob")
public class ExcellentCommentJob extends AbstractJobDetail{
	public final static Logger logger = Logger.getLogger(ExcellentCommentJob.class);
	@Autowired
	private SysCommentService sysCommentService;
	@Override
	public void doWork() {
		try {
			 sysCommentService.handleExcellentComment();
		} catch (Exception e) {
			logger.error("处理优秀展示评论队列任务出错",e);
		}
	}
}

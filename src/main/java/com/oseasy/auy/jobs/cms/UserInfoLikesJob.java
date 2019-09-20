package com.oseasy.auy.jobs.cms;

import com.oseasy.com.jobserver.jobs.AbstractJobDetail;
import com.oseasy.pro.modules.interactive.service.SysLikesService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userInfoLikesJob")
public class UserInfoLikesJob extends AbstractJobDetail{
	public final static Logger logger = Logger.getLogger(UserInfoLikesJob.class);
	
	@Autowired
	private SysLikesService sysLikesService;

	@Override
	public void doWork() {
		try {
			sysLikesService.handleUserInfoLikes();
		} catch (Exception e) {
			logger.error("处理导师、学生详情点赞队列任务出错",e);
		}
	}
}

package com.oseasy.auy.jobs.cms;

import com.oseasy.com.jobserver.jobs.AbstractJobDetail;
import com.oseasy.pro.modules.interactive.service.SysViewsService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userInfoViewsJob")
public class UserInfoViewsJob extends AbstractJobDetail{
	public final static Logger logger = Logger.getLogger(UserInfoViewsJob.class);
	
	@Autowired
	private SysViewsService sysViewsService ;
	@Override
	public void doWork() {
		try {
			 sysViewsService.handleUserInfoViews();
		} catch (Exception e) {
			logger.error("处理导师、学生详情浏览队列任务出错",e);
		}
	}
}

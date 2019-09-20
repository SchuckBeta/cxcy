package com.oseasy.auy.jobs.cms;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oseasy.auy.modules.cms.service.AuyCmsDeclareNotifyService;
import com.oseasy.com.jobserver.jobs.AbstractJobDetail;

@Service("cmsDeclareNotifyViewsJob")
public class CmsDeclareNotifyViewsJob extends AbstractJobDetail{
	public final static Logger logger = Logger.getLogger(CmsDeclareNotifyViewsJob.class);
	@Autowired
	private AuyCmsDeclareNotifyService auyCmsDeclareNotifyService ;
	@Override
	public void doWork() {
		try {
		    auyCmsDeclareNotifyService.handleViews();
		} catch (Exception e) {
			logger.error("处理申报通知浏览队列任务出错",e);
		}
	}
}

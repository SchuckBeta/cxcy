package com.oseasy.auy.jobs.excellent;

import com.oseasy.com.jobserver.jobs.AbstractJobDetail;
import com.oseasy.pro.modules.interactive.service.SysViewsService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("excellentViewsJob")
public class ExcellentViewsJob extends AbstractJobDetail{
	public final static Logger logger = Logger.getLogger(ExcellentViewsJob.class);

	@Autowired
	private SysViewsService sysViewsService;
	@Override
	public void doWork() {
		try {
			sysViewsService.handleExcellentViews();
		} catch (Exception e) {
			logger.error("处理优秀展示浏览队列任务出错",e);
		}
	}
}

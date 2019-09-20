package com.oseasy.pro.jobs.gcontesthots;

import com.oseasy.com.jobserver.jobs.AbstractJobDetail;
import com.oseasy.pro.modules.gcontest.service.GcontestHotsService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("gcontestHotsViewsJob")
public class GcontestHotsViewsJob extends AbstractJobDetail{
	public final static Logger logger = Logger.getLogger(GcontestHotsViewsJob.class);
	@Autowired
	private GcontestHotsService gcontestHotsService;
	@Override
	public void doWork() {
		try {
			gcontestHotsService.handleViews();
		} catch (Exception e) {
			logger.error("处理大赛热点浏览队列任务出错",e);
		}
	}
}

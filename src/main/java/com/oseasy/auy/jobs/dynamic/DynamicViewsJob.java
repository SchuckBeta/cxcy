package com.oseasy.auy.jobs.dynamic;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oseasy.auy.modules.cms.service.CmsOaNotifyService;
import com.oseasy.com.jobserver.jobs.AbstractJobDetail;

@Service("dynamicViewsJob")
public class DynamicViewsJob extends AbstractJobDetail {
    public final static Logger logger = Logger.getLogger(DynamicViewsJob.class);
    @Autowired
    private CmsOaNotifyService cmsOaNotifyService;

    @Override
    public void doWork() {
        try {
            cmsOaNotifyService.handleViews();
        } catch (Exception e) {
            logger.error("处理双创动态浏览队列任务出错", e);
        }
    }
}

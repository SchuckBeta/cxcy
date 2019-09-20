package com.oseasy.cms.jobs.cms;

import com.oseasy.cms.modules.cms.service.CmsArticleService;
import com.oseasy.com.jobserver.jobs.AbstractJobDetail;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("cmsPassDateJob")
public class cmsPassDateJob extends AbstractJobDetail {
    public final static Logger logger = Logger.getLogger(cmsPassDateJob.class);
    @Autowired
    private CmsArticleService cmsArticleService;
    public  void doWork(){
        try {
            cmsArticleService.changeStatusByDate();
        } catch (Exception e) {
            logger.error("处理过期时间错误:"+ExceptionUtil.getStackTrace(e));
        }
    }


}

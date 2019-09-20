package com.oseasy.cas.jobs.cas;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oseasy.cas.modules.cas.service.SysCasUserService;
import com.oseasy.cas.modules.cas.service.anzhi.SysCasAnzhiService;
import com.oseasy.com.jobserver.jobs.AbstractJobDetail;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

@Service("casUserJob")
public class CasUserJob extends AbstractJobDetail {
    public final static Logger logger = Logger.getLogger(CasUserJob.class);
    @Autowired
    private SysCasUserService sysCasUserService;

    public void doWork() {
        try {
            logger.info("CAS用户数据同步开始:" + new Date());
            sysCasUserService.casJobs();
            logger.info("CAS用户数据同步结束:" + new Date());
        } catch (Exception e) {
            logger.error("CAS用户数据同步出错:" + ExceptionUtil.getStackTrace(e));
        }
    }
}

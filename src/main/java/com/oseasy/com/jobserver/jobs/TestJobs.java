package com.oseasy.com.jobserver.jobs;

import com.oseasy.util.common.utils.DateUtil;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service("testJobs")
public class TestJobs   extends AbstractJobDetail {

    public final static Logger logger = Logger.getLogger(TestJobs.class);

    @Override
    public void doWork() {
        logger.info("测试定时任务执行时间:"+DateUtil.getDateTime());

    }

}

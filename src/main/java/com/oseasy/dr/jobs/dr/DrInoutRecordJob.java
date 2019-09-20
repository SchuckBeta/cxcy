package com.oseasy.dr.jobs.dr;

import com.oseasy.com.jobserver.jobs.AbstractJobDetail;
import com.oseasy.dr.modules.dr.manager.DrUtils;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service("drInoutRecordJob")
public class DrInoutRecordJob extends AbstractJobDetail {
    public final static Logger logger = Logger.getLogger(DrInoutRecordJob.class);

    public  void doWork(){
        try {
        	Date yesDay=DateUtil.addDays(new Date(), -1);
            DrUtils.disposeDrCardRecord(DateUtil.formatDate(yesDay, "yyyy-MM-dd"));
        } catch (Exception e) {
            logger.error("处理出入记录出错:"+ExceptionUtil.getStackTrace(e));
        }
    }


}

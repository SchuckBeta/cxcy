package com.oseasy.dr.jobs.dr;

import com.oseasy.com.jobserver.jobs.AbstractJobDetail;
import com.oseasy.dr.modules.dr.service.DRDeviceService;
import com.oseasy.dr.modules.dr.service.DrCardRecordService;
import com.oseasy.dr.modules.dr.vo.DrCardRecordParam;
import com.oseasy.util.common.utils.DateUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service("drCardRecordStateJob")
public class DrCardRecordStateJob extends AbstractJobDetail {
    public final static Logger logger = Logger.getLogger(DrCardRecordStateJob.class);
    @Autowired
    private DRDeviceService deviceService;
    @Autowired
    private DrCardRecordService drCardRecordService;

    public  void doWork(){
        logger.info("开始getNewRecords   date:" + DateUtil.getDateTime());
        try {
           for (int i = 0; i < 5; i++) {
                /**
                 * deviceService.getRecordsByIndex();
                 */
                deviceService.getNewRecords();
                Thread.sleep(1000L * 60 * 1);
           }
           DrCardRecordParam cardRecordParam = new DrCardRecordParam();
           cardRecordParam.setMinPcTime(DateUtil.getCurDateYMD000(DateUtil.addDays(new Date(), -1)));
           cardRecordParam.setMaxPcTime(DateUtil.getCurDateYMD999(new Date()));
           drCardRecordService.ajaxSynch(cardRecordParam);
        } catch (Exception e) {
            logger.error("获取出入记录失败",e);
        }
    }
}

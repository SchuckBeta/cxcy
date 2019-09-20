package com.oseasy.pw.jobs.pw;

import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.jobserver.jobs.AbstractJobDetail;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.service.SysService;
import com.oseasy.pw.modules.pw.service.PwEnterService;
import com.oseasy.pw.modules.pw.vo.PwEnterEvo;
import com.oseasy.pw.modules.pw.vo.SvalPw;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 入驻-已到期.
 *
 * @author chenhao
 */
@Service("pwEnterExitJob")
public class PwEnterExitJob extends AbstractJobDetail {
    public final static Logger logger = Logger.getLogger(PwEnterExitJob.class);
    @Autowired
    private PwEnterService pwEnterService;
    @Autowired
    private SysService sysService;

    @Override
    public void doWork() {
        try {
            logger.info("处理开始(退孵):pwEnterExitJob");
            if (SvalPw.getEnterExitAuto()) {
                ApiTstatus<PwEnterEvo> exitVoRstatus = pwEnterService.enterAllByExit(sysService.getDbCurDateYmdHms());
                logger.info("处理完成、处理结果请查看日志文件:" + exitVoRstatus.getDatas().getLogFile());
            }
            logger.info("处理完成:pwEnterExitJob");
        } catch (Exception e) {
            logger.error("处理入驻-退孵队列任务出错", e);
        }
    }
}
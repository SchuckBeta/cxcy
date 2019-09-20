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
 * 入驻-即将到期.
 *
 * @author chenhao
 */
@Service("pwEnterExpireJob")
public class PwEnterExpireJob extends AbstractJobDetail {
	public final static Logger logger = Logger.getLogger(PwEnterExpireJob.class);
	@Autowired
	private PwEnterService pwEnterService;
	@Autowired
	private SysService sysService ;

	@Override
	public void doWork() {
		try {
			logger.info("处理开始(即将到期):pwEnterExpireJob");
			if (SvalPw.getEnterExpireAuto()) {
				ApiTstatus<PwEnterEvo> expireVoRstatus = pwEnterService.enterAllByExpire(sysService.getDbCurDateYmdHms());
				logger.info("处理完成、处理结果请查看日志文件:" + expireVoRstatus.getDatas().getLogFile());
			}
			logger.info("处理完成:pwEnterExpireJob");
		} catch (Exception e) {
			logger.error("处理入驻-即将到期队列任务出错", e);
		}
	}
}
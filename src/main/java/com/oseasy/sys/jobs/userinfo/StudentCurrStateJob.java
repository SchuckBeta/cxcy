package com.oseasy.sys.jobs.userinfo;

import com.oseasy.com.jobserver.jobs.AbstractJobDetail;
import com.oseasy.sys.modules.sys.service.StudentExpansionService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("studentCurrStateJob")
public class StudentCurrStateJob extends AbstractJobDetail{
	public final static Logger logger = Logger.getLogger(StudentCurrStateJob.class);
	@Autowired
	private StudentExpansionService studentExpansionService;
	
	@Override
	public void doWork() {
		try {
			studentExpansionService.updateCurrState();
		} catch (Exception e) {
			logger.error("处理学生现状任务出错",e);
		}
	}
}

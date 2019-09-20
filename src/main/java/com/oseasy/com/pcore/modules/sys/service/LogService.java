/**
 * Copyright &copy; 2012-2013 <a href="httparamMap://github.com/oseasy/initiate">JeeSite</a> All rights reserved.
 */
package com.oseasy.com.pcore.modules.sys.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.dao.LogDao;
import com.oseasy.com.pcore.modules.sys.entity.Log;
import com.oseasy.util.common.utils.DateUtil;

/**
 * 日志Service

 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class LogService extends CrudService<LogDao, Log> {

	public Page<Log> findPage(Page<Log> page, Log log) {
		
		// 设置默认时间范围，默认当前月
		if (log.getBeginDate() == null) {
			log.setBeginDate(DateUtil.setDays(DateUtil.parseDate(DateUtil.getDate()), 1));
		}
		if (log.getEndDate() == null) {
			log.setEndDate(DateUtil.addMonths(log.getBeginDate(), 1));
		}
		
		return super.findPage(page, log);
		
	}
	
}

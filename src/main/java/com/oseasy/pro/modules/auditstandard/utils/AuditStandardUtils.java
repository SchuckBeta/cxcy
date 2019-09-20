package com.oseasy.pro.modules.auditstandard.utils;

import java.util.List;

import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.pro.modules.auditstandard.entity.AuditStandard;
import com.oseasy.pro.modules.auditstandard.service.AuditStandardService;

public class AuditStandardUtils {
	private static AuditStandardService auditStandardService = SpringContextHolder.getBean(AuditStandardService.class);
	public static List<AuditStandard> getAuditStandardList() {
	    return auditStandardService.findList(new AuditStandard());
	}
}

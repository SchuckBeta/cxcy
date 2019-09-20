package com.oseasy.pro.modules.promodel.utils;

import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.sys.modules.sys.entity.SysNumberRule;
import com.oseasy.sys.modules.sys.service.SysNumberRuleService;
import com.oseasy.sys.modules.sys.utils.NumRuleUtils;

public class ProActYwUtils {
	public static SysNumberRuleService sysNumberRuleService = SpringContextHolder.getBean(SysNumberRuleService.class);
    public static String isNumberRule(String key) {
    	SysNumberRule sysNumberRule= sysNumberRuleService.getRuleByAppType(key,"");
    	//请设置编号规则
    	String rule="请设置编号规则";
    	if(sysNumberRule!=null){
    		//规则名称
    		rule=sysNumberRule.getName();
    	}
    	return rule;
    }
    public static String getNumberRule(String key) {
    	SysNumberRule sysNumberRule= sysNumberRuleService.getRuleByAppType(key);
    	String rule="";
    	if(sysNumberRule!=null){
    		rule=NumRuleUtils.getNumberText(key, "","", true);
    		//rule=StringUtil.abbr(rule,8);
    	}
    	return rule;
    }
}

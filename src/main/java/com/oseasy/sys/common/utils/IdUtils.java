/**
 * Copyright (c) 2005-2012 springside.org.cn
 */
package com.oseasy.sys.common.utils;

import org.apache.log4j.Logger;

import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.sys.modules.seq.service.SequenceService;
import com.oseasy.util.common.utils.DateUtil;
/**
 * 封装根据指定规则生成编号
 * eg：ZZ  20170309 111111  00012  AA
 */
public class IdUtils {
	static SequenceService sequenceService=(SequenceService) SpringContextHolder.getBean(SequenceService.class);

	public final static Logger logger = Logger.getLogger(IdUtils.class);
    public static String getProjectNumberByDb() {
		String num="";
    	try {
			num=sequenceService.getProjectNextSequence();
		} catch (Exception e) {
			num=DateUtil.getDate("yyyyMMddHHmmss");
		}
    	return num;
    }
    public static String getGContestNumberByDb() {
		String num="";
			try {
				num=sequenceService.getGcontestNextSequence();
			} catch (Exception e) {
				num=DateUtil.getDate("yyyyMMddHHmmss");

			}
		return num;
	}

    public static String getTeamNumberByDb() {
		String num="";
        	try {
				num=sequenceService.getTeamNextSequence();
    		} catch (Exception e) {
				num=DateUtil.getDate("yyyyMMddHHmmss");
    		}

    	return num;
    }
    public static String getDictNumberByDb() {
    	return sequenceService.getDictNextSequence();
    }
}

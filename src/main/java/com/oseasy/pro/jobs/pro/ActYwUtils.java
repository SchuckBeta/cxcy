package com.oseasy.pro.jobs.pro;

import com.oseasy.act.common.config.ActSval;
import com.oseasy.act.modules.actyw.entity.ActYwYear;
import com.oseasy.com.rediserver.common.utils.JedisUtils;

/**
 * @author: QM
 * @date: 2019/4/21 20:41
 * @description: 流程年份
 */
public class ActYwUtils {

	/**
	 * 获取流程年份信息
	 * @param actywId actywId
	 * @param tenantId 租户Id
	 * @return ActYwYear
	 */
	public static ActYwYear findActYwYear(String actywId,String tenantId){
		String cacheKey = ActSval.ck.cks(ActSval.ActEmskey.ACTYWYEAR, tenantId)+actywId;
		ActYwYear ayy = null;
		if (JedisUtils.hasKey(cacheKey)){
			ayy = (ActYwYear)JedisUtils.getObject(cacheKey);
			if (ayy != null){
				JedisUtils.setObject(cacheKey,ayy);
			}
		}
		return ayy;
	}
}

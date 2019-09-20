package com.oseasy.pro.modules.promodel.utils;

import com.oseasy.act.common.config.ActSval;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.com.rediserver.common.utils.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author: QM
 * @date: 2019/4/27 13:45
 * @description:
 */
@Component
public class ActYwUtils {


	private static ActYwService actYwService;

	@Autowired
	public void setActYwService(ActYwService actYwService){
		ActYwUtils.actYwService = actYwService;
	}


	public static ActYw getActYwOfCache(String tenantId,String id){
		String cacheKey = ActSval.ck.cks(ActSval.ActEmskey.ACTYW, tenantId)+id;
		ActYw actYw = null;
		if (JedisUtils.hasKey(cacheKey)){
			actYw = (ActYw)JedisUtils.getObject(cacheKey);
		}
		if (actYw == null){
			actYw = actYwService.get(id);
			if (actYw != null){
				JedisUtils.setObject(cacheKey,actYw);
			}
		}
		return actYw;
	}
}

/**
 *
 */
package com.oseasy.pro.modules.proprojectmd.utils;

import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.pro.modules.proprojectmd.entity.ProModelMd;
import com.oseasy.pro.modules.proprojectmd.service.ProModelMdService;

/**
 * 内容管理工具类


 */
public class ProModelMdUtils {
	private static ProModelMdService proModelMdService = SpringContextHolder.getBean(ProModelMdService.class);
	/**根据模板类型获取大赛结果html代码*/
	public static ProModelMd getProModelMdById(String  modelId) {
		ProModelMd pmd=new ProModelMd();
		if (modelId!=null) {
			pmd=proModelMdService.getByProModelId(modelId);
			if (pmd!=null) {
				return pmd;
			}else{
				return new ProModelMd();
			}
		}
		return pmd;
	}

}
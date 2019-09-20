/**
 *
 */
package com.oseasy.auy.modules.cms.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.pro.modules.interactive.entity.SysViews;

/**
 * 通知通告Service
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class CmsOaNotifyService extends OaNotifyService {
	/**
	 *双创动态浏览量队列的处理
	 * @return 处理的数据条数
	 */
	@Transactional(readOnly = false)
	public int handleViews() {
		Map<String,Integer> map=new HashMap<String,Integer>();//需要更新浏览量数量的map
		int tatol=10000;
		int count=0;
		Integer up=null;
		SysViews sv=(SysViews)CacheUtils.rpop(CacheUtils.DYNAMIC_VIEWS_QUEUE);
		while(count<tatol&&sv!=null) {
			count++;//增加了一条数据
			up=map.get(sv.getForeignId());
			if (up==null) {
				map.put(sv.getForeignId(), 1);
			}else{
				map.put(sv.getForeignId(), up+1);
			}
			if (count<tatol) {
				sv=(SysViews)CacheUtils.rpop(CacheUtils.DYNAMIC_VIEWS_QUEUE);
			}
		}
		if (count>0) {//有数据需要处理
			dao.updateViews(map);
		}
		return count;
	}
}
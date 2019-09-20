package com.oseasy.auy.modules.cms.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.oseasy.cms.modules.cms.service.CmsDeclareNotifyService;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.pro.modules.interactive.entity.SysViews;

/**
 * declareService.
 * @author 奔波儿灞
 * @version 2018-01-24
 */
@Service
@Transactional(readOnly = true)
public class AuyCmsDeclareNotifyService extends CmsDeclareNotifyService{
    /**
     *浏览量队列的处理
     * @return 处理的数据条数
     */
    @Transactional(readOnly = false)
    public int handleViews() {
        Map<String,Integer> map= Maps.newHashMap();//需要更新浏览量数量的map
        int tatol=10000;
        int count=0;
        Integer up=null;
        SysViews sv=(SysViews)CacheUtils.rpop(CacheUtils.DECLARENOTIFY_VIEWS_QUEUE);
        while(count<tatol&&sv!=null) {
            count++;//增加了一条数据
            up=map.get(sv.getForeignId());
            if (up==null) {
                map.put(sv.getForeignId(), 1);
            }else{
                map.put(sv.getForeignId(), up+1);
            }
            if (count<tatol) {
                sv=(SysViews)CacheUtils.rpop(CacheUtils.DECLARENOTIFY_VIEWS_QUEUE);
            }
        }
        if (count>0) {//有数据需要处理
            dao.updateViews(map);
        }
        return count;
    }
}
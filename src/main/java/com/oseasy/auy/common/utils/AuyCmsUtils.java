/**
 *
 */
package com.oseasy.auy.common.utils;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.auy.modules.cms.service.CmsOaNotifyService;
import com.oseasy.cms.common.config.SysCacheKeys;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.utils.OaUtils;
import com.oseasy.com.mqserver.modules.oa.vo.OaNotifySendType;
import com.oseasy.com.mqserver.modules.oa.vo.OaNotifyType;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.rediserver.common.utils.CacheUtils;

/**
 * 内容管理工具类
 */
public class AuyCmsUtils {
	private static CmsOaNotifyService cmsOaNotifyService = SpringContextHolder.getBean(CmsOaNotifyService.class);

    /**
     * 根据类型获得系统广播通知
     */
    public static List<OaNotify> getOaNotifys(SysCacheKeys key, OaNotify oaNotify) {
        List<OaNotify> oaNotifys = Lists.newArrayList();
//      禁用缓存
//      List<OaNotify> oaNotifys = (List<OaNotify>)CacheUtils.get(OaUtils.OA_CACHE, key.getKey());
        if ((oaNotifys ==  null) || (oaNotifys.isEmpty())) {
            Page<OaNotify> page = new Page<OaNotify>(1, OaUtils.OA_CACHE_NOTIFYS_MAXNUM);
            page = cmsOaNotifyService.findPage(page, oaNotify);
            oaNotifys = page.getList();
            CacheUtils.put(OaUtils.OA_CACHE, key.getKey(), oaNotifys);
        }
        return oaNotifys;
    }

    /**
     * 获得系统广播通知-双创通知
     */
    public static List<OaNotify> getOaNotifysTZ() {
        OaNotify oaNotify = new OaNotify();
        oaNotify.setType(OaNotifyType.SCTZ.getVal());
        oaNotify.setSendType(OaNotifySendType.DIS_DIRECRIONAL.getVal());
        oaNotify.setStatus("1");
        return getOaNotifys(SysCacheKeys.OA_CACHE_NOTIFYS_TZ, oaNotify);
    }

    /**
     * 获得系统广播通知-省市动态
     */
    public static List<OaNotify> getOaNotifysSS() {
        OaNotify oaNotify = new OaNotify();
        oaNotify.setType(OaNotifyType.SSDT.getVal());
        oaNotify.setSendType(OaNotifySendType.DIS_DIRECRIONAL.getVal());
        oaNotify.setStatus("1");
        return getOaNotifys(SysCacheKeys.OA_CACHE_NOTIFYS_SS, oaNotify);
    }

    /**
     * 获得系统广播通知-双创动态
     */
    public static List<OaNotify> getOaNotifysSC() {
        OaNotify oaNotify = new OaNotify();
        oaNotify.setType(OaNotifyType.SCDT.getVal());
        oaNotify.setSendType(OaNotifySendType.DIS_DIRECRIONAL.getVal());
        oaNotify.setStatus("1");
        return getOaNotifys(SysCacheKeys.OA_CACHE_NOTIFYS_SC, oaNotify);
    }
}
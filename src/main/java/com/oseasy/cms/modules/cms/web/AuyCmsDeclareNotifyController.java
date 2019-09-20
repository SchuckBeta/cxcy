package com.oseasy.cms.modules.cms.web;

import javax.servlet.http.HttpServletRequest;

import com.oseasy.pro.modules.interactive.service.SysViewsService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oseasy.cms.common.config.CmsSval;
import com.oseasy.cms.common.config.CmsSval.CmsEmskey;
import com.oseasy.cms.modules.cms.entity.CmsDeclareNotify;
import com.oseasy.com.mqserver.modules.oa.utils.OaUtils;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.util.common.utils.StringUtil;

/**
 * declareController.
 * @author 奔波儿灞
 * @version 2018-01-24
 */
@Controller
public class AuyCmsDeclareNotifyController extends BaseController {
	public static final String FRONT_URL = CoreSval.getConfig("sysFrontIp")+CoreSval.getConfig("frontPath");

    @RequestMapping(value = "${frontPath}/cms/cmsDeclareNotify/view")
    public String view(CmsDeclareNotify cmsDeclareNotify, Model model,HttpServletRequest request) {
        if (StringUtil.isNotEmpty(cmsDeclareNotify.getContent())) {
            cmsDeclareNotify.setContent(OaUtils.convertFront(cmsDeclareNotify.getContent()));
            cmsDeclareNotify.setContent(StringEscapeUtils.unescapeHtml4(cmsDeclareNotify.getContent()));
        }
        if (StringUtil.isNotEmpty(cmsDeclareNotify.getId())) {
            SysViewsService.updateViews(cmsDeclareNotify.getId(), request,CacheUtils.DECLARENOTIFY_VIEWS_QUEUE);
        }
        model.addAttribute("cmsDeclareNotify", cmsDeclareNotify);
        return CmsSval.path.vms(CmsEmskey.CMS.k()) + "cmsDeclareNotifyView";
    }
}
/**
 *
 */
package com.oseasy.cms.modules.cms.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.pro.modules.interactive.service.SysViewsService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oseasy.auy.modules.cms.service.CmsOaNotifyService;
import com.oseasy.com.fileserver.common.utils.FtpUtil;
import com.oseasy.com.mqserver.common.config.MqsrSval;
import com.oseasy.com.mqserver.common.config.MqsrSval.MqsrEmskey;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyKeywordService;
import com.oseasy.com.mqserver.modules.oa.utils.OaUtils;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.util.common.utils.StringUtil;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 通知通告Controller
 *
 * @version 2014-05-16
 */
@Controller
@RequestMapping(value = "${frontPath}/oa/oaNotify")
public class FrontCmsOaNotifyController extends BaseController {
    @Autowired
    OaNotifyKeywordService oaNotifyKeywordService;
    @Autowired
    CmsOaNotifyService cmsOaNotifyService;
    @Autowired
    private OaNotifyService oaNotifyService;
    @ModelAttribute
    public OaNotify get(@RequestParam(required = false) String id) {
        OaNotify entity = null;
        if (StringUtil.isNotBlank(id)) {
            entity = oaNotifyService.get(id);
        }
        if (entity == null) {
            entity = new OaNotify();
        }
        return entity;
    }
    /**
     * 首页我的通知列表
     */
    @RequestMapping(value = "indexMyNoticeList")
    public String indexMyNoticeList(OaNotify oaNotify, HttpServletRequest request, HttpServletResponse response, Model model) {
        User currUser = UserUtils.getUser();
        if(UserUtils.checkToLogin(currUser)){
            return CoreSval.LOGIN_REDIRECT;
        }
        //logger.info("curre========="+currUser.getId());
        if (currUser != null && currUser.getId() != null) {
            oaNotify.setUserId(String.valueOf(currUser.getId()));
        } else {
            oaNotify.setType("error");
        }

        oaNotify.setIsSelf(true);
        Page<OaNotify> page = cmsOaNotifyService.findAllRecord(new Page<OaNotify>(request, response), oaNotify);
        model.addAttribute("page", page);
        return MqsrSval.path.vms(MqsrEmskey.OA.k()) + "indexOaNotifyList";
    }

    /**
     * 查看双创动态、双创通知、省市动态
     */
    @RequestMapping(value = "viewDynamic")
    public String viewDynamic(OaNotify oaNotify, Model model, HttpServletRequest request) {
        if (oaNotify != null) {
            if (StringUtil.isEmpty(oaNotify.getViews())) {
                oaNotify.setViews("0");
            }
            if (StringUtil.isNotEmpty(oaNotify.getId())) {
                oaNotify.setKeywords(oaNotifyKeywordService.findListByEsid(oaNotify.getId()));
            }
            if (StringUtil.isNotEmpty(oaNotify.getContent())) {
                oaNotify.setContent(OaUtils.convertFront(oaNotify.getContent()));
                oaNotify.setContent(StringEscapeUtils.unescapeHtml4(oaNotify.getContent()));
            }
            if (oaNotify != null && StringUtil.isNotEmpty(oaNotify.getContent())) {
                oaNotify.setContent(oaNotify.getContent().replaceAll(FtpUtil.FTP_MARKER, FtpUtil.FTP_HTTPURL));
            }
            if (StringUtil.isNotEmpty(oaNotify.getId())) {
                model.addAttribute("more", cmsOaNotifyService.getMore(oaNotify.getType(), oaNotify.getId(), oaNotify.getKeywords()));
            }
            if (StringUtil.isNotEmpty(oaNotify.getId())) {
                SysViewsService.updateViews(oaNotify.getId(), request, CacheUtils.DYNAMIC_VIEWS_QUEUE);
            }
        }
        return MqsrSval.path.vms(MqsrEmskey.OA.k()) + "dynamicView";
    }
}
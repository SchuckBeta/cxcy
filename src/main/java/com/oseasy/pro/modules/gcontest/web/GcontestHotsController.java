package com.oseasy.pro.modules.gcontest.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.pro.modules.interactive.service.SysViewsService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.fileserver.common.utils.FtpUtil;
import com.oseasy.com.mqserver.modules.oa.utils.OaUtils;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.config.ProSval.ProEmskey;
import com.oseasy.pro.modules.gcontest.entity.GcontestHots;
import com.oseasy.pro.modules.gcontest.service.GcontestHotsKeywordService;
import com.oseasy.pro.modules.gcontest.service.GcontestHotsService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 大赛热点Controller.
 * @author 9527
 * @version 2017-07-12
 */
@Controller
public class GcontestHotsController extends BaseController {
	public static final String FRONT_URL = CoreSval.getConfig("sysFrontIp")+CoreSval.getConfig("frontPath");
	@Autowired
	private GcontestHotsKeywordService gcontestHotsKeywordService;
	@Autowired
	private GcontestHotsService gcontestHotsService;

	@ModelAttribute
	public GcontestHots get(@RequestParam(required=false) String id) {
		GcontestHots entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = gcontestHotsService.get(id);
		}
		if (entity == null) {
			entity = new GcontestHots();
		}
		return entity;
	}

	@RequiresPermissions("gcontesthots:gcontestHots:view")
	@RequestMapping(value = {"${adminPath}/gcontesthots/list"})
	public String list(GcontestHots gcontestHots, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<GcontestHots> page = gcontestHotsService.findPage(new Page<GcontestHots>(request, response), gcontestHots);
		model.addAttribute("page", page);
		return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "gcontestHotsList";
	}

	@RequiresPermissions("gcontesthots:gcontestHots:view")
	@RequestMapping(value = "${adminPath}/gcontesthots/form")
	public String form(GcontestHots gcontestHots, Model model) {
		if (StringUtil.isNotEmpty(gcontestHots.getId())) {
			gcontestHots.setKeywords(gcontestHotsKeywordService.findListByEsid(gcontestHots.getId()));
		}
		if (StringUtil.isNotEmpty(gcontestHots.getContent())) {
			gcontestHots.setContent(StringEscapeUtils.unescapeHtml4(gcontestHots.getContent()));
			gcontestHots.setContent(gcontestHots.getContent().replaceAll(FtpUtil.FTP_MARKER,FtpUtil.FTP_HTTPURL));
		}
		model.addAttribute("gcontestHots", gcontestHots);
		model.addAttribute("front_url", FRONT_URL);
		return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "gcontestHotsForm";
	}

	@RequiresPermissions("gcontesthots:gcontestHots:edit")
	@RequestMapping(value = "${adminPath}/gcontesthots/save")
	public String save(GcontestHots gcontestHots, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, gcontestHots)) {
			return form(gcontestHots, model);
		}
		gcontestHotsService.save(gcontestHots);
		addMessage(redirectAttributes, "保存大赛热点成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/gcontesthots/list?repage";
	}

	@RequiresPermissions("gcontesthots:gcontestHots:edit")
	@RequestMapping(value = "${adminPath}/gcontesthots/delete")
	public String delete(GcontestHots gcontestHots, RedirectAttributes redirectAttributes) {
		gcontestHotsService.delete(gcontestHots);
		addMessage(redirectAttributes, "删除大赛热点成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/gcontesthots/list?repage";
	}
	@RequestMapping(value = "${frontPath}/gcontesthots/view")
	public String view(GcontestHots gcontestHots, Model model,HttpServletRequest request) {
		if (StringUtil.isEmpty(gcontestHots.getId())) {
			gcontestHots=gcontestHotsService.getTop();
		}
		if (StringUtil.isNotEmpty(gcontestHots.getId())) {
			gcontestHots.setKeywords(gcontestHotsKeywordService.findListByEsid(gcontestHots.getId()));
		}
		if (StringUtil.isNotEmpty(gcontestHots.getContent())) {
            gcontestHots.setContent(OaUtils.convertFront(gcontestHots.getContent()));
			gcontestHots.setContent(StringEscapeUtils.unescapeHtml4(gcontestHots.getContent()));
			gcontestHots.setContent(gcontestHots.getContent().replaceAll(FtpUtil.FTP_MARKER,FtpUtil.FTP_HTTPURL));
		}
		if (StringUtil.isNotEmpty(gcontestHots.getId())) {
			model.addAttribute("more",gcontestHotsService.getMore(gcontestHots.getId(),gcontestHots.getKeywords()));
		}
		if (StringUtil.isNotEmpty(gcontestHots.getId())) {
			SysViewsService.updateViews(gcontestHots.getId(), request,CacheUtils.GCONTESTHOTS_VIEWS_QUEUE);
		}
		model.addAttribute("gcontestHots", gcontestHots);
		return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "gcontestHotsView";
	}
	@RequestMapping(value = "${frontPath}/gcontesthots/preView")
	public String preView(GcontestHots gcontestHots, Model model,HttpServletRequest request) {
		if (StringUtil.isNotEmpty(gcontestHots.getContent())) {
            gcontestHots.setContent(OaUtils.convertFront(gcontestHots.getContent()));
			gcontestHots.setContent(StringEscapeUtils.unescapeHtml4(gcontestHots.getContent()));
			gcontestHots.setContent(gcontestHots.getContent().replaceAll(FtpUtil.FTP_MARKER,FtpUtil.FTP_HTTPURL));
		}
		model.addAttribute("more",gcontestHotsService.getMore(null,gcontestHots.getKeywords()));
		model.addAttribute("gcontestHots", gcontestHots);
		return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "gcontestHotsView";
	}
}
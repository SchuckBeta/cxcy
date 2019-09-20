package com.oseasy.cms.modules.cms.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;
import com.oseasy.cms.common.config.CmsSval;
import com.oseasy.cms.common.config.CmsSval.CmsEmskey;
import com.oseasy.cms.modules.cms.entity.CmsIndex;
import com.oseasy.cms.modules.cms.entity.CmsLink;
import com.oseasy.cms.modules.cms.entity.CmsSiteconfig;
import com.oseasy.cms.modules.cms.entity.Site;
import com.oseasy.cms.modules.cms.service.CmsIndexService;
import com.oseasy.cms.modules.cms.service.CmsSiteconfigService;
import com.oseasy.cms.modules.cms.service.SiteService;
import com.oseasy.cms.modules.cms.utils.CmsUtils;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.service.DictService;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

/**
 * 首页管理Controller.
 * @author zy
 * @version 2018-09-03
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/cmsIndex")
public class CmsIndexController extends BaseController {

	@Autowired
	private CmsIndexService cmsIndexService;
	@Autowired
	private SiteService siteService;
	@Autowired
	private CmsSiteconfigService cmsSiteconfigService;
	@Autowired
	private DictService dictService;

	@ModelAttribute
	public CmsIndex get(@RequestParam(required=false) String id) {
		CmsIndex entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = cmsIndexService.get(id);
		}
		if (entity == null){
			entity = new CmsIndex();
		}
		return entity;
	}

	@RequiresPermissions("cms:cmsIndex:view")
	@RequestMapping(value = {"list", ""})
	public String list(CmsIndex cmsIndex, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsIndex> page = cmsIndexService.findPage(new Page<CmsIndex>(request, response), cmsIndex);
		model.addAttribute(Page.PAGE, page);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "cmsIndexList";
	}

	@RequestMapping(value = "form")
	public String form(CmsIndex cmsIndex, Model model) {
		model.addAttribute("cmsIndex", cmsIndex);
		//获取"通知通告类型"字典对象
		Dict dict = dictService.findDictByValue("oa_notify_type");
		model.addAttribute("dict", dict);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "cmsIndexForm";
	}

	@RequiresPermissions("cms:cmsIndex:edit")
	@RequestMapping(value = "save")
	public String save(CmsIndex cmsIndex, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cmsIndex)){
			return form(cmsIndex, model);
		}
		cmsIndexService.save(cmsIndex);
		addMessage(redirectAttributes, "保存首页管理成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/cms/cmsIndex/?repage";
	}

	@RequiresPermissions("cms:cmsIndex:edit")
	@RequestMapping(value = "delete")
	public String delete(CmsIndex cmsIndex, RedirectAttributes redirectAttributes) {
		cmsIndexService.delete(cmsIndex);
		addMessage(redirectAttributes, "删除首页管理成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/cms/cmsIndex/?repage";
	}

	@RequestMapping(value = "cmsIndexList")
	@ResponseBody
	public ApiResult cmsIndexList(CmsIndex cmsIndex, HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String,Object> map= Maps.newHashMap();
			Page<CmsIndex> page = cmsIndexService.findPage(new Page<CmsIndex>(request, response), cmsIndex);
			map.put("page",page);
			List<CmsIndex> cmsIndexList=cmsIndexService.findShowIndexList();
			List<CmsIndex> hiddenCmsIndexList=cmsIndexService.findhiddenList();
			map.put("cmsIndexList",cmsIndexList);
			map.put("hiddenCmsIndexList",hiddenCmsIndexList);
			return ApiResult.success(map);
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequestMapping(value = "cmsIndexSave" , method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiResult cmsIndexSave(@RequestBody CmsIndex cmsIndex, HttpServletRequest request, HttpServletResponse response) {
		ApiResult  result = new ApiResult();
		try {
			cmsIndexService.saveIndex(cmsIndex);
			result.setStatus(ApiConst.STATUS_SUCCESS);
			result.setCode(ApiConst.CODE_REQUEST_SUCCESS);
			return result;
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequestMapping(value = "cmsIndexSaveSort")
	@ResponseBody
	public ApiResult cmsLinkSaveSort(String ids ,String sorts, HttpServletRequest request) {
		ApiResult  result = new ApiResult();
		try {
			cmsIndexService.cmsIndexSaveSort(ids,sorts);
			result.setStatus(ApiConst.STATUS_SUCCESS);
			result.setCode(ApiConst.CODE_REQUEST_SUCCESS);
			return result;
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequestMapping(value = "cmsLinkList")
	@ResponseBody
	public ApiResult cmsLinkList() {
		try {
//			CmsLink cmsLink=new CmsLink();
			Site site=siteService.getAutoSite();
			List<CmsLink> cmsLinkList= CmsUtils.getCmsLinks();
			Map<String,Object> map= Maps.newHashMap();
			CmsSiteconfig cmsSiteconfig=cmsSiteconfigService.getBySiteIdAndType(site.getId(),"linkType");
			for (CmsLink cmsLink : cmsLinkList) {
				String linkType = "2";
				if (cmsSiteconfig != null) {
					linkType = cmsSiteconfig.getLinkType();
				}
				cmsLink.setSitetype(linkType);
			}
			map.put("cmsLinkList", cmsLinkList);
			return ApiResult.success(map);
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

}
package com.oseasy.cms.modules.cms.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;
import com.oseasy.cms.common.config.CmsSval;
import com.oseasy.cms.common.config.CmsSval.CmsEmskey;
import com.oseasy.cms.modules.cms.entity.CmsLink;
import com.oseasy.cms.modules.cms.entity.CmsSiteconfig;
import com.oseasy.cms.modules.cms.entity.Site;
import com.oseasy.cms.modules.cms.service.CmsLinkService;
import com.oseasy.cms.modules.cms.service.CmsSiteconfigService;
import com.oseasy.cms.modules.cms.service.SiteService;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.fileserver.common.vsftp.VsftpUtils;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

/**
 * 友情链接Controller.
 * @author zy
 * @version 2018-08-30
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/cmsLink")
public class CmsLinkController extends BaseController {

	@Autowired
	private CmsLinkService cmsLinkService;
	@Autowired
	private SiteService siteService;
	@Autowired
	private CmsSiteconfigService cmsSiteconfigService;

	@ModelAttribute
	public CmsLink get(@RequestParam(required=false) String id) {
		CmsLink entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = cmsLinkService.get(id);
		}
		if (entity == null){
			entity = new CmsLink();
		}
		return entity;
	}

	@RequiresPermissions("cms:cmsLink:view")
	@RequestMapping(value = {"list", ""})
	public String list(CmsLink cmsLink, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsLink> page = cmsLinkService.findPage(new Page<CmsLink>(request, response), cmsLink);
		model.addAttribute(Page.PAGE, page);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "cmsLinkList";
	}

	@RequestMapping(value = {"cmsLinkList", ""})
	public String cmsLinklist(HttpServletRequest request, HttpServletResponse response) {
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "cmsLinkList";
	}

	/**
	 * @deprecated
	 */
	@RequiresPermissions("cms:cmsLink:view")
	@RequestMapping(value = "form")
	public String form(CmsLink cmsLink, Model model) {
		model.addAttribute("cmsLink", cmsLink);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "cmsLinkForm";
	}

	@RequiresPermissions("cms:cmsLink:edit")
	@RequestMapping(value = "save")
	public String save(CmsLink cmsLink, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cmsLink)){
			return form(cmsLink, model);
		}
		cmsLinkService.save(cmsLink);
		addMessage(redirectAttributes, "保存友情链接成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/cms/cmsLink/?repage";
	}

	@RequiresPermissions("cms:cmsLink:edit")
	@RequestMapping(value = "delete")
	public String delete(CmsLink cmsLink, RedirectAttributes redirectAttributes) {
		cmsLinkService.delete(cmsLink);
		addMessage(redirectAttributes, "删除友情链接成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/cms/cmsLink/?repage";
	}

	@RequestMapping(value = "linkList")
	@ResponseBody
	public ApiResult linkList(CmsLink cmsLink, HttpServletRequest request, HttpServletResponse response) {
		try {
			Page<CmsLink> page = cmsLinkService.findPage(new Page<CmsLink>(request, response), cmsLink);
			return ApiResult.success(page);
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequestMapping(value="checkLinkName", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Boolean checkLinkName(CmsLink cmsLink){
		return cmsLinkService.checkLinkName(cmsLink);
	}

	@RequestMapping(value = "cmsLinkType")
	@ResponseBody
	public ApiResult cmsLinkType() {
		try {
			Map<String,Object> map= Maps.newHashMap();
			Site site=siteService.getAutoSite();
			CmsSiteconfig cmsSiteconfig=cmsSiteconfigService.getBySiteIdAndType(site.getId(),"linkType");
			if(cmsSiteconfig!=null){
				map.put("linkType",cmsSiteconfig.getLinkType());
			}else{
				map.put("linkType","1");
			}
			return ApiResult.success(map);
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequestMapping(value = "cmsLinkSave")
	@ResponseBody
	public ApiResult cmsLinkSave(CmsLink cmsLink, HttpServletRequest request) {
		ApiResult  result = new ApiResult();
		try {
			if(StringUtil.isNotEmpty(cmsLink.getLogo())){
				String newUrl= null;
				newUrl = VsftpUtils.moveFile(cmsLink.getLogo());
				if(StringUtil.isNotEmpty(newUrl)){
					cmsLink.setLogo(newUrl);
				}
			}
			cmsLinkService.save(cmsLink);
			result.setStatus(ApiConst.STATUS_SUCCESS);
			result.setCode(ApiConst.CODE_REQUEST_SUCCESS);
			return result;
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequestMapping(value = "cmsLinkDel")
	@ResponseBody
	public ApiResult cmsLinkDel(CmsLink cmsLink, HttpServletRequest request) {
		ApiResult  result = new ApiResult();
		try {
			cmsLinkService.delete(cmsLink);
			result.setStatus(ApiConst.STATUS_SUCCESS);
			result.setCode(ApiConst.CODE_REQUEST_SUCCESS);
			return result;
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequestMapping(value = "cmsLinkPlDel")
	@ResponseBody
	public ApiResult cmsLinkPlDel(String ids, HttpServletRequest request) {
		ApiResult  result = new ApiResult();
		try {
			cmsLinkService.deletePl(ids);
			result.setStatus(ApiConst.STATUS_SUCCESS);
			result.setCode(ApiConst.CODE_REQUEST_SUCCESS);
			return result;
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequestMapping(value = "cmsLinkSaveSort")
	@ResponseBody
	public ApiResult cmsLinkSaveSort(String ids,String sorts, HttpServletRequest request) {
		ApiResult  result = new ApiResult();
		try {
			cmsLinkService.cmsLinkSaveSort(ids,sorts);
//			cmsLinkService.cmsLinkSaveSort(cmsList);
			result.setStatus(ApiConst.STATUS_SUCCESS);
			result.setCode(ApiConst.CODE_REQUEST_SUCCESS);
			return result;
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}
}
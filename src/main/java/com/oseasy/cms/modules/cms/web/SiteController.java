package com.oseasy.cms.modules.cms.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.cms.common.config.CmsSval;
import com.oseasy.cms.common.config.CmsSval.CmsEmskey;
import com.oseasy.cms.modules.cms.entity.Site;
import com.oseasy.cms.modules.cms.service.SiteService;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.util.common.utils.CookieUtils;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 站点Controller


 */
@Controller
@RequestMapping(value = "${adminPath}/cms/site")
public class SiteController extends BaseController {

	@Autowired
	private SiteService siteService;

	@ModelAttribute
	public Site get(@RequestParam(required=false) String id) {
		if (StringUtil.isNotBlank(id)) {
			return siteService.get(id);
		}else{
			return new Site();
		}
	}

	@RequiresPermissions("cms:site:view")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "siteList";
	}

	@RequestMapping(value = "siteList")
	@ResponseBody
	public JSONObject siteList( Site site,HttpServletRequest request ,HttpServletResponse response) {
		JSONObject  js= new JSONObject();
		try {
			Page<Site> page = siteService.findPage(new Page<Site>(request, response), site);
			if(StringUtil.checkNotEmpty(page.getList())&&page.getList().size()>0){
				JSONObject list=new JSONObject();
				list.put("pageNo",page.getPageNo());
				list.put("pageSize",page.getPageSize());
				list.put("count",page.getCount());
				JSONArray jsList=new JSONArray();
				for(int i=0;i<page.getList().size();i++){
					JSONObject index=new JSONObject();
					Site siteIndex=page.getList().get(i);
					index.put("id",siteIndex.getId());
					index.put("name",siteIndex.getName());
					index.put("url",siteIndex.getUrl());
					index.put("description",siteIndex.getDescription());
					index.put("copyright",siteIndex.getCopyright());
					index.put("isCurrentsite",siteIndex.getIsCurrentsite());
					jsList.add(index);
				}
				list.put("list",jsList);
				js.put("page", list);
			}

			js.put("ret", ApiConst.STATUS_SUCCESS_STR);
		}catch (Exception e){
			js.put("ret", ApiConst.STATUS_FAIL_STR);
			js.put("msg", "删除失败,出现了未知的错误，请重试或者联系管理员");
		}
		return js;
	}


	@RequestMapping(value = "siteSave")
	@ResponseBody
	public JSONObject siteSave( HttpServletRequest request) {
		JSONObject  js= new JSONObject();

		String description=request.getParameter("description");
		String copyright=request.getParameter("copyright");
		String name=request.getParameter("name");
		String url=request.getParameter("url");
		try {
			Site site =new Site();
			site.setDescription(description);
			site.setCopyright(copyright);
			site.setName(name);
			site.setUrl(url);
			boolean isFirst=siteService.getFirstSite();
			if(isFirst){
				//设置默认站点
				site.setIsCurrentsite("1");
			}
			siteService.saveNewId(site,request);
			js.put("ret", ApiConst.STATUS_SUCCESS_STR);
		}catch (Exception e){
			logger.info(e.toString());
			js.put("ret", ApiConst.STATUS_FAIL_STR);
			js.put("msg", "保存失败,出现了未知的错误，请重试或者联系管理员");
		}
		return js;
	}

	@RequestMapping(value = "siteEdit")
	@ResponseBody
	public JSONObject siteEdit(  Site site) {
		JSONObject  js= new JSONObject();
		try {
			siteService.updateSite(site);
			js.put("ret", ApiConst.STATUS_SUCCESS_STR);
		}catch (Exception e){
			js.put("ret", ApiConst.STATUS_FAIL_STR);
			js.put("msg", "保存失败,出现了未知的错误，请重试或者联系管理员");
		}
		return js;
	}

	@RequestMapping(value = "siteChange")
	@ResponseBody
	public JSONObject siteChange(Site site) {
		JSONObject  js= new JSONObject();
		try {
			siteService.siteChange(site);
			js.put("ret", ApiConst.STATUS_SUCCESS_STR);
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			js.put("ret", ApiConst.STATUS_FAIL_STR);
			js.put("msg", "保存失败,出现了未知的错误，请重试或者联系管理员");
		}
		return js;
	}


	@RequestMapping(value = "siteDel")
	@ResponseBody
	public JSONObject siteDel( Site site,HttpServletRequest request) {
		JSONObject  js= new JSONObject();
		try {
			siteService.siteDelete(site);
			js.put("ret", ApiConst.STATUS_SUCCESS_STR);
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			js.put("ret", ApiConst.STATUS_FAIL_STR);
			js.put("msg", "保存失败,出现了未知的错误，请重试或者联系管理员");
		}
		return js;
	}




	/**
	 * 选择站点
	 * @param id
	 * @return
	 */
	@RequiresPermissions("cms:site:select")
	@RequestMapping(value = "select")
	public String select(String id, boolean flag, HttpServletResponse response) {
		if (id!=null) {
			CoreUtils.putCache("siteId", id);
			// 保存到Cookie中，下次登录后自动切换到该站点
			CookieUtils.setCookie(response, "siteId", id);
		}
		if (flag) {
			return CoreSval.REDIRECT + adminPath;
		}
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "siteSelect";
	}
}

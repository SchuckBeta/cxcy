package com.oseasy.cms.modules.cms.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
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

import com.oseasy.cms.common.config.CmsSval;
import com.oseasy.cms.common.config.CmsSval.CmsEmskey;
import com.oseasy.cms.modules.cms.entity.CmsSiteconfig;
import com.oseasy.cms.modules.cms.entity.Site;
import com.oseasy.cms.modules.cms.service.CmsSiteconfigService;
import com.oseasy.cms.modules.cms.service.SiteService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 网站配置Controller.
 * @author zy
 * @version 2018-08-27
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/cmsSiteconfig")
public class CmsSiteconfigController extends BaseController {

	@Autowired
	private CmsSiteconfigService cmsSiteconfigService;

	@Autowired
	private SiteService siteService;

	@ModelAttribute
	public CmsSiteconfig get(@RequestParam(required=false) String id) {
		CmsSiteconfig entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = cmsSiteconfigService.get(id);
		}
		if (entity == null){
			entity = new CmsSiteconfig();
		}
		return entity;
	}

	/**
     * @deprecated
	 */
	@RequiresPermissions("cms:cmsSiteconfig:view")
	@RequestMapping(value = {"list", ""})
	public String list(CmsSiteconfig cmsSiteconfig, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsSiteconfig> page = cmsSiteconfigService.findPage(new Page<CmsSiteconfig>(request, response), cmsSiteconfig);
		model.addAttribute(Page.PAGE, page);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "cmsSiteconfigList";
	}

	@RequiresPermissions("cms:cmsSiteconfig:view")
	@RequestMapping(value = "form")
	public String form(CmsSiteconfig cmsSiteconfig, Model model) {
		model.addAttribute("cmsSiteconfig", cmsSiteconfig);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "cmsSiteconfigForm";
	}

	@RequestMapping(value = "cmsSiteconfigForm")
	public String cmsSiteconfigForm(CmsSiteconfig cmsSiteconfig, Model model) {
		model.addAttribute("cmsSiteconfig", cmsSiteconfig);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "cmsSiteconfigForm";
	}

	@RequiresPermissions("cms:cmsSiteconfig:edit")
	@RequestMapping(value = "save")
	public String save(CmsSiteconfig cmsSiteconfig, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cmsSiteconfig)){
			return form(cmsSiteconfig, model);
		}
		cmsSiteconfigService.save(cmsSiteconfig);
		addMessage(redirectAttributes, "保存网站配置成功");
		return "redirect:"+CoreSval.getAdminPath()+"/cms/cmsSiteconfig/?repage";
	}

	@RequiresPermissions("cms:cmsSiteconfig:edit")
	@RequestMapping(value = "delete")
	public String delete(CmsSiteconfig cmsSiteconfig, RedirectAttributes redirectAttributes) {
		cmsSiteconfigService.delete(cmsSiteconfig);
		addMessage(redirectAttributes, "删除网站配置成功");
		return "redirect:"+CoreSval.getAdminPath()+"/cms/cmsSiteconfig/?repage";
	}

	//@RequestMapping(value = "siteConfigSave")
	@RequestMapping(value="siteConfigSave" , method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public JSONObject siteConfigSave(@RequestBody CmsSiteconfig cmsSiteconfig,HttpServletRequest request) {
		JSONObject  js= new JSONObject();
		try {
			if(StringUtil.isEmpty(cmsSiteconfig.getId())){
				cmsSiteconfigService.saveNewId(cmsSiteconfig);
			}else{
				cmsSiteconfigService.update(cmsSiteconfig);
			}
			js.put("ret", "1");
		}catch (Exception e){
			logger.error(e.toString());
			js.put("ret", "0");
			js.put("msg", "保存失败,出现了未知的错误，请重试或者联系管理员");
		}
		return js;
	}


	//@RequestMapping(value = "siteConfigSave")
	@RequestMapping(value="siteConfigLinkSave" , method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public JSONObject siteConfigLinkSave(String linkType) {
		JSONObject  js= new JSONObject();
		try {
			Site site=siteService.getAutoSite();
			CmsSiteconfig cmsSiteconfig=cmsSiteconfigService.getBySiteIdAndType(site.getId(),"linkType");
			if(cmsSiteconfig!=null){
				cmsSiteconfig.setLinkType(linkType);
				cmsSiteconfigService.updateLinkType(cmsSiteconfig);
			}else{
				cmsSiteconfig=new CmsSiteconfig ();
				cmsSiteconfig.setType("linkType");
				cmsSiteconfig.setSiteId(site.getId());
				cmsSiteconfig.setLinkType(linkType);
				cmsSiteconfigService.save(cmsSiteconfig);
			}
			js.put("ret", "1");
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			js.put("ret", "0");
			js.put("msg", "保存失败,出现了未知的错误，请重试或者联系管理员");
		}
		return js;
	}


	@RequestMapping(value = "siteConfigForm")
	@ResponseBody
	public JSONObject siteConfigForm() {
		JSONObject  js= new JSONObject();
		Site site=siteService.getAutoSite();
		if(site==null){
			js.put("ret", "0");
			js.put("msg", "没有默认站点，请在站点管理页面选择站点");
			return js;
		}
		try {
			List<CmsSiteconfig> cmsSiteconfigList=cmsSiteconfigService.getBySiteId(site.getId());
			JSONObject  jsData= new JSONObject();
			jsData.put("siteName",site.getName());
			jsData.put("siteId",site.getId());
			if(StringUtil.checkNotEmpty(cmsSiteconfigList)){
				CmsSiteconfig cmsSiteconfig=cmsSiteconfigList.get(0);
				jsData.put("theme", cmsSiteconfig.getTheme());
				jsData.put("id", cmsSiteconfig.getId());
				//如果是省中心，则显示背景图片和头部文字
				String curTenant = (String) TenantConfig.getCacheTenant();
				Boolean isProvince = false;
				//todo 将1替换成省中心的租户id
				// if(StringUtil.isNotEmpty(curTenant) && curTenant.equals(CoreIds.NPR_SYS_TENANT.getId())){
				if(StringUtil.isNotEmpty(curTenant) && curTenant.equals(CoreIds.NPR_SYS_TENANT.getId())){
					isProvince = true;
					jsData.put("headText",cmsSiteconfig.getHeadText());
					jsData.put("isProvince",CoreSval.Const.YES);
				}else{
					jsData.remove("headText");
					jsData.put("isProvince",CoreSval.Const.NO);
				}
				JSONArray jsonArray= new JSONArray();
				for(CmsSiteconfig cmsSiteconfigIndex:cmsSiteconfigList){
					JSONObject  jsDataindex= new JSONObject();
					if(!isProvince && cmsSiteconfigIndex.getType().equals("backgroundUrl")){
						continue;
					}
					jsDataindex.put(cmsSiteconfigIndex.getType(),cmsSiteconfigIndex.getPicUrl());

					jsonArray.add(jsDataindex);
				}

				jsData.put("picList",jsonArray);
			}
			js.put("data", jsData);
			js.put("ret", "1");
		}catch (Exception e){
			js.put("ret", "0");
			js.put("msg", "查看失败,出现了未知的错误，请重试或者联系管理员");
		}
		return js;
	}

}
/**
 *
 */
package com.oseasy.pro.modules.excellent.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.cms.common.config.CmsSval;
import com.oseasy.cms.common.config.CmsSval.CmsEmskey;
import com.oseasy.cms.modules.cms.vo.ExcellentGcontestVo;
import com.oseasy.cms.modules.cms.vo.ExcellentProjectVo;
import com.oseasy.com.fileserver.common.utils.FtpUtil;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.modules.excellent.entity.ExcellentShow;
import com.oseasy.pro.modules.excellent.service.ExcellentKeywordService;
import com.oseasy.pro.modules.excellent.service.ExcellentShowService;

import net.sf.json.JSONObject;

/**
 * 内容管理优秀展示Controller

 * @version 2013-4-21
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/excellent")
public class ProCmsExcellentController extends BaseController {
	public static final String FRONT_URL = CoreSval.getConfig("sysFrontIp")+CoreSval.getConfig("frontPath");
	@Autowired
	private ExcellentShowService excellentShowService;
	@Autowired
	private ExcellentKeywordService excellentKeywordService;
	@RequestMapping(value = "gcontestList")
	public String gcontestList(ExcellentGcontestVo vo,HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("vo", vo);
		Page<ExcellentGcontestVo> page = excellentShowService.findPage(new Page<ExcellentGcontestVo>(request, response), vo);
		model.addAttribute("page", page);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "excellent/gcontestList";
	}
	@RequestMapping(value = "projectList")
	public String projectList(ExcellentProjectVo vo,HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("vo", vo);
		Page<ExcellentProjectVo> page = excellentShowService.findPage(new Page<ExcellentProjectVo>(request, response), vo);
		model.addAttribute("page", page);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "excellent/projectList";
	}
	@RequestMapping(value = "unrelease")
	public String unrelease(String ids,String from,HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes) {
		JSONObject ret=excellentShowService.unrelease(ids);
		addMessage(redirectAttributes, ret.getString("msg"));
		return CoreSval.REDIRECT + adminPath + "/cms/excellent/"+from+"?repage";
	}
	@RequestMapping(value = "delete")
	public String delete(String ids,String from,HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes) {
		JSONObject ret=excellentShowService.delete(ids);
		addMessage(redirectAttributes, ret.getString("msg"));
		return CoreSval.REDIRECT + adminPath + "/cms/excellent/"+from+"?repage";
	}
	@RequestMapping(value = {"projectShowForm"})
	public String projectShowForm( HttpServletRequest request, HttpServletResponse response, Model model) {
		String projectId=request.getParameter("projectId");
		ExcellentShow es=excellentShowService.getByForid(projectId);
		if (es!=null) {
			es.setKeywords(excellentKeywordService.findListByEsid(es.getId()));
			es.setContent(StringEscapeUtils.unescapeHtml4(es.getContent()));
			es.setContent(es.getContent().replaceAll(FtpUtil.FTP_MARKER,FtpUtil.FTP_HTTPURL));
		}else{
			es=new ExcellentShow();
			es.setIsRelease("1");
			es.setIsTop("0");
			es.setIsComment("1");
			es.setType(ExcellentShow.Type_Project);
			es.setForeignId(projectId);
		}
		model.addAttribute("es", es);
		model.addAttribute("projectInfo", excellentShowService.getProjectInfo(projectId));
		model.addAttribute("projectTeacherInfo", excellentShowService.getProjectTeacherInfo(projectId));
		model.addAttribute("front_url", FRONT_URL);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "excellent/excellentForm";
	}
	@RequestMapping(value = {"gcontestShowForm"})
	public String gcontestShowForm( HttpServletRequest request, HttpServletResponse response, Model model) {
		String gcontestId=request.getParameter("gcontestId");
		ExcellentShow es=excellentShowService.getByForid(gcontestId);
		if (es!=null) {
			es.setKeywords(excellentKeywordService.findListByEsid(es.getId()));
			es.setContent(StringEscapeUtils.unescapeHtml4(es.getContent()));
			es.setContent(es.getContent().replaceAll(FtpUtil.FTP_MARKER,FtpUtil.FTP_HTTPURL));
		}else{
			es=new ExcellentShow();
			es.setIsRelease("1");
			es.setIsTop("0");
			es.setIsComment("1");
			es.setType(ExcellentShow.Type_Gcontest);
			es.setForeignId(gcontestId);
		}
		model.addAttribute("es", es);
		model.addAttribute("projectInfo", excellentShowService.getGcontestInfo(gcontestId));
		model.addAttribute("projectTeacherInfo", excellentShowService.getGcontestTeacherInfo(gcontestId));
		model.addAttribute("front_url", FRONT_URL);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "excellent/excellentForm";
	}
}

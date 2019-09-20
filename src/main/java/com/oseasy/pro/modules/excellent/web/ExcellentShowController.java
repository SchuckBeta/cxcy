package com.oseasy.pro.modules.excellent.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.pro.modules.interactive.service.SysViewsService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.fileserver.common.utils.FtpUtil;
import com.oseasy.com.mqserver.modules.oa.utils.OaUtils;
import com.oseasy.com.pcore.common.config.CorePages;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.config.ProSval.ProEmskey;
import com.oseasy.pro.modules.excellent.entity.ExcellentShow;
import com.oseasy.pro.modules.excellent.service.ExcellentKeywordService;
import com.oseasy.pro.modules.excellent.service.ExcellentShowService;

import net.sf.json.JSONObject;

/**
 * 优秀展示Controller.
 * @author 9527
 * @version 2017-06-23
 */
@Controller
public class ExcellentShowController extends BaseController {
	public static final String FRONT_URL = CoreSval.getConfig("sysFrontIp")+CoreSval.getConfig("frontPath");
	@Autowired
	private ExcellentKeywordService excellentKeywordService;
	@Autowired
	private ExcellentShowService excellentShowService;
	/*后台添加编辑大赛展示信息*/
	@RequiresPermissions("excellent:gcontestShow:edit")
	@RequestMapping(value = {"${adminPath}/excellent/gcontestShowForm"})
	public String gcontestShowForm( HttpServletRequest request, HttpServletResponse response, Model model) {
		String gcontestId=request.getParameter("gcontestId");
		ExcellentShow es=excellentShowService.getByForid(gcontestId);
		Map<String, String> projectInfo=excellentShowService.getGcontestInfo(gcontestId);
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
			es.setSubType(projectInfo.get("subtype"));
			es.setForeignId(gcontestId);
		}

		HashMap<String, Object> formData = new HashMap<>();
		model.addAttribute("es", es);
		model.addAttribute("projectInfo", projectInfo);
		model.addAttribute("projectTeacherInfo", excellentShowService.getGcontestTeacherInfo(gcontestId));
		model.addAttribute("front_url", FRONT_URL);
		formData.put("excellentProVo", es);
        formData.put("projectInfo", projectInfo);
        formData.put("projectTeacherInfo", excellentShowService.getGcontestTeacherInfo(gcontestId));
        formData.put("front_url", FRONT_URL);
        model.addAttribute("excellentPro", formData);
		return ProSval.path.vms(ProEmskey.EXCELLENT.k()) + "excellentForm";
	}
	/*前台添加编辑大赛展示信息*/
	@RequestMapping(value = {"${frontPath}/excellent/gcontestShowForm"})
	public String frontGcontestShowForm( HttpServletRequest request, HttpServletResponse response, Model model) {
		String gcontestId=request.getParameter("gcontestId");
		ExcellentShow es=excellentShowService.getByForid(gcontestId);
		Map<String, String> projectInfo=excellentShowService.getGcontestInfo(gcontestId);
		if (es!=null) {
			if ("1".equals(es.getIsRelease())) {
				return CoreSval.REDIRECT+frontPath+"/frontExcellentView-"+es.getId();
			}
			es.setKeywords(excellentKeywordService.findListByEsid(es.getId()));
            es.setContent(OaUtils.convertFront(es.getContent()));
			es.setContent(StringEscapeUtils.unescapeHtml4(es.getContent()));
			es.setContent(es.getContent().replaceAll(FtpUtil.FTP_MARKER,FtpUtil.FTP_HTTPURL));
		}else{
			es=new ExcellentShow();
			es.setType(ExcellentShow.Type_Gcontest);
			es.setSubType(projectInfo.get("subtype"));
			es.setForeignId(gcontestId);
		}
		model.addAttribute("fromPage", "gcontest");
		model.addAttribute("es", es);
		model.addAttribute("projectInfo", projectInfo);
		model.addAttribute("projectTeacherInfo", excellentShowService.getGcontestTeacherInfo(gcontestId));
		model.addAttribute("front_url", FRONT_URL);
		return ProSval.path.vms(ProEmskey.EXCELLENT.k()) + "frontExcellentForm";
	}
	/*后台预览优秀展示*/
	/*@RequestMapping(value = {"${adminPath}/excellent/excellentPreview"})
	public String excellentPreview(ExcellentShow es,HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute(request.getParameterMap());
		return CoreSval.REDIRECT+FRONT_URL+"/frontExcellentPreviewForBack";
	}*/
	/*接收后台重定向的前台预览优秀展示*/
	/*@SuppressWarnings("unchecked")
	@RequestMapping(value = {"${frontPath}/frontExcellentPreviewForBack"})
	public String frontExcellentPreviewForBack(ExcellentShow es,HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String,?> map = RequestContextUtils.getInputFlashMap(request);
		if (map!=null) {
			Map<String,Object> parameterMap =(Map<String,Object>)map.get("parameterMap");
			if (parameterMap != null && es != null) {
				if (parameterMap.get("type")!=null) {
					es.setType(((String[])parameterMap.get("type"))[0]);
				}
				if (parameterMap.get("content")!=null) {
					es.setContent(((String[])parameterMap.get("content"))[0]);
			    }
				if (parameterMap.get("foreignId")!=null) {
					es.setForeignId(((String[])parameterMap.get("foreignId"))[0]);
				}
				if (parameterMap.get("coverImg")!=null) {
					es.setCoverImg(((String[])parameterMap.get("coverImg"))[0]);
				}
				if (parameterMap.get("keywords")!=null) {
					es.setKeywords(Arrays.asList((String[])parameterMap.get("keywords")));
			    }
			    if (es!=null&&ExcellentShow.Type_Project.equals(es.getType())&&"0".equals(es.getDelFlag())) {
			      es.setContent(StringEscapeUtils.unescapeHtml4(es.getContent()));
			      model.addAttribute("es", es);
			      model.addAttribute("projectInfo", excellentShowService.getProjectInfo(es.getForeignId()));
			      model.addAttribute("projectTeacherInfo", excellentShowService.getProjectTeacherInfo(es.getForeignId()));
			    }
			    if (es!=null&&ExcellentShow.Type_Gcontest.equals(es.getType())&&"0".equals(es.getDelFlag())) {
			      es.setContent(StringEscapeUtils.unescapeHtml4(es.getContent()));
			      model.addAttribute("es", es);
			      model.addAttribute("projectInfo", excellentShowService.getGcontestInfo(es.getForeignId()));
			      model.addAttribute("projectTeacherInfo", excellentShowService.getGcontestTeacherInfo(es.getForeignId()));
			    }
			}
		}
		return ProSval.path.vms(ProEmskey.EXCELLENT.k()) + "frontExcellentPreview";
	}*/
	/*前台预览优秀展示*/
	@RequestMapping(value = {"${frontPath}/frontExcellentPreview"})
	public String frontExcellentPreview(ExcellentShow es,HttpServletRequest request, HttpServletResponse response, Model model) {
		if (es!=null&&ExcellentShow.Type_Project.equals(es.getType())&&"0".equals(es.getDelFlag())) {
            es.setContent(OaUtils.convertFront(es.getContent()));
			es.setContent(StringEscapeUtils.unescapeHtml4(es.getContent()));
			model.addAttribute("es", es);
			model.addAttribute("projectInfo", excellentShowService.getProjectInfo(es.getForeignId()));
			model.addAttribute("projectTeacherInfo", excellentShowService.getProjectTeacherInfo(es.getForeignId()));
		}
		if (es!=null&&ExcellentShow.Type_Gcontest.equals(es.getType())&&"0".equals(es.getDelFlag())) {
            es.setContent(OaUtils.convertFront(es.getContent()));
			es.setContent(StringEscapeUtils.unescapeHtml4(es.getContent()));
			model.addAttribute("es", es);
			model.addAttribute("projectInfo", excellentShowService.getGcontestInfo(es.getForeignId()));
			model.addAttribute("projectTeacherInfo", excellentShowService.getGcontestTeacherInfo(es.getForeignId()));
		}
		return ProSval.path.vms(ProEmskey.EXCELLENT.k()) + "frontExcellentPreview";
	}
	/*前台查看优秀展示*/
	@RequestMapping(value = {"${frontPath}/frontExcellentView-{excid}"})
	public String frontExcellentProjectView(@PathVariable String excid,HttpServletRequest request, HttpServletResponse response, Model model) {
		ExcellentShow es=excellentShowService.get(excid);
		if (es!=null&&"0".equals(es.getDelFlag())) {
			es.setKeywords(excellentKeywordService.findListByEsid(es.getId()));
            es.setContent(OaUtils.convertFront(es.getContent()));
			es.setContent(StringEscapeUtils.unescapeHtml4(es.getContent()));
			es.setContent(es.getContent().replaceAll(FtpUtil.FTP_MARKER,FtpUtil.FTP_HTTPURL));
			model.addAttribute("es", es);
			/*记录浏览量*/
			SysViewsService.updateViews(excid, request,CacheUtils.EXCELLENT_VIEWS_QUEUE);
			/*记录浏览量*/
			if (ExcellentShow.Type_Project.equals(es.getType())) {//双创项目
				model.addAttribute("projectInfo", excellentShowService.getProjectInfo(es.getForeignId()));
				model.addAttribute("projectTeacherInfo", excellentShowService.getProjectTeacherInfo(es.getForeignId()));
			}
			if (ExcellentShow.Type_Gcontest.equals(es.getType())) {//互联网+大赛
				model.addAttribute("projectInfo", excellentShowService.getGcontestInfo(es.getForeignId()));
				model.addAttribute("projectTeacherInfo", excellentShowService.getGcontestTeacherInfo(es.getForeignId()));
			}
		}else{
			return CorePages.ERROR_404.getIdxUrl();
		}
		return ProSval.path.vms(ProEmskey.EXCELLENT.k()) + "frontExcellentView";
	}
	/*后台添加编辑项目展示信息*/
	@RequiresPermissions("excellent:projectShow:edit")
	@RequestMapping(value = {"${adminPath}/excellent/projectShowForm"})
	public String projectShowForm( HttpServletRequest request, HttpServletResponse response, Model model) {
		String projectId=request.getParameter("projectId");
		ExcellentShow es=excellentShowService.getByForid(projectId);
		Map<String, String> projectInfo=excellentShowService.getProjectInfo(projectId);
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
			es.setSubType(projectInfo.get("subtype"));
			es.setForeignId(projectId);
		}
		HashMap<String, Object> formData = new HashMap<>();
		model.addAttribute("es", es);
		model.addAttribute("projectInfo", projectInfo);
		model.addAttribute("projectTeacherInfo", excellentShowService.getProjectTeacherInfo(projectId));
		model.addAttribute("front_url", FRONT_URL);
		formData.put("excellentProVo", es);
		formData.put("projectInfo", projectInfo);
		formData.put("projectTeacherInfo", excellentShowService.getProjectTeacherInfo(projectId));
		formData.put("front_url", FRONT_URL);
		model.addAttribute("excellentPro", formData);
		return ProSval.path.vms(ProEmskey.EXCELLENT.k()) + "excellentForm";
	}
	/*前台添加编辑项目展示信息*/
	@RequestMapping(value = {"${frontPath}/excellent/projectShowForm"})
	public String frontProjectShowForm( HttpServletRequest request, HttpServletResponse response, Model model) {
		String projectId=request.getParameter("projectId");
		ExcellentShow es=excellentShowService.getByForid(projectId);
		Map<String, String> projectInfo=excellentShowService.getProjectInfo(projectId);
		if (es!=null) {
			/*if ("1".equals(es.getIsRelease())) {
				return CoreSval.REDIRECT+frontPath+"/frontExcellentView-"+es.getId();
			}*/
			es.setKeywords(excellentKeywordService.findListByEsid(es.getId()));
            es.setContent(OaUtils.convertFront(es.getContent()));
			es.setContent(StringEscapeUtils.unescapeHtml4(es.getContent()));
			es.setContent(es.getContent().replaceAll(FtpUtil.FTP_MARKER,FtpUtil.FTP_HTTPURL));
		}else{
			es=new ExcellentShow();
			es.setType(ExcellentShow.Type_Project);
			es.setSubType(projectInfo.get("subtype"));
			es.setForeignId(projectId);
		}
		model.addAttribute("fromPage", "project");
		model.addAttribute("es", es);
		model.addAttribute("projectInfo", projectInfo);
		model.addAttribute("projectTeacherInfo", excellentShowService.getProjectTeacherInfo(projectId));
	    model.addAttribute("front_url", FRONT_URL);
	    return ProSval.path.vms(ProEmskey.EXCELLENT.k()) + "frontExcellentForm";
	}
	/*首页优秀项目展示查询*/
	@RequestMapping(value = {"${frontPath}/excellent/findForIndex"})
	@ResponseBody
	public Map<String,Object> findForIndex( HttpServletRequest request, HttpServletResponse response, Model model) {
		return excellentShowService.findForIndex();
	}

	/*后台保存优秀展示*/
	@RequestMapping(value = "${adminPath}/excellent/save")
	@ResponseBody
	public JSONObject save(ExcellentShow excellentShow, Model model, RedirectAttributes redirectAttributes) {
		return excellentShowService.saveExcellentShow(excellentShow);
	}
	/*前台保存优秀展示*/
	@RequestMapping(value = "${frontPath}/excellent/save")
	@ResponseBody
	public JSONObject frontSave(ExcellentShow excellentShow, Model model, RedirectAttributes redirectAttributes) {
		return excellentShowService.frontSaveExcellentShow(excellentShow);
	}
	/*批量发布*/
	@RequestMapping(value = "${adminPath}/excellent/resall")
	@ResponseBody
	public JSONObject resall(String fids, Model model, RedirectAttributes redirectAttributes) {
		return excellentShowService.resall(fids);
	}
}
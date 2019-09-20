package com.oseasy.pro.modules.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.com.common.config.ApiStatus;
import com.oseasy.com.fileserver.common.utils.FtpUtil;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.config.ProSval.ProEmskey;
import com.oseasy.pro.common.service.CommonService;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.pro.modules.proprojectmd.entity.ProModelMd;
import com.oseasy.pro.modules.proprojectmd.service.ProModelMdService;
import com.oseasy.pro.modules.tpl.service.WordService;
import com.oseasy.pro.modules.tpl.vo.IWparam;
import com.oseasy.pro.modules.tpl.vo.WproType;
import com.oseasy.pro.modules.tpl.vo.Wtparam;
import com.oseasy.pro.modules.tpl.vo.Wtype;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;
import com.oseasy.sys.modules.sys.service.BackTeacherExpansionService;
import com.oseasy.sys.modules.sys.service.StudentExpansionService;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * proProjectMdController.
 * @author zy
 * @version 2017-09-18
 */
@Controller
@RequestMapping(value = "${frontPath}/proprojectmd/proModelMd")
public class FrontProModelMdController extends BaseController {
	@Autowired
	private ProModelService proModelService;
	@Autowired
	private ProModelMdService proModelMdService;
	@Autowired
	private WordService wordService;
	@Autowired
	private TeamService teamService;

	@Autowired
	private BackTeacherExpansionService backTeacherExpansionService;
	@Autowired
	private StudentExpansionService studentExpansionService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private ActYwService actYwService;
	@ModelAttribute
	public ProModelMd get(@RequestParam(required=false) String id) {
		ProModelMd entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = proModelMdService.get(id);
		}
		if (entity == null) {
			entity = new ProModelMd();
		}
		return entity;
	}

	@RequiresPermissions("proprojectmd:proModelMd:view")
	@RequestMapping(value = {"list", ""})
	public String list(ProModelMd proModelMd, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ProModelMd> page = proModelMdService.findPage(new Page<ProModelMd>(request, response), proModelMd);
		model.addAttribute("page", page);
		return ProSval.path.vms(ProEmskey.PROPROJECTMD.k()) + "proModelMdList";
	}


	@RequestMapping(value = "form")
	public String form(ProModelMd proModelMd, Model model) {
    model.addAttribute("proModelMd", proModelMd);
		//return ProSval.path.vms(ProEmskey.PROPROJECTMD.k()) + "proModelMdForm";
		return "/template/form/project/md_applyForm";
	}
	//保存第一步
	@RequestMapping(value = "ajaxSave")
	@ResponseBody
	public JSONObject ajaxSave(ProModelMd proModelMd, Model model, RedirectAttributes redirectAttributes) {
		JSONObject js=new JSONObject();
		if(StringUtil.isEmpty(proModelMd.getProModel().getYear())){
			proModelMd.getProModel().setYear(commonService.getApplyYear(proModelMd.getProModel().getActYwId()));
		}
		js=commonService.onProjectSaveStep1(proModelMd.getProModel().getActYwId(),proModelMd.getProModel().getId(), proModelMd.getProModel().getYear());
		if ("0".equals(js.getString("ret"))) {
			return js;
		}
		js=proModelMdService.ajaxSaveProModelMd(proModelMd);
		js.put("id", proModelMd.getId());
		js.put("proModelId", proModelMd.getProModel().getId());
		js.put("proCategory", proModelMd.getProModel().getProCategory());
		return js;
	}
	//保存第二步
	@RequestMapping(value = "ajaxSave2")
	@ResponseBody
	public JSONObject ajaxSave2(ProModelMd proModelMd, Model model, RedirectAttributes redirectAttributes) {
		JSONObject js=new JSONObject();
		if(StringUtil.isEmpty(proModelMd.getProModel().getYear())){
			proModelMd.getProModel().setYear(commonService.getApplyYear(proModelMd.getProModel().getActYwId()));
		}
		js=commonService.onProjectSaveStep2(proModelMd.getProModel().getId(), proModelMd.getProModel().getActYwId(), proModelMd.getProModel().getProCategory(), proModelMd.getProModel().getTeamId(), proModelMd.getProModel().getYear());
		if ("0".equals(js.getString("ret"))) {
			return js;
		}
		js=proModelMdService.ajaxSaveProModelMd(proModelMd);
		js.put("id", proModelMd.getId());
		js.put("proModelId", proModelMd.getProModel().getId());
		js.put("proCategory", proModelMd.getProModel().getProCategory());
		return js;
	}
	//保存第三步
	@RequestMapping(value = "save")
	@ResponseBody
	public JSONObject save(ProModelMd proModelMd, Model model, RedirectAttributes redirectAttributes) {
		JSONObject js=new JSONObject();
		js.put("ret", 1);
		if(StringUtil.isEmpty(proModelMd.getProModel().getYear())){
			proModelMd.getProModel().setYear(commonService.getApplyYear(proModelMd.getProModel().getActYwId()));
		}
		js=commonService.onProjectSaveStep2(proModelMd.getProModel().getId(), proModelMd.getProModel().getActYwId(), proModelMd.getProModel().getProCategory(), proModelMd.getProModel().getTeamId(), proModelMd.getProModel().getYear());
		if ("0".equals(js.getString("ret"))) {
			return js;
		}
		String msg=proModelMdService.saveProModelMd(proModelMd);
		js.put("msg", msg);
		js.put("id", proModelMd.getId());
		js.put("proModelId", proModelMd.getProModel().getId());
		js.put("proCategory", proModelMd.getProModel().getProCategory());
		js.put("fileUrl", proModelMd.getFileUrl());
		js.put("fileHttpUrl", FtpUtil.getFileUrl(proModelMd.getFileUrl()));
		js.put("fileId", proModelMd.getFileId());
		return js;
	}

	@RequestMapping(value = "ajaxWtparam")
	@ResponseBody
	public Wtparam ajaxWtparam() {
        return new Wtparam(IWparam.getFileTplPreFix(), Wtype.toJson());
	}
	//下载word模板文档
	@RequestMapping(value = "ajaxWord")
	@ResponseBody
	public ApiStatus ajaxWord(String proId, String type, String vsn, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		//ProModelMd proModelMd = proModelMdService.get(proId);
		ProModelMd proModelMd = proModelMdService.getByProModelId(proId);

		if ((proModelMd == null) || StringUtil.isEmpty(proModelMd.getModelId())) {
		  	return new ApiStatus(false, "ProId 或 proModelMd 参数不能为空！");
		}

		ProModel proModel = proModelService.get(proModelMd.getModelId());
		if ((proModel == null) || StringUtil.isEmpty(proModel.getTeamId())) {
      		return new ApiStatus(false, "ProModel Id 或 Team id 参数不能为空！");
		}

		String teamId = proModelMd.getProModel().getTeamId();
		Team team = teamService.get(teamId);
		List<BackTeacherExpansion> qytes = backTeacherExpansionService.getQYTeacher(teamId);
		List<BackTeacherExpansion> xytes = backTeacherExpansionService.getXYTeacher(teamId);
		List<StudentExpansion> tms = studentExpansionService.getStudentByTeamId(teamId);
		if ((team == null) || ((qytes == null) && (xytes == null)) || (tms == null)) {
      		return new ApiStatus(false, "团队、导师参数不能为空！");
		}

		if (qytes == null) {
		  qytes = Lists.newArrayList();
		}

		if (xytes == null) {
		  xytes = Lists.newArrayList();
		}

		WproType wproType = WproType.getByKey(proModel.getProCategory());
		if (wproType == null) {
		  	return new ApiStatus(false, "proCategory 项目类型未定义["+proModel.getProCategory()+"]");
		}

		IWparam wordParam = proModelMdService.initIWparam(type, vsn, proModel, proModelMd, team, xytes, qytes, tms);
		if (wordParam != null) {
			wordService.exeDownload(vsn, wordParam, request, response);
			return null;
		}
		return new ApiStatus(false, "模板下载失败！");
	}
	//第三步提交
	@RequestMapping(value = "submit")
	@ResponseBody
	public JSONObject submit(ProModelMd proModelMd, Model model, RedirectAttributes redirectAttributes) {
		JSONObject js=new JSONObject();
		js.put("ret", 1);
		if(StringUtil.isEmpty(proModelMd.getProModel().getYear())){
			proModelMd.getProModel().setYear(commonService.getApplyYear(proModelMd.getProModel().getActYwId()));
		}
		js=commonService.onProjectSubmitStep3(proModelMd.getProModel().getId(), proModelMd.getProModel().getActYwId(), proModelMd.getProModel().getProCategory(), proModelMd.getProModel().getTeamId(), proModelMd.getProModel().getYear());
		if ("0".equals(js.getString("ret"))) {
			return js;
		}
		js=proModelMdService.submit(proModelMd,js);
		ActYw actYw = actYwService.get(proModelMd.getProModel().getActYwId());
		if (actYw != null) {
			js.put("pptype", actYw.getProProject().getType());
			js.put("proProjectId", actYw.getProProject().getId());
		}
		js.put("actywId", proModelMd.getProModel().getActYwId());
		js.put("projectId", proModelMd.getProModel().getId());
		return js;
	}

	@RequestMapping(value = "midSubmit")
	@ResponseBody
	public JSONObject midSubmit(ProModelMd proModelMd, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		JSONObject js=new JSONObject();
		js.put("ret", 1);
		String gnodeId=request.getParameter("gnodeId");

		js=proModelMdService.midSubmit(proModelMd,js,gnodeId);
		return js;
	}

	@RequestMapping(value = "closeSubmit")
	@ResponseBody
	public JSONObject closeSubmit(ProModelMd proModelMd, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		JSONObject js=new JSONObject();
		js.put("ret", 1);
		String gnodeId=request.getParameter("gnodeId");

		js=proModelMdService.closeSubmit(proModelMd,js,gnodeId);
		return js;
	}

	@RequestMapping(value = "delete")
	public String delete(ProModelMd proModelMd, RedirectAttributes redirectAttributes) {
		proModelMdService.delete(proModelMd);
		addMessage(redirectAttributes, "删除proProjectMd成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/proprojectmd/proModelMd/?repage";
	}

}
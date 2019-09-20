package com.oseasy.pro.modules.web;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.config.ProSval.ProEmskey;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.pro.modules.proprojectmd.entity.ProModelMd;
import com.oseasy.pro.modules.proprojectmd.service.ProModelMdService;
import com.oseasy.pro.modules.promodel.web.ProModelController;

import net.sf.json.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * proProjectMdController.
 * @author zy
 * @version 2017-09-18
 */
@Controller
@RequestMapping(value = "${adminPath}/proprojectmd/proModelMd")
public class ProModelMdController extends BaseController {
	@Autowired
	private ProjectDeclareService projectDeclareService;
	@Autowired
	private ProModelService proModelService;
	@Autowired
	private ProModelMdService proModelMdService;
	@Autowired
	private SysAttachmentService sysAttachmentService;
	@Autowired
	private ActYwService actYwService;
	@Autowired
	private TeamService teamService;
	@Autowired
	private SystemService systemService;
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

	@RequiresPermissions("proprojectmd:proModelMd:edit")
	@RequestMapping(value = "save")
	public String save(ProModelMd proModelMd, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, proModelMd)) {
			return form(proModelMd, model);
		}
		proModelMdService.save(proModelMd);
		addMessage(redirectAttributes, "保存proProjectMd成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/proprojectmd/proModelMd/?repage";
	}

	@RequiresPermissions("proprojectmd:proModelMd:edit")
	@RequestMapping(value = "delete")
	public String delete(ProModelMd proModelMd, RedirectAttributes redirectAttributes) {
		proModelMdService.delete(proModelMd);
		addMessage(redirectAttributes, "删除proProjectMd成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/proprojectmd/proModelMd/?repage";
	}

	@RequestMapping(value = "saveModify")
	@ResponseBody
	public JSONObject saveModify(ProModelMd proModelMd, RedirectAttributes redirectAttributes, Model model) {
		try {
			return proModelMdService.saveModify(proModelMd);
		} catch (Exception e) {
			logger.error(e.getMessage());
			JSONObject js=new JSONObject();
			js.put("ret", "0");
			js.put("msg", "保存失败,出现了未知的错误，请重试或者联系管理员");
			return js;
		}
	}
	@RequestMapping(value = "toModifyPage")
	public String toModifyPage(String proModelId, RedirectAttributes redirectAttributes, Model model) {
		ProModel proModel=proModelService.get(proModelId);
		model.addAttribute("proModel", proModel);
		User user=systemService.getUser(proModel.getDeclareId());
		ActYw actYw=actYwService.get(proModel.getActYwId());
		ProProject proProject=actYw.getProProject();
		if (proProject!=null) {
			ProModelController.showFrontMessage(proProject,model);
		}
		if (proModel.getDeclareId()!=null) {
			model.addAttribute("sse", systemService.getUser(proModel.getDeclareId()));
		}else{
			model.addAttribute("sse", user);
		}

		model.addAttribute("projectName", actYw.getProProject().getProjectName());
		model.addAttribute("team", teamService.get(proModel.getTeamId()));
		model.addAttribute("teamStu", projectDeclareService.findTeamStudentFromTUH(proModel.getTeamId(),proModel.getId()));
		model.addAttribute("teamTea", projectDeclareService.findTeamTeacherFromTUH(proModel.getTeamId(),proModel.getId()));
		ProModelMd proModelMd=proModelMdService.getByProModelId(proModel.getId());
		SysAttachment sa=new SysAttachment();
		sa.setUid(proModel.getId());
		sa.setType(FileTypeEnum.S10);
		sa.setFileStep(FileStepEnum.S2000);
		List<SysAttachment> fileListMap1 =  sysAttachmentService.getFiles(sa);
		model.addAttribute("appSysAttachments", fileListMap1);
		sa.setUid(proModel.getId());
		sa.setType(FileTypeEnum.S10);
		sa.setFileStep(FileStepEnum.S2200);
		List<SysAttachment> fileListMap2 =  sysAttachmentService.getFiles(sa);
		model.addAttribute("midSysAttachments", fileListMap2);
		sa.setUid(proModel.getId());
		sa.setType(FileTypeEnum.S10);
		sa.setFileStep(FileStepEnum.S2300);
		List<SysAttachment> fileListMap3 =  sysAttachmentService.getFiles(sa);
		model.addAttribute("closeSysAttachments", fileListMap3);
		model.addAttribute("proModelMd",proModelMd);
		return ProSval.path.vms(ProEmskey.PROPROJECTMD.k()) + "md_modifyForm";
	}
}
package com.oseasy.pro.modules.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.AttachMentEntity;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.config.ProSval.ProEmskey;
import com.oseasy.pro.common.service.CommonService;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.pro.modules.tpl.vo.IWparam;
import com.oseasy.pro.modules.tpl.vo.Wtparam;
import com.oseasy.pro.modules.tpl.vo.Wtype;
import com.oseasy.pro.modules.workflow.entity.ProModelGzsmxx;
import com.oseasy.pro.modules.workflow.service.ProModelGzsmxxService;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

import net.sf.json.JSONObject;

/**
 * proProjectMdController.
 *
 * @author zy
 * @version 2017-09-18
 */
@Controller
@RequestMapping(value = "${frontPath}/proModelGzsmxx")
public class FrontProModelGzsmxxController extends BaseController {

    @Autowired
    private ProModelService proModelService;

    @Autowired
    private ProModelGzsmxxService proModelGzsmxxService;

    @Autowired
    private ProjectDeclareService projectDeclareService;

    @Autowired
    private SysAttachmentService sysAttachmentService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private ActYwService actYwService;

    @ModelAttribute
    public ProModelGzsmxx get(@RequestParam(required = false) String id) {
        ProModelGzsmxx entity = null;
        if (StringUtil.isNotBlank(id)) {
            entity = proModelGzsmxxService.get(id);
        }
        if (entity == null) {
            entity = new ProModelGzsmxx();
        }
        return entity;
    }

    @RequiresPermissions("proprojectmd:proModelGzsmxx:view")
    @RequestMapping(value = {"list", ""})
    public String list(ProModelGzsmxx proModelGzsmxx, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ProModelGzsmxx> page = proModelGzsmxxService.findPage(new Page<ProModelGzsmxx>(request, response), proModelGzsmxx);
        model.addAttribute("page", page);
        return ProSval.path.vms(ProEmskey.WORKFLOW.k()) + "gzsmxx/proModelGzsmxxList";
    }


    @RequestMapping(value = "form")
    public String form(ProModelGzsmxx proModelGzsmxx, Model model) {
        model.addAttribute("proModelGzsmxx", proModelGzsmxx);
        //return ProSval.path.vms(ProEmskey.WORKFLOW.k()) + "proModelGzsmxxForm";
        return "/template/form/project/md_applyForm";
    }

    @RequestMapping(value = "/applyStep1")
    public String applyStep1(ProModelGzsmxx proModelGzsmxx, HttpServletRequest request, HttpServletResponse response, Model model) {
        User cuser = UserUtils.getUser();
        String parmact = null;
        if (StringUtil.isNotEmpty(proModelGzsmxx.getId())) {
            proModelGzsmxx = proModelGzsmxxService.get(proModelGzsmxx.getId());
            ProModel proModel = proModelGzsmxx.getProModel();
            if (StringUtil.isEmpty(proModel.getDeclareId())
                    || StringUtil.isEmpty(cuser.getId()) || !cuser.getId().equals(proModel.getDeclareId())) {
                return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/promodel/proModel/viewForm?id=" + proModelGzsmxx.getId();
            }
            parmact = proModel.getActYwId();
        } else {
            proModelGzsmxx.setCreateDate(new Date());
            parmact = request.getParameter("actywId");
        }
        proModelGzsmxx.getProModel().setActYwId(parmact);
        if (StringUtil.isNotEmpty(parmact)) {
            ActYw actYw = actYwService.get(parmact);
            if (actYw != null) {
                ProProject proProject = actYw.getProProject();
                if (proProject != null && ProSval.PRO_TYPE_PROJECT.equals(proProject.getProType())
                        && StringUtil.isNotEmpty(proProject.getType())) {
                    model.addAttribute("proModelType", DictUtils.getDictLabel(proProject.getType(), "project_style", ""));
                }
            }
        }
        ActYw actYw = actYwService.get(parmact);
        model.addAttribute("sysdate", DateUtil.formatDate(proModelGzsmxx.getCreateDate(), "yyyy-MM-dd"));
        model.addAttribute("actYw", actYw);
        model.addAttribute("teams", projectDeclareService.findTeams(cuser.getId(), ""));
        model.addAttribute("proModelGzsmxx", proModelGzsmxx);
        model.addAttribute("cuser", cuser);
        model.addAttribute("teamStu", projectDeclareService.findTeamStudentFromTUH(proModelGzsmxx.getProModel().getTeamId(), proModelGzsmxx.getProModel().getId()));
        model.addAttribute("teamTea", projectDeclareService.findTeamTeacherFromTUH(proModelGzsmxx.getProModel().getTeamId(), proModelGzsmxx.getProModel().getId()));
        return "template/formtheme/gcontest/gzsmxx_applyForm";
    }

    //保存第一步
    @RequestMapping(value = "/applyStep2")
    public String applyStep2(ProModelGzsmxx proModelGzsmxx, HttpServletRequest request, HttpServletResponse response, Model model) {
        String id = request.getParameter("id");
        User cuser = UserUtils.getUser();
        String parmact = null;
        if (StringUtil.isNotEmpty(proModelGzsmxx.getId())) {
            proModelGzsmxx = proModelGzsmxxService.get(proModelGzsmxx.getId());
            if (StringUtil.isEmpty(proModelGzsmxx.getProModel().getDeclareId())
                    || StringUtil.isEmpty(cuser.getId()) || !cuser.getId().equals(proModelGzsmxx.getProModel().getDeclareId())) {
                return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/promodel/proModel/viewForm?id=" + proModelGzsmxx.getId();
            }
            parmact = proModelGzsmxx.getProModel().getActYwId();
        } else {
            proModelGzsmxx.setCreateDate(new Date());
            parmact = request.getParameter("actywId");
        }
        proModelGzsmxx.getProModel().setActYwId(parmact);
        List<Dict> dictList = DictUtils.getDictList("project_region");
        List<Dict> dicts = new ArrayList<>();
        List<Dict> regionGroupList = new ArrayList<>();
        Dict regionDict = null;
        for (Dict dict : dictList) {
            if ("产品".equals(dict.getLabel())) {
                dicts.add(dict);
            } else if ("服务".equals(dict.getLabel())) {
                dicts.add(dict);
            }
            if (!StringUtils.isEmpty(proModelGzsmxx.getRegion())) {
                if (dict.getValue().equals(proModelGzsmxx.getRegion())) {
                    regionDict = dict;
                }
            }
        }
        if (regionDict == null) {
            regionDict = dicts.stream().filter(dict -> dict.getLabel().equals("产品")).collect(Collectors.toList()).get(0);
            proModelGzsmxx.setRegion(regionDict.getValue());
        }
        if (!StringUtils.isEmpty(proModelGzsmxx.getRegion())) {
            for (Dict dict : dictList) {
                if (regionDict.getId().equals(dict.getParentId())) {
                    regionGroupList.add(dict);
                }
            }
        }
        if (StringUtil.isNotEmpty(parmact)) {
            ActYw actYw = actYwService.get(parmact);
            if (actYw != null) {
                ProProject proProject = actYw.getProProject();
                if (proProject != null && ProSval.PRO_TYPE_PROJECT.equals(proProject.getProType())
                        && StringUtil.isNotEmpty(proProject.getType())) {
                    model.addAttribute("proModelType", DictUtils.getDictLabel(proProject.getType(), "project_style", ""));
                }
            }
        }
        model.addAttribute("sysdate", DateUtil.formatDate(proModelGzsmxx.getCreateDate(), "yyyy-MM-dd"));
        model.addAttribute("dicts", dicts);
        model.addAttribute("regionGroupList", regionGroupList);
        model.addAttribute("proModelGzsmxx", proModelGzsmxx);
        model.addAttribute("cuser", cuser);
        return "template/formtheme/gcontest/gzsmxx_applyStep2";
    }

    @RequestMapping(value = "getRegionGroup")
    @ResponseBody
    public ApiTstatus<List<Dict>> getRegionGroup(@RequestParam String value) {
        List<Dict> dictList = DictUtils.getDictList("project_region");
        List<Dict> dicts = new ArrayList<>();
        Dict regionDict = null;
        for (Dict dict : dictList) {
            if (value.equals(dict.getValue())) {
                regionDict = dict;
                break;
            }
        }
        for (Dict dict : dictList) {
            if (regionDict.getId().equals(dict.getParentId())) {
                dicts.add(dict);
            }
        }
        return new ApiTstatus<List<Dict>>(true, "查询成功！", dicts);
    }

    @RequestMapping(value = "/applyStep3")
    public String applyStep3(ProModelGzsmxx proModelGzsmxx, HttpServletRequest request, HttpServletResponse response, Model model) {
        User cuser = UserUtils.getUser();
        ProModel proModel = proModelGzsmxx.getProModel();
        if (StringUtil.isNotEmpty(proModel.getId())) {
            proModel = proModelService.get(proModel.getId());
            if (StringUtil.isEmpty(proModel.getDeclareId())
                    || StringUtil.isEmpty(cuser.getId()) || !cuser.getId().equals(proModel.getDeclareId())) {
                return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/promodel/proModel/viewForm?id=" + proModel.getId();
            }
        }
        String parmact = proModel.getActYwId();
        if (StringUtil.isNotEmpty(parmact)) {
            ActYw actYw = actYwService.get(parmact);
            if (actYw != null) {
                ProProject proProject = actYw.getProProject();
                if (proProject != null && ProSval.PRO_TYPE_PROJECT.equals(proProject.getProType())
                        && StringUtil.isNotEmpty(proProject.getType())) {
                    model.addAttribute("proModelType", DictUtils.getDictLabel(proProject.getType(), "project_style", ""));
                }
            }
        }
        SysAttachment sa = new SysAttachment();
        sa.setUid(proModel.getId());
        sa.setFileStep(FileStepEnum.S1102);
        sa.setType(FileTypeEnum.S11);
        proModel.setFileInfo(sysAttachmentService.getFiles(sa));
        proModelGzsmxx.setProModel(proModel);
        model.addAttribute("ProModelGzsmxx", proModelGzsmxx);
        model.addAttribute("sysdate", DateUtil.formatDate(proModelGzsmxx.getCreateDate(), "yyyy-MM-dd"));
        model.addAttribute("cuser", cuser);
        return "template/formtheme/gcontest/gzsmxx_applyStep3";
    }

    @RequestMapping(value = "ajaxSave")
    @ResponseBody
    public JSONObject ajaxSave(ProModelGzsmxx proModelGzsmxx, Model model, RedirectAttributes redirectAttributes) {
        JSONObject js = new JSONObject();
        if (StringUtil.isEmpty(proModelGzsmxx.getProModel().getYear())) {
            proModelGzsmxx.getProModel().setYear(commonService.getApplyYear(proModelGzsmxx.getProModel().getActYwId()));
        }
        js = commonService.onGcontestApply(proModelGzsmxx.getProModel().getId(), proModelGzsmxx.getProModel().getActYwId(), proModelGzsmxx.getProModel().getTeamId(), proModelGzsmxx.getProModel().getYear());
        if (StringUtils.isEmpty(proModelGzsmxx.getProModel().getTeamId())) {
            js.put("msg", "请选择团队信息！");
            js.put("ret", 0);
        }
        if ("0".equals(js.getString("ret"))) {
            return js;
        }
        js = proModelGzsmxxService.saveStep1(proModelGzsmxx);
        js.put("id", proModelGzsmxx.getId());
        js.put("proModelId", proModelGzsmxx.getProModel().getId());
        js.put("proCategory", proModelGzsmxx.getProModel().getProCategory());
        return js;
    }

    //不校验保存第二步
    @RequestMapping(value = "ajaxUncheckSave2")
    @ResponseBody
    public JSONObject ajaxUncheckSave2(ProModelGzsmxx proModelGzsmxx, Model model, RedirectAttributes redirectAttributes) {
        JSONObject js = new JSONObject();
        if (StringUtil.isEmpty(proModelGzsmxx.getProModel().getYear())) {
            proModelGzsmxx.getProModel().setYear(commonService.getApplyYear(proModelGzsmxx.getProModel().getActYwId()));
        }
        js = proModelGzsmxxService.saveStep2(proModelGzsmxx);
        js.put("id", proModelGzsmxx.getId());
        js.put("proModelId", proModelGzsmxx.getProModel().getId());
        js.put("proCategory", proModelGzsmxx.getProModel().getProCategory());
        return js;
    }

    @RequestMapping(value = "ajaxSave2")
    @ResponseBody
    public JSONObject ajaxSave2(ProModelGzsmxx proModelGzsmxx, Model model, RedirectAttributes redirectAttributes) {
        JSONObject js = new JSONObject();
        if (StringUtil.isEmpty(proModelGzsmxx.getProModel().getYear())) {
            proModelGzsmxx.getProModel().setYear(commonService.getApplyYear(proModelGzsmxx.getProModel().getActYwId()));
        }
        js = commonService.onGcontestApply(proModelGzsmxx.getProModel().getId(), proModelGzsmxx.getProModel().getActYwId(), proModelGzsmxx.getProModel().getTeamId(), proModelGzsmxx.getProModel().getYear());
        if (StringUtils.isEmpty(proModelGzsmxx.getProModel().getTeamId())) {
            js.put("msg", "请选择团队信息！");
            js.put("ret", 0);
        }
        if ("0".equals(js.getString("ret"))) {
            return js;
        }
        js = proModelGzsmxxService.saveStep2(proModelGzsmxx);
        js.put("id", proModelGzsmxx.getId());
        js.put("proModelId", proModelGzsmxx.getProModel().getId());
        js.put("proCategory", proModelGzsmxx.getProModel().getProCategory());
        return js;
    }

    @RequestMapping(value = "ajaxSave3")
    @ResponseBody
    public JSONObject ajaxSave3(ProModelGzsmxx proModelGzsmxx, Model model, RedirectAttributes redirectAttributes) {
        JSONObject js = new JSONObject();
        ProModel proModel = proModelGzsmxx.getProModel();
        AttachMentEntity attachMentEntity = proModel.getAttachMentEntity();
        proModelGzsmxx.setAttachMentEntity(attachMentEntity);
        if(StringUtil.isEmpty(proModel.getYear())){
            proModel.setYear(commonService.getApplyYear(proModel.getActYwId()));
        }
        js = commonService.onGcontestApply(proModelGzsmxx.getProModel().getId(), proModelGzsmxx.getProModel().getActYwId(), proModelGzsmxx.getProModel().getTeamId(), proModelGzsmxx.getProModel().getYear());
        if (!"1".equals(js.getString("ret"))) {
            return js;
        }
        try {
            proModelGzsmxxService.uploadFile(proModelGzsmxx);
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            js.put("ret", 0);
            js.put("msg", "保存失败,系统异常请联系管理员");
            return js;
        }
        js.put("ret", 1);
        js.put("msg", "保存成功");
        return js;
    }

    //第三步提交
    @RequestMapping(value = "submit")
    @ResponseBody
    public JSONObject submit(ProModelGzsmxx proModelGzsmxx, Model model, RedirectAttributes redirectAttributes) {
        JSONObject js = new JSONObject();
        js.put("ret", 1);
        if (StringUtil.isEmpty(proModelGzsmxx.getProModel().getYear())) {
            proModelGzsmxx.getProModel().setYear(commonService.getApplyYear(proModelGzsmxx.getProModel().getActYwId()));
        }
        js = commonService.onProjectSubmitStep3(proModelGzsmxx.getProModel().getId(), proModelGzsmxx.getProModel().getActYwId(), proModelGzsmxx.getProModel().getProCategory(), proModelGzsmxx.getProModel().getTeamId(), proModelGzsmxx.getProModel().getYear());
        if ("0".equals(js.getString("ret"))) {
            return js;
        }
        try {
            proModelGzsmxxService.submit(proModelGzsmxx);
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            js.put("ret", 0);
            js.put("msg", "提交失败,系统异常请联系管理员");
            return js;
        }

        ActYw actYw = actYwService.get(proModelGzsmxx.getProModel().getActYwId());
        if (actYw != null) {
            js.put("pptype", actYw.getProProject().getType());
            js.put("proProjectId", actYw.getProProject().getId());
        }
        js.put("msg", "提交成功");
        js.put("actywId", proModelGzsmxx.getProModel().getActYwId());
        js.put("projectId", proModelGzsmxx.getProModel().getId());
        return js;
    }

    @RequestMapping(value = "ajaxWtparam")
    @ResponseBody
    public Wtparam ajaxWtparam() {
        return new Wtparam(IWparam.getFileTplPreFix(), Wtype.toJson());
    }

//	//下载word模板文档
//	@RequestMapping(value = "ajaxWord")
//	@ResponseBody
//	public Rstatus ajaxWord(String proId, String type, String vsn, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
//		//ProModelGzsmxx proModelGzsmxx = proModelGzsmxxService.get(proId);
//		ProModelGzsmxx proModelGzsmxx = proModelGzsmxxService.getByProModelId(proId);
//
//		if ((proModelGzsmxx == null) || StringUtil.isEmpty(proModelGzsmxx.getModelId())) {
//		  	return new Rstatus(false, "ProId 或 proModelGzsmxx 参数不能为空！");
//		}
//
//		ProModel proModel = proModelService.get(proModelGzsmxx.getModelId());
//		if ((proModel == null) || StringUtil.isEmpty(proModel.getTeamId())) {
//      		return new Rstatus(false, "ProModel Id 或 Team id 参数不能为空！");
//		}
//
//		String teamId = proModelGzsmxx.getProModel().getTeamId();
//		Team team = teamService.get(teamId);
//		List<BackTeacherExpansion> qytes = backTeacherExpansionService.getQYTeacher(teamId);
//		List<BackTeacherExpansion> xytes = backTeacherExpansionService.getXYTeacher(teamId);
//		List<StudentExpansion> tms = studentExpansionService.getStudentByTeamId(teamId);
//		if ((team == null) || ((qytes == null) && (xytes == null)) || (tms == null)) {
//      		return new Rstatus(false, "团队、导师参数不能为空！");
//		}
//
//		if (qytes == null) {
//		  qytes = Lists.newArrayList();
//		}
//
//		if (xytes == null) {
//		  xytes = Lists.newArrayList();
//		}
//
//		WproType wproType = WproType.getByKey(proModel.getProCategory());
//		if (wproType == null) {
//		  	return new Rstatus(false, "proCategory 项目类型未定义["+proModel.getProCategory()+"]");
//		}
//
////		IWparam wordParam = proModelGzsmxxService.initIWparam(type, vsn, proModel, proModelGzsmxx, team, xytes, qytes, tms);
////		if (wordParam != null) {
////			wordService.exeDownload(vsn, wordParam, request, response);
////			return null;
////		}
//		return new Rstatus(false, "模板下载失败！");
//	}

    @RequestMapping(value = "delete")
    public String delete(ProModelGzsmxx proModelGzsmxx, RedirectAttributes redirectAttributes) {
        proModelGzsmxxService.delete(proModelGzsmxx);
        addMessage(redirectAttributes, "删除proModelGzsmxx成功");
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/proprojectmd/proModelGzsmxx/?repage";
    }

}
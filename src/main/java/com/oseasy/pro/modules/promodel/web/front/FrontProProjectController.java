package com.oseasy.pro.modules.promodel.web.front;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.rediserver.common.utils.JedisUtils;
import com.oseasy.pro.jobs.pro.ProModelTopic;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwYear;
import com.oseasy.act.modules.actyw.exception.GroupErrorException;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.actyw.service.ActYwYearService;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowPcategoryType;
import com.oseasy.act.modules.actyw.vo.ActYwRuntimeException;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.AttachMentEntity;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.service.CommonService;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.entity.ProReport;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

import net.sf.json.JSONObject;

/**
 * 创建项目Controller.
 *
 * @author zhangyao
 * @version 2017-06-15
 */
@Controller
public class FrontProProjectController extends BaseController {

    @Autowired
    private ProModelService proModelService;
    @Autowired
    private ActYwService actYwService;
    @Autowired
    private ProjectDeclareService projectDeclareService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private SysAttachmentService sysAttachmentService;
    @Autowired
    private ActYwYearService actYwYearService;


    @RequestMapping(value = "${frontPath}/proproject/applyStep1")
    public String applyStep1(ProModel proModel, HttpServletRequest request, HttpServletResponse response, Model model) {
        User cuser = UserUtils.getUser();
        User leader = null;
        String parmact = null;
        if (StringUtil.isNotEmpty(proModel.getId())) {
            proModel = proModelService.get(proModel.getId());
            if (StringUtil.isEmpty(proModel.getDeclareId())
                    || StringUtil.isEmpty(cuser.getId()) || !cuser.getId().equals(proModel.getDeclareId())) {
                return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/promodel/proModel/viewForm?id=" + proModel.getId();
            }
            leader = UserUtils.get(proModel.getDeclareId());
            parmact = proModel.getActYwId();
        } else {
            proModel.setCreateDate(new Date());
            leader = cuser;
            parmact = request.getParameter("actywId");
        }
        proModel.setActYwId(parmact);
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
        model.addAttribute("proModel", proModel);
        model.addAttribute("leader", leader);
        model.addAttribute("cuser", cuser);
        return "template/formtheme/project/common_applyForm";
    }

    @RequestMapping(value = "${frontPath}/proproject/applyStep2")
    public String applyStep2(ProModel proModel, HttpServletRequest request, HttpServletResponse response, Model model) {
        User cuser = UserUtils.getUser();
        User leader =null;
        String parmact=null;
        if (StringUtil.isNotEmpty(proModel.getId())) {
            proModel =proModelService.get(proModel.getId());
            if (StringUtil.isEmpty(proModel.getDeclareId())
                    ||StringUtil.isEmpty(cuser.getId())||!cuser.getId().equals(proModel.getDeclareId())) {
                return CoreSval.REDIRECT+CoreSval.getFrontPath()+"/promodel/proModel/viewForm?id="+proModel.getId();
            }
            model.addAttribute("teamStu", projectDeclareService.findTeamStudentFromTUH(proModel.getTeamId(),proModel.getId()));
            model.addAttribute("teamTea", projectDeclareService.findTeamTeacherFromTUH(proModel.getTeamId(),proModel.getId()));
            leader=UserUtils.get(proModel.getDeclareId());
            parmact=proModel.getActYwId();
        }else{
            proModel.setCreateDate(new Date());
            leader=cuser;
            parmact=request.getParameter("actywId");
        }
        model.addAttribute("teams", projectDeclareService.findTeams(leader.getId(),proModel.getTeamId()));
        proModel.setActYwId(parmact);
        if (StringUtil.isNotEmpty(parmact)){
            ActYw actYw =actYwService.get(parmact);
            if (actYw!=null) {
                ProProject proProject=actYw.getProProject();
                if (proProject!=null&&ProSval.PRO_TYPE_PROJECT.equals(proProject.getProType())
                        &&StringUtil.isNotEmpty(proProject.getType())) {
                    model.addAttribute("proModelType", DictUtils.getDictLabel(proProject.getType(), "project_style", ""));
                }
            }
        }
        model.addAttribute("proModel", proModel);
        model.addAttribute("leader", leader);
        model.addAttribute("cuser", cuser);
        return "template/formtheme/project/common_applyStep2";
    }

    @RequestMapping(value = "${frontPath}/proproject/saveStep1")
    @ResponseBody
    public JSONObject saveStep1(ProModel proModel, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        JSONObject js = new JSONObject();
        if(StringUtil.isEmpty(proModel.getYear())){
        	proModel.setYear(commonService.getApplyYear(proModel.getActYwId()));
		}
        js = commonService.onProjectSaveStep1(proModel.getActYwId(), proModel.getId(),proModel.getYear());
        return js;
    }

    @RequestMapping(value="${frontPath}/proproject/saveProjectStep2/{isChecked}", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult saveProjectStep2(@PathVariable String isChecked, @RequestBody ProModel proModel){
        try {
            if(StringUtil.isEmpty(proModel.getYear())){
                proModel.setYear(commonService.getApplyYear(proModel.getActYwId()));
            }
            if(StringUtil.isEmpty(isChecked) && isChecked.equals("1")){
                JSONObject js = commonService.onProjectSaveStep2(proModel.getId(), proModel.getActYwId(), proModel.getProCategory(), proModel.getTeamId(),proModel.getYear());
                if(js.getString("ret").equals("0")){
                    return ApiResult.failed(ApiConst.CODE_OTHER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_OTHER_ERROR)+":"+js.getString("msg"));
                }
            }else {
                if (!StringUtil.isEmpty(proModel.getId())) {//修改
                    ProModel pm = proModelService.get(proModel.getId());
                    if (pm == null || "1".equals(pm.getDelFlag())) {
                        return ApiResult.failed(ApiConst.CODE_OTHER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_OTHER_ERROR)+":保存失败，项目已被删除");
                    }
                    if (Const.YES.equals(pm.getSubStatus())) {
                        return ApiResult.failed(ApiConst.CODE_OTHER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_OTHER_ERROR)+":保存失败，项目已被提交");
                    }
                }
            }

            ActYw actYw = actYwService.get(proModel.getActYwId());
            if (actYw != null) {
                ProProject proProject = actYw.getProProject();
                if (proProject != null) {
                    proModel.setProType(proProject.getProType());
                    proModel.setType(proProject.getType());
                    setYear(proModel);
                }

            }
            User cuser = UserUtils.getUser();
            proModel.setDeclareId(cuser.getId());
            proModelService.saveStep2(proModel);
            return ApiResult.success(proModel);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":保存失败,系统异常请联系管理员");
        }
    }


    @RequestMapping(value = "${frontPath}/proproject/saveStep2")
    @ResponseBody
    public JSONObject saveStep2(ProModel proModel, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        JSONObject js = new JSONObject();
        if(StringUtil.isEmpty(proModel.getYear())){
        	proModel.setYear(commonService.getApplyYear(proModel.getActYwId()));
		}
        js = commonService.onProjectSaveStep2(proModel.getId(), proModel.getActYwId(), proModel.getProCategory(), proModel.getTeamId(),proModel.getYear());
        if (!"1".equals(js.getString("ret"))) {
            return js;
        }
        try {
            ActYw actYw = actYwService.get(proModel.getActYwId());
            if (actYw != null) {
                ProProject proProject = actYw.getProProject();
                if (proProject != null) {
                    proModel.setProType(proProject.getProType());
                    proModel.setType(proProject.getType());
                    setYear(proModel);
                }

            }
            User cuser = UserUtils.getUser();
            proModel.setDeclareId(cuser.getId());
//            if (StringUtil.isEmpty(proModel.getCompetitionNumber())) {
//                proModel.setCompetitionNumber(IdUtils.getProjectNumberByDb());
//            }
            proModelService.saveStep2(proModel);
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            js.put("ret", 0);
            js.put("msg", "保存失败,系统异常请联系管理员");
            return js;
        }
        js.put("ret", 1);
        js.put("id", proModel.getId());
        js.put("msg", "保存成功");
        return js;
    }

    private void setYear(ProModel proModel) {
        List<ActYwYear> years = actYwYearService.findListByActywId(proModel.getActYwId());
        long now = System.currentTimeMillis();
        for (ActYwYear year : years) { //设置年份
            if(now >= year.getNodeStartDate().getTime() && now <= year.getNodeEndDate().getTime()){
                proModel.setYear(year.getYear());
                break;
            }
        }
    }

    @RequestMapping(value = "${frontPath}/proproject/saveStep2Uncheck")
    @ResponseBody
    public JSONObject saveStep2Uncheck(ProModel proModel, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        JSONObject js = new JSONObject();
        try {
            if (!StringUtil.isEmpty(proModel.getId())) {//修改
                ProModel pm = proModelService.get(proModel.getId());
                if (pm == null || "1".equals(pm.getDelFlag())) {
                    js.put("ret", 0);
                    js.put("msg", "保存失败，项目已被删除");
                    return js;
                }
                if (Const.YES.equals(pm.getSubStatus())) {
                    js.put("ret", 0);
                    js.put("msg", "保存失败，项目已被提交");
                    return js;
                }
            }
            ActYw actYw = actYwService.get(proModel.getActYwId());
            if (actYw != null) {
                ProProject proProject = actYw.getProProject();
                if (proProject != null) {
                    proModel.setProType(proProject.getProType());
                    proModel.setType(proProject.getType());
                    setYear(proModel);
                }

            }
            User cuser = UserUtils.getUser();
            proModel.setDeclareId(cuser.getId());
//            if (StringUtil.isEmpty(proModel.getCompetitionNumber())) {
//                proModel.setCompetitionNumber(IdUtils.getProjectNumberByDb());
//            }
            proModelService.saveStep2(proModel);
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            js.put("ret", 0);
            js.put("msg", "保存失败,系统异常请联系管理员");
            return js;
        }
        js.put("ret", 1);
        js.put("id", proModel.getId());
        js.put("msg", "保存成功");
        return js;
    }

    @RequestMapping(value = "${frontPath}/proproject/applyStep3")
    public String applyStep3(ProModel proModel, HttpServletRequest request, HttpServletResponse response, Model model) {
        User cuser = UserUtils.getUser();
        User leader = null;
        if (StringUtil.isNotEmpty(proModel.getId())) {
            proModel = proModelService.get(proModel.getId());
           /* proModel =(ProModel)JedisUtils.getObject("pro:com"+proModel.getId());*/
            if (StringUtil.isEmpty(proModel.getDeclareId())
                    || StringUtil.isEmpty(cuser.getId()) || !cuser.getId().equals(proModel.getDeclareId())) {
                return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/promodel/proModel/viewForm?id=" + proModel.getId();
            }
            leader = UserUtils.get(proModel.getDeclareId());
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
        model.addAttribute("proModel", proModel);
        model.addAttribute("leader", leader);
        model.addAttribute("cuser", cuser);
        return "template/formtheme/project/common_applyStep3";
    }

    @RequestMapping(value = "${frontPath}/proproject/saveStep3")
    @ResponseBody
    public JSONObject saveStep3(ProModel proModel, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        JSONObject js = new JSONObject();
        AttachMentEntity attachMentEntity = proModel.getProModel().getAttachMentEntity();
        proModel = proModelService.get(proModel.getId());
        proModel.setAttachMentEntity(attachMentEntity);
        if(StringUtil.isEmpty(proModel.getYear())){
        	proModel.setYear(commonService.getApplyYear(proModel.getActYwId()));
		}
        js = commonService.onProjectSaveStep2(proModel.getId(), proModel.getActYwId(), proModel.getProCategory(), proModel.getTeamId(),proModel.getYear());
        if (!"1".equals(js.getString("ret"))) {
            return js;
        }
        try {
            proModelService.saveStep3(proModel);
            JedisUtils.publishMsg(ProModelTopic.TOPIC_THREE,proModel);
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

    @RequestMapping(value = "${frontPath}/proproject/submitStep3")
    @ResponseBody
    public JSONObject submitStep3(ProModel proModel, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        JSONObject js = new JSONObject();
        AttachMentEntity attachMentEntity = proModel.getProModel().getAttachMentEntity();
        proModel = proModelService.get(proModel.getId());
        proModel.setAttachMentEntity(attachMentEntity);
        if(StringUtil.isEmpty(proModel.getYear())){
        	proModel.setYear(commonService.getApplyYear(proModel.getActYwId()));
		}
        js = commonService.onProjectSubmitStep3(proModel.getId(), proModel.getActYwId(), proModel.getProCategory(), proModel.getTeamId(),proModel.getYear());
        if (!"1".equals(js.getString("ret"))) {
            return js;
        }

        try {
            proModelService.submitStep3(proModel);
        }catch (GroupErrorException e) {
            js.put("ret", 0);
            js.put("msg", e.getMessage());
            return js;
        } catch (RuntimeException e) {
            js.put("ret", 0);
            js.put("msg", e.getMessage());
            return js;
         }catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            js.put("ret", 0);
            js.put("msg", "提交失败,系统异常请联系管理员");
            return js;
        }
        ActYw actYw = actYwService.get(proModel.getActYwId());
		if (actYw != null) {
			js.put("pptype", actYw.getProProject().getType());
			js.put("proProjectId", actYw.getProProject().getId());
		}
        js.put("actywId", proModel.getActYwId());
		js.put("projectId", proModel.getId());
        js.put("ret", 1);
        js.put("msg", "提交成功");
        return js;
    }

    @RequestMapping(value = "${frontPath}/proproject/saveCommonStep3/{isSubmit}", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult saveCommonStep3(@PathVariable String isSubmit, @RequestBody ProModel proModel){
        try {
            JSONObject js = new JSONObject();
            List<SysAttachment> sysAttachmentList = proModel.getFileInfo();
            proModel = proModelService.get(proModel.getId());
            proModel.setFileInfo(sysAttachmentList);
            if(isSubmit.equals("0")){
                //保存
                js = commonService.onProjectSaveStep2(proModel.getId(), proModel.getActYwId(), proModel.getProCategory(), proModel.getTeamId(),proModel.getYear());
            }else {
                //提交
                js = commonService.onProjectSubmitStep3(proModel.getId(), proModel.getActYwId(), proModel.getProCategory(), proModel.getTeamId(),proModel.getYear());
            }
            if(js.getString("ret").equals("0")){
                return ApiResult.failed(ApiConst.CODE_OTHER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_OTHER_ERROR)+":"+js.getString("msg"));
            }
            if(isSubmit.equals("0")){
                proModelService.saveStep3(proModel);
                JedisUtils.publishMsg(ProModelTopic.TOPIC_THREE,proModel);
            }else {
                proModelService.submitStep3(proModel);
            }
            return ApiResult.success(proModel);
        }catch (GroupErrorException e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_OTHER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_OTHER_ERROR)+":"+e.getMessage());
        } catch (RuntimeException e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_OTHER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_OTHER_ERROR)+":"+e.getMessage());
        }catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":提交失败,系统异常请联系管理员");
        }
    }

    /*下载模板*/
    @RequestMapping(value = "${frontPath}/proproject/downTemplate")
    public void downTemplate(HttpServletRequest request, HttpServletResponse response) {
        String type = request.getParameter("type");
        String rootpath = request.getSession().getServletContext().getRealPath("/");
        FileInputStream fs = null;
        OutputStream out = null;
        try {
            String fileName = null;
            String fileName2 = type + ".doc";
            File fi = new File(rootpath + File.separator + "static" + File.separator + "project-word"
                    + File.separator + fileName2);
            if (fi.exists()) {
                if ("mid".equals(type)) {
                    fileName = "大学生创新创业项目中期检查表.doc";
                } else if ("close".equals(type)) {
                    fileName = "大学生创新创业项目结项报告.doc";
                } else if ("modify".equals(type)) {
                    fileName = "大学生创新创业项目调整申请表.doc";
                } else if ("spec".equals(type)) {
                    fileName = "大学生创新创业项目免鉴定申请表.doc";
                } else {
                    fileName = DictUtils.getDictLabel(type, FlowPcategoryType.PCT_XM.getKey(), "") + "项目申请表.doc";
                }
            } else {
                if ("mid".equals(type)) {
                    fileName = "大学生创新创业项目中期检查表.docx";
                } else if ("close".equals(type)) {
                    fileName = "大学生创新创业项目结项报告.docx";
                } else if ("modify".equals(type)) {
                    fileName = "大学生创新创业项目调整申请表.docx";
                } else if ("spec".equals(type)) {
                    fileName = "大学生创新创业项目免鉴定申请表.docx";
                } else {
                    fileName = DictUtils.getDictLabel(type, FlowPcategoryType.PCT_XM.getKey(), "") + "项目申请表.docx";
                }
                fileName2 = type + ".docx";
                fi = new File(rootpath + File.separator + "static" + File.separator + "projectgt-word"
                        + File.separator + fileName2);
            }
            if (!fi.exists()) {
                return;
            }
            out = response.getOutputStream();
            String headStr = "attachment; filename=\"" + new String(fileName.getBytes(), "ISO-8859-1") + "\"";
            response.setContentType("APPLICATION/OCTET-STREAM");
            response.setHeader("Content-Disposition", headStr);

            fs = new FileInputStream(fi);
            out = response.getOutputStream();
            byte[] b = new byte[1024];
            int len;
            while ((len = fs.read(b)) > 0) {
                out.write(b, 0, len);
            }
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(fs);
        }
    }

    /**
     * 学生端提交报告（比如中期报告、结项报告）
     * @param proReport
     * @param model
     * @param request
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "${frontPath}/proproject/reportSubmit")
    @ResponseBody
    public JSONObject reportSubmit(ProReport proReport, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            String gnodeId=request.getParameter("gnodeId");
            return proModelService.reportSubmit(proReport,gnodeId);
        } catch (GroupErrorException e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            JSONObject js = new JSONObject();
            js.put("msg", "提交失败," + e.getCode());
            js.put("ret", 0);
            return js;
        }catch (ActYwRuntimeException e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            JSONObject js = new JSONObject();
            js.put("msg", "提交失败," + e.getMessage());
            js.put("ret", 0);
            return js;
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            JSONObject js = new JSONObject();
            js.put("msg", "提交失败,系统异常请联系管理员");
            js.put("ret", 0);
            return js;
        }
    }


    @RequestMapping(value = "${frontPath}/proproject/saveReport", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult saveReport(@RequestBody ProReport proReport){
        try {
            String gnodeId= proReport.getGnodeId();
            JSONObject js = proModelService.reportSubmit(proReport,gnodeId);
            if(js.getString("ret").equals("0")){
                return ApiResult.failed(ApiConst.CODE_OTHER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_OTHER_ERROR)+":"+js.getString("msg"));
            }
            return ApiResult.success(proReport);
        }catch (GroupErrorException e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_OTHER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_OTHER_ERROR)+":"+e.getCode());
        }catch (ActYwRuntimeException e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_OTHER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_OTHER_ERROR)+":"+e.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_OTHER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_OTHER_ERROR)+":保存失败,出现了未知的错误，请重试或者联系管理员");
        }
    }
}
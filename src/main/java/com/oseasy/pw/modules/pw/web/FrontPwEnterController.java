package com.oseasy.pw.modules.pw.web;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.fileserver.modules.vsftp.service.UeditorUploadService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.persistence.annotation.CheckToken;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
//import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pw.common.config.PwSval;
import com.oseasy.pw.common.config.PwSval.PwEmskey;
import com.oseasy.pw.modules.pw.entity.PwCompany;
import com.oseasy.pw.modules.pw.entity.PwEnter;
import com.oseasy.pw.modules.pw.entity.PwProject;
import com.oseasy.pw.modules.pw.service.PwEnterService;
import com.oseasy.pw.modules.pw.vo.DtypeTerm;
import com.oseasy.pw.modules.pw.vo.PwElistType;
import com.oseasy.pw.modules.pw.vo.PwEnterAtype;
import com.oseasy.pw.modules.pw.vo.PwEnterAuditEnum;
import com.oseasy.pw.modules.pw.vo.PwEnterStatus;
import com.oseasy.pw.modules.pw.vo.PwEnterType;
import com.oseasy.pw.modules.pw.vo.TempTypeEnum;
import com.oseasy.sys.common.utils.SysUserUtils;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 入驻申报Controller.
 *
 * @author chenh
 * @version 2017-11-26
 */
@Controller
@RequestMapping(value = "${frontPath}/pw/pwEnter")
public class FrontPwEnterController extends BaseController {

    @Autowired
    private PwEnterService pwEnterService;
    @Autowired
    private SysAttachmentService sysAttachmentService;
    @Autowired
    private UeditorUploadService ueditorUploadService;

    @ModelAttribute
    public PwEnter get(@RequestParam(required = false) String id) {
        PwEnter entity = null;
        if (StringUtil.isNotBlank(id)) {
            entity = pwEnterService.get(id);
        }
        if (entity == null) {
            entity = new PwEnter();
        }
        return entity;
    }

    @RequestMapping(value = "form")
    public String form(PwEnter pwEnter, Model model, HttpServletRequest request) {

        if (pwEnter == null) {
            pwEnter = new PwEnter();
        }
//

        pwEnter.setApplicant(UserUtils.getUser());
//

        if (SysUserUtils.checkInfoPerfect(UserUtils.getUser())) {
//            js.put("msg", "个人信息未完善,立即完善个人信息？");
//            js.put("ret", 2);
//            addBtnArrs(js, "确定", "/f/sys/frontStudentExpansion/findUserInfoById?custRedict=1&isEdit=1");
//            return js;
        }

        if (StringUtil.isEmpty(pwEnter.getApplicant().getId())) {
            return UserUtils.toLogin();
        }

        String changeEnterType = request.getParameter("changeEnterType");
//
//        if (StringUtil.isNotEmpty(pwEnter.getApplicant().getId()) && StringUtil.isEmpty(pwEnter.getId())) {
//            pwEnter.setIsTemp(CoreSval.YES);
//            List<PwEnter> pwEnters = pwEnterService.findList(pwEnter);
//            if ((pwEnters != null) && (pwEnters.size() >= SvalPw.getEnterApplyMaxNum())) {
//                model.addAttribute(SvalPw.IS_MAX, true);
//                model.addAttribute(SvalPw.MAXNUM, SvalPw.getEnterApplyMaxNum());
//            } else {
//                model.addAttribute(SvalPw.IS_MAX, false);
//            }
//            model.addAttribute("pwEnters", pwEnters);
//        }
        model.addAttribute("changeEnterType", changeEnterType);
        model.addAttribute("pwEnter", pwEnter);
        return PwSval.path.vms(PwEmskey.PW.k()) + "frontPwEnterForm";
    }

    @CheckToken(value = Const.NO)
    @RequestMapping(value = "view")
    public String view(PwEnter pwEnter , Model model) {
        if(UserUtils.getUser() == null){
            return UserUtils.toLogin();
        }
        if(pwEnter == null){
            return CoreSval.REDIRECT+CoreSval.getFrontPath()+"/pw/pwEnterRel/list";
        }
        model.addAttribute("pwEnter", pwEnter);
        model.addAttribute("pwEnterId", pwEnter.getId());
        model.addAttribute("type", pwEnter.getType());
//    model.addAttribute("pwEnterRel", pwEnterRel);
        return PwSval.path.vms(PwEmskey.PW.k()) + "frontPwEnterRelView";
    }

    @RequestMapping(value="getPwEnterTypes", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List getPwEnterTypes(){
        List<HashMap> list = Lists.newArrayList();
        for(PwEnterType pwEnterType: PwEnterType.values()){
            if(!pwEnterType.isEnable()){
                continue;
            }
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("label", pwEnterType.getName());
            hashMap.put("value", pwEnterType.getKey());
            list.add(hashMap);
        }
        return list;
    }

    @RequestMapping(value = "formStep2")
    public String formStep2(PwEnter pwEnter, Model model) {
        model.addAttribute("pwEnter", pwEnter);
//        PwEnterVo pwEnterVo = PwEnterVo.convert(pwEnter);
//
//        /**
//         * 判断页面是否可以点保存.
//         */
//        if (StringUtil.isNotEmpty(pwEnter.getId())) {
//            ActYwApply actYwApply = new ActYwApply();
//            actYwApply.setApplyUser(UserUtils.getUser());
//            actYwApply.setRelId(pwEnter.getId());
//
//            if (UserUtils.getUser() == null) {
//                return UserUtils.toLogin();
//            }
//
//            List<ActYwApply> actYwApplys = actYwApplyService.findList(actYwApply);
//            pwEnterVo.setIsSave((actYwApplys != null) && (actYwApplys.size() > 0));
//        } else {
//            pwEnterVo.setIsSave(false);
//        }
//
//        if ((pwEnter.getCursel() == null)) {
//          pwEnterVo.setCursel(PwEnter.CS_COMPANY);
//        }
//        model.addAttribute("pwEnterVo", pwEnterVo);
//        if ((pwEnter == null) || StringUtil.isEmpty(pwEnter.getId())) {
//            return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/pw/pwEnter/form?repage";
//        }
        return PwSval.path.vms(PwEmskey.PW.k()) + "frontPwEnterFormStep2";
    }

    @RequiresPermissions("pw:pwEnter:edit")
    @RequestMapping(value = "save")
    public String save(PwEnter pwEnter, Model model, RedirectAttributes redirectAttributes) {
//        if (!beanValidator(model, pwEnter)) {
//            return form(pwEnter, model);
//        }
//        pwEnterService.save(pwEnter);
//        addMessage(redirectAttributes, "保存入驻申报成功");
        return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/pw/pwEnter/?repage";
    }

    @RequestMapping(value = "ajaxDelete")
    public String ajaxDelete(PwEnter pwEnter, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        pwEnterService.deleteWLEnter(pwEnter.getId(), request, response);
        addMessage(redirectAttributes, "删除入驻申报成功");
        return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/pw/pwEnter/form";
    }

    /**
     * 保存入驻基本信息.
     *
     * @param uid  用户ID
     * @param term 周期
     * @return ActYwRstatus 结果状态
     */
    @ResponseBody
    @RequestMapping(value = "ajaxSave/{uid}")
    public ApiTstatus<PwEnter> ajaxSave(@PathVariable(value = "uid") String uid, @RequestParam(required = false) String id, @RequestParam(required = false) Integer term) {
        if (StringUtil.isEmpty(uid)) {
            return new ApiTstatus<PwEnter>(false, "入驻失败, 申请人不能为空！");
        }
        if ((term == null)) {
            return new ApiTstatus<PwEnter>(false, "入驻失败, 申请周期不能为空！");
        }
        PwEnter pwEnter = null;
        if (StringUtil.isNotEmpty(id)) {
            pwEnter = get(id);
        }

        if (pwEnter == null) {
            pwEnter = new PwEnter();
            pwEnter.setStatus(PwEnterStatus.PES_DSH.getKey());
        }
        pwEnter.setApplicant(new User(uid));
        Integer addTerm = DtypeTerm.addDayByType(term, DateUtil.getCurDateYMD000());
        if (addTerm == null) {
            addTerm = 0;
        }
        pwEnter.setTerm(addTerm);

        pwEnterService.save(pwEnter);
        return new ApiTstatus<PwEnter>(true, "请求成功", pwEnter);
    }

    /**
     * 保存入驻基本信息.
     *
     * @return ActYwRstatus 结果状态
     */
    @ResponseBody
    @RequestMapping(value = "moveAttachment")
    public ApiTstatus<List<SysAttachment>> moveAttachment(@RequestParam(required = false) String uId) {
        if (StringUtil.isNotEmpty(uId)) {
            SysAttachment sysAttachment = new SysAttachment();
            sysAttachment.setUid(uId);
            List<SysAttachment> sysAttachmentList = sysAttachmentService.getFiles(sysAttachment);
            JSONArray jsonArray = new JSONArray();
            if (sysAttachmentList != null && sysAttachmentList.size() > 0) {
                for (SysAttachment sy : sysAttachmentList) {
                    JSONObject bl = ueditorUploadService.copyFile(sy);
                    jsonArray.add(bl);
                }
            }
            return new ApiTstatus(true, "", jsonArray);
        } else {
            return new ApiTstatus(false, "请选择项目");
        }
    }

    /**
     * 获取入驻.
     * @param id 入驻审核ID
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxPwEnter", method = RequestMethod.GET)
    public ApiResult ajaxPwEnter(@RequestParam String id, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if(StringUtil.isEmpty(id)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败！");
            }
            PwEnter pwEnter = pwEnterService.get(new PwEnter(id));
            pwEnter.setApplicant(UserUtils.get(pwEnter.getDeclareId()));
            return ApiResult.success(pwEnter);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    //获得入驻申请团队接口
    @RequestMapping(value = "ajaxFindPwEnterTeam" , method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxFindPwEnterTeam(String pwEnterId ,HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String,Object> map= pwEnterService.findPwEnterTeamById(pwEnterId);
            return ApiResult.success(map);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }


    @ResponseBody
    @RequestMapping(value = "ajaxRoom", method = RequestMethod.GET)
    public ApiResult ajaxRoom(@RequestParam String pwEnterId, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if(StringUtil.isEmpty(pwEnterId)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败！");
            }
            PwEnter rpwEnter = pwEnterService.getRoom(new PwEnter(pwEnterId));
            if((rpwEnter == null) || StringUtil.checkEmpty(rpwEnter.getErooms())){
                return ApiResult.failed(ApiConst.CODE_NULL_ERROR, ApiConst.getErrMsg(ApiConst.CODE_NULL_ERROR));
            }
            return ApiResult.success(rpwEnter.getErooms());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }


    //获得入驻申请期望接口
    @RequestMapping(value = "ajaxFindPwEnterPwSpace" , method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxFindPwEnterPwSpace(String pwEnterId ,HttpServletRequest request, HttpServletResponse response) {
        try {
            PwEnter pwEnter= pwEnterService.get(pwEnterId);
            return ApiResult.success(pwEnter);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    //获得入驻申请企业接口
    @RequestMapping(value = "ajaxFindPwEnterCompany" , method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxFindPwEnterCompany(String pwEnterId ,HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String,PwCompany> map= pwEnterService.findPwEnterCompanyById(pwEnterId);
            return ApiResult.success(map);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    //获得入驻申请项目接口
    @RequestMapping(value = "ajaxFindPwEnterProjects" , method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxFindPwEnterProjects(String pwEnterId ,HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String,List<PwProject>> map= pwEnterService.findPwEnterProjectsById(pwEnterId);
            return ApiResult.success(map);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    //获得入驻申请成果物接口
    @RequestMapping(value = "ajaxFindPwEnterResultList" , method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxFindPwEnterResultList(String pwEnterId ,HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String,List<SysAttachment>> map= pwEnterService.findPwEnterResultList(pwEnterId);
            return ApiResult.success(map);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }


    //获得申请团队接口
    @RequestMapping(value = "ajaxGetTeams" , method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxGetTeams(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<Team>teams=pwEnterService.getTeams();
            return ApiResult.success(teams);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    //获得关联审核枚举接口
    @RequestMapping(value = "ajaxGetPwEnterAudit" , method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxGetPwEnterAudit(HttpServletRequest request, HttpServletResponse response) {
        try {
            return ApiResult.success(PwEnterAuditEnum.getByKeys());
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    //获得关联审核枚举接口
    @RequestMapping(value = "getPwEnterAuditList" , method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public List<Object> getPwEnterAuditList() {
        return PwEnterAuditEnum.getPwEnterAuditList();
    }


    //保存
    @CheckToken(value = Const.YES)
    @RequestMapping(value = "ajaxPwEnterApplySave" , method = RequestMethod.POST, produces = "application/json;charset=utf-8")
   	@ResponseBody
   	public ApiResult ajaxPwEnterApplySave(@RequestBody PwEnter pwEnter, HttpServletRequest request, HttpServletResponse response) {
        boolean isValite=true;
        try {
            if(TempTypeEnum.R1.getValue().equals(pwEnter.getIsTemp())){
                isValite=pwEnterService.checkPwEnterTeam(pwEnter);
                if(isValite){
                    return ApiResult.failed(ApiConst.CODE_MORE_ERROR,ApiConst.getErrMsg(ApiConst.CODE_MORE_ERROR)+":团队重复");
                }
                if(PwEnterType.PET_QY.getKey().equals(pwEnter.getType())){
                    isValite=pwEnterService.checkPwEnterPwCompanyHas(pwEnter);
                    if(isValite){
                        return ApiResult.failed(ApiConst.CODE_MORE_ERROR,ApiConst.getErrMsg(ApiConst.CODE_MORE_ERROR)+":企业重复");
                    }
                }
                isValite=pwEnterService.checkPwEnterChange(pwEnter.getId());
                if(isValite){
                    return ApiResult.failed(ApiConst.CODE_MORE_ERROR,"已经发起申请,不能发起多次申请");
                }

                if(StringUtil.isEmpty(pwEnter.getTeam().getId())){
                    return ApiResult.failed(ApiConst.CODE_NULL_ERROR,ApiConst.getErrMsg(ApiConst.CODE_NULL_ERROR)+":数据为空");
                }
            }
            //pwEnterService.savePwEnterApply(pwEnter);
            return pwEnterService.savePwEnterApply(pwEnter);
   		}catch (Exception e){
   			logger.error(ExceptionUtil.getStackTrace(e));
   			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
   		}
   	}

   	@RequestMapping(value="checkPwEnterTeam", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Boolean checkPwEnterTeam(@RequestBody PwEnter pwEnter){
        return !pwEnterService.checkPwEnterTeam(pwEnter);
    }

    @RequestMapping(value="checkPwEnterCompany", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Boolean checkPwEnterCompany(@RequestBody PwEnter pwEnter){
        return !pwEnterService.checkPwEnterPwCompanyHas(pwEnter);
    }

    @RequestMapping(value="checkPwEnterCompanyName", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Boolean checkPwEnterCompanyName(@RequestBody PwEnter pwEnter){
        return !pwEnterService.checkPwEnterPwCompanyName(pwEnter);
    }

    @RequestMapping(value="checkPwEnterCompanyNo", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Boolean checkPwEnterCompanyNo(@RequestBody PwEnter pwEnter){
        return !pwEnterService.checkPwEnterPwCompanyNo(pwEnter);
    }


    //存在给false
    @RequestMapping(value="checkPwEnterProject", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Boolean checkPwEnterProject(@RequestBody PwProject pwProject){
        return pwEnterService.checkPwEnterProject(pwProject);
    }


    /**
     * 获取所有入驻审核状态.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxPwEnterStatus", method = RequestMethod.GET)
    public ApiResult ajaxPwEnterStatus(String type) {
        try {
            if((PwElistType.ET_FPCD).equals(type)){
                return ApiResult.success(Arrays.asList(PwEnterStatus.getKeysByFPCD()).toString());
            }else if((PwElistType.ET_XQ).equals(type)){
                return ApiResult.success(Arrays.asList(PwEnterStatus.getKeysByXQRZ()).toString());
            }else if((PwElistType.ET_QX).equals(type)){
                return ApiResult.success(Arrays.asList(PwEnterStatus.getKeysByQXRZ()).toString());
            }else if((PwElistType.ET_QUERY).equals(type)){
                return ApiResult.success(Arrays.asList(PwEnterStatus.getKeysByQuery()).toString());
            }
            return ApiResult.success(Arrays.asList(PwEnterStatus.values()).toString());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    //保存成果物
    @RequestMapping(value = "ajaxPwEnterResultApply" , method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxPwEnterResultApply(@RequestBody PwEnter pwEnter, HttpServletRequest request, HttpServletResponse response) {
        try {
            pwEnterService.savePwEnterResultApply(pwEnter);
            return ApiResult.success();
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value = "ajaxGetPwEnterById" , method = RequestMethod.POST, produces = "application/json;charset=utf-8")
   	@ResponseBody
   	public ApiResult ajaxGetPwEnterById(String id, HttpServletRequest request, HttpServletResponse response) {
   		try {
            PwEnter pwEnter=pwEnterService.getPwEnterById(id);
            return ApiResult.success(pwEnter);
   		}catch (Exception e){
   			logger.error(ExceptionUtil.getStackTrace(e));
   			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
   		}
   	}

    @CheckToken(value = Const.YES)
    @RequestMapping(value = "ajaxDelPwEnterApply" , method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxDelPwEnterApply(String id, HttpServletRequest request, HttpServletResponse response) {
        try {
            pwEnterService.delPwEnterAndDetail(id);
            return ApiResult.success();
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    //续期申请
    @RequestMapping(value = "ajaxPwEnterRenewalApply" , method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxPwEnterRenewalApply(String id,Integer term, HttpServletRequest request, HttpServletResponse response) {
        try {
            boolean isChangeHas=pwEnterService.checkPwEnterChange(id);
            if(isChangeHas){
                return ApiResult.failed(ApiConst.CODE_MORE_ERROR,"已经发起申请,不能发起多次申请");
            }
            pwEnterService.savePwEnterRenewalApply(id,term);
            return ApiResult.success();
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    //退孵申请
    @RequestMapping(value = "ajaxPwEnterHatchApply" , method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxPwEnterHatchApply(String pwEnterId, HttpServletRequest request, HttpServletResponse response) {
        try {
            boolean isChangeHas=pwEnterService.checkPwEnterChange(pwEnterId);
            if(isChangeHas){
                return ApiResult.failed(ApiConst.CODE_MORE_ERROR,"已经发起申请,不能发起多次申请");
            }
            pwEnterService.savePwEnterHatchApply(pwEnterId);
            return ApiResult.success();
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value = "ajaxCheckPwEnter" , method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxCheckPwEnter(String id, HttpServletRequest request, HttpServletResponse response) {
        try {
            PwEnter pwEnter=get(id);
//            //检查一个团队是否多次入驻
//            boolean isTeamHas=pwEnterService.checkPwEnterTeam(pwEnter);
//            if(isTeamHas){
//                return ApiResult.failed(ApiConst.CODE_MORE_ERROR,ApiConst.getErrMsg(ApiConst.CODE_MORE_ERROR)+":团队重复");
//            }
            //检查一个团队是否发起申请
            boolean isChangeHas=pwEnterService.checkPwEnterChange(id);
            if(isChangeHas){
                return ApiResult.failed(ApiConst.CODE_MORE_ERROR,"已经发起申请,不能发起多次申请");
            }
            return ApiResult.success();
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @CheckToken(value = Const.YES)
    @ResponseBody
    @RequestMapping(value = "ajaxList")
    public ApiResult ajaxlist(PwEnter pwEnter, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            Page<PwEnter> pag = new Page<PwEnter>(request, response);
            if (StringUtil.isEmpty(pag.getOrderBy())) {
                pag.setOrderBy(PwEnter.TABLEA + DataEntity.CREATE_DATE + StringUtil.KGE + Page.ORDER_DESC + StringUtil.DOTH + StringUtil.KGE + PwEnter.TABLEA + PwEnter.TYPE);
                pag.setOrderByType(Page.ORDER_ASC);
            }
            pwEnter.setIsCopy(Const.NO);
            pwEnter.setAppType(PwEnterAtype.PAT_DEFAULT.getKey());
            pwEnter.setApplicant(UserUtils.getUser());
            Page<PwEnter> page = pwEnterService.findFrontPageByGroup(pag, pwEnter);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 获取所有入驻期限类型.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxDtypeTerms", method = RequestMethod.GET)
    public ApiResult ajaxDtypeTerms(@RequestParam Boolean isAll) {
        try {
            if(isAll == null){
                isAll = false;
            }
            if(isAll){
                return ApiResult.success(DtypeTerm.getAll().toString());
            }else{
                return ApiResult.success(DtypeTerm.getAll().toString());
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

}
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.persistence.annotation.CheckToken;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pw.common.config.PwSval;
import com.oseasy.pw.common.config.PwSval.PwEmskey;
import com.oseasy.pw.modules.pw.entity.PwApplyRecord;
import com.oseasy.pw.modules.pw.entity.PwCompany;
import com.oseasy.pw.modules.pw.entity.PwEnter;
import com.oseasy.pw.modules.pw.entity.PwProject;
import com.oseasy.pw.modules.pw.service.PwApplyRecordService;
import com.oseasy.pw.modules.pw.service.PwEnterService;
import com.oseasy.pw.modules.pw.vo.DtypeTerm;
import com.oseasy.pw.modules.pw.vo.PwElistType;
import com.oseasy.pw.modules.pw.vo.PwEnterAtype;
import com.oseasy.pw.modules.pw.vo.PwEnterAuditEnum;
import com.oseasy.pw.modules.pw.vo.PwEnterBgremarks;
import com.oseasy.pw.modules.pw.vo.PwEnterRemarks;
import com.oseasy.pw.modules.pw.vo.PwEnterStatus;
import com.oseasy.pw.modules.pw.vo.PwEnterType;
import com.oseasy.pw.modules.pw.vo.PwEroomStatus;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

/**
 * 入驻申报Controller.
 *
 * @author chenh
 * @version 2017-11-26
 */
@Controller
@RequestMapping(value = "${adminPath}/pw/pwEnter")
public class PwEnterController extends BaseController {
	public static final String pwEnterEdit="pw:pwEnter:edit";
	@Autowired
	private PwEnterService pwEnterService;
	@Autowired
	private PwApplyRecordService pwApplyRecordService;

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

    /**
     * 入驻审核-提醒完善资料.
     * @param pwEnter 审核ID
     * @return
     */
    @RequestMapping(value = "ajaxSendNotify")
    public String ajaxSendNotify(PwEnter pwEnter, String type, RedirectAttributes redirectAttributes) {
        ApiTstatus<PwEnter> rstatus = pwEnterService.sendMsg(pwEnter.getId(), UserUtils.getUser(), type);
        addMessage(redirectAttributes, rstatus.getMsg());
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/pw/pwEnter/list?repage";
    }

    /*******************************************************************************
     ***已处理的方法******************************************************************
     ******************************************************************************/

    @RequiresPermissions("pw:pwEnter:view")
    @RequestMapping(value = { "list", "" })
    public String list(PwEnter pwEnter, HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute(PwEnter.STATUS, pwEnter.getType());
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwEnterList";
    }

    @RequiresPermissions("pw:pwEnter:view")
    @RequestMapping(value = { "listBGSH"})
    public String listBg(PwEnter pwEnter, HttpServletRequest request, HttpServletResponse response, Model model) {
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwEnterListBGSH";
    }

    @RequiresPermissions("pw:pwEnter:view")
    @RequestMapping(value = { "listFPCD" })
    public String listFPCD(PwEnter pwEnter, HttpServletRequest request, HttpServletResponse response, Model model) {
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwEnterListFPCD";
    }

    @RequiresPermissions("pw:pwEnter:view")
    @RequestMapping(value = { "listXQRZ" })
    public String listXQRZ(PwEnter pwEnter, HttpServletRequest request, HttpServletResponse response, Model model) {
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwEnterListXQRZ";
    }
    @RequiresPermissions("pw:pwEnter:view")
    @RequestMapping(value = { "listXQRZTeam" })
    public String listXQRZTeam(PwEnter pwEnter, HttpServletRequest request, HttpServletResponse response, Model model) {
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwEnterListXQRZTeam";
    }
    @RequiresPermissions("pw:pwEnter:view")
    @RequestMapping(value = { "listXQRZCompany" })
    public String listXQRZCompany(PwEnter pwEnter, HttpServletRequest request, HttpServletResponse response, Model model) {
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwEnterListXQRZCompany";
    }

    @RequiresPermissions("pw:pwEnter:view")
    @RequestMapping(value = { "listQXRZ" })
    public String listQXRZ(PwEnter pwEnter, HttpServletRequest request, HttpServletResponse response, Model model) {
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwEnterListQXRZ";
    }
    @RequiresPermissions("pw:pwEnter:view")
    @RequestMapping(value = { "listQXRZTeam" })
    public String listQXRZTeam(PwEnter pwEnter, HttpServletRequest request, HttpServletResponse response, Model model) {
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwEnterListQXRZTeam";
    }

    @RequiresPermissions("pw:pwEnter:view")
    @RequestMapping(value = { "listQXRZCompany" })
    public String listQXRZCompany(PwEnter pwEnter, HttpServletRequest request, HttpServletResponse response, Model model) {
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwEnterListQXRZCompany";
    }

    @RequiresPermissions("pw:pwEnter:view")
    @RequestMapping(value = { "listQueryCompany" })
    public String listQueryCompany(PwEnter pwEnter, HttpServletRequest request, HttpServletResponse response, Model model) {
        if(StringUtil.isNotEmpty(pwEnter.getType())){
            model.addAttribute(PwEnter.TYPE, pwEnter.getType());
        }
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwEnterListQueryCompany";
    }

    @RequiresPermissions("pw:pwEnter:view")
    @RequestMapping(value = { "listQueryTeam" })
    public String listQueryTeam(PwEnter pwEnter, HttpServletRequest request, HttpServletResponse response, Model model) {
        if(StringUtil.isNotEmpty(pwEnter.getType())){
            model.addAttribute(PwEnter.TYPE, pwEnter.getType());
        }
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwEnterListQueryTeam";
    }

    @RequiresPermissions("pw:pwEnter:view")
    @RequestMapping(value = "form")
    public String form(String id, String type, Model model, HttpServletRequest request) {
        model.addAttribute("pwEnterId", id);
        model.addAttribute(PwEnter.TYPE, type);
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwEnterForm";
    }

    @RequiresPermissions("pw:pwEnter:view")
    @RequestMapping(value = "view")
    public String view(PwEnter pwEnter, Model model, HttpServletRequest request) {
        model.addAttribute("pwEnter", pwEnter);
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwEnterView";
    }

    @RequiresPermissions("pw:pwEnter:view")
    @RequestMapping(value = "formBgsh")
    public String formBgsh(PwEnter pwEnter, Model model, HttpServletRequest request) {
        model.addAttribute("pwEnter", pwEnter);
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwEnterFormBgsh";
    }

    @RequiresPermissions("pw:pwEnter:view")
    @RequestMapping(value = "formBg")
    public String formBg(PwEnter pwEnter, Model model, HttpServletRequest request) {
        model.addAttribute("pwEnter", pwEnter);
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwEnterFormBg";
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

    /**
     * 获取入驻审核列表.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxList")
    public ApiResult ajaxlist(PwEnter pwEnter, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            Page<PwEnter> pag = new Page<PwEnter>(request, response);
            if (StringUtil.isEmpty(pag.getOrderBy())) {
                pag.setOrderBy(PwEnter.TABLEA + DataEntity.CREATE_DATE + StringUtil.KGE + Page.ORDER_DESC + StringUtil.DOTH + StringUtil.KGE + PwEnter.TABLEA + PwEnter.TYPE);
                pag.setOrderByType(Page.ORDER_ASC);
            }
            pwEnter.setIsTemp(Const.NO);
            pwEnter.setIsCopy(Const.NO);
            pwEnter.setIsShow(Const.YES);
            pwEnter.setAppType(PwEnterAtype.PAT_DEFAULT.getKey());
            pwEnter.setStatus(PwEnterStatus.PES_DSH.getKey());
            Page<PwEnter> page = pwEnterService.findPageByGroup(pag, pwEnter);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }


    /**
     * 获取入驻变更审核列表.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxListBGSH")
    public ApiResult ajaxList(PwEnter pwEnter, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {

            Page<PwEnter> pag = new Page<PwEnter>(request, response);
            if (StringUtil.isEmpty(pag.getOrderBy())) {
                pag.setOrderBy(PwEnter.TABLEA + DataEntity.CREATE_DATE + StringUtil.KGE + Page.ORDER_DESC + StringUtil.DOTH + StringUtil.KGE + PwEnter.TABLEA + PwEnter.TYPE);
                pag.setOrderByType(Page.ORDER_ASC);
            }
            pwEnter.setIsTemp(Const.NO);
            pwEnter.setIsCopy(Const.YES);
            pwEnter.setAppType(PwEnterAtype.PAT_BGSQ.getKey());
            pwEnter.setStatus(PwEnterStatus.PES_DSH.getKey());
            Page<PwEnter> page = pwEnterService.findPageBgsqByGroup(pag, pwEnter);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 获取入驻场地分配列表.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxListFPCD")
    public ApiResult ajaxListFPCD(PwEnter pwEnter, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            Page<PwEnter> pag = new Page<PwEnter>(request, response);
            if (StringUtil.isEmpty(pag.getOrderBy())) {
                pag.setOrderBy(PwEnter.TABLEA + DataEntity.CREATE_DATE + StringUtil.KGE + Page.ORDER_DESC + StringUtil.DOTH + StringUtil.KGE + PwEnter.TABLEA + PwEnter.TYPE);
                pag.setOrderByType(Page.ORDER_ASC);
            }
            pwEnter.setIsTemp(Const.NO);
            pwEnter.setIsCopy(Const.NO);
            pwEnter.setIsShow(Const.YES);
            pwEnter.setAppType(PwEnterAtype.PAT_DEFAULT.getKey());
            //pwEnter.setStatus(null);
            pwEnter.setPstatus(PwEnterStatus.getKeyByFPCD());
            return ApiResult.success(pwEnterService.findPageFpcdByGroup(pag, pwEnter));
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 获取入驻续期列表.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxListXQRZ")
    public ApiResult ajaxListXQRZ(PwEnter pwEnter, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            Page<PwEnter> pag = new Page<PwEnter>(request, response);
            if (StringUtil.isEmpty(pag.getOrderBy())) {
                pag.setOrderBy(PwEnter.TABLEA + PwEnter.END_DATE + StringUtil.KGE + Page.ORDER_DESC + StringUtil.DOTH + PwEnter.TABLEA + DataEntity.CREATE_DATE + StringUtil.KGE + Page.ORDER_DESC + StringUtil.DOTH + StringUtil.KGE + PwEnter.TABLEA + PwEnter.TYPE);
                pag.setOrderByType(Page.ORDER_ASC);
            }
            pwEnter.setIsTemp(Const.NO);
            pwEnter.setIsCopy(Const.NO);
            pwEnter.setAppType(PwEnterAtype.PAT_DEFAULT.getKey());
            pwEnter.setPstatus(PwEnterStatus.getKeyByXQRZ());
            return ApiResult.success(pwEnterService.findPageXqrzByGroup(pag, pwEnter));
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 获取入驻续期列表-申请记录.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxListXQRZRecord")
    public ApiResult ajaxListXQRZRecord(PwEnter pwEnter, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            Page<PwEnter> pag = new Page<PwEnter>(request, response);
            if (StringUtil.isEmpty(pwEnter.getIsCopy())) {
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败！");
            }

            if (StringUtil.isEmpty(pag.getOrderBy())) {
                pag.setOrderBy(PwEnter.TABLEAYR + DataEntity.CREATE_DATE + StringUtil.KGE);
                pag.setOrderByType(Page.ORDER_DESC);
            }

            pwEnter.setIsTemp(Const.NO);
            pwEnter.setIsCopy(Const.NO);
            pwEnter.setAppType(PwEnterAtype.PAT_DEFAULT.getKey());
            pwEnter.setPstatus(PwEnterStatus.getKeyByXQRZ());
            if(pwEnter.getApplyRecord() == null){
                pwEnter.setApplyRecord(new PwApplyRecord());
            }
            pwEnter.getApplyRecord().setType(PwEnterBgremarks.R3.getKey());
            pwEnter.getApplyRecord().setStatus(PwEnterAuditEnum.DSH.getValue());
            return ApiResult.success(pwEnterService.findPageXqrzRecoredByGroup(pag, pwEnter, true));
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 获取入驻退孵列表.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxListQXRZ")
    public ApiResult ajaxListQXRZ(PwEnter pwEnter, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            pwEnter.setPstatus(PwEnterStatus.getKeyByQXRZ());
            Page<PwEnter> pag = new Page<PwEnter>(request, response);
            Boolean isDefault = false;
            if (StringUtil.isEmpty(pag.getOrderBy())) {
                isDefault = true;
                pag.setOrderBy(PwEnter.TABLEA + DataEntity.CREATE_DATE + StringUtil.KGE + Page.ORDER_DESC + StringUtil.DOTH + StringUtil.KGE + PwEnter.TABLEA + PwEnter.TYPE);
                pag.setOrderByType(Page.ORDER_DESC);
            }

            pwEnter.setIsTemp(Const.NO);
            pwEnter.setIsCopy(Const.NO);
            pwEnter.setAppType(PwEnterAtype.PAT_DEFAULT.getKey());
            pwEnter.setPstatus(PwEnterStatus.getKeyByQXRZ());
            Page<PwEnter> page = pwEnterService.findPageQxrzByGroup(pag, pwEnter);
            if (isDefault && StringUtil.checkNotEmpty(pag.getList())) {
                List<PwEnter> ydqlist = Lists.newArrayList();
                List<PwEnter> jjdqlist = Lists.newArrayList();
                List<PwEnter> rzcglist = Lists.newArrayList();
                List<PwEnter> ytflist = Lists.newArrayList();
                List<PwEnter> deflist = Lists.newArrayList();
                for (PwEnter penter : page.getList()) {
                    if((PwEnterStatus.PES_YDQ.getKey()).equals(penter.getStatus())){
                        ydqlist.add(penter);
                    }else if((PwEnterStatus.PES_DXQ.getKey()).equals(penter.getStatus())){
                        jjdqlist.add(penter);
                    }else if((PwEnterStatus.PES_RZCG.getKey()).equals(penter.getStatus())){
                        rzcglist.add(penter);
                    }else if((PwEnterStatus.PES_YTF.getKey()).equals(penter.getStatus())){
                        ytflist.add(penter);
                    }else{
                        deflist.add(penter);
                    }
                }
                deflist.addAll(ydqlist);
                deflist.addAll(jjdqlist);
                deflist.addAll(rzcglist);
                deflist.addAll(ytflist);
                page.setList(deflist);
            }
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 获取入驻退孵列表.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxListQXRZRecord")
    public ApiResult ajaxListQXRZRecord(PwEnter pwEnter, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            pwEnter.setPstatus(PwEnterStatus.getKeyByQXRZ());
            Page<PwEnter> pag = new Page<PwEnter>(request, response);
            Boolean isDefault = false;
            if (StringUtil.isEmpty(pag.getOrderBy())) {
                isDefault = true;
                pag.setOrderBy(PwEnter.TABLEAYR + DataEntity.CREATE_DATE + StringUtil.KGE);
                pag.setOrderByType(Page.ORDER_DESC);
            }

            pwEnter.setIsTemp(Const.NO);
            if(pwEnter.getApplyRecord() == null){
                pwEnter.setApplyRecord(new PwApplyRecord());
            }
            pwEnter.getApplyRecord().setType(PwEnterBgremarks.R4.getKey());
            pwEnter.getApplyRecord().setStatus(PwEnterAuditEnum.DSH.getValue());
            Page<PwEnter> page = pwEnterService.findPageQxrzRecoredByGroup(pag, pwEnter, true);
            if (isDefault && StringUtil.checkNotEmpty(pag.getList())) {
                List<PwEnter> ydqlist = Lists.newArrayList();
                List<PwEnter> jjdqlist = Lists.newArrayList();
                List<PwEnter> rzcglist = Lists.newArrayList();
                List<PwEnter> ytflist = Lists.newArrayList();
                List<PwEnter> deflist = Lists.newArrayList();
                for (PwEnter penter : page.getList()) {
                    if((PwEnterStatus.PES_YDQ.getKey()).equals(penter.getStatus())){
                        ydqlist.add(penter);
                    }else if((PwEnterStatus.PES_DXQ.getKey()).equals(penter.getStatus())){
                        jjdqlist.add(penter);
                    }else if((PwEnterStatus.PES_RZCG.getKey()).equals(penter.getStatus())){
                        rzcglist.add(penter);
                    }else if((PwEnterStatus.PES_YTF.getKey()).equals(penter.getStatus())){
                        ytflist.add(penter);
                    }else{
                        deflist.add(penter);
                    }
                }
                ydqlist.addAll(jjdqlist);
                ydqlist.addAll(rzcglist);
                ydqlist.addAll(ytflist);
                ydqlist.addAll(deflist);
                page.setList(ydqlist);
            }
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
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

    /**
     * 获取入驻团队列表.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxTeamList")
    public ApiResult ajaxTeamList(PwEnter pwEnter, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            Page<PwEnter> pag = new Page<PwEnter>(request, response);
            queryList(pwEnter, pag);
            if (StringUtil.isEmpty(pag.getOrderBy())) {
                pag.setOrderBy(PwEnter.START_DATE + StringUtil.KGE + Page.ORDER_DESC + StringUtil.DOTH + DataEntity.CREATE_DATE + StringUtil.KGE + Page.ORDER_DESC + StringUtil.DOTH + StringUtil.KGE + PwEnter.TYPE);
                pag.setOrderByType(Page.ORDER_DESC);
            }
            pwEnter.setType(PwEnterType.PET_TEAM.getKey());
            pwEnter.setPstatus(PwEnterStatus.getKeyByQuery());
            Page<PwEnter> page = pwEnterService.findQueryPageByGroup(pag, pwEnter);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }
    /**
     * 团队、企业查询列表查询规则.
     * @param pwEnter
     * @param pag
     */
    public static void queryList(PwEnter pwEnter, Page<PwEnter> pag) {
        if (StringUtil.isEmpty(pag.getOrderBy())) {
            pag.setOrderBy(PwEnter.TABLEA + DataEntity.CREATE_DATE + StringUtil.KGE + Page.ORDER_DESC + StringUtil.DOTH + StringUtil.KGE + PwEnter.TABLEA + PwEnter.TYPE);
            pag.setOrderByType(Page.ORDER_ASC);
        }
        pwEnter.setIsTemp(Const.NO);
        pwEnter.setIsCopy(Const.NO);
        pwEnter.setAppType(PwEnterAtype.PAT_DEFAULT.getKey());
    }

    /**
     * 获取入驻企业列表.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxCompanyList")
    public ApiResult ajaxCompanyList(PwEnter pwEnter, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            Page<PwEnter> pag = new Page<PwEnter>(request, response);
            queryList(pwEnter, pag);
            if (StringUtil.isEmpty(pag.getOrderBy())) {
                pag.setOrderBy(PwEnter.START_DATE + StringUtil.KGE + Page.ORDER_DESC + StringUtil.DOTH + DataEntity.CREATE_DATE + StringUtil.KGE + Page.ORDER_DESC + StringUtil.DOTH + StringUtil.KGE + PwEnter.TYPE);
                pag.setOrderByType(Page.ORDER_DESC);
            }
            pwEnter.setType(PwEnterType.PET_QY.getKey());
            pwEnter.setPstatus(PwEnterStatus.getKeyByQuery());
            Page<PwEnter> page = pwEnterService.findQueryPageByGroup(pag, pwEnter);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
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

    /**
     * 退孵.
     * @param id 入驻对象、审核ID必填
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxExit", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ApiResult ajaxExit(String id, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if(StringUtil.isEmpty(id)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败！");
            }
            ApiTstatus<PwEnter> rtstatus = pwEnterService.enterByExit(id);
            if(rtstatus.getStatus()){
                return ApiResult.success(rtstatus.getDatas());
            }else{
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+ rtstatus.getMsg());
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 批量退孵.
     * @param ids 入驻对象、审核ID必填
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxExits", method = RequestMethod.GET)
    public ApiResult ajaxExits(@RequestParam("ids") String ids, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if(StringUtil.isEmpty(ids)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败！");
            }
            ApiTstatus<PwEnter> rtstatus = pwEnterService.enterByExits(ids);
            if(rtstatus.getStatus()){
                return ApiResult.success(rtstatus.getDatas());
            }else{
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+ rtstatus.getMsg());
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 入驻审核.
     * @param id 审核ID
     * @param atype 审核结果类型
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxAudit", method = RequestMethod.GET)
    public ApiResult ajaxAudit(@RequestParam("id") String id, @RequestParam("atype") String atype, @RequestParam("term") Integer term, @RequestParam("remarks") String remarks) {
        try {
            ApiTstatus<PwEnter> rtstatus = pwEnterService.auditEnter(id, atype, term, remarks);
            if(rtstatus.getStatus()){
                return ApiResult.success(rtstatus.getDatas());
            }else{
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+ rtstatus.getMsg());
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 申请审核.
     * @param id 审核ID
     * @param type 审核类型(PwEnterBgremarks.R3、续期申请;PwEnterBgremarks.R4、退孵申请)
     * @param atype 审核结果类型
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxAuditRecored", method = RequestMethod.GET)
    public ApiResult ajaxAuditRecored(@RequestParam("id") String id, @RequestParam("type") String type, @RequestParam("atype") String atype, @RequestParam("remarks") String remarks) {
        try {
            ApiTstatus<PwEnter> rtstatus = pwApplyRecordService.auditRecord(id, type, atype, remarks);
            if(rtstatus.getStatus()){
                return ApiResult.success(rtstatus.getDatas());
            }else{
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+ rtstatus.getMsg());
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 入驻变更审核.
     * @param id 审核ID
     * @return ApiResult
     */
    @CheckToken(value = Const.YES)
    @ResponseBody
    @RequestMapping(value = "ajaxAuditBGSH", method = RequestMethod.GET)
    public ApiResult ajaxAuditBGSH(@RequestParam("id") String id, @RequestParam("isPass") String isPass
            , @RequestParam("remarks") String remarks ) {
        try {
            PwEnter entity = pwEnterService.changeAudit(id, isPass,remarks);
            return ApiResult.success(entity);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 入驻审核-忽略.
     * @param id 入驻对象、审核ID必填
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxShow", method = RequestMethod.GET)
    public ApiResult ajaxShow(@RequestParam String id, @RequestParam String isShow, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if(StringUtil.isEmpty(id) || StringUtil.isEmpty(isShow)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败！");
            }
            pwEnterService.updateIsShow(id, isShow);
            return ApiResult.success(id);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 入驻审核-删除.
     * @param id 入驻对象、审核ID必填
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxDelete", method = RequestMethod.GET)
    public ApiResult ajaxDelete(@RequestParam String id, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if(StringUtil.isEmpty(id)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败！");
            }
            ApiTstatus<PwEnter> rstatus = pwEnterService.deleteWLEnter(id, request, response);
            if(rstatus.getStatus()){
                return ApiResult.success(rstatus.getDatas());
            }else{
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+ rstatus.getMsg());
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 入驻审核-批量状态删除.
     * @param ids 入驻对象、审核ID必填
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxDeletePL", method = RequestMethod.POST)
    public ApiResult ajaxDeletePL(@RequestParam String ids, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if(StringUtil.isEmpty(ids)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败！");
            }
            ApiTstatus<PwEnter> rstatus = pwEnterService.deletePLEnter(ids, request, response);
            if(rstatus.getStatus()){
                return ApiResult.success(rstatus.getDatas());
            }else{
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+ rstatus.getMsg());
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
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

    @Deprecated
    @RequestMapping(value="getApplicantInfo", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getApplicantInfo(PwEnter pwEnter){
        try {
            return ApiResult.success(pwEnter);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 获取项目.
     * @param id 入驻审核ID
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxProject", method = RequestMethod.GET)
    public ApiResult ajaxProject(@RequestParam String id, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if(StringUtil.isEmpty(id)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败！");
            }
            PwEnter pwEnter = new PwEnter(id);
            pwEnter.setType(PwEnterType.PET_XM.getKey());
            PwEnter rpwEnter = pwEnterService.getProjects(pwEnter);
            if((rpwEnter == null) || StringUtil.checkEmpty(rpwEnter.getEprojects())){
                return ApiResult.failed(ApiConst.CODE_NULL_ERROR, ApiConst.getErrMsg(ApiConst.CODE_NULL_ERROR));
            }
            return ApiResult.success(rpwEnter.getEprojects());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 获取企业.
     * @param id 入驻审核ID
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxCompany", method = RequestMethod.GET)
    public ApiResult ajaxCompany(@RequestParam String id, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if(StringUtil.isEmpty(id)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败！");
            }
            PwEnter pwEnter = new PwEnter(id);
            pwEnter.setType(PwEnterType.PET_QY.getKey());
            PwEnter rpwEnter = pwEnterService.getCompany(pwEnter);
            if((rpwEnter == null) || (rpwEnter.getEcompany() == null)){
                return ApiResult.failed(ApiConst.CODE_NULL_ERROR, ApiConst.getErrMsg(ApiConst.CODE_NULL_ERROR));
            }
            return ApiResult.success(rpwEnter.getEcompany());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 获取团队.
     * @param id 入驻审核ID
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxTeam", method = RequestMethod.GET)
    public ApiResult ajaxTeam(@RequestParam String id, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if(StringUtil.isEmpty(id)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败！");
            }
            PwEnter pwEnter = new PwEnter(id);
            pwEnter.setType(PwEnterType.PET_TEAM.getKey());
            PwEnter rpwEnter = pwEnterService.getTeam(pwEnter);
            if((rpwEnter == null) || (rpwEnter.getEteam() == null)){
                return ApiResult.failed(ApiConst.CODE_NULL_ERROR, ApiConst.getErrMsg(ApiConst.CODE_NULL_ERROR));
            }
            return ApiResult.success(rpwEnter.getEteam());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 获取场地.
     * @param id 入驻审核ID
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxRoom", method = RequestMethod.GET)
    public ApiResult ajaxRoom(@RequestParam String id, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if(StringUtil.isEmpty(id)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败！");
            }
            PwEnter rpwEnter = pwEnterService.getRoom(new PwEnter(id));
            if((rpwEnter == null) || StringUtil.checkEmpty(rpwEnter.getErooms())){
                return ApiResult.failed(ApiConst.CODE_NULL_ERROR, ApiConst.getErrMsg(ApiConst.CODE_NULL_ERROR));
            }
            return ApiResult.success(rpwEnter.getErooms());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }



    /**
     * 获取所有场地状态.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxPwEroomStatus", method = RequestMethod.GET)
    public ApiResult ajaxPwEroomStatus(Boolean isAll) {
        try {
            if(isAll == null){
                return ApiResult.success(PwEroomStatus.getAll().toString());
            }

            if(isAll){
                return ApiResult.success(Arrays.asList(PwEroomStatus.values()).toString());
            }
            return ApiResult.success(PwEroomStatus.getAll().toString());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 获取所有入驻类型.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxPwEnterTypes", method = RequestMethod.GET)
    public ApiResult ajaxPwEnterTypes(@RequestParam Boolean isAll) {
        try {
            if(isAll == null){
                isAll = false;
            }
            if(isAll){
                return ApiResult.success(Arrays.asList(PwEnterType.values()).toString());
            }else{
                return ApiResult.success(Arrays.asList(PwEnterType.getAll()).toString());
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
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

    /**
     * 获取所有入驻审核状态.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxPwEnterStatus", method = RequestMethod.GET)
    public ApiResult ajaxPwEnterStatus(String type) {
        try {
            if((PwElistType.ET_FPCD.getKey()).equals(type)){
                return ApiResult.success(PwEnterStatus.getKeysByFPCD().toString());
            }else if((PwElistType.ET_XQ.getKey()).equals(type)){
                return ApiResult.success(PwEnterStatus.getKeysByXQRZ().toString());
            }else if((PwElistType.ET_QX.getKey()).equals(type)){
                return ApiResult.success(PwEnterStatus.getKeysByQXRZ().toString());
            }else if((PwElistType.ET_QUERY.getKey()).equals(type)){
                return ApiResult.success(PwEnterStatus.getKeysByQuery().toString());
            }
            return ApiResult.success(Arrays.asList(PwEnterStatus.values()).toString());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 获取所有入驻变更说明.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxPwEnterBgremarks", method = RequestMethod.GET)
    public ApiResult ajaxPwEnterBgremarks() {
        try {
            return ApiResult.success(Arrays.asList(PwEnterBgremarks.values()).toString());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 获取所有入驻说明.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxPwEnterRemarks", method = RequestMethod.GET)
    public ApiResult ajaxPwEnterRemarks() {
        try {
            return ApiResult.success(Arrays.asList(PwEnterRemarks.values()).toString());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 续期.
     * @param id 入驻对象、审核ID必填
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxFormXq", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ApiResult ajaxFormXq(String id, Integer term, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if(StringUtil.isEmpty(id) || (term == null)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败！");
            }
            ApiTstatus<PwEnter> rstatus = pwEnterService.enterByXq(id, term);
            if(rstatus.getStatus()){
                return ApiResult.success(rstatus.getDatas());
            }else{
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+ rstatus.getMsg());
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 批量续期.
     * @param ids 入驻对象、审核ID必填
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxFormXqs", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiResult ajaxFormXqs(String ids, Integer term, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if(StringUtil.isEmpty(ids) || (term == null)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败！");
            }
            ApiTstatus<PwEnter> rstatus = pwEnterService.enterByXqs(ids, term);
            if(rstatus.getStatus()){
                return ApiResult.success(rstatus.getDatas());
            }else{
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+ rstatus.getMsg());
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }
    /*******************************************************************************
     ***待处理的方法******************************************************************
     ******************************************************************************/
    /**
     * 获取入驻保存接口.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxSave", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiResult ajaxSave(@RequestBody PwEnter pwEnter, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if (!beanValidator(model, pwEnter)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败！");
            }
            pwEnterService.save(pwEnter);
            return ApiResult.success(pwEnter);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

	/**
	 * 获取入驻查询列表.
	 * @return ApiResult
	 */
	@ResponseBody
	@RequestMapping(value = "ajaxListQuery")
	public ApiResult ajaxListQuery(PwEnter pwEnter, HttpServletRequest request, HttpServletResponse response, Model model) {
	    try {
	        pwEnter.setPstatus(PwEnterStatus.getKeyByQuery());
	        return ApiResult.success(pwEnterService.findPageByGroup(new Page<PwEnter>(request, response), pwEnter));
	    }catch (Exception e){
	        logger.error(e.getMessage());
	        return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
	    }
	}

    /**
     * 查询房间分配情况. isAll == true，表示查询所有 isAll = false,表示非所有,此时rids必填
     *
     * @param isAll
     *            查询所有
     * @param status
     *            状态
     * @param rids
     *            房间ID
     * @param response
     * @return List
     */
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "treeData")
    public ApiResult treeData(@RequestParam(required = false) Boolean isAll,
            @RequestParam(required = false) String status, @RequestParam(required = false) String rids,
            HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        if (isAll == null) {
            isAll = false;
        }

        PwEnter pwEnter = new PwEnter();
        if (!isAll) {
            if (StringUtil.isEmpty(rids)) {
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，房间ID不能为空！");
            }
            List<String> ids = Arrays.asList((rids).split(StringUtil.DOTH));
            if ((ids == null) || (ids.size() <= 0)) {
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，房间ID不能为空！");
            }
            pwEnter.setIds(ids);
        }

        if (StringUtil.isNotEmpty(status)) {
            if ((PwEnterStatus.getKeyByDFP()).equals(status)) {
                pwEnter.setPstatus(PwEnterStatus.getKeyByDFP());
            } else if ((PwEnterStatus.getKeyByYFP()).equals(status)) {
                pwEnter.setPstatus(PwEnterStatus.getKeyByYFP());
            } else if ((PwEnterStatus.getKeyByQXRZ()).equals(status)) {
                pwEnter.setPstatus(PwEnterStatus.getKeyByQXRZ());
            } else if ((PwEnterStatus.getKeyByXQRZ()).equals(status)) {
                pwEnter.setPstatus(PwEnterStatus.getKeyByXQRZ());
            }
        }

        List<PwEnter> list = Lists.newArrayList();
        if (StringUtil.isNotEmpty(pwEnter.getPstatus())) {
            list = pwEnterService.findListByGroup(pwEnter);
        }

        StringBuffer name;
        for (int i = 0; i < list.size(); i++) {
            PwEnter e = list.get(i);
            if (e != null) {
                name = new StringBuffer();
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", e.getId());
                map.put("pId", CoreIds.NCE_SYS_TREE_ROOT.getId());
                map.put("name", e.getNo());
                map.put(CoreJkey.JK_DATA, e);
                mapList.add(map);
            }
        }
        return ApiResult.success(mapList);
    }

    //存在给false
    @RequestMapping(value="checkPwEnterProject", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Boolean checkPwEnterProject(@RequestBody PwProject pwProject){
        return pwEnterService.checkPwEnterProject(pwProject);
    }

    /**
     * 获取所有待审核记录数.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxCountToAudit", method = RequestMethod.GET)
    public long ajaxCountToAudit() {
        return pwEnterService.getCountToAudit();
    }

    /**
     * 获取所有待审核记录数.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "getCountToFPCD", method = RequestMethod.GET)
    public long getCountToFPCD() {
        return pwEnterService.getCountToFPCD();
    }

    //后台管理员直接变更
    @RequestMapping(value = "ajaxPwEnterChange" , method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxPwEnterChange(@RequestBody PwEnter pwEnter, HttpServletRequest request, HttpServletResponse response) {
        try {
            boolean isTeamHas=pwEnterService.checkPwEnterTeam(pwEnter);
            if(isTeamHas){
                return ApiResult.failed(ApiConst.CODE_MORE_ERROR,ApiConst.getErrMsg(ApiConst.CODE_MORE_ERROR)+":团队重复");
            }
            if(PwEnterType.PET_QY.getKey().equals(pwEnter.getType())){
                boolean isPwCompanyHas=pwEnterService.checkPwEnterPwCompanyHas(pwEnter);
                if(isPwCompanyHas){
                    return ApiResult.failed(ApiConst.CODE_MORE_ERROR,ApiConst.getErrMsg(ApiConst.CODE_MORE_ERROR)+":企业重复");
                }
            }
            boolean isChangeHas=pwEnterService.checkBackChange(pwEnter.getId());
            if(isChangeHas){
                return ApiResult.failed(ApiConst.CODE_MORE_ERROR,"已经发起变更申请,审核完之前，不能进行变更");
            }
            if(pwEnter.getTeam()!=null && StringUtil.isEmpty(pwEnter.getTeam().getId())){
                return ApiResult.failed(ApiConst.CODE_NULL_ERROR,ApiConst.getErrMsg(ApiConst.CODE_NULL_ERROR)+":数据为空");
            }
            //pwEnterService.savePwEnterApply(pwEnter);
            return pwEnterService.savePwEnterChange(pwEnter);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    //获得关联项目接口
    @RequestMapping(value = "ajaxGetProjects" , method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxGetProjects(HttpServletRequest request, HttpServletResponse response,String pwEnterId) {
        try {
            List<ProModel> proModels=pwEnterService.getBackProjects(pwEnterId);
            return ApiResult.success(proModels);
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


    //获得关联审核枚举接口
    @RequestMapping(value = "getPwEnterAuditList" , method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public List<Object> getPwEnterAuditList() {
        return PwEnterAuditEnum.getPwEnterAuditList();
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
}
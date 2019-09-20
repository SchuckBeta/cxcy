package com.oseasy.scr.modules.scr.web;

import java.util.Arrays;
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

import com.google.common.collect.Maps;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.common.persistence.IOrby;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.scr.common.config.ScrSval;
import com.oseasy.scr.common.config.ScrSval.ScrEmskey;
import com.oseasy.scr.modules.scr.entity.ScoRapply;
import com.oseasy.scr.modules.scr.entity.ScoRapplyRecord;
import com.oseasy.scr.modules.scr.service.ScoRapplyRecordService;
import com.oseasy.scr.modules.scr.service.ScoRapplyService;
import com.oseasy.scr.modules.scr.vo.ScoAuditVo;
import com.oseasy.scr.modules.scr.vo.ScoCreditType;
import com.oseasy.scr.modules.scr.vo.ScoQuery;
import com.oseasy.scr.modules.scr.vo.ScoRstatus;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 学分申请Controller.
 * @author chenh
 * @version 2018-12-21
 */
@Controller
@RequestMapping(value = "${adminPath}/scr/scoRapply")
public class ScoRapplyController extends BaseController {

	@Autowired
	private ScoRapplyService entityService;
    @Autowired
    private ScoRapplyRecordService scoRapplyRecordService;
    @Autowired
    private SysAttachmentService sysAttachmentService;

	@ModelAttribute
	public ScoRapply get(@RequestParam(required=false) String id) {
		ScoRapply entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = entityService.get(id);
		}
		if (entity == null){
			entity = new ScoRapply();
		}
		return entity;
	}

	@RequiresPermissions("scr:scoRapply:view")
	@RequestMapping(value = {"list", ""})
	public String list(ScoRapply entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute(IOrby.ORBY_ORS_KEY, entity.ors());
	    return ScrSval.path.vms(ScrEmskey.SCR.k()) + "scoRapplyList";
	}

	@RequestMapping(value = "form")
	public String form(String scrId, String creditType, Model model) {
	    model.addAttribute("scrId", scrId);
        model.addAttribute("creditType", creditType);
        model.addAttribute("SKILLCREDITID",  CoreSval.getScoreSkillId());
		return ScrSval.path.vms(ScrEmskey.SCR.k()) + "scoRapplyForm";
	}

    @RequestMapping(value = "view")
    public String view(String scrId, String creditType, Model model) {
        model.addAttribute("scrId", scrId);
        model.addAttribute("creditType", creditType);
        return ScrSval.path.vms(ScrEmskey.SCR.k()) + "scoRapplyView";
    }

	@RequiresPermissions("scr:scoRapply:edit")
	@RequestMapping(value = "save")
	public String save(ScoRapply entity, Model model, RedirectAttributes redirectAttributes) {
//		if (!beanValidator(model, entity)){
//			return form(entity, model);
//		}
		entityService.save(entity);
		addMessage(redirectAttributes, "保存学分申请成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/scr/scoRapply/?repage";
	}

    @RequiresPermissions("scr:scoRapply:edit")
	@RequestMapping(value = "delete")
	public String delete(ScoRapply entity, RedirectAttributes redirectAttributes) {
		entityService.delete(entity);
		addMessage(redirectAttributes, "删除学分申请成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/scr/scoRapply/?repage";
	}

    /**
     * 获取申请.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxScoRapply")
    public ApiResult ajaxTeamList(ScoRapply entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if (StringUtil.isEmpty(entity.getId())) {
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败！");
            }
            return ApiResult.success(entityService.get(entity.getId()));
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    //学分查询--查看详情中审核结果
    @RequestMapping(value = "ajaxFindScoAuditResult", method = RequestMethod.GET)
    @ResponseBody
    public ApiResult ajaxFindScoAuditResult(ScoQuery scoQuery){
        try{
            //创新创业学分类型
            if(scoQuery.getCreditType().equals(ScoCreditType.SCO_CREDIT.getKey())){
                List<ScoRapplyRecord> scoRapplyRecordList = entityService.scoRapplyRecordList(new ScoRapply(scoQuery.getId()));
                return ApiResult.success(scoRapplyRecordList);
            }
            //课程学分类型

            List<ScoRapplyRecord> scoRapplyRecordList = scoRapplyRecordService.findCourseAuditList(new ScoRapplyRecord(scoQuery.getId()));
            return ApiResult.success(scoRapplyRecordList);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
        }
    }

	/**
     * 获取审核列表.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxList")
    public ApiResult ajaxlist(ScoRapply entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            Page<ScoRapply> pag = new Page<ScoRapply>(request, response);
            if (StringUtil.isEmpty(pag.getOrderBy())) {
                pag.setOrderBy(ScoRapply.TABLEA + DataEntity.CREATE_DATE + StringUtil.KGE);
                pag.setOrderByType(Page.ORDER_DESC);
            }
            entity.setIsTemp(Const.NO);
            entity.setStatus(ScoRstatus.SRS_DSH.getKey());
            return ApiResult.success(entityService.findPageByg(new Page<ScoRapply>(request, response), entity));
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

	/**
     * 学分审核.
     * @param id 审核ID
     * @param atype 审核结果类型
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxAudit", method = RequestMethod.POST)
    public ApiResult ajaxAudit(@RequestBody ScoAuditVo scoaVo) {
        try {
            if(StringUtil.isEmpty(scoaVo.getId()) || StringUtil.isEmpty(scoaVo.getAtype()) || StringUtil.isEmpty(scoaVo.getIgnodeId())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE));
            }
            if(entityService.audit(scoaVo)){
                return ApiResult.success();
            }else{
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE));
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 状态.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxScoRstatuss", method = RequestMethod.GET)
    public ApiResult ajaxScoRstatuss(@RequestParam Boolean isAll) {
        try {
            if(isAll == null){
                isAll = false;
            }
            if(isAll){
                return ApiResult.success(Arrays.asList(ScoRstatus.values()).toString());
            }else{
                return ApiResult.success(Arrays.asList(ScoRstatus.getAll()).toString());
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

	//学分查询详情接口
	@RequestMapping(value = "ajaxFindScoDetail", method = RequestMethod.POST)
	@ResponseBody
	public ApiResult ajaxFindScoDetail(@RequestBody ScoQuery scoQuery){
		ScoRapply sr = new ScoRapply();
		sr.setId(scoQuery.getId());
		//创新创业学分类型
		if(scoQuery.getCreditType().equals(ScoCreditType.SCO_CREDIT.getKey())){
			sr = entityService.ajaxFindScoDetail(sr);
			return ApiResult.success(sr);
		}
		//课程学分类型
		sr = entityService.ajaxFindCourseDetail(sr);
		return ApiResult.success(sr);
	}

	//查询当前用户的申请
	@ResponseBody
    @RequestMapping(value = "ajaxProjects")
	public ApiResult ajaxProjects(String id){
        Map<String, Object> rmap = Maps.newHashMap();
	    rmap.put(CoreJkey.JK_IS_TRUE, entityService.checkHasProject(id));
	    rmap.put(CoreJkey.JK_DATAS, entityService.findProjects(new ScoRapply(id)));
	    return ApiResult.success(rmap);
	}

    //根据申请获取附件
    @ResponseBody
    @RequestMapping(value = "ajaxFiles")
    public ApiResult ajaxFiles(String id){
        SysAttachment sysAttachment=new SysAttachment();
        sysAttachment.setUid(id);
        return ApiResult.success(sysAttachmentService.getFiles(sysAttachment));
    }

    //根据申请获取总分
    @ResponseBody
    @RequestMapping(value = "ajaxScoSum")
    public ApiResult ajaxScoSum(ScoRapply scoRapply){
        return entityService.findRsumByUid(scoRapply);
    }
}
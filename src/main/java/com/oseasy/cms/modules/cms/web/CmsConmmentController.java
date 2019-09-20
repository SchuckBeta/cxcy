package com.oseasy.cms.modules.cms.web;

import java.util.List;
import java.util.stream.Collectors;

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
import com.oseasy.cms.common.config.CmsSval;
import com.oseasy.cms.common.config.CmsSval.CmsEmskey;
import com.oseasy.cms.modules.cms.entity.CmsConmment;
import com.oseasy.cms.modules.cms.entity.CmsConmmentLikes;
import com.oseasy.cms.modules.cms.service.CmsConmmentLikesService;
import com.oseasy.cms.modules.cms.service.CmsConmmentService;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiParam;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

/**
 * 评论Controller.
 * @author chenh
 * @version 2018-09-04
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/cmsConmment")
public class CmsConmmentController extends BaseController {

	@Autowired
	private CmsConmmentService cmsConmmentService;
	@Autowired
	private CmsConmmentLikesService cmsConmmentLikesService;
    @Autowired
   	private OaNotifyService oaNotifyService;
	@ModelAttribute
	public CmsConmment get(@RequestParam(required=false) String id) {
		CmsConmment entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = cmsConmmentService.get(id);
		}
		if (entity == null){
			entity = new CmsConmment();
		}
		return entity;
	}

	@RequiresPermissions("cms:cmsConmment:view")
	@RequestMapping(value = {"list", ""})
	public String list(CmsConmment cmsConmment, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsConmment> page = cmsConmmentService.findPage(new Page<CmsConmment>(request, response), cmsConmment);
		model.addAttribute(Page.PAGE, page);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "cmsConmmentList";
	}

	@RequiresPermissions("cms:cmsConmment:view")
	@RequestMapping(value = "form")
	public String form(CmsConmment cmsConmment, Model model) {
        model.addAttribute("cmsConmment", cmsConmment);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "cmsConmmentForm";
	}

	@RequiresPermissions("cms:cmsConmment:edit")
	@RequestMapping(value = "save")
	public String save(CmsConmment cmsConmment, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cmsConmment)){
			return form(cmsConmment, model);
		}
		cmsConmmentService.save(cmsConmment);
		addMessage(redirectAttributes, "保存评论成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/cms/cmsConmment/?repage";
	}

	@RequiresPermissions("cms:cmsConmment:edit")
	@RequestMapping(value = "delete")
	public String delete(CmsConmment cmsConmment, RedirectAttributes redirectAttributes) {
		cmsConmmentService.delete(cmsConmment);
		addMessage(redirectAttributes, "删除评论成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/cms/cmsConmment/?repage";
	}

    @ResponseBody
    @RequestMapping(value = "ajaxList")
    public ApiResult ajaxlist(CmsConmment cmsConmment, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            Page<CmsConmment> page = cmsConmmentService.findPage(new Page<CmsConmment>(request, response), cmsConmment);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "ajaxSave", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiResult ajaxSave(@RequestBody CmsConmment cmsConmment, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if (!beanValidator(model, cmsConmment)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败！");
            }
            cmsConmmentService.save(cmsConmment);
            //评论审核发送消息
            if(cmsConmment.getCreateBy()!=null){
                if(cmsConmment.getAuditstatus().equals(ApiConst.AUDITSTATUS_SUCCESS_STR)){
                      oaNotifyService.sendOaNotifyByType(UserUtils.getUser(), cmsConmment.getCreateBy(), "评论管理",
                          "你的评论被管理员审核通过了",OaNotify.Type_Enum.TYPE22.getValue(), cmsConmment.getId());
                }else if (cmsConmment.getAuditstatus().equals(ApiConst.AUDITSTATUS_FAIL_STR)){
                      oaNotifyService.sendOaNotifyByType(UserUtils.getUser(), cmsConmment.getCreateBy(), "评论管理",
                          "你的评论被管理员审核拒绝了",OaNotify.Type_Enum.TYPE22.getValue(), cmsConmment.getId());
                }
            }
            return ApiResult.success(cmsConmment);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "ajaxUpdateContent", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiResult ajaxUpdateContent(@RequestBody CmsConmment cmsConmment, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if (StringUtil.isEmpty(cmsConmment.getId()) || StringUtil.isEmpty(cmsConmment.getContent())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，ID标识和评论内容不能为空！");
            }
            CmsConmment curCmsConmment = cmsConmmentService.get(cmsConmment.getId());
            curCmsConmment.setContent(cmsConmment.getContent());
            //curCmsConmment.setUser(UserUtils.getUser());
            cmsConmmentService.save(curCmsConmment);
            return ApiResult.success(curCmsConmment);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }


    @ResponseBody
    @RequestMapping(value = "ajaxUpdateReply", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiResult ajaxUpdateReply(@RequestBody CmsConmment cmsConmment, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if (StringUtil.isEmpty(cmsConmment.getId()) || StringUtil.isEmpty(cmsConmment.getReply())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，ID标识不能为空！");
            }
            CmsConmment curCmsConmment = cmsConmmentService.get(cmsConmment.getId());
            if((curCmsConmment == null)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，ID标识不能为空！");
            }
            curCmsConmment.setReply(cmsConmment.getReply());
            curCmsConmment.setReUser(UserUtils.getUser());
            curCmsConmment.setReDate(DateUtil.newDate());
            cmsConmmentService.save(curCmsConmment);
            oaNotifyService.sendOaNotifyByType(UserUtils.getUser(), curCmsConmment.getUser(), "评论管理",
            					"管理员回复了你的评论", OaNotify.Type_Enum.TYPE22.getValue(), curCmsConmment.getId());
            return ApiResult.success(curCmsConmment);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "ajaxUpdateLikes", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiResult ajaxUpdateLikes(@RequestBody CmsConmment cmsConmment, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if (StringUtil.isEmpty(cmsConmment.getId()) || (cmsConmment.getLikes() == null)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，ID标识和操作不能为空！");
            }

            if ((Const.Z1 != cmsConmment.getLikes()) && (Const.F1 != cmsConmment.getLikes())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，Likes只能为(1 点赞)或(-1 取消点赞)！");
            }
            CmsConmment curCmsConmment = cmsConmmentService.get(cmsConmment.getId());
            curCmsConmment.setLikes(curCmsConmment.getLikes() + cmsConmment.getLikes());
            cmsConmmentService.save(curCmsConmment);

            CmsConmmentLikes curCmsConmmentLikes = new CmsConmmentLikes();
            curCmsConmmentLikes.setUser(UserUtils.getUser());
            curCmsConmmentLikes.setParent(curCmsConmment);
            List<CmsConmmentLikes> cmsConmmentLikess = cmsConmmentLikesService.findList(curCmsConmmentLikes);
            if(StringUtil.checkEmpty(cmsConmmentLikess) && (Const.Z1 == cmsConmment.getLikes())){
                cmsConmmentLikesService.save(curCmsConmmentLikes);
            }else if(StringUtil.checkNotEmpty(cmsConmmentLikess) && (Const.F1 == cmsConmment.getLikes())){
                List<String> ids = cmsConmmentLikess.stream().map(e -> e.getId()).collect(Collectors.toList());
                curCmsConmmentLikes.setIds(ids);
                cmsConmmentLikesService.deleteWLPL(curCmsConmmentLikes);
            }
            return ApiResult.success(curCmsConmment);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "ajaxUpdateRecommend", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiResult ajaxUpdateRecommend(@RequestBody CmsConmment cmsConmment, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if (StringUtil.isEmpty(cmsConmment.getId()) || StringUtil.isEmpty(cmsConmment.getIsRecommend())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，ID标识和操作不能为空！");
            }

            CmsConmment curCmsConmment = cmsConmmentService.get(cmsConmment.getId());
            curCmsConmment.setIsRecommend(cmsConmment.getIsRecommend());
            cmsConmmentService.save(curCmsConmment);
            return ApiResult.success(curCmsConmment);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/ajaxUpdatePLRecommend", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiResult ajaxUpdatePLRecommend(@RequestBody ApiParam<List<CmsConmment>> api, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            List<CmsConmment> entitys = api.getDatas();
            if(StringUtil.checkEmpty(entitys)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，"+CoreJkey.JK_DATAS+"不能为空！");
            }
            cmsConmmentService.updatePLRecommend(entitys);
            return ApiResult.success(entitys);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/ajaxUpdatePLAudit", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiResult ajaxUpdatePLAudit(@RequestBody ApiParam<List<CmsConmment>> api, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            List<CmsConmment> entitys = api.getDatas();
            if(StringUtil.checkEmpty(entitys)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，"+CoreJkey.JK_DATAS+"不能为空！");
            }
            List<CmsConmment> curentitys = Lists.newArrayList();
            for (CmsConmment cmsConmment : entitys) {
                cmsConmment.setAuditDate(DateUtil.newDate());
                cmsConmment.setAuUser(UserUtils.getUser());
                curentitys.add(cmsConmment);

                //评论审核发送消息
                CmsConmment oldCmsConmment=get(cmsConmment.getId());
                if(oldCmsConmment.getUser()!=null){
                    if(cmsConmment.getAuditstatus().equals(ApiConst.AUDITSTATUS_SUCCESS_STR)){
                          oaNotifyService.sendOaNotifyByType(UserUtils.getUser(), oldCmsConmment.getUser(), "评论管理",
                              "你的评论被管理员审核通过了",OaNotify.Type_Enum.TYPE22.getValue(), cmsConmment.getId());
                    }else if (cmsConmment.getAuditstatus().equals(ApiConst.AUDITSTATUS_FAIL_STR)){
                          oaNotifyService.sendOaNotifyByType(UserUtils.getUser(), oldCmsConmment.getUser(), "评论管理",
                              "你的评论被管理员审核拒绝了",OaNotify.Type_Enum.TYPE22.getValue(), cmsConmment.getId());
                    }
                }
            }
            cmsConmmentService.updatePLAudit(curentitys);
            return ApiResult.success(curentitys);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/ajaxUpdatePLAuditByArticle", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiResult ajaxUpdatePLAuditByArticle(@RequestBody ApiParam<List<CmsConmment>> api, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            List<CmsConmment> entitys = api.getDatas();
            if(StringUtil.checkEmpty(entitys)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，"+CoreJkey.JK_DATAS+"不能为空！");
            }
            cmsConmmentService.updatePLAuditByCntIds(entitys);
            return ApiResult.success(entitys);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "ajaxDelete")
    public ApiResult ajaxDelete(CmsConmment cmsConmment, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if (StringUtil.isEmpty(cmsConmment.getId())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，ID标识和操作不能为空！");
            }
            cmsConmmentService.delete(cmsConmment);
            return ApiResult.success(cmsConmment);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "ajaxDeletePL", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiResult ajaxDeletePL(@RequestBody ApiParam<List<String>> api) {
        try {
            List<String> entitys = api.getDatas();
            if(StringUtil.checkEmpty(entitys)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，"+CoreJkey.JK_DATAS+"不能为空！");
            }
            CmsConmment cmsConmment = new CmsConmment(entitys);
            cmsConmmentService.deletePL(cmsConmment);
            return ApiResult.success(cmsConmment);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "ajaxDeletePLByArticle", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiResult ajaxDeletePLByArticle(@RequestBody ApiParam<List<String>> api, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            List<String> entitys = api.getDatas();
            if(StringUtil.checkEmpty(entitys)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，"+CoreJkey.JK_DATAS+"不能为空！");
            }
            cmsConmmentService.deletePLByCntIds(entitys);
            return ApiResult.success(entitys);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }
}
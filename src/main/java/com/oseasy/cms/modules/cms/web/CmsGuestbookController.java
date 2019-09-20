package com.oseasy.cms.modules.cms.web;

import java.util.List;

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

import com.oseasy.cms.common.config.CmsSval;
import com.oseasy.cms.common.config.CmsSval.CmsEmskey;
import com.oseasy.cms.modules.cms.entity.CmsGuestbook;
import com.oseasy.cms.modules.cms.service.CmsGuestbookService;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiParam;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

/**
 * 留言Controller.
 * @author chenh
 * @version 2018-09-04
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/cmsGuestbook")
public class CmsGuestbookController extends BaseController {

	@Autowired
	private CmsGuestbookService cmsGuestbookService;

	@Autowired
	private OaNotifyService oaNotifyService;

	@ModelAttribute
	public CmsGuestbook get(@RequestParam(required=false) String id) {
		CmsGuestbook entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = cmsGuestbookService.get(id);
		}
		if (entity == null){
			entity = new CmsGuestbook();
		}
		return entity;
	}

	@RequiresPermissions("cms:cmsGuestbook:view")
	@RequestMapping(value = {"list", ""})
	public String list(CmsGuestbook cmsGuestbook, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsGuestbook> page = cmsGuestbookService.findPage(new Page<CmsGuestbook>(request, response), cmsGuestbook);
		model.addAttribute(Page.PAGE, page);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "cmsGuestbookList";
	}

	@RequiresPermissions("cms:cmsGuestbook:view")
	@RequestMapping(value = "form")
	public String form(CmsGuestbook cmsGuestbook, Model model) {
		model.addAttribute("cmsGuestbook", cmsGuestbook);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "cmsGuestbookForm";
	}

	@RequiresPermissions("cms:cmsGuestbook:edit")
	@RequestMapping(value = "save")
	public String save(CmsGuestbook cmsGuestbook, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cmsGuestbook)){
			return form(cmsGuestbook, model);
		}
		cmsGuestbookService.save(cmsGuestbook);
		addMessage(redirectAttributes, "保存留言成功");
		return "redirect:"+CoreSval.getAdminPath()+"/cms/cmsGuestbook/?repage";
	}

	@RequiresPermissions("cms:cmsGuestbook:edit")
	@RequestMapping(value = "delete")
	public String delete(CmsGuestbook cmsGuestbook, RedirectAttributes redirectAttributes) {
		cmsGuestbookService.delete(cmsGuestbook);
		addMessage(redirectAttributes, "删除留言成功");
		return "redirect:"+CoreSval.getAdminPath()+"/cms/cmsGuestbook/?repage";
	}


    @RequestMapping(value = "ajaxList")
    @ResponseBody
    public ApiResult ajaxlist(CmsGuestbook cmsGuestbook, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            Page<CmsGuestbook> page = cmsGuestbookService.findPage(new Page<CmsGuestbook>(request, response), cmsGuestbook);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value = "ajaxSave", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxSave(@RequestBody CmsGuestbook cmsGuestbook, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if (!beanValidator(model, cmsGuestbook)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败！");
            }
            cmsGuestbookService.save(cmsGuestbook);
			//留言审核发送消息
			if(cmsGuestbook.getAuditstatus().equals(ApiConst.AUDITSTATUS_SUCCESS_STR)){
				oaNotifyService.sendOaNotifyByType(UserUtils.getUser(), cmsGuestbook.getCreateUser(), "留言管理",
					"你的留言被管理员审核通过了",OaNotify.Type_Enum.TYPE23.getValue(), cmsGuestbook.getId());
			}else if (cmsGuestbook.getAuditstatus().equals(ApiConst.AUDITSTATUS_FAIL_STR)){
				oaNotifyService.sendOaNotifyByType(UserUtils.getUser(), cmsGuestbook.getCreateUser(), "留言管理",
					"你的留言被管理员审核拒绝了",OaNotify.Type_Enum.TYPE23.getValue(), cmsGuestbook.getId());
			}
            return ApiResult.success(cmsGuestbook);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value = "ajaxUpdateRec", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxUpdateRec(@RequestBody CmsGuestbook cmsGuestbook, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if (StringUtil.isEmpty(cmsGuestbook.getId())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，ID标识不能为空！");
            }
            if (StringUtil.isEmpty(cmsGuestbook.getReContent())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败, 回复内容不能为空！");
            }
            CmsGuestbook curCmsGuestbook = cmsGuestbookService.get(cmsGuestbook.getId());
            if((curCmsGuestbook == null)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败, 回复内容不能为空！");
            }
            curCmsGuestbook.setReContent(cmsGuestbook.getReContent());
            curCmsGuestbook.setReUser(UserUtils.getUser());
            curCmsGuestbook.setReDate(DateUtil.newDate());
            cmsGuestbookService.save(curCmsGuestbook);

			oaNotifyService.sendOaNotifyByType(UserUtils.getUser(), curCmsGuestbook.getCreateBy(), "留言管理",
					"管理员回复了你的留言",OaNotify.Type_Enum.TYPE23.getValue(), curCmsGuestbook.getId());

            return ApiResult.success(curCmsGuestbook);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value = "ajaxUpdateRecommend")
    @ResponseBody
    public ApiResult ajaxUpdateRecommend(CmsGuestbook cmsGuestbook, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if (StringUtil.isEmpty(cmsGuestbook.getId()) || StringUtil.isEmpty(cmsGuestbook.getIsRecommend())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，ID标识和操作不能为空！");
            }

            CmsGuestbook curCmsGuestbook = cmsGuestbookService.get(cmsGuestbook.getId());
            curCmsGuestbook.setIsRecommend(cmsGuestbook.getIsRecommend());
            cmsGuestbookService.save(curCmsGuestbook);
            return ApiResult.success(curCmsGuestbook);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }


    @ResponseBody
    @RequestMapping(value = "/ajaxUpdatePLAudit", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiResult ajaxUpdatePLAudit(@RequestBody ApiParam<List<CmsGuestbook>> api, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            List<CmsGuestbook> entitys = api.getDatas();
            if(StringUtil.checkEmpty(entitys)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，"+CoreJkey.JK_DATAS+"不能为空！");
            }
            cmsGuestbookService.updatePLAudit(entitys);
			for(CmsGuestbook cmsGuestbook:entitys){
				CmsGuestbook indexCmsGuestbook=get(cmsGuestbook.getId());
				if(cmsGuestbook.getAuditstatus().equals(ApiConst.AUDITSTATUS_SUCCESS_STR)){
					oaNotifyService.sendOaNotifyByType(UserUtils.getUser(), indexCmsGuestbook.getCreateUser(), "留言管理",
						"你的留言被管理员审核通过了",OaNotify.Type_Enum.TYPE23.getValue(), cmsGuestbook.getId());
				}else if (cmsGuestbook.getAuditstatus().equals(ApiConst.AUDITSTATUS_FAIL_STR)){
					oaNotifyService.sendOaNotifyByType(UserUtils.getUser(), indexCmsGuestbook.getCreateUser(), "留言管理",
						"你的留言被管理员审核拒绝了",OaNotify.Type_Enum.TYPE23.getValue(), cmsGuestbook.getId());
				}
			}
            return ApiResult.success(entitys);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }


    @RequestMapping(value = "ajaxDelete")
    @ResponseBody
    public ApiResult ajaxDelete(CmsGuestbook cmsGuestbook, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if (StringUtil.isEmpty(cmsGuestbook.getId())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，ID标识和操作不能为空！");
            }
            cmsGuestbookService.delete(cmsGuestbook);
            return ApiResult.success(cmsGuestbook);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value = "ajaxDeletePL", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxDeletePL(@RequestBody ApiParam<List<String>> api) {
        try {
            List<String> entitys = api.getDatas();
            if(StringUtil.checkEmpty(entitys)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，"+ CoreJkey.JK_DATAS+"不能为空！");
            }
            CmsGuestbook cmsGuestbook = new CmsGuestbook(entitys);
            cmsGuestbookService.deletePL(cmsGuestbook);
            return ApiResult.success(cmsGuestbook);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }
}
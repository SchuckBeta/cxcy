package com.oseasy.act.modules.actyw.web;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.act.common.config.ActSval;
import com.oseasy.act.common.config.ActSval.ActEmskey;
import com.oseasy.act.modules.actyw.entity.ActYwGtheme;
import com.oseasy.act.modules.actyw.service.ActYwGthemeService;
import com.oseasy.act.modules.actyw.tool.process.vo.FormTheme;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiParam;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 表单组Controller.
 * @author chenh
 * @version 2018-09-06
 */
@Controller
@RequestMapping(value = "${adminPath}/actyw/actYwGtheme")
public class ActYwGthemeController extends BaseController {

	@Autowired
	private ActYwGthemeService entityService;

	@ModelAttribute
	public ActYwGtheme get(@RequestParam(required=false) String id) {
		ActYwGtheme entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = entityService.get(id);
		}
		if (entity == null){
			entity = new ActYwGtheme();
		}
		return entity;
	}


	@RequestMapping(value = {"list", ""})
	public String list(ActYwGtheme entity, HttpServletRequest request, HttpServletResponse response, Model model) {
//		Page<ActYwGtheme> page = entityService.findPage(new Page<ActYwGtheme>(request, response), entity);
//		model.addAttribute(Page.PAGE, page);
		return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwGthemeList";
	}

    @ResponseBody
    @RequestMapping(value = "ajaxList")
    public ApiResult ajaxlist(ActYwGtheme entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            Page<ActYwGtheme> page = entityService.findPage(new Page<ActYwGtheme>(request, response), entity);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "ajaxRest")
    public ApiResult ajaxRest() {
        try {
            entityService.deleteWLAll();
            entityService.insertPL(ActYwGtheme.converts(Arrays.asList(FormTheme.values())));
            return ApiResult.success();
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "ajaxSave", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiResult ajaxSave(@RequestBody ActYwGtheme entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if (!beanValidator(model, entity)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败！");
            }
            entityService.save(entity);
            return ApiResult.success(entity);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "ajaxUpdateName")
    public ApiResult ajaxUpdateName(ActYwGtheme entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if (StringUtil.isEmpty(entity.getId())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，ID标识不能为空！");
            }
            if (StringUtil.isEmpty(entity.getName())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败, 名称不能为空！");
            }
            entityService.save(entity);
            return ApiResult.success(entity);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "ajaxUpdateSname")
    public ApiResult ajaxUpdateSname(ActYwGtheme entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if (StringUtil.isEmpty(entity.getId())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，ID标识不能为空！");
            }
            if (StringUtil.isEmpty(entity.getSname())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败, 英文名不能为空！");
            }
            entityService.save(entity);
            return ApiResult.success(entity);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "ajaxUpdateEnable")
    public ApiResult ajaxUpdateEnable(ActYwGtheme entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if (StringUtil.isEmpty(entity.getId())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，ID标识不能为空！");
            }
            entityService.save(entity);
            return ApiResult.success(entity);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "ajaxUpdateSort")
    public ApiResult ajaxUpdateSort(ActYwGtheme entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if (StringUtil.isEmpty(entity.getId())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，ID标识不能为空！");
            }
            entityService.save(entity);
            return ApiResult.success(entity);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/ajaxUpdatePLSort", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiResult ajaxUpdatePLSort(@RequestBody ApiParam<List<ActYwGtheme>> api, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            List<ActYwGtheme> entitys = api.getDatas();
            if(StringUtil.checkEmpty(entitys)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，"+CoreJkey.JK_DATAS+"不能为空！");
            }
            entityService.updatePLSort(entitys);
            return ApiResult.success(entitys);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "ajaxUpdateDelFlag")
    public ApiResult ajaxUpdateDelFlag(ActYwGtheme entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if (StringUtil.isEmpty(entity.getId())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，ID标识不能为空！");
            }
            if (StringUtil.isEmpty(entity.getDelFlag())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败, 删除状态不能为空！");
            }
            entityService.save(entity);
            return ApiResult.success(entity);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "ajaxDelete")
    public ApiResult ajaxDelete(ActYwGtheme entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if (StringUtil.isEmpty(entity.getId())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，ID标识和操作不能为空！");
            }
            entityService.delete(entity);
            return ApiResult.success(entity);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "ajaxDeletePL", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiResult ajaxDeletePL(@RequestBody ActYwGtheme entity, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if (StringUtil.checkEmpty(entity.getIds())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，ID标识和操作不能为空！");
            }
            entityService.deletePL(entity);
            return ApiResult.success(entity);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }
}
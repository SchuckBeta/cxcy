/**
 * .
 */

package com.oseasy.cas.modules.cas.web;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.cas.common.config.CasSval;
import com.oseasy.cas.common.config.CasSval.CasEmskey;
import com.oseasy.cas.modules.cas.entity.SysCasUser;
import com.oseasy.cas.modules.cas.service.SysCasUserService;
import com.oseasy.cas.modules.cas.vo.CasType;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.util.common.utils.StringUtil;

/**
 * .
 * @author chenhao
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/cas")
public class CasController extends BaseController {
    public static Logger logger = Logger.getLogger(CasController.class);

    @Autowired
    SysCasUserService sysCasUserService;

    @ModelAttribute
    public SysCasUser get(@RequestParam(required=false) String id) {
        SysCasUser entity = null;
        if (StringUtil.isNotBlank(id)){
            entity = sysCasUserService.get(id);
        }
        if (entity == null){
            entity = new SysCasUser();
        }
        return entity;
    }

    @RequiresPermissions("cas:sysCasUser:view")
    @RequestMapping(value = {"list", ""})
    public String list(SysCasUser sysCasUser, HttpServletRequest request, HttpServletResponse response, Model model) {
        return CasSval.path.vms(CasEmskey.CAS.k()) + "/sysCasUserList";
    }

    @ResponseBody
    @RequestMapping(value="getSysCasUserList", method = RequestMethod.GET, produces = "application/json")
    public ApiResult getSysCasUserList(SysCasUser sysCasUser, HttpServletRequest request, HttpServletResponse response){
        try {
            Page<SysCasUser> page = sysCasUserService.findPage(new Page<SysCasUser>(request, response), sysCasUser);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value="ajaxEnable", method = RequestMethod.GET, produces = "application/json")
    public ApiResult ajaxEnable(SysCasUser entity, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            if (StringUtil.isNotEmpty(entity.getId()) && (entity.getEnable() != null)) {
                SysCasUser centity = sysCasUserService.get(entity.getId());
                centity.setEnable(entity.getEnable());
                sysCasUserService.save(centity);
                return ApiResult.success(centity);
            }
            return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, "ID和Enable为空");
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }
    @ResponseBody
    @RequestMapping(value="ajaxDeleteBy/{uid}", method = RequestMethod.GET, produces = "application/json")
    public ApiResult ajaxEnable(@PathVariable String uid) {
        try {
            if (StringUtil.isNotEmpty(uid)) {
                sysCasUserService.deletePlwlByUid(uid);
                return ApiResult.success();
            }
            return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, "uid为空");
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value="ajaxUpdateALLEnable", method = RequestMethod.GET, produces = "application/json")
    public ApiResult ajaxUpdateALLEnable(SysCasUser entity, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            sysCasUserService.updateALLEnable(entity);
            return ApiResult.success(entity);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 获取所有CAS类型.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxCasTypes", method = RequestMethod.GET)
    public ApiResult ajaxCasTypes() {
        try {
            return ApiResult.success(Arrays.asList(CasType.values()).toString());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }
}

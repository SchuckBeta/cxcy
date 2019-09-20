package com.oseasy.com.pcore.modules.syt.web;

import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.syt.entity.SysTenant;
import com.oseasy.com.pcore.modules.syt.service.SysTenantService;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by PW on 2019/4/19.
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/tenant")
public class SysTenantController extends BaseController {

    @Autowired
    private SysTenantService tenantService;

    @ModelAttribute
    public SysTenant get(@RequestParam(required=false) String id) {
        SysTenant entity = null;
        if (StringUtil.isNotBlank(id)){
            entity = tenantService.get(id);
        }
        if (entity == null){
            entity = new SysTenant();
        }
        return entity;
    }

    //跳转列表
    @RequiresPermissions("sys:menu:view")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return CoreSval.path.vms(CoreSval.CoreEmskey.SYS.k()) + "sysTenantList";
    }
    //租户管理列表
    @RequestMapping(value="tenantList", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult getTenantList(SysTenant sysTenant, HttpServletRequest request, HttpServletResponse response){
        try{
            User user = CoreUtils.getUser();
            if (User.isSuper(user)) {
                return ApiResult.success(tenantService.findPageNtTpl(new Page<SysTenant>(request, response), sysTenant));
            }else if (User.isAdmin(user) || User.isAdmyw(user)) {
                sysTenant.setIsTpl(CoreSval.Const.NO);
                return ApiResult.success(tenantService.findPageNt(new Page<SysTenant>(request, response), sysTenant));
            }else{
                sysTenant.setIsTpl(CoreSval.Const.NO);
                return ApiResult.success(tenantService.findPage(new Page<SysTenant>(request, response), sysTenant));
            }
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR);
        }
    }

    //校验唯一
    @RequestMapping(value="validTenant", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult validTenant(SysTenant sysTenant){
        try{
            if(StringUtil.isNotEmpty(sysTenant.getId())){
                SysTenant tempSysTenant = tenantService.get(sysTenant.getId());
                if(tempSysTenant != null){
                    if(!tempSysTenant.getSchoolCode().equals(sysTenant.getSchoolCode())){
                        SysTenant queryEntity = new SysTenant();
                        queryEntity.setSchoolCode(sysTenant.getSchoolCode());
                        List<SysTenant> sysTenantList = tenantService.findList(queryEntity);
                        if(StringUtil.checkNotEmpty(sysTenantList)){
                            return ApiResult.failed(ApiConst.CODE_MORE_ERROR,ApiConst.getErrMsg(ApiConst.CODE_MORE_ERROR));
                        }
                    }else if(!tempSysTenant.getSchoolName().equals(sysTenant.getSchoolName())){
                        SysTenant queryEntity = new SysTenant();
                        queryEntity.setSchoolName(sysTenant.getSchoolName());
                        List<SysTenant> sysTenantList = tenantService.findList(queryEntity);
                        if(StringUtil.checkNotEmpty(sysTenantList)){
                            return ApiResult.failed(ApiConst.CODE_MORE_ERROR,ApiConst.getErrMsg(ApiConst.CODE_MORE_ERROR));
                        }
                    }else if(!tempSysTenant.getDomainName().equals(sysTenant.getDomainName())){
                        SysTenant queryEntity = new SysTenant();
                        queryEntity.setDomainName(sysTenant.getDomainName());
                        List<SysTenant> sysTenantList = tenantService.findList(queryEntity);
                        if(StringUtil.checkNotEmpty(sysTenantList)){
                            return ApiResult.failed(ApiConst.CODE_MORE_ERROR,ApiConst.getErrMsg(ApiConst.CODE_MORE_ERROR));
                        }
                    }
                }
            }else{
                List<SysTenant> sysTenantList = tenantService.findList(sysTenant);
                if(StringUtil.checkNotEmpty(sysTenantList)){
                    return ApiResult.failed(ApiConst.CODE_MORE_ERROR,ApiConst.getErrMsg(ApiConst.CODE_MORE_ERROR));
                }
            }
            return ApiResult.success();
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR);
        }
    }

    //跳转表单
    @RequestMapping(value = {"toForm", ""})
    public String toForm(SysTenant sysTenant, Model model) {
        model.addAttribute("sysTenant",sysTenant);
        return CoreSval.path.vms(CoreSval.CoreEmskey.SYS.k()) + "sysTenantForm";
    }


    //修改、新增保存
    @RequestMapping(value="ajaxSaveTenant", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxSaveTenant(@RequestBody SysTenant sysTenant, Model model){
        try{
            if (!beanValidator(model, sysTenant)){
                return ApiResult.failed(ApiConst.PARAM_ERROR.getCode(), ApiConst.PARAM_ERROR.getMsg());
            }
            if(sysTenant != null){
                if(StringUtil.isEmpty(sysTenant.getId())){
                    sysTenant.setTenantId(sysTenant.getSchoolCode());
                    sysTenant.setType(Sval.EmPn.NSCHOOL.getPrefix());
                }

                if(StringUtil.isEmpty(sysTenant.getIsTpl())){
                    sysTenant.setIsTpl(CoreSval.Const.NO);
                }

                if(StringUtil.isEmpty(sysTenant.getStatus())){
                    sysTenant.setStatus(CoreSval.Const.NO);
                }
                if(StringUtil.isEmpty(sysTenant.getId())){
                    List<SysTenant> sysTenantList = tenantService.findList(sysTenant);
                    if(StringUtil.checkNotEmpty(sysTenantList) && sysTenantList.size() >= 1){
                        return ApiResult.failed(ApiConst.MORE_ERROR.getCode(), ApiConst.MORE_ERROR.getMsg());
                    }
                }
                tenantService.save(sysTenant);
                return ApiResult.success(sysTenant);
            }
            return ApiResult.failed(ApiConst.PARAM_ERROR.getCode(), ApiConst.PARAM_ERROR.getMsg());
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR);
        }
    }

    //删除、批量删除
    @RequestMapping(value="ajaxDeleteTenant", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxDeleteTenant(@RequestBody List<SysTenant> sysTenantList){
        try{
            tenantService.deletePL(sysTenantList);
            return ApiResult.success();
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR);
        }
    }
}

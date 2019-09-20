package com.oseasy.com.pcore.modules.syt.web;

import com.google.common.collect.Lists;
import com.oseasy.auy.modules.pcore.manager.OpenUtil;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CorePages;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.BaseEntity;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Area;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.AreaService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.pcore.modules.sys.vo.TenantCvtype;
import com.oseasy.com.pcore.modules.syt.vo.SytStatus;
import com.oseasy.com.pcore.modules.syt.entity.SysTenant;
import com.oseasy.com.pcore.modules.syt.vo.TenantSchoolType;
import com.oseasy.com.pcore.modules.syt.service.SysTenantService;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by PW on 2019/4/19.
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/tenant")
public class SysTenantComController extends BaseController {

    @Autowired
    private AreaService areaService;
    @Autowired
    private SysTenantService tenantService;

    /**
     * 开户.
     * @param id
     * @return ApiResult
     */
    @RequestMapping(value="ajaxOpen", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxOpen(String id){
        try{
            SysTenant curTenant = tenantService.get(id);
            if((curTenant == null) || StringUtil.isEmpty(curTenant.getType())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, "租户不存在或类型未定义.");
            }

            if((curTenant.getType()).equals(Sval.EmPn.NCENTER.getPrefix())){
                if(OpenUtil.openNce(curTenant.getTenantId())){
                    curTenant.setStatus(SytStatus.YKH.getKey());
                    tenantService.save(curTenant);
                    return ApiResult.success(curTenant);
                }
            }else if((curTenant.getType()).equals(Sval.EmPn.NPROVINCE.getPrefix())){
                if(OpenUtil.openNpr(curTenant.getTenantId())){
                    curTenant.setStatus(SytStatus.YKH.getKey());
                    tenantService.save(curTenant);
                    return ApiResult.success(curTenant);
                }
            }else if((curTenant.getType()).equals(Sval.EmPn.NSCHOOL.getPrefix())){
                if(OpenUtil.openNsc(curTenant.getTenantId())){
                    curTenant.setStatus(SytStatus.YKH.getKey());
                    tenantService.save(curTenant);
                    return ApiResult.success(curTenant);
                }
            }
            return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, "租户类型未定义.");
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR);
        }
    }

    /**
     * 切换租户.
     * @param id
     * @return ApiResult
     */
    @RequestMapping(value="ajaxChange", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxChange(String id, HttpServletRequest request, HttpServletResponse response){
        try{
            SysTenant curTenant = tenantService.get(id);
            if((curTenant == null) || StringUtil.isEmpty(curTenant.getTenantId())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, "租户不存在.");
            }
            TenantConfig config =  TenantConfig.getConfig();
            config.setIsFirst(false);
            config.setRequest(request);
            config.setResponse(response);
            config.setCuser(UserUtils.getUser());
            config.setChangeTid(curTenant.getTenantId());
            config = TenantConfig.initCache(config, TenantCvtype.CHANGE);
            return ApiResult.success(config, "租户切换成功.");
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR);
        }
    }


    /**
     * 重置模板.
     * @param id
     * @return ApiResult
     */
    @RequestMapping(value="ajaxResetTpl", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxTplReset(String id, HttpServletRequest request, HttpServletResponse response){
        try{
            SysTenant curTenant = tenantService.get(id);
            if((curTenant == null) || StringUtil.isEmpty(curTenant.getType())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, "租户不存在或类型未定义.");
            }

            if((curTenant.getType()).equals(Sval.EmPn.NCENTER.getPrefix())){
                if(OpenUtil.resetTNce(curTenant.getTenantId())){
                    curTenant.setStatus(SytStatus.YKH.getKey());
                    tenantService.save(curTenant);
                    return ApiResult.success(curTenant);
                }
            }else if((curTenant.getType()).equals(Sval.EmPn.NPROVINCE.getPrefix())){
                if(OpenUtil.resetTNpr(curTenant.getTenantId())){
                    curTenant.setStatus(SytStatus.YKH.getKey());
                    tenantService.save(curTenant);
                    return ApiResult.success(curTenant);
                }
            }else if((curTenant.getType()).equals(Sval.EmPn.NSCHOOL.getPrefix())){
                if(OpenUtil.resetTNsc(curTenant.getTenantId())){
                    curTenant.setStatus(SytStatus.YKH.getKey());
                    tenantService.save(curTenant);
                    return ApiResult.success(curTenant);
                }
            }
            return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, "租户类型未定义.");
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR);
        }
    }

    //查询条件--高校类型（租户）
    @RequestMapping(value="schoolTypeList", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult schoolTypeList(){
        try{
            return ApiResult.success(TenantSchoolType.getAll().toString());
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR);
        }
    }

    //查询条件--地区（租户）
    @RequestMapping(value="schoolCityList", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult schoolCityList(String type){
        try{
            Area area = new Area();
            //湖北省区域code为420000
            area.setCode("420000");
            List<Area> list = Lists.newArrayList();
            if(StringUtil.isNotEmpty(type) && type.equals("1")){
                return ApiResult.success(areaService.findCityList(area));
            }else{
                List<Area> lists = areaService.findCityList(area);
                for(Area area1 : lists){
                    if(!area1.getCode().equals(area.getCode())){
                        list.add(area1);
                    }
                }
                return ApiResult.success(list);

            }

        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR);
        }
    }

    //查询条件--学校（租户）
    @RequestMapping(value="schoolList", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult schoolList(SysTenant sysTenant){
        try{
            //todo 根据登录的角色，过滤查询的租户id
            List<SysTenant> list = null;
            User curuser = UserUtils.getUser();
            String curpn = CoreSval.getTenantCurrpn();
            sysTenant.setStatus(CoreSval.Const.YES);
            if((Sval.EmPn.NCENTER.getPrefix()).equals(curpn)){
                sysTenant.setFilterIds(CoreIds.filterChangeTenantByNc(curuser));
                if(User.isSuper(curuser)){
                    list = tenantService.findListNtTpl(sysTenant);
                }else{
                    sysTenant.setIsTpl(CoreSval.Const.NO);
                    list = tenantService.findListNtTpl(sysTenant);
                }
//                sysTenant.setIsTpl(CoreSval.Const.NO);
//                list = tenantService.findListNtTpl(sysTenant);
            }
            if((Sval.EmPn.NPROVINCE.getPrefix()).equals(curpn)){
                sysTenant.setFilterIds(CoreIds.filterTenantByNsAdmin());
                sysTenant.setStatus("1");
                list = tenantService.findListNtTpl(sysTenant);
            }

            if(list == null){
                list = Lists.newArrayList();
            }
            return ApiResult.success(list);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR);
        }
    }




}

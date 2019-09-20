package com.oseasy.com.pcore.modules.syt.web;

import com.google.common.collect.Lists;
import com.oseasy.auy.modules.pcore.manager.OpenUtil;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Area;
import com.oseasy.com.pcore.modules.sys.enums.RedisEnum;
import com.oseasy.com.pcore.modules.sys.service.AreaService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.pcore.modules.sys.vo.TenantCvtype;
import com.oseasy.com.pcore.modules.syt.entity.SysTenant;
import com.oseasy.com.pcore.modules.syt.service.SysTenantService;
import com.oseasy.com.pcore.modules.syt.vo.SytStatus;
import com.oseasy.com.pcore.modules.syt.vo.TenantSchoolType;
import com.oseasy.com.rediserver.common.config.RedisrSval;
import com.oseasy.com.rediserver.common.utils.JedisUtils;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by PW on 2019/4/19.
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/redis")
public class SysRedisController extends BaseController {
    //跳转列表
    @RequestMapping(value = {"list", ""})
    public String list() {

        return CoreSval.path.vms(CoreSval.CoreEmskey.SYS.k()) + "sysRedis";
    }

    //获取页面 最左边的大k
    @RequestMapping(value="ajaxGetAllCache", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxGetAllCache(){
        try{
//            List<String> keys = JedisUtils.hashGetKeys();
            RedisEnum[] redisLi= RedisEnum.values();
            List<Map<String,String>> list=new ArrayList<Map<String,String> >();
            String tenantId=TenantConfig.getCacheTenant();
            String cacheName="";
            if(tenantId!=null){
                cacheName=RedisrSval.dota+tenantId;
            }
            for(RedisEnum rdisEnum:redisLi ){
                Map mapValue=JedisUtils.hashGetKey(rdisEnum.getValue()+ cacheName);
                if(mapValue.size()>0){
                    Map<String,String> map=new HashMap<String,String>();
                    map.put("cache",rdisEnum.getValue());
                    list.add(map);
                }
            }
            return ApiResult.success(list);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR);
        }
    }
    //删除最左边的大k内的所有缓存
    @RequestMapping(value="ajaxDeleteCache", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxDeleteCache(String cache,String tenantId){
        try{
            String cacheName="";
            if(tenantId==null){
                cacheName=cache;
            }else{
                cacheName=cache+ RedisrSval.dota+tenantId;
            }
            JedisUtils.hashDelByKey(cacheName);
            return ApiResult.success();
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR);
        }
    }
    //根据大k获得下面所有缓存数据
    @RequestMapping(value="ajaxAllByKey", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxAllByKey(String cache,String tenantId){
        try{
            String cacheName="";
            if(tenantId==null){
                cacheName=cache;
            }else{
                cacheName=cache+ RedisrSval.dota+tenantId;
            }
            Map<String,Object> mapHash= JedisUtils.hashGetKey(cacheName);
            Set<String> set=mapHash.keySet();
            List<Map<String,String>> list=new ArrayList<Map<String,String> >();
            for (String str : set) {
                Map<String,String> map=new HashMap<String,String> ();
                map.put("cache",cache);
                map.put("key",str);
                list.add(map);
            }
            return ApiResult.success(list);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR);
        }
    }

    //根据大k 小k 获得缓存数据
    @RequestMapping(value="ajaxValueByKey", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxValueByKey(String cache,String key,String tenantId){
        try{
            String cacheName="";
            if(tenantId==null){
                cacheName=cache;
            }else{
                cacheName=cache+ RedisrSval.dota+tenantId;
            }
            Object object=JedisUtils.hashGet(cacheName,key);
            Map<String,String> map=new HashMap<String,String> ();
            map.put("cache",cache);
            map.put("key",key);
            if(object!=null){
                map.put("value",object.toString());
            }
            return ApiResult.success(map);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR);
        }
    }

    //根据大k 小k 删除缓存数据
    @RequestMapping(value="ajaxDelValueByKey", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxDelValueByKey(String cache,String key,String tenantId){
        try{
            String cacheName="";
            if(tenantId==null){
                cacheName=cache;
            }else{
                cacheName=cache+ RedisrSval.dota+tenantId;
            }
            JedisUtils.hashDel(cacheName,key);
            return ApiResult.success();
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR);
        }
    }

}

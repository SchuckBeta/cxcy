package com.oseasy.com.pcore.modules.sys.web;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.common.config.CoreSval.PassNot;
import com.oseasy.com.pcore.common.mapper.JsonMapper;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.service.SysService;
import com.oseasy.util.common.utils.SpSteel;
import com.oseasy.util.common.utils.StringUtil;

@Controller
public class SysController {
    /**
     * 日志对象.
     */
    protected Logger logger = LoggerFactory.getLogger(SysController.class);

  @Autowired
  private SysService sysService;

  /**
   * 获取系统同步时钟.
   * @param type 类型
   * @return Date
   */
  @ResponseBody
  @RequestMapping(value = "${adminPath}/sys/type/{type}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
  public Date getDbCurDate(@PathVariable String type) {
    return sysService.getDbCurDate(type);
  }

  @ResponseBody
  @RequestMapping(value = "${frontPath}/sys/type/{type}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
  public Date fgetDbCurDate(@PathVariable String type) {
    return sysService.getDbCurDate(type);
  }

  /**
   * 获取系统同步UUID.
   * @return String
   */
  @ResponseBody
  @RequestMapping(value = "${adminPath}/sys/uuid", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
  public Map<String, Object> uuid() {
      Map<String, Object> ret = new HashMap<>();
      ret.put(CoreJkey.JK_STATUS, true);
      ret.put(CoreJkey.JK_ID, IdGen.uuid());
    return ret;
  }

  @ResponseBody
  @RequestMapping(value = "${adminPath}/sys/uuids/{num}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
  public Map<String, Object> uuids(@PathVariable int num) {
      Map<String, Object> ret = new HashMap<>();
      List<String> ids = Lists.newArrayList();
      if(num <= 0){
          ret.put(CoreJkey.JK_STATUS, false);
          ret.put(CoreJkey.JK_ID, ids);
          return ret;
      }

      for (int i = 0; i < num; i++) {
          ids.add(IdGen.uuid());
      }
      ret.put(CoreJkey.JK_STATUS, true);
      ret.put(CoreJkey.JK_ID, JsonMapper.toJsonString(ids));
      return ret;
  }

  @ResponseBody
  @RequestMapping(value = "${frontPath}/sys/uuid", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
  public Map<String, Object> fuuid() {
    return uuid();
  }

  @ResponseBody
  @RequestMapping(value = "${frontPath}/sys/uuids/{num}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
  public Map<String, Object> fuuids(@PathVariable int num) {
      return uuids(num);
  }

  /**
   * 获取系统同步时钟.
   * @return Date
   */
  @ResponseBody
  @RequestMapping(value = "${adminPath}/sys/sysCurDateYmdHms", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
  public Date sysCurDateYmdHms() {
      return sysService.getSysCurDateYmdHms();
  }

  @ResponseBody
  @RequestMapping(value = "${frontPath}/sys/sysCurDateYmdHms", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
  public Date fsysCurDateYmdHms() {
      return sysService.getSysCurDateYmdHms();
  }

  /**
   * 状态.
   * @return ApiResult
   */
  @ResponseBody
  @RequestMapping(value = "${adminPath}/sys/passNots", method = RequestMethod.GET)
  public ApiResult sysPassNots() {
      try {
          return ApiResult.success(Arrays.asList(PassNot.values()).toString());
      }catch (Exception e){
          logger.error(e.getMessage());
          return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
      }
  }
  @ResponseBody
  @RequestMapping(value = "${frontPath}/sys/passNots", method = RequestMethod.GET)
  public ApiResult fsysPassNots() {
      try {
          return ApiResult.success(Arrays.asList(PassNot.values()).toString());
      }catch (Exception e){
          logger.error(e.getMessage());
          return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
      }
  }

  /**
   * 检查没有特殊字符(true,不包含；false包含).
   * @param str 带检查字符
   * @return ApiResult
   */
  @ResponseBody
  @RequestMapping(value = "${adminPath}/sys/checkNotSpSteel", method = RequestMethod.GET)
  public ApiResult checkNotSpSteel(String str) {
      try {
          str = str.trim();
          if(StringUtil.isEmpty(str)){
              return ApiResult.success(true);
          }
          if(SpSteel.checkNotFronts(str)){
              return ApiResult.success(true);
          }else{
              return ApiResult.success(false, "请不要输入特殊字符");
          }
      }catch (Exception e){
          logger.error(e.getMessage());
          return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
      }
  }

  @ResponseBody
  @RequestMapping(value = "${frontPath}/sys/checkNotSpSteel", method = RequestMethod.GET)
  public ApiResult fcheckNotSpSteel(String str) {
      try {
          str = str.trim();
          if(StringUtil.isEmpty(str)){
              return ApiResult.success(true);
          }
          if(SpSteel.checkNotFronts(str)){
              return ApiResult.success(true);
          }else{
              return ApiResult.success(false, "请不要输入特殊字符");
          }
      }catch (Exception e){
          logger.error(e.getMessage());
          return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
      }
  }
}

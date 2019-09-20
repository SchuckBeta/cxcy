package com.oseasy.pw.modules.pw.vo;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.pw.modules.pw.dao.PwRenewalRuleDao;
import com.oseasy.pw.modules.pw.entity.PwRenewalRule;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 创业基地模块系统配置常量类.
 * @author chenhao
 */
public class SvalPw {
  public static final String CACHE_PW_RENEWAL_RULE = "pwRenewalRule";
  private static PwRenewalRuleDao pwRenewalRuleDao = SpringContextHolder.getBean(PwRenewalRuleDao.class);

  /**
   * 最大申报记录数.
   */
  public static final String IS_MAX = "isMax";
  public static final String MAXNUM = "maxNum";
  /**
   * 最大续期天数.
   */
  public static final String MAXDAY_EXPIRE = "maxDayExpire";
  public static final String ENTER_LOGFILE = CoreSval.getConfig("enter.logFile");
  /**
   * 限制最大申请记录数.
   */
  public static final String ENTER_APPLY_MAXNUM = CoreSval.getConfig("enter.apply.maxNum");
  /**
   * 设置续期提醒天数.
   */
  public static final String ENTER_EXPIRE_KY = "expire";
  public static final String ENTER_EXPIRE_KEY = CoreSval.getConfig("enter.expire.key");
  public static final String ENTER_EXPIRE_AUTO = CoreSval.getConfig("enter.expire.isAuto");
  public static final String ENTER_EXPIRE_MAXDAY = CoreSval.getConfig("enter.expire.maxDay");
  public static final String ENTER_EXPIRE_LOGFILE = CoreSval.getConfig("enter.expire.logFile");

  /**
   * 设置已到期天数.
   */
  public static final String ENTER_YDQ_KY = "ydq";
  public static final String ENTER_YDQ_KEY = CoreSval.getConfig("enter.ydq.key");
  public static final String ENTER_YDQ_AUTO = CoreSval.getConfig("enter.ydq.isAuto");
  public static final String ENTER_YDQ_MAXDAY = CoreSval.getConfig("enter.ydq.maxDay");
  public static final String ENTER_YDQ_LOGFILE = CoreSval.getConfig("enter.ydq.logFile");

  /**
   * 设置退孵天数.
   */
  public static final String ENTER_EXIT_KY = "exit";
  public static final String ENTER_EXIT_KEY = CoreSval.getConfig("enter.exit.key");
  public static final String ENTER_EXIT_AUTO = CoreSval.getConfig("enter.exit.isAuto");
  public static final String ENTER_EXIT_MAXDAY = CoreSval.getConfig("enter.exit.maxDay");
  public static final String ENTER_EXIT_LOGFILE = CoreSval.getConfig("enter.exit.logFile");

  /**
   * 获取入驻配置缓存.
   * @return
   */
  public static PwRenewalRule getPwRenewalRule() {
    PwRenewalRule pwRenewalRule = (PwRenewalRule)CacheUtils.get(CACHE_PW_RENEWAL_RULE);
    if (pwRenewalRule == null) {
      pwRenewalRule = pwRenewalRuleDao.getPwRenewalRule();
      if (pwRenewalRule != null) {
        putCache(null, pwRenewalRule);
      }
    }
    return pwRenewalRule;
  }

  /**
   * 清除入驻配置缓存.
   * @return
   */
  public static void removeCache(String id) {
    if(StringUtil.isEmpty(id)){
      CacheUtils.remove(CACHE_PW_RENEWAL_RULE);
    }
    CacheUtils.remove(CACHE_PW_RENEWAL_RULE, id);
  }

  /**
   * 清除入驻配置缓存.
   * @return
   */
  public static void putCache(String id, PwRenewalRule entity) {
    if(StringUtil.isEmpty(id)){
      CacheUtils.put(CACHE_PW_RENEWAL_RULE, entity);
    }
    CacheUtils.put(CACHE_PW_RENEWAL_RULE, id, entity);
  }

  /**
   * 获取入驻日志文件根路径.
   * @return String
   */
  public static String getEnterLogFile() {
    return ENTER_LOGFILE;
  }

  /********************************************************
   * 获取最大申报数.
   * @return String
   */
  public static Integer getEnterApplyMaxNum() {
    PwRenewalRule pwRenewalRule = getPwRenewalRule();
    if((pwRenewalRule != null) && (pwRenewalRule.getApplyMaxNum() != null)){
      return pwRenewalRule.getApplyMaxNum();
    }
    return Integer.parseInt(ENTER_APPLY_MAXNUM);
  }

  /********************************************************
   * 获取到期标识.
   * @return String
   */
  public static String getEnterExpireKey() {
    if(StringUtil.isNotEmpty(ENTER_EXPIRE_KEY)){
      return ENTER_EXPIRE_KEY;
    }
    return ENTER_EXPIRE_KY;
  }

  /**
   * 获取到期提醒开关.
   * @return String
   */
  public static Boolean getEnterExpireAuto() {
    PwRenewalRule pwRenewalRule = getPwRenewalRule();
    if((pwRenewalRule != null) && (StringUtil.isNotEmpty(pwRenewalRule.getIsWarm()))){
      return (Const.YES).equals(pwRenewalRule.getIsWarm());
    }
    return (Const.YES).equals(ENTER_EXPIRE_AUTO);
  }

  /**
   * 获取到期提醒天数.
   * @return Integer
   */
  public static Integer getEnterExpireMaxDay() {
    PwRenewalRule pwRenewalRule = getPwRenewalRule();
    if((pwRenewalRule != null) && (pwRenewalRule.getWarmTime() != null)){
      return pwRenewalRule.getWarmTime();
    }
    return Integer.parseInt(ENTER_EXPIRE_MAXDAY);
  }

  /**
   * 获取到期提醒执行的日志文件.
   * @return String
   */
  public static String getEnterExpireLogFile() {
    if(StringUtil.isNotEmpty(ENTER_EXPIRE_LOGFILE)){
      return ENTER_EXPIRE_LOGFILE;
    }
    return getEnterLogFile();
  }

  /********************************************************
   * 获取已到期标识.
   * @return String
   */
  public static String getEnterYdqKey() {
    if(StringUtil.isNotEmpty(ENTER_YDQ_KEY)){
      return ENTER_YDQ_KEY;
    }
    return ENTER_YDQ_KY;
  }

  /**
   * 获取已到期开关.
   * @return String
   */
  public static Boolean getEnterYdqAuto() {
    if(StringUtil.isNotEmpty(ENTER_YDQ_AUTO)){
      return (Const.YES).equals(ENTER_YDQ_AUTO);
    }
    return true;
  }
  /**
   * 获取已到期天数.
   * @return Integer
   */
  public static Integer getEnterYdqMaxDay() {
    if(StringUtil.isNotEmpty(ENTER_YDQ_MAXDAY)){
      return Integer.parseInt(ENTER_YDQ_MAXDAY);
    }
    return 1;
  }

  /**
   * 获取已到期执行的日志文件.
   * @return String
   */
  public static String getEnterYdqLogFile() {
    if(StringUtil.isNotEmpty(ENTER_YDQ_LOGFILE)){
      return ENTER_YDQ_LOGFILE;
    }
    return getEnterLogFile();
  }

  /********************************************************
   * 获取退孵标识.
   * @return String
   */
  public static String getEnterExitKey() {
    if(StringUtil.isNotEmpty(ENTER_EXIT_KEY)){
      return ENTER_EXIT_KEY;
    }
    return ENTER_EXIT_KY;
  }

  /**
   * 获取退孵开关.
   * @return String
   */
  public static Boolean getEnterExitAuto() {
    PwRenewalRule pwRenewalRule = getPwRenewalRule();
    if((pwRenewalRule != null) && (StringUtil.isNotEmpty(pwRenewalRule.getIsHatback()))){
      return (Const.YES).equals(pwRenewalRule.getIsHatback());
    }
    return (Const.YES).equals(ENTER_EXIT_AUTO);
  }

  /**
   * 获取退孵天数.
   * @return Integer
   */
  public static Integer getEnterExitMaxDay() {
    PwRenewalRule pwRenewalRule = getPwRenewalRule();
    if((pwRenewalRule != null) && (pwRenewalRule.getHatbackTime() != null)){
      return pwRenewalRule.getHatbackTime();
    }
    return Integer.parseInt(ENTER_EXIT_MAXDAY);
  }

  /**
   * 获取退孵执行的日志文件.
   * @return String
   */
  public static String getEnterExitLogFile() {
    if(StringUtil.isNotEmpty(ENTER_EXIT_LOGFILE)){
      return ENTER_EXIT_LOGFILE;
    }
    return getEnterLogFile();
  }
}

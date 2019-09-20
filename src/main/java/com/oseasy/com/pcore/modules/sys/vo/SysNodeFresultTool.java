package com.oseasy.com.pcore.modules.sys.vo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.FreeMarkers;
import com.oseasy.util.common.utils.StringUtil;

public class SysNodeFresultTool {

  /**
   * 生成最终格式, 并渲染.
   * @param date 日期
   * @param fresult 结果集
   * @param model 渲染参数
   * @return String
   */
  public static String renderFormat(Date date, SysNodeFresult fresult, Map<String, Object> model) {
    if (model == null) {
      return null;
    }

    String result = genFormat(date, fresult);
    if (StringUtil.isEmpty(result)) {
      return null;
    }

    return FreeMarkers.renderString(StringUtil.trimToEmpty(result.toString()), model);
  }

  /**
   *
   * 生成最终格式,完成日期格式化和变量字符串回填.
   * @param date 日期
   * @param fresultVo 结果Vo
   * @return String
   */
  public static String genFormat(Date date, SysNodeFresult formatResult) {
    return genFormat(date, formatRunner(formatResult));
  }

  public static String genFormat(Date date, SysNodeFresultVo fresultVo) {
    if ((fresultVo.getFormatResults() == null) || (fresultVo.getFormatResults().size() <= 0)) {
      return null;
    }

    List<SysNodeFresult> fresults = fresultVo.getFormatResults();
    String lastDataFormat = genDateFormat(date, fresultVo.getFormatResult());
    if (lastDataFormat != null) {
      StringBuffer lastDataFormatBuffer = new StringBuffer(lastDataFormat);
      for (SysNodeFresult curFresult : fresults) {
        lastDataFormatBuffer.insert(curFresult.getIdxPrefix() + curFresult.getMaxPrefix(), curFresult.getParam());
      }
      return lastDataFormatBuffer.toString();
    }
    return null;
  }

  /**
   * 将所有变量字符移除,生成符合日期格式字符串.
   * @param date 日期
   * @param formatResult 结果格式对象.
   * @return
   */
  public static String genDateFormat(Date date, SysNodeFresult formatResult) {
    if ((date == null) || (formatResult == null) || (StringUtil.isEmpty(formatResult.getResult()))) {
      return null;
    }

    if (!formatResult.getIsEnable()) {
      SysNodeFresultVo fresultVo = formatRunner(formatResult);
      if (fresultVo != null) {
        formatResult = fresultVo.getFormatResult();
      }
    }
    return DateUtil.formatDate(date, formatResult.getResult());
  }

  /**
   * 循环将所有变量字符移除,直到所有被移除，返回结果格式Vo对象.
   * @param format 格式
   * @return FormatResultVo
   */
  public static SysNodeFresultVo formatRunner(SysNodeFresult fresult) {
    SysNodeFresultVo fresultVo = new SysNodeFresultVo();
    /**
     * 所有变量位置
     */
    List<SysNodeFresult> formatResults = Lists.newArrayList();
    /**
     * 最终的格式化字符串.
     */
    SysNodeFresult formatResult = fresult;
    while(!formatResult.getIsEnable()) {
      SysNodeFresult curfresult = formatDeal(formatResult);
      formatResult = curfresult;

      formatResults.add(curfresult);
      if (formatResult.getIsEnable()) {
        fresultVo = new SysNodeFresultVo(formatResult);
        break;
      }
    }
    fresultVo.setFormatResults(formatResults);
    return fresultVo;
  }

  /**
   * 将所有变量字符移除,返回结果格式对象.
   * @param string 格式
   * @return FormatResult
   */
  public static SysNodeFresult formatDeal(SysNodeFresult fresult) {
    Integer idxPrefix = null;
    Integer idxPostfix = null;
    String format = fresult.getResult();
    StringBuffer newFormat = new StringBuffer();
    SysNodeFresult formatResult = new SysNodeFresult();

    /**
     * 获取特殊标识位置.
     */
    if (format.indexOf(SysNodeFresult.FP_PREFIX) != -1) {
      idxPrefix = format.indexOf(SysNodeFresult.FP_PREFIX);
    }

    if (format.indexOf(SysNodeFresult.FP_POSTFIX) != -1) {
      idxPostfix = format.indexOf(SysNodeFresult.FP_POSTFIX);
    }

    if ((idxPrefix != null) && (idxPostfix != null) && (idxPrefix != -1) && (idxPostfix != -1)) {
      fresult.setIsEnable(false);
    }else{
      fresult.setIsEnable(true);
    }

    if (!fresult.getIsEnable()) {
      newFormat.append(format.substring(0, idxPrefix));
      newFormat.append(format.substring((idxPostfix + 1), format.length()));
      formatResult.setParam(format.substring((idxPrefix), (idxPostfix + 1)));

      /**
       * 验证是否已经适合做日期格式化.
       */
    }else{
      newFormat.append(format);
    }

    formatResult.setIdxPrefix(idxPrefix);
    formatResult.setIdxPostfix(idxPostfix);
    if (formatResult.getParam() != null) {
      if (fresult.getMaxPrefix() == null) {
        formatResult.setMaxPrefix(0);
      }else{
        formatResult.setMaxPrefix(fresult.getMaxPrefix() + formatResult.getParam().length());
      }
    }
    formatResult.setResult(newFormat.toString());
    formatResult.setIsEnable(!formatValidate(formatResult.getResult()));
    return formatResult;
  }

  /**
   * 格式校验是否含有变量字符${xxx}.
   * @param format 格式
   * @return Boolean
   */
  public static Boolean formatValidate(String format) {
    Integer idxPrefix = null;
    Integer idxPostfix = null;
    if (format.indexOf(SysNodeFresult.FP_PREFIX) != -1) {
      idxPrefix = format.indexOf(SysNodeFresult.FP_PREFIX);
    }

    if (format.indexOf(SysNodeFresult.FP_POSTFIX) != -1) {
      idxPostfix = format.indexOf(SysNodeFresult.FP_POSTFIX);
    }

    if ((idxPrefix != null) && (idxPostfix != null) && (idxPrefix != -1) && (idxPostfix != -1)) {
      return true;
    }
    return false;
  }
}

package com.oseasy.sys.modules.sys.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.sys.modules.sys.entity.SysNumberRule;
import com.oseasy.sys.modules.sys.entity.SysNumberRuleDetail;
import com.oseasy.sys.modules.sys.service.SysNumberRuleDetailService;
import com.oseasy.sys.modules.sys.service.SysNumberRuleService;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.RunException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 编号规则工具类
 *
 * @author 李志超
 * @version 2018-05-24
 */
public class NumRuleUtils {

    private static SysNumberRuleService sysNumberRuleService = SpringContextHolder.getBean("sysNumberRuleService");
    private static SysNumberRuleDetailService sysNumberRuleDetailService= SpringContextHolder.getBean("sysNumberRuleDetailService");

    /**
     * 根据应用类型获取编码
     * @param appType  业务类型id
     * @return
     */
    public static String getNumberText(String appType) {
        return getNumberText(appType, null,"", false);
    }

    /**
     * 根据应用类型和项目年份生成编码
     * @param appType 业务类型id
     * @param year 项目年份
     * @return
     */
    public static String getNumberText(String appType, String year) {
        return getNumberText(appType, year,"", false);
    }

    /**
     * 省项目大赛规则
     * @param appType
     * @param year
     * @return
     */
    public static String getProvinceNumberText(String appType, String year, Date subTime) {
        return getProvNumberText(appType, year,"", false,subTime);
    }


    /**
     * 生产编码
     * @param appType  业务类型id
     * @param isShow 是否为显示demo使用
     * @return
     */
//    public static String getNumberText(String appType, String year, boolean isShow) {
//        SysNumberRule sysNumberRule = sysNumberRuleService.getRuleByAppType(appType, null);
//        if(sysNumberRule==null){
//            return "";
//        }
//        String rule = sysNumberRule.getRule();
//        String rgex = "\\{(.*?)\\}";
//        Pattern pattern = Pattern.compile(rgex);// 匹配的模式
//        Matcher m = pattern.matcher(rule);
//        while (m.find()) {
//            String part = m.group().replaceAll("[{}]", "");
//            String[] rules = part.split("-");
//            String type = rules[0];
//            switch (type) {
//                case EnumUtils.DATE_FLAG: //
//                    rule = rule.replaceAll("\\{" + part + "\\}", DateUtil.formatDate(new Date(), rules[1]));
//                    break;
//                case EnumUtils.RANDOM_FLAG:
//                    String random = generateRandomText(Integer.parseInt(rules[1]));
//                    rule = rule.replaceAll("\\{" + part + "\\}", random);
//                    break;
//                case EnumUtils.CUSTOM_FLAG:
//                    String custom = generateCustomText(sysNumberRule, Integer.parseInt(rules[1]), isShow);
//                    rule = rule.replaceAll("\\{" + part + "\\}", custom);
//                    break;
//                case EnumUtils.PRO_YEAR:
//                    if (isShow) {
//                        rule = rule.replaceAll("\\{" + part + "\\}", DateUtil.formatDate(new Date(), "yyyy"));
//                    } else {
//                        if (StringUtils.isEmpty(year)) {
//                            throw new RunException("项目年份不能为空！");
//                        }
//                        rule = rule.replaceAll("\\{" + part + "\\}", year);
//                    }
//                    break;
//                case EnumUtils.PRO_TYPE:
//                    if (isShow) {
//                        rule = rule.replaceAll("\\{" + part + "\\}", "s");
//                    } else {
//                        rule = rule.replaceAll("\\{" + part + "\\}", year);
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }
//        return rule;
//    }

    /**
    * 生产编码
    * @param appType  业务类型id
    * @param isShow 是否为显示demo使用
    * @return
    */
   public static String getNumberText(String appType, String year,String proType, boolean isShow) {
       SysNumberRule sysNumberRule = sysNumberRuleService.getRuleByAppType(appType, null);
       if(sysNumberRule==null){
           return "";
       }
       String rule = sysNumberRule.getRule();
       String rgex = "\\{(.*?)\\}";
       Pattern pattern = Pattern.compile(rgex);// 匹配的模式
       Matcher m = pattern.matcher(rule);
       while (m.find()) {
           String part = m.group().replaceAll("[{}]", "");
           String[] rules = part.split("-");
           String type = rules[0];
           switch (type) {
               case SysEnumUtils.DATE_FLAG: //
                   rule = rule.replaceAll("\\{" + part + "\\}", DateUtil.formatDate(new Date(), rules[1]));
                   break;
               case SysEnumUtils.RANDOM_FLAG:
                   String random = generateRandomText(Integer.parseInt(rules[1]));
                   rule = rule.replaceAll("\\{" + part + "\\}", random);
                   break;
               case SysEnumUtils.CUSTOM_FLAG:
                   String custom = generateCustomText(sysNumberRule, Integer.parseInt(rules[1]), isShow);
                   rule = rule.replaceAll("\\{" + part + "\\}", custom);
                   break;
               case SysEnumUtils.PRO_YEAR:
                   if (isShow) {
                       rule = rule.replaceAll("\\{" + part + "\\}", DateUtil.formatDate(new Date(), "yyyy"));
                   } else {
                       if (StringUtils.isEmpty(year)) {
                           throw new RunException("项目年份不能为空！");
                       }
                       rule = rule.replaceAll("\\{" + part + "\\}", year);
                   }
                   break;
               case SysEnumUtils.PRO_TYPE:
                   if (isShow) {
                       rule = rule.replaceAll("\\{" + part + "\\}", "S");
                   } else {
//                       //创业实践项目后缀+S
//                       if(StringUtil.isNotEmpty(proType)&& "3".equals(proType)){
//                           proType="S";
//                           rule = rule.replaceAll("\\{" + part + "\\}", proType);
//                       //创业训练项目后缀+X
//                       }else if(StringUtil.isNotEmpty(proType)&& "2".equals(proType)){
//                           proType="X";
//                           rule = rule.replaceAll("\\{" + part + "\\}", proType);
//                       }
                       if(StringUtil.isNotEmpty(proType)) {
                           String proTypeValue = generateProTypeValue(sysNumberRule, proType);
                           rule = rule.replaceAll("\\{" + part + "\\}", proTypeValue);
                       }else{
                           rule = rule.replaceAll("\\{" + part + "\\}", "");
                       }
                   }
                   break;
               default:
                   break;
           }
       }
       return rule;
   }


    public static String getProvNumberText(String appType, String year,String proType, boolean isShow, Date subTime) {
        SysNumberRule sysNumberRule = sysNumberRuleService.getRuleByAppType(appType, null);
        if(sysNumberRule==null){
            return "";
        }
        String rule = sysNumberRule.getRule();
        String rgex = "\\{(.*?)\\}";
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式
        Matcher m = pattern.matcher(rule);
        while (m.find()) {
            String part = m.group().replaceAll("[{}]", "");
            String[] rules = part.split("-");
            String type = rules[0];
            switch (type) {
                case SysEnumUtils.DATE_FLAG: //
                    rule = rule.replaceAll("\\{" + part + "\\}", DateUtil.formatDate(subTime, rules[1]));
                    break;
                case SysEnumUtils.RANDOM_FLAG:
                    String random = generateRandomText(Integer.parseInt(rules[1]));
                    rule = rule.replaceAll("\\{" + part + "\\}", random);
                    break;
                case SysEnumUtils.CUSTOM_FLAG:
                    String custom = generateCustomText(sysNumberRule, Integer.parseInt(rules[1]), isShow);
                    rule = rule.replaceAll("\\{" + part + "\\}", custom);
                    break;
                case SysEnumUtils.PRO_YEAR:
                    if (isShow) {
                        rule = rule.replaceAll("\\{" + part + "\\}", DateUtil.formatDate(new Date(), "yyyy"));
                    } else {
                        if (StringUtils.isEmpty(year)) {
                            throw new RunException("项目年份不能为空！");
                        }
                        rule = rule.replaceAll("\\{" + part + "\\}", year);
                    }
                    break;
                case SysEnumUtils.PRO_TYPE:
                    if (isShow) {
                        rule = rule.replaceAll("\\{" + part + "\\}", "S");
                    } else {
//                       //创业实践项目后缀+S
//                       if(StringUtil.isNotEmpty(proType)&& "3".equals(proType)){
//                           proType="S";
//                           rule = rule.replaceAll("\\{" + part + "\\}", proType);
//                       //创业训练项目后缀+X
//                       }else if(StringUtil.isNotEmpty(proType)&& "2".equals(proType)){
//                           proType="X";
//                           rule = rule.replaceAll("\\{" + part + "\\}", proType);
//                       }
                        if(StringUtil.isNotEmpty(proType)) {
                            String proTypeValue = generateProTypeValue(sysNumberRule, proType);
                            rule = rule.replaceAll("\\{" + part + "\\}", proTypeValue);
                        }else{
                            rule = rule.replaceAll("\\{" + part + "\\}", "");
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return rule;
    }

    private static String generateProTypeValue(SysNumberRule sysNumberRule, String proType) {
        SysNumberRuleDetail sysNumberRuleDetail=sysNumberRuleDetailService.getBySysNumberRule(sysNumberRule.getId(),SysEnumUtils.PRO_TYPE);
        String value=sysNumberRuleDetail.getTypeValue();
        JSONObject jsonobject = JSONObject.fromObject(value);
        JSONArray jsonArry = jsonobject.getJSONArray("map");
                //JSONArray.fromObject(value);//value是json字符串
        String ret="";
        for(int i=0;i<jsonArry.size();i++){
            JSONObject jSONObject= (JSONObject)jsonArry.get(i);
            if(jSONObject.get(proType)!=null){
                ret=(String)jSONObject.get(proType);
            }
        }
        return ret;
    }


    public static String getNumberText(String appType, String year,Date subDate,boolean isShow) {
           SysNumberRule sysNumberRule = sysNumberRuleService.getRuleByAppType(appType, null);
           if(sysNumberRule==null){
               return "";
           }
           String rule = sysNumberRule.getRule();
           String rgex = "\\{(.*?)\\}";
           Pattern pattern = Pattern.compile(rgex);// 匹配的模式
           Matcher m = pattern.matcher(rule);
           while (m.find()) {
               String part = m.group().replaceAll("[{}]", "");
               String[] rules = part.split("-");
               String type = rules[0];
               switch (type) {
                   case SysEnumUtils.DATE_FLAG: //
                       rule = rule.replaceAll("\\{" + part + "\\}", DateUtil.formatDate(subDate, rules[1]));
                       break;
                   case SysEnumUtils.RANDOM_FLAG:
                       String random = generateRandomText(Integer.parseInt(rules[1]));
                       rule = rule.replaceAll("\\{" + part + "\\}", random);
                       break;
                   case SysEnumUtils.CUSTOM_FLAG:
                       String custom = generateCustomText(sysNumberRule, Integer.parseInt(rules[1]), isShow);
                       rule = rule.replaceAll("\\{" + part + "\\}", custom);
                       break;
                   case SysEnumUtils.PRO_YEAR:
                       if (isShow) {
                           rule = rule.replaceAll("\\{" + part + "\\}", DateUtil.formatDate(new Date(), "yyyy"));
                       } else {
                           if (StringUtils.isEmpty(year)) {
                               throw new RunException("项目年份不能为空！");
                           }
                           rule = rule.replaceAll("\\{" + part + "\\}", year);
                       }
                       break;
                   default:
                       break;
               }
           }
           return rule;
       }
    /**
     * 根据应用类型和级别，生成最终编码
     * @param numberText 临时编号
     * @param appType  应用类型
     * @param level   项目级别
     * @return 最终编码
     */
    public static String getFinalNumberText(String numberText, String appType, String level) {
        int index = NumRuleUtils.getLevelIndexInNumberText(appType);
        StringBuffer stringBuffer = new StringBuffer(numberText);
        stringBuffer.insert(index, level).toString();
        return stringBuffer.toString();
    }

    /**
     * 获取项目级别在编码中下标
     * @param appType 应用类型
     * @return
     */
    public static Integer getLevelIndexInNumberText(String appType) {
        SysNumberRule sysNumberRule = sysNumberRuleService.getRuleByAppType(appType, null);
        return sysNumberRule.getLevelIndex();
    }

    /**
     * 生成指定位数随机数
     * @param length 随机数长度
     * @return
     */
    private static String generateRandomText(int length) {
        StringBuffer stringBuffer = new StringBuffer();
        String numStr = "0123456789";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int num = random.nextInt(numStr.length());
            stringBuffer.append(numStr.charAt(num));
        }
        return stringBuffer.toString();
    }

    /**
     * 生成自定义编码
     * @param sysNumberRule
     * @param length
     * @param isShow  true : 显示值；false : 为业务值
     * @return
     */
    private static String generateCustomText(SysNumberRule sysNumberRule, int length, boolean isShow) {
        int num = 1;
        //获取最后一次的更新时间
        Date updateDate = sysNumberRule.getUpdateDate();

        //获取昨天日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date yesterday = calendar.getTime();

        //判断当前日期为最后一次更新日期，并且最后一次更新日期不是昨天时，直接获取当前自增值
        if (!DateUtil.formatDate(yesterday, "yyyyMMdd").equals(DateUtil.formatDate(updateDate, "yyyyMMdd"))
                && DateUtil.formatDate(new Date(), "yyyyMMdd").equals(DateUtil.formatDate(updateDate, "yyyyMMdd"))) {
            num = sysNumberRule.getIncreNum();
        }
        if (!isShow) {
            //将自增值持久化
            sysNumberRule.setIncreNum(num + 1);
            sysNumberRule.setUpdateDate(new Date());
            sysNumberRule.setUpdateBy(UserUtils.getUser());
            sysNumberRuleService.update(sysNumberRule);
        }

        //如果当前自增值长度已大于限定长度时直接返回自增值，未达到限定长度时进行站位处理
        if (String.valueOf(num).length() > length) {
            return num + "";
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < length - String.valueOf(num).length(); i++) {
                stringBuffer.append("0");
            }
            stringBuffer.append(num);
            return stringBuffer.toString();
        }
    }
}

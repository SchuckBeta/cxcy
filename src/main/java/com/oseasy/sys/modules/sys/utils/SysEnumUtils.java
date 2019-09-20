package com.oseasy.sys.modules.sys.utils;

import com.oseasy.sys.modules.sys.entity.SysNumberRuleDetail;

/**
 * Created by zhangzheng on 2017/3/18.
 *
 * update by 李志超 on 2018-05-24 新增RuleType枚举
 *
 */
public class SysEnumUtils {
    //编码固定值标识
    public final static String FIXED_FLAG = "FIXED";
    //编码日期标识
    public final static String DATE_FLAG = "DATE";
    //编码随机数标识
    public final static String RANDOM_FLAG = "RANDOM";
    //编码项目级别标识
    public final static String LEVEL_FLAG = "LEVEL";
    //编码自定义标识
    public final static String CUSTOM_FLAG = "CUSTOM";
    //项目申报年份
    public final static String PRO_YEAR = "YEAR";
    //项目类型
    public final static String PRO_TYPE= "PROTYPE";


    public enum RuleType {

        FIXED_VALUE(FIXED_FLAG, "固定值"),
        DATE_VALUE(DATE_FLAG, "申报日期"),
        RANDOM_VALUE(RANDOM_FLAG, "随机数"),
        PROJECT_LEVEL_VALUE(LEVEL_FLAG, "项目级别"),
        PRO_YEAR_VALUE(PRO_YEAR, "项目年份"),
        PRO_TYPE_VALUE(PRO_TYPE, "项目类别"),
        CUSTOM_VALUE(CUSTOM_FLAG, "自定义编号");

        private String key;
        private String value;

        RuleType(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public static String getRuleText(SysNumberRuleDetail detail) {
            String ruleText = "";
            switch (detail.getRuleType()) {
                case FIXED_FLAG:
                    ruleText = detail.getText();
                    break;
                case DATE_FLAG:
                    ruleText = "{" + DATE_FLAG + "-" + detail.getText() + "}";
                    break;
                case RANDOM_FLAG:
                    ruleText = "{" + RANDOM_FLAG + "-" + detail.getNumLength() + "}";
                    break;
                case CUSTOM_FLAG:
                    ruleText = "{" + CUSTOM_FLAG + "-" + detail.getNumLength() + "}";
                    break;
                case PRO_YEAR:
                    ruleText = "{" + PRO_YEAR + "}";
                    break;
                case PRO_TYPE:
                    ruleText = "{" + PRO_TYPE + "}";
                    break;
                default:
                    ruleText = "";
                    break;
            }
            return ruleText;
        }
    }

}

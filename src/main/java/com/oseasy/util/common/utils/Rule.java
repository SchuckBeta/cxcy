package com.oseasy.util.common.utils;

/**
 * Created by victor on 2017/3/11.
 */
public class Rule {
    private    String prefix;//
    private    String suffix; //
    private      String d_format;//日期格式
    private      String t_format;//时间格式
    private      Integer start;//开始值
    private      Integer digit;//位数


    public Rule(String prefix, String suffix, String d_format, String t_format, Integer start, Integer digit) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.d_format = d_format;
        this.t_format = t_format;
        this.start = start;
        this.digit = digit;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getD_format() {
        return d_format;
    }

    public void setD_format(String d_format) {
        this.d_format = d_format;
    }

    public String getT_format() {
        return t_format;
    }

    public void setT_format(String t_format) {
        this.t_format = t_format;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getDigit() {
        return digit;
    }

    public void setDigit(Integer digit) {
        this.digit = digit;
    }


}

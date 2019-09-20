package com.oseasy.act.modules.actyw.tool.process.vo;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 流程主题固定栏目.
 * 对应 FormTheme .
 *
 * @author chenhao
 */
public enum FlowPcms {
    FPCMR_APPLY(FormTheme.F_MR, FlowPcmsType.FPC_HOME.getKey(), ""),
    FPMD_APPLY(FormTheme.F_MD, FlowPcmsType.FPC_HOME.getKey(), ""),
    FPGT_APPLY(FormTheme.F_GT, FlowPcmsType.FPC_HOME.getKey(), "");

    private FormTheme theme;//主题
    private String key;//栏目标识
    private String url;//

    private FlowPcms(FormTheme theme, String key, String url) {
        this.theme = theme;
        this.key = key;
        this.url = url;
    }

    /**
     * 根据主题和key获取枚举 .
     *
     * @author chenhao
     * @param theme
     *            主题标识
     * @param key
     *            页面标识
     * @return FlowPcms
     */
    public static FlowPcms getByKey(FormTheme theme, String key) {
        if ((theme != null) && (key != null)) {
            FlowPcms[] entitys = FlowPcms.values();
            for (FlowPcms entity : entitys) {
                if ((theme).equals(entity.getTheme()) && (key).equals(entity.getKey())) {
                    return entity;
                }
            }
        }
        return null;
    }

    /**
     * 根据主题获取枚举 .
     *
     * @author chenhao
     * @param theme
     *            主题标识
     * @return FlowPcms
     */
    public static List<FlowPcms> getByTheme(FormTheme theme) {
        List<FlowPcms> FlowPcmss = Lists.newArrayList();
        if ((theme != null)) {
            FlowPcms[] entitys = FlowPcms.values();
            for (FlowPcms entity : entitys) {
                if ((theme).equals(entity.getTheme())) {
                    FlowPcmss.add(entity);
                }
            }
        }
        return FlowPcmss;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public FormTheme getTheme() {
        return theme;
    }

    public void setTheme(FormTheme theme) {
        this.theme = theme;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
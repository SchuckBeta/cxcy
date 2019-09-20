package com.oseasy.act.modules.actyw.tool.process.vo;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 流程主题固定页面.
 * 对应 FormTheme .
 * @author chenhao
 */
public enum FlowPmenu {
    FPMMR_APPLY(FormTheme.F_MR, FlowPmenuType.FPM_HOME.getKey(), ""),
    FPMMD_APPLY(FormTheme.F_MD, FlowPmenuType.FPM_HOME.getKey(), ""),
    FPMGT_APPLY(FormTheme.F_GT, FlowPmenuType.FPM_HOME.getKey(), "");

    private FormTheme theme;//主题
    private String key;//栏目标识
    private String url;//

    private FlowPmenu(FormTheme theme, String key, String url) {
        this.theme = theme;
        this.key = key;
        this.url = url;
    }

 /**
  * 根据key获取枚举 .
  *
  * @author chenhao
  * @param key 枚举标识
  * @return FlowPmenu
  */
 public static FlowPmenu getByKey(String key) {
   if ((key != null)) {
     FlowPmenu[] entitys = FlowPmenu.values();
     for (FlowPmenu entity : entitys) {
       if ((key).equals(entity.getKey())) {
         return entity;
       }
     }
   }
   return null;
 }


 /**
  * 根据主题和key获取枚举 .
  *
  * @author chenhao
  * @param theme
  *            主题标识
  * @param key
  *            页面标识
  * @return FlowPmenu
  */
 public static FlowPmenu getByKey(FormTheme theme, String key) {
     if ((theme != null) && (key != null)) {
         FlowPmenu[] entitys = FlowPmenu.values();
         for (FlowPmenu entity : entitys) {
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
  * @return List
  */
 public static List<FlowPmenu> getByTheme(FormTheme theme) {
     List<FlowPmenu> flowPmenus = Lists.newArrayList();
     if ((theme != null)) {
         FlowPmenu[] entitys = FlowPmenu.values();
         for (FlowPmenu entity : entitys) {
             if ((theme).equals(entity.getTheme())) {
                 flowPmenus.add(entity);
             }
         }
     }
     return flowPmenus;
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

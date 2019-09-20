package com.oseasy.act.modules.actyw.tool.process.vo;

/**
 * 小类别.
 * @author chenhao
 */
public enum FlowPcategoryType {
  PCT_DASAI("competition_net_type", "大赛类别")
 ,PCT_XM("project_type", "项目类别")
 ,PCT_TECHNOLOGY(null, "科研类别")
 ,PCT_QINGJIA(null, "请假类别")
 ,PCT_SCORE("sco_pctype", "学分类别")
 ,PCT_APPOINTMENT(null, "预约类别")
 ,PCT_ENTER(null, "入驻类别")
 ,PCT_ALL(null, "通用类别");

    public static final String HAS_PCTYPE = "hasPctype";
    public static final String DICT_PCTYPES = "dictPctype";
    public static final String DICT_PCTYPESKS = "dictPctypeks";
    public static final String DICT_PCTYPE_KEY = "dictPctypeKey";
 private String key;
 private String name;

 private FlowPcategoryType(String key, String name) {
   this.key = key;
   this.name = name;
 }

 /**
  * 根据key获取枚举 .
  *
  * @author chenhao
  * @param key 枚举标识
  * @return FlowPcategoryType
  */
 public static FlowPcategoryType getByKey(String key) {
   if ((key != null)) {
     FlowPcategoryType[] entitys = FlowPcategoryType.values();
     for (FlowPcategoryType entity : entitys) {
       if ((entity.getKey() != null) && (key).equals(entity.getKey())) {
         return entity;
       }
     }
   }
   return null;
 }

  public String getKey() {
    return key;
  }

  public String getName() {
    return name;
  }
}

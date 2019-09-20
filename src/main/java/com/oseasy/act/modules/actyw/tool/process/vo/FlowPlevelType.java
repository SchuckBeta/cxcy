package com.oseasy.act.modules.actyw.tool.process.vo;
/**
 * 级别
 * @author chenhao
 *
 */
public enum FlowPlevelType {
  PLT_DASAI("gcontest_level", "大赛级别")
 ,PLT_XM("project_degree", "项目级别")
 ,PLT_TECHNOLOGY(null, "科研级别")
 ,PLT_QINGJIA(null, "请假级别")
 ,PLT_SCORE(null, "学分级别")
 ,PLT_APPOINTMENT(null, "预约级别")
 ,PLT_ENTER(null, "入驻级别")
 ,PLT_ALL(null, "通用级别");

 private String key;
 private String name;

 private FlowPlevelType(String key, String name) {
   this.key = key;
   this.name = name;
 }

 /**
  * 根据key获取枚举 .
  *
  * @author chenhao
  * @param key 枚举标识
  * @return FlowPlevelType
  */
 public static FlowPlevelType getByKey(String key) {
   if ((key != null)) {
     FlowPlevelType[] entitys = FlowPlevelType.values();
     for (FlowPlevelType entity : entitys) {
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

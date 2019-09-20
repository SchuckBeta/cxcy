package com.oseasy.act.modules.actyw.tool.process.vo;

/**
 * 结果状态.
 * @author chenhao
 *
 */
public enum FlowPstatusType {
  PST_DASAI("competition_college_prise", "大赛结果状态")
 ,PST_XM("project_result", "项目结果状态")
 ,PST_TECHNOLOGY(null, "科研结果状态")
 ,PST_QINGJIA(null, "请假结果状态")
 ,PST_SCORE(null, "学分结果状态")
 ,PST_APPOINTMENT(null, "预约结果状态")
 ,PST_ENTER(null, "入驻结果状态")
 ,PST_ALL(null, "通用结果状态");

 private String key;
 private String name;

 private FlowPstatusType(String key, String name) {
   this.key = key;
   this.name = name;
 }

 /**
  * 根据key获取枚举 .
  *
  * @author chenhao
  * @param key 枚举标识
  * @return FlowPstatusType
  */
 public static FlowPstatusType getByKey(String key) {
   if ((key != null)) {
     FlowPstatusType[] entitys = FlowPstatusType.values();
     for (FlowPstatusType entity : entitys) {
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

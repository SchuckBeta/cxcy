package com.oseasy.act.modules.actyw.tool.process.vo;
/**
 * 类型.
 * @author chenhao
 */
public enum FlowPtype {
  PTT_DASAI("competition_type", "大赛类型")
 ,PTT_XM("project_style", "项目类型")
 ,PTT_TECHNOLOGY(null, "科研类型")
 ,PTT_QINGJIA(null, "请假类型")
 ,PTT_SCORE("sco_ptype", "学分类型")
 ,PTT_APPOINTMENT("pw_appointment_fptype", "预约类型")
 ,PTT_ENTER("pw_enter_fptype", "入驻类型")
 ,PTT_ALL(null, "通用类型");

    public static final String HAS_PTYPE = "hasPtype";
    public static final String DICT_PTYPES = "dictPtype";
    public static final String DICT_PTYPESKS = "dictPtypeks";
    public static final String DICT_PTYPE_KEY = "dictPtypeKey";
 private String key;
 private String name;

 private FlowPtype(String key, String name) {
   this.key = key;
   this.name = name;
 }

 /**
  * 根据key获取枚举 .
  *
  * @author chenhao
  * @param key 枚举标识
  * @return FlowPtype
  */
 public static FlowPtype getByKey(String key) {
   if ((key != null)) {
     FlowPtype[] entitys = FlowPtype.values();
     for (FlowPtype entity : entitys) {
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

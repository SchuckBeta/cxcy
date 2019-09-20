package com.oseasy.pw.modules.pw.vo;

/**
 * 列表类型.
 * pw_enter_status
 * @author chenhao
*/
public enum PwElistType {
 ET_FPCD("10", "场地分配")
 ,ET_XQ("20", "续期")
 ,ET_QX("30", "退孵")
 ,ET_QUERY("90", "查询企业+团队");


    public static final String PW_ESTATUS = "pwElistType";
 private String key;
 private String name;

 private PwElistType(String key, String name) {
   this.key = key;
   this.name = name;
 }

 /**
  * 根据key获取枚举 .
  *
  * @author chenhao
  * @param key 枚举标识
  * @return PwElistType
  */
 public static PwElistType getByKey(String key) {
   if ((key != null)) {
     PwElistType[] entitys = PwElistType.values();
     for (PwElistType entity : entitys) {
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

@Override
  public String toString() {
      return "{\"key\":\"" + this.key + "\",\"name\":\"" + this.name + "\"}";
  }
}
package com.oseasy.pw.modules.pw.vo;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 入驻类型.
 * @author chenhao
 */
public enum PwEnterAtype {
 PAT_DEFAULT("1", "正常", false)
 ,PAT_BGSQ("2", "变更", true);

 private String key;
 private String name;
 private boolean enable;

 public static final String PW_EATYPES = "pwEnterAtype";

 private PwEnterAtype(String key, String name, boolean enable) {
   this.key = key;
   this.name = name;
   this.enable = enable;
 }

 /**
  * 根据key获取枚举 .
  *
  * @author chenhao
  * @param key 枚举标识
  * @return PwEnterAtype
  */
 public static PwEnterAtype getByKey(String key) {
   if ((key != null)) {
     PwEnterAtype[] entitys = PwEnterAtype.values();
     for (PwEnterAtype entity : entitys) {
       if ((entity.getKey() != null) && (key).equals(entity.getKey())) {
         return entity;
       }
     }
   }
   return null;
 }

 /**
  * 获取类型 .
  * @return List
  */
 public static List<PwEnterAtype> getAll(Boolean enable) {
     if(enable == null){
         enable = true;
     }

     List<PwEnterAtype> enty = Lists.newArrayList();
     PwEnterAtype[] entitys = PwEnterAtype.values();
     for (PwEnterAtype entity : entitys) {
         if ((entity.getKey() != null) && (entity.isEnable())) {
             enty.add(entity);
         }
     }
     return enty;
 }

 public static List<PwEnterAtype> getAll() {
     return getAll(true);
 }

  public boolean isEnable() {
    return enable;
}

public String getKey() {
    return key;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
      return "{\"key\":\"" + this.key + "\",\"name\":\"" + this.name + "\",\"name\":\"" + this.name + "\"}";
  }
}
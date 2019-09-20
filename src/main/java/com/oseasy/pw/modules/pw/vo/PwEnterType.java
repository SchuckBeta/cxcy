package com.oseasy.pw.modules.pw.vo;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 入驻类型.
 * pw_enter_type
 * @author chenhao
 */
public enum PwEnterType {
  PET_TEAM("0", "团队", true)
 ,PET_XM("1", "项目", false)
 ,PET_QY("2", "企业", true);

 private String key;
 private String name;
 private boolean enable;

 public static final String PW_ETYPES = "pwEtypes";

 private PwEnterType(String key, String name, boolean enable) {
   this.key = key;
   this.name = name;
   this.enable = enable;
 }

 /**
  * 根据key获取枚举 .
  *
  * @author chenhao
  * @param key 枚举标识
  * @return PwEnterType
  */
 public static PwEnterType getByKey(String key) {
   if ((key != null)) {
     PwEnterType[] entitys = PwEnterType.values();
     for (PwEnterType entity : entitys) {
       if ((entity.getKey() != null) && (key).equals(entity.getKey())) {
         return entity;
       }
     }
   }
   return null;
 }

 /**
  * 获取主题 .
  * @return List
  */
 public static List<PwEnterType> getAll(Boolean enable) {
     if(enable == null){
         enable = true;
     }

     List<PwEnterType> enty = Lists.newArrayList();
     PwEnterType[] entitys = PwEnterType.values();
     for (PwEnterType entity : entitys) {
         if ((entity.getKey() != null) && (entity.isEnable())) {
             enty.add(entity);
         }
     }
     return enty;
 }

 public static List<PwEnterType> getAll() {
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
      return "{\"key\":\"" + this.key + "\",\"name\":\"" + this.name + "\",\"enable\":\"" + this.enable + "\"}";
  }
}
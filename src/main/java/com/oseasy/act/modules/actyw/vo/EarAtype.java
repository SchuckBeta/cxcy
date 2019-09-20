/**
 * .
 */

package com.oseasy.act.modules.actyw.vo;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 规则类型:1、指派 0、委派.
 * @author chenhao
 */
public enum EarAtype {
    ZP("1", "指派", false),
    WP("0", "委派", true);

   private String key;
   private String name;
   private boolean enable;

   public static final String EAR_ATYPES = "earAtypes";

   private EarAtype(String key, String name, boolean enable) {
     this.key = key;
     this.name = name;
     this.enable = enable;
   }

   /**
    * 根据key获取枚举 .
    *
    * @author chenhao
    * @param key 枚举标识
    * @return EarAtype
    */
   public static EarAtype getByKey(String key) {
     if ((key != null)) {
       EarAtype[] entitys = EarAtype.values();
       for (EarAtype entity : entitys) {
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
   public static List<EarAtype> getAll(Boolean enable) {
       if(enable == null){
           enable = true;
       }

       List<EarAtype> enty = Lists.newArrayList();
       EarAtype[] entitys = EarAtype.values();
       for (EarAtype entity : entitys) {
           if ((entity.getKey() != null) && (entity.isEnable())) {
               enty.add(entity);
           }
       }
       return enty;
   }

   public static List<EarAtype> getAll() {
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
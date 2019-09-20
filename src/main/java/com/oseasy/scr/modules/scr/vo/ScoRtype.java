/**
 * .
 */

package com.oseasy.scr.modules.scr.vo;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 级别：1：类型 ;2：类别（级别）;3、子类别（子级别）.
 * @author chenhao
 */
public enum ScoRtype {
    SR_LX("1", "类型", false)
    ,SR_LB("2", "类别", true)
    ,SR_LB_SUB("3", "子类别", true);

   private String key;
   private String name;
   private boolean enable;

   public static final String SCO_RTYPES = "scoRtypes";

   private ScoRtype(String key, String name, boolean enable) {
     this.key = key;
     this.name = name;
     this.enable = enable;
   }

   /**
    * 根据key获取枚举 .
    *
    * @author chenhao
    * @param key 枚举标识
    * @return ScoRtype
    */
   public static ScoRtype getByKey(String key) {
     if ((key != null)) {
       ScoRtype[] entitys = ScoRtype.values();
       for (ScoRtype entity : entitys) {
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
   public static List<ScoRtype> getAll(Boolean enable) {
       if(enable == null){
           enable = true;
       }

       List<ScoRtype> enty = Lists.newArrayList();
       ScoRtype[] entitys = ScoRtype.values();
       for (ScoRtype entity : entitys) {
           if ((entity.getKey() != null) && (entity.isEnable())) {
               enty.add(entity);
           }
       }
       return enty;
   }

   public static List<ScoRtype> getAll() {
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
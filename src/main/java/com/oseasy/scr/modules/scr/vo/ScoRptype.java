/**
 * .
 */

package com.oseasy.scr.modules.scr.vo;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 认定形式：1：个人 2：团队.
 * @author chenhao
 *
 */
public enum ScoRptype {
    SRP_PERSION("1", "个人", true)
    ,SRP_TEAM("2", "团队", true);

   private String key;
   private String name;
   private boolean enable;

   public static final String SCO_RPTYPES = "ScoRptypes";

   private ScoRptype(String key, String name, boolean enable) {
     this.key = key;
     this.name = name;
     this.enable = enable;
   }

   /**
    * 根据key获取枚举 .
    *
    * @author chenhao
    * @param key 枚举标识
    * @return ScoRptype
    */
   public static ScoRptype getByKey(String key) {
     if ((key != null)) {
       ScoRptype[] entitys = ScoRptype.values();
       for (ScoRptype entity : entitys) {
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
   public static List<ScoRptype> getAll(Boolean enable) {
       if(enable == null){
           enable = true;
       }

       List<ScoRptype> enty = Lists.newArrayList();
       ScoRptype[] entitys = ScoRptype.values();
       for (ScoRptype entity : entitys) {
           if ((entity.getKey() != null) && (entity.isEnable())) {
               enty.add(entity);
           }
       }
       return enty;
   }

   public static List<ScoRptype> getAll() {
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
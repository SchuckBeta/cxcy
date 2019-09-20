/**
 * .
 */

package com.oseasy.scr.modules.scr.vo;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 特殊值类型：便于处理有特殊情况的数据：默认：0:正常规则；10：折半处理；20:取最高分处理；100:自定义分值.
 * @author chenhao
 */
public enum ScoValType {
    SV_DEFAULT("0", "默认", true)
    ,SV_HALF("10", "折半处理", true)
    ,SV_MAX("20", "取最高分处理", true)
    ,SV_ZDY("100", "自定义", true);

   private String key;
   private String name;
   private boolean enable;

   public static final String SCO_RTYPES = "scoRtypes";

   private ScoValType(String key, String name, boolean enable) {
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
   public static ScoValType getByKey(String key) {
     if ((key != null)) {
       ScoValType[] entitys = ScoValType.values();
       for (ScoValType entity : entitys) {
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
   public static List<ScoValType> getAll(Boolean enable) {
       if(enable == null){
           enable = true;
       }

       List<ScoValType> enty = Lists.newArrayList();
       ScoValType[] entitys = ScoValType.values();
       for (ScoValType entity : entitys) {
           if ((entity.getKey() != null) && (entity.isEnable())) {
               enty.add(entity);
           }
       }
       return enty;
   }

   public static List<ScoValType> getAll() {
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
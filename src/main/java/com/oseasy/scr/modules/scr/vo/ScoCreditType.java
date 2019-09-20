/**
 * .
 */

package com.oseasy.scr.modules.scr.vo;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author chenhao
 */
public enum ScoCreditType {
    SCO_CREDIT("1", "创新创业学分类型", false)
    ,SCO_COURSE("2", "课程学分类型", false);

   private String key;
   private String name;
   private boolean enable;


   private ScoCreditType(String key, String name, boolean enable) {
     this.key = key;
     this.name = name;
     this.enable = enable;
   }

   /**
    * 根据key获取枚举 .
    *
    * @author chenhao
    * @param key 枚举标识
    * @return ScoRstatus
    */
   public static ScoCreditType getByKey(String key) {
     if ((key != null)) {
       ScoCreditType[] entitys = ScoCreditType.values();
       for (ScoCreditType entity : entitys) {
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
   public static List<ScoCreditType> getAll(Boolean enable) {
       if(enable == null){
           enable = true;
       }

       List<ScoCreditType> enty = Lists.newArrayList();
       ScoCreditType[] entitys = ScoCreditType.values();
       for (ScoCreditType entity : entitys) {
           if ((entity.getKey() != null) && (entity.isEnable())) {
               enty.add(entity);
           }
       }
       return enty;
   }

   public static List<ScoCreditType> getAll() {
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
/**
 * .
 */

package com.oseasy.scr.modules.scr.vo;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 状态:2、待审核 3、通过;4、未通过.
 * @author chenhao
 */
public enum ScoRstatus {
    SRS_DQD("1", "待提交认定", false),
    SRS_DSH("2", "待审核", true),
    SRS_PASS("3", "通过", true),
    SRS_NOPASS("4", "未通过", true);

   private String key;
   private String name;
   private boolean enable;

   public static final String SCO_RSTATUSS = "scoRstatuss";

   private ScoRstatus(String key, String name, boolean enable) {
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
   public static ScoRstatus getByKey(String key) {
     if ((key != null)) {
       ScoRstatus[] entitys = ScoRstatus.values();
       for (ScoRstatus entity : entitys) {
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
   public static List<ScoRstatus> getAll(Boolean enable) {
       if(enable == null){
           enable = true;
       }

       List<ScoRstatus> enty = Lists.newArrayList();
       ScoRstatus[] entitys = ScoRstatus.values();
       for (ScoRstatus entity : entitys) {
           if ((entity.getKey() != null) && (entity.isEnable())) {
               enty.add(entity);
           }
       }
       return enty;
   }

   public static List<ScoRstatus> getAll() {
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
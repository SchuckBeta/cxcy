package com.oseasy.pw.modules.pw.vo;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 入驻场地审核状态.
 * pw_enter_status
 * @author chenhao
*/
public enum PwEroomStatus {
 PER_DSH("0", "待审核", false)
 ,PER_DFP("10", "待分配", true)
 ,PER_DBGFP("11", "变更待分配", true)
 ,PER_YFP("20", "已分配", true)
 ,PER_SB("30", "失败", false);

    public static final String PW_ESTATUS = "PwEroomStatus";
 private String key;
 private String name;
 private boolean enable;

 private PwEroomStatus(String key, String name, boolean enable) {
   this.key = key;
   this.name = name;
   this.enable = enable;
 }

 /**
  * 根据key获取枚举 .
  *
  * @author chenhao
  * @param key 枚举标识
  * @return PwEroomStatus
  */
 public static PwEroomStatus getByKey(String key) {
   if ((key != null)) {
     PwEroomStatus[] entitys = PwEroomStatus.values();
     for (PwEroomStatus entity : entitys) {
       if ((entity.getKey() != null) && (key).equals(entity.getKey())) {
         return entity;
       }
     }
   }
   return null;
 }

 /**
  * 根据key获取枚举 .
  *
  * @author chenhao
  * @param key 枚举标识
  * @return PwEroomStatus
  */
 public static List<PwEroomStatus> getByKeys(String keys) {
   if ((keys != null)) {
     PwEroomStatus[] entitys = PwEroomStatus.values();
     List<PwEroomStatus> PwEroomStatuss = Lists.newArrayList();
     List<String> keyss = Arrays.asList(StringUtil.split(keys, StringUtil.DOTH));
     for (PwEroomStatus entity : entitys) {
       for (String key : keyss) {
         if (StringUtil.isNotEmpty(key) && (key).equals(entity.getKey())) {
           PwEroomStatuss.add(entity);
         }
       }
     }
     return PwEroomStatuss;
   }
   return null;
 }

 /**
  * 获取场地状态 .
  * @return List
  */
 public static List<PwEroomStatus> getAll(Boolean enable) {
     if(enable == null){
         enable = true;
     }

     List<PwEroomStatus> enty = Lists.newArrayList();
     PwEroomStatus[] entitys = PwEroomStatus.values();
     for (PwEroomStatus entity : entitys) {
         if ((entity.getKey() != null) && (entity.isEnable())) {
             enty.add(entity);
         }
     }
     return enty;
 }

 public static List<PwEroomStatus> getAll() {
     return getAll(true);
 }

  public String getKey() {
    return key;
  }

  public String getName() {
    return name;
  }

  public boolean isEnable() {
    return enable;
}

/**
   * 打卡记录查询状态.
   */
  public static String getKeyByCard() {
    return null;
  }


  /**
   * 退孵（取消入驻）.
   */
  public static String getKeyByQXRZ() {
    return null;
  }


  /**
   * 待分配.
   */
  public static List<PwEroomStatus> getKeysByDFP() {
      List<PwEroomStatus> es = Lists.newArrayList();
      es.add(PwEroomStatus.PER_DFP);
      es.add(PwEroomStatus.PER_DBGFP);
      es.add(PwEroomStatus.PER_YFP);
      return es;
  }
  public static String getKeyByDFP() {
      return PwEroomStatus.listToStr(PwEroomStatus.getKeysByDFP());
  }



  /**
   * 已分配.
   */
  public static String getKeyByYFP() {
    return PwEroomStatus.PER_YFP.getKey();
  }

  /**
   * 三个管理页面只显示待分配，已分配，已续期的，即将到期，已到期 ，已退孵.
   */
  public static String getKeyByManager() {
    return PwEroomStatus.PER_DFP.getKey() + StringUtil.DOTH +
        PwEroomStatus.PER_YFP.getKey();
  }

  /**
   * 入驻查询列表查询条件.
   */
  public static String getKeyByQuery() {
    return PwEroomStatus.PER_DSH.getKey() + StringUtil.DOTH +
        PwEroomStatus.PER_DFP.getKey() + StringUtil.DOTH +
        PwEroomStatus.PER_YFP.getKey() + StringUtil.DOTH +
        PwEroomStatus.PER_SB.getKey();
  }


  /**
   * 枚举转字符串.
   */
  public static String listToStr(List<PwEroomStatus> pestatus) {
      StringBuffer buffer = new StringBuffer();
      for (int i = 0; i < pestatus.size(); i++) {
          PwEroomStatus estatus = pestatus.get(i);
          if(i == 0){
              buffer.append(estatus.getKey());
          }else{
              buffer.append(StringUtil.DOTH);
              buffer.append(estatus.getKey());
          }
      }
      return buffer.toString();
  }


  @Override
  public String toString() {
      return "{\"key\":\"" + this.key + "\",\"name\":\"" + this.name+ "\",\"enable\":\"" + this.enable + "\"}";
  }
}
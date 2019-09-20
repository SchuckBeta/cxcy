package com.oseasy.pw.modules.pw.vo;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 入驻审核状态.
 * pw_enter_status
 * @author chenhao
*/
public enum PwEnterStatus {
 PES_DSH("0", "待审核")
 ,PES_RZCG("10", "入驻成功")
 ,PES_RZSB("20", "入驻失败")
 ,PES_DXQ("30", "即将到期")
 ,PES_YXQ("40", "已续期")
 ,PES_YDQ("50", "已到期")
 ,PES_YTF("60", "已退孵");


    public static final String PW_ESTATUS = "pwEnterStatus";
 private String key;
 private String name;

 private PwEnterStatus(String key, String name) {
   this.key = key;
   this.name = name;
 }

 /**
  * 根据key获取枚举 .
  *
  * @author chenhao
  * @param key 枚举标识
  * @return PwEnterType
  */
 public static PwEnterStatus getByKey(String key) {
   if ((key != null)) {
     PwEnterStatus[] entitys = PwEnterStatus.values();
     for (PwEnterStatus entity : entitys) {
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
  * @return PwEnterType
  */
 public static List<PwEnterStatus> getByKeys(String keys) {
   if ((keys != null)) {
     PwEnterStatus[] entitys = PwEnterStatus.values();
     List<PwEnterStatus> pwEnterStatuss = Lists.newArrayList();
     List<String> keyss = Arrays.asList(StringUtil.split(keys, StringUtil.DOTH));
     for (PwEnterStatus entity : entitys) {
       for (String key : keyss) {
         if (StringUtil.isNotEmpty(key) && (key).equals(entity.getKey())) {
           pwEnterStatuss.add(entity);
         }
       }
     }
     return pwEnterStatuss;
   }
   return null;
 }

 /**
  * 枚举转字符串.
  */
 public static String listToStr(List<PwEnterStatus> pestatus) {
     StringBuffer buffer = new StringBuffer();
     for (int i = 0; i < pestatus.size(); i++) {
         PwEnterStatus estatus = pestatus.get(i);
         if(i == 0){
             buffer.append(estatus.getKey());
         }else{
             buffer.append(StringUtil.DOTH);
             buffer.append(estatus.getKey());
         }
     }
     return buffer.toString();
 }

  public String getKey() {
    return key;
  }

  public String getName() {
    return name;
  }


  /**
   * 打卡记录查询状态.
   */
  public static List<PwEnterStatus> getKeysByCard() {
      List<PwEnterStatus> es = Lists.newArrayList();
      es.add(PwEnterStatus.PES_RZCG);
      es.add(PwEnterStatus.PES_DXQ);
      es.add(PwEnterStatus.PES_YXQ);
      es.add(PwEnterStatus.PES_YDQ);
      return es;
  }
  public static String getKeyByCard() {
      return PwEnterStatus.listToStr(PwEnterStatus.getKeysByCard());
  }

    /**
     * 定时任务处理续期数据.
     * @return
     */
    public static String getKeyByJobExpire() {
        List<PwEnterStatus> es = Lists.newArrayList();
        es.add(PwEnterStatus.PES_RZCG);
        es.add(PwEnterStatus.PES_YXQ);
        return PwEnterStatus.listToStr(es);
    }
    /**
     * 定时任务处理已到期数据.
     * @return
     */
    public static String getKeyByJobYDQ() {
        List<PwEnterStatus> es = Lists.newArrayList();
        es.add(PwEnterStatus.PES_RZCG);
        es.add(PwEnterStatus.PES_DXQ);
        es.add(PwEnterStatus.PES_YXQ);
        return PwEnterStatus.listToStr(es);
    }
    /**
     * 定时任务处理退孵数据.
     * @return
     */
    public static String getKeyByJobExit() {
        List<PwEnterStatus> es = Lists.newArrayList();
        es.add(PwEnterStatus.PES_RZCG);
        es.add(PwEnterStatus.PES_DXQ);
        es.add(PwEnterStatus.PES_YXQ);
        es.add(PwEnterStatus.PES_YDQ);
        return PwEnterStatus.listToStr(es);
    }

  /**
   * 待分配.
   */
  public static List<PwEnterStatus> getKeysByDFP() {
      List<PwEnterStatus> es = Lists.newArrayList();
      es.add(PwEnterStatus.PES_RZCG);
      return es;
  }
  public static String getKeyByDFP() {
      return PwEnterStatus.listToStr(PwEnterStatus.getKeysByDFP());
  }

  /**
   * 已分配.
   */
  public static List<PwEnterStatus> getKeysByYFP() {
      List<PwEnterStatus> es = Lists.newArrayList();
      es.add(PwEnterStatus.PES_DXQ);
      es.add(PwEnterStatus.PES_YXQ);
      es.add(PwEnterStatus.PES_YDQ);
      return es;
  }
  public static String getKeyByYFP() {
      return PwEnterStatus.listToStr(PwEnterStatus.getKeysByYFP());
  }

  /**
   * 场地分配列表.
   */
  public static List<PwEnterStatus> getKeysByFPCD() {
      List<PwEnterStatus> es = Lists.newArrayList();
      es.addAll(PwEnterStatus.getKeysByDFP());
      es.addAll(PwEnterStatus.getKeysByYFP());
      return es;
  }
  public static String getKeyByFPCD() {
      return PwEnterStatus.listToStr(PwEnterStatus.getKeysByFPCD());
  }

  /**
   * 续期列表查询状态.
   */
  public static List<PwEnterStatus> getKeysByXQRZ() {
      List<PwEnterStatus> es = Lists.newArrayList();
      es.add(PwEnterStatus.PES_RZCG);
      es.add(PwEnterStatus.PES_DXQ);
      es.add(PwEnterStatus.PES_YXQ);
      es.add(PwEnterStatus.PES_YDQ);
      return es;
  }
  public static String getKeyByXQRZ() {
      return PwEnterStatus.listToStr(PwEnterStatus.getKeysByXQRZ());
  }

  /**
   * 退孵列表查询状态（取消入驻）.
   */
  public static List<PwEnterStatus> getKeysByQXRZ() {
      List<PwEnterStatus> es = Lists.newArrayList();
      es.add(PwEnterStatus.PES_RZCG);
      es.add(PwEnterStatus.PES_DXQ);
      es.add(PwEnterStatus.PES_YXQ);
      es.add(PwEnterStatus.PES_YDQ);
      es.add(PwEnterStatus.PES_YTF);
      return es;
  }
  public static String getKeyByQXRZ() {
      return PwEnterStatus.listToStr(PwEnterStatus.getKeysByQXRZ());
  }

  /**
   * 入驻查询列表查询条件（企业和团队）.
   */
  public static List<PwEnterStatus> getKeysByQuery() {
      List<PwEnterStatus> es = Lists.newArrayList();
      es.add(PwEnterStatus.PES_RZCG);
      es.add(PwEnterStatus.PES_RZSB);
      es.add(PwEnterStatus.PES_DXQ);
      es.add(PwEnterStatus.PES_YXQ);
      es.add(PwEnterStatus.PES_YDQ);
      es.add(PwEnterStatus.PES_YTF);
      return es;
  }
  public static String getKeyByQuery() {
      return PwEnterStatus.listToStr(PwEnterStatus.getKeysByQuery());
  }

  public static List<PwEnterStatus> getCG(){
      List<PwEnterStatus> es = Lists.newArrayList();
      es.add(PwEnterStatus.PES_RZCG);
      es.add(PwEnterStatus.PES_DXQ);
      es.add(PwEnterStatus.PES_YXQ);
      return es;
  }
  public static String getCGKeyByQuery(){
      return PwEnterStatus.listToStr(PwEnterStatus.getCG());
    }

  @Override
  public String toString() {
      return "{\"key\":\"" + this.key + "\",\"name\":\"" + this.name + "\"}";
  }
}
package com.oseasy.pro.modules.tpl.vo;

/**
 * Created by Administrator on 2017/9/27 0027.
 */
public enum MsgType {
	M_TEAM_YQ("1", "团队对邀请")
	,M_TEAM_JJJR("2", "团队拒绝加入");

  private String key;
  private String name;

  private MsgType(String key, String name) {
    this.key = key;
    this.name=name;
  }

  /**
   * 根据key获取枚举 .
   *
   * @author chenhao
   * @param key 枚举标识
   * @return MsgType
   */
  public static MsgType getByKey(String key) {
    if ((key != null)) {
		MsgType[] entitys = MsgType.values();
      for (MsgType entity : entitys) {
        if ((key).equals(entity.getKey())) {
          return entity;
        }
      }
    }
    return null;
  }

   public String getKey() {
     return key;
   }

   public String getName() {
     return name;
   }
}
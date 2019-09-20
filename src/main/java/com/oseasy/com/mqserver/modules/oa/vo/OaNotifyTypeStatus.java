package com.oseasy.com.mqserver.modules.oa.vo;

/**
 *通知通告类型
 * @author Administrator
 *
 */
public enum OaNotifyTypeStatus {
	EDIT("0", "草稿"),
	DEPLOY("1", "发布");

	private String key;
	private String remark;

	private OaNotifyTypeStatus(String key, String remark) {
    this.key = key;
    this.remark = remark;
  }

  public String getKey() {
    return key;
  }

  public String getRemark() {
    return remark;
  }

  public static OaNotifyTypeStatus getByKey(String key) {
		OaNotifyTypeStatus[] entitys = OaNotifyTypeStatus.values();
		for (OaNotifyTypeStatus entity : entitys) {
			if ((key).equals(entity.getKey())) {
				return entity;
			}
		}
		return null;
	}
}

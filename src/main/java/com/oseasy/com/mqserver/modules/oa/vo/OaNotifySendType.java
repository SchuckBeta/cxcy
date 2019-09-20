package com.oseasy.com.mqserver.modules.oa.vo;

/**
 *通知发送类型
 * @author Administrator
 *
 */
public enum OaNotifySendType {
	DIS_DIRECRIONAL("1", "广播"),
	DIRECRIONAL("2", "定向");

	private String val;
	private String remark;
	private OaNotifySendType(String val, String remark) {
		this.val = val;
		this.remark = remark;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public static OaNotifySendType getByVal(String val) {
		OaNotifySendType[] entitys = OaNotifySendType.values();
		for (OaNotifySendType entity : entitys) {
			if ((val).equals(entity.getVal())) {
				return entity;
			}
		}
		return null;
	}
}

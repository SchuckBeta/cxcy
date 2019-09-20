package com.oseasy.com.mqserver.modules.oa.vo;

/**
 *通知通告类型
 * @author Administrator
 *
 */
public enum OaNotifyType {
	TEAM_BUILD("1", "团建通告"),
	JLTG("2", "奖惩通告"),
	ACTIVIT("3", "活动通告"),
	SCDT("4", "双创动态"),
	SQJR("5", "申请加入"),
	YQJR("6", "邀请加入"),
	XXFB("7", "信息发布"),
	SCTZ("8", "双创通知"),
	SSDT("9", "省市动态"),
	TYJR("10", "同意加入"),
	JJJR("11", "拒绝加入");

	private String val;
	private String remark;
	private OaNotifyType(String val, String remark) {
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

	public static OaNotifyType getByVal(String val) {
		OaNotifyType[] entitys = OaNotifyType.values();
		for (OaNotifyType entity : entitys) {
			if ((val).equals(entity.getVal())) {
				return entity;
			}
		}
		return null;
	}
}

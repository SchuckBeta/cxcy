package com.oseasy.com.mqserver.modules.oa.entity;

public class OaNotifySent {
	private String sentName;   //邀请人
	private String notifyId;   //通知Id
	private String teamName;  //团队名称
	private String type;       //5 、申请 6、邀请7、发布消息10、同意加入11、拒绝加入
	private String teamId;    //团队ID
	private String content;   //内容
	
	
	
	
	
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSentName() {
		return sentName;
	}
	public String getNotifyId() {
		return notifyId;
	}
	public void setSentName(String sentName) {
		this.sentName = sentName;
	}
	public void setNotifyId(String notifyId) {
		this.notifyId = notifyId;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	
	

}

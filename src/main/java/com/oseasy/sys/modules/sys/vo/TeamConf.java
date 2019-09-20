/**
  * Copyright 2017 bejson.com 
  */
package com.oseasy.sys.modules.sys.vo;

/**
 * Auto-generated: 2017-11-07 16:22:10
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class TeamConf {

    /**
	 * 
	 */
	private String maxMembers;//团队成员最大人数
	private String maxOnOff;//每个人创建团队的限制，1-有限制，0-无限制
    private String max;//每个人创建的最大团队数
    private String invitationOnOff;//邀请加入团队限制 1-有限制，0-无限制
    private String joinOnOff;//申请加入团队限制 1-有限制，0-无限制
    private String intramuralValiaOnOff;//团队创建时，校园导师数量限制 1-有限制，0-无限制
    private ConfRange intramuralValia;//团队创建时，校园导师数量范围
    private String teamCheckOnOff;//团队创建、修改是否需要审核 1-需审核，0-无需审核
    private String teamCreateReject;//团队创建审核驳回次数限制，0-无限制
    private String teamUpdateReject;//团队修改审核驳回次数限制，0-无限制
    private String enterYear;//入学N年的，才可创建团队
    
    
	public String getEnterYear() {
		return enterYear;
	}
	public void setEnterYear(String enterYear) {
		this.enterYear = enterYear;
	}
	public String getMaxMembers() {
		return maxMembers;
	}
	public void setMaxMembers(String maxMembers) {
		this.maxMembers = maxMembers;
	}
	public String getTeamCreateReject() {
		return teamCreateReject;
	}
	public void setTeamCreateReject(String teamCreateReject) {
		this.teamCreateReject = teamCreateReject;
	}
	public String getTeamUpdateReject() {
		return teamUpdateReject;
	}
	public void setTeamUpdateReject(String teamUpdateReject) {
		this.teamUpdateReject = teamUpdateReject;
	}
	public String getMaxOnOff() {
		return maxOnOff;
	}
	public void setMaxOnOff(String maxOnOff) {
		this.maxOnOff = maxOnOff;
	}
	public String getMax() {
		return max;
	}
	public void setMax(String max) {
		this.max = max;
	}
	public String getInvitationOnOff() {
		return invitationOnOff;
	}
	public void setInvitationOnOff(String invitationOnOff) {
		this.invitationOnOff = invitationOnOff;
	}
	public String getJoinOnOff() {
		return joinOnOff;
	}
	public void setJoinOnOff(String joinOnOff) {
		this.joinOnOff = joinOnOff;
	}
	public String getIntramuralValiaOnOff() {
		return intramuralValiaOnOff;
	}
	public void setIntramuralValiaOnOff(String intramuralValiaOnOff) {
		this.intramuralValiaOnOff = intramuralValiaOnOff;
	}
	public ConfRange getIntramuralValia() {
		return intramuralValia;
	}
	public void setIntramuralValia(ConfRange intramuralValia) {
		this.intramuralValia = intramuralValia;
	}
	public String getTeamCheckOnOff() {
		return teamCheckOnOff;
	}
	public void setTeamCheckOnOff(String teamCheckOnOff) {
		this.teamCheckOnOff = teamCheckOnOff;
	}
    

}
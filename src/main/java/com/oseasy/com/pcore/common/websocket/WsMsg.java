package com.oseasy.com.pcore.common.websocket;

import java.util.ArrayList;
import java.util.List;

public class WsMsg {
	/**
	 * 消息内容
	 */
	private String content;
	/**
	 * 消息操作按钮
	 */
	private List<WsMsgBtn> btns;
	private String notifyId;
	private String teamId;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<WsMsgBtn> getBtns() {
		return btns;
	}
	public void setBtns(List<WsMsgBtn> btns) {
		this.btns = btns;
	}
	public void addBtn(WsMsgBtn btn) {
		if (btns==null) {
			btns=new ArrayList<WsMsgBtn>();
		}
		btns.add(btn);
	}
	public String getNotifyId() {
		return notifyId;
	}
	public void setNotifyId(String notifyId) {
		this.notifyId = notifyId;
	}
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	
}

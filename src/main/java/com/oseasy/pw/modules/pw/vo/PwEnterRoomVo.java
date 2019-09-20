package com.oseasy.pw.modules.pw.vo;

import com.oseasy.util.common.utils.StringUtil;

public class PwEnterRoomVo {
	private String roomId;//房间id
	private String roomName;//房间名
	private String spaceId;//场地id
	private String spaceFullName;//场地全路径名
	private String roomFullName;//房间全路径名
	
	
	public String getRoomFullName() {
		if(StringUtil.isNotEmpty(spaceFullName)&&StringUtil.isNotEmpty(roomName)){
			return spaceFullName+">"+roomName;
		}
		return roomFullName;
	}
	public void setRoomFullName(String roomFullName) {
		this.roomFullName = roomFullName;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String getSpaceId() {
		return spaceId;
	}
	public void setSpaceId(String spaceId) {
		this.spaceId = spaceId;
	}
	public String getSpaceFullName() {
		return spaceFullName;
	}
	public void setSpaceFullName(String spaceFullName) {
		this.spaceFullName = spaceFullName;
	}
	
}

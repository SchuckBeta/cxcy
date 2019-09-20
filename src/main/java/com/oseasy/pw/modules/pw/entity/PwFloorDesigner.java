package com.oseasy.pw.modules.pw.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 楼层设计Entity.
 * @author 章传胜
 * @version 2017-11-28
 */
public class PwFloorDesigner extends DataEntity<PwFloorDesigner> {

	private static final long serialVersionUID = 1L;
	private String type;		// 类型
	private String name;		// 名称
	private Double x;		// x坐标
	private Double y;		// y
	private String floorId;		// 楼层ID
	private String roomId;		// roomid
	private Integer isclickable;		// 是否可点击

	public PwFloorDesigner(String type, String name, Double x, Double y, String floorId, String roomId, Integer isclickable) {
		this.type = type;
		this.name = name;
		this.x = x;
		this.y = y;
		this.floorId = floorId;
		this.roomId = roomId;
		this.isclickable = isclickable;
	}

	public PwFloorDesigner() {
		super();
	}



	public PwFloorDesigner(String id){
		super(id);
	}

	@Length(min=1, max=32, message="类型长度必须介于 1 和 32 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=0, max=64, message="名称长度必须介于 0 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	@Length(min=0, max=64, message="楼层ID长度必须介于 0 和 64 之间")
	public String getFloorId() {
		return floorId;
	}

	public void setFloorId(String floorId) {
		this.floorId = floorId;
	}

	@Length(min=0, max=64, message="roomid长度必须介于 0 和 64 之间")
	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public Integer getIsclickable() {
		return isclickable;
	}

	public void setIsclickable(Integer isclickable) {
		this.isclickable = isclickable;
	}




}
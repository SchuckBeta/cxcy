package com.oseasy.pw.modules.pw.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 房间设计表Entity.
 * @author zy
 * @version 2017-12-18
 */
public class PwDesignerRoom extends DataEntity<PwDesignerRoom> {

	private static final long serialVersionUID = 1L;
	private String cid;		// 画布表ID
	private String transform;		// 画布调整尺寸
	private String shapetype;		// 形状类型
	private String type;		// 房间类型
	private String x;		// x
	private String y;		// y
	private String width;		// width
	private String height;		// height
	private String style;		// style
	private String fill;		// fill
	private String stroke;		// stroke
	private String cssclass;		// class
	private String roomId;		// 房间ID 备用
	private String name;		// 房间名称
	private String href;		// href
	private String sort;		//排序
	private String angle;		//角度
	private	 String rotate;		//旋转

	public PwDesignerRoom() {
		super();
	}

	public PwDesignerRoom(String id){
		super(id);
	}

	public String getAngle() {
		return angle;
	}

	public void setAngle(String angle) {
		this.angle = angle;
	}

	public String getRotate() {
		return rotate;
	}

	public void setRotate(String rotate) {
		this.rotate = rotate;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=1, max=64, message="画布表ID长度必须介于 1 和 64 之间")
	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	@Length(min=0, max=64, message="画布调整尺寸长度必须介于 0 和 64 之间")
	public String getTransform() {
		return transform;
	}

	public void setTransform(String transform) {
		this.transform = transform;
	}

	@Length(min=0, max=64, message="形状类型长度必须介于 0 和 64 之间")
	public String getShapetype() {
		return shapetype;
	}

	public void setShapetype(String shapetype) {
		this.shapetype = shapetype;
	}

	@Length(min=0, max=64, message="房间类型长度必须介于 0 和 64 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=0, max=64, message="x长度必须介于 0 和 64 之间")
	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	@Length(min=0, max=64, message="y长度必须介于 0 和 64 之间")
	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	@Length(min=0, max=64, message="width长度必须介于 0 和 64 之间")
	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	@Length(min=0, max=64, message="height长度必须介于 0 和 64 之间")
	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	@Length(min=0, max=64, message="style长度必须介于 0 和 64 之间")
	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	@Length(min=0, max=64, message="fill长度必须介于 0 和 64 之间")
	public String getFill() {
		return fill;
	}

	public void setFill(String fill) {
		this.fill = fill;
	}

	@Length(min=0, max=64, message="stroke长度必须介于 0 和 64 之间")
	public String getStroke() {
		return stroke;
	}

	public void setStroke(String stroke) {
		this.stroke = stroke;
	}

	public String getCssclass() {
		return cssclass;
	}

	public void setCssclass(String cssclass) {
		this.cssclass = cssclass;
	}

	@Length(min=0, max=64, message="房间ID 备用长度必须介于 0 和 64 之间")
	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

}
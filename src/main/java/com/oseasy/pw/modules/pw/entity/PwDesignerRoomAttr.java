package com.oseasy.pw.modules.pw.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 房间属性表Entity.
 * @author zy
 * @version 2017-12-18
 */
public class PwDesignerRoomAttr extends DataEntity<PwDesignerRoomAttr> {

	private static final long serialVersionUID = 1L;
	private String rid;		// 房间设计表ID
	private String x;		// x
	private String y;		// y
	private String name;		// 物品名称
	private String href;		// href
	private String width;		// width
	private String height;		// height
	private String fill;		// fill
	private String style;		// style
	private String cssclass;		// class
	private String shapetype;		// shapetype
	private String type;		// 类型
	private String text;		// 房间描述
	private String showIndex;		// 排序

	public PwDesignerRoomAttr() {
		super();
	}

	public PwDesignerRoomAttr(String id){
		super(id);
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getShowIndex() {
		return showIndex;
	}

	public void setShowIndex(String showIndex) {
		this.showIndex = showIndex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWidth() {
		return width;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	@Length(min=1, max=64, message="房间设计表ID长度必须介于 1 和 64 之间")
	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
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

	@Length(min=0, max=64, message="fill长度必须介于 0 和 64 之间")
	public String getFill() {
		return fill;
	}

	public void setFill(String fill) {
		this.fill = fill;
	}

	@Length(min=0, max=64, message="style长度必须介于 0 和 64 之间")
	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}


	public String getCssclass() {
		return cssclass;
	}

	public void setCssclass(String cssclass) {
		this.cssclass = cssclass;
	}

	@Length(min=0, max=64, message="shapetype长度必须介于 0 和 64 之间")
	public String getShapetype() {
		return shapetype;
	}

	public void setShapetype(String shapetype) {
		this.shapetype = shapetype;
	}

	@Length(min=0, max=64, message="类型长度必须介于 0 和 64 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=0, max=64, message="房间描述长度必须介于 0 和 64 之间")
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
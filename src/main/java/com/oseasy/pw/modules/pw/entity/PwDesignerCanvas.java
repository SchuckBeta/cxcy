package com.oseasy.pw.modules.pw.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 画布表Entity.
 * @author zy
 * @version 2017-12-18
 */
public class PwDesignerCanvas extends DataEntity<PwDesignerCanvas> {

	private static final long serialVersionUID = 1L;
	private String floorId;		// 楼层ID
	private String picUrl;		// 画布图片url
	private String backgroundColor;		// 画布背景色
	private String backgroundImage;		// 画布背景
	private String svgHtml;		// html
	private String width;		// 画布宽度
	private String height;		// 画布高度

	public PwDesignerCanvas() {
		super();
	}

	public PwDesignerCanvas(String id){
		super(id);
	}

	@Length(min=1, max=64, message="楼层ID长度必须介于 1 和 64 之间")
	public String getFloorId() {
		return floorId;
	}

	public void setFloorId(String floorId) {
		this.floorId = floorId;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getSvgHtml() {
        return svgHtml;
    }

    public void setSvgHtml(String svgHtml) {
        this.svgHtml = svgHtml;
    }

    public String getWidth() {
        return width;
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
}
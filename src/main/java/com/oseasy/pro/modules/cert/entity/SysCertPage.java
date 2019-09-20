package com.oseasy.pro.modules.cert.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 证书模板页面Entity.
 * @author 奔波儿灞
 * @version 2018-02-06
 */
public class SysCertPage extends DataEntity<SysCertPage> {

	private static final long serialVersionUID = 1L;
	private String certId;		// 证书主表id
	private String name;		// 证书页面名称
	private Integer height;		// 高
	private Integer width;		// 宽
	private String html;		// 页面html代码
	private Integer sort;		// 排序
	private String uiJson;//文本段落数据
	public SysCertPage() {
		super();
	}

	public SysCertPage(String id){
		super(id);
	}

	@Length(min=0, max=64, message="证书主表id长度必须介于 0 和 64 之间")
	public String getCertId() {
		return certId;
	}

	public void setCertId(String certId) {
		this.certId = certId;
	}

	@Length(min=1, max=128, message="证书页面名称长度必须介于 1 和 128 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getUiJson() {
		return uiJson;
	}

	public void setUiJson(String uiJson) {
		this.uiJson = uiJson;
	}

}
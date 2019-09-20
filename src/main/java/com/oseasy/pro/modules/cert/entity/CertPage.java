package com.oseasy.pro.modules.cert.entity;

import java.util.List;

public class CertPage {
	private List<CertElement> list;//页面内元素
	private String certid;//证书模板id
	private String certname;//证书模板名称
	private String certpageid;//证书模板页面id
	private String certpagename;//证书模板页面名称
	private String uiHtml;//html标签文本 超长文本
	private String uiJson;//文本段落数据
	private Integer width;//
	private Integer height;//
	private String imgPath; //下发证书用的模板图
	
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public String getUiJson() {
		return uiJson;
	}
	public void setUiJson(String uiJson) {
		this.uiJson = uiJson;
	}
	public List<CertElement> getList() {
		return list;
	}
	public void setList(List<CertElement> list) {
		this.list = list;
	}
	public String getCertid() {
		return certid;
	}
	public void setCertid(String certid) {
		this.certid = certid;
	}
	public String getCertname() {
		return certname;
	}
	public void setCertname(String certname) {
		this.certname = certname;
	}
	public String getCertpageid() {
		return certpageid;
	}
	public void setCertpageid(String certpageid) {
		this.certpageid = certpageid;
	}
	public String getCertpagename() {
		return certpagename;
	}
	public void setCertpagename(String certpagename) {
		this.certpagename = certpagename;
	}
	public String getUiHtml() {
		return uiHtml;
	}
	public void setUiHtml(String uiHtml) {
		this.uiHtml = uiHtml;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	
}

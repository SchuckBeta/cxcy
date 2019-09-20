package com.oseasy.pro.modules.cert.vo;

public class SysCertPageVo {
	private String id;
	private String certid;//证书模板id
	private String certname;//证书模板名称
	private String certpagename;//证书模板页面名称
	private String imgUrl;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getCertpagename() {
		return certpagename;
	}
	public void setCertpagename(String certpagename) {
		this.certpagename = certpagename;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
}

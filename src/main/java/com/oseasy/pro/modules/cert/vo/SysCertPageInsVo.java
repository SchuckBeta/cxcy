package com.oseasy.pro.modules.cert.vo;

import com.oseasy.com.fileserver.common.utils.FtpUtil;
import com.oseasy.util.common.utils.StringUtil;

public class SysCertPageInsVo {
	private String id;
	private String certid;//证书模板id
	private String name;//证书模板页面名称
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImgUrl() {
		if(StringUtil.isNotEmpty(imgUrl)){
			return FtpUtil.ftpImgUrl(imgUrl);
		}
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	
}

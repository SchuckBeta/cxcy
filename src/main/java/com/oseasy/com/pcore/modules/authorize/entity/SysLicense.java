package com.oseasy.com.pcore.modules.authorize.entity;


import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 授权信息Entity
 * @author 9527
 * @version 2017-04-13
 */
public class SysLicense extends DataEntity<SysLicense> {

	private static final long serialVersionUID = 1L;
	private String license;		// 授权文件信息
	
	public SysLicense() {
		super();
	}

	public SysLicense(String id) {
		super(id);
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}
	
}
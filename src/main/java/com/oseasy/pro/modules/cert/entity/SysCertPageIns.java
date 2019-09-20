package com.oseasy.pro.modules.cert.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 证书颁发记录页面表Entity.
 * @author 奔波儿灞
 * @version 2018-02-09
 */
public class SysCertPageIns extends DataEntity<SysCertPageIns> {

	private static final long serialVersionUID = 1L;
	private String certInsId;		// 证书记录表id
	private String name;		// 证书页面名称
	private String sort;		// 排序

	public SysCertPageIns() {
		super();
	}

	public SysCertPageIns(String id){
		super(id);
	}

	@Length(min=0, max=64, message="证书记录表id长度必须介于 0 和 64 之间")
	public String getCertInsId() {
		return certInsId;
	}

	public void setCertInsId(String certInsId) {
		this.certInsId = certInsId;
	}

	@Length(min=1, max=128, message="证书页面名称长度必须介于 1 和 128 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=1, max=3, message="排序长度必须介于 1 和 3 之间")
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

}
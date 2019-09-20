package com.oseasy.pro.modules.cert.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 证书模板元素Entity.
 * @author 奔波儿灞
 * @version 2018-02-06
 */
public class SysCertElement extends DataEntity<SysCertElement> {

	private static final long serialVersionUID = 1L;
	private String certPageId;		// 证书页面表id
	private Integer sort;		// 元素图层层级
	private String elementType;		// 元素类型
	private String content;		// 元素属性json

	public SysCertElement() {
		super();
	}

	public SysCertElement(String id){
		super(id);
	}

	@Length(min=0, max=64, message="证书页面表id长度必须介于 0 和 64 之间")
	public String getCertPageId() {
		return certPageId;
	}

	public void setCertPageId(String certPageId) {
		this.certPageId = certPageId;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Length(min=0, max=64, message="元素类型长度必须介于 0 和 64 之间")
	public String getElementType() {
		return elementType;
	}

	public void setElementType(String elementType) {
		this.elementType = elementType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
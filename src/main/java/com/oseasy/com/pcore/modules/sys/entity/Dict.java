/**
 * 
 */
package com.oseasy.com.pcore.modules.sys.entity;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

import java.util.List;

/**
 * 字典Entity


 */
public class Dict extends DataEntity<Dict> {

	private static final long serialVersionUID = 1L;
	private String value;	// 数据值
	private String label;	// 标签名
	private String type;	// 类型
	private String description;// 描述
	private Integer sort;	// 排序
	private String parentId;//父Id
	private String isSys;//是否系统字典，0-否，1-是
	private Boolean isPush; // 是否全局的字典:0、否；1、是
	private List<String> tenantIds;

	public Dict() {
		super();
	}
	
	public Dict(String id) {
		super(id);
	}
	
	public Dict(String value, String label) {
		this.value = value;
		this.label = label;
		this.isPush = false;
	}

	public Dict(String value, String label, String type, String description, String parentId, Integer sort,String isSys, List<String> tenantIds) {
		this.value = value;
		this.label = label;
		this.type = type;
		this.description = description;
		this.parentId = parentId;
		this.sort = sort;
		this.isSys = isSys;
		this.isPush = false;
		this.tenantIds = tenantIds;
	}

	public Boolean getIsPush() {
		if(this.isPush == null){
			this.isPush = false;
		}
		return isPush;
	}

	public void setIsPush(boolean isPush) {
		this.isPush = isPush;
	}

	@XmlAttribute
	@Length(min=1, max=100)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getIsSys() {
		return isSys;
	}

	public void setIsSys(String isSys) {
		this.isSys = isSys;
	}

	@XmlAttribute
	@Length(min=1, max=100)
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Length(min=1, max=100)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlAttribute
	@Length(min=0, max=100)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@NotNull
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Length(min=1, max=100)
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public List<String> getTenantIds() {
		return tenantIds;
	}

	public void setTenantIds(List<String> tenantIds) {
		this.tenantIds = tenantIds;
	}

	@Override
	public String toString() {
		return label;
	}
}
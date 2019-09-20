/**
 * 
 */
package com.oseasy.com.pcore.modules.sys.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.TreeEntity;

import java.util.List;

/**
 * 区域Entity


 */
public class Area extends TreeEntity<Area> {

	private static final long serialVersionUID = 1L;
//	private Area parent;	// 父级编号
//	private String parentIds; // 所有父级编号
	private String code; 	// 区域编码
//	private String name; 	// 区域名称
//	private Integer sort;		// 排序
	private String type; 	// 区域类型（1：国家；2：省份、直辖市；3：地市；4：区县）
	private List<Area> children;
	private String level; //层级
	public Area() {
		super();
		this.sort = 30;
	}

	public Area(String id) {
		super(id);
	}
	

	public Area getParent() {
		return parent;
	}

	public void setParent(Area parent) {
		this.parent = parent;
	}

	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	@Length(min=1, max=1)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=0, max=100)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<Area> getChildren() {
		return children;
	}

	public void setChildren(List<Area> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return name;
	}
}
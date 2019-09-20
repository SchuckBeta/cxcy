/**
 *
 */
package com.oseasy.com.pcore.common.persistence;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oseasy.util.common.utils.Reflections;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 数据Entity类

 * @version 2014-05-16
 */
public abstract class TreeEntity<T> extends DataEntity<T> {

	private static final long serialVersionUID = 1L;

	protected T parent;	// 父级编号
	protected String parentIds; // 所有父级编号
	protected Integer lver; 	// 级别 TreeLver
	protected String name; 	// 机构名称
	protected Integer sort;		// 排序
	protected String queryStr;       // 查询字符串
	protected List<String> ids;       // 查询Ids

	public TreeEntity() {
		super();
		this.sort = 30;
	}

	public TreeEntity(String id) {
		super(id);
	}

	/**
	 * 父对象，只能通过子类实现，父类实现mybatis无法读取
	 * @return
	 */
	@JsonBackReference
	@NotNull
	public abstract T getParent();

	/**
	 * 父对象，只能通过子类实现，父类实现mybatis无法读取
	 * @return
	 */
	public abstract void setParent(T parent);

	@Length(min=1, max=2000)
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	@Length(min=1, max=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getLver() {
		if((this.lver == null) && StringUtil.isNotEmpty(this.parentIds)){
			this.lver = StringUtil.splitLength(StringUtil.DOTH, this.parentIds);
			if(this.lver == 0){
				this.lver = null;
			}
		}
		return lver;
	}

	public void setLver(Integer lver) {
		this.lver = lver;
	}

	@JsonIgnore
    @XmlTransient
    public String getQueryStr() {
        return queryStr;
    }

    public void setQueryStr(String queryStr) {
        this.queryStr = queryStr;
    }

    @JsonIgnore
    @XmlTransient
    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

	public String getParentId() {
		String id = null;
		if (parent != null) {
			id = (String)Reflections.getFieldValue(parent, "id");
		}
		return StringUtil.isNotBlank(id) ? id : "0";
	}

}

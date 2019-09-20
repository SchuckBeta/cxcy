/**
 *
 */
package com.oseasy.com.pcore.common.persistence;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 数据Entity类

 * @version 2014-05-16
 */
public abstract class DataExtEntity<T> extends DataEntity<T> implements Serializable{

	private static final long serialVersionUID = 1L;
	private String queryStr;       // 查询字符串
    private String keys;       // 查询keys
    protected List<String> ids;       // IN查询Ids
    protected List<String> filterIds;       // NOT IN排除查询Ids

    public DataExtEntity() {
        super();
    }

    public DataExtEntity(String id) {
        super(id);
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
    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    @JsonIgnore
    @XmlTransient
    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public List<String> getFilterIds() {
        return filterIds;
    }

    public void setFilterIds(List<String> filterIds) {
        this.filterIds = filterIds;
    }
}

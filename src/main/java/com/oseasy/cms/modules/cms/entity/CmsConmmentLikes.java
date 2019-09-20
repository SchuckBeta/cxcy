package com.oseasy.cms.modules.cms.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.modules.sys.entity.User;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 评论Entity.
 * @author chenh
 * @version 2018-09-04
 */
public class CmsConmmentLikes extends DataEntity<CmsConmmentLikes> {

	private static final long serialVersionUID = 1L;
	private User user;		// 点赞人
	private CmsConmment parent;		// 评论编号
    private List<String> ids;       // 查询Ids

	public CmsConmmentLikes() {
		super();
	}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CmsConmment getParent() {
        return parent;
    }

    public void setParent(CmsConmment parent) {
        this.parent = parent;
    }

    @JsonIgnore
    @XmlTransient
    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
package com.oseasy.scr.modules.scr.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataExtEntity;
import com.oseasy.com.pcore.modules.sys.entity.User;

/**
 * 学分申请成员Entity.
 * @author chenhao
 * @version 2018-12-21
 */
public class ScoRapplyMember extends DataExtEntity<ScoRapplyMember> {

	private static final long serialVersionUID = 1L;
    private ScoRapply apply;        // 申请ID
    private User user;      // 申请人ID
	private Integer rate;		// 学分配比
	private ScoRapplyPb scoRapplyPb;

	public ScoRapplyPb getScoRapplyPb() {
		return scoRapplyPb;
	}

	public void setScoRapplyPb(ScoRapplyPb scoRapplyPb) {
		this.scoRapplyPb = scoRapplyPb;
	}

	public ScoRapplyMember() {
		super();
	}

	public ScoRapplyMember(String id){
		super(id);
	}

	public ScoRapply getApply() {
        return apply;
    }

    public void setApply(ScoRapply apply) {
        this.apply = apply;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}


	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
package com.oseasy.act.modules.actyw.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataExtEntity;

/**
 * 指派专家组的项目Entity.
 * @author zy
 * @version 2019-01-23
 */
public class ActYwEtAuditNum extends DataExtEntity<ActYwEtAuditNum> {

	private static final long serialVersionUID = 1L;
	private String actywId;		// 项目类别编号
	private String gnodeId;		// 审核节点编号
	private String experts;		// 专家名字list 例如 1|3|5 编号为1,3,5的专家为一组
	private String proId;		// proId为指派的项目名称
	private String atype;       // EarAtype类型：0、指派； 1、委派
	private String isFinish;		//是否完成

	public ActYwEtAuditNum() {
		super();
	}

	public ActYwEtAuditNum(String id){
		super(id);
	}

	public String getAtype() {
        return atype;
    }

    public void setAtype(String atype) {
        this.atype = atype;
    }

    public String getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(String isFinish) {
        this.isFinish = isFinish;
    }

    @Length(min=1, max=64, message="项目类别编号长度必须介于 1 和 64 之间")
	public String getActywId() {
		return actywId;
	}

	public void setActywId(String actywId) {
		this.actywId = actywId;
	}

	@Length(min=1, max=64, message="审核节点编号长度必须介于 1 和 64 之间")
	public String getGnodeId() {
		return gnodeId;
	}

	public void setGnodeId(String gnodeId) {
		this.gnodeId = gnodeId;
	}

	@Length(min=0, max=1024, message="专家名字list 例如 1|3|5 编号为1,3,5的专家为一组长度必须介于 0 和 1024 之间")
	public String getExperts() {
		return experts;
	}

	public void setExperts(String experts) {
		this.experts = experts;
	}

	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
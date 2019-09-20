package com.oseasy.act.modules.actyw.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.act.common.persistence.ActEntity;
import com.oseasy.com.pcore.modules.sys.entity.User;

/**
 * 流程申请Entity.
 * @author zy
 * @version 2017-12-05
 */
public class ActYwApply extends ActEntity<ActYwApply> {

	private static final long serialVersionUID = 1L;
	public ActYw actYw;		// 业务流程编号
	public String procInsId;		// 流程实例id
	public String no;		// 申请编号
	public String type;		// 业务类型
	public String relId;		// 业务编号
	public User applyUser;		// 业务编号

	public ActYwApply() {
		super();
	}

	public ActYwApply(String id) {
		super(id);
	}

	public ActYwApply(String id, ActYw actYw, User applyUser) {
    super();
    this.id = id;
    this.actYw = actYw;
    this.applyUser = applyUser;
  }

	public ActYwApply(String id, ActYw actYw, User applyUser, String procInsId) {
	  super();
	  this.id = id;
	  this.actYw = actYw;
	  this.procInsId = procInsId;
	  this.applyUser = applyUser;
	}

	public ActYwApply(String id, ActYw actYw, User applyUser, String procInsId, String relId) {
	  super();
	  this.id = id;
	  this.actYw = actYw;
	  this.procInsId = procInsId;
	  this.applyUser = applyUser;
	  this.relId = relId;
	}

	public ActYwApply(String id, ActYw actYw, User applyUser,
	    String procInsId, String no, String type, String relId) {
	  super();
	  this.id = id;
	  this.actYw = actYw;
	  this.procInsId = procInsId;
	  this.no = no;
	  this.type = type;
	  this.relId = relId;
	  this.applyUser = applyUser;
	}

  public User getApplyUser() {
    return applyUser;
  }

  public void setApplyUser(User applyUser) {
    this.applyUser = applyUser;
  }

  public ActYw getActYw() {
    return actYw;
  }

  public void setActYw(ActYw actYw) {
    this.actYw = actYw;
  }

  @Length(min=0, max=64, message="流程实例id长度必须介于 0 和 64 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}

	@Length(min=1, max=64, message="申请编号长度必须介于 1 和 64 之间")
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	@Length(min=0, max=255, message="业务类型长度必须介于 0 和 255 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=1, max=64, message="业务编号长度必须介于 1 和 64 之间")
	public String getRelId() {
		return relId;
	}

	public void setRelId(String relId) {
		this.relId = relId;
	}

}
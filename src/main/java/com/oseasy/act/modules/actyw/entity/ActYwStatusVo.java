package com.oseasy.act.modules.actyw.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 节点状态表Entity.
 * @author zy
 * @version 2018-01-15
 */
public class ActYwStatusVo extends DataEntity<ActYwStatusVo> {
    private static final long serialVersionUID = 1L;
	private String status;		// 保存值eg:1,2,3,4
	private String state;		//业务状态说明
	private String alias;		// 范围等
	private ActYwSgtype actYwSgtype;		// 状态类型：分数，通过/不通过 级别类型 act_gnode_status
	private String regType;   // 业务状态类型：审核 评分 act_status_type

	public ActYwSgtype getActYwSgtype() {
		return actYwSgtype;
	}

	public void setActYwSgtype(ActYwSgtype actYwSgtype) {
		this.actYwSgtype = actYwSgtype;
	}

	public ActYwStatusVo() {
		super();
	}

	public ActYwStatusVo(String id){
		super(id);
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRegType() {
		return regType;
	}

	public void setRegType(String regType) {
		this.regType = regType;
	}

	@Length(min=0, max=64, message="eg:1,2,3,4长度必须介于 0 和 64 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Length(min=0, max=200, message="状态说明 ：A 1 , B 2 ,C 3 ,D 4长度必须介于 0 和 200 之间")
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

}
package com.oseasy.pie.modules.impdata.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 导入互联网+大赛错误数据Entity.
 * @author 奔波儿灞
 * @version 2017-12-07
 */
public class GcontestError extends DataEntity<GcontestError> {

	private static final long serialVersionUID = 1L;
	private String impId;		// 导入信息表主键
	private String name;		// 参赛项目名称
	private String type;		// 大赛类别
	private String groups;		// 参赛组别
	private String leader;		// 申报人/学号
	private String office;		// 所属学院
	private String profes;		// 所属专业
	private String mobile;		// 申报人电话
	private String steachers;		// 校内导师/工号
	private String eteachers;		// 企业导师/工号
	private String huojiang;//荣获奖项
	public GcontestError() {
		super();
	}

	public String getHuojiang() {
		return huojiang;
	}

	public void setHuojiang(String huojiang) {
		this.huojiang = huojiang;
	}

	public GcontestError(String id) {
		super(id);
	}

	@Length(min=1, max=64, message="导入信息表主键长度必须介于 1 和 64 之间")
	public String getImpId() {
		return impId;
	}

	public void setImpId(String impId) {
		this.impId = impId;
	}

	@Length(min=0, max=128, message="参赛项目名称长度必须介于 0 和 128 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=0, max=128, message="大赛类别长度必须介于 0 和 128 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=0, max=128, message="参赛组别长度必须介于 0 和 128 之间")
	public String getGroups() {
		return groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
	}

	@Length(min=0, max=128, message="申报人/学号长度必须介于 0 和 128 之间")
	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	@Length(min=0, max=128, message="所属学院长度必须介于 0 和 128 之间")
	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	@Length(min=0, max=128, message="所属专业长度必须介于 0 和 128 之间")
	public String getProfes() {
		return profes;
	}

	public void setProfes(String profes) {
		this.profes = profes;
	}

	@Length(min=0, max=30, message="申报人电话长度必须介于 0 和 30 之间")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Length(min=0, max=256, message="校内导师/工号长度必须介于 0 和 256 之间")
	public String getSteachers() {
		return steachers;
	}

	public void setSteachers(String steachers) {
		this.steachers = steachers;
	}

	@Length(min=0, max=256, message="企业导师/工号长度必须介于 0 和 256 之间")
	public String getEteachers() {
		return eteachers;
	}

	public void setEteachers(String eteachers) {
		this.eteachers = eteachers;
	}

}
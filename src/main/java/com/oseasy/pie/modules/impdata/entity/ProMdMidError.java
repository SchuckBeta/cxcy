package com.oseasy.pie.modules.impdata.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 民大项目中期检查导入错误数据Entity.
 * @author 9527
 * @version 2017-10-18
 */
public class ProMdMidError extends DataEntity<ProMdMidError> {

	private static final long serialVersionUID = 1L;
	private String impId;		// 导入信息表主键
	private String pNumber;		// 项目编号
	private String pName;		// 项目名称
	private String leaderName;		// 负责人-姓名
	private String no;		// 负责人-学号
	private String mobile;		// 负责人-手机
	private String teachers;		// 导师-姓名
	private String modify;		// 是否有变动情况
	private String proCategory;		// 项目类型
	private String level;		// 项目级别
	private String result;		// 学院审查意见
	private String stageResult;		// 阶段成果
	private String reimbursementAmount;		// 报销金额
	private String proModelMdId;		// 项目id
	private String gnodeid;		// 节点

	public ProMdMidError() {
		super();
	}

	public ProMdMidError(String id) {
		super(id);
	}

	@Length(min=1, max=64, message="导入信息表主键长度必须介于 1 和 64 之间")
	public String getImpId() {
		return impId;
	}

	public void setImpId(String impId) {
		this.impId = impId;
	}

	@Length(min=0, max=128, message="项目编号长度必须介于 0 和 128 之间")
	public String getPNumber() {
		return pNumber;
	}

	public void setPNumber(String pNumber) {
		this.pNumber = pNumber;
	}

	@Length(min=0, max=128, message="项目名称长度必须介于 0 和 128 之间")
	public String getPName() {
		return pName;
	}

	public void setPName(String pName) {
		this.pName = pName;
	}

	@Length(min=0, max=128, message="负责人-姓名长度必须介于 0 和 128 之间")
	public String getLeaderName() {
		return leaderName;
	}

	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}

	@Length(min=0, max=128, message="负责人-学号长度必须介于 0 和 128 之间")
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	@Length(min=0, max=128, message="负责人-手机长度必须介于 0 和 128 之间")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Length(min=0, max=128, message="导师-姓名长度必须介于 0 和 128 之间")
	public String getTeachers() {
		return teachers;
	}

	public void setTeachers(String teachers) {
		this.teachers = teachers;
	}

	@Length(min=0, max=12, message="是否有变动情况长度必须介于 0 和 12 之间")
	public String getModify() {
		return modify;
	}

	public void setModify(String modify) {
		this.modify = modify;
	}

	@Length(min=0, max=128, message="项目类型长度必须介于 0 和 128 之间")
	public String getProCategory() {
		return proCategory;
	}

	public void setProCategory(String proCategory) {
		this.proCategory = proCategory;
	}

	@Length(min=0, max=128, message="项目级别长度必须介于 0 和 128 之间")
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	@Length(min=0, max=12, message="学院审查意见长度必须介于 0 和 12 之间")
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getStageResult() {
		return stageResult;
	}

	public void setStageResult(String stageResult) {
		this.stageResult = stageResult;
	}

	public String getReimbursementAmount() {
		return reimbursementAmount;
	}

	public void setReimbursementAmount(String reimbursementAmount) {
		this.reimbursementAmount = reimbursementAmount;
	}

	@Length(min=0, max=64, message="项目id长度必须介于 0 和 64 之间")
	public String getProModelMdId() {
		return proModelMdId;
	}

	public void setProModelMdId(String proModelMdId) {
		this.proModelMdId = proModelMdId;
	}

	@Length(min=0, max=64, message="节点长度必须介于 0 和 64 之间")
	public String getGnodeid() {
		return gnodeid;
	}

	public void setGnodeid(String gnodeid) {
		this.gnodeid = gnodeid;
	}

}
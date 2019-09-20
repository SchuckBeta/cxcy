package com.oseasy.scr.modules.sco.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

import java.util.Date;

/**
 * 创新、创业、素质学分认定表Entity.
 * @author chenhao
 * @version 2017-07-18
 */
public class ScoAffirm extends DataEntity<ScoAffirm> {

	private static final long serialVersionUID = 1L;
	private String procInsId;		// 流程实例ID
	private String type;		// 学分类型：1、创新学分/2、创业学分/3、素质学分
	private String proId;		// 项目/大赛ID
	private String proType;		// 项目类型/大赛类型：具体值可查看文档
	private String proLevelType;		// 项目级别/大赛级别：具体值可查看文档
	private String proPtype;		// 只有项目有类别，大赛无类别：具体值可查看文档
	private String proResult;		// 项目/大赛结果：具体值可查看文档
	private Long proScore;		// 大赛有得分，项目无得分
	private float scoreVal;		// 实际认定学分
	private float scoreStandard;   //学分认定标准
	private Date affirmDate;  //认定时间

	public ScoAffirm() {
		super();
	}

	public ScoAffirm(String id) {
		super(id);
	}

	@Length(min=1, max=64, message="流程实例ID长度必须介于 1 和 64 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}

	@Length(min=0, max=1, message="学分类型：1、创新学分/2、创业学分/3、素质学分长度必须介于 0 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=0, max=64, message="项目/大赛ID长度必须介于 0 和 64 之间")
	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	@Length(min=0, max=1, message="项目类型/大赛类型：具体值可查看文档长度必须介于 0 和 1 之间")
	public String getProType() {
		return proType;
	}

	public void setProType(String proType) {
		this.proType = proType;
	}

	@Length(min=0, max=1, message="项目级别/大赛级别：具体值可查看文档长度必须介于 0 和 1 之间")
	public String getProLevelType() {
		return proLevelType;
	}

	public void setProLevelType(String proLevelType) {
		this.proLevelType = proLevelType;
	}

	@Length(min=0, max=1, message="只有项目有类别，大赛无类别：具体值可查看文档长度必须介于 0 和 1 之间")
	public String getProPtype() {
		return proPtype;
	}

	public void setProPtype(String proPtype) {
		this.proPtype = proPtype;
	}

	@Length(min=0, max=2, message="项目/大赛结果：具体值可查看文档长度必须介于 0 和 2 之间")
	public String getProResult() {
		return proResult;
	}

	public void setProResult(String proResult) {
		this.proResult = proResult;
	}

	public Long getProScore() {
		return proScore;
	}

	public void setProScore(Long proScore) {
		this.proScore = proScore;
	}

	public float getScoreVal() {
		return scoreVal;
	}

	public void setScoreVal(float scoreVal) {
		this.scoreVal = scoreVal;
	}

	public float getScoreStandard() {
		return scoreStandard;
	}

	public void setScoreStandard(float scoreStandard) {
		this.scoreStandard = scoreStandard;
	}

	public Date getAffirmDate() {
		return affirmDate;
	}

	public void setAffirmDate(Date affirmDate) {
		this.affirmDate = affirmDate;
	}
}
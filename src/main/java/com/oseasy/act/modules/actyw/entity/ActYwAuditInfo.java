package com.oseasy.act.modules.actyw.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.modules.sys.entity.User;

/**
 * 自定义审核信息Entity.
 * @author zy
 * @version 2017-11-01
 */
public class ActYwAuditInfo extends DataEntity<ActYwAuditInfo> implements Comparable {
	public static final String repulseCode="999";
	public static final String repulseStr="打回";
	public static final String pass="998"; // 通过
	public static final String nopass="997";//不通过

	private static final long serialVersionUID = 1L;
	private String promodelId;		// 自定义大赛或项目id
	private String auditId;		// 审核人id
	private String auditLevel;		// 审核级别：1代表院级 2代表校级
	private String auditName;		// 评审名称，如评分、答辩等
	private String gnodeId;		// 审核节点id
	private String suggest;		// 意见
	private double score;		// 分数
	private String grade;		// 审核结果 0：不合格1：合格
	private String procInsId;		// 流程实例id
	private User user; //审核人
	private String result;   //结果（审核结果（标识）或是打分的分数）
	private String isBack; //回退记录标识
	private String gnodeVesion; //审核批次
	private String isDelegate; //是否为委派记录1：是  0不是

	private transient boolean isComplete = false; //标记节点

	public ActYwAuditInfo() {
		super();
	}

	public ActYwAuditInfo(String id) {
		super(id);
	}

	public String getIsDelegate() {
		return isDelegate;
	}

	public void setIsDelegate(String isDelegate) {
		this.isDelegate = isDelegate;
	}

	public String getIsBack() {
		return isBack;
	}

	public void setIsBack(String isBack) {
		this.isBack = isBack;
	}

	public String getGnodeVesion() {
		return gnodeVesion;
	}

	public void setGnodeVesion(String gnodeVesion) {
		this.gnodeVesion = gnodeVesion;
	}

	@Length(min=0, max=64, message="自定义大赛或项目id长度必须介于 0 和 64 之间")
	public String getPromodelId() {
		return promodelId;
	}

	public void setPromodelId(String promodelId) {
		this.promodelId = promodelId;
	}

	@Length(min=0, max=64, message="审核人id长度必须介于 0 和 64 之间")
	public String getAuditId() {
		return auditId;
	}

	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}

	@Length(min=0, max=64, message="审核级别：1代表院级 2代表校级长度必须介于 0 和 64 之间")
	public String getAuditLevel() {
		return auditLevel;
	}

	public void setAuditLevel(String auditLevel) {
		this.auditLevel = auditLevel;
	}

	@Length(min=0, max=64, message="评审名称，如评分、答辩等长度必须介于 0 和 64 之间")
	public String getAuditName() {
		return auditName;
	}

	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}

	@Length(min=0, max=64, message="审核节点id长度必须介于 0 和 64 之间")
	public String getGnodeId() {
		return gnodeId;
	}

	public void setGnodeId(String gnodeId) {
		this.gnodeId = gnodeId;
	}

	@Length(min=0, max=512, message="意见长度必须介于 0 和 512 之间")
	public String getSuggest() {
		return suggest;
	}

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	@Length(min=0, max=1, message="审核结果 0：不合格1：合格长度必须介于 0 和 1 之间")
	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	@Length(min=0, max=64, message="流程实例id长度必须介于 0 和 64 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean complete) {
		isComplete = complete;
	}

	@Override
	public int compareTo(Object o) {
		ActYwAuditInfo info = (ActYwAuditInfo)o;
		return Integer.valueOf((this.getUpdateDate().getTime() - info.getUpdateDate().getTime()) + "");
	}
}
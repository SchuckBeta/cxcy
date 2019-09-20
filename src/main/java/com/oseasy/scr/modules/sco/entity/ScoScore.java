package com.oseasy.scr.modules.sco.entity;

import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.modules.sys.entity.User;

import org.hibernate.validator.constraints.Length;

/**
 * 学分汇总Entity.
 * @author chenh
 * @version 2017-07-18
 */
public class ScoScore extends DataEntity<ScoScore> {

	private static final long serialVersionUID = 1L;
	private User user;		// 用户ID
	private String courseId;		// 课程ID
	private Double courseScore;		// 课程学分
	private String businessProId;		// 创新项目ID
	private Double businessScore;		// 创新学分
	private String innovateProId;		// 创业项目ID
	private Double innovateScore;		// 创业学分
	private String creditId;		// 素质ID
	private Double creditScore;		// 素质学分
	private String skillId;		// 技能ID
	private Double skillScore;		// 技能学分
    private Double totalScore;   // 总学分

	public ScoScore() {
		super();
	}

	public ScoScore(String id) {
		super(id);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Length(min=0, max=64, message="课程ID长度必须介于 0 和 64 之间")
	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public Double getCourseScore() {
		return courseScore;
	}

	public void setCourseScore(Double courseScore) {
		this.courseScore = courseScore;
	}

	@Length(min=0, max=64, message="创新项目ID长度必须介于 0 和 64 之间")
	public String getBusinessProId() {
		return businessProId;
	}

	public void setBusinessProId(String businessProId) {
		this.businessProId = businessProId;
	}

	public Double getBusinessScore() {
		return businessScore;
	}

	public void setBusinessScore(Double businessScore) {
		this.businessScore = businessScore;
	}

	@Length(min=0, max=64, message="创业项目ID长度必须介于 0 和 64 之间")
	public String getInnovateProId() {
		return innovateProId;
	}

	public void setInnovateProId(String innovateProId) {
		this.innovateProId = innovateProId;
	}

	public Double getInnovateScore() {
		return innovateScore;
	}

	public void setInnovateScore(Double innovateScore) {
		this.innovateScore = innovateScore;
	}

	@Length(min=0, max=64, message="素质ID长度必须介于 0 和 64 之间")
	public String getCreditId() {
		return creditId;
	}

	public void setCreditId(String creditId) {
		this.creditId = creditId;
	}

	public Double getCreditScore() {
		return creditScore;
	}

	public void setCreditScore(Double creditScore) {
		this.creditScore = creditScore;
	}

	@Length(min=0, max=64, message="技能ID长度必须介于 0 和 64 之间")
	public String getSkillId() {
		return skillId;
	}

	public void setSkillId(String skillId) {
		this.skillId = skillId;
	}

	public Double getSkillScore() {
		return skillScore;
	}

	public void setSkillScore(Double skillScore) {
		this.skillScore = skillScore;
	}

  public Double getTotalScore() {
    if (totalScore == null) {
      double total = 0.0;
      if (this.courseScore == null) {
        total += 0.0;
      }else{
        total += this.courseScore;
      }
      if (this.businessScore == null) {
        total += 0.0;
      }else{
        total += this.businessScore;
      }
      if (this.innovateScore == null) {
        total += 0.0;
      }else{
        total += this.innovateScore;
      }
      if (this.creditScore == null) {
        total += 0.0;
      }else{
        total += this.creditScore;
      }
      if (this.skillScore == null) {
        total += 0.0;
      }else{
        total += this.skillScore;
      }
      this.totalScore = total;
    }
    return totalScore;
  }

  public void setTotalScore(Double totalScore) {
    this.totalScore = totalScore;
  }
}
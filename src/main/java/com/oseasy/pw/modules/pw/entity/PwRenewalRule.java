package com.oseasy.pw.modules.pw.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 续期配置Entity.
 * @author zy
 * @version 2018-01-04
 */
public class PwRenewalRule extends DataEntity<PwRenewalRule> {

	private static final long serialVersionUID = 1L;
	private String isWarm;		// 是否预警 1：是自动，0不是自动
	private Integer applyMaxNum;		// 入驻申报最大数
	private Integer warmTime;		// 多长时间自动预警（天）
	private String isHatback;		// 是否自动退孵 1：是自动，0不是自动
	private Integer hatbackTime;		// 多长时间自动退孵（天）

	public PwRenewalRule() {
		super();
	}

	public PwRenewalRule(String id){
		super(id);
	}

	public Integer getApplyMaxNum() {
    return applyMaxNum;
  }

  public void setApplyMaxNum(Integer applyMaxNum) {
    this.applyMaxNum = applyMaxNum;
  }

  @Length(min=0, max=2, message="是否预警 1：是自动，0不是自动长度必须介于 0 和 2 之间")
	public String getIsWarm() {
		return isWarm;
	}

	public void setIsWarm(String isWarm) {
		this.isWarm = isWarm;
	}

  public Integer getWarmTime() {
    return warmTime;
  }

  public void setWarmTime(Integer warmTime) {
    this.warmTime = warmTime;
  }

  public String getIsHatback() {
    return isHatback;
  }

  public void setIsHatback(String isHatback) {
    this.isHatback = isHatback;
  }

  public Integer getHatbackTime() {
    return hatbackTime;
  }

  public void setHatbackTime(Integer hatbackTime) {
    this.hatbackTime = hatbackTime;
  }
}
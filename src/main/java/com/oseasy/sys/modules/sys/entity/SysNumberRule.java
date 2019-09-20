package com.oseasy.sys.modules.sys.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

import java.util.List;

/**
 * 编号规则管理Entity.
 * @author 李志超
 * @version 2018-05-17
 */
public class SysNumberRule extends DataEntity<SysNumberRule> {

	private static final long serialVersionUID = 1L;
	private String name;		// 规则名称
	private String appType;		// 应用类型
	private String appTypeName;		// 应用类型名称
	private String rule;		// 规则
	private Integer increNum;		// 当前自增编号值
	private Integer levelIndex;   //项目级别下标
	private List<SysNumberRuleDetail> sysNumberRuleDetailList;

	private String isPublish;		// 是否发布1：发布 0：未发布
	public static final String KEY_SYS_NUMBER_RULE = "sysNumberRule";

	public SysNumberRule() {
		super();
	}

	public SysNumberRule(String id){
		super(id);
	}

	public String getIsPublish() {
		return isPublish;
	}

	public void setIsPublish(String isPublish) {
		this.isPublish = isPublish;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=0, max=64, message="应用类型长度必须介于 0 和 64 之间")
	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getAppTypeName() {
		return appTypeName;
	}

	public void setAppTypeName(String appTypeName) {
		this.appTypeName = appTypeName;
	}

	@Length(min=0, max=128, message="规则长度必须介于 0 和 128 之间")
	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	@Length(min=0, max=11, message="当前自增编号值长度必须介于 0 和 11 之间")
	public Integer getIncreNum() {
		return increNum;
	}

	public void setIncreNum(Integer increNum) {
		this.increNum = increNum;
	}

	public Integer getLevelIndex() {
		return levelIndex;
	}

	public void setLevelIndex(Integer levelIndex) {
		this.levelIndex = levelIndex;
	}

	public List<SysNumberRuleDetail> getSysNumberRuleDetailList() {
		return sysNumberRuleDetailList;
	}

	public void setSysNumberRuleDetailList(List<SysNumberRuleDetail> sysNumberRuleDetailList) {
		this.sysNumberRuleDetailList = sysNumberRuleDetailList;
	}
}
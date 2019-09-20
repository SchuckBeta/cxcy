package com.oseasy.scr.modules.scr.entity;

import com.oseasy.com.pcore.common.persistence.DataExtEntity;
import com.oseasy.scr.modules.scr.vo.IScoObj;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import org.hibernate.validator.constraints.Length;

/**
 * 学分规则详情Entity.
 * @author chenh
 * @version 2018-12-21
 */
public class ScoRuleDetail extends DataExtEntity<ScoRuleDetail> implements IScoObj {

	private static final long serialVersionUID = 1L;
	private ScoRule rule;		// 规则编号
	private String name;		// 名称
	private double score;		// 分值
	private double scMin;		// 最小分值:自定义分值不能低于该值
	private double scMax;		// 最大分值：自定义分值不能超过该值
	private Integer level;		// 权重,标准有互斥情况下，根据权重取分值:默认0
	private String isLimitm;		// 是否限制学分最值（最大、最小）：默认是：0、否；1、是
	private String isHalf;		// 是否需要折半：默认是：0、否；1、是
	private String halfRemarks;		// 折半备注说明：需要折半时必填,便于对最终生成的分数不正确情况作原因说明
	private String joinType;		// 是否为参与类型：0：否 1：是，有次数上限(取join_max）；2、是：无次数上限（可无限累加）
	private String joinMax;		// 累加最大值：is_join=1时生效
	private String condition;		// 区间(60,}&gt;60 [50,70]50 									&lt;/td&gt;									&lt;td nowrap&gt;										&lt;input type=
	private String condName;		// 区间名称
	private String condType;		// 条件校验类型：默认0：1、字典值(使用字典key）;2、区间值(）
	private String isCondcheck;		// 是否对执行条件校验：默认否：0、否；1、是
	private String unit;		// 认定单位、部门、认定对象所属机构
	private String maxOrSum; //计算学分规则 0-取最高级别分数 ，1-累计分值
	private String isLowSco; //是否低于x分按x分计算: 0否1是
	private String isJoin; //是否累计不超过x分: 0否1是
	private String lowSco; //低于x分中x的值
	private String lowScoMax; //按x分计算中x的值
	private int  sort; //排序值
	private String isSpecial; //是否特例

	public String getIsSpecial() {
		return isSpecial;
	}

	public void setIsSpecial(String isSpecial) {
		this.isSpecial = isSpecial;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getMaxOrSum() {
		return maxOrSum;
	}

	public void setMaxOrSum(String maxOrSum) {
		this.maxOrSum = maxOrSum;
	}

	public String getIsLowSco() {
		return isLowSco;
	}

	public void setIsLowSco(String isLowSco) {
		this.isLowSco = isLowSco;
	}

	public String getIsJoin() {
		return isJoin;
	}

	public void setIsJoin(String isJoin) {
		this.isJoin = isJoin;
	}

	public String getLowSco() {
		return lowSco;
	}

	public void setLowSco(String lowSco) {
		this.lowSco = lowSco;
	}

	public String getLowScoMax() {
		return lowScoMax;
	}

	public void setLowScoMax(String lowScoMax) {
		this.lowScoMax = lowScoMax;
	}

	public ScoRuleDetail() {
		super();
	}

	public ScoRuleDetail(String id){
		super(id);
	}

	public ScoRule getRule() {
        return rule;
    }

    public void setRule(ScoRule rule) {
        this.rule = rule;
    }

    @Length(min=1, max=100, message="名称长度必须介于 1 和 100 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public double getScMin() {
		return scMin;
	}

	public void setScMin(double scMin) {
		this.scMin = scMin;
	}

	public double getScMax() {
		return scMax;
	}

	public void setScMax(double scMax) {
		this.scMax = scMax;
	}


	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Length(min=0, max=11, message="是否限制学分最值（最大、最小）：默认是：0、否；1、是长度必须介于 0 和 11 之间")
	public String getIsLimitm() {
		return isLimitm;
	}

	public void setIsLimitm(String isLimitm) {
		this.isLimitm = isLimitm;
	}

	@Length(min=0, max=11, message="是否需要折半：默认是：0、否；1、是长度必须介于 0 和 11 之间")
	public String getIsHalf() {
		return isHalf;
	}

	public void setIsHalf(String isHalf) {
		this.isHalf = isHalf;
	}

	@Length(min=0, max=1000, message="折半备注说明：需要折半时必填,便于对最终生成的分数不正确情况作原因说明长度必须介于 0 和 1000 之间")
	public String getHalfRemarks() {
		return halfRemarks;
	}

	public void setHalfRemarks(String halfRemarks) {
		this.halfRemarks = halfRemarks;
	}

//	@Length(min=1, max=1, message="是否为参与类型：0：否 1：是，有次数上限(取join_max）；2、是：无次数上限（可无限累加）长度必须介于 1 和 1 之间")
	public String getJoinType() {
		return joinType;
	}

	public void setJoinType(String joinType) {
		this.joinType = joinType;
	}

	public String getJoinMax() {
		return joinMax;
	}

	public void setJoinMax(String joinMax) {
		this.joinMax = joinMax;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	@Length(min=0, max=255, message="区间名称长度必须介于 0 和 255 之间")
	public String getCondName() {
		return condName;
	}

	public void setCondName(String condName) {
		this.condName = condName;
	}

	@Length(min=0, max=11, message="条件校验类型：默认0：1、字典值(使用字典key）;2、区间值(）长度必须介于 0 和 11 之间")
	public String getCondType() {
		return condType;
	}

	public void setCondType(String condType) {
		this.condType = condType;
	}

	@Length(min=0, max=11, message="是否对执行条件校验：默认否：0、否；1、是长度必须介于 0 和 11 之间")
	public String getIsCondcheck() {
		return isCondcheck;
	}

	public void setIsCondcheck(String isCondcheck) {
		this.isCondcheck = isCondcheck;
	}

	@Length(min=0, max=1000, message="认定单位、部门、认定对象所属机构长度必须介于 0 和 1000 之间")
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}


	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
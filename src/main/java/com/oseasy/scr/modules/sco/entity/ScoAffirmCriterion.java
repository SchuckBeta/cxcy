package com.oseasy.scr.modules.sco.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 学分认定标准Entity.
 * @author 9527
 * @version 2017-07-18
 */
public class ScoAffirmCriterion extends DataEntity<ScoAffirmCriterion> {

	private static final long serialVersionUID = 1L;
	private String affirmConfId;		// 学分认定配置表id
	private String category;		// 标准分类
	private String result;		// 标准结果
	private String score;		// 分值
	private String sort;		// 显示排序
	private String dataJson;//页面输入值json串
	
	public String getDataJson() {
		return dataJson;
	}

	public void setDataJson(String dataJson) {
		this.dataJson = dataJson;
	}

	public ScoAffirmCriterion() {
		super();
	}

	public ScoAffirmCriterion(String id) {
		super(id);
	}

	@Length(min=1, max=64, message="学分认定配置表id长度必须介于 1 和 64 之间")
	public String getAffirmConfId() {
		return affirmConfId;
	}

	public void setAffirmConfId(String affirmConfId) {
		this.affirmConfId = affirmConfId;
	}

	@Length(min=0, max=11, message="标准分类长度必须介于 0 和 11 之间")
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Length(min=0, max=64, message="标准结果长度必须介于 0 和 64 之间")
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	@Length(min=0, max=2, message="显示排序长度必须介于 0 和 2 之间")
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

}
package com.oseasy.scr.modules.sco.entity;

import org.hibernate.validator.constraints.Length;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.oseasy.com.pcore.common.persistence.DataEntity;

import javax.validation.constraints.NotNull;

/**
 * 课程学分认定标准Entity.
 * @author 9527
 * @version 2017-07-18
 */
public class ScoAffirmCriterionCouse extends DataEntity<ScoAffirmCriterionCouse> {

	private static final long serialVersionUID = 1L;
	private String foreignId;		// 学分认定配置表id/课程id
	private String parentId;		// 父级id,0代表是描述课程,非零代表是描述分数,只有两级
	private String start;		// 起始值
	private String end;		// 截止值
	private String sort;		// 排序
	private String score;		// 规则分值
	private String dataJson;
	private String fromPage;
	
	public String getFromPage() {
		return fromPage;
	}

	public void setFromPage(String fromPage) {
		this.fromPage = fromPage;
	}

	public String getDataJson() {
		return dataJson;
	}

	public void setDataJson(String dataJson) {
		this.dataJson = dataJson;
	}

	public ScoAffirmCriterionCouse() {
		super();
	}

	public ScoAffirmCriterionCouse(String id) {
		super(id);
	}

	@Length(min=1, max=64, message="学分认定配置表id/课程id长度必须介于 1 和 64 之间")
	public String getForeignId() {
		return foreignId;
	}

	public void setForeignId(String foreignId) {
		this.foreignId = foreignId;
	}

	@JsonBackReference
	@NotNull(message="父级id,0代表是描述课程,非零代表是描述分数,只有两级不能为空")
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	@Length(min=1, max=3, message="排序长度必须介于 1 和 3 之间")
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

}
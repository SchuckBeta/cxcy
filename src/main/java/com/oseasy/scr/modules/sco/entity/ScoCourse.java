package com.oseasy.scr.modules.sco.entity;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 学分课程Entity.
 * @author 张正
 * @version 2017-07-13
 */
public class ScoCourse extends DataEntity<ScoCourse> {

	private static final long serialVersionUID = 1L;
	private String code;		// 课程代码
	private String name;		// 课程名
	private String type;		// 课程类型
	private String nature;		// 课程性质
	private String professional;		// 面向专业科类：理科/文科/工科/医科
	private String planScore;		// 计划学分
	private String planTime;		// 计划课时
	private String overScore;		// 合格成绩

	private String keyword; //关键字

	private List<String> professionalList;

	public ScoCourse() {
		super();
	}

	public ScoCourse(String id) {
		super(id);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public String getProfessional() {
		return professional;
	}

	public void setProfessional(String professional) {
		this.professional = professional;
	}

	public String getPlanScore() {
		return planScore;
	}

	public void setPlanScore(String planScore) {
		this.planScore = planScore;
	}

	public String getPlanTime() {
		return planTime;
	}

	public void setPlanTime(String planTime) {
		this.planTime = planTime;
	}

	public String getOverScore() {
		return overScore;
	}

	public void setOverScore(String overScore) {
		this.overScore = overScore;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public List<String> getProfessionalList() {
		if (this.professionalList!=null) {
			return this.professionalList;
		}
		List<String> list = Lists.newArrayList();
		if (StringUtil.isNotBlank(professional)) {
			String[]  professionalStr = professional.split(",");
			for (String str:professionalStr) {
				list.add(str);
			}
		}
		return list;
	}

	public void setProfessionalList(List<String> professionalList) {
		this.professionalList = professionalList;
		if (this.professionalList!=null) {
			this.setProfessional(StringUtils.join(this.professionalList.toArray(), ","));
		}
	}

}
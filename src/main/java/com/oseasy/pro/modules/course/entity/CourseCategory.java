package com.oseasy.pro.modules.course.entity;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 课程分类Entity.
 * @author zhangzheng
 * @version 2017-06-28
 */
public class CourseCategory extends DataEntity<CourseCategory> {

	private static final long serialVersionUID = 1L;
	private String courseId;		// 课程信息表id
	private String value;		// 专业课程值（对应字典表的value)
	private String label;     // 专业课程名称(对应字典表的label)

	public CourseCategory() {
		super();
	}

	public CourseCategory(String id) {
		super(id);
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
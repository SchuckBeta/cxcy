package com.oseasy.pro.modules.course.entity;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 课程教师Entity.
 * @author zhangzheng
 * @version 2017-06-28
 */
public class CourseTeacher extends DataEntity<CourseTeacher> {

	private static final long serialVersionUID = 1L;
	private String courseId;		// 课程信息表id
	private String teacherId;		// 教师userid
	private String teacherName;     //教师姓名
	private String collegeName;		// 学院
	private String postName;		// 职务

	public CourseTeacher() {
		super();
	}

	public CourseTeacher(String id) {
		super(id);
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getCollegeName() {
		return collegeName;
	}

	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	@Override
	public boolean equals(Object obj) {
	  if (obj != null) {
  		CourseTeacher teacher = (CourseTeacher) obj ;
  		if (this.getTeacherId().equals(teacher.getTeacherId())) {
  			return true;
  		}
	  }
		return false;
	}

  @Override
  public int hashCode() {
    return teacherId.hashCode();
  }
}
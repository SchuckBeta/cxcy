package com.oseasy.sys.modules.sys.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * teacherKeywordEntity.
 * @author zy
 * @version 2017-07-03
 */
public class TeacherKeyword extends DataEntity<TeacherKeyword> {

	private static final long serialVersionUID = 1L;
	private String teacherId;		// 导师信息表id
	private String keyword;		// 关键字

	public TeacherKeyword() {
		super();
	}

	public TeacherKeyword(String id) {
		super(id);
	}

	@Length(min=0, max=64, message="导师信息表id长度必须介于 0 和 64 之间")
	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	@Length(min=0, max=16, message="关键字长度必须介于 0 和 16 之间")
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
package com.oseasy.pro.modules.course.dao;

import com.oseasy.pro.modules.course.entity.CourseTeacher;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

import java.util.List;

/**
 * 课程教师DAO接口.
 * @author zhangzheng
 * @version 2017-06-28
 */
@MyBatisDao
public interface CourseTeacherDao extends CrudDao<CourseTeacher> {

    public List<CourseTeacher> getByCourseId(String courseId);

    public void deleteByCourseId(String courseId);

}
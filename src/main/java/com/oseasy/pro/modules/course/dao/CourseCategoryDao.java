package com.oseasy.pro.modules.course.dao;

import com.oseasy.pro.modules.course.entity.CourseCategory;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

import java.util.List;

/**
 * 课程分类DAO接口.
 * @author zhangzheng
 * @version 2017-06-28
 */
@MyBatisDao
public interface CourseCategoryDao extends CrudDao<CourseCategory> {
    public List<CourseCategory> getByCourseId(String courseId);

    public void deleteByCourseId(String courseId);
}
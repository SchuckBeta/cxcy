package com.oseasy.pro.modules.course.dao;

import com.oseasy.com.pcore.common.persistence.annotation.FindDictByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import com.oseasy.pro.modules.course.entity.Course;
import com.oseasy.pro.modules.course.entity.CourseTeacher;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 课程主表DAO接口.
 * @author 张正
 * @version 2017-06-28
 */
@MyBatisDao
public interface CourseDao extends CrudDao<Course> {
    @Override
    @FindListByTenant
    public List<Course> findList(Course entity);
    @Override
    @InsertByTenant
    public int insert(Course entity);
    //更新下载量
    public void updateDownloads(Course course);
    //更新浏览量
    public void updateViews(Course course);
    //查找所有能搜索的老师
    @FindListByTenant
    public List<CourseTeacher> findTeacherListForCourse();
    @FindListByTenant
    @FindDictByTenant
    public List<Course> findFrontCourse();

    public List<Course> findRecommedList(Course course);
    @FindListByTenant
    public List<Course> findFrontList(Course course);
	public void updateComments(@Param("param") Map<String, Integer> param);
    public void updateViewsPlus(@Param("param") Map<String, Integer> param);
    public void updateLikes(@Param("param") Map<String, Integer> param);

}
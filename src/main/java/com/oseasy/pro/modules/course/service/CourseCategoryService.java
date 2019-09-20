package com.oseasy.pro.modules.course.service;

import com.oseasy.pro.modules.course.dao.CourseCategoryDao;
import com.oseasy.pro.modules.course.entity.CourseCategory;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 课程分类Service.
 * @author zhangzheng
 * @version 2017-06-28
 */
@Service
@Transactional(readOnly = true)
public class CourseCategoryService extends CrudService<CourseCategoryDao, CourseCategory> {

	public CourseCategory get(String id) {
		return super.get(id);
	}

	public List<CourseCategory> findList(CourseCategory courseCategory) {
		return super.findList(courseCategory);
	}

	public Page<CourseCategory> findPage(Page<CourseCategory> page, CourseCategory courseCategory) {
		return super.findPage(page, courseCategory);
	}

	@Transactional(readOnly = false)
	public void save(CourseCategory courseCategory) {
		super.save(courseCategory);
	}

	@Transactional(readOnly = false)
	public void delete(CourseCategory courseCategory) {
		super.delete(courseCategory);
	}

	public List<CourseCategory>  getByCourseId(String courseId) {
		return dao.getByCourseId(courseId);
	}

}
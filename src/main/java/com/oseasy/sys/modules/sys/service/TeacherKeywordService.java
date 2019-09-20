package com.oseasy.sys.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.sys.modules.sys.dao.TeacherKeywordDao;
import com.oseasy.sys.modules.sys.entity.TeacherKeyword;

/**
 * teacherKeywordService.
 * @author zy
 * @version 2017-07-03
 */
@Service
@Transactional(readOnly = true)
public class TeacherKeywordService extends CrudService<TeacherKeywordDao, TeacherKeyword> {
	@Autowired
	private TeacherKeywordDao teacherKeywordDao;
	public TeacherKeyword get(String id) {
		return super.get(id);
	}

	public List<TeacherKeyword> findList(TeacherKeyword teacherKeyword) {
		return super.findList(teacherKeyword);
	}

	public Page<TeacherKeyword> findPage(Page<TeacherKeyword> page, TeacherKeyword teacherKeyword) {
		return super.findPage(page, teacherKeyword);
	}

	public List<TeacherKeyword> getKeywordByTeacherid(String teacherId) {
		return teacherKeywordDao.findByTeacherid(teacherId);
	}

	public List<String> getStringKeywordByTeacherid(String teacherId) {
		return teacherKeywordDao.findStringByTeacherid(teacherId);
	}

	@Transactional(readOnly = false)
	public void save(TeacherKeyword teacherKeyword) {
		super.save(teacherKeyword);
	}

	@Transactional(readOnly = false)
	public void delete(TeacherKeyword teacherKeyword) {
		super.delete(teacherKeyword);
	}

}
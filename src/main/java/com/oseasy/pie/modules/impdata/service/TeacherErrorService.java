package com.oseasy.pie.modules.impdata.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pie.modules.impdata.dao.TeacherErrorDao;
import com.oseasy.pie.modules.impdata.entity.TeacherError;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.service.SysSystemService;

/**
 * 导入导师错误数据表Service
 * @author 9527
 * @version 2017-05-22
 */
@Service
@Transactional(readOnly = true)
public class TeacherErrorService extends CrudService<TeacherErrorDao, TeacherError> {
    @Autowired
    private SysSystemService systemService;
	public List<Map<String,String>> getListByImpId(String impid) {
		return dao.getListByImpId(impid);
	}

	public TeacherError get(String id) {
		return super.get(id);
	}

	public List<TeacherError> findList(TeacherError teacherError) {
		return super.findList(teacherError);
	}

	public Page<TeacherError> findPage(Page<TeacherError> page, TeacherError teacherError) {
		return super.findPage(page, teacherError);
	}

	@Transactional(readOnly = false)
	public void save(TeacherError teacherError) {
		super.save(teacherError);
	}

	@Transactional(readOnly = false)
	public void delete(TeacherError teacherError) {
		super.delete(teacherError);
	}
	@Transactional(readOnly = false)
	public void insert(TeacherError teacherError) {
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())) {
			teacherError.setUpdateBy(user);
			teacherError.setCreateBy(user);
		}
		teacherError.setUpdateDate(new Date());
		teacherError.setCreateDate(teacherError.getUpdateDate());
		dao.insert(teacherError);
	}
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void saveTeacher(BackTeacherExpansion tc) {
        User nuser=tc.getUser();
        User user = UserUtils.getUser();
        User ouser = UserUtils.getByLoginNameOrNo(nuser.getNo());
        if(ouser != null){
            systemService.updateTeacherByLoginName(tc, nuser, ouser);
        }else{
            systemService.newTeacherByLoginName(tc, nuser, user);
        }
	}
	@Transactional(readOnly = false)
	public void insertPl(List<TeacherError> teList) {
		for(TeacherError teacherError:teList){
			User user = UserUtils.getUser();
			if (StringUtils.isNotBlank(user.getId())) {
				teacherError.setUpdateBy(user);
				teacherError.setCreateBy(user);
			}
			teacherError.setUpdateDate(new Date());
			teacherError.setCreateDate(teacherError.getUpdateDate());
			dao.insert(teacherError);
		}
	}
}
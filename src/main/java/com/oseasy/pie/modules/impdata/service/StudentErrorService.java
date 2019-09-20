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
import com.oseasy.pie.modules.impdata.dao.StudentErrorDao;
import com.oseasy.pie.modules.impdata.entity.StudentError;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;
import com.oseasy.sys.modules.sys.service.SysSystemService;

/**
 * 导入学生错误数据表Service
 * @author 9527
 * @version 2017-05-16
 */
@Service
@Transactional(readOnly = true)
public class StudentErrorService extends CrudService<StudentErrorDao, StudentError> {
    @Autowired
    private SysSystemService systemService;
	public List<Map<String,String>> getListByImpId(String impid) {
		return dao.getListByImpId(impid);
	}
	public StudentError get(String id) {
		return super.get(id);
	}

	public List<StudentError> findList(StudentError studentError) {
		return super.findList(studentError);
	}

	public Page<StudentError> findPage(Page<StudentError> page, StudentError studentError) {
		return super.findPage(page, studentError);
	}

	@Transactional(readOnly = false)
	public void save(StudentError studentError) {
		super.save(studentError);
	}
	@Transactional(readOnly = false)
	public void insert(StudentError studentError) {
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())) {
			studentError.setUpdateBy(user);
			studentError.setCreateBy(user);
		}
		studentError.setUpdateDate(new Date());
		studentError.setCreateDate(studentError.getUpdateDate());
		dao.insert(studentError);
	}
	@Transactional(readOnly = false)
	public void delete(StudentError studentError) {
		super.delete(studentError);
	}
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public StudentExpansion saveStudent(StudentExpansion st) {
		User nuser=st.getUser();
		User user = UserUtils.getUser();
		User ouser = UserUtils.getByLoginNameOrNo(nuser.getNo());
		if(ouser != null){
		    nuser=systemService.updateStudentByLoginName(st, nuser, ouser);
		}else{
		    nuser=systemService.newStudentByLoginName(st, nuser, user);
		}
		st.setUser(nuser);
		return st;
	}
	@Transactional(readOnly = false)
	public void insertPl(List<StudentError> seList) {
		for(StudentError studentError:seList){
			User user = UserUtils.getUser();
			if (StringUtils.isNotBlank(user.getId())) {
				studentError.setUpdateBy(user);
				studentError.setCreateBy(user);
			}
			studentError.setUpdateDate(new Date());
			studentError.setCreateDate(studentError.getUpdateDate());
			dao.insert(studentError);

		}
	}
}
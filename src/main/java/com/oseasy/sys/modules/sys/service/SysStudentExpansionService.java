package com.oseasy.sys.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.sys.modules.sys.dao.SysStudentExpansionDao;
import com.oseasy.sys.modules.sys.entity.SysStudentExpansion;
import com.oseasy.sys.modules.sys.entity.UserInfo;

/**
 * 学生信息表Service
 * @author zy
 * @version 2017-03-14
 */
@Service
@Transactional(readOnly = true)
public class SysStudentExpansionService {
	@Autowired
   	private SysStudentExpansionDao sysStudentExpansionDao;
	
	public SysStudentExpansion get(String id) {
		return sysStudentExpansionDao.get(id);
	}
	public SysStudentExpansion getByUserId(String user_id) {
		return sysStudentExpansionDao.getByUserId(user_id);
	}
	public List<SysStudentExpansion> findList(SysStudentExpansion sysStudentExpansion) {
		return sysStudentExpansionDao.findList(sysStudentExpansion);
	}
	
	/*public Page<SysStudentExpansion> findPage(Page<SysStudentExpansion> page, SysStudentExpansion sysStudentExpansion) {
		return sysStudentExpansionDao.findPage(page, sysStudentExpansion);
	}*/
	
	@Transactional(readOnly = false)
	public void save(SysStudentExpansion sysStudentExpansion) {
		sysStudentExpansionDao.insert(sysStudentExpansion);
	}
	
	@Transactional(readOnly = false)
	public void delete(SysStudentExpansion sysStudentExpansion) {
		sysStudentExpansionDao.delete(sysStudentExpansion);
	}


//	public List<SysStudentExpansion> getStudentList() {
//		return  sysStudentExpansionDao.getStudentList();
//	}

	public UserInfo findUserInfo(String id) {
		return sysStudentExpansionDao.findUserInfo(id);
	}
	
}
package com.oseasy.sys.modules.sys.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.sys.modules.sys.entity.SysStudentExpansion;
import com.oseasy.sys.modules.sys.entity.UserInfo;

import java.util.List;

/**
 * 学生信息表DAO接口
 * @author zy
 * @version 2017-03-14
 */
@MyBatisDao
public interface SysStudentExpansionDao extends CrudDao<SysStudentExpansion> {
	public SysStudentExpansion getByUserId(String user_id);
	//关联sys_user表，获取学生list addBy张正
	public List<SysStudentExpansion> getStudentList();
	public UserInfo findUserInfo(String id);
}
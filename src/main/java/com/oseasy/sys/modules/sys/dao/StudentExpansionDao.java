package com.oseasy.sys.modules.sys.dao;

import java.util.List;
import java.util.Map;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;

/**
 * 学生信息表DAO接口
 * @author zy
 * @version 2017-03-27
 */
@MyBatisDao
public interface StudentExpansionDao extends CrudDao<StudentExpansion> {
	@Override
	@InsertByTenant
	public int insert(StudentExpansion entity);
	public void updateCurrState();
	public void updateCurrStateByConfig(String mo);

	public StudentExpansion getByUserId(String userId);

	public int getStuExListCount(Map<String, Object> param);

	public List<Map<String, String>> getStuExList(Map<String, Object> param);

	List<StudentExpansion> getStudentByTeamId(String teamId);
}
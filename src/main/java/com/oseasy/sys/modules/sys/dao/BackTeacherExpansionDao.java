package com.oseasy.sys.modules.sys.dao;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.annotation.FindDictByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.team.entity.Team;

/**
 * 导师信息表DAO接口
 * @author l
 * @version 2017-03-31
 */
@MyBatisDao
public interface BackTeacherExpansionDao extends CrudDao<BackTeacherExpansion> {

	@Override
	@InsertByTenant
	public int insert(BackTeacherExpansion entity);
	public List<BackTeacherExpansion> getServiceIntention(@Param("teas")List<BackTeacherExpansion> teas);
	BackTeacherExpansion findTeacherByUserIdAndType(@Param(value="userId")String userId,@Param(value="type")String type);
	public BackTeacherExpansion getByUserId(String uid);
	List<Team> findTeamById(@Param(value="userId") String userId);

	public List<String> getKeys(String tid);
	public List<BackTeacherExpansion> findTeacherAward(String userId);


	BackTeacherExpansion findTeacherByUserId(String userId);
	@FindListByTenant
	List<BackTeacherExpansion> findTeacherList(BackTeacherExpansion backTeacherExpansion);
	@FindListByTenant
	List<BackTeacherExpansion> findProvinceTeacherList(BackTeacherExpansion backTeacherExpansion);

	BackTeacherExpansion findTeacherByTopShow(@Param(value="teachertype") String teacherType);

	List<BackTeacherExpansion> getQYTeacher(String id);
	List<BackTeacherExpansion> getXYTeacher(String id);

	List<BackTeacherExpansion> findExpertList(BackTeacherExpansion backTeacherExpansion);
	@FindDictByTenant
	List<BackTeacherExpansion> findIndexTeacherList(BackTeacherExpansion backTeacherExpansion);

	void deleteByUserId(String userId);

	void updateExpertChange(BackTeacherExpansion backTeacherExpansion);

	List<String> findAllExpertList();

	@FindListByTenant
	List<String> findAllExpertListById(String id);

	List<String> findCollegeExpertListByPro(String proId);

	void updateType(BackTeacherExpansion backTeacherExpansion);

	List<String> getExpertAuditPro(String userId);

	void updateExpertType(BackTeacherExpansion backTeacherExpansion);

	List<String> findExpertListByType(String auditRole);
}
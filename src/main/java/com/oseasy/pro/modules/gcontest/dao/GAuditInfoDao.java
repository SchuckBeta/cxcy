package com.oseasy.pro.modules.gcontest.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.gcontest.entity.GAuditInfo;
import com.oseasy.pro.modules.project.entity.ProjectAuditInfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * 大赛信息DAO接口
 * @author zy
 * @version 2017-03-11
 */
@MyBatisDao
public interface GAuditInfoDao extends CrudDao<GAuditInfo> {
    public List<GAuditInfo> getInfo(GAuditInfo gAuditInfo);
    
    public List<GAuditInfo> getSortInfo(GAuditInfo gAuditInfo);
    
    public GAuditInfo getSortInfoByIdAndState(GAuditInfo gAuditInfo);

	public int getCollegeCount(@Param("state") String state,@Param("collegeId") String collegeId);

	public int getSchoolCount(@Param("state") String state, @Param("collegeId") String collegeId);

	public void insertByOther(GAuditInfo gAuditInfo);

	public void updateData(GAuditInfo gAuditInfo);

	public GAuditInfo getGAuditInfoByIdAndState(GAuditInfo gAuditInfo);

	public void deleteByGid(@Param("gid")String gid);

	GAuditInfo getInfoByUserId(GAuditInfo gAuditInfo);

	List<GAuditInfo> getSortByAudit(GAuditInfo pai);

	//public void changeScoreOrGrade(GAuditInfo gAuditInfo);
}
package com.oseasy.pro.modules.project.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.project.entity.ProjectAuditInfo;

import java.util.List;

/**
 * 项目审核信息DAO接口
 * @author 9527
 * @version 2017-03-11
 */
@MyBatisDao
public interface ProjectAuditInfoDao extends CrudDao<ProjectAuditInfo> {
    //addBy zhangzheng 根据projectId和update_by删除信息
	public void deleteByPidAndStep(ProjectAuditInfo projectAuditInfo);
    //addBy zhangzheng  根据projectId删除信息
    public void deleteByPid(String projectId);

    //根据 projectId 和 auditStep获取评分、审核意见
    // auditStep(1立项评审 2中期检查评分 3中期检查评级 4结项审核 5结项评级)
    public List<ProjectAuditInfo> getInfo(ProjectAuditInfo projectAuditInfo);

    public ProjectAuditInfo findInfoByUserId(ProjectAuditInfo projectAuditInfo);

    List<ProjectAuditInfo> findProjectDeclareChangeGnode(ProjectAuditInfo projectAuditInfo);
}
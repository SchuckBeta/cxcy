package com.oseasy.act.modules.actyw.dao;

import com.oseasy.act.modules.actyw.entity.ActYwAuditInfo;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自定义审核信息DAO接口.
 * @author zy
 * @version 2017-11-01
 */
@MyBatisDao
public interface ActYwAuditInfoDao extends CrudDao<ActYwAuditInfo> {

	void updateIsBack(@Param("id") String id);

	ActYwAuditInfo getLastAudit(ActYwAuditInfo actYwAuditInfo);

	List<String> findSubStringList(@Param("promodelId") String promodelId);

	ActYwAuditInfo getLastAuditByPromodel(ActYwAuditInfo actYwAuditInfo);

	List<ActYwAuditInfo> getProModelChangeGnode(ActYwAuditInfo actYwAuditInfo);
	ActYwAuditInfo getGnodeByNextGnode(ActYwAuditInfo actYwAuditInfoIn);

	ActYwAuditInfo getInAudit(ActYwAuditInfo actYwAuditInfo);

	List<ActYwAuditInfo> findNoBackList(ActYwAuditInfo actYwAuditInfo);

	List<ActYwAuditInfo> getLastAuditListByPromodel(ActYwAuditInfo actYwAuditInfo);

	void deleteByProIdAndGnodeId(ActYwAuditInfo actYwAuditInfo);

	List<ActYwAuditInfo> getActYwAuditRecord(String promodelId);

	ActYwAuditInfo getFirstAuditByPromodel(ActYwAuditInfo actYwAuditInfo);
}
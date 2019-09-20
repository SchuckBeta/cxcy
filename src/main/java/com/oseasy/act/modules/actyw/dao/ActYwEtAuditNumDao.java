package com.oseasy.act.modules.actyw.dao;

import java.util.List;

import com.oseasy.act.modules.actyw.entity.ActYwEtAssignRule;
import com.oseasy.act.modules.actyw.entity.ActYwEtAuditNum;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

import org.apache.ibatis.annotations.Param;

/**
 * 指派专家组的项目DAO接口.
 * @author zy
 * @version 2019-01-23
 */
@MyBatisDao
public interface ActYwEtAuditNumDao extends CrudDao<ActYwEtAuditNum> {
    /**
     * 批量新增.
     * @param entitys
     */
    public void insertPL(@Param("entitys") List<ActYwEtAuditNum> entitys);

    /**
     * 批量修改.
     * @param entitys
     */
    public void updatePL(@Param("entitys") List<ActYwEtAuditNum> entitys);


  	/**
   	 * 物理删除.
   	 * @param entity
   	 */
  	public void deleteWL(ActYwEtAuditNum entity);

   	/**
   	 * 批量状态删除.
   	 * @param entity
   	 */
  	public void deletePL(ActYwEtAuditNum entity);

    /**
   	 * 批量物理删除.
   	 * @param entity
   	 */
  	public void deleteWLPL(ActYwEtAuditNum entity);

    /**
     * 清空表.
     */
    public void deleteWLAll();

	List<ActYwEtAuditNum> getListByRule(ActYwEtAuditNum actYwEtAuditNum);

	void deleteByProId(ActYwEtAuditNum actYwEtAuditNum);

	ActYwEtAuditNum getByProId(ActYwEtAuditNum actYwEtAuditNum);

	@InsertByTenant
	@Override
	int insert(ActYwEtAuditNum actYwEtAuditNum);
}
package com.oseasy.act.modules.actyw.dao;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import org.apache.ibatis.annotations.Param;

import com.oseasy.act.modules.actyw.entity.ActYwEtAssignRule;
import com.oseasy.act.modules.actyw.entity.ActYwEtAssignTaskVo;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

/**
 * 专家指派规则DAO接口.
 * @author zy
 * @version 2019-01-03
 */
@MyBatisDao
public interface ActYwEtAssignRuleDao extends CrudDao<ActYwEtAssignRule> {
    /**
     * 批量新增.
     * @param entitys
     */
    public void insertPL(@Param("entitys") List<ActYwEtAssignRule> entitys);

    /**
     * 批量修改.
     * @param entitys
     */
    public void updatePL(@Param("entitys") List<ActYwEtAssignRule> entitys);


  	/**
   	 * 物理删除.
   	 * @param entity
   	 */
  	public void deleteWL(ActYwEtAssignRule entity);

   	/**
   	 * 批量状态删除.
   	 * @param entity
   	 */
  	public void deletePL(ActYwEtAssignRule entity);

    /**
   	 * 批量物理删除.
   	 * @param entity
   	 */
  	public void deleteWLPL(ActYwEtAssignRule entity);

    /**
     * 清空表.
     */
    public void deleteWLAll();

	@FindListByTenant
	List<ActYwEtAssignTaskVo> findActYwEtAssignTaskVoList(ActYwEtAssignTaskVo actYwEtAssignTaskVo);

	ActYwEtAssignRule getActYwEtAssignRuleByActywIdAndGnodeId(@Param("actywId") String actywId,@Param("gnodeId") String gnodeId);

	@FindListByTenant
	List<ActYwEtAssignTaskVo> findQueryList(String proType);

}
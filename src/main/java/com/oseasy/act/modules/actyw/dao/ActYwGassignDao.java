package com.oseasy.act.modules.actyw.dao;

import com.oseasy.act.modules.actyw.entity.ActYwEtAssignTaskVo;
import com.oseasy.act.modules.actyw.entity.ActYwGassign;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务指派表DAO接口.
 *
 * @author zy
 * @version 2018-04-03
 */
@MyBatisDao
public interface ActYwGassignDao extends CrudDao<ActYwGassign> {
	/**
	 * 物理删除.
	 */
	void deleteWL(ActYwGassign actYwGassign);

	@FindListByTenant
	List<ActYwGassign> getListByActYwGassign(ActYwGassign actYwGassign);

	@InsertByTenant
	void insertPl(@Param("list") List<ActYwGassign> insertActYwGassignList);

	void deleteByAssign(ActYwGassign actYwGassign);

	@FindListByTenant
	List<String> getProListByActYwEtAssignTaskVo(ActYwEtAssignTaskVo actYwEtAssignTaskVo);

	@FindListByTenant
	ActYwGassign getByActYwGassign(ActYwGassign actYwGassign);

	@FindListByTenant
	List<String> getProListByActYwGassign(ActYwGassign actYwGassign);

	@FindListByTenant
	List<String> getProListTodoDelegate(ActYwEtAssignTaskVo actYwEtAssignTaskVo);

	void deleteTodo(ActYwGassign actYwGassign);

	@FindListByTenant
	List<String> getTodoDelegateList(ActYwGassign actYwGassign);

	@FindListByTenant
	List<ActYwGassign> findListByPro(ActYwGassign entity);

	@FindListByTenant
	List<String> getTodoProListByActYwGassign(ActYwGassign actYwGassign);

	@FindListByTenant
	List<ActYwGassign> getUserDelegateList(ActYwGassign actYwGassign);

	@FindListByTenant
	List<String> getProListHasDelegate(ActYwEtAssignTaskVo actYwEtAssignTaskVo);

	@FindListByTenant
	public List<ActYwGassign> findListByTenant(ActYwGassign entity);

	/**
	 * 插入数据
	 *
	 * @param entity
	 * @return
	 */
	@InsertByTenant
	public int insert(ActYwGassign entity);
}
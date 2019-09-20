package com.oseasy.scr.modules.scr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.scr.modules.scr.entity.ScoRapplyMember;
import com.oseasy.scr.modules.scr.entity.ScoRapplyValid;

/**
 * 学分申请成员DAO接口.
 * @author chenhao
 * @version 2018-12-21
 */
@MyBatisDao
public interface ScoRapplyMemberDao extends CrudDao<ScoRapplyMember> {
    /**
     * 批量新增.
     * @param entitys
     */
    public void insertPL(@Param("entitys") List<ScoRapplyMember> entitys);

    /**
     * 批量修改.
     * @param entitys
     */
    public void updatePL(@Param("entitys") List<ScoRapplyMember> entitys);


  	/**
   	 * 物理删除.
   	 * @param entity
   	 */
  	public void deleteWL(ScoRapplyMember entity);

	public void deleteRapplyMembers(ScoRapplyMember entity);

   	/**
   	 * 批量状态删除.
   	 * @param entity
   	 */
  	public void deletePL(ScoRapplyMember entity);

    /**
   	 * 批量物理删除.
   	 * @param entity
   	 */
  	public void deleteWLPL(ScoRapplyMember entity);

    /**
     * 清空表.
     */
    public void deleteWLAll();

	public List<ScoRapplyMember> findScoMemberList(ScoRapplyMember entity);

	public List<ScoRapplyValid> ajaxValidScoMemberList(ScoRapplyValid entity);

	public List<ScoRapplyValid> ajaxValidScoRapplyList(ScoRapplyValid entity);
}
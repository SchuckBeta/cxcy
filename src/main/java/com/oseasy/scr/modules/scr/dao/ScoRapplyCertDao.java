package com.oseasy.scr.modules.scr.dao;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.TreeDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.scr.modules.scr.entity.ScoRapply;
import com.oseasy.scr.modules.scr.entity.ScoRapplyCert;

import org.apache.ibatis.annotations.Param;

/**
 * 学分申请认定DAO接口.
 * @author liangjie
 * @version 2019-01-08
 */
@MyBatisDao
public interface ScoRapplyCertDao extends TreeDao<ScoRapplyCert> {
    /**
     * 批量新增.
     * @param entitys
     */
    public void insertPL(@Param("entitys") List<ScoRapplyCert> entitys);

    /**
     * 批量修改.
     * @param entitys
     */
    public void updatePL(@Param("entitys") List<ScoRapplyCert> entitys);

	public void updateSort(ScoRapplyCert entity);
  	/**
   	 * 物理删除.
   	 * @param entity
   	 */
  	public void deleteWL(ScoRapplyCert entity);

	public void deleteChildren(ScoRapplyCert entity);

   	/**
   	 * 批量状态删除.
   	 * @param entity
   	 */
  	public void deletePL(ScoRapplyCert entity);

    /**
   	 * 批量物理删除.
   	 * @param entity
   	 */
  	public void deleteWLPL(ScoRapplyCert entity);

    /**
     * 清空表.
     */
    public void deleteWLAll();

	public Integer checkScoRaCertName(@Param("id") String id, @Param("name") String name);

	public List<ScoRapplyCert> findCertByUserList(ScoRapply scoRapply);

}
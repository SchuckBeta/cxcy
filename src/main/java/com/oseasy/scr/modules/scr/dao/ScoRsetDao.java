package com.oseasy.scr.modules.scr.dao;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.scr.modules.scr.entity.ScoRset;

/**
 * 设置学分规则DAO接口.
 * @author liangjie
 * @version 2018-12-27
 */
@MyBatisDao
public interface ScoRsetDao extends CrudDao<ScoRset> {
	@Override
	@FindListByTenant
	public List<ScoRset> findList(ScoRset entity);
	@Override
	@InsertByTenant
	public int insert(ScoRset entity);
	/**
     * 批量新增.
     * @param entitys
     */
    public void insertPL(@Param("entitys") List<ScoRset> entitys);

    /**
     * 批量修改.
     * @param entitys
     */
    public void updatePL(@Param("entitys") List<ScoRset> entitys);


  	/**
   	 * 物理删除.
   	 * @param entity
   	 */
  	public void deleteWL(ScoRset entity);

   	/**
   	 * 批量状态删除.
   	 * @param entity
   	 */
  	public void deletePL(ScoRset entity);

    /**
   	 * 批量物理删除.
   	 * @param entity
   	 */
  	public void deleteWLPL(ScoRset entity);

    /**
     * 清空表.
     */
    public void deleteWLAll();

	public void deleteByTenant(ScoRset entity);
}
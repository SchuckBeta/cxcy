package com.oseasy.scr.modules.scr.dao;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.scr.modules.scr.entity.ScoRule;
import com.oseasy.scr.modules.scr.entity.ScoRulePb;

import org.apache.ibatis.annotations.Param;

/**
 * 学分规则配比DAO接口.
 * @author chenh
 * @version 2018-12-26
 */
@MyBatisDao
public interface ScoRulePbDao extends CrudDao<ScoRulePb> {
    /**
     * 批量新增.
     * @param entitys
     */
    public void insertPL(@Param("entitys") List<ScoRulePb> entitys);

    /**
     * 批量修改.
     * @param entitys
     */
    public void updatePL(@Param("entitys") List<ScoRulePb> entitys);


  	/**
   	 * 物理删除.
   	 * @param entity
   	 */
  	public void deleteWL(ScoRulePb entity);
	public void deleteByRid(ScoRulePb entity);
   	/**
   	 * 批量状态删除.
   	 * @param entity
   	 */
  	public void deletePL(ScoRulePb entity);

    /**
   	 * 批量物理删除.
   	 * @param entity
   	 */
  	public void deleteWLPL(ScoRulePb entity);

    /**
     * 清空表.
     */
    public void deleteWLAll();
}
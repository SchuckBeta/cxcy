package com.oseasy.scr.modules.scr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.scr.modules.scr.entity.ScoRuleDetail;

/**
 * 学分规则详情DAO接口.
 * @author chenh
 * @version 2018-12-21
 */
@MyBatisDao
public interface ScoRuleDetailDao extends CrudDao<ScoRuleDetail> {
    /**
     * 批量新增.
     * @param entitys
     */
    public void insertPL(@Param("entitys") List<ScoRuleDetail> entitys);

    /**
     * 批量修改.
     * @param entitys
     */
    public void updatePL(@Param("entitys") List<ScoRuleDetail> entitys);


  	/**
   	 * 物理删除.
   	 * @param entity
   	 */
  	public void deleteWL(ScoRuleDetail entity);

	public void updateSort(ScoRuleDetail entity);

   	/**
   	 * 批量状态删除.
   	 * @param entity
   	 */
  	public void deletePL(ScoRuleDetail entity);

    /**
   	 * 批量物理删除.
   	 * @param entity
   	 */
  	public void deleteWLPL(ScoRuleDetail entity);

    /**
     * 清空表.
     */
    public void deleteWLAll();
	public void deleteByRid(ScoRuleDetail entity);
	public void updateMaxOrSumByRid(ScoRuleDetail entity);
	public List<ScoRuleDetail> ajaxValiScrRuleDetailName(ScoRuleDetail entity);


}
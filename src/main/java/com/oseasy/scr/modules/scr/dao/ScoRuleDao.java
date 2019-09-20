package com.oseasy.scr.modules.scr.dao;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.TreeDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.scr.modules.scr.entity.ScoRule;
import com.oseasy.scr.modules.scr.entity.ScoRuleDetailMould;

/**
 * 学分规则DAO接口.
 * @author chenh
 * @version 2018-12-21
 */
@MyBatisDao
public interface ScoRuleDao  extends TreeDao<ScoRule> {
    /**
     * 批量新增.
     * @param entitys
     */
    public void insertPL(@Param("entitys") List<ScoRule> entitys);

    /**
     * 批量修改.
     * @param entitys
     */
    public void updatePL(@Param("entitys") List<ScoRule> entitys);


  	/**
   	 * 物理删除.
   	 * @param entity
   	 */
  	public void deleteWL(ScoRule entity);

   	/**
   	 * 批量状态删除.
   	 * @param entity
   	 */
  	public void deletePL(ScoRule entity);

	public void deleteByTenant(ScoRule entity);
	public void deleteScoRuleDetailMouldByScoRule(ScoRuleDetailMould entity);
    /**
   	 * 批量物理删除.
   	 * @param entity
   	 */
  	public void deleteWLPL(ScoRule entity);
	public void updateSort(ScoRule entity);
  	//添加规则标准模板
	public void insertScoRuleDetailMould(ScoRuleDetailMould entity);
	//添加规则标准模板
	public void updateScoRuleDetailMould(ScoRuleDetailMould entity);
    /**
     * 清空表.
     */
    public void deleteWLAll();
	public void deleteByParentIds(ScoRule entity);
	@FindListByTenant
    public List<ScoRule> scoRuleSingleList(ScoRule entity);
	@FindListByTenant
	public List<ScoRule> ajaxValiScrRuleName(ScoRule entity);
	public List<ScoRule> findByParentIds(ScoRule entity);

	@Override
	@InsertByTenant
	public int insert(ScoRule entity);

	public ScoRule findScoRule(ScoRule entity);

	public ScoRule findScoRuleSingleDetail(ScoRule entity);
}
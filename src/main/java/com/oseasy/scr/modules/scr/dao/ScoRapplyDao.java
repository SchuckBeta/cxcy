package com.oseasy.scr.modules.scr.dao;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.scr.modules.scr.entity.ScoRapply;
import com.oseasy.scr.modules.scr.entity.ScoRapplyValid;
import com.oseasy.scr.modules.scr.entity.ScoRule;
import com.oseasy.scr.modules.scr.vo.ScoQuery;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.com.pcore.common.persistence.annotation.PageNcount;

/**
 * 学分申请DAO接口.
 * @author chenh
 * @version 2018-12-21
 */
@MyBatisDao
public interface ScoRapplyDao extends CrudDao<ScoRapply> {
    /**
     * 批量新增.
     * @param entitys
     */
    public void insertPL(@Param("entitys") List<ScoRapply> entitys);

    /**
     * 批量修改.
     * @param entitys
     */
    public void updatePL(@Param("entitys") List<ScoRapply> entitys);


  	/**
   	 * 物理删除.
   	 * @param entity
   	 */
  	public void deleteWL(ScoRapply entity);

   	/**
   	 * 批量状态删除.
   	 * @param entity
   	 */
  	public void deletePL(ScoRapply entity);

    /**
   	 * 批量物理删除.
   	 * @param entity
   	 */
  	public void deleteWLPL(ScoRapply entity);

    /**
     * 清空表.
     */
    public void deleteWLAll();

    public List<ScoRapply> findListByg(ScoRapply entity);
	public List<ScoQuery> findCreditList(ScoQuery entity);
	public ScoRapply ajaxFindScoDetail(ScoRapply entity);
	//课程学分详情
	public ScoRapply ajaxFindCourseDetail(ScoRapply entity);
	//学分查询（后台）
	@FindListByTenant
	public List<ScoQuery> findScoCreditQueryList(ScoQuery entity);

    public List<ScoRapply> findProjects(ScoRapply entity);

    public List<ScoRapply> findListBygIds(ScoRapply apply);
    //@PageNcount
	//public List<ScoRapply> findListBygIdsNew(ScoRapply apply);
	//@PageNcount
	//public List<ScoRapply> findListBygIdsNews(ScoRapply apply);
    //判断当前选中的类别下有没有标准被认定
	public List<ScoRapply> scoRuleInRapply(ScoRule entity);

	List<ScoRapply> findScoProjects(String userId);
	@FindListByTenant
	List<ScoRapplyValid> findDetailRapplyCertList(ScoRapplyValid scoRapplyValid);
	@FindListByTenant
	List<ScoRapplyValid> findDetailNameList(ScoRapplyValid scoRapplyValid);
	@FindListByTenant
	List<ScoRapply> findRuleDetailIsApply(ScoRule scoRule);
    /**
     * 检查项目名是否存在.
     * @param rapply 申请
     */
    public Integer checkHasProject(ScoRapply rapply);
}
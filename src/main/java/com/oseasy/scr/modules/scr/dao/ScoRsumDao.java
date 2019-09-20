package com.oseasy.scr.modules.scr.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.scr.modules.scr.entity.ScoCreditValue;
import com.oseasy.scr.modules.scr.entity.ScoRapplyValid;
import com.oseasy.scr.modules.scr.entity.ScoRsum;

/**
 * 学分汇总DAO接口.
 * @author chenh
 * @version 2018-12-21
 */
@MyBatisDao
public interface ScoRsumDao extends CrudDao<ScoRsum> {
    /**
     * 批量新增.
     * @param entitys
     */
    public void insertPL(@Param("entitys") List<ScoRsum> entitys);

    /**
     * 批量修改.
     * @param entitys
     */
    public void updatePL(@Param("entitys") List<ScoRsum> entitys);


	public void updateVal(ScoRsum entity);
  	/**
   	 * 物理删除.
   	 * @param entity
   	 */
  	public void deleteWL(ScoRsum entity);
  	public void deleteWLByAppId(ScoRsum entity);

   	/**
   	 * 批量状态删除.
   	 * @param entity
   	 */
  	public void deletePL(ScoRsum entity);

    /**
   	 * 批量物理删除.
   	 * @param entity
   	 */
  	public void deleteWLPL(ScoRsum entity);

    /**
     * 清空表.
     */
    public void deleteWLAll();

    //学分查询-修改学分的列表
	public List<ScoRsum> updateValList(ScoRsum entity);

	//学分查询-修改学分的列表
	public List<ScoRsum> findListByAppId(ScoRsum entity);

	//学分查询-修改学分的列表
	public List<ScoRsum> findListByAppIds(ScoRsum entity);



	public List<ScoCreditValue> findScoRsumCourseList(ScoCreditValue entity);

	public List<ScoCreditValue> findScoRsumCreditList(ScoCreditValue entity);

	/**
	 * 逻辑删除
	 * @param entity
	 */
	public void deleteByAppId(ScoRsum entity);

	public List<Map<String,Double>> findScoSumList(ScoRsum scoRsum);

	public ScoRsum findRdetailSum(ScoRsum scoRsum);
	public ScoRsum findRdetailPersonalSum(ScoRapplyValid scoRsum);

}
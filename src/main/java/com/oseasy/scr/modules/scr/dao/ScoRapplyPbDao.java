package com.oseasy.scr.modules.scr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.scr.modules.scr.entity.ScoRapplyPb;
import com.oseasy.scr.modules.scr.entity.ScoRsum;

/**
 * 学分申请配比DAO接口.
 * @author chenh
 * @version 2018-12-26
 */
@MyBatisDao
public interface ScoRapplyPbDao extends CrudDao<ScoRapplyPb> {
    /**
     * 批量新增.
     * @param entitys
     */
    public void insertPL(@Param("entitys") List<ScoRapplyPb> entitys);

    /**
     * 批量修改.
     * @param entitys
     */
    public void updatePL(@Param("entitys") List<ScoRapplyPb> entitys);


  	/**
   	 * 物理删除.
   	 * @param entity
   	 */
  	public void deleteWL(ScoRapplyPb entity);

   	/**
   	 * 批量状态删除.
   	 * @param entity
   	 */
  	public void deletePL(ScoRapplyPb entity);

    /**
   	 * 批量物理删除.
   	 * @param entity
   	 */
  	public void deleteWLPL(ScoRapplyPb entity);

    /**
     * 清空表.
     */
    public void deleteWLAll();

	public void deleteWLByApplyId(ScoRapplyPb entity);

	public void updateVal(ScoRsum entity);
}
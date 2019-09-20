package com.oseasy.scr.modules.scr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.scr.modules.scr.entity.ScoRapplyRecord;

/**
 * 学分申请记录DAO接口.
 * @author chenh
 * @version 2018-12-21
 */
@MyBatisDao
public interface ScoRapplyRecordDao extends CrudDao<ScoRapplyRecord> {
    /**
     * 批量新增.
     * @param entitys
     */
    public void insertPL(@Param("entitys") List<ScoRapplyRecord> entitys);

    /**
     * 批量修改.
     * @param entitys
     */
    public void updatePL(@Param("entitys") List<ScoRapplyRecord> entitys);


  	/**
   	 * 物理删除.
   	 * @param entity
   	 */
  	public void deleteWL(ScoRapplyRecord entity);

   	/**
   	 * 批量状态删除.
   	 * @param entity
   	 */
  	public void deletePL(ScoRapplyRecord entity);

    /**
   	 * 批量物理删除.
   	 * @param entity
   	 */
  	public void deleteWLPL(ScoRapplyRecord entity);

    /**
     * 清空表.
     */
    public void deleteWLAll();

	/**
	 * 查找课程学分审核记录.
	 */
    public List<ScoRapplyRecord> findCourseAuditList(ScoRapplyRecord entity);
}
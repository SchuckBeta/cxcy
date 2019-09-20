package com.oseasy.pw.modules.pw.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pw.modules.pw.entity.PwApplyRecord;

/**
 * pwApplyRecordDAO接口.
 * @author zy
 * @version 2018-11-20
 */
@MyBatisDao
public interface PwApplyRecordDao extends CrudDao<PwApplyRecord> {
    /**
     * 批量新增.
     * @param entitys
     */
    public void insertPL(@Param("entitys") List<PwApplyRecord> entitys);

    /**
     * 批量修改.
     * @param entitys
     */
    public void updatePL(@Param("entitys") List<PwApplyRecord> entitys);


  	/**
   	 * 物理删除.
   	 * @param entity
   	 */
  	public void deleteWL(PwApplyRecord entity);

   	/**
   	 * 批量状态删除.
   	 * @param entity
   	 */
  	public void deletePL(PwApplyRecord entity);

    /**
   	 * 批量物理删除.
   	 * @param entity
   	 */
  	public void deleteWLPL(PwApplyRecord entity);
  	/**
  	 * 根据入驻ID更新记录为失败状态 2.
  	 * @param id
  	 */
  	public void updateFailByEid(@Param("id") String id);

    /**
     * 清空表.
     */
    public void deleteWLAll();

	List<PwApplyRecord> getListByPwApplyRecord(PwApplyRecord pwApplyRecord);

	List<PwApplyRecord> getAuditList(PwApplyRecord pwApplyRecord);

	PwApplyRecord getLastByEid(String eid);
	/**
	 * 根据类型统计待审核的记录数.
	 * @param type
	 * @return
	 */
	Long findCountByType(@Param("type") String type);

	PwApplyRecord getLastAuditByEid(String eid);

	List<PwApplyRecord> getFrontAuditList(PwApplyRecord pwApplyRecord);

	List<PwApplyRecord> getBackAuditList(PwApplyRecord pwApplyRecord);

	PwApplyRecord getChangeAppByEid(String eid);
}
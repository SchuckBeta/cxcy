package com.oseasy.pw.modules.pw.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pw.modules.pw.entity.PwEnterRoomRecord;

/**
 * 场地分配记录DAO接口.
 * @author chenh
 * @version 2018-12-10
 */
@MyBatisDao
public interface PwEnterRoomRecordDao extends CrudDao<PwEnterRoomRecord> {
    /**
     * 批量新增.
     * @param entitys
     */
    public void insertPL(@Param("entitys") List<PwEnterRoomRecord> entitys);

    /**
     * 批量修改.
     * @param entitys
     */
    public void updatePL(@Param("entitys") List<PwEnterRoomRecord> entitys);


  	/**
   	 * 物理删除.
   	 * @param entity
   	 */
  	public void deleteWL(PwEnterRoomRecord entity);

   	/**
   	 * 批量状态删除.
   	 * @param entity
   	 */
  	public void deletePL(PwEnterRoomRecord entity);

    /**
   	 * 批量物理删除.
   	 * @param entity
   	 */
  	public void deleteWLPL(PwEnterRoomRecord entity);

    /**
     * 清空表.
     */
    public void deleteWLAll();
}
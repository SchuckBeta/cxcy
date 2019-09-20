package com.oseasy.sys.modules.seq.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.sys.modules.seq.entity.Sequence;

/**
 * 序列表DAO接口.
 * @author zy
 * @version 2018-10-08
 */
@MyBatisDao
public interface SequenceDao extends CrudDao<Sequence> {
    /**
     * 批量新增.
     * @param entitys
     */
    public void insertPL(@Param("entitys") List<Sequence> entitys);

    /**
     * 批量修改.
     * @param entitys
     */
    public void updatePL(@Param("entitys") List<Sequence> entitys);


  	/**
   	 * 物理删除.
   	 * @param entity
   	 */
  	public void deleteWL(Sequence entity);

   	/**
   	 * 批量状态删除.
   	 * @param entity
   	 */
  	public void deletePL(Sequence entity);

    /**
   	 * 批量物理删除.
   	 * @param entity
   	 */
  	public void deleteWLPL(Sequence entity);

    /**
     * 清空表.
     */
    public void deleteWLAll();

	String nextSequence(String name);
}
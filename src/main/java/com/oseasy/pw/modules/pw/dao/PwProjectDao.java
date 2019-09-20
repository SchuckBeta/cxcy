package com.oseasy.pw.modules.pw.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pw.modules.pw.entity.PwProject;

/**
 * pwDAO接口.
 * @author zy
 * @version 2018-11-20
 */
@MyBatisDao
public interface PwProjectDao extends CrudDao<PwProject> {
    /**
     * 批量新增.
     * @param entitys
     */
    public void insertPL(@Param("entitys") List<PwProject> entitys);

    /**
     * 批量修改.
     * @param entitys
     */
    public void updatePL(@Param("entitys") List<PwProject> entitys);


  	/**
   	 * 物理删除.
   	 * @param entity
   	 */
  	public void deleteWL(PwProject entity);

   	/**
   	 * 批量状态删除.
   	 * @param entity
   	 */
  	public void deletePL(PwProject entity);

    /**
   	 * 批量物理删除.
   	 * @param entity
   	 */
  	public void deleteWLPL(PwProject entity);

    /**
     * 清空表.
     */
    public void deleteWLAll();

	List<PwProject> getPwProjectListByEid(String eid);

	List<PwProject> findListByPwProject(PwProject pwProject);
}
package com.oseasy.pie.modules.iep.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.TreeDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pie.modules.iep.entity.IepTpl;

/**
 * 模板导入导出DAO接口.
 * @author chenhao
 * @version 2019-02-14
 */
@MyBatisDao
public interface IepTplDao extends TreeDao<IepTpl> {
    /**
     * 找到所有子节点
     * @param entity
     * @return
     */
    public List<IepTpl> findTreeById(IepTpl entity);

    /**
     * 批量新增.
     * @param entitys
     */
    public void insertPL(@Param("entitys") List<IepTpl> entitys);

    /**
     * 批量修改.
     * @param entitys
     */
    public void updatePL(@Param("entitys") List<IepTpl> entitys);


  	/**
   	 * 物理删除.
   	 * @param entity
   	 */
  	public void deleteWL(IepTpl entity);

   	/**
   	 * 批量状态删除.
   	 * @param entity
   	 */
  	public void deletePL(IepTpl entity);

    /**
   	 * 批量物理删除.
   	 * @param entity
   	 */
  	public void deleteWLPL(IepTpl entity);

    /**
     * 清空表.
     */
    public void deleteWLAll();

    /**
     * 获取当前组的Step列表.
     * @param entity
     * @return List
     */
    public List<IepTpl> findListBySteps(IepTpl entity);
}
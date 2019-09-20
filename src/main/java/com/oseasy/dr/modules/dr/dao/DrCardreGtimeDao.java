package com.oseasy.dr.modules.dr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.dr.modules.dr.entity.DrCardreGtime;

/**
 * 卡记录规则时间DAO接口.
 * @author chenh
 * @version 2018-05-16
 */
@MyBatisDao
public interface DrCardreGtimeDao extends CrudDao<DrCardreGtime> {
    /**
     * 批量更新数据
     * @param entity
     * @return
     */
    public int savePl(@Param("list") List<DrCardreGtime> entitys);

  /**
   * 批量物理删除.
   * @param entity
   */
  public void deleteWLPLByGid(@Param("gid")String gid);

  /**
   * 批量物理删除.
   * @param entity
   */
  public void deleteWLPLByIds(@Param("ids") List<String> ids);

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(DrCardreGtime drCardreGtime);
}
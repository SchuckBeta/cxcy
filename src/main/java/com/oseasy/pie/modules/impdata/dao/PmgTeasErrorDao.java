package com.oseasy.pie.modules.impdata.dao;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pie.modules.impdata.entity.PmgTeasError;

/**
 * 自定义大赛导师信息错误数据DAO接口.
 * @author 自定义大赛导师信息错误数据
 * @version 2018-05-14
 */
@MyBatisDao
public interface PmgTeasErrorDao extends CrudDao<PmgTeasError> {

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(PmgTeasError pmgTeasError);
  public List<PmgTeasError> getListByImpId(String impid);
  public void deleteByImpId(String impid);
}
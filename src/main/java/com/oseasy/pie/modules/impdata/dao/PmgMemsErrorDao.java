package com.oseasy.pie.modules.impdata.dao;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pie.modules.impdata.entity.PmgMemsError;

/**
 * 自定义大赛成员信息错误数据DAO接口.
 * @author 自定义大赛成员信息错误数据
 * @version 2018-05-14
 */
@MyBatisDao
public interface PmgMemsErrorDao extends CrudDao<PmgMemsError> {

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(PmgMemsError pmgMemsError);
  public List<PmgMemsError> getListByImpId(String impid);
  public void deleteByImpId(String impid);
}
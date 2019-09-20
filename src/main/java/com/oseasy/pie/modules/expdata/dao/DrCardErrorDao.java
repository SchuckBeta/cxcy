package com.oseasy.pie.modules.expdata.dao;

import java.util.List;
import java.util.Map;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pie.modules.impdata.vo.DrCardError;

/**
 * 卡导入错误信息DAO接口.
 * @author chenh
 * @version 2018-07-19
 */
@MyBatisDao
public interface DrCardErrorDao extends CrudDao<DrCardError> {

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(DrCardError drCardError);
  /**
   * 根据ImpId物理删除.
   * @param entity
   */
  public void deleteWLByImpId(DrCardError drCardError);

  public List<Map<String, String>> findListByImpId(String impId);
}
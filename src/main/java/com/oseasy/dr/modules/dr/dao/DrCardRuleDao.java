package com.oseasy.dr.modules.dr.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.dr.modules.dr.entity.DrCardRule;

/**
 * 门禁预警DAO接口.
 * @author zy
 * @version 2018-04-13
 */
@MyBatisDao
public interface DrCardRuleDao extends CrudDao<DrCardRule> {

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(DrCardRule drCardRule);

  DrCardRule getDrCardRule();
}
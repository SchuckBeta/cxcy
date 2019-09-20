package com.oseasy.pw.modules.pw.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pw.modules.pw.entity.PwRenewalRule;

/**
 * 续期配置DAO接口.
 * @author zy
 * @version 2018-01-04
 */
@MyBatisDao
public interface PwRenewalRuleDao extends CrudDao<PwRenewalRule> {

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(PwRenewalRule pwRenewalRule);

  PwRenewalRule getPwRenewalRule();
}
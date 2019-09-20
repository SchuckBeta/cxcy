package com.oseasy.com.pcore.modules.sys.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.com.pcore.modules.sys.entity.SysProp;

/**
 * 系统功能DAO接口.
 * @author chenh
 * @version 2018-03-30
 */
@MyBatisDao
public interface SysPropDao extends CrudDao<SysProp> {

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(SysProp sysProp);
}
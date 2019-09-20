package com.oseasy.com.pcore.modules.sys.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.com.pcore.modules.sys.entity.SysPropItem;

/**
 * 系统功能配置项DAO接口.
 * @author chenh
 * @version 2018-03-30
 */
@MyBatisDao
public interface SysPropItemDao extends CrudDao<SysPropItem> {

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(SysPropItem sysPropItem);
}
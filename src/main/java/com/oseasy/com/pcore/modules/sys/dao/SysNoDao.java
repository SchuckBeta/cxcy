package com.oseasy.com.pcore.modules.sys.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.com.pcore.modules.sys.entity.SysNo;

/**
 * 系统全局编号DAO接口.
 * @author chenhao
 * @version 2017-07-17
 */
@MyBatisDao
public interface SysNoDao extends CrudDao<SysNo> {
  /**
   * 根据唯一标识获取编号对象.
   * @param key 唯一标识
   * @return SysNo
   */
  public SysNo getByKeyss(String key);
}
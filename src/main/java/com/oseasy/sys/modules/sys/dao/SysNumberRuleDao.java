package com.oseasy.sys.modules.sys.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.sys.modules.sys.entity.SysNumberRule;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 编号规则管理DAO接口.
 * @author 李志超
 * @version 2018-05-17
 */
@MyBatisDao
public interface SysNumberRuleDao extends CrudDao<SysNumberRule> {
  @Override
  @FindListByTenant
  public List<SysNumberRule> findList(SysNumberRule entity);
  @Override
  @InsertByTenant
  public int insert(SysNumberRule entity);
  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(SysNumberRule sysNumberRule);

  SysNumberRule getRuleByAppType(@Param("appType") String appType, @Param("id") String id);
}
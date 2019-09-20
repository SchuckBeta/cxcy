package com.oseasy.act.modules.actyw.dao;

import com.oseasy.act.modules.actyw.entity.ActYwSgtype;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 状态条件DAO接口.
 * @author zy
 * @version 2018-02-01
 */
@MyBatisDao
public interface ActYwSgtypeDao extends CrudDao<ActYwSgtype> {

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(ActYwSgtype actYwSgtype);

  List<ActYwSgtype> checkName(ActYwSgtype actYwSgtype);

  List<ActYwSgtype> getInuseActYwSgtype(@Param("id") String id);
}
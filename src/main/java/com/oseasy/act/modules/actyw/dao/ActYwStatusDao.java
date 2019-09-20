package com.oseasy.act.modules.actyw.dao;

import com.oseasy.act.modules.actyw.entity.ActYwStatus;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 节点状态表DAO接口.
 * @author zy
 * @version 2018-01-15
 */
@MyBatisDao
public interface ActYwStatusDao extends CrudDao<ActYwStatus> {

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(ActYwStatus actYwStatus);

  List<ActYwStatus> getAllStateByGnodeId(@Param("gnodeId") String gnodeId);

  List<ActYwStatus> checkState(ActYwStatus actYwStatus);

  List<ActYwStatus> getInuseStateById(@Param("statusId") String statusId,@Param("gnodeId") String gnodeId);

  List<ActYwStatus> getInuseState(@Param("statusId") String statusId);

  void deleteWLBySgtypeId(@Param("gtypeId")String gtypeId);
}
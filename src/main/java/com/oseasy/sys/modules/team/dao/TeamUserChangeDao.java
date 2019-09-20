package com.oseasy.sys.modules.team.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.sys.modules.team.entity.TeamUserChange;

import java.util.List;

/**
 * 团队人员变更表DAO接口.
 * @author 团队人员变更表
 * @version 2018-07-19
 */
@MyBatisDao
public interface TeamUserChangeDao extends CrudDao<TeamUserChange> {

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(TeamUserChange teamUserChange);

  void savePl(List<TeamUserChange> list);
}
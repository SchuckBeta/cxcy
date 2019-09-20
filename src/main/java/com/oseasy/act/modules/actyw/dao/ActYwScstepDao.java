package com.oseasy.act.modules.actyw.dao;

import com.oseasy.act.modules.actyw.entity.ActYwScstep;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 节点状态中间表DAO接口.
 * @author zy
 * @version 2018-01-15
 */
@MyBatisDao
public interface ActYwScstepDao extends CrudDao<ActYwScstep> {

  /**
   * 物理删除.
   * @param actYwScstep
   */
  public void deleteWL(ActYwScstep actYwScstep);

  public void saveAll(List<ActYwScstep> actYwScstepList);
  /**
   * 批量保存.
   * @param list 列表
   * @return
   */
  public int savePl(@Param("list") List<ActYwScstep> list);

  List<ActYwScstep> getStepList(ActYwScstep actYwScstep);
}
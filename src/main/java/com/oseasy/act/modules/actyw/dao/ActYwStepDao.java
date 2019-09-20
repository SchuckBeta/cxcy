package com.oseasy.act.modules.actyw.dao;

import com.oseasy.act.modules.actyw.entity.ActYwStep;
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
public interface ActYwStepDao extends CrudDao<ActYwStep> {

  /**
   * 物理删除.
   * @param actYwStep
   */
  public void deleteWL(ActYwStep actYwStep);

  public void saveAll(List<ActYwStep> actYwStepList);
  /**
   * 批量保存.
   * @param list 列表
   * @return
   */
  public int savePl(@Param("list") List<ActYwStep> list);

  List<ActYwStep> getStepList(ActYwStep actYwStep);

  ActYwStep getActYwStepByGroupId(@Param("groupId")String groupId);

  void updateStep(ActYwStep actYwStep);
}
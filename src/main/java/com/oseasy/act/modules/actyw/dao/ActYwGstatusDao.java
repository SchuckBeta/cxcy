package com.oseasy.act.modules.actyw.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oseasy.act.modules.actyw.entity.ActYwGstatus;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

/**
 * 节点状态中间表DAO接口.
 * @author zy
 * @version 2018-01-15
 */
@MyBatisDao
public interface ActYwGstatusDao extends CrudDao<ActYwGstatus> {

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(ActYwGstatus actYwGstatus);


  public void saveAll(List<ActYwGstatus> actYwGstatusList);

  void deleteByGroupIdAndGnodeId(@Param("groupId")String groupId,@Param("gnodeId") String gnodeId);

  /**
   * 批量保存.
   * @param list 列表
   * @return
   */
  public int savePl(@Param("list") List<ActYwGstatus> list);

  /**
   * 根据groupId批量物理删除.
   * @param groupId 流程ID
   */
  public void deletePlwlByGroup(@Param("groupId") String groupId);
  /**
   * 根据groupId和gnodeId批量物理删除.
   * @param groupId 流程ID
   * @param gnodeId 节点ID
   */
  public void deletePlwl(@Param("groupId") String groupId, @Param("gnodeId") String gnodeId);
}
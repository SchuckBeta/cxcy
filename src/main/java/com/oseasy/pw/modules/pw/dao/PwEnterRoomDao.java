package com.oseasy.pw.modules.pw.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oseasy.act.modules.actyw.entity.ActYwGassign;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pw.modules.pw.entity.PwEnterRoom;

/**
 * 入驻场地分配DAO接口.
 * @author chenh
 * @version 2017-11-26
 */
@MyBatisDao
public interface PwEnterRoomDao extends CrudDao<PwEnterRoom> {
    /**
     * 批量插入.
     * @author chenhao
     * @param entitys 参数
     */
    public void insertPl(@Param("list")List<PwEnterRoom> entitys);

  /**
   * 物理删除.
   * @author chenhao
   * @param pwEnterRoom 参数
   */
  public void deleteWL(PwEnterRoom pwEnterRoom);

  /**
   * 批量物理删除.
   * @author chenhao
   * @param ids ID列表
   */
  public void deletePLWL(@Param("ids")List<String> ids);
  /**
   * 批量物理删除.
   * @author chenhao
   * @param ids ID列表
   */
  public void deletePLWLByErids(@Param("eid")String eid, @Param("ids")List<String> ids);

  /**
   * 根据Eid和Rid批量物理删除.
   * @author chenhao
   * @param pwEnterRoom 参数
   */
  public void deletePLWLByErid(PwEnterRoom pwEnterRoom);

  /**
   * 根据Eid批量物理删除.
   * @author chenhao
   * @param pwEnterRoom 参数
   */
  public void deletePLWLByEid(PwEnterRoom pwEnterRoom);

  /**
   * 根据Rid批量物理删除.
   * @author chenhao
   * @param pwEnterRoom 参数
   */
  public void deletePLWLByRid(PwEnterRoom pwEnterRoom);

  /**
   * @author chenhao
   * @param ids ID列表
   * @return List
   */
  public List<PwEnterRoom> findListByinIds(@Param("ids") List<String> ids);
}
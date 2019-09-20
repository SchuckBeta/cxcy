package com.oseasy.pro.modules.promodel.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.promodel.entity.ProModelMdGcHistory;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 互联网+大赛记录表DAO接口.
 * @author zy
 * @version 2018-06-05
 */
@MyBatisDao
public interface ProModelMdGcHistoryDao extends CrudDao<ProModelMdGcHistory> {

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(ProModelMdGcHistory proModelMdGcHistory);

  ProModelMdGcHistory getProModelMdGcHistory(ProModelMdGcHistory proModelMdGcHistory);

  List<ProModelMdGcHistory> getProModelMdGcHistoryList(ProModelMdGcHistory proModelMdGcHistory);

  void updateAward(@Param("id")String id, @Param("result")String result);
}
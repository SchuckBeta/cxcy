package com.oseasy.act.modules.actyw.dao;

import com.oseasy.act.modules.actyw.entity.ActYwYear;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 流程年份表DAO接口.
 * @author zy
 * @version 2018-03-21
 */
@MyBatisDao
public interface ActYwYearDao extends CrudDao<ActYwYear> {

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(ActYwYear actYwYear);

  ActYwYear getByActywIdAndYear(@Param("actywId")String actywId, @Param("year")String year);

  List<ActYwYear> findListByActywId(@Param("actywId")String actywId);

  List<ActYwYear> findOverYear(ActYwYear actYwYear);

  int checkYearTimeByActYw(@Param("actywId")String actywId);

  int checkYearByActYw(@Param("actywId")String actywId);
}
package com.oseasy.pie.modules.impdata.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pie.modules.impdata.entity.ProModelError;
import com.oseasy.pie.modules.impdata.entity.ProModelGcontestError;

/**
 * 自定义大赛信息错误数据DAO接口.
 * @author 自定义大赛信息错误数据
 * @version 2018-05-14
 */
@MyBatisDao
public interface ProModelGcontestErrorDao extends CrudDao<ProModelGcontestError> {

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(ProModelGcontestError proModelGcontestError);
  public List<Map<String, String>> getMapByImpId(String impid);
  public List<ProModelGcontestError> getListByImpId(String impid);
  public void deleteByImpId(String impid);

  public void updatePL(@Param("entitys") List<ProModelGcontestError> entitys);
}
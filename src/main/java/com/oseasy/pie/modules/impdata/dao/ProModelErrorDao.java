package com.oseasy.pie.modules.impdata.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pie.modules.impdata.entity.ProModelError;

/**
 * 导入项目错误数据表DAO接口.
 * @author 奔波儿灞
 * @version 2018-03-13
 */
@MyBatisDao
public interface ProModelErrorDao extends CrudDao<ProModelError> {

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(ProModelError proModelError);
  public List<Map<String,String>> getListByImpId(String impid);
  public void deleteByImpId(String impid);

  public void updatePL(@Param("entitys") List<? extends ProModelError> entitys);
}
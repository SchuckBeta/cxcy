package com.oseasy.pro.modules.workflow.dao;

import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.workflow.WorkFlowDao;
import com.oseasy.pro.modules.workflow.entity.ProModelMdGc;
import com.oseasy.pro.modules.workflow.entity.ProvinceProModelGc;
import com.oseasy.pro.modules.workflow.vo.ExpProModelVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 互联网+大赛模板DAO接口.
 * @author zy
 * @version 2018-06-05
 */
@MyBatisDao
public interface ProvinceProModelGcDao extends WorkFlowDao<ProvinceProModelGc, ExpProModelVo> {
  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(ProvinceProModelGc proModelMdGc);

  List<ProvinceProModelGc> findListByIds(ProvinceProModelGc proModelMdGc);

  List<ProvinceProModelGc> findListByView(ProvinceProModelGc proModelMdGc);

  ProvinceProModelGc getByProModelId(@Param("proModelId") String proModelId);

  List<ExpProModelVo> export(ProvinceProModelGc proModelMdGc);
}
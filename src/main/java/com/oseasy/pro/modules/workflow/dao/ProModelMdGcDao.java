package com.oseasy.pro.modules.workflow.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.workflow.WorkFlowDao;
import com.oseasy.pro.modules.workflow.entity.ProModelMdGc;
import com.oseasy.pro.modules.workflow.vo.ExpProModelVo;


/**
 * 互联网+大赛模板DAO接口.
 * @author zy
 * @version 2018-06-05
 */
@MyBatisDao
public interface ProModelMdGcDao extends WorkFlowDao<ProModelMdGc, ExpProModelVo> {
  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(ProModelMdGc proModelMdGc);

  List<ProModelMdGc> findListByIds(ProModelMdGc proModelMdGc);

  List<ProModelMdGc> findListByView(ProModelMdGc proModelMdGc);

  ProModelMdGc getByProModelId(@Param("proModelId")String proModelId);

  List<ExpProModelVo> export(ProModelMdGc proModelMdGc);
}
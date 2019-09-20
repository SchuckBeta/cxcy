package com.oseasy.pro.modules.workflow.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.workflow.WorkFlowDao;
import com.oseasy.pro.modules.workflow.entity.ProModelTlxy;
import com.oseasy.pro.modules.workflow.vo.ExpProModelTlxyVo;


/**
 * 互联网+大赛模板DAO接口.
 * @author zy
 * @version 2018-06-05
 */
@MyBatisDao
public interface ProModelTlxyDao extends WorkFlowDao<ProModelTlxy, ExpProModelTlxyVo> {
  /**
   * 物理删除.
   */
  public void deleteWL(ProModelTlxy proModelMdGc);

  List<ProModelTlxy> findPmByIds(ProModelTlxy proModel);
  List<ProModelTlxy> findListByIds(ProModelTlxy proModelMdGc);
  public void updatePnum(@Param("pnum") String pnum, @Param("id") String id);
  ProModelTlxy getByProModelId(@Param("proModelId") String proModelId);

  List<ExpProModelTlxyVo> export(ProModelTlxy proModelTlxy);

  void updateGcompetitionnumber(@Param("proModelId")String proModelId, @Param("num")String num);

  void updatePcompetitionnumber(@Param("proModelId")String proModelId, @Param("num")String num);

  int checkGcompetitionnumber(@Param("num")String num);

  int checkPcompetitionnumber(@Param("num")String num);

  void updateXcompetitionnumber(@Param("proModelId")String proModelId,@Param("num") String num);
}
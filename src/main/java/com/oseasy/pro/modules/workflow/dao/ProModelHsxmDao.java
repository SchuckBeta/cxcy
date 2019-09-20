package com.oseasy.pro.modules.workflow.dao;

import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.workflow.WorkFlowDao;
import com.oseasy.pro.modules.workflow.entity.ProModelHsxm;
import com.oseasy.pro.modules.workflow.vo.ExpProModelVo;

import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 互联网+大赛模板DAO接口.
 * @author zy
 * @version 2018-06-05
 */
@MyBatisDao
public interface ProModelHsxmDao extends WorkFlowDao<ProModelHsxm, ExpProModelVo> {
  /**
   * 物理删除.
   * @param proModelHsxm
   */
  public void deleteWL(ProModelHsxm proModelHsxm);

  List<ProModelHsxm> findListByIds(ProModelHsxm proModelHsxm);

  List<ProModelHsxm> findListByIdsOfSt(ProModelHsxm proModelHsxm);

  List<ProModelHsxm> findListByIdsOfBefore(ProModelHsxm proModelHsxm);

  List<ProModelHsxm> findListByIdsOfProfessor(ProModelHsxm proModelHsxm);

  ProModelHsxm getByProModelId(@Param("proModelId") String proModelId);

  List<ExpProModelVo> export(ProModelHsxm proModelHsxm);

  List<ProModelHsxm> findPmByIds(ProModelHsxm proModelHsxm);
}
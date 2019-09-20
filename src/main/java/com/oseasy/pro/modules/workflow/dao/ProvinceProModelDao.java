package com.oseasy.pro.modules.workflow.dao;

import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.workflow.WorkFlowDao;
import com.oseasy.pro.modules.workflow.entity.ProModelHsxm;
import com.oseasy.pro.modules.workflow.entity.ProvinceProModel;
import com.oseasy.pro.modules.workflow.vo.ExpProModelVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 互联网+大赛模板DAO接口.
 * @author zy
 * @version 2018-06-05
 */
@MyBatisDao
public interface ProvinceProModelDao extends WorkFlowDao<ProvinceProModel, ExpProModelVo> {
  /**
   * 物理删除.
   * @param provinceProModel
   */
  public void deleteWL(ProvinceProModel provinceProModel);

  @Override
  List<ProvinceProModel> findListByIds(ProvinceProModel provinceProModel);

  /**
   * 省后台列表查询
   * @param provinceProModel provinceProModel
   * @return
   */
  List<ProvinceProModel> findListByView(ProvinceProModel provinceProModel);

  /**
   * 省后台节点列表查询
   * @param provinceProModel provinceProModel
   * @return
   */
  List<ProvinceProModel> findListByData(ProvinceProModel provinceProModel);

  /**
   * 视图角色
   * @param provinceProModel
   * @return
   */
  List<ProvinceProModel> findListByDataOfSt(ProvinceProModel provinceProModel);

  ProvinceProModel getByProModelId(@Param("proModelId") String proModelId);

  ProvinceProModel getByProvinceProModelId(@Param("id") String id);

  List<ExpProModelVo> export(ProvinceProModel provinceProModel);

  List<ProvinceProModel> findPmByIds(ProvinceProModel provinceProModel);

  List<ProvinceProModel> findProvListByIdsAssign(ProvinceProModel provinceProModel);

  ProvinceProModel getByProInsId(String proInsId);

  List<String> findListByIdsWithoutJoin(ProvinceProModel provinceProModel);

}
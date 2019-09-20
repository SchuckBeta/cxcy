package com.oseasy.dr.modules.dr.dao;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.dr.modules.dr.entity.DrEquipment;

import java.util.List;

/**
 * 门禁设备DAO接口.
 * @author chenh
 * @version 2018-03-30
 */
@MyBatisDao
public interface DrEquipmentDao extends CrudDao<DrEquipment> {

  /**
   * 物理删除.
   */
  public void deleteWL(DrEquipment drEquipment);

  /**
   * 获取单条数据Tindex
   * @return
   */
  public long getTindexByNo(@Param("no") String no);

  /**
   * 更新Tindex数据
   * @return
   */
  public int updateTindexByNo(@Param("no") String no, @Param("tindex") int tindex);
  /**
   * 更新Tindex数据
   * @return
   */
  public void updateDelFlag(DrEquipment drEquipment);

  List<DrEquipment> getListByDrEquipmentNo(DrEquipment drEquipment);

  List<DrEquipment> getListByDrEquipmentIpPort(DrEquipment drEquipment);

  void updateBatchEquipmentStatusByIds(@Param("dealStatus") Integer dealStatus, @Param("ids") List<String> ids);
}
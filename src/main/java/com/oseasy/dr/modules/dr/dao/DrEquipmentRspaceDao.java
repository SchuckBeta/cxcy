package com.oseasy.dr.modules.dr.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.dr.modules.dr.entity.DrEquipment;
import com.oseasy.dr.modules.dr.entity.DrEquipmentRspace;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 门禁设备场地DAO接口.
 * @author chenh
 * @version 2018-03-30
 */
@MyBatisDao
public interface DrEquipmentRspaceDao extends CrudDao<DrEquipmentRspace> {

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(DrEquipmentRspace drEquipmentRspace);

  List<DrEquipmentRspace> getAllRelationListByDrEquipment(String drEquipment);

  void deleteByDrEquipmentId(@Param("drEquipmentId")String drEquipmentId);

    /**
     * 根据ID获取门禁场地关联房间.
     * @param id
     * @return DrEquipmentRspace
     */
    public DrEquipmentRspace getByRoom(String id);

    /**
     * 查询获取门禁场地关联房间.
     * @param id
     * @return DrEquipmentRspace
     */
    public List<DrEquipmentRspace> findListByRoom(DrEquipmentRspace drEquipmentRspace);

    /**
     * 批量更新数据处理状态
     * @param cids 授权ID
     * @param dealStatus 状态
     * @return
     */
    public int updateDealStatusByPl(@Param("ids") List<String> ids, @Param("dealStatus") Integer dealStatus);

  /**
   * 修改门的状态
   * @param dealStatus 状态值
   * @param equipmentId 设备Id（对应数据库中的equipment表中的主键ID）
   * @param doorNos 需修改状态的门编号数组
   */
    public void updateDoorStatusByEquipmentIdAndDoorNos(@Param("dealStatus")Integer dealStatus, @Param("equipmentId")String equipmentId, @Param("doorNos")List<String> doorNos);
}
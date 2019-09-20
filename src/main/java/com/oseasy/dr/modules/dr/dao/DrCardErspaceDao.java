package com.oseasy.dr.modules.dr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.dr.modules.dr.entity.DrCardErspace;
import com.oseasy.dr.modules.dr.entity.DrEmentNo;

/**
 * 卡设备DAO接口.
 * @author chenh
 * @version 2018-04-03
 */
@MyBatisDao
public interface DrCardErspaceDao extends CrudDao<DrCardErspace> {
    /**
     * 获取单条数据
     * @param entity
     * @return
     */
    public DrCardErspace getByg(String id);

    /**
     * 查询数据列表，如果需要分页，请设置分页对象，如：entity.setPage(new Page<T>());
     * @param entity
     * @return
     */
    public List<DrCardErspace> findListByg(DrCardErspace entity);

    /**
     * 根据卡id查询所有数据列表
     * 查询所有数据列表
     * @param cids 卡ID
     * @return List
     */
    public List<DrEmentNo> findDrEmentNosByCid(@Param("cid") String cid);


    /**
     * 根据卡ids查询所有数据列表
     * @param cids 卡IDs
     * @return List
     */
    public List<DrEmentNo> findDrEmentNosByCids(@Param("cids") List<String> cids);

    /**
     * 查询所有数据列表
     * @param entity
     * @return
     */
    public List<DrCardErspace> findAllListByg(DrCardErspace entity);

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(DrCardErspace drCardErspace);

  /**
   * 批量更新数据
   * @param entity
   * @return
   */
  public int savePl(@Param("list") List<DrCardErspace> entitys);

  /**
   * 批量更新数据
   * @param entity
   * @return
   */
  public int updateByPl(@Param("entitys") List<DrCardErspace> entitys);
  /**
   * 批量更新数据状态
   * @param cids 卡号
   * @param status 状态
   * @return
   */
  public int updateStatusByCid(@Param("cids") List<String> cids, @Param("status") Integer status);

  /**
   * 批量更新数据状态
   * @param cids 卡号
   * @param status 状态
   * @return
   */
  public int updateStatusByPl(@Param("ids") List<String> ids, @Param("status") Integer status);

  /**
   * 批量物理删除.
   * @param entity
   */
  public void deletePlwl(@Param("ids") List<String> ids);

  /**
   * 根据申报ID物理删除.
   * @param entity
   */
  public void deletePlwlByCard(@Param("cardId") String cardId);
}
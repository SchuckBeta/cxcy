package com.oseasy.dr.modules.dr.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.dr.modules.dr.entity.DrCardRecord;
import com.oseasy.dr.modules.dr.vo.DrCardRecordShowVo;
import com.oseasy.dr.modules.dr.vo.DrCardRecordVo;
import com.oseasy.dr.modules.dr.vo.DrCardRecordWarnVo;

/**
 * 门禁卡记录DAO接口.
 * @author chenh
 * @version 2018-03-30
 */
@MyBatisDao
public interface DrCardRecordDao extends CrudDao<DrCardRecord> {

    /**
     * 更新处理标志
     * @return
     */
    public void updateDisposeDone(@Param("ds") List<DrCardRecordVo> list);
    /**
     * 获取所有待处理数据
     * @return
     */
    public List<DrCardRecordVo> getDataToDispose(@Param("date") String date, @Param("gids") List<String> gids, @Param("rspaceIds") List<String> rspaceIds);
    /**
     * 获取单条数据
     * @param entity
     * @return
     */
    public DrCardRecord getByg(String id);

    /**
     * 查询数据列表，如果需要分页，请设置分页对象，如：entity.setPage(new Page<T>());
     * @param entity
     * @return
     */
    public List<DrCardRecord> findListByg(DrCardRecord entity);

    /**
     * 查询所有数据列表
     * @param entity
     * @return
     */
    public List<DrCardRecord> findAllListByg(DrCardRecord entity);

    /**
     * 查询卡信息列表
     * @param entity
     * @return
     */
    public List<DrCardRecord> findAllCard();


    /**
     * 查询卡记录状态信息
     * @return
     */
    public List<DrCardRecord> findAllMsg();



    /**
     * 查询场地和房间列表
     * @return
     */
    public List<DrCardRecord> findAllSpace();

    /**
     * 查询所有待同步的记录列表
     * @return
     */
    public List<DrCardRecord> findAllRecord(@Param("minPcTime") Date minPcTime, @Param("maxPcTime") Date maxPcTime);
    /**
     * 查询所有待同步的记录列表
     * @return
     */
    public List<DrCardRecord> findAllSynRecord(@Param("minPcTime") Date minPcTime, @Param("maxPcTime") Date maxPcTime);

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(DrCardRecord drCardRecord);

  /**
   * 批量新增.
   * @param entity
   */
  public int savePl(@Param("list") List<DrCardRecord> list);

  /**
   * 批量更新数据
   * @param entity
   * @return
   */
  public int updateByPl(@Param("entitys") List<DrCardRecord> entitys);
  /**
   * 批量更新数据
   * @param entity
   * @return
   */
  public int updateUidByPl(@Param("entitys") List<DrCardRecord> entitys);

    List<String> findInList(@Param("day")String  day);

    List<String> findOutList(@Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

    List<DrCardRecordWarnVo> getListVo(DrCardRecordWarnVo drCardRecordWarnVo);

    Date getLastEnterTime(@Param("cardId")String cardId);

    List<DrCardRecordShowVo> findRecordList(DrCardRecordShowVo drCardRecordShowVo);
}
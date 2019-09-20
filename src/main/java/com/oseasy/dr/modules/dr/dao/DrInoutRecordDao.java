package com.oseasy.dr.modules.dr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.dr.modules.dr.entity.DrCardRecord;
import com.oseasy.dr.modules.dr.entity.DrInoutRecord;
import com.oseasy.dr.modules.dr.vo.DrCardRecordVo;
import com.oseasy.dr.modules.dr.vo.DrInoutRecordVo;

/**
 * 门禁卡出入记录DAO接口.
 * @author 奔波儿灞
 * @version 2018-04-08
 */
@MyBatisDao
public interface DrInoutRecordDao extends CrudDao<DrInoutRecord> {

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(DrInoutRecord drInoutRecord);
  public DrInoutRecord getLastEnterData(DrCardRecordVo vo);
  public List<DrInoutRecordVo> getListVo(DrInoutRecordVo vo);
  public void updateChildEmptyOutTime(@Param("spaceOrRoomId")String spaceOrRoomId,@Param("maxTime")Long maxTime,@Param("date")String date);

  List<DrInoutRecord> getAllDrByCardId(@Param("cardId")String cardId);
  /**
   * 批量更新数据
   * @param entity
   * @return
   */
  public int updateByPl(@Param("entitys") List<DrInoutRecord> entitys);
  /**
   * 批量更新数据
   * @param entity
   * @return
   */
  public int updateUidByPl(@Param("entitys") List<DrInoutRecord> entitys);

}
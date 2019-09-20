/**
 *
 */
package com.oseasy.com.mqserver.modules.oa.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotifyRecord;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.com.pcore.modules.sys.entity.User;

/**
 * 通知通告记录DAO接口

 * @version 2014-05-16
 */
@MyBatisDao
public interface OaNotifyRecordDao extends CrudDao<OaNotifyRecord> {
	public String getReadFlag(@Param("oid")String oid, @Param("user")User user);
	/**
	 * 插入通知记录
	 * @param oaNotifyRecordList
	 * @return
	 */
	public int insertAll(List<OaNotifyRecord> oaNotifyRecordList);
	public int insertAllOffice(List<OaNotifyRecord> oaNotifyRecordList);
	public int insert(OaNotifyRecord oaNotifyRecord);
	/**
	 * 根据通知ID删除通知记录
	 * @param oaNotifyId 通知ID
	 * @return
	 */
	public int deleteByOaNotifyId(String oaNotifyId);
	public int deleteAllTeamOaNotify(String oaNotifyId);
	public OaNotifyRecord findMyList(OaNotify oaNotify);
	public OaNotifyRecord getTeamOaNotify(OaNotifyRecord oaNotifyRecord);
	public OaNotifyRecord getMine(OaNotifyRecord oaNotifyRecord);
	public void updateOperateFlag(OaNotifyRecord oaNotifyRecord);
	public void updateReadFlag(OaNotifyRecord oaNotifyRecord);
	public void updateReadOperateFlag(OaNotifyRecord oaNotifyRecord);

	public void deleteTeamOaNotify(OaNotifyRecord oaNotifyRecord);
}
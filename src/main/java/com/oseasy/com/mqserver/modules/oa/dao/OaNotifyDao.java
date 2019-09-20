/**
 *
 */
package com.oseasy.com.mqserver.modules.oa.dao;

import java.util.List;
import java.util.Map;

import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import org.apache.ibatis.annotations.Param;

import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.com.pcore.modules.sys.entity.User;

/**
 * 通知通告DAO接口

 * @version 2014-05-16
 */
@MyBatisDao
public interface OaNotifyDao extends CrudDao<OaNotify> {
	@Override
	@FindListByTenant
	public List<OaNotify> findList(OaNotify entity);
	@Override
	@InsertByTenant
	public int insert(OaNotify entity);
	@FindListByTenant
	public Integer getUnreadCount(String uid);
	@FindListByTenant
	public Integer getUnreadCountByUser(User user);
	/**
	 * 获取通知数目
	 * @param oaNotify
	 * @return
	 */
	@FindListByTenant
	public Long findCount(OaNotify oaNotify);
	@FindListByTenant
	public List<OaNotify> loginList(Integer number);
	@FindListByTenant
	public List<OaNotify> findLoginList(OaNotify oaNotify);

	public List<OaNotify> unReadOaNotifyList(OaNotify oaNotify);
	@FindListByTenant
	public Integer findNotifyCount(String createBy,String userId);
	@FindListByTenant
	public OaNotify findOaNotifyByTeamID(String userId,String sId);
	@FindListByTenant
	List<OaNotify> findSendList(OaNotify oaNotify);
	@FindListByTenant
	public List<Map<String,Object>> getMore(@Param(value="type")String type,@Param(value="id")String id,@Param(value="keys") List<String> keys);
	public void updateViews(@Param("param") Map<String,Integer> param);

	public List<OaNotify> findAllRecord(OaNotify entity);
}
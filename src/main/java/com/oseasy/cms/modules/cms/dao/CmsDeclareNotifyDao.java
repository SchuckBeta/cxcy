package com.oseasy.cms.modules.cms.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.oseasy.cms.modules.cms.entity.CmsDeclareNotify;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

/**
 * declareDAO接口.
 * 
 * @author 奔波儿灞
 * @version 2018-01-24
 */
@MyBatisDao
public interface CmsDeclareNotifyDao extends CrudDao<CmsDeclareNotify> {

	/**
	 * 物理删除.
	 * 
	 * @param entity
	 */
	public void deleteWL(CmsDeclareNotify cmsDeclareNotify);

	public void updateViews(@Param("param") Map<String, Integer> param);
	public void updateRelease(@Param("id") String id,@Param("release")String release);
}
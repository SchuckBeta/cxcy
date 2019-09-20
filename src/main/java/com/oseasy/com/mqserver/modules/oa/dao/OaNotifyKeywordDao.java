package com.oseasy.com.mqserver.modules.oa.dao;

import java.util.List;

import com.oseasy.com.mqserver.modules.oa.entity.OaNotifyKeyword;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

/**
 * 通知通告关键词DAO接口.
 * @author 9527
 * @version 2017-07-11
 */
@MyBatisDao
public interface OaNotifyKeywordDao extends CrudDao<OaNotifyKeyword> {
	public List<String> findListByEsid(String esid);
	public void delByEsid(String esid);
}
package com.oseasy.pro.modules.excellent.dao;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.excellent.entity.ExcellentKeyword;

/**
 * 优秀展示关键词DAO接口.
 * @author 9527
 * @version 2017-06-23
 */
@MyBatisDao
public interface ExcellentKeywordDao extends CrudDao<ExcellentKeyword> {
	public List<String> findListByEsid(String esid);
	public void delByEsid(String esid);

}
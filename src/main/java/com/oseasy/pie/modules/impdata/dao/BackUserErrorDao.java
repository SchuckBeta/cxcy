package com.oseasy.pie.modules.impdata.dao;

import java.util.List;
import java.util.Map;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pie.modules.impdata.entity.BackUserError;

/**
 * 后台用户导入DAO接口
 * @author 9527
 * @version 2017-05-23
 */
@MyBatisDao
public interface BackUserErrorDao extends CrudDao<BackUserError> {
	public List<Map<String,String>> getListByImpId(String impid);
	public void deleteByImpId(String impid);
}
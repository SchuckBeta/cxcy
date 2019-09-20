package com.oseasy.scr.modules.sco.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.scr.modules.sco.entity.ScoApply;

/**
 * 学分课程申请DAO接口.
 * @author zhangzheng
 * @version 2017-07-13
 */
@MyBatisDao
public interface ScoApplyDao extends CrudDao<ScoApply> {
	@FindListByTenant
	public Long getCount(ScoApply p);
	@Override
	@InsertByTenant
	public int insert(ScoApply entity);
}
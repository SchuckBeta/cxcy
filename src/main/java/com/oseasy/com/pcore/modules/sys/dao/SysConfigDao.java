package com.oseasy.com.pcore.modules.sys.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.com.pcore.modules.sys.entity.SysConfig;

import java.util.List;

/**
 * 系统配置DAO接口.
 * @author 9527
 * @version 2017-10-19
 */
@MyBatisDao
public interface SysConfigDao extends CrudDao<SysConfig> {
	@Override
	@FindListByTenant
	public List<SysConfig> findList(SysConfig entity);
	@FindListByTenant
	public List<SysConfig> findListByTenant(SysConfig entity);

	@FindListByTenant
	public SysConfig getSysConfig();

	@FindListByTenant
	SysConfig getByTenant(SysConfig entity);

	@Override
	@InsertByTenant
	public int insert(SysConfig entity);
}
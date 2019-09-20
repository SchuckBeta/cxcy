package com.oseasy.com.pcore.modules.authorize.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.com.pcore.modules.authorize.entity.SysLicense;

import java.util.List;

/**
 * 授权信息DAO接口
 * @author 9527
 * @version 2017-04-13
 */
@MyBatisDao
public interface SysLicenseDao extends CrudDao<SysLicense> {

	 void insertWithId(SysLicense s);

	@Override
	SysLicense get(SysLicense sysLicense);

	@InsertByTenant
	@Override
	int insert(SysLicense sysLicense);


	List<SysLicense> getAllSysLicenses();

	String getLicense(String id);
}
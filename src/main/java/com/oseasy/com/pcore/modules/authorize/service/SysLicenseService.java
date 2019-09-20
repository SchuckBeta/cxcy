package com.oseasy.com.pcore.modules.authorize.service;

import java.util.Date;
import java.util.List;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.authorize.dao.SysLicenseDao;
import com.oseasy.com.pcore.modules.authorize.entity.SysLicense;

/**
 * 授权信息Service
 * @author 9527
 * @version 2017-04-13
 */
@Service
@Transactional(readOnly = true)
public class SysLicenseService extends CrudService<SysLicenseDao, SysLicense> {
	public static boolean unValid=false;
	public static final String KEY="0e531a31dd73418c8951b1a1c83db33c";

	public SysLicense getLicense(String tenantId) {
		String key = KEY;
		if (tenantId != null){
			key = key+"@"+tenantId;
		}
		return super.get(key);
	}

	public SysLicense get(String id) {
		return super.get(id);
	}

	public List<SysLicense> findList(SysLicense sysLicense) {
		return super.findList(sysLicense);
	}

	public Page<SysLicense> findPage(Page<SysLicense> page, SysLicense sysLicense) {
		return super.findPage(page, sysLicense);
	}

	@Transactional(readOnly = false)
	public void saveLicense(SysLicense sysLicense) {

		String key = KEY;
		if (sysLicense.getTenantId() != null){
			key = key+"@"+sysLicense.getTenantId();
		}
		String lic = dao.getLicense(key);
		sysLicense.setUpdateDate(new Date());
		if (lic == null){
			sysLicense.setId(key);
			sysLicense.setCreateDate(new Date());
			dao.insertWithId(sysLicense);
		}else{
			dao.update(sysLicense);
		}

	}
	@Transactional(readOnly = false)
	public void save(SysLicense sysLicense) {
		super.save(sysLicense);
	}
	@Transactional(readOnly = false)
	public void delete(SysLicense sysLicense) {
		super.delete(sysLicense);
	}
	@Transactional(readOnly = false)
	public void insertWithId(SysLicense sysLicense) {
		dao.insertWithId(sysLicense);
	}


	public List<SysLicense> getAllSysLicenses(){

		return dao.getAllSysLicenses();
	}
}
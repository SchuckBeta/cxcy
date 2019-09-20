package com.oseasy.com.pcore.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.dao.SysConfigDao;
import com.oseasy.com.pcore.modules.sys.entity.SysConfig;
import com.oseasy.com.pcore.modules.sys.utils.CoreConfigUtil;

/**
 * 系统配置Service.
 * @author 9527
 * @version 2017-10-19
 */
@Service
@Transactional(readOnly = true)
public class CoreConfigService extends CrudService<SysConfigDao, SysConfig> {

	public SysConfig get(String id) {
		return super.get(id);
	}

	public List<SysConfig> findList(SysConfig sysConfig) {
		return super.findList(sysConfig);
	}

	public Page<SysConfig> findPage(Page<SysConfig> page, SysConfig sysConfig) {
		return super.findPage(page, sysConfig);
	}

	@Transactional(readOnly = false)
	public void save(SysConfig sysConfig) {
		super.save(sysConfig);
		CoreConfigUtil.removeCache();
	}

	@Transactional(readOnly = false)
	public void delete(SysConfig sysConfig) {
		super.delete(sysConfig);
		CoreConfigUtil.removeCache();
	}
	@Transactional(readOnly = false)
	public void saveSysConfig(SysConfig sysConfig) {
		String content=sysConfig.getContent().replace("&quot;","\"");
		sysConfig.setContent(content);
		save(sysConfig);
	}
}
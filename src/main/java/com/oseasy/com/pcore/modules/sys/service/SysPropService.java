package com.oseasy.com.pcore.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.dao.SysPropDao;
import com.oseasy.com.pcore.modules.sys.entity.SysProp;

/**
 * 系统功能Service.
 * @author chenh
 * @version 2018-03-30
 */
@Service
@Transactional(readOnly = true)
public class SysPropService extends CrudService<SysPropDao, SysProp> {

	public SysProp get(String id) {
		return super.get(id);
	}

	public List<SysProp> findList(SysProp sysProp) {
		return super.findList(sysProp);
	}

	public Page<SysProp> findPage(Page<SysProp> page, SysProp sysProp) {
		return super.findPage(page, sysProp);
	}

	@Transactional(readOnly = false)
	public void save(SysProp sysProp) {
		super.save(sysProp);
	}

	@Transactional(readOnly = false)
	public void delete(SysProp sysProp) {
		super.delete(sysProp);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(SysProp sysProp) {
  	  dao.deleteWL(sysProp);
  	}
}
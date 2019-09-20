package com.oseasy.pro.modules.cert.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.pro.modules.cert.dao.SysCertElementDao;
import com.oseasy.pro.modules.cert.entity.SysCertElement;

/**
 * 证书模板元素Service.
 * @author 奔波儿灞
 * @version 2018-02-06
 */
@Service
@Transactional(readOnly = true)
public class SysCertElementService extends CrudService<SysCertElementDao, SysCertElement> {

	public SysCertElement get(String id) {
		return super.get(id);
	}

	public List<SysCertElement> findList(SysCertElement sysCertElement) {
		return super.findList(sysCertElement);
	}
	public List<SysCertElement> getCertPage(String pageid){
		return dao.getSysCertElement(pageid);
	}
	public Page<SysCertElement> findPage(Page<SysCertElement> page, SysCertElement sysCertElement) {
		return super.findPage(page, sysCertElement);
	}
	@Transactional(readOnly = false)
	public void deleteByPageid(String pageid) {
		dao.deleteByPageid(pageid);
	}
	@Transactional(readOnly = false)
	public void save(SysCertElement sysCertElement) {
		super.save(sysCertElement);
	}
	@Transactional(readOnly = false)
	public void insert(SysCertElement sysCertElement) {
		dao.insert(sysCertElement);
	}
	@Transactional(readOnly = false)
	public void delete(SysCertElement sysCertElement) {
		super.delete(sysCertElement);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(SysCertElement sysCertElement) {
  	  dao.deleteWL(sysCertElement);
  	}
}
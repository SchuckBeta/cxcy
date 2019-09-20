package com.oseasy.pro.modules.cert.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.pro.modules.cert.dao.SysCertPageDao;
import com.oseasy.pro.modules.cert.entity.SysCertPage;

/**
 * 证书模板页面Service.
 * @author 奔波儿灞
 * @version 2018-02-06
 */
@Service
@Transactional(readOnly = true)
public class SysCertPageService extends CrudService<SysCertPageDao, SysCertPage> {
	@Transactional(readOnly = false,rollbackFor=Exception.class)
	public void savePageName(String pageid,String pagename){
		dao.savePageName(pageid, pagename);
	}
	public List<SysCertPage> getSysCertPages(String certid){
		return dao.getSysCertPages(certid);
	}
	public SysCertPage get(String id) {
		return super.get(id);
	}

	public List<SysCertPage> findList(SysCertPage sysCertPage) {
		return super.findList(sysCertPage);
	}

	public Page<SysCertPage> findPage(Page<SysCertPage> page, SysCertPage sysCertPage) {
		return super.findPage(page, sysCertPage);
	}
	public Integer getMaxSort(String certid){
		return dao.getMaxSort(certid);
	}

	@Transactional(readOnly = false)
	public void save(SysCertPage sysCertPage) {
		super.save(sysCertPage);
	}

	@Transactional(readOnly = false)
	public void delete(SysCertPage sysCertPage) {
		super.delete(sysCertPage);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(SysCertPage sysCertPage) {
  	  dao.deleteWL(sysCertPage);
  	}
	
}
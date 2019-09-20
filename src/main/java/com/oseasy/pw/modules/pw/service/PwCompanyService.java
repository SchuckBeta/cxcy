package com.oseasy.pw.modules.pw.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.pw.modules.pw.dao.PwCompanyDao;
import com.oseasy.pw.modules.pw.entity.PwCompany;

/**
 * 入驻企业Service.
 * @author chenh
 * @version 2017-11-26
 */
@Service
@Transactional(readOnly = true)
public class PwCompanyService extends CrudService<PwCompanyDao, PwCompany> {

	public PwCompany get(String id) {
		return super.get(id);
	}

	public List<PwCompany> findList(PwCompany pwCompany) {
		return super.findList(pwCompany);
	}

	public Page<PwCompany> findPage(Page<PwCompany> page, PwCompany pwCompany) {
		return super.findPage(page, pwCompany);
	}

	@Transactional(readOnly = false)
	public void save(PwCompany pwCompany) {
		super.save(pwCompany);
	}

	@Transactional(readOnly = false)
	public void delete(PwCompany pwCompany) {
		super.delete(pwCompany);
	}

	public PwCompany getByEid(String eid) {
		return dao.getByEid(eid);
	}

	public List<PwCompany> findListByPwCompany(PwCompany pwCompany) {
		return dao.findListByPwCompany(pwCompany);
	}
}
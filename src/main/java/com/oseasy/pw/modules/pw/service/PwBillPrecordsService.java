package com.oseasy.pw.modules.pw.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.pw.modules.pw.dao.PwBillPrecordsDao;
import com.oseasy.pw.modules.pw.entity.PwBillPrecords;

/**
 * 缴费记录Service.
 * @author chenh
 * @version 2017-11-26
 */
@Service
@Transactional(readOnly = true)
public class PwBillPrecordsService extends CrudService<PwBillPrecordsDao, PwBillPrecords> {

	public PwBillPrecords get(String id) {
		return super.get(id);
	}

	public List<PwBillPrecords> findList(PwBillPrecords pwBillPrecords) {
		return super.findList(pwBillPrecords);
	}

	public Page<PwBillPrecords> findPage(Page<PwBillPrecords> page, PwBillPrecords pwBillPrecords) {
		return super.findPage(page, pwBillPrecords);
	}

	@Transactional(readOnly = false)
	public void save(PwBillPrecords pwBillPrecords) {
		super.save(pwBillPrecords);
	}

	@Transactional(readOnly = false)
	public void delete(PwBillPrecords pwBillPrecords) {
		super.delete(pwBillPrecords);
	}

}
package com.oseasy.pw.modules.pw.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.pw.modules.pw.dao.PwBillDetailDao;
import com.oseasy.pw.modules.pw.entity.PwBillDetail;

/**
 * 账单明细Service.
 * @author chenh
 * @version 2017-11-26
 */
@Service
@Transactional(readOnly = true)
public class PwBillDetailService extends CrudService<PwBillDetailDao, PwBillDetail> {

	public PwBillDetail get(String id) {
		return super.get(id);
	}

	public List<PwBillDetail> findList(PwBillDetail pwBillDetail) {
		return super.findList(pwBillDetail);
	}

	public Page<PwBillDetail> findPage(Page<PwBillDetail> page, PwBillDetail pwBillDetail) {
		return super.findPage(page, pwBillDetail);
	}

	@Transactional(readOnly = false)
	public void save(PwBillDetail pwBillDetail) {
		super.save(pwBillDetail);
	}

	@Transactional(readOnly = false)
	public void delete(PwBillDetail pwBillDetail) {
		super.delete(pwBillDetail);
	}

}
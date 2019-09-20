package com.oseasy.pw.modules.pw.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.pw.modules.pw.dao.PwCosMaterialUhistoryDao;
import com.oseasy.pw.modules.pw.entity.PwCosMaterialUhistory;

/**
 * 耗材使用记录Service.
 * @author chenh
 * @version 2017-11-26
 */
@Service
@Transactional(readOnly = true)
public class PwCosMaterialUhistoryService extends CrudService<PwCosMaterialUhistoryDao, PwCosMaterialUhistory> {

	public PwCosMaterialUhistory get(String id) {
		return super.get(id);
	}

	public List<PwCosMaterialUhistory> findList(PwCosMaterialUhistory pwCosMaterialUhistory) {
		return super.findList(pwCosMaterialUhistory);
	}

	public Page<PwCosMaterialUhistory> findPage(Page<PwCosMaterialUhistory> page, PwCosMaterialUhistory pwCosMaterialUhistory) {
		return super.findPage(page, pwCosMaterialUhistory);
	}

	@Transactional(readOnly = false)
	public void save(PwCosMaterialUhistory pwCosMaterialUhistory) {
		super.save(pwCosMaterialUhistory);
	}

	@Transactional(readOnly = false)
	public void delete(PwCosMaterialUhistory pwCosMaterialUhistory) {
		super.delete(pwCosMaterialUhistory);
	}

}
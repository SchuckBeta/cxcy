package com.oseasy.pw.modules.pw.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.pw.modules.pw.dao.PwEnterXqhistoryDao;
import com.oseasy.pw.modules.pw.entity.PwEnter;
import com.oseasy.pw.modules.pw.entity.PwEnterXqhistory;

/**
 * 入驻续期历史Service.
 * @author zy
 * @version 2018-01-02
 */
@Service
@Transactional(readOnly = true)
public class PwEnterXqhistoryService extends CrudService<PwEnterXqhistoryDao, PwEnterXqhistory> {

	public PwEnterXqhistory get(String id) {
		return super.get(id);
	}

	public List<PwEnterXqhistory> findList(PwEnterXqhistory pwEnterXqhistory) {
		return super.findList(pwEnterXqhistory);
	}

	public Page<PwEnterXqhistory> findPage(Page<PwEnterXqhistory> page, PwEnterXqhistory pwEnterXqhistory) {
		return super.findPage(page, pwEnterXqhistory);
	}

	@Transactional(readOnly = false)
	public void save(PwEnter pwEnter) {
        PwEnterXqhistory pwEnterXqhistory = new PwEnterXqhistory();
        pwEnterXqhistory.setEid(pwEnter.getId());
        pwEnterXqhistory.setTerm(pwEnter.getTerm());
        pwEnterXqhistory.setStartDate(pwEnter.getStartDate());
        pwEnterXqhistory.setEndDate(pwEnter.getEndDate());
		super.save(pwEnterXqhistory);
	}

	@Transactional(readOnly = false)
	public void save(PwEnterXqhistory pwEnterXqhistory) {
	  super.save(pwEnterXqhistory);
	}

	@Transactional(readOnly = false)
	public void delete(PwEnterXqhistory pwEnterXqhistory) {
		super.delete(pwEnterXqhistory);
	}


}
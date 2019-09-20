package com.oseasy.scr.modules.sco.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.scr.modules.sco.dao.ScoAuditingDao;
import com.oseasy.scr.modules.sco.entity.ScoAuditing;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 学分记录审核表Service.
 * @author zhangzheng
 * @version 2017-07-18
 */
@Service
@Transactional(readOnly = true)
public class ScoAuditingService extends CrudService<ScoAuditingDao, ScoAuditing> {

	public ScoAuditing get(String id) {
		return super.get(id);
	}

	public List<ScoAuditing> findList(ScoAuditing scoAuditing) {
		return super.findList(scoAuditing);
	}

	public Page<ScoAuditing> findPage(Page<ScoAuditing> page, ScoAuditing scoAuditing) {
		return super.findPage(page, scoAuditing);
	}

	@Transactional(readOnly = false)
	public void save(ScoAuditing scoAuditing) {
		super.save(scoAuditing);
	}

	@Transactional(readOnly = false)
	public void delete(ScoAuditing scoAuditing) {
		super.delete(scoAuditing);
	}

}
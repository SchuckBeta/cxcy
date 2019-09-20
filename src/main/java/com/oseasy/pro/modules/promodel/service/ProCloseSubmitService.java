package com.oseasy.pro.modules.promodel.service;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.pro.modules.promodel.dao.ProCloseSubmitDao;
import com.oseasy.pro.modules.promodel.entity.ProCloseSubmit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 结项提交信息表Service.
 * @author zy
 * @version 2017-12-01
 */
@Service
@Transactional(readOnly = true)
public class ProCloseSubmitService extends CrudService<ProCloseSubmitDao, ProCloseSubmit> {

	public ProCloseSubmit get(String id) {
		return super.get(id);
	}

	public List<ProCloseSubmit> findList(ProCloseSubmit proCloseSubmit) {
		return super.findList(proCloseSubmit);
	}

	public Page<ProCloseSubmit> findPage(Page<ProCloseSubmit> page, ProCloseSubmit proCloseSubmit) {
		return super.findPage(page, proCloseSubmit);
	}

	@Transactional(readOnly = false)
	public void save(ProCloseSubmit proCloseSubmit) {
		super.save(proCloseSubmit);
	}

	@Transactional(readOnly = false)
	public void delete(ProCloseSubmit proCloseSubmit) {
		super.delete(proCloseSubmit);
	}

	public ProCloseSubmit getByGnodeId(String proModelId, String gnodeId) {
		return dao.getByGnodeId(proModelId, gnodeId) ;
	}
}
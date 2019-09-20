package com.oseasy.pro.modules.project.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.pro.modules.project.dao.ProProgressDao;
import com.oseasy.pro.modules.project.entity.ProProgress;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 国创项目进度表单Service
 * @author 9527
 * @version 2017-03-29
 * @Deprecated
 */
@Service
@Transactional(readOnly = true)
public class ProProgressService extends CrudService<ProProgressDao, ProProgress> {

	public ProProgress get(String id) {
		return super.get(id);
	}

	public List<ProProgress> findList(ProProgress proProgress) {
		return super.findList(proProgress);
	}

	public Page<ProProgress> findPage(Page<ProProgress> page, ProProgress proProgress) {
		return super.findPage(page, proProgress);
	}

	@Transactional(readOnly = false)
	public void save(ProProgress proProgress) {
		super.save(proProgress);
	}

	@Transactional(readOnly = false)
	public void delete(ProProgress proProgress) {
		super.delete(proProgress);
	}

	public List<ProProgress> getByFid(String fid) {
		return dao.getByFid(fid);
	}

}
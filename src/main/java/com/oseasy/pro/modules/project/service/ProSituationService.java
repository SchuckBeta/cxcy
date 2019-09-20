package com.oseasy.pro.modules.project.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.pro.modules.project.dao.ProSituationDao;
import com.oseasy.pro.modules.project.entity.ProSituation;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 国创项目完成情况表单Service
 * @author 9527
 * @version 2017-03-29
 * @Deprecated
 */
@Service
@Transactional(readOnly = true)
public class ProSituationService extends CrudService<ProSituationDao, ProSituation> {

	public ProSituation get(String id) {
		return super.get(id);
	}

	public List<ProSituation> findList(ProSituation proSituation) {
		return super.findList(proSituation);
	}

	public Page<ProSituation> findPage(Page<ProSituation> page, ProSituation proSituation) {
		return super.findPage(page, proSituation);
	}

	@Transactional(readOnly = false)
	public void save(ProSituation proSituation) {
		super.save(proSituation);
	}

	@Transactional(readOnly = false)
	public void delete(ProSituation proSituation) {
		super.delete(proSituation);
	}

	public List<ProSituation>  getByFid(String fid) {
		return dao.getByFid(fid);
	}

}
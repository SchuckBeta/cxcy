package com.oseasy.pro.modules.project.service;

import java.util.List;

import com.oseasy.pro.modules.project.dao.ProMidDao;
import com.oseasy.pro.modules.project.dao.ProProgressDao;
import com.oseasy.pro.modules.project.dao.ProSituationDao;
import com.oseasy.pro.modules.project.entity.ProMid;
import com.oseasy.pro.modules.project.entity.ProProgress;
import com.oseasy.pro.modules.project.entity.ProSituation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 国创项目中期检查表单Service
 * @author 9527
 * @version 2017-03-29
 */
@Service
@Transactional(readOnly = true)
public class ProMidService extends CrudService<ProMidDao, ProMid> {
	@Autowired
	ProSituationDao proSituationDao;

	@Autowired
	ProProgressDao  proProgressDao;

	public ProMid get(String id) {
		return super.get(id);
	}
	
	public List<ProMid> findList(ProMid proMid) {
		return super.findList(proMid);
	}
	
	public Page<ProMid> findPage(Page<ProMid> page, ProMid proMid) {
		return super.findPage(page, proMid);
	}
	
	@Transactional(readOnly = false)
	public void save(ProMid proMid) {
			super.save(proMid);  //保存主表
		//保存子表  组成员完成情况
		//先删除以前的数据
		proSituationDao.deleteByMidId(proMid.getId());
		for (ProSituation proSituation : proMid.getProSituationList()) {
			proSituation.setFId(proMid.getId());
			proSituation.setType("1");  // 1代表中期检查
			proSituation.preInsert();
			proSituationDao.insert(proSituation);
		}
		//保存子表  当前项目进度
		//先删除以前的数据
		proProgressDao.deleteByMidId(proMid.getId());
		for (ProProgress proProgress:proMid.getProProgresseList()) {
			proProgress.setFId(proMid.getId());
			proProgress.setType("1");
			proProgress.preInsert();
			proProgressDao.insert(proProgress);
		}


	}
	@Transactional(readOnly = false)
	public void saveSuggest(ProMid proMid) {
		super.save(proMid);
	}
	
	@Transactional(readOnly = false)
	public void delete(ProMid proMid) {
		super.delete(proMid);
	}

	public ProMid getByProjectId(String pid) {
		return dao.getByProjectId(pid);
	}
	
}
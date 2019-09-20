package com.oseasy.pro.modules.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.pro.modules.project.dao.ProProgressDao;
import com.oseasy.pro.modules.project.dao.ProSituationDao;
import com.oseasy.pro.modules.project.dao.ProjectCloseDao;
import com.oseasy.pro.modules.project.dao.ProjectCloseFundDao;
import com.oseasy.pro.modules.project.dao.ProjectCloseResultDao;
import com.oseasy.pro.modules.project.entity.ProProgress;
import com.oseasy.pro.modules.project.entity.ProSituation;
import com.oseasy.pro.modules.project.entity.ProjectClose;
import com.oseasy.pro.modules.project.entity.ProjectCloseFund;
import com.oseasy.pro.modules.project.entity.ProjectCloseResult;

/**
 * project_closeService
 * @author zhangzheng
 * @version 2017-03-29
 */
@Service
@Transactional(readOnly = true)
public class ProjectCloseService extends CrudService<ProjectCloseDao, ProjectClose> {
	@Autowired
	ProSituationDao proSituationDao;
	@Autowired
	ProProgressDao proProgressDao;
	@Autowired
	ProjectCloseFundDao projectCloseFundDao;
	@Autowired
	ProjectCloseResultDao projectCloseResultDao;


	public ProjectClose get(String id) {
		return super.get(id);
	}

	public List<ProjectClose> findList(ProjectClose projectClose) {
		return super.findList(projectClose);
	}

	public Page<ProjectClose> findPage(Page<ProjectClose> page, ProjectClose projectClose) {
		return super.findPage(page, projectClose);
	}

	@Transactional(readOnly = false)
	public void save(ProjectClose projectClose) {
		super.save(projectClose);  //保存主表
		//先删除以前的数据
		proSituationDao.deleteByMidId(projectClose.getId());
		for (ProSituation proSituation : projectClose.getProSituationList()) {  //组成员完成情况
			proSituation.setFId(projectClose.getId());
			proSituation.setType("2");  // 2代表结项审核
			proSituation.preInsert();
			proSituationDao.insert(proSituation);
		}

		//先删除以前的数据
		proProgressDao.deleteByMidId(projectClose.getId());
		for (ProProgress proProgress:projectClose.getProProgresseList()) {//保存子表  当前项目进度
			proProgress.setFId(projectClose.getId());
			proProgress.setType("2");
			proProgress.preInsert();
			proProgressDao.insert(proProgress);
		}

		//经费使用情况
		projectCloseFundDao.deleteByCloseId(projectClose.getId());
		for(ProjectCloseFund projectCloseFund:projectClose.getProjectCloseFundList()) {
			projectCloseFund.setCloseId(projectClose.getId());
			projectCloseFund.preInsert();
			projectCloseFundDao.insert(projectCloseFund);
		}

		//成果描述
		projectCloseResultDao.deleteByCloseId(projectClose.getId());
		for (ProjectCloseResult projectCloseResult:projectClose.getProjectCloseResultList()) {
			projectCloseResult.setCloseId(projectClose.getId());
			projectCloseResult.preInsert();
			projectCloseResultDao.insert(projectCloseResult);
		}

	}

	@Transactional(readOnly = false)
	public void saveSuggest(ProjectClose projectClose) {
		super.save(projectClose);  //保存主表
	}

	@Transactional(readOnly = false)
	public void delete(ProjectClose projectClose) {
		super.delete(projectClose);
	}

	public ProjectClose getByProjectId(String pid) {
		return dao.getByProjectId(pid);
	}

}
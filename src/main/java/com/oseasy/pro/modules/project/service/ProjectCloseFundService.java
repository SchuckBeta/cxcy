package com.oseasy.pro.modules.project.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.pro.modules.project.dao.ProjectCloseFundDao;
import com.oseasy.pro.modules.project.entity.ProjectCloseFund;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * project_close_fundService
 * @author zhangzheng
 * @version 2017-03-29
 */
@Service
@Transactional(readOnly = true)
public class ProjectCloseFundService extends CrudService<ProjectCloseFundDao, ProjectCloseFund> {

	public ProjectCloseFund get(String id) {
		return super.get(id);
	}
	
	public List<ProjectCloseFund> findList(ProjectCloseFund projectCloseFund) {
		return super.findList(projectCloseFund);
	}
	
	public Page<ProjectCloseFund> findPage(Page<ProjectCloseFund> page, ProjectCloseFund projectCloseFund) {
		return super.findPage(page, projectCloseFund);
	}
	
	@Transactional(readOnly = false)
	public void save(ProjectCloseFund projectCloseFund) {
		super.save(projectCloseFund);
	}
	
	@Transactional(readOnly = false)
	public void delete(ProjectCloseFund projectCloseFund) {
		super.delete(projectCloseFund);
	}

	public List<ProjectCloseFund> getByCloseId(String closeId) {
		return dao.getByCloseId(closeId);
	}
	
}
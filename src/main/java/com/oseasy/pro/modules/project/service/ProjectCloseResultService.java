package com.oseasy.pro.modules.project.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.pro.modules.project.dao.ProjectCloseResultDao;
import com.oseasy.pro.modules.project.entity.ProjectCloseResult;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * project_close_resultService
 * @author zhangzheng
 * @version 2017-03-29
 */
@Service
@Transactional(readOnly = true)
public class ProjectCloseResultService extends CrudService<ProjectCloseResultDao, ProjectCloseResult> {

	public ProjectCloseResult get(String id) {
		return super.get(id);
	}
	
	public List<ProjectCloseResult> findList(ProjectCloseResult projectCloseResult) {
		return super.findList(projectCloseResult);
	}
	
	public Page<ProjectCloseResult> findPage(Page<ProjectCloseResult> page, ProjectCloseResult projectCloseResult) {
		return super.findPage(page, projectCloseResult);
	}
	
	@Transactional(readOnly = false)
	public void save(ProjectCloseResult projectCloseResult) {
		super.save(projectCloseResult);
	}
	
	@Transactional(readOnly = false)
	public void delete(ProjectCloseResult projectCloseResult) {
		super.delete(projectCloseResult);
	}

	public List<ProjectCloseResult> getByCloseId(String closeId) {
		return dao.getByCloseId(closeId);
	}
	
}
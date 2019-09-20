package com.oseasy.pro.modules.project.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.pro.modules.project.dao.ProjectPlanDao;
import com.oseasy.pro.modules.project.entity.ProjectPlan;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 项目任务Service
 * @author 9527
 * @version 2017-03-11
 */
@Service
@Transactional(readOnly = true)
public class ProjectPlanService extends CrudService<ProjectPlanDao, ProjectPlan> {

	public ProjectPlan get(String id) {
		return super.get(id);
	}

	public List<ProjectPlan> findListByProjectId(String projectId) {
		if (projectId==null) {
			return null;
		}else {
			return dao.findListByProjectId(projectId);
		}
	}
	public List<ProjectPlan> findList(ProjectPlan projectPlan) {
		return super.findList(projectPlan);
	}
	
	public Page<ProjectPlan> findPage(Page<ProjectPlan> page, ProjectPlan projectPlan) {
		return super.findPage(page, projectPlan);
	}
	
	@Transactional(readOnly = false)
	public void save(ProjectPlan projectPlan) {
		super.save(projectPlan);
	}
	
	@Transactional(readOnly = false)
	public void delete(ProjectPlan projectPlan) {
		super.delete(projectPlan);
	}
	
}
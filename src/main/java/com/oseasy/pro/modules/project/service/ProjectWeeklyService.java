package com.oseasy.pro.modules.project.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.modules.project.dao.ProjectWeeklyDao;
import com.oseasy.pro.modules.project.entity.ProjectWeekly;
import com.oseasy.pro.modules.project.vo.ProjectWeeklyVo;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 项目周报Service
 * @author 张正
 * @version 2017-03-29
 */
@Service
@Transactional(readOnly = true)
public class ProjectWeeklyService extends CrudService<ProjectWeeklyDao, ProjectWeekly> {
	@Autowired
	SysAttachmentService sysAttachmentService;
	public ProjectWeekly get(String id) {
		return super.get(id);
	}
	
	public List<ProjectWeekly> findList(ProjectWeekly projectWeekly) {
		return super.findList(projectWeekly);
	}
	
	public Page<ProjectWeekly> findPage(Page<ProjectWeekly> page, ProjectWeekly projectWeekly) {
		return super.findPage(page, projectWeekly);
	}
	
	@Transactional(readOnly = false)
	public void save(ProjectWeekly projectWeekly) {
		super.save(projectWeekly);
	}
	@Transactional(readOnly = false)
	public void submitVo(ProjectWeeklyVo vo) {
		if(StringUtil.isNotEmpty(vo.getProjectWeekly().getId())){
			ProjectWeekly old=get(vo.getProjectWeekly().getId());
			if(old==null){
				return;
			}
			old.setSuggest(vo.getProjectWeekly().getSuggest());
			old.setSuggestDate(vo.getProjectWeekly().getSuggestDate());
			old.setPlan(vo.getProjectWeekly().getPlan());
			old.setStartDate(vo.getProjectWeekly().getStartDate());
			old.setEndDate(vo.getProjectWeekly().getEndDate());
			super.save(old);
		}else{
			super.save(vo.getProjectWeekly());
		}
		if(StringUtil.isNotEmpty(vo.getProjectWeekly().getLastId())){
			ProjectWeekly lst=get(vo.getProjectWeekly().getLastId());
			if(lst!=null){
				lst.setAchieved(vo.getLastpw().getAchieved());
				lst.setProblem(vo.getLastpw().getProblem());
				super.save(lst);
			}
		}
		sysAttachmentService.saveByVo(vo.getAttachMentEntity(),vo.getProjectWeekly().getId(),FileTypeEnum.S0,FileStepEnum.S101);
	}
	@Transactional(readOnly = false)
	public void saveVo(ProjectWeeklyVo vo) {
		vo.getProjectWeekly().setSuggestDate(new Date());
		User u=UserUtils.getUser();
		vo.getProjectWeekly().setUpdateBy(u);
		vo.getProjectWeekly().setUpdateDate(new Date());
		dao.saveSuggest(vo.getProjectWeekly());
		sysAttachmentService.saveByVo(vo.getAttachMentEntity(),vo.getProjectWeekly().getId(),FileTypeEnum.S0,FileStepEnum.S101);
	}
	@Transactional(readOnly = false)
	public void delete(ProjectWeekly projectWeekly) {
		super.delete(projectWeekly);
	}
	public ProjectWeekly getLast(Map<String,String> map) {
		return dao.getLast(map);
	}
}
package com.oseasy.pro.modules.project.dao;

import java.util.List;
import java.util.Map;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.project.entity.ProjectWeekly;

/**
 * 项目周报DAO接口
 * @author 张正
 * @version 2017-03-29
 */
@MyBatisDao
public interface ProjectWeeklyDao extends CrudDao<ProjectWeekly> {
	public List<Map<String,String>> getInfoByProjectId(String pid);
	public ProjectWeekly getLast(Map<String,String> map);
	public void saveSuggest(ProjectWeekly p);
}
package com.oseasy.pro.modules.project.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.project.entity.ProjectCloseFund;

import java.util.List;

/**
 * project_close_fundDAO接口
 * @author zhangzheng
 * @version 2017-03-29
 */
@MyBatisDao
public interface ProjectCloseFundDao extends CrudDao<ProjectCloseFund> {

    public void deleteByCloseId(String closeId);

    public List<ProjectCloseFund> getByCloseId(String closeId);
	
}
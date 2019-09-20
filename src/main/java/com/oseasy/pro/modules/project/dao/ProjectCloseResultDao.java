package com.oseasy.pro.modules.project.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.project.entity.ProjectCloseResult;

import java.util.List;

/**
 * project_close_resultDAO接口
 * @author zhangzheng
 * @version 2017-03-29
 */
@MyBatisDao
public interface ProjectCloseResultDao extends CrudDao<ProjectCloseResult> {
    public void deleteByCloseId(String closeId);

    public List<ProjectCloseResult> getByCloseId(String closeId);
}
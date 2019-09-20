package com.oseasy.com.jobserver.modules.task.dao;

import com.oseasy.com.jobserver.modules.task.entity.TaskScheduleJob;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

import java.util.List;
@MyBatisDao
public interface TaskScheduleJobDao /*extends CrudDao<TaskScheduleJob> */{
	int deleteByPrimaryKey(Long jobId);

	int insert(TaskScheduleJob record);

	int insertSelective(TaskScheduleJob record);

	TaskScheduleJob selectByPrimaryKey(Long jobId);

	int updateByPrimaryKeySelective(TaskScheduleJob record);

	int updateByPrimaryKey(TaskScheduleJob record);

	List<TaskScheduleJob> getAll();
}
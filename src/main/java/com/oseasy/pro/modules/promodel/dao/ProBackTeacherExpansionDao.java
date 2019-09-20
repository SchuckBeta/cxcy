package com.oseasy.pro.modules.promodel.dao;

import java.util.List;

import com.oseasy.act.modules.actyw.entity.ActYwEtAssignTaskVo;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.project.vo.ProjectExpVo;
import com.oseasy.pro.modules.promodel.entity.GContestUndergo;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;

/**
 * 导师信息表DAO接口
 * @author l
 * @version 2017-03-31
 */
@MyBatisDao
public interface ProBackTeacherExpansionDao extends CrudDao<BackTeacherExpansion> {
    List<ProjectExpVo> findProjectByTeacherId(String id);
    List<GContestUndergo> findGContestByTeacherId(String id);
    List<BackTeacherExpansion> getUserTaskList(ActYwEtAssignTaskVo actYwEtAssignTaskVo);
    List<BackTeacherExpansion> getUserToDoTaskList(ActYwEtAssignTaskVo actYwEtAssignTaskVo);
}
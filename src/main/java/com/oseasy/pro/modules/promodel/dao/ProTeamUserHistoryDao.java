package com.oseasy.pro.modules.promodel.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.workflow.vo.TeacherVo;
import com.oseasy.pro.modules.workflow.vo.TeamVo;
import com.oseasy.sys.modules.team.entity.TeamUserHistory;

/**
 * 团队历史纪录DAO接口.
 * @author chenh
 * @version 2017-11-14
 */
@MyBatisDao
public interface ProTeamUserHistoryDao extends CrudDao<TeamUserHistory> {
    List<TeamVo> findStudentByTeamId(@Param("teamId")String teamId);

    List<TeacherVo> findTeacherByTeamId(@Param("teamId")String teamId);
}
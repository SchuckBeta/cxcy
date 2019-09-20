/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/oseasy/initiate">JeeSite</a> All rights reserved.
 */
package com.oseasy.pro.modules.promodel.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.project.vo.ProjectExpVo;
import com.oseasy.pro.modules.promodel.entity.GContestUndergo;
import com.oseasy.sys.modules.team.entity.Team;

/**
 * 团队信息表DAO接口
 * @author zhangzheng
 * @version 2017-03-06
 */
@MyBatisDao
public interface ProTeamDao extends CrudDao<Team> {
    List<ProjectExpVo> findProjectByTeamId(@Param("teamId")String teamId);
    List<GContestUndergo> findGContestByTeamId(@Param("teamId")String teamId);
}

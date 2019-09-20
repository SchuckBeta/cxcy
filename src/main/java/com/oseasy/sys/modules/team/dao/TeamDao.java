/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/oseasy/initiate">JeeSite</a> All rights reserved.
 */
package com.oseasy.sys.modules.team.dao;

import java.util.List;
import java.util.Map;

import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.entity.TeamDetails;
import com.oseasy.sys.modules.team.entity.TeamUserRelation;

/**
 * 团队信息表DAO接口
 * @author zhangzheng
 * @version 2017-03-06
 */
@MyBatisDao
public interface TeamDao extends CrudDao<Team> {
	@Override
	@FindListByTenant
	public List<Team> findList(Team entity);
	@Override
	@InsertByTenant
	public int insert(Team entity);

	@FindListByTenant
	public Long getTeamCount(Team team);
	public void updateAllInfo(Team team);

	public List<Team> findTeamListByUserId(String  userid);

	public Integer hiddenDeleteWithNotify(@Param("tid")String tid,@Param("uid")String uid);
	public Integer checkTeamIsInProject(String tid);
	public Integer checkTeamIsInCyjd(String tid);
	public void auditOne(@Param("teamId")String teamId,@Param("res")String res,@Param("uid")String uid);
	public void auditAllBiuldOver(String uid);
	public void auditAllBiuldIng(String uid);
	public Team findTeamJoinInNums(String teamId);
	public List<Map<String,Object>> checkIsJoinInTUR(@Param("tds")List<Team> tds,@Param("uid")String uid);
	public List<Map<String,Object>> checkIsJoinInTeams(@Param("tds")List<Team> tds,@Param("uid")String uid);
	public TeamDetails findTeamDetails(String id);
	public List<TeamDetails> findTeamInfo(String id,String usertype);
	/**
	 * 根据团队中间表的teamId修改团队表的团队状态
	 * @param teamUserRelation
	 */
	public void updateTeamState(Team team);
	/**
	 * 添加团队中间表信息
	 * @param teamUserRelation
	 */
	public void saveTeamUserRelation(TeamUserRelation teamUserRelation);
	/**
	 * 根据teamId查询已加入团队的成员
	 * @param teamId
	 * @return
	 */
	public List<Team> findTeamUserName(String teamId);
/*	public int updateAddState(TeamUserRelation teamUserRelation);*/

	/**
	 * 获取可用团队.
	 * @param id 团队ID
	 * @return Team
	 */
	public Team getByUsable(String id);

	public List<TeamDetails> findTeamByTeamId(String id,String usertype);
	/**
	 * 根据当前登录用户的userid查询已创建的有效的团队
	 * @param curUser
	 * @return
	 */
	public Long countBuildByUserId(User curUser);
	public int findStuNumByTeamId(String teamId);
	public int findTe1NumByTeamId(String teamId);
	public int findTe2NumByTeamId(String teamId);
	@FindListByTenant
	public List<Team> selectTeamByName(String name);
	public List<Team> findListByCreatorId(Team team);
	public List<Team> findListByCreatorIdAndState(Team team);
	int findTeamNumByUserId(TeamUserRelation teamUserRelation);
     //根据两个人的userId，查找所在同一个团队数量
	int findTeamByUserId(@Param("user1Id")String user1Id,@Param("user2Id") String user2Id);
	@FindListByTenant
	List<Team> findInTeamList(Team team);

	List<Team> findTeamByIds(@Param("ids")List<String> ids);

	List<Team> findTeams(@Param("userId")String userId);

	Team getById(@Param("teamId")String teamId);

	String findFistTeacherByTeamId(@Param("teamId")String teamId);
}

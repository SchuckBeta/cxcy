package com.oseasy.pro.modules.project.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.oseasy.com.pcore.common.persistence.annotation.FindDictByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.auditstandard.vo.AsdVo;
import com.oseasy.pro.modules.project.entity.ProjectDeclare;
import com.oseasy.pro.modules.project.vo.ProjectDeclareListVo;
import com.oseasy.pro.modules.project.vo.ProjectExpVo;
import com.oseasy.pro.modules.workflow.vo.ExpProModelVo;
import com.oseasy.sys.modules.team.entity.Team;

/**
 * 项目申报DAO接口
 * @author 9527
 * @version 2017-03-11
 */
@MyBatisDao
public interface ProjectDeclareDao extends CrudDao<ProjectDeclare> {
	public Integer getByNumberAndId(@Param("number")String number,@Param("id")String id);
	public void modifyLeaderAndTeam(@Param("uid")String uid,@Param("tid")String tid,@Param("pid")String pid);
	public List<Map<String,String>> getTimeIndexSecondTabs(@Param("actywId") String actywId,@Param("uid") String uid);
	public List<Map<String,String>> getTimeIndexSecondTabsFromModel(@Param("actywId") String actywId,@Param("uid") String uid);
	public Map<String,BigDecimal> getProjectNumForAsdIndexFromModel(@Param("vo")AsdVo vo);
	public Map<String,BigDecimal> getPersonNumForAsdIndexFromModel(@Param("vo")AsdVo vo);
	@FindDictByTenant
	public List<Map<String,String>> getCurProProject();
	public List<ProjectDeclareListVo> getMyProjectListPlus(ProjectDeclareListVo vo);
	public Map<String,BigDecimal> getProjectNumForAsdIndex(String date);
	public Map<String,BigDecimal> getPersonNumForAsdIndex(String date);
    public List<Team> findTeams(@Param("userid") String userid,@Param("teamid") String teamid);
	public void updateStatus(ProjectDeclare projectDeclare);
	public void updateNumber(ProjectDeclare projectDeclare);
	public void updateMidCount(ProjectDeclare projectDeclare);
	public void updateMidScore(ProjectDeclare projectDeclare);
	public void updateFinalScore(ProjectDeclare projectDeclare);
	public void updateFinalResult(ProjectDeclare projectDeclare);
	public List<Map<String,String>> getMyProjectList(Map<String,Object> param);
	public int getMyProjectListCount(Map<String,Object> param);
	public List<Map<String,String>> getMyProjectListPersonPlus(List<String> ids);

	public List<Map<String,String>> getMyProjectListPersonOfTeam(@Param("ids") List<String> ids);

	public List<Map<String,String>> getMyProjectListPerson(List<String> ids);
	public List<Map<String,String>> getProjectAuditResult(String projectId);
	public List<Map<String,String>> getProjectAuditInfo(String projectId);
	public ProjectDeclare getVars(String id);  //addby zhangzheng 获得工作流需要查询的数据
	//public List<ProjectExpVo> getExps(String userId); //addby 张正 根据userId得到项目经历
	public List<ProjectExpVo> getExpsByUserId(String userId); //addby 张正 根据userId得到项目经历
	public List<Map<String,String>> findTeamStudentFromTUH(@Param("teamid")String teamid,@Param("proid")String proid);
	public List<Map<String,String>> findTeamStudentFromTUHByTeamId(@Param("teamid")String teamid);

	/**
	 * 新版团队获取老师列表
	 * @param teamId
	 * @return
	 */
	List<Map<String,String>> findTeamOfTeacherFromTUH(@Param("teamId")String teamId);

	public List<Map<String,String>> findTeamTeacherFromTUH(@Param("teamid")String teamid,@Param("proid")String proid);
	public List<Map<String,String>> findTeamStudent(String teamid);


	List<Map<String,String>> findTeamOfStudentByTeamId(@Param("teamId")String teamId);

	public List<Map<String,String>> findTeamTeacher(String teamid);
	public List<Map<String,String>> getProjectAnnounceByid(String id);
	public List<Map<String,String>> getValidProjectAnnounce();
	public List<Map<String,String>> getCurProjectInfo(String uid);
	public List<Map<String,String>> getCurProjectInfoByTeam(String tid);
	public Map<String,String> getLastProjectInfo(String uid);

	public Map<String,String> getProjectInfoByActyw(String projectId);
	public Map<String,String> getDcInfoByActyw(String projectId);
	public int getProjectByName(Map<String,String> param);

	public int findByTeamId(String teamId);//根据teamid查询项目是否正在进行中
	public ProjectDeclare getProjectByTeamId(String tid);
	public List<ProjectDeclare> getCurProjectInfoByLeader(String leaderId);
	public List<ProjectDeclare> getProjectByCdn(@Param("num") String num,@Param("name") String name,@Param("uid") String uid);
	public void myDelete(String  id);
	public ProjectDeclare getScoreConfigure(String id);

	ProjectDeclareListVo getProjectDeclareListVoById(String id);

	List<ProjectDeclare> findListByPro(ProjectDeclare projectDeclare);

	void deleteTeamUserHisByProId(@Param("id")String id);

	void deleteReportByProId(@Param("id")String id);

	ProjectDeclare getByNo(@Param("number") String number);

    /**
     * 导出.
     * @param projectDeclare
     * @return List
     */
    public List<ExpProModelVo> export(ProjectDeclare projectDeclare);

	ProjectDeclare getExcellentById(String id);
}
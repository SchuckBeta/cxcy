package com.oseasy.sys.modules.team.dao;

import java.util.List;
import java.util.Map;

import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;
import com.oseasy.sys.modules.team.entity.TeamUserHistory;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.com.pcore.modules.sys.entity.User;

/**
 * 团队历史纪录DAO接口.
 * @author chenh
 * @version 2017-11-14
 */
@MyBatisDao
public interface TeamUserHistoryDao extends CrudDao<TeamUserHistory> {

	public List<BackTeacherExpansion> getCanInviteTeamIdsByTeas(@Param("teas")List<BackTeacherExpansion> teas,@Param("teamLeaderId")String teamLeaderId);
	public List<StudentExpansion> getCanInviteTeamIdsByStus(@Param("stus")List<StudentExpansion> stus,@Param("teamLeaderId")String teamLeaderId);
	public List<User> getUserCurJoinByUsers(@Param("users")List<User> users);
	public List<User> getUserCurJoinByTeas(@Param("teas")List<BackTeacherExpansion> teas);
	public List<User> getUserCurJoinByStus(@Param("stus")List<StudentExpansion> stus);
	Integer getDoingCountByTeamId(String tid);
	int getBuildingCountByUserId(String uid);
	Integer countByCdn1(@Param("tuhs")List<TeamUserHistory> tuhs,@Param("proid")String proid,@Param("protype")String protype);
	Integer countByCdn2(@Param("tuhs")List<TeamUserHistory> tuhs,@Param("proid")String proid,@Param("protype")String protype,@Param("subtype")String subtype);
	Integer countByCdn3(@Param("tuhs")List<TeamUserHistory> tuhs,@Param("proid")String proid,@Param("protype")String protype,@Param("subtype")String subtype,@Param("year")String year);
	String getProIdByCdn(@Param("uid")String uid,@Param("protype")String protype,@Param("subtype")String subtype,@Param("finish")String finish);
	void insertAll(List<TeamUserHistory> tuhs);
	List<TeamUserHistory> getByProId(@Param("proId")String proId,@Param("teamId")String teamId);
	List<TeamUserHistory> getTeamUserHistoryFromTUR(@Param("tid")String tid,@Param("userType")String userType);
	void deleteByProId(String  proId);
	List<TeamUserHistory> getGcontestInfoByActywId(@Param("id")String id, @Param("actywId")String actywId, @Param("gcontesId")String gcontesId);

	int getWeightTotalByTeamId( @Param("teamId")String teamId, @Param("proId")String proId);

	void updateWeight(TeamUserHistory tur);
	void updateFinishAsClose(String proid);
	void updateFinishAsStart(String proid);
	void updateFinishAsSave(String proid);
	void updateDelByProid(String proid);

	Integer getDoOtherByTeamId(String tid);

	void deleteByProIdAndTeamId(@Param("proId")String proId, @Param("teamId")String teamId);

	void insertAllPw(List<TeamUserHistory> stus);

	List<Map<String,String>> findTeamStudent(@Param("proId")String proId, @Param("teamId")String teamId);

	List<Map<String,String>> findTeamTeacher(@Param("proId")String proId, @Param("teamId")String teamId);

	int getBuildingTeamCountByUserId(String uid);

	int getHisByTeamId(String teamId);
}
package com.oseasy.pro.modules.gcontest.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.auditstandard.vo.AsdVo;
import com.oseasy.pro.modules.gcontest.entity.GContest;
import com.oseasy.pro.modules.gcontest.vo.GContestListVo;

/**
 * 大赛信息DAO接口
 * @author zy
 * @version 2017-03-11
 */
@MyBatisDao
public interface GContestDao extends CrudDao<GContest> {
	public void modifyLeaderAndTeam(@Param("uid")String uid,@Param("tid")String tid,@Param("pid")String pid);
	public Map<String,Long> getProjectNumForAsdIndexFromModel(@Param("vo")AsdVo vo);
	public Map<String,Object> getPersonNumForAsdIndexFromModel(@Param("vo")AsdVo vo);
	public List<GContestListVo> getMyGcontestListPlus(GContestListVo vo);
	public Map<String,Long> getProjectNumForAsdIndex(String date);
	public Map<String,Object> getPersonNumForAsdIndex(String date);
    public void updateState(GContest gContest);

	public int getMyGcontestListCount(Map<String, Object> param);

	public List<Map<String, String>> getMyGcontestList(Map<String, Object> param);

	public List<Map<String, String>> getMyGcontestListPerson(List<String> ids);
	public List<Map<String, String>> getMyGcontestListPersonPlus(List<String> ids);
	
	public List<Map<String, String>> getGcontestList(Map<String, Object> param);
	
	public int getGcontestListCount(Map<String, Object> param);
	
	public List<Map<String, String>> getGcontestListPerson(List<String> ids);

	public List<GContest> getGcontestByName(String gContestName);
	
	public List<GContest> getGcontestInfo(String gcontestUserId);

	public GContest getLastGcontestInfo(String gcontestUserId);

	public List<Map<String, String>> getEndGcontestList(Map<String, Object> param);

	public int getEndGcontestListCount(Map<String, Object> param);

	public List<GContest> getGcontestByNameNoId(@Param("id") String id, @Param("pName")String pName);

	public int getGcontestChangeListCount(Map<String, Object> param);

	public List<Map<String, String>> getGcontestChangeList(Map<String, Object> param);

	int findGcontestByTeamId(String id);
	
	List<Map<String,String>> getAuditList(Map<String, Object> param);

	int getAuditListCount(Map<String, Object> param);

	int getTodoCount(Map<String, Object> param);

	int getHasdoCount(Map<String, Object> param);

	public GContest getScoreConfigure(String id);

	List<GContest> getGcontestInfoByActywId(@Param("userId") String userId,@Param("actywId")  String actywId);

	List<Map<String,String>>  getInGcontestNameList(@Param("userId") String userId );

	void deleteTeamUserHisByGConId(@Param("id")String id);

	GContest getExcellentById(String id);
}


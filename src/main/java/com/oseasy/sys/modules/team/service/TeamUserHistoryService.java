package com.oseasy.sys.modules.team.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.sys.modules.team.dao.TeamUserHistoryDao;
import com.oseasy.sys.modules.team.entity.TeamUserHistory;


/**
 * 团队历史纪录Service.
 * @author chenh
 * @version 2017-11-14
 */
@Service
@Transactional(readOnly = true)
public class TeamUserHistoryService extends CrudService<TeamUserHistoryDao, TeamUserHistory> {
	public int getBuildingCountByUserId(String uid) {
		//加入团队
		return dao.getBuildingTeamCountByUserId(uid);
		//项目未做完
		//return dao.getBuildingCountByUserId(uid);
	}

	public int getHisByTeamId(String teamId) {
		//开始做项目
		return dao.getHisByTeamId(teamId);
	}

	public List<TeamUserHistory> getByProId(String proId,String teamId) {
		return dao.getByProId(proId,teamId);
	}

	public TeamUserHistory get(String id) {
		return super.get(id);
	}

	public List<TeamUserHistory> findList(TeamUserHistory teamUserHistory) {
		return super.findList(teamUserHistory);
	}

	public Page<TeamUserHistory> findPage(Page<TeamUserHistory> page, TeamUserHistory teamUserHistory) {
		return super.findPage(page, teamUserHistory);
	}

	@Transactional(readOnly = false)
	public void save(TeamUserHistory teamUserHistory) {
		super.save(teamUserHistory);
	}

	@Transactional(readOnly = false)
	public void delete(TeamUserHistory teamUserHistory) {
		super.delete(teamUserHistory);
	}

	public List<TeamUserHistory> getGcontestInfoByActywId(String id, String actywId ,String gcontesId) {
		return dao.getGcontestInfoByActywId(id,actywId,gcontesId);
	}

	/*
	* teamId 团队id
	* proid 大赛或项目id
	* userId 创建者id
	* actywId 业务id
	* */



	public int getWeightTotalByTeamId(String teamId,String proId) {
		return dao.getWeightTotalByTeamId(teamId,proId);
	}
	@Transactional(readOnly = false)
	public void updateWeight(TeamUserHistory tur) {
		dao.updateWeight(tur);
	}

	public void updateFinishAsClose(String proid) {
		dao.updateFinishAsClose(proid);
	}

	public void updateDelByProid(String proid) {
		dao.updateDelByProid(proid);
	}

	public boolean getOtherHistory(String tid) {
		Integer count=dao.getDoOtherByTeamId(tid);
		if(count>0){
			return true;
		}
		return false;
	}

	public void saveAll(List<TeamUserHistory> stus) {
		dao.insertAll(stus);
	}

	public void insertAllPw(List<TeamUserHistory> stus) {
		dao.insertAllPw(stus);
	}

	public void deleteByProIdAndTeamId(String proId, String teamId) {
		dao.deleteByProIdAndTeamId(proId,teamId);
	}

	public List<Map<String,String>> findTeamStudent(String proId, String teamId) {
		return dao.findTeamStudent(proId,teamId);
	}

	public List<Map<String,String>> findTeamTeacher(String proId, String teamId) {
		return dao.findTeamTeacher(proId,teamId);
	}

	public void deleteByProId(String proId) {
		dao.deleteByProId(proId);
	}
}
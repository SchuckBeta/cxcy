package com.oseasy.sys.modules.team.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.sys.modules.sys.enums.TeamChangeEnum;
import com.oseasy.sys.modules.sys.enums.TeamDutyEnum;
import com.oseasy.sys.modules.team.dao.TeamUserChangeDao;
import com.oseasy.sys.modules.team.entity.TeamUserChange;
import com.oseasy.sys.modules.team.entity.TeamUserHistory;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 团队人员变更表Service.
 * @author 团队人员变更表
 * @version 2018-07-19
 */
@Service
@Transactional(readOnly = true)
public class TeamUserChangeService extends CrudService<TeamUserChangeDao, TeamUserChange> {
	@Autowired
	private TeamUserHistoryService teamUserHistoryService;

	public TeamUserChange get(String id) {
		return super.get(id);
	}

	public List<TeamUserChange> findList(TeamUserChange teamUserChange) {
		return super.findList(teamUserChange);
	}

	public Page<TeamUserChange> findPage(Page<TeamUserChange> page, TeamUserChange teamUserChange) {
		return super.findPage(page, teamUserChange);
	}

	@Transactional(readOnly = false)
	public void save(TeamUserChange teamUserChange) {
		super.save(teamUserChange);
	}

	@Transactional(readOnly = false)
	public void delete(TeamUserChange teamUserChange) {
		super.delete(teamUserChange);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(TeamUserChange teamUserChange) {
  	  dao.deleteWL(teamUserChange);
  	}

	public List<TeamUserChange> findListByTeamId(TeamUserChange teamUserChange) {
		return super.findList(teamUserChange);
	}

	//stus 传入学生list
	//teas 传入老师list
	//teamId 团队id
	//dutyId 团队负责人id
	@Transactional(readOnly = false)
	public void changeBySave(List<TeamUserHistory> stus, List<TeamUserHistory> teas, String teamId, String proId,String dutyId) {
		List<TeamUserHistory> oldTeamHistory=teamUserHistoryService.getByProId(proId,teamId);

		List<TeamUserHistory> oldStus=new ArrayList<TeamUserHistory>();
		List<TeamUserHistory> oldTeas=new ArrayList<TeamUserHistory>();
		for(TeamUserHistory teamUserHistory:oldTeamHistory){
			if("1".equals(teamUserHistory.getUserType())){
				if(teamUserHistory.getUser().getId().equals(dutyId)){
					teamUserHistory.setUserzz("0");
				}else{
					teamUserHistory.setUserzz("1");
				}
				oldStus.add(teamUserHistory);
			}else{
				oldTeas.add(teamUserHistory);
			}
		}
		addStuChange(stus ,oldStus,teamId,proId);
		addTeaChange(teas ,oldTeas,teamId,proId);
	}

	//stus 传入学生list
	//teas 传入老师list
	//teamId 团队id
	//dutyId 团队负责人id
	@Transactional(readOnly = false)
	public void changeBySaveInState(List<TeamUserHistory> stus, List<TeamUserHistory> teas, String teamId,String newTeamId, String proId,String dutyId) {
		List<TeamUserHistory> oldTeamHistory=teamUserHistoryService.getByProId(proId,teamId);

		List<TeamUserHistory> oldStus=new ArrayList<TeamUserHistory>();
		List<TeamUserHistory> oldTeas=new ArrayList<TeamUserHistory>();
		for(TeamUserHistory teamUserHistory:oldTeamHistory){
			if("1".equals(teamUserHistory.getUserType())){
				if(teamUserHistory.getUser().getId().equals(dutyId)){
					teamUserHistory.setUserzz("0");
				}else{
					teamUserHistory.setUserzz("1");
				}
				oldStus.add(teamUserHistory);
			}else{
				oldTeas.add(teamUserHistory);
			}
		}
		addStuChange(stus ,oldStus,newTeamId,proId);
		addTeaChange(teas ,oldTeas,newTeamId,proId);
	}

	//团队学生成员变更记录
	//方法需要重构(list对比 无法确认具体哪个字段有所改变)
    @Transactional(readOnly = false)
	private  void addStuChange(List<TeamUserHistory>stus,List<TeamUserHistory>oldStus,String teamId, String proId){
		//团队成员变更记录
		List<TeamUserChange> list=new ArrayList<TeamUserChange>();
		//添加人员 新团队
		for(TeamUserHistory teamUserHistory:stus){
			//找出 用户id和责任人 不全相等
			if(!oldStus.contains(teamUserHistory)){
				//默认为添加
				String changeType=TeamChangeEnum.ADD.getValue();
				//加入用户id一样 则是变更责任
				if(compareValue(teamUserHistory,oldStus)){
					//变更人
					changeType= TeamChangeEnum.MOD.getValue();
				}
				addChangeList(teamUserHistory,list,teamId,proId, changeType);
			}
		}
		//减少人员  gua api  Collections.
		for(TeamUserHistory inStuHistory:oldStus){
				//旧团队人员 id和职责不相同 //去除掉 旧团队人员 id相同的 已经修改为变更
			if(!compareValue(inStuHistory,stus)){
				addChangeList(inStuHistory,list,teamId,proId, TeamChangeEnum.DEL.getValue());
			}
		}
		if(StringUtil.checkNotEmpty(list)){
	        dao.savePl(list);
		}
	}

    @Transactional(readOnly = false)
	private boolean compareValue(TeamUserHistory value, List<TeamUserHistory> oldStus) {
		boolean isContain=false;
		for(TeamUserHistory teamUserHistory:oldStus){
			if(StringUtil.isEmpty(teamUserHistory.getUserId()) || StringUtil.isEmpty(value.getUserId())){
				isContain=false;
			}

			if(teamUserHistory.getUserId().equals(value.getUserId())){
				isContain=true;
				break;
			}
		}
		return isContain;
	}

    @Transactional(readOnly = false)
	private void addChangeList(TeamUserHistory teamUserHistory, List<TeamUserChange> list,String teamId,String proId,String operType) {
		TeamUserChange teamUserChange=new TeamUserChange();
		teamUserChange.setId(IdGen.uuid());
		teamUserChange.setTeamId(teamId);
		//1为添加 2为删除 3为修改
		teamUserChange.setOperType(operType);
		teamUserChange.setUserId(teamUserHistory.getUserId());
		//0为负责人 1为组员
		if("0".equals(teamUserHistory.getUserzz())){

			teamUserChange.setDuty(TeamDutyEnum.FZR.getValue());
		}else{
			teamUserChange.setDuty(TeamDutyEnum.ZY.getValue());
		}
		teamUserChange.setProId(proId);
		teamUserChange.setOperUserId(UserUtils.getUser().getId());
		teamUserChange.preInsert();
		list.add(teamUserChange);
	}

    @Transactional(readOnly = false)
	private  void addTeaChange(List<TeamUserHistory>teas,List<TeamUserHistory>oldTeas,String teamId, String proId){
		//添加人员
		for(TeamUserHistory teamUserHistory:teas){
			boolean isAddChange=true;
			for(TeamUserHistory inStuHistory:oldTeas){
				if(inStuHistory.getUser()!=null && inStuHistory.getUser().getId().equals(teamUserHistory.getUserId())){
					isAddChange=false;
				}
			}
			if(isAddChange){
				//添加新增成员
				TeamUserChange teamUserChange=new TeamUserChange();
				teamUserChange.setTeamId(teamId);
				//1为添加
				teamUserChange.setOperType("1");
				teamUserChange.setUserId(teamUserHistory.getUserId());
				//3为导师
				teamUserChange.setDuty(TeamDutyEnum.ZDDS.getValue());
				teamUserChange.setProId(proId);
				teamUserChange.setOperUserId(UserUtils.getUser().getId());
				save(teamUserChange);
			}
		}
		//减少人员
		for(TeamUserHistory inStuHistory:oldTeas){
			boolean isSubtractChange=true;
			for(TeamUserHistory teamUserHistory:teas){
				if(inStuHistory.getUser()!=null && inStuHistory.getUser().getId().equals(teamUserHistory.getUserId())){
					isSubtractChange=false;
				}
			}
			if(isSubtractChange){
				//减少成员
				TeamUserChange teamUserChange=new TeamUserChange();
				teamUserChange.setTeamId(teamId);
				//2为删除
				teamUserChange.setOperType("2");
				if(inStuHistory.getUser()!=null){
					teamUserChange.setUserId(inStuHistory.getUser().getId());
				}
				//0为负责人 1为组员
				teamUserChange.setDuty("3");
				teamUserChange.setOperUserId(UserUtils.getUser().getId());
				teamUserChange.setProId(proId);
				save(teamUserChange);
			}
		}
	}
}
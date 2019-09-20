package com.oseasy.sys.modules.sys.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 学生信息表Entity
 * @author zy
 * @version 2017-03-27
 */
public class StudentExpansion extends DataEntity<StudentExpansion> {
	private static final long serialVersionUID = 1L;
	protected User user;		// 人员基本信息ID
	protected String projectExperience;		// 项目经历
	protected String contestExperience;		// 大赛经历
	protected String award;		// 获奖作品
	protected String isOpen="1";		// 是否公开个人信息 0:不公开 1:公开
	protected Date enterdate;		// 入学时间
	protected Date graduation;		// 毕业时间
	protected String tClass;		// 班级
	protected String instudy;		// 在读学位
	protected Date temporaryDate;		// 休学时间
	protected Team team;
	protected String nowProject;   //是否在研项目  1是 0否
	protected String address ;
	protected String msg;
	protected String cycle;//学年制
	protected String isFront;//是否是前台查询 0或空：否 1：是
	protected String myFind;  //后台关键字  姓名 学院 专业 学号
	protected String keyWord;  //前台关键字 姓名 学院 专业

	protected String currState;
	protected String status; // 学生状态： 1在读2已毕业
	protected Boolean canInvite;//用于页面显示是否可被邀请
	protected String canInviteTeamIds;//用于列表页面显示可被邀请进入的团队id
	protected String teamLeaderId;//用于查询条件，团队负责人id
	protected String curJoin;// 当前在研，显示
	protected List<Map<String,String>> curJoinParam;//当前在研查询条件
	protected List<String> curJoinStr;//当前在研查询条件,接收页面传值

    public StudentExpansion(User user) {
        super();
        this.user = user;
    }

    public List<Map<String, String>> getCurJoinParam() {
		if (curJoinParam!=null) {
			return curJoinParam;
		}
		if (curJoinStr==null||curJoinStr.size()==0) {
			return null;
		}
		List<Map<String,String>> l=new ArrayList<Map<String,String>>();
		for(String s:curJoinStr) {
			if (StringUtil.isNotEmpty(s)) {
				Map<String,String> map=new HashMap<String,String>();
				String[] ss=s.split("-");
				map.put("pType", ss[0]);
				map.put("psType", ss[1]);
				l.add(map);
			}
		}
		if (l.size()==0) {
			return null;
		}
		return l;
	}

	public void setCurJoinParam(List<Map<String, String>> curJoinParam) {
		this.curJoinParam = curJoinParam;
	}

	public List<String> getCurJoinStr() {
		return curJoinStr;
	}

	public void setCurJoinStr(List<String> curJoinStr) {
		this.curJoinStr = curJoinStr;
	}

	public String getCurJoin() {
		return curJoin;
	}

	public void setCurJoin(String curJoin) {
		this.curJoin = curJoin;
	}

    private String topPrise; // 最高奖项



	public String getCycle() {
		return cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}

	public String getTeamLeaderId() {
		return teamLeaderId;
	}

	public void setTeamLeaderId(String teamLeaderId) {
		this.teamLeaderId = teamLeaderId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getCanInvite() {
		return canInvite;
	}

	public void setCanInvite(Boolean canInvite) {
		this.canInvite = canInvite;
	}

	public String getCanInviteTeamIds() {
		return canInviteTeamIds;
	}

	public void setCanInviteTeamIds(String canInviteTeamIds) {
		this.canInviteTeamIds = canInviteTeamIds;
	}

	public String getMyFind() {
		return myFind;
	}

	public String getCurrState() {
		return currState;
	}

	public void setCurrState(String currState) {
		this.currState = currState;
	}

	public void setMyFind(String myFind) {
		this.myFind = myFind;
	}

	public SimpleDateFormat getFormat() {
		return format;
	}

	public void setFormat(SimpleDateFormat format) {
		this.format = format;
	}

	public String getIsFront() {
		return isFront;
	}

	public void setIsFront(String isFront) {
		this.isFront = isFront;
	}

	public String getNowProject() {
		return nowProject;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setNowProject(String nowProject) {
		this.nowProject = nowProject;
	}


	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public StudentExpansion() {
		super();
	}

	public String gettClass() {
		return tClass;
	}

	public void settClass(String tClass) {
		this.tClass = tClass;
	}

	public StudentExpansion(String id) {
		super(id);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getProjectExperience() {
		return projectExperience;
	}

	public void setProjectExperience(String projectExperience) {
		this.projectExperience = projectExperience;
	}

	public String getContestExperience() {
		return contestExperience;
	}

	public void setContestExperience(String contestExperience) {
		this.contestExperience = contestExperience;
	}

	public String getAward() {
		return award;
	}

	public void setAward(String award) {
		this.award = award;
	}


	public String getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}


	public Date getEnterdate() {
		return enterdate;
	}

	public void setEnterdate(Date enterdate) {
		this.enterdate = enterdate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getGraduation() {
		return graduation;
	}
	public String getGraduationNow() {
		if (graduation!=null) {
			if (graduation.compareTo(new Date())>1) {
				return "未毕业";
			}else {
				return "已毕业";
			}
		}
		return null;
	}

	public void setGraduation(Date graduation) {
		this.graduation = graduation;
	}

	@Length(min=0, max=32, message="班级长度必须介于 0 和 32 之间")
	public String getTClass() {
		return tClass;
	}

	public void setTClass(String tClass) {
		this.tClass = tClass;
	}

	@Length(min=0, max=32, message="在读学位长度必须介于 0 和 32 之间")
	public String getInstudy() {
		return instudy;
	}

	public void setInstudy(String instudy) {
		this.instudy = instudy;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getTemporaryDate() {
		return temporaryDate;
	}
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	public String getTemporaryDateStr() {
		if (temporaryDate!=null) {
			String td = format.format(temporaryDate);
			return td;
		}else {
			return null;
		}
	}
	public String getGraduationStr() {
		if (graduation!=null) {
			String td = format.format(graduation);
			return td;
		}else {
			return null;
		}
	}

	public void setTemporaryDate(Date temporaryDate) {
		this.temporaryDate = temporaryDate;
	}

	public String getTopPrise() {
		return topPrise;
	}

	public void setTopPrise(String topPrise) {
		this.topPrise = topPrise;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
}
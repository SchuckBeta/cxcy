package com.oseasy.pro.modules.promodel.entity;

import java.util.ArrayList;
import java.util.List;

import com.oseasy.pro.modules.project.vo.ProjectExpVo;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;

/**
 * 学生信息表Entity
 * @author zy
 * @version 2017-03-27
 */
public class ProStudentExpansion extends StudentExpansion {
    private static final long serialVersionUID = 1L;
	private List<ProjectExpVo> projectList= new ArrayList<ProjectExpVo>();//查询项目经历
	private List<GContestUndergo> gContestList= new ArrayList<GContestUndergo>();;

	public ProStudentExpansion() {
		super();
	}

	public ProStudentExpansion(String id) {
		super(id);
	}

    public ProStudentExpansion(StudentExpansion studentExpansion) {
        this.createBy = studentExpansion.getCreateBy();
        this.createDate = studentExpansion.getCreateDate();
        this.currentUser = studentExpansion.getCurrentUser();
        this.delFlag = studentExpansion.getDelFlag();
        this.id = studentExpansion.getId();
        this.isNewRecord = studentExpansion.getIsNewRecord();
        this.page = studentExpansion.getPage();
        this.remarks = studentExpansion.getRemarks();
        this.updateBy = studentExpansion.getUpdateBy();
        this.updateDate = studentExpansion.getUpdateDate();
        this.user = studentExpansion.getUser();
        this.projectExperience = studentExpansion.getProjectExperience();
        this.contestExperience = studentExpansion.getContestExperience();
        this.award = studentExpansion.getAward();
        this.isOpen = studentExpansion.getIsOpen();
        this.enterdate = studentExpansion.getEnterdate();
        this.graduation = studentExpansion.getGraduation();
        this.tClass = studentExpansion.getTClass();
        this.instudy = studentExpansion.getInstudy();
        this.temporaryDate = studentExpansion.getTemporaryDate();
        this.team = studentExpansion.getTeam();
        this.nowProject = studentExpansion.getNowProject();
        this.address = studentExpansion.getAddress();
        this.msg = studentExpansion.getMsg();
        this.cycle = studentExpansion.getCycle();
        this.isFront = studentExpansion.getIsFront();
        this.myFind = studentExpansion.getMyFind();
        this.keyWord = studentExpansion.getKeyWord();
        this.currState = studentExpansion.getCurrState();
        this.status = studentExpansion.getStatus();
        this.canInvite = studentExpansion.getCanInvite();
        this.canInviteTeamIds = studentExpansion.getCanInviteTeamIds();
        this.teamLeaderId = studentExpansion.getTeamLeaderId();
        this.curJoin = studentExpansion.getCurJoin();
        this.curJoinParam = studentExpansion.getCurJoinParam();
        this.curJoinStr = studentExpansion.getCurJoinStr();
    }

    public List<ProjectExpVo> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<ProjectExpVo> projectList) {
        this.projectList = projectList;
    }

    public List<GContestUndergo> getgContestList() {
        return gContestList;
    }
    public void setgContestList(List<GContestUndergo> gContestList) {
        this.gContestList = gContestList;
    }
}
package com.oseasy.pro.modules.gcontest.vo;

import java.util.List;
import java.util.Map;

import com.oseasy.pro.modules.gcontest.entity.GContest;
import com.oseasy.pro.modules.project.entity.ProjectDeclare;
import com.oseasy.pro.modules.project.entity.ProjectPlan;

/**
 * 项目申报Vo
 * @author 9527
 * @version 2017-03-11
 */
public class GContestVo  {
	private GContest gcontest;
	private List<ProjectPlan> plans;
	private List<Map<String,String>> teamStudent;
	private List<Map<String,String>> teamTeacher;
	private Map<String,String> auditInfo;
	private List<Map<String,String>> fileInfo;


	public GContest getGcontest() {
		return gcontest;
	}

	public void setGcontest(GContest gcontest) {
		this.gcontest = gcontest;
	}

	public List<Map<String, String>> getFileInfo() {
		return fileInfo;
	}

	public void setFileInfo(List<Map<String, String>> fileInfo) {
		this.fileInfo = fileInfo;
	}

	public Map<String, String> getAuditInfo() {
		return auditInfo;
	}

	public void setAuditInfo(Map<String, String> auditInfo) {
		this.auditInfo = auditInfo;
	}



	public List<Map<String, String>> getTeamStudent() {
		return teamStudent;
	}

	public void setTeamStudent(List<Map<String, String>> teamStudent) {
		this.teamStudent = teamStudent;
	}

	public List<Map<String, String>> getTeamTeacher() {
		return teamTeacher;
	}

	public void setTeamTeacher(List<Map<String, String>> teamTeacher) {
		this.teamTeacher = teamTeacher;
	}

	public List<ProjectPlan> getPlans() {
		return plans;
	}

	public void setPlans(List<ProjectPlan> plans) {
		this.plans = plans;
	}
}
package com.oseasy.act.modules.actyw.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.oseasy.com.pcore.common.persistence.DataExtEntity;

import java.util.List;

/**
 * 专家指派任务Entity.
 * @author zy
 * @version 2019-01-03
 */
public class ActYwEtAssignTaskVo extends DataExtEntity<ActYwEtAssignTaskVo> {

	private static final long serialVersionUID = 1L;
	private String actywId;		// 项目类别编号
	private String actywName;		// 项目类别名称
	private String gnodeId;		// 审核节点编号
	private String gnodeName;		// 审核节点名称
	private String todoNum;		// 	待分配数
	private String toauditNum;   // 待审核数
	private String hasNum;		// 	已经审核数
	private String expertNum;		// 需求专家数

	private String proType;		// 类型（项目或者大赛）
	//查询专家任务参数
	private String userId;		// 专家userId

	private List<String> proIds; //项目id列表
	//查询学院
	private String officeId;		// 学院id
	//查询关键字
	private String queryStr;		// 关键字

	//查询关键字
	private String expertType;		// 查询类型 专家来源

	private String roleId;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public List<String> getProIds() {
		return proIds;
	}

	public void setProIds(List<String> proIds) {
		this.proIds = proIds;
	}

	public ActYwEtAssignTaskVo() {
		super();
	}

	public ActYwEtAssignTaskVo(String id){
		super(id);
	}

	public String getActywId() {
		return actywId;
	}

	public void setActywId(String actywId) {
		this.actywId = actywId;
	}

	public String getActywName() {
		return actywName;
	}

	public void setActywName(String actywName) {
		this.actywName = actywName;
	}

	public String getGnodeId() {
		return gnodeId;
	}

	public void setGnodeId(String gnodeId) {
		this.gnodeId = gnodeId;
	}

	public String getGnodeName() {
		return gnodeName;
	}

	public void setGnodeName(String gnodeName) {
		this.gnodeName = gnodeName;
	}

	public String getTodoNum() {
		return todoNum;
	}

	public void setTodoNum(String todoNum) {
		this.todoNum = todoNum;
	}

	public String getHasNum() {
		return hasNum;
	}

	public void setHasNum(String hasNum) {
		this.hasNum = hasNum;
	}

	public String getToauditNum() {
		return toauditNum;
	}

	public void setToauditNum(String toauditNum) {
		this.toauditNum = toauditNum;
	}

	public String getExpertNum() {
		return expertNum;
	}

	public void setExpertNum(String expertNum) {
		this.expertNum = expertNum;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProType() {
		return proType;
	}

	public void setProType(String proType) {
		this.proType = proType;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	@Override
	public String getQueryStr() {
		return queryStr;
	}

	@Override
	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public String getExpertType() {
		return expertType;
	}

	public void setExpertType(String expertType) {
		this.expertType = expertType;
	}
}
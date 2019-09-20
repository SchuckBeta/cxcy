package com.oseasy.pro.modules.auditstandard.vo;

import java.util.List;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.tool.process.GnodeGsep;
import com.oseasy.act.modules.actyw.tool.process.GnodeGstree;

public class AsdVo {

	private String type;// 类型：1项目，2-大赛
	private String subType;// 类型：大创项目、小创、互联网+、创青春
	private String name;// 名称
	private String year;// 项目年份
	private String dataYear;// 数据年份 申报时间
	private String applyNum;// 申报人数
	private String innovateNum;// 创新训练项目数
	private String innovateBusNum;// 创业训练项目数
	private String businessNum;// 创业项目数
	private String teacherNum;// 导师数

	private ActYw actYw;// 项目流程
	private List<AsdYwGnode> asdYwGnodes;// 流程图结果
	//private List<GnodeGsep> gnodeGseps;//流程图链表结果
	private GnodeGstree gstree;//流程图链表结果
	public String getDataYear() {
		return dataYear;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public void setDataYear(String dataYear) {
		this.dataYear = dataYear;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ActYw getActYw() {
		return actYw;
	}

	public void setActYw(ActYw actYw) {
		this.actYw = actYw;
	}

	public List<AsdYwGnode> getAsdYwGnodes() {
		return asdYwGnodes;
	}

	public void setAsdYwGnodes(List<AsdYwGnode> asdYwGnodes) {
		this.asdYwGnodes = asdYwGnodes;
	}

    public GnodeGstree getGstree() {
        return gstree;
    }

    public void setGstree(GnodeGstree gstree) {
        this.gstree = gstree;
    }

    public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getApplyNum() {
		return applyNum;
	}

	public void setApplyNum(String applyNum) {
		this.applyNum = applyNum;
	}

	public String getInnovateBusNum() {
		return innovateBusNum;
	}

	public void setInnovateBusNum(String innovateBusNum) {
		this.innovateBusNum = innovateBusNum;
	}

	public String getInnovateNum() {
		return innovateNum;
	}

	public void setInnovateNum(String innovateNum) {
		this.innovateNum = innovateNum;
	}

	public String getBusinessNum() {
		return businessNum;
	}

	public void setBusinessNum(String businessNum) {
		this.businessNum = businessNum;
	}

	public String getTeacherNum() {
		return teacherNum;
	}

	public void setTeacherNum(String teacherNum) {
		this.teacherNum = teacherNum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}

package com.oseasy.pw.modules.pw.entity;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.sys.modules.team.entity.Team;

/**
 * 入驻申报详情Entity.
 * @author chenh
 * @version 2017-11-26
 */
public class PwEnterDetail extends DataEntity<PwEnterDetail> {

	private static final long serialVersionUID = 1L;
	private String eid;   // 申报表id
	private String type;		// 类型(PwEnterType)：0、团队；1、项目；2、企业
	private String rid;   // 团队、项目、企业 id 业务编号

	private PwProject project;   // 项目
	private PwCompany pwCompany;   // 企业
	@JsonIgnore
	private PwEnter pwEnter;   // 入驻


    private Team team;   // 团队
    @Transient
    private String snames;  //项目组成员
    @Transient
    private String tnames;  //指导老师


	public PwEnterDetail() {
		super();
	}

	public PwEnterDetail(String id){
		super(id);
	}

	public PwEnterDetail(Team team) {
    	super();
  	}

	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

    public PwProject getProject() {
        return project;
    }

    public void setProject(PwProject project) {
        this.project = project;
    }

    public PwEnter getPwEnter() {
        return pwEnter;
    }

    public void setPwEnter(PwEnter pwEnter) {
        this.pwEnter = pwEnter;
    }

    public PwCompany getPwCompany() {
        return pwCompany;
    }

    public void setPwCompany(PwCompany pwCompany) {
        this.pwCompany = pwCompany;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getSnames() {
        return snames;
    }

    public void setSnames(String snames) {
        this.snames = snames;
    }

    public String getTnames() {
        return tnames;
    }

    public void setTnames(String tnames) {
        this.tnames = tnames;
    }
}
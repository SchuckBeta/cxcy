package com.oseasy.pie.modules.impdata.entity;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;
import com.oseasy.pro.modules.workflow.entity.ProModelTlxy;

/**
 * 导入项目错误数据表Entity.
 * @author 奔波儿灞
 * @version 2018-03-13
 */
public class ProModelError extends DataEntity<ProModelError> implements IitCheckEetyExt{

	private static final long serialVersionUID = 1L;
	private String impId;		// 导入信息表主键
	private String office;		// 学院
	private String name;		// 项目名称
	private String number;		// 项目编号
	private String type;		// 项目类型
	private String leader;		// 负责人姓名
	private String no;		// 负责人学号
	private String mobile;		// 负责人电话
	private String email;		// 负责人邮箱
	private String school;		// 学校
	private String profes;		// 负责人专业
	private String members;		// 团队成员及学号
	private String teachers;		// 指导教师
    private String businfos;        // 工商信息
	private String teaNo;		// 指导教师工号
	private String teaTitle;		// 指导教师职称
	private String result;//项目结果
	private String year;//项目年份
    private String introduction;        // 项目简介
    private String hasfile;     // 是否有附件
    private boolean validName;//标识name是否有效
    private String level;//参赛组别
    private String stage;//项目阶段
    private String projectSource;//项目来源
    private String teamId;//团队ID
    private String teamName;//团队名称

    @JsonIgnore
    private Office curOffice;

    private ProModelTlxy pmTlxy;//铜陵学院实体
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getBusinfos() {
        return businfos;
    }

    public void setBusinfos(String businfos) {
        this.businfos = businfos;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public boolean isValidName() {
        return validName;
    }

    public void setValidName(boolean validName) {
        this.validName = validName;
    }

    public ProModelError() {
		super();
	}

	public ProModelError(String id){
		super(id);
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getHasfile() {
        return hasfile;
    }

    public void setHasfile(String hasfile) {
        this.hasfile = hasfile;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public ProModelTlxy getPmTlxy() {
        return pmTlxy;
    }

    public void setPmTlxy(ProModelTlxy pmTlxy) {
        this.pmTlxy = pmTlxy;
    }

    @Length(min=1, max=64, message="导入信息表主键长度必须介于 1 和 64 之间")
	public String getImpId() {
		return impId;
	}

	public void setImpId(String impId) {
		this.impId = impId;
	}

	public Office getCurOffice() {
        return curOffice;
    }

    public void setCurOffice(Office curOffice) {
        this.curOffice = curOffice;
    }

    @Length(min=0, max=128, message="学院长度必须介于 0 和 128 之间")
	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	@Length(min=0, max=128, message="项目名称长度必须介于 0 和 128 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=0, max=128, message="项目编号长度必须介于 0 和 128 之间")
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Length(min=0, max=128, message="项目类型长度必须介于 0 和 128 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getProjectSource() {
        return projectSource;
    }

    public void setProjectSource(String projectSource) {
        this.projectSource = projectSource;
    }

    @Length(min=0, max=128, message="负责人姓名长度必须介于 0 和 128 之间")
	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	@Length(min=0, max=128, message="负责人学号长度必须介于 0 和 128 之间")
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	@Length(min=0, max=128, message="负责人电话长度必须介于 0 和 128 之间")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Length(min=0, max=128, message="负责人邮箱长度必须介于 0 和 128 之间")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Length(min=0, max=128, message="负责人专业长度必须介于 0 和 128 之间")
	public String getProfes() {
		return profes;
	}

	public void setProfes(String profes) {
		this.profes = profes;
	}

	@Length(min=0, max=256, message="团队成员及学号长度必须介于 0 和 256 之间")
	public String getMembers() {
		return members;
	}

	public void setMembers(String members) {
		this.members = members;
	}

	@Length(min=0, max=128, message="指导教师长度必须介于 0 和 128 之间")
	public String getTeachers() {
		return teachers;
	}

	public void setTeachers(String teachers) {
		this.teachers = teachers;
	}

	@Length(min=0, max=128, message="指导教师工号长度必须介于 0 和 128 之间")
	public String getTeaNo() {
		return teaNo;
	}

	public void setTeaNo(String teaNo) {
		this.teaNo = teaNo;
	}

	@Length(min=0, max=128, message="指导教师职称长度必须介于 0 和 128 之间")
	public String getTeaTitle() {
		return teaTitle;
	}

	public void setTeaTitle(String teaTitle) {
		this.teaTitle = teaTitle;
	}

}
package com.oseasy.pro.modules.promodel.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.tool.apply.IActYw;
import com.oseasy.act.modules.actyw.tool.apply.IAmap;
import com.oseasy.act.modules.actyw.tool.apply.IApply;
import com.oseasy.act.modules.actyw.tool.apply.IAprop;
import com.oseasy.act.modules.actyw.tool.apply.IAsup;
import com.oseasy.act.modules.actyw.tool.apply.IAurl;
import com.oseasy.act.modules.actyw.tool.apply.IGnode;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.pcore.common.persistence.AttachMentEntity;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.vo.IAuser;
import com.oseasy.pro.modules.cert.vo.SysCertInsVo;
import com.oseasy.pro.modules.project.entity.ProjectPlan;
import com.oseasy.pro.modules.workflow.IWorkFety;
import com.oseasy.pro.modules.workflow.IWorkRes;
import com.oseasy.pro.modules.workflow.impl.WorkFetyPm;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.entity.TeamUserHistory;

/**
 * proModelEntity.
 *
 * @author zy
 * @version 2017-07-13
 */
public class ProModel extends WorkFetyPm<ProModel> implements IWorkRes, IWorkFety, IApply, Serializable {

    private static final long serialVersionUID = 1L;
    public static final String JK_PROMODEL = "proModel";
    private String gnodeId;//页面传参
    private String actYwId;        //业务id
    private String pName;        // 项目名称
	private String shortName;        // 项目简称
    private String declareId;        // 申报人ID
    private User deuser;
    private String proType;        //项目 大赛
    private String type;                 // 大创 小创    互联网+ 创青春
    private String proCategory;        //项目类别：创新,创业//项目类别：project_type；大赛类别：competition_net_type
    private String level;                // 大赛级别
    private String introduction;        // 项目简介
    private String projectSource;        // 项目来源
    private String financingStat;        // 融资情况 0 未，1 100w一下 2 100w以上
	private String endGnodeId;        // 最终结果节点id
	private String endGnodeVesion;        // 最终结果节点批次
    private String finalStatus;        // 最终结果
    private String teamId;        // 团队ID
    private Team team;        // 团队ID
    private String procInsId;        // 流程实例id
    private String proMark;        // 项目标识
    private String source;        // source
    private String competitionNumber;        // 编号
    private String grade;        // 大赛和项目结果
    private String gScore;        // 评分
    private Date subTime;        // 提交时间
    private String subStatus;//提交状态 0-未提交，1-已提交
    private String stage;//项目阶段
    private String impdata;//是否导入的数据1-是，0-否
    private ActYw actYw;

	private String rnames;  //角色名称
	private String snames;  //项目组成员
	private String tnames;  //指导老师
	private String sourceName;     //来源名称

	private Date beginDate;        //开始时间
	private Date endDate;            //结束时间

	private Map<String,String> auditMap;  //审核参数

//    private String projectLevel;      //项目级别
    private String projectLevelDict;//项目级别对应字典表值

	private String state;      //状态是否结束 1是结束 0是未结束
	private String delegateState;    //是否委派 1是已经委派 0是未委派

	private String isAll;//是否所有数据1、是，0、否

	private String isSend;//是否推送省平台1、是，0、否
	private String step;        // 发展阶段1：初始 2：发展 3：成熟
	/**
	 * 参赛赛道:1.高教主赛道2.青年红色筑梦之旅3.职教赛道
	 */
	private String gcTrack;
	/**
	 * 高校成果转化：1.是，0否
	 */
	private String achievementTf;
	/**
	 * 成果完成人或所有人：0否，1是
	 */
	private String achievementUser;
	/**
	 * 是否师生共创：0否，1是
	 */
	private String coCreation;
	/**
	 * 大赛项目进展：0.创意计划阶段1.已注册公司运营2.已注册社会组织'
	 */
	private String ideaStage;
	/**
	 * 学校名
	 */
	private String schoolName;
	/**
	 * 学校性质
	 */
	private String schoolType;
	/**
	 * 学校地区
	 */
	private String schoolCity;
	/**
	 * 所属领域
	 */
	private String belongsField;

	/**
	 * 开户银行
	 */
	private String bankName;

	/**
	 * 开户账号
	 */
	private String bankNumber;
	/**
	 * 排名
	 */
	private Integer ranking;

    private AttachMentEntity attachMentEntity; //附件
    private List<TeamUserHistory> teacherList = Lists.newArrayList();
    private List<TeamUserHistory> studentList = Lists.newArrayList();
    private List<TeamUserHistory> teamUserHistoryList = Lists.newArrayList(); //团队信息

    private List<SysAttachment> fileInfo;
    private String logoUrl;//页面传值项目logo url
    private SysAttachment logo;//logo图片文件
	private List<ProjectPlan> planList;  //任务分工
    private List<String> ids;
    private String queryStr;  //模糊查询字段
    private String year; //项目年份
	private String finalResult;  //项目结果
	private String taskAssigns;//指派人名称
	private String hasAssigns;//是否指派
	private String result;        // 大赛和项目结果

	private String toGnodeId;//变更流程到节点id

	//添加模板查询方式
	private List<String> proCategoryList;

	private List<String> projectLevelList;

	private List<String> officeIdList ;


	private String officeId;		//查询条件 学院id
	@Transient
    private String members;     // 团队成员及学号
	@Transient
	private String teachers;        // 指导教师
	@Transient
	private String businfos;        // 工商信息

	private String roleFlag;

	private String professor;

	private String cityName;

	private String provId;

	public String getProvId() {
		return provId;
	}

	public void setProvId(String provId) {
		this.provId = provId;
	}

	public String getGnodeIdList() {
		return gnodeIdList;
	}

	public void setGnodeIdList(String gnodeIdList) {
		this.gnodeIdList = gnodeIdList;
	}

	@Override
	public String getTenantId() {
		return tenantId;
	}

	@Override
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	private String gnodeIdList;

	private String tenantId;


	public String getIsSend() {
		return isSend;
	}

	public void setIsSend(String isSend) {
		this.isSend = isSend;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public ProModel(List<String> ids) {
        super();
        this.ids = ids;
    }

    public ProModel(List<String> ids, String actYwId) {
        super(ids, actYwId);
        this.actYwId = actYwId;
        this.ids = ids;
    }

	public String getDelegateState() {
		return delegateState;
	}

	public void setDelegateState(String delegateState) {
		this.delegateState = delegateState;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getTeachers() {
        return teachers;
    }

    public void setTeachers(String teachers) {
        this.teachers = teachers;
    }

    public String getBusinfos() {
        return businfos;
    }

    public void setBusinfos(String businfos) {
        this.businfos = businfos;
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

	public List<ProjectPlan> getPlanList() {
		return planList;
	}

	public void setPlanList(List<ProjectPlan> planList) {
		this.planList = planList;
	}

	public Map<String, String> getAuditMap() {
		return auditMap;
	}

	public void setAuditMap(Map<String, String> auditMap) {
		this.auditMap = auditMap;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getImpdata() {
		return impdata;
	}

	public void setImpdata(String impdata) {
		this.impdata = impdata;
	}

    public String getIsAll() {
        return isAll;
    }

    public void setIsAll(String isAll) {
        this.isAll = isAll;
    }

    public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getToGnodeId() {
		return toGnodeId;
	}

	public void setToGnodeId(String toGnodeId) {
		this.toGnodeId = toGnodeId;
	}

	public String getEndGnodeVesion() {
		return endGnodeVesion;
	}

	public void setEndGnodeVesion(String endGnodeVesion) {
		this.endGnodeVesion = endGnodeVesion;
	}

	public String getSubStatus() {
		return subStatus;
	}

	public void setSubStatus(String subStatus) {
		this.subStatus = subStatus;
	}

	public String getHasAssigns() {
		return hasAssigns;
	}

	public void setHasAssigns(String hasAssigns) {
		this.hasAssigns = hasAssigns;
	}

	public String getTaskAssigns() {
		return taskAssigns;
	}

	public void setTaskAssigns(String taskAssigns) {
		this.taskAssigns = taskAssigns;
	}

	private List<SysCertInsVo> scis;

	public List<SysCertInsVo> getScis() {
		return scis;
	}

	public void setScis(List<SysCertInsVo> scis) {
		this.scis = scis;
	}
	public String getEndGnodeId() {
		return endGnodeId;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public void setEndGnodeId(String endGnodeId) {
		this.endGnodeId = endGnodeId;
	}

	public String getGnodeId() {
		return gnodeId;
	}

	public void setGnodeId(String gnodeId) {
		this.gnodeId = gnodeId;
	}

	public List<TeamUserHistory> getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(List<TeamUserHistory> teacherList) {
        this.teacherList = teacherList;
    }

    public List<TeamUserHistory> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<TeamUserHistory> studentList) {
        this.studentList = studentList;
    }

	public List<TeamUserHistory> getTeamUserHistoryList() {
		return teamUserHistoryList;
	}

	public void setTeamUserHistoryList(List<TeamUserHistory> teamUserHistoryList) {
		this.teamUserHistoryList = teamUserHistoryList;
	}

	public HashMap<String,Object> getVars() {
		HashMap<String,Object> vars=new HashMap<String, Object>();
		vars.put("number",competitionNumber);  //编号
		vars.put("id",id);  //id
		vars.put("name",pName);              //项目名称
		vars.put("type",type);               //项目类型
		vars.put("proCategory",proCategory);               //项类别
		vars.put("level",level);             //项目级别
		vars.put("financingStat",financingStat);  //融资情况
		vars.put("projectSource",projectSource);  //项目来源
		vars.put("teamId",teamId);  //项目来源
		vars.put("finalStatus",finalStatus);             //项目评级
		vars.put("actYwId",actYwId);             //项目评级
		vars.put("declareId",declareId);     //大赛申报人id
		if ((deuser != null) && (deuser.getOffice() != null)) {
	    vars.put("dofficeName",deuser.getOffice().getName());     //
		}
		return vars;
	}

	public Team getTeam() {
    return team;
  }

  public void setTeam(Team team) {
    this.team = team;
  }

  public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public User getDeuser() {
    return deuser;
  }

  	public void setDeuser(User deuser) {
    this.deuser = deuser;
  }

	public String getFinalStatus() {
		return finalStatus;
	}

	public void setFinalStatus(String finalStatus) {
		this.finalStatus = finalStatus;
	}

	public String getProType() {
		return proType;
	}

	public void setProType(String proType) {
		this.proType = proType;
	}

	public Date getSubTime() {
		return subTime;
	}

	public void setSubTime(Date subTime) {
		this.subTime = subTime;
	}

	public String getProCategory() {
		return proCategory;
	}

	public void setProCategory(String proCategory) {
		this.proCategory = proCategory;
	}

	public AttachMentEntity getAttachMentEntity() {
		return attachMentEntity;
	}

	public void setAttachMentEntity(AttachMentEntity attachMentEntity) {
		this.attachMentEntity = attachMentEntity;
	}

	public ProModel() {
		super();
	}

	public ProModel(String id) {
		super(id);
	}

	public String getProjectSource() {
		return projectSource;
	}

	public void setProjectSource(String projectSource) {
		this.projectSource = projectSource;
	}

	@Length(min=0, max=128, message="项目名称长度必须介于 0 和 128 之间")
	public String getPName() {
		return pName;
	}

	public void setPName(String pName) {
		this.pName = pName;
	}

	@Length(min=0, max=64, message="申报人ID长度必须介于 0 和 64 之间")
	public String getDeclareId() {
		return declareId;
	}

	public void setDeclareId(String declareId) {
		this.declareId = declareId;
	}

	@Length(min=0, max=20, message="项目类型长度必须介于 0 和 20 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getgScore() {
		return gScore;
	}

	public void setgScore(String gScore) {
		this.gScore = gScore;
	}

	public String getActYwId() {
		return actYwId;
	}

	public void setActYwId(String actYwId) {
		this.actYwId = actYwId;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	@Length(min=0, max=20, message="项目级别长度必须介于 0 和 20 之间")
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	@Length(min=0, max=20, message="融资情况 0 未，1 100w一下 2 100w以上长度必须介于 0 和 20 之间")
	public String getFinancingStat() {
		return financingStat;
	}

	public void setFinancingStat(String financingStat) {
		this.financingStat = financingStat;
	}

	@Length(min=0, max=64, message="团队ID长度必须介于 0 和 64 之间")
	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	@Length(min=0, max=64, message="流程实例id长度必须介于 0 和 64 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}

	@Length(min=0, max=64, message="项目标识长度必须介于 0 和 64 之间")
	public String getProMark() {
		return proMark;
	}

	public void setProMark(String proMark) {
		this.proMark = proMark;
	}

	@Length(min=0, max=64, message="source长度必须介于 0 和 64 之间")
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Length(min=1, max=64, message="大赛编号长度必须介于 1 和 64 之间")
	public String getCompetitionNumber() {
		return competitionNumber;
	}

	public void setCompetitionNumber(String competitionNumber) {
		this.competitionNumber = competitionNumber;
	}

	@Length(min=0, max=20, message="大赛结果长度必须介于 0 和 20 之间")
	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getGScore() {
		return gScore;
	}

	public void setGScore(String gScore) {
		this.gScore = gScore;
	}

  public ActYw getActYw() {
    return actYw;
  }

  public void setActYw(ActYw actYw) {
    this.actYw = actYw;
  }

	public List<SysAttachment> getFileInfo() {
		return fileInfo;
	}

    public void setFileInfo(List<SysAttachment> fileInfo) {
        this.fileInfo = fileInfo;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public SysAttachment getLogo() {
        return logo;
    }

    public void setLogo(SysAttachment logo) {
        this.logo = logo;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public String getQueryStr() {
        return queryStr;
    }

    public void setQueryStr(String queryStr) {
        this.queryStr = queryStr;
    }

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getFinalResult() {
		return finalResult;
	}

	public void setFinalResult(String finalResult) {
		this.finalResult = finalResult;
	}

//	public String getProjectLevel() {
//		return projectLevel;
//	}
//
//	public void setProjectLevel(String projectLevel) {
//		this.projectLevel = projectLevel;
//	}


	public List<String> getProCategoryList() {
		return proCategoryList;
	}

	public void setProCategoryList(List<String> proCategoryList) {
		this.proCategoryList = proCategoryList;
	}

	public List<String> getProjectLevelList() {
		return projectLevelList;
	}

	public void setProjectLevelList(List<String> projectLevelList) {
		this.projectLevelList = projectLevelList;
	}

	public List<String> getOfficeIdList() {
		return officeIdList;
	}

	public void setOfficeIdList(List<String> officeIdList) {
		this.officeIdList = officeIdList;
	}

	public String getProjectLevelDict() {
		return projectLevelDict;
	}

	public void setProjectLevelDict(String projectLevelDict) {
		this.projectLevelDict = projectLevelDict;
	}

	/* (non-Javadoc)
		 * @see com.oseasy.pro.modules.workflow.IWorkRes#getOfficeName()
		 */
    @Override
    public String getOfficeName() {
        if((this.getDeuser() == null) || (this.getDeuser().getOffice() == null)){
            return "";
        }
        return this.getDeuser().getOffice().getName();
    }


    /**********************************************************************************
     * IApply流程接口实现方法
     **********************************************************************************/
    @JsonIgnore
    @Override
    public String getIactYwId() {
        if(this.actYw != null){
            return this.actYw.getId();
        }
        return null;
    }

    @Override
    public IAsup iasup() {
        return null;
    }

    @Override
    public IAsup iasup(IAsup iasup) {
        return null;
    }

    @JsonIgnore
    @Override
    public IActYw iactYw() {
        return this.actYw;
    }

    @JsonIgnore
    @Override
    public ActYw iactYw(IActYw actYw) {
        this.actYw = (ActYw) actYw;
        return this.actYw;
    }

    @JsonIgnore
    @Override
    public User iauser() {
        return this.deuser;
    }

    @JsonIgnore
    @Override
    public String iauserId() {
        if(this.deuser != null){
            return this.deuser.getId();
        }
        return null;
    }

    @JsonIgnore
    @Override
    public User iauser(IAuser auser) {
        this.deuser = (User) auser;
        return this.deuser;
    }

    @Override
    public String getIid() {
        return this.id;
    }

    @JsonIgnore
    @Override
    public String getIname() {
        return this.pName;
    }

    @JsonIgnore
    @Override
    public IGnode ignode() {
        return null;
    }

    @JsonIgnore
    @Override
    public ActYwGnode ignode(IGnode gnode) {
        return null;
    }

    @JsonIgnore
    @Override
    public String getIgnodeId() {
        return this.gnodeId;
    }

    @JsonIgnore
    @Override
    public String getIprocInsId() {
        return this.procInsId;
    }

    @JsonIgnore
    @Override
    public String iprocInsId(String piid) {
        this.procInsId = piid;
        return this.procInsId;
    }

    @JsonIgnore
    @Override
    public Map<String, Object> ivars() {
        return getVars();
    }

    @Override
    public String iid(String iid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IAprop iaprop() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IAprop iaprop(IAprop iaprop) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IAmap getIamap() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IAmap iamap(IAmap iamap) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IAurl getIaurl() {
        return new IAurl();
    }

    /* (non-Javadoc)
     * @see com.oseasy.com.pcore.common.persistence.IOrby#ors()
     */
    @Override
    public Map<String, String> ors() {
        // TODO Auto-generated method stub
        return null;
    }

	public String getRnames() {
		return rnames;
	}

	public void setRnames(String rnames) {
		this.rnames = rnames;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}


	public String getGcTrack() {
		return gcTrack;
	}

	public void setGcTrack(String gcTrack) {
		this.gcTrack = gcTrack;
	}

	public String getAchievementTf() {
		return achievementTf;
	}

	public void setAchievementTf(String achievementTf) {
		this.achievementTf = achievementTf;
	}

	public String getAchievementUser() {
		return achievementUser;
	}

	public void setAchievementUser(String achievementUser) {
		this.achievementUser = achievementUser;
	}

	public String getCoCreation() {
		return coCreation;
	}

	public void setCoCreation(String coCreation) {
		this.coCreation = coCreation;
	}

	public String getIdeaStage() {
		return ideaStage;
	}

	public void setIdeaStage(String ideaStage) {
		this.ideaStage = ideaStage;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getSchoolType() {
		return schoolType;
	}

	public void setSchoolType(String schoolType) {
		this.schoolType = schoolType;
	}

	public String getBelongsField() {
		return belongsField;
	}

	public void setBelongsField(String belongsField) {
		this.belongsField = belongsField;
	}

	public String getSchoolCity() {
		return schoolCity;
	}

	public void setSchoolCity(String schoolCity) {
		this.schoolCity = schoolCity;
	}

	public String getRoleFlag() {
		return roleFlag;
	}

	public void setRoleFlag(String roleFlag) {
		this.roleFlag = roleFlag;
	}

	public String getProfessor() {
		return professor;
	}

	public void setProfessor(String professor) {
		this.professor = professor;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankNumber() {
		return bankNumber;
	}

	public void setBankNumber(String bankNumber) {
		this.bankNumber = bankNumber;
	}

	public Integer getRanking() {
		return ranking;
	}

	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
}
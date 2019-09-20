package com.oseasy.pw.modules.pw.entity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.act.modules.actyw.entity.ActYwApply;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.pcore.common.persistence.AttachMentEntity;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.entity.TeamUserHistory;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 入驻申报Entity.
 *
 * @author chenh
 * @version 2017-11-26
 */
public class PwEnter extends DataEntity<PwEnter> {
    private static final long serialVersionUID = 1L;
    public static final Integer CS_COMPANY = 1;
    public static final Integer CS_PROJECT = 2;
    public static final Integer CS_TEAM = 3;
    public static final String TABLEA = "a.";
    public static final String TABLEAYR = "ayr.";
    public static final String TYPE = "type";
    public static final String STATUS = "status";
    public static final String SYS_DATE = "sysDate";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";

    private String parentId; // 父版本
    private String isCopy;// 是否变更数据：0、否；1、是
    private String type;// 入驻类型:0、团队入驻;2、企业入驻
    private String appType;// 入驻申请类型:1、申请;2、变更申请
    private String no; // 申请编号

    private String declareId; // 申请人
    private String declarePhoto; // 申请人 入驻图片
    private String status; // '入驻状态： 0、待审核；  20、审核失败；10、入驻成功；30、即将到期 40、已续期 50、已到期 60、已退孵
    private String restatus; // 房间分配状态： 待分配、待变更分配、已分配
    private String isTemp; // 状态:是否临时数据
    private String isShow; // 是否显示:0、忽略；1不忽略
    private Date reDate; // 续期时间
    private Date exitDate; // 退孵时间
    private Date startDate; // 有效期开始时间
    private Date endDate; // 有效期结束时间
    private Integer cursel; // 当前选中:1、企业;2、项目;3、团队
    private Integer term; // 期限,单位：年
    private Integer termNum; // 续期次数
    private String rid; //房间id
    private String isShowWorkNum; // 是否展示工位数 0不展示 1展示
    private Integer expectWorkNum; // 期望工位数
    private Integer expectTerm; // 期望入孵时间,单位：天
    private String expectRemark; // 申请 场地 备注

    private User applicant; // 申请人
    private List<PwProject> pwProjectList; // 申请人
    private PwCompany pwCompany;//申请企业
    private Team team;//团队
    private List<TeamUserHistory> stus;//团队学生list
    private List<TeamUserHistory> teas;//团队导师list

    private AttachMentEntity attachMentEntity; //成果物附件提交
    private List<SysAttachment> fileInfo;
    private List<SysAttachment> sysAttachmentList;
    private Integer querystatus;
    private int isTF;
    private int isYGH;

    @Transient
    private PwEnterDetail eteam; // 团队
    @Transient
    private List<PwEnterDetail> eprojects; // 项目
    @Transient
    private PwEnterDetail ecompany; // 企业
    @Transient
    private List<PwEnterRoom> erooms; // 入驻场地
    private ActYwApply apply; // 申报信息
    @Transient
    private List<PwApplyRecord> applyRecords; // 申请记录
    @Transient
    private PwApplyRecord applyRecord; // 最新的变更申请记录


    //四种变更
    boolean  companyIsChange=true;
    boolean  teamIsChange=true;
    boolean  projectIsChange=true;
    boolean  spaceIsChange=true;
    // 查询使用的属性
    @Transient
    private Boolean isView;
    @Transient
    private Integer hasYfp;
    @Transient
    private List<String> ids; // 查询ID
    private String keys; // 关键字
    @Transient
    private String pstatus; // 状态:pw_enter_status/PwEnterStatus
    @Transient
    private String prestatus; // 状态:pw_enter_status/PwEnterStatus
    @Transient
    private List<String> pstatuss; // 状态:pw_enter_status/PwEnterStatus
    @Transient
    private List<String> prestatuss; // 分配状态/PwEroomStatus
    @Transient
    private Date exitQDate; // 查询-退孵时间
    @Transient
    private Date reQDate; // 查询-续期时间
    @Transient
    private Date startQDate; // 查询-开始时间
    @Transient
    private Date endQDate; // 查询-结束时间



    // @Transient
    // private Boolean isAudited;
    // @Transient
    // private String pstatussql; // 状态:pw_enter_status/PwEnterStatus
    // @Transient
    // @JsonIgnore
    // private PwEnterDetail[] pwEnterDetails;
    // @Transient
    // @JsonIgnore
    // private List<String> delIds;

    public PwEnter() {
        super();
    }

    public PwEnter(String id, String isShow) {
        super(id);
        this.isShow = isShow;
    }

    public PwEnter(PwEnterDetail eteam, List<PwEnterDetail> eprojects, PwEnterDetail ecompany) {
        super();
        this.eteam = eteam;
        this.eprojects = eprojects;
        this.ecompany = ecompany;
    }

    public int getIsTF() {
        return isTF;
    }

    public void setIsTF(int isTF) {
        this.isTF = isTF;
    }

    public int getIsYGH() {
        return isYGH;
    }

    public void setIsYGH(int isYGH) {
        this.isYGH = isYGH;
    }

    public List<String> getPrestatuss() {
        if (StringUtil.isNotEmpty(this.prestatus)) {
            this.prestatuss = Arrays.asList((this.prestatus).split(StringUtil.DOTH));
        }
        return prestatuss;
    }

    public void setPrestatuss(List<String> prestatuss) {
        this.prestatuss = prestatuss;
    }

    public String getPrestatus() {
        return prestatus;
    }

    public void setPrestatus(String prestatus) {
        this.prestatus = prestatus;
    }

    public Integer getQuerystatus() {
        return querystatus;
    }

    public void setQuerystatus(Integer querystatus) {
        this.querystatus = querystatus;
    }

    public Integer getHasYfp() {
        return hasYfp;
    }

    public Boolean isYfp() {
        return ((hasYfp == null) || (hasYfp <= 0)) ? false : true;
    }

    public void setHasYfp(Integer hasYfp) {
        this.hasYfp = hasYfp;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public boolean isCompanyIsChange() {
        return companyIsChange;
    }

    public void setCompanyIsChange(boolean companyIsChange) {
        this.companyIsChange = companyIsChange;
    }

    public boolean isTeamIsChange() {
        return teamIsChange;
    }

    public void setTeamIsChange(boolean teamIsChange) {
        this.teamIsChange = teamIsChange;
    }

    public boolean isProjectIsChange() {
        return projectIsChange;
    }

    public void setProjectIsChange(boolean projectIsChange) {
        this.projectIsChange = projectIsChange;
    }

    public boolean isSpaceIsChange() {
        return spaceIsChange;
    }

    public void setSpaceIsChange(boolean spaceIsChange) {
        this.spaceIsChange = spaceIsChange;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
         this.parentId = parentId;
     }

    public String getDeclareId() {
        if(StringUtil.isEmpty(this.declareId) && (this.applicant != null) && StringUtil.isEmpty(this.applicant.getId())){
            this.declareId = this.applicant.getId();
        }
        return declareId;
    }

    public void setDeclareId(String declareId) {
        this.declareId = declareId;
    }

    public String getDeclarePhoto() {
        return declarePhoto;
    }

    public void setDeclarePhoto(String declarePhoto) {
        this.declarePhoto = declarePhoto;
    }

    public String getIsCopy() {
        return isCopy;
    }

    public void setIsCopy(String isCopy) {
        this.isCopy = isCopy;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getRestatus() {
        return restatus;
    }

    public void setRestatus(String restatus) {
        this.restatus = restatus;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getIsShowWorkNum() {
        return isShowWorkNum;
    }

    public void setIsShowWorkNum(String isShowWorkNum) {
        this.isShowWorkNum = isShowWorkNum;
    }

    public List<TeamUserHistory> getStus() {
        return stus;
    }

    public void setStus(List<TeamUserHistory> stus) {
        this.stus = stus;
    }

    public List<TeamUserHistory> getTeas() {
        return teas;
    }

    public void setTeas(List<TeamUserHistory> teas) {
        this.teas = teas;
    }

    public Boolean getView() {
        return isView;
    }

    public void setView(Boolean view) {
        isView = view;
    }

    public void setPstatuss(List<String> pstatuss) {
        this.pstatuss = pstatuss;
    }

    public PwEnter(String id) {
        super(id);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ActYwApply getApply() {
        return apply;
    }

    public void setApply(ActYwApply apply) {
        this.apply = apply;
    }

    public Date getExitDate() {
        return exitDate;
    }

    public void setExitDate(Date exitDate) {
        this.exitDate = exitDate;
    }

    public PwEnterDetail getEteam() {
        return eteam;
    }

    public void setEteam(PwEnterDetail eteam) {
        this.eteam = eteam;
    }

    public String getIsTemp() {
        return isTemp;
    }

    public void setIsTemp(String isTemp) {
        this.isTemp = isTemp;
    }

    public List<PwEnterDetail> getEprojects() {
        return eprojects;
    }

    public void setEprojects(List<PwEnterDetail> eprojects) {
        this.eprojects = eprojects;
    }

    public PwEnterDetail getEcompany() {
        return ecompany;
    }

    public void setEcompany(PwEnterDetail ecompany) {
        this.ecompany = ecompany;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public User getApplicant() {
        return applicant;
    }

    public void setApplicant(User applicant) {
        this.applicant = applicant;
    }

    @Length(min = 0, max = 1, message = "状态长度必须介于 0 和 1 之间")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public Integer getCursel() {
        return cursel;
    }

    public void setCursel(Integer cursel) {
        this.cursel = cursel;
    }

    public Integer getTermNum() {
        return termNum;
    }

    public void setTermNum(Integer termNum) {
        this.termNum = termNum;
    }

    public Date getReDate() {
        return reDate;
    }

    public void setReDate(Date reDate) {
        this.reDate = reDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<PwEnterRoom> getErooms() {
        return erooms;
    }

    public void setErooms(List<PwEnterRoom> erooms) {
        this.erooms = erooms;
    }

    public String getPstatus() {
        return pstatus;
    }

    public void setPstatus(String pstatus) {
        this.pstatus = pstatus;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public List<String> getPstatuss() {
        if (StringUtil.isNotEmpty(this.pstatus)) {
            this.pstatuss = Arrays.asList((this.pstatus).split(StringUtil.DOTH));
        }
        return pstatuss;
    }

    public Date getExitQDate() {
        return exitQDate;
    }

    public void setExitQDate(Date exitQDate) {
        this.exitQDate = exitQDate;
    }

    public Date getReQDate() {
        return reQDate;
    }

    public void setReQDate(Date reQDate) {
        this.reQDate = reQDate;
    }

    public Date getStartQDate() {
        return startQDate;
    }

    public void setStartQDate(Date startQDate) {
        this.startQDate = startQDate;
    }

    public Date getEndQDate() {
        return endQDate;
    }

    public void setEndQDate(Date endQDate) {
        this.endQDate = endQDate;
    }

    public Boolean getIsView() {
        if (isView == null) {
            isView = true;
        }
        return isView;
    }

    public void setIsView(Boolean isView) {
        this.isView = isView;
    }

    public Integer getExpectWorkNum() {
        return expectWorkNum;
    }

    public void setExpectWorkNum(Integer expectWorkNum) {
        this.expectWorkNum = expectWorkNum;
    }

    public Integer getExpectTerm() {
        return expectTerm;
    }

    public void setExpectTerm(Integer expectTerm) {
        this.expectTerm = expectTerm;
    }

    public String getExpectRemark() {
        return expectRemark;
    }

    public void setExpectRemark(String expectRemark) {
        this.expectRemark = expectRemark;
    }

    public List<PwProject> getPwProjectList() {
        return pwProjectList;
    }

    public void setPwProjectList(List<PwProject> pwProjectList) {
        this.pwProjectList = pwProjectList;
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

    public AttachMentEntity getAttachMentEntity() {
        return attachMentEntity;
    }

    public void setAttachMentEntity(AttachMentEntity attachMentEntity) {
        this.attachMentEntity = attachMentEntity;
    }

    public List<SysAttachment> getFileInfo() {
        return fileInfo;
    }

    public List<PwApplyRecord> getApplyRecords() {
        return applyRecords;
    }

    public void setApplyRecords(List<PwApplyRecord> applyRecords) {
        this.applyRecords = applyRecords;
    }

    public PwApplyRecord getApplyRecord() {
        return applyRecord;
    }

    public void setApplyRecord(PwApplyRecord applyRecord) {
        this.applyRecord = applyRecord;
    }

    public void setFileInfo(List<SysAttachment> fileInfo) {
        this.fileInfo = fileInfo;
    }

    public List<SysAttachment> getSysAttachmentList() {
        return sysAttachmentList;
    }

    public void setSysAttachmentList(List<SysAttachment> sysAttachmentList) {
        this.sysAttachmentList = sysAttachmentList;
    }
}
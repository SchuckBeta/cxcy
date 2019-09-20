package com.oseasy.pw.modules.pw.vo;

import com.oseasy.com.fileserver.modules.attachment.vo.SysAttachmentVo;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.pw.modules.pw.entity.PwCompany;
import com.oseasy.pw.modules.pw.entity.PwEnter;
import com.oseasy.pw.modules.pw.entity.PwProject;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.entity.TeamUserHistory;

import java.io.Serializable;
import java.util.List;


/**
 * 入驻申请Vo.
 * @author chenhao
 *
 */
public class PwEnterApplyVo implements Serializable{

    private static final long serialVersionUID = 1L;
    private String id;
    private String type;// 入驻类型:1、团队入驻;3、企业入驻
    private String appType;// 入驻申请类型:1、申请;2、变更申请
    private String declareId; // 申请人
    private String declarePhoto; // 申请人 入驻图片
    private User Applicant; // 用户
    private String isShow; // 是否显示:0、忽略；1不忽略
    private List<PwProject> pwProjectList; // 申请人
    private PwCompany pwCompany;//申请企业
    private Team team;//团队
    private List<TeamUserHistory> stus;//团队学生list
    private List<TeamUserHistory> teas;//团队导师list
    private String parentId; // 父版本
    private String isCopy;// 是否变更数据：0、否；1、是
    private String isTemp; // 状态:是否临时数据
    private String status; // '入驻状态： 0、待审核；  20、审核失败；10、入驻成功；30、即将到期 40、已续期 50、已到期 60、已退孵

    //四种变更
    boolean  companyIsChange=false;
    boolean  teamIsChange=false;
    boolean  projectIsChange=false;
    boolean  spaceIsChange=false;

    private Integer expectWorkNum; // 期望工位数
    private Integer expectTerm; // 期望入孵时间,单位：年
    private String expectRemark; // 申请 场地 备注

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getApplicant() {
        return Applicant;
    }

    public void setApplicant(User applicant) {
        Applicant = applicant;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getDeclareId() {
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

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getIsCopy() {
        return isCopy;
    }

    public void setIsCopy(String isCopy) {
        this.isCopy = isCopy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsTemp() {
        return isTemp;
    }

    public void setIsTemp(String isTemp) {
        this.isTemp = isTemp;
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
}

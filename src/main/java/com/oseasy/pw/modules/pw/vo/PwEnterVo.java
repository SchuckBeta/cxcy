package com.oseasy.pw.modules.pw.vo;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.vo.SysAttachmentVo;
import com.oseasy.pw.modules.pw.entity.PwCompany;
import com.oseasy.pw.modules.pw.entity.PwEnter;
import com.oseasy.util.common.utils.StringUtil;


/**
 * 预定日历Vo.
 * @author chenhao
 *
 */
public class PwEnterVo implements Serializable{
  private static final long serialVersionUID = 1L;
  private String eid;//入驻编号信息
  private Integer cursel;//是否提交
  private Boolean isSave;//是否提交
  private Boolean hasTeam;//团队是否入驻
  private Boolean hasCompany;//企业是否入驻
  private Boolean hasProject;//项目是否入驻
  private String projectRemarks;//入驻项目说明
  private String teamRemarks;//入驻团队说明
  private String pwCompanyRemarks;//入驻企业说明
  private String projectPteam;//是否使用项目团队
  private List<SysAttachmentVo> tfiles;//团队附件
  private List<SysAttachmentVo> cfiles;//企业附件
  private List<SysAttachmentVo> pfiles;//项目附件
  private List<SysAttachmentVo>  projectFiles;//项目附件
  private String projectId;//入驻编号信息
  private String teamId;//入驻编号信息
  private PwCompany pwCompany;

  public PwEnterVo() {
    super();
  }

  public PwEnterVo(String eid) {
    super();
    this.eid = eid;
  }

  public List<SysAttachmentVo> getProjectFiles() {
    return projectFiles;
  }

  public void setProjectFiles(List<SysAttachmentVo> projectFiles) {
    this.projectFiles = projectFiles;
  }

  public Boolean getIsSave() {
    return isSave;
  }

  public void setIsSave(Boolean isSave) {
    this.isSave = isSave;
  }

  public String getProjectPteam() {
    return projectPteam;
  }

  public void setProjectPteam(String projectPteam) {
    this.projectPteam = projectPteam;
  }

  public String getEid() {
    return eid;
  }

  public Boolean getSave() {
    return isSave;
  }

  public void setSave(Boolean save) {
    isSave = save;
  }

  public List<SysAttachmentVo> getTfiles() {
    return tfiles;
  }

  public void setTfiles(List<SysAttachmentVo> tfiles) {
    this.tfiles = tfiles;
  }

  public List<SysAttachmentVo> getCfiles() {
    return cfiles;
  }

  public void setCfiles(List<SysAttachmentVo> cfiles) {
    this.cfiles = cfiles;
  }

  public List<SysAttachmentVo> getPfiles() {
    return pfiles;
  }

  public void setPfiles(List<SysAttachmentVo> pfiles) {
    this.pfiles = pfiles;
  }

  public Integer getCursel() {
    return cursel;
  }

  public void setCursel(Integer cursel) {
    this.cursel = cursel;
  }

  public void setEid(String eid) {
    this.eid = eid;
  }
  public String getProjectId() {
    return projectId;
  }
  public void setProjectId(String projectId) {
    this.projectId = projectId;
  }
  public String getTeamId() {
    return teamId;
  }
  public void setTeamId(String teamId) {
    this.teamId = teamId;
  }
  public Boolean getHasTeam() {
    return hasTeam;
  }
  public void setHasTeam(Boolean hasTeam) {
    this.hasTeam = hasTeam;
  }
  public Boolean getHasCompany() {
    return hasCompany;
  }
  public void setHasCompany(Boolean hasCompany) {
    this.hasCompany = hasCompany;
  }
  public Boolean getHasProject() {
    return hasProject;
  }
  public void setHasProject(Boolean hasProject) {
    this.hasProject = hasProject;
  }
  public PwCompany getPwCompany() {
    return pwCompany;
  }
  public void setPwCompany(PwCompany pwCompany) {
    this.pwCompany = pwCompany;
  }

  public String getProjectRemarks() {
    return projectRemarks;
  }

  public void setProjectRemarks(String projectRemarks) {
    this.projectRemarks = projectRemarks;
  }

  public String getTeamRemarks() {
    return teamRemarks;
  }

  public void setTeamRemarks(String teamRemarks) {
    this.teamRemarks = teamRemarks;
  }

  public String getPwCompanyRemarks() {
    return pwCompanyRemarks;
  }

  public void setPwCompanyRemarks(String pwCompanyRemarks) {
    this.pwCompanyRemarks = pwCompanyRemarks;
  }

  public static PwEnterVo convert(PwEnter pwEnter) {
    PwEnterVo peVo = new PwEnterVo(pwEnter.getId());
//    if((pwEnter.getEcompany() != null) && (pwEnter.getEcompany().getPwCompany() != null)){
//      peVo.setHasCompany(true);
//      peVo.setPwCompany(pwEnter.getEcompany().getPwCompany());
//      peVo.setPwCompanyRemarks(pwEnter.getEcompany().getRemarks());
//    }else{
//      peVo.setHasCompany(false);
//    }
//
//    if((pwEnter.getEproject() != null) && (pwEnter.getEproject().getProject() != null) && StringUtil.isNotEmpty(pwEnter.getEproject().getProject().getId())){
//      peVo.setHasProject(true);
//      peVo.setProjectId(pwEnter.getEproject().getProject().getId());
//      peVo.setProjectRemarks(pwEnter.getEproject().getRemarks());
//      peVo.setProjectPteam(pwEnter.getEproject().getPteam());
//    }else{
//      peVo.setHasProject(false);
//    }
//
//    if((pwEnter.getEteam() != null) && (pwEnter.getEteam().getTeam() != null) && StringUtil.isNotEmpty(pwEnter.getEteam().getTeam().getId())){
//      peVo.setHasTeam(true);
//      peVo.setTeamRemarks(pwEnter.getEteam().getRemarks());
//    }else{
//      peVo.setHasTeam(false);
//    }
    return peVo;
  }
}

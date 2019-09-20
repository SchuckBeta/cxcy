package com.oseasy.pro.modules.gcontest.vo;

import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.pro.modules.gcontest.enums.GContestStatusEnum;

/**
 * 项目申报Vo
 * @author 9527
 * @version 2017-03-11
 */
public class GContestListVo  extends DataEntity<GContestListVo>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6266681292529811602L;
	private String competitionNumber;
	private String pName;
	private String declareId;
	private String declareName;
	private String collegeName;
	private String level;
	private String type;
	private String state;//流程状态
	private String grade;//审核状态
	private String financingStat;
	private String proc_ins_id;
	private String currentSystem;
	private String schoolendResult;
	private String auditState;
	private String auditCode;
	private String subStatus;
	private String proType;
	private String proTypeStr;
	private String create_by;
	private String snames;
	private String tnames;
	private String ftb;
	private String actywId;
	private String groupId;
	//cdn
	private String userid;
	private String gcTrack;

	private String logoUrl;

	private SysAttachment logo;//logo图片文件

	private String belongsField;

	public String getBelongsField() {
		return belongsField;
	}

	public void setBelongsField(String belongsField) {
		this.belongsField = belongsField;
	}

	public SysAttachment getLogo() {
		return logo;
	}

	public void setLogo(SysAttachment logo) {
		this.logo = logo;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getSubStatus() {
		return subStatus;
	}

	public void setSubStatus(String subStatus) {
		this.subStatus = subStatus;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getDeclareId() {
		return declareId;
	}

	public void setDeclareId(String declareId) {
		this.declareId = declareId;
	}

	public String getActywId() {
		return actywId;
	}

	public void setActywId(String actywId) {
		this.actywId = actywId;
	}

	public String getFtb() {
		return ftb;
	}

	public void setFtb(String ftb) {
		this.ftb = ftb;
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



	public String getCreate_by() {
		return create_by;
	}

	public void setCreateBy(String create_by) {
		this.create_by = create_by;
	}

	public String getAuditCode() {
		return auditCode;
	}

	public void setAuditCode(String auditCode) {
		this.auditCode = auditCode;
	}

	public String getCompetitionNumber() {
		return competitionNumber;
	}

	public void setCompetitionNumber(String competitionNumber) {
		this.competitionNumber = competitionNumber;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public String getDeclareName() {
		return declareName;
	}

	public void setDeclareName(String declareName) {
		this.declareName = declareName;
	}

	public String getCollegeName() {
		return collegeName;
	}

	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFinancingStat() {
		return financingStat;
	}

	public void setFinancingStat(String financingStat) {
		this.financingStat = financingStat;
	}

	public String getProc_ins_id() {
		return proc_ins_id;
	}

	public void setProcInsId(String proc_ins_id) {
		this.proc_ins_id = proc_ins_id;
	}

	public String getCurrentSystem() {
		return currentSystem;
	}

	public void setCurrentSystem(String currentSystem) {
		this.currentSystem = currentSystem;
	}

	public String getSchoolendResult() {
		return schoolendResult;
	}

	public void setSchoolendResult(String schoolendResult) {
		this.schoolendResult = schoolendResult;
	}

	public String getAuditState() {
		if ("-999".equals(auditCode)) {
			return this.auditState;
		}else{
			return GContestStatusEnum.getNameByValue(auditCode);
		}
	}

	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}

	public String getProType() {
		return proType;
	}

	public void setProType(String proType) {
		this.proType = proType;
	}

	public String getProTypeStr() {
		return proTypeStr;
	}

	public void setProTypeStr(String proTypeStr) {
		this.proTypeStr = proTypeStr;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getGcTrack() {
		return gcTrack;
	}

	public void setGcTrack(String gcTrack) {
		this.gcTrack = gcTrack;
	}
}
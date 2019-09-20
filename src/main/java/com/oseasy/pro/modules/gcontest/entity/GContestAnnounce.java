package com.oseasy.pro.modules.gcontest.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.com.pcore.common.persistence.DataEntity;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 * 大赛通告表Entity
 * @author zdk
 * @version 2017-03-29
 */
public class GContestAnnounce extends DataEntity<GContestAnnounce> {
	
	private static final long serialVersionUID = 1L;
	private Date applyStart;		// 报名开始时间
	private Date applyEnd;		// 报名结束时间
	private String content;		// content
	private String flowId;		// 流程id（互联网大赛模板id等）
	private String gName;		// 大赛名称
	private String type;		// 大赛类型（1 互联+大赛）
	private Date collegeStart;		// 院级赛事开始结束时间
	private Date collegeEnd;		// college_end
	private Date schoolStart;		// 校赛开始 结束时间
	private Date schoolEnd;		// school_end
	private Date provinceStart;		// 省赛开始结束时间
	private Date provinceEnd;		// province_end
	private Date countryStart;		// 国赛的开始结束时间
	private Date countryEnd;		// country_end
	private String status;             // 发布状态

	private String contestLevel; //竞赛级别
	
	public GContestAnnounce() {
		super();
	}

	public GContestAnnounce(String id) {
		super(id);
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
/*	@NotNull(message="报名开始时间不能为空")*/
	public Date getApplyStart() {
		return applyStart;
	}

	public void setApplyStart(Date applyStart) {
		this.applyStart = applyStart;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
/*	@NotNull(message="报名结束时间不能为空")*/
	public Date getApplyEnd() {
		return applyEnd;
	}

	public void setApplyEnd(Date applyEnd) {
		this.applyEnd = applyEnd;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=0, max=64, message="流程id（互联网大赛模板id等）长度必须介于 0 和 64 之间")
	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	
	/*@Length(min=0, max=64, message="大赛名称长度必须介于 0 和 64 之间")
	public String getGName() {
		return gName;
	}

	public void setGName(String gName) {
		this.gName = gName;
	}*/
	
	@Length(min=0, max=20, message="大赛类型（1 互联+大赛）长度必须介于 0 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCollegeStart() {
		return collegeStart;
	}

	public void setCollegeStart(Date collegeStart) {
		this.collegeStart = collegeStart;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCollegeEnd() {
		return collegeEnd;
	}

	public void setCollegeEnd(Date collegeEnd) {
		this.collegeEnd = collegeEnd;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getSchoolStart() {
		return schoolStart;
	}

	public void setSchoolStart(Date schoolStart) {
		this.schoolStart = schoolStart;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getSchoolEnd() {
		return schoolEnd;
	}

	public void setSchoolEnd(Date schoolEnd) {
		this.schoolEnd = schoolEnd;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getProvinceStart() {
		return provinceStart;
	}

	public void setProvinceStart(Date provinceStart) {
		this.provinceStart = provinceStart;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getProvinceEnd() {
		return provinceEnd;
	}

	public void setProvinceEnd(Date provinceEnd) {
		this.provinceEnd = provinceEnd;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCountryStart() {
		return countryStart;
	}

	public void setCountryStart(Date countryStart) {
		this.countryStart = countryStart;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCountryEnd() {
		return countryEnd;
	}

	public void setCountryEnd(Date countryEnd) {
		this.countryEnd = countryEnd;
	}

	public String getgName() {
		return gName;
	}

	public void setgName(String gName) {
		this.gName = gName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getContestLevel() {
		return contestLevel;
	}

	public void setContestLevel(String contestLevel) {
		this.contestLevel = contestLevel;
	}
}
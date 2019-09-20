package com.oseasy.pw.modules.pw.entity;

import java.util.Date;

import com.oseasy.com.pcore.common.persistence.DataExtEntity;
import com.oseasy.pw.modules.pw.vo.PwEnterBgremarks;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Length;

import com.oseasy.util.common.utils.StringUtil;

/**
 * pwApplyRecordEntity.
 * @author zy
 * @version 2018-11-20
 */
public class PwApplyRecord extends DataExtEntity<PwApplyRecord> {

	private static final long serialVersionUID = 1L;
	private String eid;		// 申报单编号【pw_enter-&gt;id】
	private String type;		// 申请类型:10、入驻团队申请;20、入驻企业申请; 	30、续期申请;40、退孵申请;【50~80 变更】50、场地变更  60、企业变更 70、项目变更 80、团队变更      90管理员审核 100:管理员变更
	private String parentId;		// 申报单编号【pw_enter-&gt;id】							//变更审核有多个 参数为 多个
	private String typeString;  //入驻说明
	private String status;		// 审核状态：0、待审核；1、审核通过；2、审核不通过
	private Integer term;		// 期限,单位：天
	private String bgremarks;		// 变更说明

	private String frontBgremarks;		//前台变更说明

	private String declareId;		//申请人ID
	private Date declareTime;		//申请时间
	private String declareName;		//申请人名称
	private String auditId;		//审核人ID
	private Date auditTime;		//审核时间
	private String auditName;		//审核人名称

	public PwApplyRecord() {
		super();
	}

	public PwApplyRecord(String id){
		super(id);
	}

	@Length(min=1, max=64, message="申报单编号【pw_enter-&gt;id】长度必须介于 1 和 64 之间")
	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	@Length(min=0, max=2, message="申请类型:1、入驻团队申请;2、入驻企业申请; 	3、续期申请;4、退孵申请;【5~8 变更】5、场地变更  6、企业变更 7、项目变更 8、团队变更      9管理员审核 10:管理员变更长度必须介于 0 和 2 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=0, max=2, message="审核状态：0、待审核；1、审核通过；2、审核不通过长度必须介于 0 和 2 之间")
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

	@Length(min=0, max=500, message="变更说明   废弃长度必须介于 0 和 500 之间")
	public String getBgremarks() {
		if(StringUtil.isEmpty(this.bgremarks) && StringUtil.isNotEmpty(this.type)){
			this.bgremarks = PwEnterBgremarks.getTypeStringByType(this.type);
		}
		return bgremarks;
	}

	public void setBgremarks(String bgremarks) {
		this.bgremarks = bgremarks;
	}

	@Length(min=0, max=500, message="变更说明   废弃长度必须介于 0 和 500 之间")
	public String getFrontBgremarks() {
		if(StringUtil.isEmpty(this.frontBgremarks) && StringUtil.isNotEmpty(this.type)){
			this.frontBgremarks = PwEnterBgremarks.getFrontTypeStringByType(this.type);
		}
		return frontBgremarks;
	}

	public void setFrontBgremarks(String frontBgremarks) {
		this.frontBgremarks = frontBgremarks;
	}

	public String getTypeString() {
		return typeString;
	}

	public void setTypeString(String typeString) {
		this.typeString = typeString;
	}

	public String getAuditName() {
		return auditName;
	}

	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public String getDeclareId() {
		return declareId;
	}

	public void setDeclareId(String declareId) {
		this.declareId = declareId;
	}

	public Date getDeclareTime() {
		return declareTime;
	}

	public void setDeclareTime(Date declareTime) {
		this.declareTime = declareTime;
	}

	public String getDeclareName() {
		return declareName;
	}

	public void setDeclareName(String declareName) {
		this.declareName = declareName;
	}

	public String getAuditId() {
		return auditId;
	}

	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
}
package com.oseasy.act.modules.actyw.entity;

import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataExtEntity;

/**
 * 专家指派规则Entity.
 * @author zy
 * @version 2019-01-03
 */
public class ActYwEtAssignRule extends DataExtEntity<ActYwEtAssignRule> {

	private static final long serialVersionUID = 1L;
	private String actywId;		// 项目类别编号
	private String gnodeId;		// 审核节点编号
    private String atype;       // 审核类型：0、指派； 1、委派
	private String auditUserNum;		// 设定审核人数 -1为无限 全部专家都要审核 默认为-1
	public static String auditNum="-1"; //全部
	private String auditType;		// 审核方式 0为自动 1为手动 默认为0
	private String auditRole;		// 审核角色 0为全部专家 1为院级 默认为0
	public static String autoRole="0"; //全部专家
	public static String collegeRole="1";
	private String auditMax;		// 设定每个项目最多审核人数 -1为无限 默认为-1
	private String isAuto;		// 是否为持续审核 0为否 1为是 默认为0

	private List<String> proIdList;		//分配项目id列表

	private List<String> userIdList;		//分配用户id列表

	private String officeId;		//查询条件 学院id

	public ActYwEtAssignRule() {
		super();
	}

	public ActYwEtAssignRule(String id){
		super(id);
	}

	@Length(min=1, max=64, message="项目类别编号长度必须介于 1 和 64 之间")
	public String getActywId() {
		return actywId;
	}

	public void setActywId(String actywId) {
		this.actywId = actywId;
	}

	public String getAtype() {
        return atype;
    }

    public void setAtype(String atype) {
        this.atype = atype;
    }

    @Length(min=1, max=64, message="审核节点编号长度必须介于 1 和 64 之间")
	public String getGnodeId() {
		return gnodeId;
	}

	public void setGnodeId(String gnodeId) {
		this.gnodeId = gnodeId;
	}

	@Length(min=1, max=64, message="设定审核人数 -1为无限 默认为-1长度必须介于 1 和 64 之间")
	public String getAuditUserNum() {
		return auditUserNum;
	}

	public void setAuditUserNum(String auditUserNum) {
		this.auditUserNum = auditUserNum;
	}

	@Length(min=1, max=2, message="审核方式 0为自动 1为手动 默认为0长度必须介于 1 和 2 之间")
	public String getAuditType() {
		return auditType;
	}

	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}

	@Length(min=1, max=2, message="审核角色 0为自动 1为院级 默认为0长度必须介于 1 和 2 之间")
	public String getAuditRole() {
		return auditRole;
	}

	public void setAuditRole(String auditRole) {
		this.auditRole = auditRole;
	}

	@Length(min=1, max=64, message="设定每个项目最多审核人数 -1为无限 默认为-1长度必须介于 1 和 64 之间")
	public String getAuditMax() {
		return auditMax;
	}

	public void setAuditMax(String auditMax) {
		this.auditMax = auditMax;
	}

	@Length(min=1, max=2, message="是否为持续审核 0为否 1为是 默认为0长度必须介于 1 和 2 之间")
	public String getIsAuto() {
		return isAuto;
	}

	public void setIsAuto(String isAuto) {
		this.isAuto = isAuto;
	}

	public List<String> getProIdList() {
		return proIdList;
	}

	public void setProIdList(List<String> proIdList) {
		this.proIdList = proIdList;
	}

	public List<String> getUserIdList() {
		return userIdList;
	}

	public void setUserIdList(List<String> userIdList) {
		this.userIdList = userIdList;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
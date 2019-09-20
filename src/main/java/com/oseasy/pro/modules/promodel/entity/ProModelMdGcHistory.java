package com.oseasy.pro.modules.promodel.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 互联网+大赛记录表Entity.
 * @author zy
 * @version 2018-06-05
 */
public class ProModelMdGcHistory extends DataEntity<ProModelMdGcHistory> {

	private static final long serialVersionUID = 1L;
	private String promodelId;		// promodel_id
	private String auditId;		// 审核记录表id
	private String gnodeId;		// 当前节点id
	private String result;		// 审核结果
	private String type;		// 表单属性对应字典表类型（校赛省赛国赛等）

	public ProModelMdGcHistory() {
		super();
	}

	public ProModelMdGcHistory(String id){
		super(id);
	}

	@Length(min=0, max=64, message="promodel_id长度必须介于 0 和 64 之间")
	public String getPromodelId() {
		return promodelId;
	}

	public void setPromodelId(String promodelId) {
		this.promodelId = promodelId;
	}

	@Length(min=0, max=64, message="审核记录表id长度必须介于 0 和 64 之间")
	public String getAuditId() {
		return auditId;
	}

	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}

	@Length(min=0, max=64, message="当前节点id长度必须介于 0 和 64 之间")
	public String getGnodeId() {
		return gnodeId;
	}

	public void setGnodeId(String gnodeId) {
		this.gnodeId = gnodeId;
	}

	@Length(min=0, max=255, message="审核结果长度必须介于 0 和 255 之间")
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Length(min=0, max=64, message="表单属性对应字典表类型（校赛省赛国赛等）长度必须介于 0 和 64 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
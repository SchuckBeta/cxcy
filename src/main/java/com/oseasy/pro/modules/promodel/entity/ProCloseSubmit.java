package com.oseasy.pro.modules.promodel.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 结项提交信息表Entity.
 * @author zy
 * @version 2017-12-01
 */
public class ProCloseSubmit extends DataEntity<ProCloseSubmit> {

	private static final long serialVersionUID = 1L;
	private String promodelId;		// 自定义表id
	private String gnodeId;		// gnode表id
	private String state;		// 保留字段
	private String stageResult;//阶段成果
	public ProCloseSubmit() {
		super();
	}

	public ProCloseSubmit(String id) {
		super(id);
	}
	public String getStageResult() {
		return stageResult;
	}

	public void setStageResult(String stageResult) {
		this.stageResult = stageResult;
	}
	@Length(min=0, max=64, message="自定义表id长度必须介于 0 和 64 之间")
	public String getPromodelId() {
		return promodelId;
	}

	public void setPromodelId(String promodelId) {
		this.promodelId = promodelId;
	}

	@Length(min=0, max=64, message="gnode表id长度必须介于 0 和 64 之间")
	public String getGnodeId() {
		return gnodeId;
	}

	public void setGnodeId(String gnodeId) {
		this.gnodeId = gnodeId;
	}

	@Length(min=0, max=255, message="保留字段长度必须介于 0 和 255 之间")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
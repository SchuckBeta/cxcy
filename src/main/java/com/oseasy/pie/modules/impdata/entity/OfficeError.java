package com.oseasy.pie.modules.impdata.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;

/**
 * 机构导入错误信息表Entity
 * @author 9527
 * @version 2017-05-24
 */
public class OfficeError extends DataEntity<OfficeError> implements IitCheckEetyExt{

	private static final long serialVersionUID = 1L;
	private String impId;		// 导入信息表主键
	private String office;		// 学院名称
	private String professional;		// 专业名称

	public OfficeError() {
		super();
	}

	public OfficeError(String id) {
		super(id);
	}

	@Length(min=1, max=64, message="导入信息表主键长度必须介于 1 和 64 之间")
	public String getImpId() {
		return impId;
	}

	public void setImpId(String impId) {
		this.impId = impId;
	}

	@Length(min=1, max=100, message="学院名称长度必须介于 1 和 100 之间")
	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	@Length(min=0, max=100, message="专业名称长度必须介于 0 和 100 之间")
	public String getProfessional() {
		return professional;
	}

	public void setProfessional(String professional) {
		this.professional = professional;
	}

}
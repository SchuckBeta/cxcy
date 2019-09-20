package com.oseasy.act.modules.actyw.entity;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 学校确认步骤Entity.
 * @author zy
 * @version 2018-01-15
 */
public class ActYwScstep extends DataEntity<ActYwScstep> {

	private static final long serialVersionUID = 1L;
	private String provinceActywId;		// 省模板流程id
    private String provinceActywName;		// 省模板流程id
	private String schoolTenantId;		// 校id
    private String schoolName;		// 学校名称
    private String step;		// 步骤（1,2,3,4）

	public ActYwScstep() {
		super();
	}

    public String getProvinceActywId() {
        return provinceActywId;
    }

    public void setProvinceActywId(String provinceActywId) {
        this.provinceActywId = provinceActywId;
    }

    public String getSchoolTenantId() {
        return schoolTenantId;
    }

    public void setSchoolTenantId(String schoolTenantId) {
        this.schoolTenantId = schoolTenantId;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getProvinceActywName() {
        return provinceActywName;
    }

    public void setProvinceActywName(String provinceActywName) {
        this.provinceActywName = provinceActywName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
}
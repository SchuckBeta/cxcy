package com.oseasy.act.modules.actyw.entity;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 节点状态中间表Entity.
 * @author zy
 * @version 2018-01-15
 */
public class ActYwPscrel extends DataEntity<ActYwPscrel> {

    private static final long serialVersionUID = 1L;
    private String provinceActywId;        // 省模板项目id
    private String schoolTenantId;        // 学校 tenantid
    private String schoolActywId;        // 学校项目id
    private String ispushed;        //是否已经推送过：0、否；1、是

    private String schoolName;        // 学校名称

    public ActYwPscrel() {
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

    public String getIspushed() {
        return ispushed;
    }

    public void setIspushed(String ispushed) {
        this.ispushed = ispushed;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolActywId() {
        return schoolActywId;
    }

    public void setSchoolActywId(String schoolActywId) {
        this.schoolActywId = schoolActywId;
    }
}

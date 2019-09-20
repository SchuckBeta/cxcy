/**
 * .
 */

package com.oseasy.pie.modules.impdata.vo;

import java.io.Serializable;

import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 用户模糊查询接口.
 *
 * @author chenhao
 *
 */
public class UserSave implements Serializable {
    private String id;
    private String office;
    private String professional;
    private String officeId;
    private String professionalId;
    private String no;
    private String name;
    private String mobile;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getProfessional() {
        return professional;
    }

    public void setProfessional(String professional) {
        this.professional = professional;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(String professionalId) {
        this.professionalId = professionalId;
    }

    public static DrCardError convertToDrCardError(UserSave user) {
        if (user == null) {
            return null;
        }

        DrCardError drCardError = new DrCardError();
        if (StringUtil.isNotEmpty(user.getOffice())) {
            drCardError.setOffice(user.getOffice());
            drCardError.setTmpOffice(user.getOffice());
        }
        if (StringUtil.isNotEmpty(user.getOfficeId())) {
            drCardError.setCurOffice(new Office(user.getOfficeId()));
        }
        if (StringUtil.isNotEmpty(user.getProfessional())) {
            drCardError.setProfessional(user.getProfessional());
        }
        if (StringUtil.isNotEmpty(user.getProfessionalId())) {
            drCardError.setCurProfessional(new Office(user.getProfessionalId()));
        }

        if (StringUtil.isNotEmpty(user.getName())) {
            drCardError.setName(user.getName());
            drCardError.setTmpName(user.getName());
        }
        if (StringUtil.isNotEmpty(user.getNo())) {
            drCardError.setTmpNo(user.getNo());
        }
        if (StringUtil.isNotEmpty(user.getMobile())) {
            drCardError.setMobile(user.getMobile());
            drCardError.setTmpTel(user.getMobile());
        }
        return drCardError;
    }
}

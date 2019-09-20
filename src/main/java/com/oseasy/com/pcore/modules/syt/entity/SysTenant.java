package com.oseasy.com.pcore.modules.syt.entity;


import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.common.config.CorePages;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.common.persistence.DataExtEntity;
import com.oseasy.com.pcore.modules.syt.vo.SytStatus;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.UrlUtil;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.Transient;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by PW on 2019/3/21.
 * 租户
 */
public class SysTenant extends DataExtEntity<SysTenant> {

    private static final long serialVersionUID = 1L;

    private String status;//状态:0,待开户;1、开户成功；2、锁定
    private String type;//租户类型（运营、省、高校）
    private String schoolCode;//学校代码
    private String schoolName;//学校名称
    private String schoolType;//学校类型（部署高校、非部署高校）
    private String schoolProvince;//省份
    private String schoolCity;//城市
    private String domainName;//域名
    private String isTpl;//是否模板数据
    private String areaCode;
    private String isCurrent;
    private String keys;//模糊查询关键字
    private String regval;// 初始化规则表达式(名称)

    @Transient
    private String httpurl;//http地址

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(String isCurrent) {
        this.isCurrent = isCurrent;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }

    public String getRegval() {
        return regval;
    }

    public void setRegval(String regval) {
        this.regval = regval;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(String schoolType) {
        this.schoolType = schoolType;
    }

    public String getSchoolProvince() {
        return schoolProvince;
    }

    public void setSchoolProvince(String schoolProvince) {
        this.schoolProvince = schoolProvince;
    }

    public String getSchoolCity() {
        return schoolCity;
    }

    public void setSchoolCity(String schoolCity) {
        this.schoolCity = schoolCity;
    }

    public String getHttpurl() {
        return httpurl;
    }

    public void setHttpurl(String httpurl) {
        this.httpurl = httpurl;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getIsTpl() {
        return isTpl;
    }

    public void setIsTpl(String isTpl) {
        this.isTpl = isTpl;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public static boolean checkYKH(SysTenant entity){
        if((entity == null) || (!(SytStatus.YKH.getKey()).equals(entity.getStatus()))) {
            return true;
        }
        return false;
    }

    public static String genRegister(String domainName, boolean isopen, String port, String prefix){
        if(StringUtil.isNotEmpty(domainName)) {
            StringBuffer buffer = new StringBuffer(UrlUtil.HTTP + UrlUtil.HTTPLINE);
            buffer.append(domainName);
            if(isopen){
                buffer.append(StringUtil.MAOH);
                buffer.append(port);
            }
            buffer.append(prefix);
            buffer.append("/toRegister");
            return buffer.toString();
        }
        return "javascript:void(0)";
    }

    @Override
    public String toString() {
        return "SysTenant{" +
                "id='" + id + '\'' +
                ", currentUser=" + currentUser +
                ", isNewRecord=" + isNewRecord +
                ", useCorpModel=" + useCorpModel +
                ", tenantId='" + tenantId + '\'' +

                "remarks='" + remarks + '\'' +
                ", createBy=" + createBy +
                ", createDate=" + createDate +
                ", updateBy=" + updateBy +
                ", updateDate=" + updateDate +
                ", delFlag='" + delFlag + '\'' +

                "schoolCode='" + schoolCode + '\'' +
                ", schoolName='" + schoolName + '\'' +
                ", schoolType='" + schoolType + '\'' +
                ", schoolProvince='" + schoolProvince + '\'' +
                ", schoolCity='" + schoolCity + '\'' +
                ", domainName='" + domainName + '\'' +
                ", isTpl='" + isTpl + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", isCurrent='" + isCurrent + '\'' +
                ", keys='" + keys + '\'' +
                '}';
    }
}

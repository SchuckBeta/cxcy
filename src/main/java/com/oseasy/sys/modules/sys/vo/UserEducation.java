/**
 * .
 */

package com.oseasy.sys.modules.sys.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * .
 * @author chenhao
 *
 */
public class UserEducation implements Serializable{
    private static final long serialVersionUID = 1L;

    private String id;
    private String tclass; //学制
    private Date enterdate; //入学年份
    private String cycle; //学制
    private String no; //学号
    private Date graduation; //毕业时间
    private String currState; //现状
    private String instudy; //在读学位
    private String education;//学历
    private String degree; //学位
    private String professional; //专业
    private String officeId; //机构
    private Date temporaryDate; //休学时间
    private List<String> domainIdList;  //技术领域
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Date getEnterdate() {
        return enterdate;
    }
    public void setEnterdate(Date enterdate) {
        this.enterdate = enterdate;
    }
    public String getCycle() {
        return cycle;
    }
    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getTclass() {
        return tclass;
    }
    public void setTclass(String tClass) {
        this.tclass = tClass;
    }
    public String getProfessional() {
        return professional;
    }
    public void setProfessional(String professional) {
        this.professional = professional;
    }
    public String getOfficeId() {
        return officeId;
    }
    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }
    public String getNo() {
        return no;
    }
    public void setNo(String no) {
        this.no = no;
    }
    public Date getGraduation() {
        return graduation;
    }
    public void setGraduation(Date graduation) {
        this.graduation = graduation;
    }
    public String getCurrState() {
        return currState;
    }
    public void setCurrState(String currState) {
        this.currState = currState;
    }
    public String getInstudy() {
        return instudy;
    }
    public void setInstudy(String instudy) {
        this.instudy = instudy;
    }
    public String getEducation() {
        return education;
    }
    public void setEducation(String education) {
        this.education = education;
    }
    public String getDegree() {
        return degree;
    }
    public void setDegree(String degree) {
        this.degree = degree;
    }
    public Date getTemporaryDate() {
        return temporaryDate;
    }
    public void setTemporaryDate(Date temporaryDate) {
        this.temporaryDate = temporaryDate;
    }
    public List<String> getDomainIdList() {
        return domainIdList;
    }
    public void setDomainIdList(List<String> domainIdList) {
        this.domainIdList = domainIdList;
    }
}

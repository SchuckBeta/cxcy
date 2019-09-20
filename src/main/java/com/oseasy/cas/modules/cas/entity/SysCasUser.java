/**
 * .
 */

package com.oseasy.cas.modules.cas.entity;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.oseasy.cas.common.config.CasSval;
import com.oseasy.cas.modules.cas.vo.ICasb;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.util.common.utils.IidEntity;
import com.oseasy.util.common.utils.StringUtil;

/**
 * .
 * @author chenhao
 *
 */
public class SysCasUser extends DataEntity<SysCasUser> implements IidEntity{
    private static final long serialVersionUID = 1L;
    public final static Logger logger = Logger.getLogger(SysCasUser.class);
    private String uid;
    private String rip;//关联用户表IP
    private String rid;//关联用户表ID
    private String ruid;
    private String rutype;//用户类型
    private String rname;
    private String rcname;
    private String rcontainerId;//容器ID
    private String rou;//OU
    private String rjson;//用户拓展信息
    private Date lastLoginDate;  // 最后登录时间
    private String type;    // 数据类型：1、安职
    private Boolean enable;    // 是否允许登录
    private Boolean checkUtype;    // 是否校验用户类型
    private Integer time;    //登录次数
    private Boolean isDeal;    //是否处理

    private Integer checkRet;    //检查sys_cas_user结果
    private Integer checkRetUser;    //检查sys_user结果
    private Integer checkRetRole;    //是否存在角色
    private Integer checkRetST;    //是否存在角色对应的子表

    private List<? extends ICasb> casbs;

    public List<? extends ICasb> getCasbs() {
        return casbs;
    }
    public void setCasbs(List<? extends ICasb> casbs) {
        this.casbs = casbs;
    }
    public String getRutype() {
        return rutype;
    }
    public String getRip() {
        return rip;
    }

    public Integer getCheckRetST() {
        return checkRetST;
    }
    public void setCheckRetST(Integer checkRetST) {
        this.checkRetST = checkRetST;
    }
    public void setRip(String rip) {
        this.rip = rip;
    }
    public String getRjson() {
        return rjson;
    }
    public void setRjson(String rjson) {
        this.rjson = rjson;
    }
    public Boolean getIsDeal() {
        return isDeal;
    }
    public void setIsDeal(Boolean isDeal) {
        this.isDeal = isDeal;
    }
    public void setRutype(String rutype) {
        this.rutype = rutype;
    }
    public Integer getCheckRet() {
        return checkRet;
    }
    public void setCheckRet(Integer checkRet) {
        this.checkRet = checkRet;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRid() {
        return rid;
    }
    public void setRid(String rid) {
        this.rid = rid;
    }
    public String getRuid() {
        return ruid;
    }

    public void setRuid(String ruid) {
        this.ruid = ruid;
    }
    public String getRname() {
        return rname;
    }
    public void setRname(String rname) {
        this.rname = rname;
    }
    public String getRcname() {
        return rcname;
    }
    public void setRcname(String rcname) {
        this.rcname = rcname;
    }
    public Date getLastLoginDate() {
        return lastLoginDate;
    }
    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public Integer getCheckRetUser() {
        return checkRetUser;
    }
    public void setCheckRetUser(Integer checkRetUser) {
        this.checkRetUser = checkRetUser;
    }
    public Boolean getCheckUtype() {
        if(this.checkUtype == null){
            this.checkUtype = true;
        }
        return checkUtype;
    }
    public void setCheckUtype(Boolean checkUtype) {
        this.checkUtype = checkUtype;
    }
    public String getType() {
        if(this.type == null){
            this.type =  CasSval.getCasTypes().get(0);
        }
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Boolean getEnable() {
        if(this.enable == null){
            this.enable =  false;
        }
        return enable;
    }
    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
    public Integer getTime() {
        return time;
    }
    public void setTime(Integer time) {
        this.time = time;
    }
    public String getRcontainerId() {
        return rcontainerId;
    }
    public void setRcontainerId(String rcontainerId) {
        this.rcontainerId = rcontainerId;
    }
    public String getRou() {
        return rou;
    }
    public void setRou(String rou) {
        this.rou = rou;
    }

    public Integer getCheckRetRole() {
        return checkRetRole;
    }
    public void setCheckRetRole(Integer checkRetRole) {
        this.checkRetRole = checkRetRole;
    }
    public String getLog() {
        return this.rcname + StringUtil.MAOH + this.rcname;
    }
}

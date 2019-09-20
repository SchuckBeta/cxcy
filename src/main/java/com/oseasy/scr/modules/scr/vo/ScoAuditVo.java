/**
 * .
 */

package com.oseasy.scr.modules.scr.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.scr.modules.scr.entity.ScoRapply;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 学分审核参数实体.
 * @author chenhao
 *
 */
public class ScoAuditVo implements Serializable{
    private static final long serialVersionUID = 1L;
    private String id;
    private String ignodeId;
    private String atype;
    private Double score;//认定分数,总分（operType = 0）
    private String isHalf;      // 是否需要折半：默认是：0、否；1、是
    private String remarks;
    private String appIds;//是否存在相同的项目
//    private List<String> appIds;//是否存在相同的项目
    private List<ScoMemberVo> members;

    @JsonIgnore
    private Double tcval;//认定计算总分（operType = 0）


    public String getAppId() {
//        if(StringUtil.checkNotEmpty(this.appIds)){
//            return this.appIds.get(0);
//        }
        if(StringUtil.isNotEmpty(this.appIds)){
            return this.appIds;
        }
        return "";
    }
    @JsonIgnore
    public Double getTcval() {
        return tcval;
    }

    public void setTcval(Double tcval) {
        this.tcval = tcval;
    }

    public String getAppIds() {
        return appIds;
    }

    public void setAppIds(String appIds) {
        this.appIds = appIds;
    }

    //    public List<String> getAppIds() {
//        if(appIds == null){
//            appIds = Lists.newArrayList();
//        }
//        return appIds;
//    }
//    public void setAppIds(List<String> appIds) {
//        this.appIds = appIds;
//    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getAtype() {
        return atype;
    }
    public void setAtype(String atype) {
        this.atype = atype;
    }
    public Double getScore() {
        return score;
    }
    public void setScore(Double score) {
        this.score = score;
    }

    public String getIgnodeId() {
        return ignodeId;
    }
    public void setIgnodeId(String ignodeId) {
        this.ignodeId = ignodeId;
    }
    public String getIsHalf() {
        if(isHalf == null){
            this.isHalf = Const.NO;
        }
        return isHalf;
    }
    public void setIsHalf(String isHalf) {
        this.isHalf = isHalf;
    }
    public List<ScoMemberVo> getMembers() {
        if(this.members == null){
            this.members = Lists.newArrayList();
        }
        return members;
    }
    public void setMembers(List<ScoMemberVo> members) {
        this.members = members;
    }
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * 计算学分算法.
     * @param scoaVo
     */
    public static ScoAuditVo countScore(ScoAuditVo scoaVo, ScoRapply rapply) {
        scoaVo.setTcval(scoaVo.getScore());
        if(StringUtil.checkEmpty(scoaVo.getMembers())){
            return scoaVo;
        }
        //处理配比数据
        scoaVo.setTcval(0.0);
        for (ScoMemberVo scoMemberVo : scoaVo.getMembers()) {
            scoaVo.setTcval(scoaVo.getTcval() + scoMemberVo.getScore());
        }
        return scoaVo;
    }
}

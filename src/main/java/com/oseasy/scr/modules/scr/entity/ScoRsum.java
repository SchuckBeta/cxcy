package com.oseasy.scr.modules.scr.entity;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.DataExtEntity;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.scr.modules.scr.vo.ScoAuditVo;
import com.oseasy.scr.modules.scr.vo.ScoMemberVo;
import com.oseasy.util.common.utils.DateUtil;

/**
 * 学分汇总Entity.
 * @author chenh
 * @version 2018-12-21
 */
public class ScoRsum extends DataExtEntity<ScoRsum> {

	private static final long serialVersionUID = 1L;
	private ScoRapply apply;        // 申请ID
	private ScoRule rule;		// 标准类型(二级、查询列表表头)ID
	private ScoRuleDetail rdetail;		// 标准详情ID
	private User user;		// 成员ID
	private double val;		// 实际成绩（针对范围区间的分数认定，必填）
	private String valType;		// 特殊值类型：便于处理有特殊情况的数据：默认：0:正常规则；10：折半处理；20:自定义分值；30:取最高分处理
	private String valRemarks;		// 特殊取值说明：比如，折半说明
    private List<ScoRsum> scoRsumList;
    private List<ScoCreditValue> scrProjectList; //总分查询
    private String name;
    private String vals;
    private List<ScoRule> entitys;
    private Double sum; //一个人的标准总分
    private String isSpro;      // 是否相同项目作废:0、否；1、是
    @Transient
    protected List<String> appIds;       // 查询Ids

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public List<ScoRule> getEntitys() {
        return entitys;
    }

    public void setEntitys(List<ScoRule> entitys) {
        this.entitys = entitys;
    }

    public List<String> getAppIds() {
        if(this.appIds == null){
            this.appIds = Lists.newArrayList();
        }
        return appIds;
    }

    public void setAppIds(List<String> appIds) {
        this.appIds = appIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVals() {
        return vals;
    }

    public void setVals(String vals) {
        this.vals = vals;
    }

    public List<ScoCreditValue> getScrProjectList() {
        return scrProjectList;
    }

    public void setScrProjectList(List<ScoCreditValue> scrProjectList) {
        this.scrProjectList = scrProjectList;
    }

    public ScoRsum() {
		super();
	}

	public ScoRsum(String id){
		super(id);
	}

	public ScoRsum(ScoRapply apply) {
        super();
        this.apply = apply;
    }

    public List<ScoRsum> getScoRsumList() {
        return scoRsumList;
    }

    public void setScoRsumList(List<ScoRsum> scoRsumList) {
        this.scoRsumList = scoRsumList;
    }

    public ScoRapply getApply() {
        return apply;
    }

    public void setApply(ScoRapply apply) {
        this.apply = apply;
    }

    public ScoRule getRule() {
        return rule;
    }

    public void setRule(ScoRule rule) {
        this.rule = rule;
    }

    public ScoRuleDetail getRdetail() {
        return rdetail;
    }

    public void setRdetail(ScoRuleDetail rdetail) {
        this.rdetail = rdetail;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Length(min=1, max=64, message="实际成绩（针对范围区间的分数认定，必填）长度必须介于 1 和 64 之间")
	public double getVal() {
		return val;
	}

	public void setVal(double val) {
		this.val = val;
	}

	@Length(min=1, max=255, message="特殊值类型：便于处理有特殊情况的数据：默认：0:正常规则；10：折半处理；20:自定义分值；30:取最高分处理长度必须介于 1 和 255 之间")
	public String getValType() {
		return valType;
	}

	public void setValType(String valType) {
		this.valType = valType;
	}

	@Length(min=1, max=64, message="特殊取值说明：比如，折半说明长度必须介于 1 和 64 之间")
	public String getValRemarks() {
		return valRemarks;
	}

	public void setValRemarks(String valRemarks) {
		this.valRemarks = valRemarks;
	}

	public String getIsSpro() {
        return isSpro;
    }

    public void setIsSpro(String isSpro) {
        this.isSpro = isSpro;
    }

    @Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

    public static List<ScoRsum> convert(ScoAuditVo scoaVo, ScoRapply rapply, boolean isSpro) {
        List<ScoRsum> scoRsums = Lists.newArrayList();
        for (ScoMemberVo scomVo : scoaVo.getMembers()) {
            scoRsums.add(ScoRsum.convert(rapply, new User(scomVo.getUid()), scomVo.getScore(), isSpro));
        }
        return scoRsums;
    }

    public static ScoRsum convert(ScoRapply rapply, User user, Double score, boolean isSpro) {
        ScoRsum scoRsum = new ScoRsum();
        scoRsum.setId(IdGen.uuid());
        scoRsum.setIsNewRecord(true);
        scoRsum.setApply(rapply);
        scoRsum.setCreateBy(UserUtils.getUser());
        scoRsum.setCreateDate(DateUtil.newDate());
        scoRsum.setRdetail(rapply.getRdetail());
        scoRsum.setRule(rapply.getRule());
        scoRsum.setUser(user);
        scoRsum.setVal(score);
        if(isSpro){
            scoRsum.setIsSpro(Const.YES);
        }else{
            scoRsum.setIsSpro(Const.NO);
        }
        return scoRsum;
    }
}
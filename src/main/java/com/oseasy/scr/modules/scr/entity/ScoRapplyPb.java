package com.oseasy.scr.modules.scr.entity;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.persistence.DataExtEntity;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.scr.modules.scr.vo.ScoAuditVo;
import com.oseasy.scr.modules.scr.vo.ScoMemberVo;
import com.oseasy.util.common.utils.DateUtil;

/**
 * 学分申请配比Entity.
 * @author chenh
 * @version 2018-12-26
 */
public class ScoRapplyPb extends DataExtEntity<ScoRapplyPb> {

	private static final long serialVersionUID = 1L;
	private ScoRapply apply;		// 申请编号
    private ScoRsum sum;        // 得分编号
	private ScoRuleDetail rdetail;     // 标准详情ID
	private User user;     // 用户
	private Integer val;		// 配比值：5
	private double score;		// 最后得分

	public ScoRapplyPb() {
		super();
	}

	public ScoRapplyPb(String id){
		super(id);
	}

	@NotNull(message="申请编号不能为空")
	public ScoRapply getApply() {
		return apply;
	}

	public void setApply(ScoRapply apply) {
		this.apply = apply;
	}

	public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ScoRsum getSum() {
        return sum;
    }

    public void setSum(ScoRsum sum) {
        this.sum = sum;
    }

    public ScoRuleDetail getRdetail() {
        return rdetail;
    }

    public void setRdetail(ScoRuleDetail rdetail) {
        this.rdetail = rdetail;
    }

    @Length(min=1, max=64, message="配比值：5长度必须介于 1 和 64 之间")
	public Integer getVal() {
		return val;
	}

	public void setVal(Integer val) {
		this.val = val;
	}

	@Length(min=1, max=64, message="最后得分长度必须介于 1 和 64 之间")
	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public static List<ScoRapplyPb> convert(ScoAuditVo scoaVo, ScoRapply rapply) {
        List<ScoRapplyPb> scoRapplyPbs = Lists.newArrayList();
        for (ScoMemberVo scomVo : scoaVo.getMembers()) {
            scoRapplyPbs.add(ScoRapplyPb.convert(rapply, scomVo));
        }
        return scoRapplyPbs;
    }
	public static List<ScoRapplyPb> convert(List<ScoRsum> scoRsums, ScoAuditVo scoaVo, ScoRapply rapply) {
	    List<ScoRapplyPb> scoRapplyPbs = Lists.newArrayList();
	    for (ScoMemberVo scomVo : scoaVo.getMembers()) {
	        for (ScoRsum scoRsum : scoRsums) {
	            if((scoRsum.getUser() != null) && (scomVo.getUid()).equals(scoRsum.getUser().getId())){
	                scoRapplyPbs.add(ScoRapplyPb.convert(rapply, scomVo, scoRsum));
	                break;
	            }
	        }
	    }
	    return scoRapplyPbs;
	}

    public static ScoRapplyPb convert(ScoRapply rapply, ScoMemberVo scomVo) {
        return convert(rapply, scomVo, null);
    }
    public static ScoRapplyPb convert(ScoRapply rapply, ScoMemberVo scomVo, ScoRsum scoRsum) {
        ScoRapplyPb scoRapplyPb = new ScoRapplyPb();
        scoRapplyPb.setId(IdGen.uuid());
        scoRapplyPb.setIsNewRecord(true);
        scoRapplyPb.setApply(rapply);
        scoRapplyPb.setCreateBy(UserUtils.getUser());
        scoRapplyPb.setCreateDate(DateUtil.newDate());
        scoRapplyPb.setRdetail(rapply.getRdetail());
        scoRapplyPb.setScore(scomVo.getScore());
        scoRapplyPb.setVal(scomVo.getRate());

        if(scoRsum != null){
            scoRapplyPb.setSum(scoRsum);
            scoRapplyPb.setUser(scoRsum.getUser());
        }else{
            if(scomVo != null){
                scoRapplyPb.setUser(new User(scomVo.getUid()));
            }
        }
        return scoRapplyPb;
    }

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
package com.oseasy.scr.modules.scr.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataExtEntity;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;

/**
 * 学分申请记录Entity.
 * @author chenh
 * @version 2018-12-21
 */
public class ScoRapplyRecord extends DataExtEntity<ScoRapplyRecord> {

	private static final long serialVersionUID = 1L;
    public static final String TABLEA = "a.";
	/**
	 * 申请ID
 	 */
	private ScoRapply apply;
	/**
	 * 申请人ID
	 */
	private User user;

    private User autBy;     // 审核人
	/**
	 * 状态:0、待审核 1、通过;2、未通过
	 */
	private String status;

	public ScoRapplyRecord() {
		super();
	}

	public ScoRapplyRecord(String id){
		super(id);
	}
	public ScoRapplyRecord(ScoRapply apply,User user,String status){
		super();
		this.apply=apply;
		this.user=user;
		this.status=status;
	}
	public ScoRapplyRecord(ScoRapply apply,User user,User autBy,String status){
	    super();
	    this.apply=apply;
	    this.user=user;
	    this.autBy=autBy;
	    this.status=status;
	}

	public User getAutBy() {
        return autBy;
    }

    public void setAutBy(User autBy) {
        this.autBy = autBy;
    }

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

    @Length(min=1, max=1, message="状态:0、待审核 1、通过;2、未通过长度必须介于 1 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

    /**
     * .
     * @param scoRapply
     * @return
     */
    public static ScoRapplyRecord convert(ScoRapply scoRapply, String remarks) {
        ScoRapplyRecord rapplyRecord = new ScoRapplyRecord();
        rapplyRecord.setId(IdGen.uuid());
        rapplyRecord.setIsNewRecord(true);
        rapplyRecord.setApply(scoRapply);
        rapplyRecord.setUser(scoRapply.getUser());
        rapplyRecord.setAutBy(scoRapply.getAutBy());
        rapplyRecord.setStatus(scoRapply.getStatus());
        rapplyRecord.setCreateBy(UserUtils.getUser());
        rapplyRecord.setCreateDate(scoRapply.getAutDate());
        rapplyRecord.setRemarks(remarks);
        return rapplyRecord;
    }
}
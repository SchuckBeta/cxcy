package com.oseasy.pw.modules.pw.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 入驻续期历史Entity.
 * @author zy
 * @version 2018-01-02
 */
public class PwEnterXqhistory extends DataEntity<PwEnterXqhistory> {

	private static final long serialVersionUID = 1L;
	private String eid;		// 入驻id
	private Integer term;		// 期限,单位：天
	private Date startDate;		// 开始时间
	private Date endDate;		// 结束时间

	public PwEnterXqhistory() {
		super();
	}

	public PwEnterXqhistory(String id){
		super(id);
	}

	@Length(min=1, max=64, message="入驻id长度必须介于 1 和 64 之间")
	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	public Integer getTerm() {
		return term;
	}

	public void setTerm(Integer term) {
		this.term = term;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
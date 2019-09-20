package com.oseasy.dr.modules.dr.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 卡记录规则时间Entity.
 * @author chenh
 * @version 2018-05-16
 */
public class DrCardreGtime extends DataEntity<DrCardreGtime> {

	private static final long serialVersionUID = 1L;
	private DrCardreGroup group;		// 预警规则
	private String sort;		// 排序
	private Date beginDate;		// 开始时间
	private Date endDate;		// 结束时间
	private String status;		// 时间开关

	public DrCardreGtime() {
		super();
	}

	public DrCardreGtime(String id){
		super(id);
	}

	public DrCardreGtime(DrCardreGroup group) {
        super();
        this.group = group;
    }

    public DrCardreGroup getGroup() {
        return group;
    }

    public void setGroup(DrCardreGroup group) {
        this.group = group;
    }

    @Length(min=1, max=11, message="排序长度必须介于 1 和 11 之间")
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Length(min=0, max=2, message="时间开关长度必须介于 0 和 2 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
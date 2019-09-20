package com.oseasy.scr.modules.sco.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 技能学分认定Entity.
 * @author chenhao
 * @version 2017-07-18
 */
public class ScoAffirmSkill extends DataEntity<ScoAffirmSkill> {

	private static final long serialVersionUID = 1L;
	private String procInsId;		// 流程实例ID
	private String item;		// 学分项
	private String name;		// 认定项目名称
	private Date prizeDate;		// 获奖时间
	private String category;		// 认定级别/类别
	private String status;		// 状态
	private Double score;		// 认定学分

	public ScoAffirmSkill() {
		super();
	}

	public ScoAffirmSkill(String id) {
		super(id);
	}

	@Length(min=1, max=64, message="流程实例ID长度必须介于 1 和 64 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}

	@Length(min=0, max=64, message="学分项长度必须介于 0 和 64 之间")
	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	@Length(min=0, max=128, message="认定项目名称长度必须介于 0 和 128 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getPrizeDate() {
		return prizeDate;
	}

	public void setPrizeDate(Date prizeDate) {
		this.prizeDate = prizeDate;
	}

	@Length(min=0, max=64, message="认定级别/类别长度必须介于 0 和 64 之间")
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Length(min=0, max=64, message="状态长度必须介于 0 和 64 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

}
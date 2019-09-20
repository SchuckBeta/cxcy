package com.oseasy.sys.modules.sys.entity;

import net.sf.json.JSONObject;
import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 编号规则详情表Entity.
 * @author 李志超
 * @version 2018-05-17
 */
public class SysNumberRuleDetail extends DataEntity<SysNumberRuleDetail> {

	private static final long serialVersionUID = 1L;
	private String ruleType;		// 规则类型
	private String text;		// 规则细则
	private String numLength;		// 随机数或自增数长度
	private String proNumberRuleId;		// pro_number_rule_id
	private Integer sort;
	private String typeValue;		// 类型键值对
	private JSONObject jsMap;		// 类型键值对

	public JSONObject getJsMap() {
		return jsMap;
	}

	public void setJsMap(JSONObject jsMap) {
		this.jsMap = jsMap;
	}

	public SysNumberRuleDetail() {
		super();
	}

	public SysNumberRuleDetail(String id){
		super(id);
	}

	@Length(min=0, max=64, message="规则类型长度必须介于 0 和 64 之间")
	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	@Length(min=0, max=64, message="规则细则长度必须介于 0 和 64 之间")
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Length(min=0, max=11, message="随机数或自增数长度长度必须介于 0 和 11 之间")
	public String getNumLength() {
		return numLength;
	}

	public void setNumLength(String numLength) {
		this.numLength = numLength;
	}

	@Length(min=0, max=64, message="pro_number_rule_id长度必须介于 0 和 64 之间")
	public String getProNumberRuleId() {
		return proNumberRuleId;
	}

	public void setProNumberRuleId(String proNumberRuleId) {
		this.proNumberRuleId = proNumberRuleId;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getTypeValue() {
		return typeValue;
	}

	public void setTypeValue(String typeValue) {
		this.typeValue = typeValue;
	}
}
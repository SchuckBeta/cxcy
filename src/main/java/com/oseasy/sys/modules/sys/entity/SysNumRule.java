package com.oseasy.sys.modules.sys.entity;

import org.hibernate.validator.constraints.Length;
import org.joda.time.Hours;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 编号规则Entity
 * @author zdk
 * @version 2017-04-01
 */
public class SysNumRule extends DataEntity<SysNumRule> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// 项目01，大赛02，科研成果03
	private String name;		// 类型名称
	private String prefix;		// prefix
	private String suffix;		// suffix
	private String dateFormat;		// YYYYMMDD
	private String timieFormat;		// HHMMSS
	private String startNum;		// 01
	private String numLength;		// 3
	
	private String year;       //年
	private String month;      //月
	private String day;        //日
	private String hour;       //时
	private String minute;       //分
	private String second;        
	
	public SysNumRule() {
		super();
	}

	public SysNumRule(String id) {
		super(id);
	}

	@Length(min=0, max=1, message="项目01，大赛02，科研成果03长度必须介于 0 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=32, message="类型名称长度必须介于 0 和 32 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=32, message="prefix长度必须介于 0 和 32 之间")
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	@Length(min=0, max=32, message="suffix长度必须介于 0 和 32 之间")
	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
	@Length(min=0, max=32, message="YYYYMMDD长度必须介于 0 和 32 之间")
	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	@Length(min=0, max=32, message="HHMMSS长度必须介于 0 和 32 之间")
	public String getTimieFormat() {
		return timieFormat;
	}

	public void setTimieFormat(String timieFormat) {
		this.timieFormat = timieFormat;
	}
	
	@Length(min=0, max=11, message="01长度必须介于 0 和 11 之间")
	public String getStartNum() {
		return startNum;
	}

	public void setStartNum(String startNum) {
		this.startNum = startNum;
	}
	
	@Length(min=0, max=11, message="3长度必须介于 0 和 11 之间")
	public String getNumLength() {
		return numLength;
	}

	public void setNumLength(String numLength) {
		this.numLength = numLength;
	}

	public String getYear() {
		return year;
	}

	public String getMonth() {
		return month;
	}

	public String getDay() {
		return day;
	}

	public String getHour() {
		return hour;
	}

	public String getMinute() {
		return minute;
	}

	public String getSecond() {
		return second;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public void setMinute(String minute) {
		this.minute = minute;
	}

	public void setSecond(String second) {
		this.second = second;
	}
	
	
	
}
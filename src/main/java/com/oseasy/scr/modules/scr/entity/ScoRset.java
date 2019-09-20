package com.oseasy.scr.modules.scr.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataExtEntity;

/**
 * 设置学分规则Entity.
 * @author liangjie
 * @version 2018-12-27
 */
public class ScoRset extends DataExtEntity<ScoRset> {

	private static final long serialVersionUID = 1L;

	public static String VAL_1="1";
	public static String VAL_2="2";
	public static String VAL_3="3";

	private String isKeepNpoint;		// 系统自动保留小数点 1：是自动，0不是自动
	private String keepNpoint;		// 小数点位数
	private String isRprd;		// 是否最后一位四舍五入 1：是自动，0不是自动
	private String isSnum;		// 是否低于X分（0否1是）
	private Float snumMin;		// 低于x分
	private Float snumVal;		// 按照x分
	private String isSnumlimit;		// 是否认定总分不超过X分（0否1是）
	private Float snumlimit;		// 认定总分

	public ScoRset() {
		super();
	}

	public ScoRset(String id){
		super(id);
	}
	public ScoRset(String tenantId,String delFlag){
		this.tenantId=tenantId;
		this.delFlag=delFlag;
	}
	public ScoRset(String id,String isKeepNpoint,String keepNpoint,String isRprd,String isSnum,String tenantId){
		this.id = id;
		this.isKeepNpoint = isKeepNpoint;
		this.keepNpoint = keepNpoint;
		this.isRprd = isRprd;
		this.isSnum = isSnum;
		this.tenantId = tenantId;
	}

	public String getIsKeepNpoint() {
		return isKeepNpoint;
	}

	public void setIsKeepNpoint(String isKeepNpoint) {
		this.isKeepNpoint = isKeepNpoint;
	}

	public String getKeepNpoint() {
		return keepNpoint;
	}

	public void setKeepNpoint(String keepNpoint) {
		this.keepNpoint = keepNpoint;
	}

	public String getIsRprd() {
		return isRprd;
	}

	public void setIsRprd(String isRprd) {
		this.isRprd = isRprd;
	}

	public String getIsSnum() {
		return isSnum;
	}

	public void setIsSnum(String isSnum) {
		this.isSnum = isSnum;
	}

	public String getIsSnumlimit() {
		return isSnumlimit;
	}

	public void setIsSnumlimit(String isSnumlimit) {
		this.isSnumlimit = isSnumlimit;
	}

	public Float getSnumMin() {
		return snumMin;
	}

	public void setSnumMin(Float snumMin) {
		this.snumMin = snumMin;
	}

	public Float getSnumVal() {
		return snumVal;
	}

	public void setSnumVal(Float snumVal) {
		this.snumVal = snumVal;
	}

	public Float getSnumlimit() {
		return snumlimit;
	}

	public void setSnumlimit(Float snumlimit) {
		this.snumlimit = snumlimit;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
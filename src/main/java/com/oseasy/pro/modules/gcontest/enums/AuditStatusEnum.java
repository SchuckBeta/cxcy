package com.oseasy.pro.modules.gcontest.enums;

public enum AuditStatusEnum {
	 S1("1","网评学院专家打分")
	,S2("2","网评学院秘书审核")
	,S3("3","网评学校专家打分")
	,S4("4","网评学校管理员审核")
	,S5("5","路演学校管理员审核")
	,S6("6","评级学校管理员审核")
/*	,S8("8","项目终止")
	,S9("9","项目结项")*/
	;

	private String value;
	private String name;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private AuditStatusEnum(String value, String name) {
		this.value=value;
		this.name=name;
	}
	public static String getNameByValue(String value) {
		if (value!=null) {
			for(AuditStatusEnum e:AuditStatusEnum.values()) {
				if (e.value.equals(value)) {
					return e.name;
				}
			}
		}
		return "";
	}
	
}

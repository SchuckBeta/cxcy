package com.oseasy.pro.modules.project.enums;

public enum ProjectMidResultEnum {
	 S0("0","合格")
	,S2("2","不合格")
	;

	//(0合格，2不合格）
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

	private ProjectMidResultEnum(String value, String name) {
		this.value=value;
		this.name=name;
	}
	public static String getNameByValue(String value) {
		if (value!=null) {
			for(ProjectMidResultEnum e:ProjectMidResultEnum.values()) {
				if (e.value.equals(value)) {
					return e.name;
				}
			}
		}
		return "";
	}
	
}

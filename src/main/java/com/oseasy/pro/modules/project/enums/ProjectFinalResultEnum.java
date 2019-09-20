package com.oseasy.pro.modules.project.enums;

public enum ProjectFinalResultEnum {
	 S0("0","合格")
	,S1("1","优秀")
	,S2("2","不合格")
	,S3("3","立项不合格")
	,S4("4","项目终止")
	,S6("6","良好")
	;

	//(0合格，1优秀，2不合格，3立项不合格，4中期不合格）
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

	private ProjectFinalResultEnum(String value, String name) {
		this.value=value;
		this.name=name;
	}
	public static String getNameByValue(String value) {
		if (value!=null) {
			for(ProjectFinalResultEnum e:ProjectFinalResultEnum.values()) {
				if (e.value.equals(value)) {
					return e.name;
				}
			}
		}
		return "";
	}


}

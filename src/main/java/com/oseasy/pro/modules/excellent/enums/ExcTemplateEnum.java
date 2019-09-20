package com.oseasy.pro.modules.excellent.enums;

public enum ExcTemplateEnum {
	 R1("1","双创项目展示模板","project.xml")
	,R2("2","互联网+大赛展示模板","gcontest.xml")
	;

	private String value;//code
	private String name;//名称
	private String tempName;//模板文件名称
	
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

	public String getTempName() {
		return tempName;
	}

	public void setTempName(String tempName) {
		this.tempName = tempName;
	}

	private ExcTemplateEnum(String value, String name,String tempName) {
		this.value=value;
		this.name=name;
		this.tempName=tempName;
	}
	public static ExcTemplateEnum getByValue(String value) {
		if (value!=null) {
			for(ExcTemplateEnum e:ExcTemplateEnum.values()) {
				if (e.value.equals(value)) {
					return e;
				}
			}
		}
		return null;
	}
	
}

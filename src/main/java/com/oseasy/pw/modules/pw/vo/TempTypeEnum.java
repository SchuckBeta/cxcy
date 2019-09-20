package com.oseasy.pw.modules.pw.vo;

public enum TempTypeEnum {
	 R0("1","保存")
	,R1("0","提交")
	;
	private String value;//code
	private String name;//名称

	public String getValue() {
		return value;
	}

	public String getName() {
		return name;
	}



	private TempTypeEnum(String value, String name) {
		this.value=value;
		this.name=name;

	}
	public static TempTypeEnum getByValue(String value) {
		if (value!=null) {
			for(TempTypeEnum e: TempTypeEnum.values()) {
				if (e.value.equals(value)) {
					return e;
				}
			}
		}
		return null;
	}

}

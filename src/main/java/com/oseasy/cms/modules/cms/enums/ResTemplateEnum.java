package com.oseasy.cms.modules.cms.enums;

public enum ResTemplateEnum {
	 R1("1","首页Banner","home-banner.xml")
	,R2("2","首页通知通告","home-notice.xml")
	,R3("3","首页大赛热点","home-hots.xml")
	,R4("4","首页优秀项目展示","home-show.xml")
	,R5("5","首页导师风采","home-dsfc.xml")
	;

	private String value;//code
	private String name;//名称
	private String tempName;//模板文件名称

	public String getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	public String getTempName() {
		return tempName;
	}

	private ResTemplateEnum(String value, String name,String tempName) {
		this.value=value;
		this.name=name;
		this.tempName=tempName;
	}
	public static ResTemplateEnum getByValue(String value) {
		if (value!=null) {
			for(ResTemplateEnum e:ResTemplateEnum.values()) {
				if (e.value.equals(value)) {
					return e;
				}
			}
		}
		return null;
	}

}

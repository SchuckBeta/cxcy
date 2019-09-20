package com.oseasy.pro.modules.cert.enums;

public enum ElementTypeEnum {
	MAIN_PIC("1", "图片"),
	TEXT("2", "文本"),
	VAR("3", "变量"),
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

	private ElementTypeEnum(String value, String name) {
		this.value = value;
		this.name = name;
	}

	/**
	 * 根据key获取枚举 .
	 *
	 * @author chenhao
	 * @param key
	 *            枚举标识
	 */
	public static ElementTypeEnum getByValue(String value) {
		if ((value != null)) {
			ElementTypeEnum[] entitys = ElementTypeEnum.values();
			for (ElementTypeEnum entity : entitys) {
				if ((value).equals(entity.getValue())) {
					return entity;
				}
			}
		}
		return null;
	}

	public static String getNameByValue(String value) {
		if (value != null) {
			for (ElementTypeEnum e : ElementTypeEnum.values()) {
				if (e.value.equals(value)) {
					return e.name;
				}
			}
		}
		return "";
	}
}

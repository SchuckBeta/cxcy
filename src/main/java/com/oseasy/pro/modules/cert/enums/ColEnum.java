package com.oseasy.pro.modules.cert.enums;

import net.sf.json.JSONObject;

public enum ColEnum {
	C1("col_1", "项目名称"),
	C2("col_2", "项目类型"),
	C3("col_3", "项目类别"),
	C4("col_4", "项目编号"),
	C5("col_5", "项目级别"),
	C6("col_6", "指导教师"),
	C7("col_7", "负责人"),
	C8("col_8", "项目成员"),
	C9("col_9", "项目周期"),
	C10("col_10", "发证年月(大写)"),
	C11("col_11", "发证年(大写)"),
	C12("col_12", "发证月(大写)"),
	C13("col_13", "发证年(小写)"),
	C14("col_14", "发证月(小写)"),
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

	private ColEnum(String value, String name) {
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
	public static ColEnum getByValue(String value) {
		if ((value != null)) {
			ColEnum[] entitys = ColEnum.values();
			for (ColEnum entity : entitys) {
				if ((value).equals(entity.getValue())) {
					return entity;
				}
			}
		}
		return null;
	}

	public static String getNameByValue(String value) {
		if (value != null) {
			for (ColEnum e : ColEnum.values()) {
				if (e.value.equals(value)) {
					return e.name;
				}
			}
		}
		return "";
	}
    @Override
    public String toString() {
        JSONObject object = new JSONObject();
        object.put("value",value);
        object.put("name",name);
        return object.toString();
    }
}

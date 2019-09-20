package com.oseasy.sys.modules.sys.enums;

import com.oseasy.util.common.utils.StringUtil;

/**
 * 用户角色业务类型枚举. 对应Role的bizType
 * 
 * @author 灞波儿奔
 */
public enum  TeamDutyEnum {
	FZR("1", "负责人"),

	ZY("2", "组员"),

	ZDDS("3", "指导导师");

	private String value;
	private String name;

	private TeamDutyEnum(String value, String name) {
		this.value = value;
		this.name = name;
	}

	/**
	 * 根据类型获取枚举。
	 * 
	 * @param value
	 * @return RoleBizTypeEnum
	 */
	public static TeamDutyEnum getByValue(String value) {
		if (StringUtil.isNotEmpty(value)) {
			for (TeamDutyEnum e : TeamDutyEnum.values()) {
				if (e.getValue().equals(value)) {
					return e;
				}
			}
		}
		return null;
	}

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

}

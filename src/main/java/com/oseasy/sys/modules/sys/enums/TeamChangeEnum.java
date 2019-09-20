package com.oseasy.sys.modules.sys.enums;

import com.oseasy.util.common.utils.StringUtil;

/**
 * 用户角色业务类型枚举. 对应Role的bizType
 * 
 * @author 灞波儿奔
 */
public enum TeamChangeEnum {
	ADD("1", "添加"),

	DEL("2", "删除"),

	MOD("3", "变更");

	private String value;
	private String name;

	private TeamChangeEnum(String value, String name) {
		this.value = value;
		this.name = name;
	}

	/**
	 * 根据类型获取枚举。
	 * 
	 * @param value
	 * @return RoleBizTypeEnum
	 */
	public static TeamChangeEnum getByValue(String value) {
		if (StringUtil.isNotEmpty(value)) {
			for (TeamChangeEnum e : TeamChangeEnum.values()) {
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

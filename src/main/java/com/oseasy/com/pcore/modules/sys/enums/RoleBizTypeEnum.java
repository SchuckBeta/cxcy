package com.oseasy.com.pcore.modules.sys.enums;

import com.oseasy.util.common.utils.StringUtil;

/**
 * 用户角色业务类型枚举. 对应Role的bizType
 *
 * @author 灞波儿奔
 */
public enum RoleBizTypeEnum {
	/**
	 * 8
	 */
	CJGLY("8", "超级管理员"),
	/**
	 * 7
	 */
	XTGLY("7", "系统管理员"),
	/**
	 * 6
	 */
	XXGLY("6", "学校管理员"),
	/**
	 * 5
	 */
	XXZJ("5", "学校专家"),
	/**
	 * 4
	 */
	XYZJ("4", "学院专家"),
	/**
	 * 3
	 */
	XYMS("3","学院秘书"),
	/**
	 * 2
	 */
	DS("2", "导师"),
	/**
	 * 1
	 */
	XS("1", "学生");

	private String value;
	private String name;

	private RoleBizTypeEnum(String value, String name) {
		this.value = value;
		this.name = name;
	}

	/**
	 * 根据类型获取枚举。
	 *
	 * @param value
	 * @return RoleBizTypeEnum
	 */
	public static RoleBizTypeEnum getByValue(String value) {
		if (StringUtil.isNotEmpty(value)) {
			for (RoleBizTypeEnum e : RoleBizTypeEnum.values()) {
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

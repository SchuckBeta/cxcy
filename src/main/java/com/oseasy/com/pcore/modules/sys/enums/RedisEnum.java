package com.oseasy.com.pcore.modules.sys.enums;

import com.oseasy.util.common.utils.StringUtil;

/**

 *

 */
public enum RedisEnum {
	/**
	 * 用户
	 */
	USER("user", "用户"),
	/**
	 *机构
	 */
	OFFICE("office", "机构"),
	/**
	 *菜单
	 */
	MENU( "menu","菜单"),
	/**
	 *角色
	 */
	ROLE("role","角色"),
	/**
	 *数据字典
	 */
	DICT("dict","数据字典"),
	/**
	 *项目
	 */
	PROMODEL("promodel","项目"),
	/**
	 *会话
	 */
	SESSION("session","会话"),
	/**
	 *项目类型
	 */
	ACTYW("actyw","项目类型");

	private String value;
	private String name;

	RedisEnum(String value, String name) {
		this.value = value;
		this.name = name;
	}

	/**
	 * 根据类型获取枚举。
	 *
	 * @param value
	 * @return RoleBizTypeEnum
	 */
	public static RedisEnum getByValue(String value) {
		if (StringUtil.isNotEmpty(value)) {
			for (RedisEnum e : RedisEnum.values()) {
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

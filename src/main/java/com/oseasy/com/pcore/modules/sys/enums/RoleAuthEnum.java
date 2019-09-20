package com.oseasy.com.pcore.modules.sys.enums;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.util.common.utils.StringUtil;

import java.util.HashMap;
import java.util.List;

/**
 * 用户角色权限
 */
public enum RoleAuthEnum {
	CJGLY("1", "超级管理员","1"),
	XTGLY("10", "系统管理员","10"),
	XXGLY("20", "学校管理员","3"),
	XYMS("30","学院秘书","431e3fc9adb248279221c0ab5b3b717f"),
	XYZJ("40", "学院专家","ef8b7924557747e2ac71fe5b52771c08"),
	XXZJ("40", "学校专家","ecee0da215d04186bdeea0373bf8eeea"),
	XWZJ("40", "校外专家","2494442643c24193bc1aa480eddcf43f"),
	DS("50", "导师","21999752ae6049e2bc3d53e8baaac9a5"),
	XS("60", "学生","13757518f4da45ecaa32a3b582e8396a");

	private String value;
	private String name;
	private String roleId;

	private RoleAuthEnum(String value, String name,String roleId) {
		this.value = value;
		this.name = name;
		this.roleId = roleId;
	}

	/**
	 * 根据类型获取枚举。
	 *
	 * @param value
	 * @return RoleBizTypeEnum
	 */
	public static RoleAuthEnum getByValue(String value) {
		if (StringUtil.isNotEmpty(value)) {
			for (RoleAuthEnum e : RoleAuthEnum.values()) {
				if (e.getValue().equals(value)) {
					return e;
				}
			}
		}
		return null;
	}

	/**
	 * 根据类型获取枚举。
	 *
	 * @param value
	 * @return RoleBizTypeEnum
	 */
	public static RoleAuthEnum getByRoleId(String roleId) {
		if (StringUtil.isNotEmpty(roleId)) {
			for (RoleAuthEnum e : RoleAuthEnum.values()) {
				if (e.getRoleId().equals(roleId)) {
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

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public static List<Object> getRoleAuthList(){
		RoleAuthEnum[] entitys = RoleAuthEnum.values();
		List<Object> roleAuthList = Lists.newArrayList();
		for(RoleAuthEnum entity : entitys){
			HashMap<String, String> hashMap = new HashMap<>();
			hashMap.put("label", entity.getName());
			hashMap.put("value", entity.getValue());
			hashMap.put("id", entity.getRoleId());
			roleAuthList.add(hashMap);
		}
		return roleAuthList;
	}
}

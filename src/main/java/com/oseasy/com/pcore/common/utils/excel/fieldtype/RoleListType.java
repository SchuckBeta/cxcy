/**
 *
 */
package com.oseasy.com.pcore.common.utils.excel.fieldtype;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.util.common.utils.Collections3;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 字段类型转换


 */
public class RoleListType {

	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		List<Role> roleList = Lists.newArrayList();
		List<Role> allRoleList = CoreUtils.getRoleList();
		for (String s : StringUtil.split(val, ",")) {
			for (Role e : allRoleList) {
				if (StringUtil.trimToEmpty(s).equals(e.getName())) {
					roleList.add(e);
				}
			}
		}
		return roleList.size()>0?roleList:null;
	}

	/**
	 * 设置对象值（导出）
	 */
	public static String setValue(Object val) {
		if (val != null) {
			@SuppressWarnings("unchecked")
			List<Role> roleList = (List<Role>)val;
			return Collections3.extractToString(roleList, "name", ", ");
		}
		return "";
	}

}

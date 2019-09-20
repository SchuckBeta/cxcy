/**
 *
 */
package com.oseasy.com.pcore.common.utils.excel.fieldtype;

import com.oseasy.com.pcore.modules.sys.entity.Area;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 字段类型转换


 */
public class AreaType {

	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		for (Area e : CoreUtils.getAreaList()) {
			if (StringUtil.trimToEmpty(val).equals(e.getName())) {
				return e;
			}
		}
		return null;
	}

	/**
	 * 获取对象值（导出）
	 */
	public static String setValue(Object val) {
		if (val != null && ((Area)val).getName() != null) {
			return ((Area)val).getName();
		}
		return "";
	}
}

package com.oseasy.com.pcore.common.persistence;

import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.util.common.utils.StringUtil;

import java.io.Serializable;

/**
 * 角色判断实体类
 * @version 2014-05-16
 */
public abstract class OfficeEntity<T> extends TreeEntity<T> implements Serializable{
	private static final long serialVersionUID = 1L;

	public OfficeEntity() {
	}

	public OfficeEntity(String id) {
		super(id);
	}

	/****************************************************************************************
	 * 运营平台
	 ****************************************************************************************/
	/**
	 * 用户是否为运营中心租户.
	 * @param officeId 用户ID
	 * @return boolean
	 */
	public static boolean isTenant(String officeId) {
		return (StringUtil.isNotEmpty(officeId) && (CoreIds.NCE_SYS_OFFICE_TOP.getId()).equals(officeId));
	}

	/**
	 * 生成运营中心顶级机构.
	 * @return T
	 */
	public T genOffice() {
		//TODO CHENHAO
		return null;
	}

	/**
	 * 初始化运营中心顶级机构.
	 * @return T
	 */
	public void initOffice() {
		//TODO CHENHAO
	}

	/****************************************************************************************
	 * 省平台
	 ****************************************************************************************/
	/**
	 * 用户是否为省顶级机构.
	 * @param officeId 用户ID
	 * @return boolean
	 */
	public static boolean isNprTenant(String officeId) {
		return (StringUtil.isNotEmpty(officeId) && (CoreIds.NPR_SYS_OFFICE_TOP.getId()).equals(officeId));
	}

	/**
	 * 生成省顶级机构.
	 * @return T
	 */
	public T genNprOffice() {
		//TODO CHENHAO
		return null;
	}

	/**
	 * 初始化省模板顶级机构.
	 * @return T
	 */
	public void initNprOffice() {
		//TODO CHENHAO
	}

	/****************************************************************************************
	 * 校平台
	 ****************************************************************************************/
	/**
	 * 用户是否为校顶级机构.
	 * @param officeId 用户ID
	 * @return boolean
	 */
	public static boolean isNscOffice(String officeId) {
		return (StringUtil.isNotEmpty(officeId) && (CoreIds.NSC_SYS_OFFICE_TOP.getId()).equals(officeId));
	}

	/**
	 * 生成校顶级机构.
	 * @return T
	 */
	public T genNscOffice() {
		//TODO CHENHAO
		return null;
	}

	/**
	 * 初始化校模板顶级机构.
	 * @return T
	 */
	public T initNscOffice() {
		//TODO CHENHAO
		return null;
	}
}

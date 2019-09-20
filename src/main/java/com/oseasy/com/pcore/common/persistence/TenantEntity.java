package com.oseasy.com.pcore.common.persistence;

import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.util.common.utils.StringUtil;

import java.io.Serializable;

/**
 * 角色判断实体类
 * @version 2014-05-16
 */
public abstract class TenantEntity<T> extends DataEntity<T> implements Serializable{
	private static final long serialVersionUID = 1L;

	public TenantEntity() {
	}

	public TenantEntity(String id) {
		super(id);
	}

	/****************************************************************************************
	 * 运营平台
	 ****************************************************************************************/
	/**
	 * 用户是否为运营中心租户.
	 * @param tenantId 用户ID
	 * @return boolean
	 */
	public static boolean isTenant(String tenantId) {
		return (StringUtil.isNotEmpty(tenantId) && (CoreIds.NCE_SYS_TENANT.getId()).equals(tenantId));
	}

	/**
	 * 生成运营中心租户.
	 * @return T
	 */
	public T genTenant() {
		//TODO CHENHAO
		return null;
	}

	/**
	 * 初始化运营中心模板租户.
	 * @return T
	 */
	public void initTenant() {
		//TODO CHENHAO
	}

	/****************************************************************************************
	 * 省平台
	 ****************************************************************************************/
	/**
	 * 用户是否为省租户.
	 * @param tenantId 用户ID
	 * @return boolean
	 */
	public static boolean isNprTenant(String tenantId) {
		return (StringUtil.isNotEmpty(tenantId) && (CoreIds.NPR_SYS_TENANT.getId()).equals(tenantId));
	}

	/**
	 * 生成省租户.
	 * @return T
	 */
	public T genNprTenant() {
		//TODO CHENHAO
		return null;
	}

	/**
	 * 初始化省模板租户.
	 * @return T
	 */
	public void initNprTenant() {
		//TODO CHENHAO
	}

	/****************************************************************************************
	 * 校平台
	 ****************************************************************************************/
	/**
	 * 用户是否为校租户.
	 * @param tenantId 用户ID
	 * @return boolean
	 */
	public static boolean isNscTenant(String tenantId) {
		return (StringUtil.isNotEmpty(tenantId) && (CoreIds.NSC_SYS_TENANT.getId()).equals(tenantId));
	}

	/**
	 * 生成校租户.
	 * @return T
	 */
	public T genNscTenant() {
		//TODO CHENHAO
		return null;
	}

	/**
	 * 初始化校模板租户.
	 * @return T
	 */
	public void initNscTenant() {
		//TODO CHENHAO
	}
}

package com.oseasy.cms.common.config;

/**
 * 系统缓存键说明
 * @author Administrator
 *
 */
public enum SysCacheKeys {
	SITE_IS_EXIST_INDEX_CACHE("isExistIndexCache", "前台首页缓存状态"),
	SITE_CATEGORYS_INDEX("siteIndexCategorys", "前台首页栏目"),
	SITE_CATEGORYS_INDEX_FIRST("siteIndexCategorysFirst", "前台首页栏目-一级"),
	SITE_CATEGORYS_INDEX_SENCOND("siteIndexCategorysSencond", "前台首页栏目-二级"),
	OA_CACHE_NOTIFYS_SC("oaNotifysSC", "系统广播通知-双创动态"),
	OA_CACHE_NOTIFYS_TZ("oaNotifysTZ", "系统广播通知-双创通知"),
	OA_CACHE_NOTIFYS_SS("oaNotifysSS", "系统广播通知-省市动态");

	private String key;
	private String remark;
	private SysCacheKeys(String key, String remark) {
		this.key = key;
		this.remark = remark;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public static SysCacheKeys getByKey(String key) {
		SysCacheKeys[] entitys = SysCacheKeys.values();
		for (SysCacheKeys entity : entitys) {
			if ((key).equals(entity.getKey())) {
				return entity;
			}
		}
		return null;
	}
}

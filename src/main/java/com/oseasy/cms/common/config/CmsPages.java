package com.oseasy.cms.common.config;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.CoreEmskey;

/**
 *  系统固定页面
 * @author Administrator
 *
 */
public enum CmsPages {
	demo(0, "demo", CoreSval.path.vms(CoreEmskey.SYS.k()) + "demo");

	private Integer val;
	private String idx;
	private String idxUrl;
	private CmsPages(Integer val, String idx, String idxUrl) {
		this.val = val;
		this.idx = idx;
		this.idxUrl = idxUrl;
	}
	public String getIdx() {
		return idx;
	}
	public Integer getVal() {
		return val;
	}
	public String getIdxUrl() {
		return idxUrl;
	}
	public static CmsPages getByVal(Integer val) {
		CmsPages[] entitys = CmsPages.values();
		for (CmsPages entity : entitys) {
			if ((val).equals(entity.getVal())) {
				return entity;
			}
		}
		return null;
	}

	public static CmsPages getByIdx(String idx) {
		CmsPages[] entitys = CmsPages.values();
		for (CmsPages entity : entitys) {
			if ((entity.getIdx()).equals(idx)) {
				return entity;
			}
		}
		return null;
	}
}

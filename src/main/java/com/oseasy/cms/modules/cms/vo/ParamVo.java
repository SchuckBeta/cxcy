package com.oseasy.cms.modules.cms.vo;

import com.oseasy.com.pcore.common.persistence.DataEntity;

public class ParamVo extends DataEntity<ParamVo>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2041998334303199831L;
	private String ids;
	private String publishStatus;



	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
	}
}

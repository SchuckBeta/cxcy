package com.oseasy.com.pcore.modules.sys.entity;


import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 系统配置Entity.
 * @author 9527
 * @version 2017-10-19
 */
public class SysConfig extends DataEntity<SysConfig> {

	private static final long serialVersionUID = 1L;
	private String content;		// 配置

	public SysConfig() {
		super();
	}

	public SysConfig(String id) {
		super(id);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
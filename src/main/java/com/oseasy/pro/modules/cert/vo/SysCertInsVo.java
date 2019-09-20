package com.oseasy.pro.modules.cert.vo;

import java.util.List;

/**
 * 证书模板Entity.
 * @author 奔波儿灞
 * @version 2018-02-06
 */
public class SysCertInsVo{
	/**
	 * 
	 */
	private String proid;
	private String id;
	private String name;		// 证书名称
	private List<SysCertPageInsVo> scp;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<SysCertPageInsVo> getScp() {
		return scp;
	}
	public void setScp(List<SysCertPageInsVo> scp) {
		this.scp = scp;
	}
	public String getProid() {
		return proid;
	}
	public void setProid(String proid) {
		this.proid = proid;
	}
	
}
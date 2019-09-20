package com.oseasy.pro.modules.cert.vo;

import java.util.Date;
import java.util.List;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 证书模板Entity.
 * @author 奔波儿灞
 * @version 2018-02-06
 */
public class SysCertVo extends DataEntity<SysCertVo>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;		// 证书名称
	private String releases;		// 是否发布：0-否，1-是
	private Date releaseDate;		// 发布时间
	private List<SysCertFlowVo> scf;
	private List<SysCertPageVo> scp;
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
	public String getReleases() {
		return releases;
	}
	public void setReleases(String releases) {
		this.releases = releases;
	}
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	public List<SysCertFlowVo> getScf() {
		return scf;
	}
	public void setScf(List<SysCertFlowVo> scf) {
		this.scf = scf;
	}
	public List<SysCertPageVo> getScp() {
		return scp;
	}
	public void setScp(List<SysCertPageVo> scp) {
		this.scp = scp;
	}
	
	
}
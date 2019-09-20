package com.oseasy.pro.modules.cert.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 证书模板Entity.
 * @author 奔波儿灞
 * @version 2018-02-06
 */
public class SysCert extends DataEntity<SysCert> {

	private static final long serialVersionUID = 1L;
	private String name;		// 证书名称
	private String releases;		// 是否发布：0-否，1-是
	private Date releaseDate;		// 发布时间

	public SysCert() {
		super();
	}

	public SysCert(String id){
		super(id);
	}

	@Length(min=1, max=128, message="证书名称长度必须介于 1 和 128 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=0, max=64, message="是否发布：0-否，1-是长度必须介于 0 和 64 之间")
	public String getReleases() {
		return releases;
	}

	public void setReleases(String releases) {
		this.releases = releases;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

}
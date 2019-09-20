package com.oseasy.com.pcore.modules.sys.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.FreeMarkers;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 系统全局编号Entity.
 * @author chenhao
 * @version 2017-07-17
 */
public class SysNo extends DataEntity<SysNo> {

	private static final long serialVersionUID = 1L;
	private String name;		// 编号处理类
	private String clazz;		// 编号处理类
	private String keyss;		// 编号惟一标识
	private int maxByte;		// 最大值占用几列
	private Long sysmaxVal;		// 全局编号最大值
	private String format;		// format
	private String prefix;		// 前缀
	private String postfix;		// 后缀
	private List<SysNoOffice> sysNoOffices;		// 机构编号列表

	public SysNo() {
		super();
	}

	public SysNo(String id) {
		super(id);
	}

	@Length(min=1, max=255, message="编号处理类长度必须介于 1 和 255 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	@Length(min=1, max=11, message="编号惟一标识长度必须介于 1 和 11 之间")
	public String getKeyss() {
		return keyss;
	}

	public void setKeyss(String keyss) {
		this.keyss = keyss;
	}

	public Long getSysmaxVal() {
		return sysmaxVal;
	}

	public void setSysmaxVal(Long sysmaxVal) {
		this.sysmaxVal = sysmaxVal;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getPostfix() {
		return postfix;
	}

	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}

  public int getMaxByte() {
    return maxByte;
  }

  public void setMaxByte(int maxByte) {
    this.maxByte = maxByte;
  }

  public List<SysNoOffice> getSysNoOffices() {
    return sysNoOffices;
  }

  public void setSysNoOffices(List<SysNoOffice> sysNoOffices) {
    this.sysNoOffices = sysNoOffices;
  }
}
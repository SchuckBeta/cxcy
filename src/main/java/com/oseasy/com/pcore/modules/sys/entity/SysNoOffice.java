package com.oseasy.com.pcore.modules.sys.entity;

import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 系统机构编号Entity.
 * @author chenhao
 * @version 2017-07-17
 */
public class SysNoOffice extends DataEntity<SysNoOffice> {

	private static final long serialVersionUID = 1L;
  private String sysNoId;    // 系统编号ID
  private SysNo sysNo;    // 系统编号
	private Office office;		// 机构ID
	private Long maxVal;		// 编号最大值

	public SysNoOffice() {
		super();
	}

	public SysNoOffice(String id) {
		super(id);
	}

	public SysNo getSysNo() {
		return sysNo;
	}

	public void setSysNo(SysNo sysNo) {
		this.sysNo = sysNo;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public Long getMaxVal() {
		return maxVal;
	}

	public void setMaxVal(Long maxVal) {
		this.maxVal = maxVal;
	}

  public String getSysNoId() {
    if (StringUtil.isEmpty(this.sysNoId) && (this.sysNo != null)) {
      this.sysNoId = this.sysNo.getId();
    }
    return sysNoId;
  }

  public void setSysNoId(String sysNoId) {
    this.sysNoId = sysNoId;
  }
}
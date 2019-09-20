package com.oseasy.dr.modules.dr.entity;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 卡记录规则组Entity.
 * @author chenh
 * @version 2018-05-16
 */
public class DrCardreGroup extends DataEntity<DrCardreGroup> {
	private static final long serialVersionUID = 1L;
	public static final String DR_CREGROUP = "drCardreGroup";
	private String name;		// 名称
	private String isTimeLimit;		// 是否时间限制
	private Boolean isShow;		// 是否显示
	private List<DrCardreGitem> drCreGitems;		// 明细
	private List<DrCardreGtime> drCreGtimes;		// 时间限制
    private List<String> ids;       // ids名称

	public DrCardreGroup() {
		super();
	}

	public DrCardreGroup(String id){
		super(id);
	}

	public DrCardreGroup(String id, Boolean isShow) {
        super();
        this.id = id;
        this.isShow = isShow;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIsTimeLimit() {
		return isTimeLimit;
	}

	public void setIsTimeLimit(String isTimeLimit) {
		this.isTimeLimit = isTimeLimit;
	}

    public List<DrCardreGitem> getDrCreGitems() {
        return drCreGitems;
    }

    public void setDrCreGitems(List<DrCardreGitem> drCreGitems) {
        this.drCreGitems = drCreGitems;
    }

    public List<DrCardreGtime> getDrCreGtimes() {
        return drCreGtimes;
    }

    public void setDrCreGtimes(List<DrCardreGtime> drCreGtimes) {
        this.drCreGtimes = drCreGtimes;
    }

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
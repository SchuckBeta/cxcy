package com.oseasy.dr.modules.dr.entity;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 卡记录规则明细Entity.
 * @author chenh
 * @version 2018-05-16
 */
public class DrCardreGitem extends DataEntity<DrCardreGitem> {

	private static final long serialVersionUID = 1L;
	private String estatus;		//进出状态:0、进和出；1、进；2、出
	private DrCardreGroup group;		// 预警规则ID
	private DrEquipmentRspace erspace;		// 预警场地

	public DrCardreGitem() {
		super();
	}

	public DrCardreGitem(String id){
		super(id);
	}

    public DrCardreGitem(DrCardreGroup group) {
        super();
        this.group = group;
    }

    public DrCardreGroup getGroup() {
        return group;
    }

    public void setGroup(DrCardreGroup group) {
        this.group = group;
    }

    public DrEquipmentRspace getErspace() {
        return erspace;
    }

    public void setErspace(DrEquipmentRspace erspace) {
        this.erspace = erspace;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
}
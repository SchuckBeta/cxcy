package com.oseasy.dr.modules.dr.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oseasy.com.pcore.common.persistence.DataExtEntity;
import com.oseasy.dr.modules.dr.vo.DrKey;
import com.oseasy.dr.modules.dr.vo.DrSpaceType;
import com.oseasy.util.common.utils.IidEntity;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 门禁设备场地Entity.
 * @author chenh
 * @version 2018-03-30
 */
public class DrEquipmentRspace extends DataExtEntity<DrEquipmentRspace> implements IidEntity{

	private static final long serialVersionUID = 1L;
	private DrEquipment epment;		// 设备ID
	private String rspType;		// 类型 ： 2房间 1楼栋  基地
	private String rspace;		// 房间号
	private String drNo;		// 设备出口编号，门1  门2 门3 门4
	private String name;		// 门名称
	private String dealStatus;		// 处理状态:0,正常；1,处理中；2,失败

	@Transient
	private String rsname;      // 房间/楼栋名称
    @Transient
    private String pnames;      // 父级名称
    @Transient
    private String rspaceName;      //场地名称

	@Transient
	private List<String> ids;        // 用于查询的设备出口编号
	@Transient
	private List<String> drNos;        // 用于查询的设备出口编号

	public DrEquipmentRspace() {
		super();
	}

	public DrEquipmentRspace(String id){
		super(id);
	}

    public DrEquipmentRspace(DrEquipment epment) {
        super();
        this.epment = epment;
    }

	public DrEquipmentRspace(String epmentId, String drNo) {
        super();
        this.epment = new DrEquipment(epmentId);
        this.drNo = drNo;
    }

	public DrEquipmentRspace(DrEquipment epment, String drNo) {
	    super();
	    this.epment = epment;
	    this.drNo = drNo;
	}

    public DrEquipment getEpment() {
		return epment;
	}

	public void setEpment(DrEquipment epment) {
		this.epment = epment;
	}

	public String getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(String dealStatus) {
        this.dealStatus = dealStatus;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public String getRspaceName() {
        return rspaceName;
    }

    public void setRspaceName(String rspaceName) {
        this.rspaceName = rspaceName;
    }

    public String getName() {
        return name;
    }

    public String getDoorName() {
	    if(StringUtil.isEmpty(this.name) && StringUtil.isNotEmpty(this.drNo)){
	        DrKey drKey = DrKey.getByKeyStr(this.drNo);
	        if(drKey != null){
	            return drKey.getName();
	        }
	    }
        return name;
    }

    public List<String> getDrNos() {
        return drNos;
    }

    public void setDrNos(List<String> drNos) {
        this.drNos = drNos;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRspType() {
		return rspType;
	}

	public void setRspType(String rspType) {
		this.rspType = rspType;
	}

	@Length(min=1, max=64, message="设备号长度必须介于 1 和 64 之间")
	public String getRspace() {
		return rspace;
	}

	public void setRspace(String rspace) {
		this.rspace = rspace;
	}

	@Length(min=1, max=1, message="设备出口编号，门1  门2 门3 门4长度必须介于 1 和 1 之间")
	public String getDrNo() {
		return drNo;
	}

	public void setDrNo(String drNo) {
		this.drNo = drNo;
	}

    @Transient
	public String getRsname() {
        if(StringUtil.isEmpty(this.pnames)){
            if((DrSpaceType.DK_ROOM.getKey()).equals(this.pnames)){
                this.rsname = this.rspaceName;
            }
        }
        return rsname;
    }

    @Transient
    public void setRsname(String rsname) {
        this.rsname = rsname;
    }

    @Transient
    public String getPnames() {
        if(StringUtil.isEmpty(this.pnames)){
            if((DrSpaceType.DK_SPACE.getKey()).equals(this.pnames)){
                this.pnames = this.rspaceName;
            }
        }
        return pnames;
    }

    @Transient
    public void setPnames(String pnames) {
        this.pnames = pnames;
    }

    @JsonIgnore
    public DrSpaceType getRstype() {
        if(StringUtil.isEmpty(this.rspType)){
            return null;
        }
        return DrSpaceType.getByKey(this.rspType);
    }
}
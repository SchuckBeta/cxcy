/**
 * .
 */

package com.oseasy.dr.modules.dr.manager;

import java.util.List;

import com.oseasy.dr.modules.dr.entity.DrCard;

/**
 * 卡执行参数.
 * @author chenhao
 *
 */
public class DrCardParam {
    private DrCard card;
    private String etpId;//设备ID
    private String etpSn;//设备编号
    private Long dealVersion;//当前数据版本（必填）//卡版本
    private Long ceVersion;//当前数据版本（必填）//设备版本
    private Boolean isRelease;//是否最后一个设备
    private Boolean isDeal;//是否处理完成
    private String type; // 命令请求类型
    private List<Integer> doors; //远程开启/关闭门时设备门参数

    public DrCardParam() {
        super();
    }
    public DrCardParam(DrCard card, String etpId) {
        super();
        this.card = card;
        this.etpId = etpId;
        this.isRelease = false;
        this.isDeal = false;
    }

    public DrCardParam(String etpId, List<Integer> doors, String type) {
        super();
        this.etpId = etpId;
        this.doors = doors;
        this.type = type;
    }

    public DrCardParam(DrCard card, String etpId, Long dealVersion, Long ceVersion) {
        super();
        this.card = card;
        this.etpId = etpId;
        this.dealVersion = dealVersion;
        this.ceVersion = ceVersion;
        this.isRelease = false;
        this.isDeal = false;
    }

    public DrCardParam(DrCard card, String etpId, Long dealVersion, Long ceVersion, Boolean isRelease) {
		super();
		this.card = card;
		this.etpId = etpId;
		this.dealVersion = dealVersion;
		this.ceVersion = ceVersion;
		this.isRelease = isRelease;
        this.isDeal = false;
	}
	public DrCard getCard() {
        return card;
    }
    public void setCard(DrCard card) {
        this.card = card;
    }
    public String getEtpId() {
        return etpId;
    }
    public void setEtpId(String etpId) {
        this.etpId = etpId;
    }
    public String getEtpSn() {
        return etpSn;
    }
    public void setEtpSn(String etpSn) {
        this.etpSn = etpSn;
    }
    public Long getDealVersion() {
        return dealVersion;
    }
    public void setDealVersion(Long dealVersion) {
        this.dealVersion = dealVersion;
    }
    public Long getCeVersion() {
        return ceVersion;
    }
    public void setCeVersion(Long ceVersion) {
        this.ceVersion = ceVersion;
    }
	public Boolean getIsRelease() {
		return isRelease;
	}
	public void setIsRelease(Boolean isRelease) {
		this.isRelease = isRelease;
	}
	public Boolean getIsDeal() {
		return isDeal;
	}
	public void setIsDeal(Boolean isDeal) {
		this.isDeal = isDeal;
	}

    public List<Integer> getDoors() {
        return doors;
    }

    public void setDoors(List<Integer> doors) {
        this.doors = doors;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

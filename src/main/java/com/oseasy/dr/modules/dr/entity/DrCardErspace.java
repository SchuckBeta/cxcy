package com.oseasy.dr.modules.dr.entity;

import java.util.List;

import org.springframework.data.annotation.Transient;

import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.util.common.utils.IidEntity;

/**
 * 卡设备Entity.
 * @author chenh
 * @version 2018-04-03
 */
public class DrCardErspace extends DataEntity<DrCardErspace> implements IidEntity{
	private static final long serialVersionUID = 1L;
	private DrCard card;		// 卡
	private DrEquipmentRspace erspace;		// 设备空间
    protected Integer status;       // 状态 @see DrCstatus 枚举类
    protected Long version;       // 版本

    @Transient
    private List<String> ids;

	public DrCardErspace() {
		super();
	}

    public DrCardErspace(String id){
        super(id);
    }

    public DrCardErspace(String id, DrEquipmentRspace erspace, String cardId) {
        super(id);
        this.card = new DrCard(cardId);
        this.erspace = erspace;
    }

    public DrCardErspace(String id, String erspaceId, String cardId) {
        super(id);
        this.card = new DrCard(cardId);
        this.erspace = new DrEquipmentRspace(erspaceId);
    }

    public DrCardErspace(DrCard card) {
        super();
        this.card = card;
    }

    public DrCardErspace(DrEquipmentRspace erspace) {
        super();
        this.erspace = erspace;
    }

    public DrCard getCard() {
        return card;
    }

    public void setCard(DrCard card) {
        this.card = card;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public DrEquipmentRspace getErspace() {
        return erspace;
    }

    public void setErspace(DrEquipmentRspace erspace) {
        this.erspace = erspace;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
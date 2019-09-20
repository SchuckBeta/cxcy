/**
 * .
 */

package com.oseasy.dr.modules.dr.vo;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.dr.modules.dr.entity.DrCard;
import com.oseasy.dr.modules.dr.entity.DrCardErspace;
import com.oseasy.dr.modules.dr.entity.DrEmentNo;
import com.oseasy.util.common.utils.StringUtil;

/**
 * .
 * @author chenhao
 *
 */
public class DrCardEquipment extends DrCard implements Serializable{
    private static final long serialVersionUID = 4955305568309543623L;
    private List<DrEmentNo> drEmentNos;

    public DrCardEquipment() {
        super();
    }
    public DrCardEquipment(String id) {
        super();
        this.id = id;
    }

    public DrCardEquipment(DrCard card) {
        super();
        if(card != null){
            this.id = card.getId();
            this.user = card.getUser();
            this.no = card.getNo();
            this.password = card.getPassword();
            this.expiry = card.getExpiry();
            this.status = card.getStatus();
            this.openTimes = card.getOpenTimes();
            this.privilege = card.getPrivilege();
            this.holidayUse = card.getHolidayUse();
            this.drNo = card.getDrNo();
            this.warnning = card.getWarnning();
            this.isCancel = card.getIsCancel();
            this.cardType = card.getCardType();
            this.tmpNo = card.getTmpNo();
            this.tmpName = card.getTmpName();
            this.tmpTel = card.getTmpTel();

        }
    }

    public DrCardEquipment(List<DrEmentNo> drEmentNos, DrCard card) {
        super();
        this.drEmentNos = drEmentNos;
        if(card != null){
            this.id = card.getId();
            this.user = card.getUser();
            this.no = card.getNo();
            this.password = card.getPassword();
            this.expiry = card.getExpiry();
            this.status = card.getStatus();
            this.openTimes = card.getOpenTimes();
            this.privilege = card.getPrivilege();
            this.holidayUse = card.getHolidayUse();
            this.drNo = card.getDrNo();
            this.warnning = card.getWarnning();
            this.isCancel = card.getIsCancel();
            this.cardType = card.getCardType();
            this.tmpNo = card.getTmpNo();
            this.tmpName = card.getTmpName();
            this.tmpTel = card.getTmpTel();
        }
    }

    public List<DrEmentNo> getDrEmentNos() {
        return drEmentNos;
    }

    public void setDrEmentNos(List<DrEmentNo> drEmentNos) {
        this.drEmentNos = drEmentNos;
    }

    /**
     * 单卡转换.
     * @param id 卡ID
     * @param drCardErspaces
     * @return
     */
    public static DrCardEquipment convert(DrCard card, List<DrCardErspace> drCardErspaces) {
        DrCardEquipment drCequipment = new DrCardEquipment(card);
        List<DrEmentNo> dreNos = Lists.newArrayList();
        for (DrCardErspace drCardErspace : drCardErspaces) {
            if((drCardErspace == null) || (drCardErspace.getCard() == null) || (drCardErspace.getErspace() == null) || (drCardErspace.getErspace().getEpment() == null)){
                continue;
            }
            dreNos.add(new DrEmentNo(drCardErspace.getErspace().getEpment().getId(), drCardErspace.getErspace().getEpment().getNo(), drCardErspace.getErspace().getDrNo()));
        }
        drCequipment.setDrEmentNos(dreNos);
        return drCequipment;
    }

    /**
     * 多卡转换.
     * @param drCardErspaces
     * @return
     */
    public static List<DrCardEquipment> converts(List<DrCardErspace> drCardErspaces) {
        List<String> cardIds = Lists.newArrayList();
        List<DrCardEquipment> drCardEquipments = Lists.newArrayList();
        for (DrCardErspace drCardErspace : drCardErspaces) {
            if((drCardErspace.getCard() == null) || StringUtil.isEmpty(drCardErspace.getCard().getId())){
                continue;
            }

            if((cardIds).contains(drCardErspace.getCard().getId())){
                continue;
            }

            cardIds.add(drCardErspace.getCard().getId());
            drCardEquipments.add(convert(drCardErspace.getCard(), drCardErspaces));
        }
        return drCardEquipments;
    }
}

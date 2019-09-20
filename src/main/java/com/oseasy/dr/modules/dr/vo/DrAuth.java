/**
 * .
 */

package com.oseasy.dr.modules.dr.vo;

import com.oseasy.dr.modules.dr.entity.DrCard;

import Net.PC15.FC8800.Command.Data.CardDetail;

/**
 * 门禁特权类型.
 *
 * @author chenhao
 *
 */
public enum DrAuth {
    DA_NONE(0, "无"), DA_SKTQ(1, "首卡特权卡"), DA_CKTQ(2, "常开特权卡"), DA_XG(3, "巡更卡"), DA_FDSZ(4, "防盗设置卡");

    public static final String DR_AUTHORS = "drAuths";

    private Integer key;
    private String name;

    private DrAuth(Integer key, String name) {
        this.key = key;
        this.name = name;
    }

    /**
     * 根据key获取枚举 .
     *
     * @author chenhao
     * @param key
     *            枚举标识
     * @return DrAuth
     */
    public static DrAuth getByKey(Integer key) {
        switch (key) {
        case 0:
            return DA_NONE;
        case 1:
            return DA_SKTQ;
        case 2:
            return DA_CKTQ;
        case 3:
            return DA_XG;
        case 4:
            return DA_FDSZ;
        default:
            return null;
        }
    }

    /**
     * 更新cardDetail特权信息.
     * @param drCard 卡信息
     * @param cardDetail 卡详情信息
     */
    public static void updateCardDetail(DrCard drCard, CardDetail cardDetail) {
        switch ((drCard.getPrivilege()).intValue()) {
            case 1:
                cardDetail.SetPrivilege();
                break;
            case 2:
                cardDetail.SetTiming();
                break;
            case 3:
                cardDetail.SetGuardTour();
                break;
            case 4:
                cardDetail.SetAlarmSetting();
            default:
                cardDetail.SetNormal();
        }
    }

    /**
     * 更新card特权信息.
     * @param card 卡信息
     * @param cardDetail 卡详情信息
     */
    public static DrCard updateCard(DrCard card, CardDetail cardDetail) {
        if (cardDetail.IsPrivilege()) {
            card.setPrivilege(DrAuth.DA_SKTQ.getKey());
        } else if (cardDetail.IsTiming()) {
            card.setPrivilege(DrAuth.DA_CKTQ.getKey());
        } else if (cardDetail.IsGuardTour()) {
            card.setPrivilege(DrAuth.DA_XG.getKey());
        } else if (cardDetail.IsAlarmSetting()) {
            card.setPrivilege(DrAuth.DA_FDSZ.getKey());
        } else {
            card.setPrivilege(DrAuth.DA_NONE.getKey());
        }
        return card;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{\"key\":\"" + this.key + "\",\"name\":\"" + this.name + "\"}";
    }
}

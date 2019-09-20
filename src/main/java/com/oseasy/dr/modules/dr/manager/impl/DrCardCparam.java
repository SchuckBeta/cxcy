/**
 * .
 */

package com.oseasy.dr.modules.dr.manager.impl;

import java.util.List;

import com.oseasy.dr.modules.dr.entity.DrCard;
import com.oseasy.dr.modules.dr.entity.DrCardRecord;
import com.oseasy.dr.modules.dr.manager.DrCardRparam;

/**
 * 卡执行回调参数.
 * @author chenhao
 *
 */
public class DrCardCparam{
    private DrCardRparam datas;//

    private String result;//结果 DRDeviceService专用
    private DrCard card;//卡 DRDeviceService专用
    private List<DrCardRecord> records;//卡记录 DRDeviceService专用

//    private String type;  //换enmu

//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }

    public DrCardCparam() {
        super();
    }

    public DrCardCparam(String etpSn, DrCard card) {
        super();
        this.datas = new DrCardRparam();
        this.datas.setEtpSn(etpSn);;
        this.card = card;
    }
    public DrCardCparam(String etpSn, DrCardRparam datas, Boolean isSuccess) {
        super();
        datas.setSuccess(isSuccess);
        datas.setEtpSn(etpSn);
        this.datas = datas;
    }
    public DrCardCparam(String etpSn, DrCard card, DrCardRparam datas, Boolean isSuccess) {
        super();
        datas.setSuccess(isSuccess);
        datas.setEtpSn(etpSn);
        this.datas = datas;
        this.card = card;
    }

    public DrCardCparam(String etpSn, DrCard card, DrCardRparam datas) {
        super();
        this.datas = datas;
        this.datas.setEtpSn(etpSn);;
        this.card = card;
    }
    public DrCardCparam(DrCardRparam datas, Boolean isSuccess) {
        super();
        datas.setSuccess(isSuccess);
        this.datas = datas;
    }

    public DrCardCparam(String etpSn, List<DrCardRecord> records) {
        super();
        this.datas = new DrCardRparam();
        this.datas.setEtpSn(etpSn);;
        this.records = records;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public DrCard getCard() {
        return card;
    }

    public void setCard(DrCard card) {
        this.card = card;
    }

    public DrCardRparam getDatas() {
        return datas;
    }

    public void setDatas(DrCardRparam datas) {
        this.datas = datas;
    }

    public List<DrCardRecord> getRecords() {
        return records;
    }

    public void setRecords(List<DrCardRecord> records) {
        this.records = records;
    }
}

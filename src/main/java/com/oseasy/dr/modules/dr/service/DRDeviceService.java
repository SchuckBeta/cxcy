package com.oseasy.dr.modules.dr.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.dr.modules.dr.entity.DrCard;
import com.oseasy.dr.modules.dr.entity.DrCardErspace;
import com.oseasy.dr.modules.dr.entity.DrCardRecord;
import com.oseasy.dr.modules.dr.entity.DrEquipment;
import com.oseasy.dr.modules.dr.excetion.MessageExcetion;
import com.oseasy.dr.modules.dr.manager.CardFactory;
import com.oseasy.dr.modules.dr.manager.CardManager;
import com.oseasy.dr.modules.dr.manager.DrCardParam;
import com.oseasy.dr.modules.dr.manager.DrCardRparam;
import com.oseasy.dr.modules.dr.manager.DrUtils;
import com.oseasy.dr.modules.dr.manager.IManager;
import com.oseasy.dr.modules.dr.manager.impl.DrCardCparam;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

@Service
public class DRDeviceService implements IManager {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    CardManager manager = null;

    @Autowired
    DrCardRecordService drCardRecordService;

    @Autowired
    private OaNotifyService oaNotifyService;

    @Autowired
    DrEquipmentRspaceService drEquipmentRspaceService;

    @Autowired
    DrEquipmentService drEquipmentService;

    @Autowired
    DrCardErspaceService drCardErspaceService;

//    static ExecutorService fixedThreadPool = null;


//    static {
//        fixedThreadPool = Executors.newFixedThreadPool(deqList.size());
//    }

    /**
     * 做业务
     */
    @Override
    public void call(DrCardCparam param) {
        DrCardRparam rparam = param.getDatas();
        if(rparam == null){
            logger.error("参数不能为空");
            return;
        }
        List<DrCardRecord> cardRecords = param.getRecords();
        if(StringUtil.checkEmpty(cardRecords)){
            logger.error("没有刷卡记录");
            return;
        }

        if (StringUtil.isEmpty(rparam.getEtpId()) && StringUtil.isEmpty(rparam.getEtpSn())){
            logger.error("参数不正确");
        }

        System.out.println("++ cardRecords size:" + cardRecords.size());
        DrEquipment drEquipment = drEquipmentService.get(rparam.getEtpId());
//        List<DrCardErspace>  drCardErspaces = drCardErspaceService.findListByg(new DrCardErspace(new DrEquipmentRspace(new DrEquipment(rparam.getEtpId()))));
        List<DrCardRecord> currCardRecordList = new ArrayList<DrCardRecord>();//需要保存的CardRecord记录集合

        try {
            for (DrCardRecord origCardRecord : cardRecords) {//设备中读取的记录
                DrCardRecord descCardRecord = new DrCardRecord();
                BeanUtils.copyProperties(descCardRecord, origCardRecord);
                descCardRecord.setEnter(origCardRecord.getIsEnter());
                descCardRecord.setId(IdGen.uuid());
                if(descCardRecord.getTime() == null){
                    descCardRecord.setTime(0);
                }
//                Boolean isTrue = true;
//                if(StringUtil.checkNotEmpty(drCardErspaces)){
//                    isTrue = buildDrCardRecord(descCardRecord, drCardErspaces);
//                }else{
//                    isTrue = false;
//                }
//
//                if(isTrue){
                    currCardRecordList.add(descCardRecord);
//                }else{
//                    logger.warn("记录数据处理失败，原因（记录card.no、drNo或授权card.no、drNo不匹配）,原始数据为："+JsonMapper.toJsonString(origCardRecord));
//                }
            }
            drCardRecordService.batchSave(currCardRecordList, param.getDatas().getEtpSn(), getTindex(cardRecords, drEquipment));
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
        }
    }

    @Override
    public void callFail(DrCardCparam param) {
        //TODO
    }

    public void senMessage(String message) {
        new Thread(new Runnable() {
            public void run() {
                System.out.println("-----------------------------------------\n\n");
                oaNotifyService.sendOaNotifyByType(UserUtils.getUser(), UserUtils.getUser(), "门禁通知", message, OaNotify.Type_Enum.TYPE17.getValue(), "");
            }
        }).start();
    }

    private int getTindex(List<DrCardRecord> cardRecords, DrEquipment drEquipment) {
        return drEquipment.getTindex() + cardRecords.size() + 1;
    }

    private Boolean buildDrCardRecord(DrCardRecord tempCardRecord, List<DrCardErspace> curDrCardErspaces) {
        Boolean isTrue = true;
        for (DrCardErspace curDrcee : curDrCardErspaces) {
            if ((curDrcee.getCard() == null) || (curDrcee.getErspace() == null)) {
                continue;
            }
            if (StringUtil.isEmpty(tempCardRecord.getCard().getNo()) || StringUtil.isEmpty(curDrcee.getCard().getNo())) {
                continue;
            }
            if (StringUtil.isEmpty(tempCardRecord.getCard().getDrNo()) || StringUtil.isEmpty(curDrcee.getErspace().getDrNo())) {
                continue;
            }

            if (!(tempCardRecord.getCard().getNo()).equals(curDrcee.getCard().getNo())) {
                continue;
            }

            if (!(tempCardRecord.getCard().getDrNo()).equals(curDrcee.getErspace().getDrNo())) {
                continue;
            }

            tempCardRecord.setCardNo(curDrcee.getCard().getNo());
            tempCardRecord.setEptId(curDrcee.getErspace().getEpment().getId());
            tempCardRecord.setRspType(curDrcee.getErspace().getRspType());
            tempCardRecord.setRspaceId(curDrcee.getErspace().getRspace());
            tempCardRecord.setDrNo(curDrcee.getErspace().getDrNo());
            tempCardRecord.setName(StringUtil.isEmpty(curDrcee.getErspace().getName())? curDrcee.getErspace().getEpment().getName()+"/"+curDrcee.getErspace().getDoorName() : curDrcee.getErspace().getName());
            tempCardRecord.setCard(curDrcee.getCard());
            tempCardRecord.setUser(curDrcee.getCard().getUser());
            tempCardRecord.setEptSn(curDrcee.getErspace().getEpment().getNo());
            tempCardRecord.setCerspace(curDrcee);
            tempCardRecord.setDispose("0");
            tempCardRecord.setDispose("0");
            isTrue = true;
            break;
        }
        return isTrue;
    }

    //开卡接口
    public Boolean uploadCard(DrCard card, String equipmentId) {
        CardManager manager = CardFactory.getCardManager(this);
        try {

            manager.uploadCard(card, CardFactory.getCommandDetial(equipmentId));
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            return false;
        }
        return true;
    }

    //获取卡信息
    public Boolean getCard(DrCard card, String equipmentId) {
        CardManager   manager = CardFactory.getCardManager(this);
        try {
            manager.getCard(card, CardFactory.getCommandDetial(equipmentId));
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            return false;
        }
        return true;
    }

    /**
     * 修改卡状态  【正常 0  ，挂失 1  ，黑名单 2】
     */
    public Boolean updateCardState(DrCard card, String equipmentId) {
        try {
            if (card != null && StringUtils.isNotEmpty(card.getNo())) {
//                this.deleteCard(Long.valueOf(card.getNo()), equipmentId);
//                if (this.deleteCard(Long.valueOf(card.getNo()), equipmentId)) {
                    return this.uploadCard(card, equipmentId);
//                }
            }
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
        }
        return false;
    }

    //删除卡信息

    public Boolean deleteCard(Long cardNo, String equipmentId) {
        CardManager manager = CardFactory.getCardManager(this);
        try {
            manager.deleteCard(cardNo, CardFactory.getCommandDetial(equipmentId));
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            return false;
        }
        return true;

    }

    //从指定索引位置开始读取打卡记录
    public Boolean getRecordsByIndex() {
        CardManager manager = CardFactory.getCardManager(this);
        DrCardRparam rparam = null;
        List<DrEquipment> deqList = DrUtils.getAllEt();//所有设备
        try {
            for (DrEquipment equipment : deqList) {
                logger.info("设备deqList：（getRecordsByIndex）：+ 长度:" + deqList.size());
//                fixedThreadPool.execute(new Runnable() {
//                    @Override
//                    public void run() {
                DrCardParam param = new DrCardParam(null, equipment.getId(), new Date().getTime(), null);
                rparam = new DrCardRparam(param);
                logger.info("设备equipment：（getRecordsByIndex）：+ \n" + equipment.toString() + "\n\n");
                manager.getRecordsByIndex(equipment.getTindex(), equipment.getTsize(), equipment.getType(), CardFactory.getCommandDetial(equipment.getId()));
                //manager.getRecordsByIndex(equipment.getTindex(), equipment.getTsize(), equipment.getType(), CardFactory.getCommandDetial(equipment.getId()), rparam);
//                    }
//                });
            }
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            return false;
        }
        return true;
    }

    //读取新打卡记录
    public void getNewRecordsByParam(int packetSize, int size, int type, String equipmentId) throws MessageExcetion {
        CardManager   manager = CardFactory.getCardManager(this);
        DrCardRparam rparam = null;
        try {
            DrCardParam param = new DrCardParam();
            param.setEtpId(equipmentId);
            rparam = new DrCardRparam(param);
            manager.getNewRecords(packetSize, size, type, CardFactory.getCommandDetial(equipmentId), rparam);
        } catch (Exception e) {
            throw new MessageExcetion("获取数据失败 设备编号：" + equipmentId);
        }
    }

    public Boolean getNewRecords() {
        int packetSize = 300;
        int size = 3000;
        int type = 1;
        List<DrEquipment> deqList = DrUtils.getAllEt();//所有设备
        for (DrEquipment de : deqList) {
            getNewRecordsByParam(packetSize, size, type, de.getId());
        }
        return true;
    }


    //复位通讯密码
    public Boolean PasseordReset() {
        //TODO
        return null;
    }

    //远程开、关门
    public Boolean openOrCloseDoor() {
        //TODO
        return null;
    }


}

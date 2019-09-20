package com.oseasy.dr.modules.dr.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.oseasy.com.pcore.common.mapper.JsonMapper;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.dr.modules.dr.entity.DrCard;
import com.oseasy.dr.modules.dr.entity.DrCardRecord;
import com.oseasy.dr.modules.dr.entity.DrEquipment;
import com.oseasy.dr.modules.dr.excetion.MessageExcetion;
import com.oseasy.dr.modules.dr.manager.CardFactory;
import com.oseasy.dr.modules.dr.manager.CardManager;
import com.oseasy.dr.modules.dr.manager.DrCardRparam;
import com.oseasy.dr.modules.dr.manager.DrConfig;
import com.oseasy.dr.modules.dr.manager.DrUtils;
import com.oseasy.dr.modules.dr.manager.IManager;
import com.oseasy.dr.modules.dr.manager.OperCardType;
import com.oseasy.dr.modules.dr.manager.impl.DrCardCparam;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

import Net.PC15.Command.CommandDetial;
import Net.PC15.Connector.E_ControllerType;
import Net.PC15.Connector.TCPClient.TCPClientDetial;
import Net.PC15.FC8800.FC8800Identity;

@Service
public class DrBaseService implements IManager {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    CardManager manager = null;
    //获取卡信息
    public Boolean getCard(DrCard card, String equipmentId) {
        manager = CardFactory.getCardManager(this);
        DrCardRparam rparam = null;
        try {
            CommandDetial  cmdDetial=getCommandDetial();
            rparam = new DrCardRparam();
            rparam.setCardNo(card.getNo());
            rparam.setType(OperCardType.READ.getKey());
            cmdDetial.setDatas(JsonMapper.toJsonString(rparam));
            manager.getCard(card, cmdDetial);
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            return false;
        }
        return true;
    }

    //获取卡信息
    public Boolean addCard(DrCard card, String equipmentId) {
        DrCardRparam rparam = null;
        try {
            rparam = new DrCardRparam();
            rparam.setType(4);
            CardFactory.getCardManager(this).getCard(card, CardFactory.getCommandDetial(equipmentId), rparam);
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            return false;
        }
        return true;
    }


    //测试设备是否连接
    public Boolean isConnectEq(DrEquipment erEquipment) {
        DrCardRparam rparam = null;
        try {
            CommandDetial cmdDetial = new CommandDetial();
            cmdDetial.Timeout = DrConfig.TIMEOUT;//此函数超时时间设定长一些
            cmdDetial.Connector = new TCPClientDetial(erEquipment.getIp(),erEquipment.getPort());//IP  ， 端口(默认8000)
            cmdDetial.Identity = new FC8800Identity(erEquipment.getNo(), erEquipment.getPsw(), E_ControllerType.FC8800);//设置SN(16位字符)，密码(8位十六进制字符)，设备类型
            rparam = new DrCardRparam();
            rparam.setEtpSn(erEquipment.getNo());
            rparam.setType(OperCardType.VERSION.getKey());
            cmdDetial.setDatas(JsonMapper.toJsonString(rparam));
            manager= CardFactory.getCardManager(this);
            manager.getVersion(cmdDetial);

            //CardFactory.getCardManager(this).getCard(card, CardFactory.getCommandDetial(equipmentId), rparam);
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            CacheUtils.put("connect",erEquipment.getNo(),"设备"+rparam.getEtpSn()+"连接失败");
            return false;
        }
        return true;
    }

    //测试开卡接口
    public Boolean uploadCard(DrCard card, String equipmentId) {
        manager = CardFactory.getCardManager(this);
        DrCardRparam rparam = null;
        try {
           //CommandDetial cmdDetial= CardFactory.getCommandDetial(equipmentId);
            CommandDetial  cmdDetial=getCommandDetial();

            rparam = new DrCardRparam();
            rparam.setEtpSn(cmdDetial.getIdentity().GetIdentity());
            rparam.setType(OperCardType.ADD.getKey());
            rparam.setCardNo(card.getNo());
            cmdDetial.setDatas(JsonMapper.toJsonString(rparam));
            manager.uploadCard(card, cmdDetial);
        } catch (Exception e) {
            CacheUtils.put("connect",card.getNo(),"设备"+rparam.getEtpSn()+"卡号:"+card.getNo()+"开卡失败");
            logger.error(ExceptionUtil.getStackTrace(e));
            return false;
        }
       return true;
    }

    public void readNewRecord(String readNum) {
        int packetSize = 300;
        int size = Integer.parseInt(readNum);
        int type = 1;

        List<DrEquipment> deqList = DrUtils.getAllEt();//所有设备
        DrCardRparam rparam = null;
        try {
            if(StringUtil.checkNotEmpty(deqList)){
                for (DrEquipment de : deqList) {
                    manager = CardFactory.getCardManager(this);
                    try {
                        CommandDetial cmdDetial =CardFactory.getCommandDetial(de.getId());
                        rparam = new DrCardRparam();
                        rparam.setEtpSn(cmdDetial.getIdentity().GetIdentity());
                        rparam.setType(OperCardType.READNEW.getKey());
                        cmdDetial.setDatas(JsonMapper.toJsonString(rparam));
                        manager.getNewRecords(packetSize, size, type, cmdDetial);
                    } catch (Exception e) {
                        CacheUtils.put("readNewRecord","读取设备"+rparam.getEtpSn()+readNum+"条记录失败");
                       throw new MessageExcetion("获取数据失败 设备编号：" + de.getId());
                    }
                }
            }else{
                manager = CardFactory.getCardManager(this);
                try {
                    CommandDetial cmdDetial =getCommandDetial();
                    rparam = new DrCardRparam();
                    rparam.setEtpSn(cmdDetial.getIdentity().GetIdentity());
                    rparam.setType(OperCardType.READNEW.getKey());
                    cmdDetial.setDatas(JsonMapper.toJsonString(rparam));
                    manager.getNewRecords(packetSize, size, type, cmdDetial);
                } catch (Exception e) {
                    CacheUtils.put("readNewRecord","读取设备"+rparam.getEtpSn()+readNum+"条记录失败");
                    throw new MessageExcetion("获取数据失败 设备编号：" + rparam.getEtpSn());
                }
            }
        } catch (MessageExcetion e) {
            CacheUtils.put("readNewRecord",rparam.getEtpSn(),"读取设备"+rparam.getEtpSn()+readNum+"条记录失败");
            logger.error(ExceptionUtil.getStackTrace(e));
        }
    }


    public CommandDetial getCommandDetial(){
        CommandDetial cmdDetial = new CommandDetial();
        cmdDetial.Timeout = DrConfig.TIMEOUT;//此函数超时时间设定长一些
        cmdDetial.Connector = new TCPClientDetial("192.168.1.150",8000);//IP  ， 端口(默认8000)
        cmdDetial.Identity = new FC8800Identity("MC-5848T47070030","FFFFFFFF", E_ControllerType.FC8800);//设置SN(16位字符)，密码(8位十六进制字符)，设备类型
        return cmdDetial;
    }

    @Override
    public void call(DrCardCparam param) {
        DrCardRparam rparam = param.getDatas();
       /**
        * 判断是否为指定操作
        */
        if(OperCardType.READ.getKey().equals(rparam.getType())){
            boolean isReadSucc=param.getDatas().getSuccess();
            if(isReadSucc){
                CacheUtils.put("readCard",param.getDatas().getCardNo(),"卡号:"+param.getDatas().getCardNo()+"信息:"+
                        param.getCard().toString());

            }
        }else if (OperCardType.ADD.getKey().equals(rparam.getType())){
            boolean isAddSucc=param.getDatas().getSuccess();
            if(isAddSucc){
                CacheUtils.put("addCard",param.getDatas().getCardNo(),"设备:"+param.getDatas().getEtpSn()+" 卡号:"+param.getDatas().getCardNo()+"开卡成功");
            }
        }else if (OperCardType.VERSION.getKey().equals(rparam.getType())){
            boolean isContect=param.getDatas().getSuccess();
            if(isContect){
                CacheUtils.put("connect",param.getDatas().getEtpSn(),"设备"+param.getDatas().getEtpSn()+"连接正常");
            }
        }else if (OperCardType.READNEW.getKey().equals(rparam.getType())){
            boolean isContect=param.getDatas().getSuccess();
            List<DrCardRecord> drcardList=param.getRecords();
            List<String> stingList=new ArrayList<String>();
            for(DrCardRecord drCardRecord :drcardList){
                String cardNo=drCardRecord.getCard().getNo();
                Date dateTime=drCardRecord.getPcTime();
                String stringRec=cardNo+"打卡时间:"+dateTime.toString();
                stingList.add(stringRec);
            }
            if(StringUtil.checkNotEmpty(drcardList)){
                CacheUtils.put("readNewRecordList",stingList);
            }
        }
    }

    @Override
    public void callFail(DrCardCparam param) {
        //TODO
    }
}

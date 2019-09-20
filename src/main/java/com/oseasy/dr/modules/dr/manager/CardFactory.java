package com.oseasy.dr.modules.dr.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oseasy.dr.modules.dr.entity.DrEquipment;
import com.oseasy.dr.modules.dr.excetion.MessageExcetion;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

import Net.PC15.Command.CommandDetial;
import Net.PC15.Connector.TCPClient.TCPClientDetial;
import Net.PC15.FC8800.FC8800Identity;

public class CardFactory {
    protected static Logger logger = LoggerFactory.getLogger(CardFactory.class);
    static  CommandDetial cmdDetial =null;

    private static CommandDetial buildCommandDetial(DrEquipment dre) {
        if(!(StringUtil.isNotEmptys(dre.getIp(), dre.getNo(), dre.getPsw()) || (dre.getType() == null))){
            return null;
        }
        //System.out.println("\n设备详细信息："+dre.toString());
        CommandDetial cmdDetial = new CommandDetial();
        cmdDetial.Timeout = DrConfig.TIMEOUT;//此函数超时时间设定长一些
        cmdDetial.Connector = new TCPClientDetial(dre.getIp(), dre.getPort());//IP  ， 端口(默认8000)
        cmdDetial.Identity = new FC8800Identity(dre.getNo(), dre.getPsw(), dre.retType());//设置SN(16位字符)，密码(8位十六进制字符)，设备类型
        return cmdDetial;
    }


    public static CommandDetial get(String equipmentId) throws MessageExcetion {
        if (StringUtil.isEmpty(equipmentId)) {
            throw new MessageExcetion("参数id不能空");
        }
        DrEquipment dre = DrUtils.getEt(equipmentId);
        if (dre == null) {
            throw new MessageExcetion("根据id，没有查询到门禁设备");
        }
        return buildCommandDetial(dre);
    }

    public  static CommandDetial getCommandDetial(String equipmentId) {
        CommandDetial detial = null;
        try {
            detial = get(equipmentId);//1 equipmentId
        } catch (MessageExcetion e) {
            logger.error("设备无法连接或命令对象初始化异常：设备IP、NO、PSW、TYPE不能为空(或设备号、密码不符合规范)！", ExceptionUtil.getStackTrace(e));
            return null;
        }
        return detial;
    }

    @SuppressWarnings("unchecked")
    public synchronized static CardManager<? extends IManager> getCardManager(IManager service) {
        return CardManager.getInstance().setService(service);
    }
}

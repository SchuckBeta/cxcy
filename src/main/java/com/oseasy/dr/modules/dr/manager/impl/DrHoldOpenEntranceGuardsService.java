package com.oseasy.dr.modules.dr.manager.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.mapper.JsonMapper;
import com.oseasy.dr.modules.dr.manager.CardConstants;
import com.oseasy.dr.modules.dr.manager.CardFactory;
import com.oseasy.dr.modules.dr.manager.DrCardParam;
import com.oseasy.dr.modules.dr.manager.DrCardRparam;
import com.oseasy.dr.modules.dr.manager.IManager;
import com.oseasy.dr.modules.dr.manager.IManagerExt;
import com.oseasy.dr.modules.dr.manager.OperType;
import com.oseasy.dr.modules.dr.service.DrEquipmentRspaceService;
import com.oseasy.dr.modules.dr.service.DrEquipmentService;
import com.oseasy.dr.modules.dr.vo.DrCdealStatus;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

import Net.PC15.Command.CommandDetial;

@Service
public class DrHoldOpenEntranceGuardsService implements IManager, IManagerExt {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DrEquipmentRspaceService drEquipmentRspaceService;

    @Autowired
    private DrEquipmentService drEquipmentService;

    @Override
    public void call(DrCardCparam param) {
        DrCardRparam rparam = param.getDatas();

        /**
         * 判断是否为指定操作
         */
        if(!(getOperType()).equals(rparam.getOperType())){
            return;
        }

        if (StringUtils.isEmpty(drEquipmentService.get(rparam.getEtpId()))) {
            throw new RuntimeException("系统中未查询到当前设备！");
        }

        // 判断当前命令类型为设备命令，仅更新设备状态
        if (CardConstants.commandType.DEVICE_COMMAND.getType().equals(rparam.getCommandType())) {
            List<String> ids = Arrays.asList(rparam.getEtpId());
            drEquipmentService.updateBatchEquipmentStatusByIds(DrCdealStatus.DCD_NORMAL.getKey(), ids);
        } else if (CardConstants.commandType.DOOR_COMMAND.getType().equals(rparam.getCommandType())) { // 当命令类型为门命令时，仅修改当前门的状态
            List<String> doorNos = new ArrayList<>();
            rparam.getDoors().forEach(door -> doorNos.add((door - 1) + ""));
            drEquipmentRspaceService.updateDoorStatusByEquipmentIdAndDoorNos(DrCdealStatus.DCD_NORMAL.getKey(), rparam.getEtpId(), doorNos);
        }

        logger.warn("***************************************设备号："+rparam.getEtpSn() + "处理完成***************************************");
        logger.warn(JsonMapper.toJsonString(rparam));
        logger.warn("*******************************************************************************************************");
    }

    @Override
    public void callFail(DrCardCparam param) {
        DrCardRparam rparam = param.getDatas();
        /**
         * 判断是否为指定操作
         */
        if(!(getOperType()).equals(rparam.getOperType())){
            return;
        }

        if (StringUtils.isEmpty(drEquipmentService.get(rparam.getEtpId()))) {
            throw new RuntimeException("系统中未查询到当前设备！");
        }

        // 判断当前命令类型为设备命令，仅更新设备状态
        if (CardConstants.commandType.DEVICE_COMMAND.getType().equals(rparam.getCommandType())) {
            List<String> ids = Arrays.asList(rparam.getEtpId());
            drEquipmentService.updateBatchEquipmentStatusByIds(DrCdealStatus.DCD_FAIL.getKey(), ids);
        } else if (CardConstants.commandType.DOOR_COMMAND.getType().equals(rparam.getCommandType())) { // 当命令类型为门命令时，仅修改当前门的状态
            List<String> doorNos = new ArrayList<>();
            rparam.getDoors().forEach(door-> {
                doorNos.add((door - 1) + "");
            });
            drEquipmentRspaceService.updateDoorStatusByEquipmentIdAndDoorNos(DrCdealStatus.DCD_FAIL.getKey(), rparam.getEtpId(), doorNos);
        }

        logger.warn("***************************************设备号："+rparam.getEtpSn() + "处理失败***************************************");
        logger.warn(JsonMapper.toJsonString(rparam));
        logger.warn("*******************************************************************************************************");
    }

    @Override
    public ApiTstatus<DrCardRparam> runner(DrCardParam param) {
        if(StringUtil.isEmpty(param.getEtpId())){
            logger.warn("设备编号不能为空！");
            return new ApiTstatus<DrCardRparam>(false, "设备编号不能为空！");
        }
        DrCardRparam rparam = null;
        try {
            rparam = new DrCardRparam(param);
            rparam.setOperType(getOperType());
            rparam.setCommandType(param.getType());
            CommandDetial commandDetial = CardFactory.getCommandDetial(param.getEtpId());
            CardFactory.getCardManager(this).holdDoorOpenAction(commandDetial, rparam);
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            return new ApiTstatus<DrCardRparam>(false, "设备处理异常！", rparam);
        }
        return new ApiTstatus<DrCardRparam>(rparam);
    }

    @Override
    public Integer getOperType() {
        return OperType.HOLD_OPEN_DOORS.getKey();
    }
}

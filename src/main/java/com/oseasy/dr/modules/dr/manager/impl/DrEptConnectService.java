/**
 * .
 */

package com.oseasy.dr.modules.dr.manager.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.dr.modules.dr.entity.DrEquipment;
import com.oseasy.dr.modules.dr.manager.CardFactory;
import com.oseasy.dr.modules.dr.manager.DrCardParam;
import com.oseasy.dr.modules.dr.manager.DrCardRparam;
import com.oseasy.dr.modules.dr.manager.IManager;
import com.oseasy.dr.modules.dr.manager.IManagerExt;
import com.oseasy.dr.modules.dr.manager.OperType;
import com.oseasy.dr.modules.dr.service.DrEquipmentService;
import com.oseasy.dr.modules.dr.vo.DrCdealStatus;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

/**
 * 卡激活.
 * @author chenhao
 *
 */
@Service
public class DrEptConnectService implements IManager, IManagerExt {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    DrEquipmentService drEquipmentService;

    @Override
    public Integer getOperType() {
        return OperType.CONNECT.getKey();
    }

    /* (non-Javadoc)
     * @see com.oseasy.dr.modules.dr.manager.IManager#manager(java.lang.Object, java.lang.String)
     */
    @Override
    public void call(DrCardCparam param) {
        //TODO 需要考虑的问题，会不会存在设备回调比系统处理业务快（可能导致回调取不到卡）
        //1、获取响应信息,包含卡信息和执行状态
        DrCardRparam rparam = param.getDatas();
        /**
         * 判断是否为指定操作
         */
        if(!(getOperType()).equals(rparam.getOperType())){
            return;
        }

        //2、根据响应信息定位处理记录
        DrEquipment drEquipment = drEquipmentService.get(rparam.getEtpId());
        if(rparam.getSuccess()){
            drEquipment.setDealStatus(DrCdealStatus.DCD_NORMAL.getKey());
            drEquipmentService.save(drEquipment);
        }else{
            logger.error("rparam.getSuccess() = "+rparam.getSuccess() +"，没有处理");
        }
    }

    /* (non-Javadoc)
     * @see com.oseasy.dr.modules.dr.manager.IManager#manager(java.lang.Object, java.lang.String)
     */
    @Override
    public void callFail(DrCardCparam param) {
        //TODO 需要考虑的问题，会不会存在设备回调比系统处理业务快（可能导致回调取不到卡）
        //1、获取响应信息,包含卡信息和执行状态
        DrCardRparam rparam = param.getDatas();
        /**
         * 判断是否为指定操作
         */
        if(!(getOperType()).equals(rparam.getOperType())){
            return;
        }

        //2、根据响应信息定位处理记录
        DrEquipment drEquipment = drEquipmentService.get(rparam.getEtpId());
        if(!rparam.getSuccess()){
            drEquipment.setDealStatus(DrCdealStatus.DCD_FAIL.getKey());
            drEquipmentService.save(drEquipment);
        }else{
            logger.error("rparam.getSuccess() = "+rparam.getSuccess() +"，没有处理");
        }
    }

    /**
     * 需要设置时间戳标识版本.
     */
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
            CardFactory.getCardManager(this).getVersion(CardFactory.getCommandDetial(param.getEtpId()), rparam);
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            return new ApiTstatus<DrCardRparam>(false, "设备处理异常！", rparam);
        }
        return new ApiTstatus<DrCardRparam>(rparam);
    }
}

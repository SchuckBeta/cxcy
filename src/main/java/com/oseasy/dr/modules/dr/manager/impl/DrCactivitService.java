/**
 * .
 */

package com.oseasy.dr.modules.dr.manager.impl;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.dr.modules.dr.entity.DrCard;
import com.oseasy.dr.modules.dr.entity.DrCardErspace;
import com.oseasy.dr.modules.dr.entity.DrEquipment;
import com.oseasy.dr.modules.dr.entity.DrEquipmentRspace;
import com.oseasy.dr.modules.dr.manager.CardFactory;
import com.oseasy.dr.modules.dr.manager.DrCardParam;
import com.oseasy.dr.modules.dr.manager.DrCardRparam;
import com.oseasy.dr.modules.dr.manager.DrCvo;
import com.oseasy.dr.modules.dr.manager.DrUtils;
import com.oseasy.dr.modules.dr.manager.IManager;
import com.oseasy.dr.modules.dr.manager.IManagerExt;
import com.oseasy.dr.modules.dr.manager.OperType;
import com.oseasy.dr.modules.dr.service.DrCardErspaceService;
import com.oseasy.dr.modules.dr.service.DrCardService;
import com.oseasy.dr.modules.dr.vo.DrCdealStatus;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

/**
 * 卡激活.
 * @author chenhao
 *
 */
@Service
public class DrCactivitService implements IManager, IManagerExt {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    DrCardErspaceService drCardErspaceService;

    @Autowired
    DrCardService drCardService;

    @Override
    public Integer getOperType() {
        return OperType.ACTIVIT.getKey();
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
        DrCard card = drCardService.get(rparam.getCardId());
        DrCvo cvo = DrUtils.getCard(card.getDealVersion());
        if(cvo == null){
        	cvo = DrUtils.getCard(rparam.getOperVersion());
        }
        if(cvo == null){
        	logger.error(">>>设备["+param.getDatas().getEtpId()+"]卡["+rparam.getCardId()+"]，处理完毕-成功->"+rparam.getSuccess());
        	return;
        }
        Boolean isFinish = cvo.getHasFinish();
        if(isFinish){
           return;
        }

        DrCardErspace drCardErspace = new DrCardErspace(null, new DrEquipmentRspace(new DrEquipment(rparam.getEtpId())), card.getId());
        drCardErspace.setStatus(DrCdealStatus.DCD_DEALING.getKey());
        List<DrCardErspace> cardErspaces = drCardErspaceService.findListByg(drCardErspace);
    	List<DrCardErspace> newCardErspaces = Lists.newArrayList();
        if(rparam.getSuccess()){
            //操作成功更改状态
            for (DrCardErspace drCerspace : cardErspaces) {
                /**
                 * 判断操作的版本是否一致
                 */
                if(!(drCerspace.getVersion() +"").equals(rparam.getOperCeVersion()+"")){
                    continue;
                }
                drCerspace.setStatus(DrCdealStatus.DCD_NORMAL.getKey());
                newCardErspaces.add(drCerspace);
            }

            card.setDealStatus(DrCdealStatus.DCD_NORMAL.getKey());
            isFinish = DrUtils.checkCvo(rparam.getOperVersion(), card, rparam.getEtpId(), newCardErspaces);
        }else{
           logger.error("rparam.getSuccess() = "+rparam.getSuccess() +"，没有处理");
        }

        DrCvo cvo2 = DrUtils.getCard(rparam.getOperVersion());
        if(isFinish){
            System.out.println("成功=================================================================v1=-->"+cvo2.getVersion());
            System.out.println("curSize = "+cvo2.getEtpCerspaces().size()+" <= maxSize = "+cvo2.getEtpIds().size() + "-->newCardErspaces.size=" + newCardErspaces.size());

            drCardService.save(card);
            //drCardErspaceService.updateByPl(DrCvo.convert(cvo2));
            drCardErspaceService.updateStatusByCid(Arrays.asList(new String[]{card.getId()}), DrCdealStatus.DCD_NORMAL.getKey());
            DrUtils.removeDrCvo(DrUtils.CACHE_DRCVO, rparam.getOperVersion().toString());
            DrCvo cvo3 = DrUtils.getCard(rparam.getOperVersion());
            System.out.println("==============================================================Clear===="+ cvo3);
        }else{
            System.out.println("=================================================================v2=-->"+cvo2.getVersion());
            System.out.println("curSize = "+cvo2.getEtpCerspaces().size()+" <= maxSize = "+cvo2.getEtpIds().size() + "-->newCardErspaces.size=" + newCardErspaces.size());
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
        DrCard card = drCardService.get(rparam.getCardId());
        DrCvo cvo = DrUtils.getCard(card.getDealVersion());
        if(cvo == null){
        	cvo = DrUtils.getCard(rparam.getOperVersion());
        }
        if(cvo == null){
        	logger.error(">>>设备["+param.getDatas().getEtpId()+"]卡["+rparam.getCardId()+"]，处理完毕-成功->"+rparam.getSuccess());
        	return;
        }
        Boolean isFinish = cvo.getHasFinish();
        if(isFinish){
           return;
        }

        DrCardErspace drCardErspace = new DrCardErspace(null, new DrEquipmentRspace(new DrEquipment(rparam.getEtpId())), card.getId());
        drCardErspace.setStatus(DrCdealStatus.DCD_DEALING.getKey());
        List<DrCardErspace> cardErspaces = drCardErspaceService.findListByg(drCardErspace);
        List<DrCardErspace> newCardErspaces = Lists.newArrayList();
        if(!rparam.getSuccess()){
            //操作失败更改状态
            for (DrCardErspace drCerspace : cardErspaces) {
                /**
                 * 判断操作的版本是否一致
                 */
                if(!(drCerspace.getVersion() +"").equals(rparam.getOperCeVersion()+"")){
                    continue;
                }
                drCerspace.setStatus(DrCdealStatus.DCD_FAIL.getKey());
                newCardErspaces.add(drCerspace);
            }

            card.setDealStatus(DrCdealStatus.DCD_FAIL.getKey());
            isFinish = DrUtils.checkCvo(rparam.getOperVersion(), card, rparam.getEtpId(), newCardErspaces);
        }else{
            logger.error("rparam.getSuccess() = "+rparam.getSuccess() +"，没有处理");
        }

        DrCvo cvo2 = DrUtils.getCard(rparam.getOperVersion());
        if(isFinish){
            System.out.println("成功=================================================================v1=-->"+cvo2.getVersion());
            System.out.println("curSize = "+cvo2.getEtpCerspaces().size()+" <= maxSize = "+cvo2.getEtpIds().size() + "-->newCardErspaces.size=" + newCardErspaces.size());

            drCardService.save(card);
            //drCardErspaceService.updateByPl(DrCvo.convert(cvo2));
            drCardErspaceService.updateStatusByCid(Arrays.asList(new String[]{card.getId()}), DrCdealStatus.DCD_FAIL.getKey());
            DrUtils.removeDrCvo(DrUtils.CACHE_DRCVO, rparam.getOperVersion().toString());
            DrCvo cvo3 = DrUtils.getCard(rparam.getOperVersion());
            System.out.println("==============================================================Clear===="+ cvo3);
        }else{
            System.out.println("=================================================================v2=-->"+cvo2.getVersion());
            System.out.println("curSize = "+cvo2.getEtpCerspaces().size()+" <= maxSize = "+cvo2.getEtpIds().size() + "-->newCardErspaces.size=" + newCardErspaces.size());
        }
    }

    /**
     * 需要设置时间戳标识版本.
     */
    @Override
    public ApiTstatus<DrCardRparam> runner(DrCardParam param) {
        if((param.getCard() == null) || StringUtil.isEmpty(param.getEtpId())){
            logger.warn("卡和设备编号不能为空！");
            return new ApiTstatus<DrCardRparam>(false, "卡和设备编号不能为空！");
        }
        DrCardRparam rparam = null;
        try {
            rparam = new DrCardRparam(param);
            rparam.setOperType(getOperType());
            CardFactory.getCardManager(this).uploadCard(param.getCard(), CardFactory.getCommandDetial(param.getEtpId()), rparam);
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            return new ApiTstatus<DrCardRparam>(false, "设备处理异常！", rparam);
        }
        return new ApiTstatus<DrCardRparam>(rparam);
    }
}

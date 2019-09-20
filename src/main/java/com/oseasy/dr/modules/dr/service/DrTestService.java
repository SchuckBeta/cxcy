package com.oseasy.dr.modules.dr.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.oseasy.dr.modules.dr.entity.DrCard;
import com.oseasy.dr.modules.dr.entity.DrCardRecord;
import com.oseasy.dr.modules.dr.manager.CardFactory;
import com.oseasy.dr.modules.dr.manager.DrCardRparam;
import com.oseasy.dr.modules.dr.manager.IManager;
import com.oseasy.dr.modules.dr.manager.impl.DrCardCparam;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

@Service
public class DrTestService  implements IManager {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    //获取卡信息
    public Boolean getCard(DrCard card, String equipmentId) {
        DrCardRparam rparam = null;

        try {
            rparam = new DrCardRparam();
            rparam.setType(1);
            CardFactory.getCardManager(this).getCard(card, CardFactory.getCommandDetial(equipmentId), rparam);
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            return false;
        }
//
//        CardManager manager = CardFactory.getCardManager(this);
//        try {
//           manager.getCard(card, CardFactory.getCommandDetial(equipmentId));
//        } catch (Exception e) {
//            logger.error(ExceptionUtil.getStackTrace(e));
//            return false;
//        }
        return true;
    }


    @Override
    public void call(DrCardCparam param) {
        DrCardRparam rparam = param.getDatas();
       /**
        * 判断是否为指定操作
        */
        if("1".equals(rparam.getType())){
            List<DrCardRecord> list=param.getRecords();
            return;
        }
    }

    @Override
    public void callFail(DrCardCparam param) {
        //TODO
    }
}

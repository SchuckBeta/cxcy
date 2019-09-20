/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import java.util.List;

import org.apache.log4j.Logger;

import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.dr.modules.dr.entity.DrCard;
import com.oseasy.dr.modules.dr.service.DrCardService;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.tool.IitCheck;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;
import com.oseasy.pie.modules.impdata.vo.DrCardError;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 重复开卡.
 * @author chenhao
 */
public class ItCkDrCardRepeat implements IitCheck<ItCparamDr>{
    public final static Logger logger = Logger.getLogger(ItCkDrCardRepeat.class);
    private DrCardService drCardService;
    public ItCkDrCardRepeat() {
        super();
    }

    public ItCkDrCardRepeat(DrCardService drCardService) {
        super();
        this.drCardService = drCardService;
    }


    @Override
    public String key() {
        return "姓名|学号|手机号|卡号";
    }

    @Override
    public boolean validateKey(ItCparamDr param) {
        if ((key()).contains(ExcelUtils.getStringByCell(param.getXs().getRow(ImpDataService.descHeadRow).getCell(param.getIdx()), param.getXs()))){
            return true;
        }
        return false;
    }

    @Override
    public ItCparamDr validate(ItCparamDr param, IitCheckEetyExt pe, IitCheckEetyExt pev) {
        if(!(param.check() && validateKey(param))){
            return param;
        }

        ImpInfo ii = param.getInfo();
        ImpInfoErrmsg iie = param.getIe();
        if((pe instanceof DrCardError) && (pev instanceof DrCardError)){
            DrCardError phe = (DrCardError)pe;

            iie = new ImpInfoErrmsg();
            iie.setImpId(ii.getId());
            iie.setDataId(phe.getId());
            iie.setColname(param.getIdx() + "");
            if (StringUtil.isNotEmpty(param.getVal())) {
                List<DrCard> cards = drCardService.findListCardByNameOrNOorMobile(param.getVal());
                if(StringUtil.checkNotEmpty(cards)){
                    param.setTag(param.getTag() + 1);
                    iie.setErrmsg("（姓名|学号|手机号|卡号）对应存在卡，不能重复开卡");
                }
            }
            if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                param.getIes().save(iie);
            }
        }

        return param;
    }

    public static boolean checkTimeYMDZG(String time) {
        try {
            if ((DateUtil.FMT_YYYYMMDD_HHmmss_ZG).length() != time.length()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
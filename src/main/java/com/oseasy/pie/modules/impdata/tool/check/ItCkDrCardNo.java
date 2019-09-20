/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

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
import com.oseasy.util.common.utils.StringUtil;

/**
 * 用户学号.
 * @author chenhao
 */
public class ItCkDrCardNo implements IitCheck<ItCparamDr>{
    public final static Logger logger = Logger.getLogger(ItCkDrCardNo.class);
    private DrCardService drCardService;

    public ItCkDrCardNo(DrCardService drCardService) {
        super();
        this.drCardService = drCardService;
    }


    @Override
    public String key() {
        return "卡号";
    }

    @Override
    public boolean validateKey(ItCparamDr param) {
        if ((key()).equals(StringUtil.trim(ExcelUtils.getStringByCell(param.getXs().getRow(ImpDataService.descHeadRow).getCell(param.getIdx()), param.getXs())))){
            return true;
        }
        return false;
    }

    @Override
    public ItCparamDr validate(ItCparamDr param, IitCheckEetyExt pe, IitCheckEetyExt pev) {
        if(!(param.check() && validateKey(param) && (this.drCardService != null))){
            return param;
        }

        ImpInfo ii = param.getInfo();
        ImpInfoErrmsg iie = param.getIe();
        if((pe instanceof DrCardError) && (pev instanceof DrCardError)){
            DrCardError phe = (DrCardError)pe;
            DrCardError validinfo = (DrCardError)pev;

            phe.setNo(param.getVal());
            validinfo.setNo(phe.getNo());
            iie = new ImpInfoErrmsg();
            iie.setImpId(ii.getId());
            iie.setDataId(phe.getId());
            iie.setColname(param.getIdx() + "");
            if (StringUtil.isNotEmpty(param.getVal())) {
                DrCard card = this.drCardService.getByNo(param.getVal());
                if(card != null){
                    param.setTag(param.getTag() + 1);
                    iie.setErrmsg("当前卡号已存在！");
                }

                if (((param.getVal()).length() < 6) || ((param.getVal()).length() > 10)) {
                    param.setTag(param.getTag() + 1);
                    iie.setErrmsg("只能6-10个字符");
                    phe.setNo(null);
                }
            }else{
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("必填信息");
            }
            if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                param.getIes().save(iie);
            }
        }

        return param;
    }
}
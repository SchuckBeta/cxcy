/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import org.apache.log4j.Logger;

import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
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
public class ItCkUserSex implements IitCheck<ItCparamDr>{
    public final static Logger logger = Logger.getLogger(ItCkUserSex.class);

    public ItCkUserSex() {
        super();
    }


    @Override
    public String key() {
        return "性别";
    }

    @Override
    public boolean validateKey(ItCparamDr param) {
        if ((key()).equals(ExcelUtils.getStringByCell(param.getXs().getRow(ImpDataService.descHeadRow).getCell(param.getIdx()), param.getXs()))){
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
            DrCardError validinfo = (DrCardError)pev;

            phe.setTmpSex(param.getVal());
            validinfo.setTmpSex(phe.getTmpSex());
            phe.setSex(param.getVal());
            validinfo.setSex(phe.getTmpSex());
            iie = new ImpInfoErrmsg();
            iie.setImpId(ii.getId());
            iie.setDataId(phe.getId());
            iie.setColname(param.getIdx() + "");
            if (StringUtil.isEmpty(param.getVal())) {
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
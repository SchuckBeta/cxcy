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
 * 用户学院.
 * @author chenhao
 */
public class ItCkUserOfficeXy implements IitCheck<ItCparamDr>{
    public final static Logger logger = Logger.getLogger(ItCkUserOfficeXy.class);

    public ItCkUserOfficeXy() {
        super();
    }


    @Override
    public String key() {
        return "学院";
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

            phe.setOffice(param.getVal());
            validinfo.setOffice(phe.getOffice());

            if(StringUtil.isNotEmpty(phe.getOffice()) && StringUtil.isNotEmpty(phe.getProfessional())){
                phe.setTmpOffice(phe.getOffice() + StringUtil.LINE + phe.getProfessional());
                validinfo.setTmpOffice(phe.getTmpOffice());
            }

            iie = new ImpInfoErrmsg();
            iie.setImpId(ii.getId());
            iie.setDataId(phe.getId());
            iie.setColname(param.getIdx() + "");
            if (StringUtil.isEmpty(param.getVal())) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg(key() + "必填");
            }
            if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                param.getIes().save(iie);
            }
        }

        return param;
    }
}
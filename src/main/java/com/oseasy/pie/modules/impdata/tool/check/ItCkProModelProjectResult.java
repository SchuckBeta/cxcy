/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import org.apache.log4j.Logger;

import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.entity.ProModelError;
import com.oseasy.pie.modules.impdata.entity.ProModelGcontestError;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.tool.IitCheck;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 检验项目名称列.
 * @author chenhao
 *
 */
public class ItCkProModelProjectResult implements IitCheck<ItCparamPm>{
    public final static Logger logger = Logger.getLogger(ItCkProModelProjectResult.class);

    @Override
    public String key() {
        return "项目结果";
    }

    @Override
    public boolean validateKey(ItCparamPm param) {
        if ((key()).equals(ExcelUtils.getStringByCell(param.getXs().getRow(ImpDataService.descHeadRow).getCell(param.getIdx()), param.getXs()))){
            return true;
        }
        return false;
    }

    @Override
    public ItCparamPm validate(ItCparamPm param, IitCheckEetyExt pe, IitCheckEetyExt pev) {
        if(!(param.check() && validateKey(param))){
            return param;
        }

        ImpInfo ii = param.getInfo();
        ImpInfoErrmsg iie = param.getIe();
        if((pe instanceof ProModelError) && (pev instanceof ProModelError)){
            ProModelError phe = (ProModelError)pe;
            ProModelError validinfo = (ProModelError)pev;

            phe.setResult(param.getVal());
            validinfo.setResult(param.getVal());
            iie = new ImpInfoErrmsg();
            iie.setImpId(ii.getId());
            iie.setDataId(phe.getId());
            iie.setColname(param.getIdx() + "");
            if (!StringUtil.isEmpty(param.getVal())) {
                if ((param.getVal()).length() > 64) {
                    param.setTag(param.getTag() + 1);
                    iie.setErrmsg("最多64个字符");
                    phe.setResult(null);
                }
            }
            if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                param.getIes().save(iie);
            }
        }else if((pe instanceof ProModelGcontestError) && (pev instanceof ProModelGcontestError)){
            ProModelGcontestError phe = (ProModelGcontestError)pe;
            ProModelGcontestError validinfo = (ProModelGcontestError)pev;
            //TODO CHENHAO
        }
        return param;
    }
}

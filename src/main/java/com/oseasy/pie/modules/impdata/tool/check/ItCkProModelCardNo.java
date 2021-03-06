/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.com.pcore.modules.sys.utils.RegexUtils;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.entity.ProModelError;
import com.oseasy.pie.modules.impdata.entity.ProModelGcontestError;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.tool.IitCheck;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 检验身份证号码列.
 * @author chenhao
 *
 */
public class ItCkProModelCardNo implements IitCheck<ItCparamPm>{
    public final static Logger logger = Logger.getLogger(ItCkProModelCardNo.class);

    public ItCkProModelCardNo() {
        super();
    }

    @Override
    public String key() {
        return "身份证号码";
    }

    @Override
    public boolean validateKey(ItCparamPm param) {
        if ((key()).contains(ExcelUtils.getStringByCell(param.getXs().getRow(ImpDataService.descHeadRow).getCell(param.getIdx()), param.getXs()))){
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
//            ProModelError phe = (ProModelError)pe;
//            ProModelError validinfo = (ProModelError)pev;
        }else if((pe instanceof ProModelGcontestError) && (pev instanceof ProModelGcontestError)){
            ProModelGcontestError phe = (ProModelGcontestError)pe;
            ProModelGcontestError validinfo = (ProModelGcontestError)pev;

            phe.setIdnum(param.getVal());
            validinfo.setIdnum(param.getVal());
            iie = new ImpInfoErrmsg();
            iie.setImpId(ii.getId());
            iie.setDataId(phe.getId());
            iie.setColname(param.getIdx() + "");
            if (StringUtil.isEmpty(param.getVal())) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("必填信息");
            } else if (!Pattern.matches(RegexUtils.idNumRegex, param.getVal())) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("身份证号码格式不正确");
                phe.setIdnum(null);
            } else if (param.getVal().length() > 32) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("最多32个字符");
                phe.setNo(null);
            }

            if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                param.getIes().save(iie);
            }
        }

        return param;
    }
}

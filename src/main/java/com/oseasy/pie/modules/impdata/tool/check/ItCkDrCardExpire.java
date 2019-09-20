/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.tool.IitCheck;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;
import com.oseasy.pie.modules.impdata.vo.DrCardError;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 用户学号.
 * @author chenhao
 */
public class ItCkDrCardExpire implements IitCheck<ItCparamDr>{
    public final static Logger logger = Logger.getLogger(ItCkDrCardExpire.class);

    public ItCkDrCardExpire() {
        super();
    }


    @Override
    public String key() {
        return "有效期";
    }

    @Override
    public boolean validateKey(ItCparamDr param) {
        if ((key()).equals(ExcelUtils.getStringByCell(param.getXs().getRow(ImpDataService.descHeadRow).getCell(param.getIdx()), param.getXs()))){
            return true;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
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

            iie = new ImpInfoErrmsg();
            iie.setImpId(ii.getId());
            iie.setDataId(phe.getId());
            iie.setColname(param.getIdx() + "");
            if (StringUtil.isNotEmpty(param.getVal())) {
                if ((param.getVal()).length() > 128) {
                    param.setTag(param.getTag() + 1);
                    iie.setErrmsg("最多128个字符");
                    phe.setExpiry(null);
                }

                if (!checkTimeYMDZG(param.getVal())) {
                    param.setTag(param.getTag() + 1);
                    iie.setErrmsg("有效期日期格式有误,正确格式如:2018-01-01");
                } else{
                    try {
                        phe.setExpiry(new SimpleDateFormat(DateUtil.FMT_YYYYMMDD_ZG).parse(param.getVal()));
                    } catch (ParseException e) {
                        param.setTag(param.getTag() + 1);
                        iie.setErrmsg("日期格式不正确，格式化失败,正确格式如:2018-01-01");
                    }
                    validinfo.setExpiry(phe.getExpiry());
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
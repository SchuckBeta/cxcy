/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import java.text.ParseException;

import org.apache.log4j.Logger;

import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.entity.ProModelError;
import com.oseasy.pie.modules.impdata.entity.ProModelGcontestError;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.tool.IitCheck;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 毕业时间列处理.
 * @author chenhao
 */
public class ItCkProModelOut implements IitCheck<ItCparamPm>{
    public final static Logger logger = Logger.getLogger(ItCkProModelOut.class);
    private String format;

    public ItCkProModelOut(String format) {
        super();
        this.format = format;
    }

    @Override
    public String key() {
        return "毕业时间|毕业年份";
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
            //iie = new ImpInfoErrmsg();
            //ProModelError phe = (ProModelError)pe;
            //ProModelError validinfo = (ProModelError)pev;
        }else if((pe instanceof ProModelGcontestError) && (pev instanceof ProModelGcontestError)){
            ProModelGcontestError phe = (ProModelGcontestError)pe;
            ProModelGcontestError validinfo = (ProModelGcontestError)pev;

            if(StringUtil.isNotEmpty(param.getVal())){
                try {
                    if(StringUtil.isEmpty(this.format)){
                        DateUtil.parseDate(param.getVal(), DateUtil.FMT_YYYYMMDD_HHmmss_ZG);
                    }else{
                        DateUtil.parseDate(param.getVal(), this.format);
                    }

                    phe.setOut(param.getVal());
                    validinfo.setOut(param.getVal());
                } catch (ParseException e) {
                    param.setTag(param.getTag() + 1);
                    iie.setErrmsg("毕业年份格式不正确");
                    e.printStackTrace();
                }

                if (!ImpDataService.checkOutYear(phe.getEnter(), phe.getOut())) {
                    param.setTag(param.getTag() + 1);
                    iie.setErrmsg("毕业年份需大于入学年份");
                }
            }

            iie.setImpId(ii.getId());
            iie.setDataId(phe.getId());
            iie.setColname(param.getIdx() + "");

            if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                param.getIes().save(iie);
            }
        }
        return param;
    }
}
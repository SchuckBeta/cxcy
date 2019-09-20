/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import java.text.ParseException;
import java.util.Date;

import org.apache.log4j.Logger;

import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.tool.IitCheck;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;
import com.oseasy.pie.modules.impdata.vo.DrCardError;
import com.oseasy.pro.modules.workflow.entity.ProModelTlxy;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.Reflections;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 用户班级.
 * @author chenhao
 */
public class ItCkDrEleDate implements IitCheck<ItCparamDr>{
    public final static Logger logger = Logger.getLogger(ItCkDrEleDate.class);
    private String key;
    private String fieldName;
    private String format;

    public ItCkDrEleDate(String key, String fieldName) {
        super();
        this.key = key;
        this.fieldName = fieldName;
        this.format = DateUtil.FMT_YYYYMMDD_HHmmss_ZG;
    }
    public ItCkDrEleDate(String key, String fieldName, String format) {
        super();
        this.key = key;
        this.fieldName = fieldName;
        this.format = format;
    }

    @Override
    public String key() {
        return this.key;
    }

    @Override
    public boolean validateKey(ItCparamDr param) {
        if (StringUtil.isNotEmpty(key()) && (key()).equals(StringUtil.trim(ExcelUtils.getStringByCell(param.getXs().getRow(ImpDataService.descHeadRow).getCell(param.getIdx()), param.getXs())))){
            return true;
        }
        return false;
    }

    @Override
    public ItCparamDr validate(ItCparamDr param, IitCheckEetyExt pe, IitCheckEetyExt pev) {
        if(!(param.check() && validateKey(param) && StringUtil.isNotEmpty(this.fieldName))){
            return param;
        }

        ImpInfo ii = param.getInfo();
        ImpInfoErrmsg iie = param.getIe();
        if((pe instanceof DrCardError) && (pev instanceof DrCardError)){
            DrCardError phe = (DrCardError)pe;
            DrCardError validinfo = (DrCardError)pev;
            if(StringUtil.isNotEmpty(param.getVal())){
//                Date curDate;
//                try {
//                    if(StringUtil.isEmpty(this.format)){
                          this.format = DateUtil.FMT_YYYYMMDD_HHmmss_ZG;
//                    }
//                    curDate = DateUtil.parseDate(param.getVal(), DateUtil.FMT_YYYYMMDD_HHmmss_ZG);
//
//                    Reflections.invokeSetter(phe, this.fieldName, curDate);
//                    Reflections.invokeSetter(validinfo, this.fieldName, curDate);
//                } catch (ParseException e) {
//                    param.setTag(param.getTag() + 1);
//                    iie.setErrmsg("时间格式化错误！");
//                    e.printStackTrace();
//                }

                Date curDate = null;
                String curFormat = null;
                Boolean isFormat = false;
                if(StringUtil.isEmpty(this.format)){
                    this.format = DateUtil.FMT_YYYYMMDD_HHmmss_ZG;
                }

                if(!isFormat){
                    try {
                        /**
                         * 判断当前日期格式是否为DateUtil.FMT_YYYYMMDD_HHmmss_ZG.
                         */
                        curFormat = DateUtil.FMT_YYYYMMDD_HHmmss_ZG;
                        curDate = DateUtil.parseDate(param.getVal(), curFormat);
                        isFormat = true;
                    } catch (ParseException e) {
                        curDate = null;
                        isFormat = false;
                        curFormat = null;
                    }
                }

                if(!isFormat){
                    try {
                        /**
                         * 判断当前日期格式是否为DateUtil.FMT_YYYYMMDD_ZG.
                         */
                        curFormat = DateUtil.FMT_YYYYMMDD_ZG;
                        curDate = DateUtil.parseDate(param.getVal(), curFormat);
                        isFormat = true;
                    } catch (ParseException e) {
                        curDate = null;
                        isFormat = false;
                        curFormat = null;
                    }
                }

                if(!isFormat){
                    try {
                        /**
                         * 判断当前日期格式是否为DateUtil.FMT_YYYYMM_ZG.
                         */
                        curFormat = DateUtil.FMT_YYYYMM_ZG;
                        curDate = DateUtil.parseDate(param.getVal(), curFormat);
                        isFormat = true;
                    } catch (ParseException e) {
                        curDate = null;
                        isFormat = false;
                        curFormat = null;
                    }
                }

                if(isFormat && (curDate != null)){
                    if((DateUtil.FMT_YYYYMMDD_ZG).equals(curFormat)){
                        curDate = DateUtil.getCurDateYMD999();
                    }
                    if((DateUtil.FMT_YYYYMM_ZG).equals(curFormat)){
                        curDate = DateUtil.setDays(curDate, 1);
                        curDate = DateUtil.getCurDateYMD999();
                    }

                    try {
                        Reflections.invokeSetter(phe, this.fieldName, curDate);
                        Reflections.invokeSetter(validinfo, this.fieldName, curDate);
                    } catch (Exception e) {
                        param.setTag(param.getTag() + 1);
                        iie.setErrmsg("设置属性"+this.fieldName+"出错");
                        logger.error(e.getMessage());
                    }
                }else{
                    param.setTag(param.getTag() + 1);
                    iie.setErrmsg("时间格式("+this.format+")错误！");
                }
            }

            iie = new ImpInfoErrmsg();
            iie.setImpId(ii.getId());
            iie.setDataId(phe.getId());
            iie.setColname(param.getIdx() + "");
        }
        return param;
    }
}
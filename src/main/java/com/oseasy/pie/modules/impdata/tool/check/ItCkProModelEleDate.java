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
import com.oseasy.pie.modules.impdata.entity.ProModelError;
import com.oseasy.pie.modules.impdata.entity.ProModelGcontestError;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.tool.IitCheck;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;
import com.oseasy.pro.modules.workflow.entity.ProModelTlxy;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.Reflections;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 时间列处理.
 * @author chenhao
 */
public class ItCkProModelEleDate implements IitCheck<ItCparamPm>{
    public final static Logger logger = Logger.getLogger(ItCkProModelEleDate.class);
    private String key;
    private Object subo;
    private String fieldName;
    private String format;

    public ItCkProModelEleDate(String key, String fieldName) {
        super();
        this.key = key;
        this.fieldName = fieldName;
    }
    public ItCkProModelEleDate(String key, Object subo, String fieldName) {
        super();
        this.key = key;
        this.subo = subo;
        this.fieldName = fieldName;
        this.format = DateUtil.FMT_YYYYMMDD_HHmmss_ZG;
    }
    public ItCkProModelEleDate(String key, String fieldName, String format) {
        super();
        this.key = key;
        this.fieldName = fieldName;
        this.format = format;
    }
    public ItCkProModelEleDate(String key, Object subo, String fieldName, String format) {
        super();
        this.key = key;
        this.subo = subo;
        this.fieldName = fieldName;
        this.format = format;
    }

    @Override
    public String key() {
        return this.key;
    }

    @Override
    public boolean validateKey(ItCparamPm param) {
        String keyName = ExcelUtils.getStringByCell(param.getXs().getRow(ImpDataService.descHeadRow).getCell(param.getIdx()), param.getXs());
        if (StringUtil.isNotEmpty(key()) && (key()).equals(StringUtil.trim(keyName))){
            return true;
        }
        return false;
    }

    @Override
    public ItCparamPm validate(ItCparamPm param, IitCheckEetyExt pe, IitCheckEetyExt pev) {
        if(!(param.check() && validateKey(param) && StringUtil.isNotEmpty(this.fieldName))){
            return param;
        }

        ImpInfo ii = param.getInfo();
        ImpInfoErrmsg iie = param.getIe();
        if((pe instanceof ProModelError) && (pev instanceof ProModelError)){
            iie = new ImpInfoErrmsg();
            ProModelError phe = (ProModelError)pe;
            ProModelError validinfo = (ProModelError)pev;
            if(StringUtil.isNotEmpty(param.getVal())){
                Date curDate;
                try {
                    if(StringUtil.isEmpty(this.format)){
                        curDate = DateUtil.parseDate(param.getVal(), DateUtil.FMT_YYYYMMDD_HHmmss_ZG);
                    }else{
                        curDate = DateUtil.parseDate(param.getVal(), this.format);
                    }

                    if(this.subo == null){
                        Reflections.invokeSetter(phe, this.fieldName, curDate);
                        Reflections.invokeSetter(validinfo, this.fieldName, curDate);
                    }else{
                        Reflections.invokeSetter(this.subo, this.fieldName, curDate);
                        phe.setPmTlxy((ProModelTlxy) this.subo);
                        Reflections.invokeSetter(this.subo, this.fieldName, curDate);
                        validinfo.setPmTlxy((ProModelTlxy) this.subo);
                    }
                } catch (ParseException e) {
                    param.setTag(param.getTag() + 1);
                    iie.setErrmsg("时间格式化错误！");
                    e.printStackTrace();
                }
            }

            iie.setImpId(ii.getId());
            iie.setDataId(phe.getId());
            iie.setColname(param.getIdx() + "");

            if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                param.getIes().save(iie);
            }

        }else if((pe instanceof ProModelGcontestError) && (pev instanceof ProModelGcontestError)){
            iie = new ImpInfoErrmsg();
            ProModelGcontestError phe = (ProModelGcontestError)pe;
            ProModelGcontestError validinfo = (ProModelGcontestError)pev;

            if(StringUtil.isNotEmpty(param.getVal())){
                Date curDate;
                try {
                    if(StringUtil.isEmpty(this.format)){
                        curDate = DateUtil.parseDate(param.getVal(), DateUtil.FMT_YYYYMMDD_HHmmss_ZG);
                    }else{
                        curDate = DateUtil.parseDate(param.getVal(), this.format);
                    }

                    if(this.subo == null){
                        Reflections.invokeSetter(phe, this.fieldName, curDate);
                        Reflections.invokeSetter(validinfo, this.fieldName, curDate);
                    }
                } catch (ParseException e) {
                    param.setTag(param.getTag() + 1);
                    iie.setErrmsg("时间格式化错误！");
                    e.printStackTrace();
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
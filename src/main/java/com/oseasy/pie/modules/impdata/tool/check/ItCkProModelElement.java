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
import com.oseasy.pro.modules.workflow.entity.ProModelTlxy;
import com.oseasy.util.common.utils.Reflections;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 用户班级.
 * @author chenhao
 */
public class ItCkProModelElement implements IitCheck<ItCparamPm>{
    public final static Logger logger = Logger.getLogger(ItCkProModelElement.class);
    private String key;
    private Object subo;
    private String fieldName;
    private Integer maxLenth;
    private Integer minLenth;

    public ItCkProModelElement(String key, String fieldName) {
        super();
        this.key = key;
        this.fieldName = fieldName;
    }
    public ItCkProModelElement(String key, Object subo, String fieldName) {
        super();
        this.key = key;
        this.subo = subo;
        this.fieldName = fieldName;
    }

    public ItCkProModelElement(String key, String fieldName, Integer maxLenth, Integer minLenth) {
        super();
        this.key = key;
        this.fieldName = fieldName;
        this.maxLenth = maxLenth;
        this.minLenth = minLenth;
    }

    public ItCkProModelElement(String key, Object subo, String fieldName, Integer maxLenth, Integer minLenth) {
        super();
        this.key = key;
        this.subo = subo;
        this.fieldName = fieldName;
        this.maxLenth = maxLenth;
        this.minLenth = minLenth;
    }
    @Override
    public String key() {
        return this.key;
    }

    @Override
    public boolean validateKey(ItCparamPm param) {
        String keyName = ExcelUtils.getStringByCell(param.getXs().getRow(ImpDataService.descHeadRow).getCell(param.getIdx()), param.getXs());
//        System.out.println(keyName + "?=" + key());
        if (StringUtil.isNotEmpty(key()) && (key()).equals(StringUtil.trim(keyName))){
            return true;
        }
        return false;
    }

    @Override
    public ItCparamPm validate(ItCparamPm param, IitCheckEetyExt pe, IitCheckEetyExt pev) {
//        System.out.println(param.check() + "-" + validateKey(param) + " - " + StringUtil.isEmpty(this.fieldName));
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
                if((this.maxLenth != null)){
                    if((param.getVal().length() > this.maxLenth)){
                        param.setTag(param.getTag() + 1);
                        iie.setErrmsg("不能超过最大长度("+this.maxLenth+")");
                    }
                }

                if((this.minLenth != null)){
                    if((param.getVal().length() < this.minLenth)){
                        param.setTag(param.getTag() + 1);
                        iie.setErrmsg("不能小于最小长度("+this.minLenth+")");
                    }
                }
            }

            if(this.subo == null){
                Reflections.invokeSetter(phe, this.fieldName, param.getVal());
                Reflections.invokeSetter(validinfo, this.fieldName, param.getVal());
            }else{
                Reflections.invokeSetter(this.subo, this.fieldName, param.getVal());
                phe.setPmTlxy((ProModelTlxy) this.subo);
                Reflections.invokeSetter(this.subo, this.fieldName, param.getVal());
                validinfo.setPmTlxy((ProModelTlxy) this.subo);
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
                if((this.maxLenth != null)){
                    if((param.getVal().length() > this.maxLenth)){
                        param.setTag(param.getTag() + 1);
                        iie.setErrmsg("不能超过最大长度("+this.maxLenth+")");
                    }
                }

                if((this.minLenth != null)){
                    if((param.getVal().length() < this.minLenth)){
                        param.setTag(param.getTag() + 1);
                        iie.setErrmsg("不能小于最小长度("+this.minLenth+")");
                    }
                }
            }

            if(this.subo == null){
                Reflections.invokeSetter(phe, this.fieldName, param.getVal());
                Reflections.invokeSetter(validinfo, this.fieldName, param.getVal());
            }else{
                Reflections.invokeSetter(this.subo, this.fieldName, param.getVal());
//                phe.setPmTlxy((ProModelTlxy) this.subo);
                Reflections.invokeSetter(this.subo, this.fieldName, param.getVal());
//                validinfo.setPmTlxy((ProModelTlxy) this.subo);
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
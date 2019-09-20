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
import com.oseasy.util.common.utils.Reflections;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 用户班级.
 * @author chenhao
 */
public class ItCkDrElement implements IitCheck<ItCparamDr>{
    public final static Logger logger = Logger.getLogger(ItCkDrElement.class);
    private String key;
    private String fieldName;
    private Integer maxLenth;
    private Integer minLenth;

    public ItCkDrElement(String key, String fieldName) {
        super();
        this.key = key;
        this.fieldName = fieldName;
    }

    public ItCkDrElement(String key, String fieldName, Integer maxLenth, Integer minLenth) {
        super();
        this.key = key;
        this.fieldName = fieldName;
        this.maxLenth = maxLenth;
        this.minLenth = minLenth;
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
            iie = new ImpInfoErrmsg();
            DrCardError phe = (DrCardError)pe;
            DrCardError validinfo = (DrCardError)pev;

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
                        iie.setErrmsg("不能小于最小长度("+this.maxLenth+")");
                    }
                }
            }

            try {
                Reflections.invokeSetter(phe, this.fieldName, param.getVal());
                Reflections.invokeSetter(validinfo, this.fieldName, param.getVal());
            } catch (Exception e) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("设置属性"+this.fieldName+"出错");
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
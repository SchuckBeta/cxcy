/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import org.apache.log4j.Logger;

import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.entity.ProjectError;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.tool.IitCheck;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 检验立项年份.
 * @author chenhao
 *
 */
public class ItCkPDProjectYear implements IitCheck<ItCparamPm>{
    public static Logger logger = Logger.getLogger(ItCkPDProjectYear.class);

    @Override
    public String key() {
        return "立项年份";
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
        ProjectError phe = (ProjectError)pe;
        ProjectError validinfo = (ProjectError)pev;

        phe.setApprovingYear(param.getVal());
        validinfo.setApprovingYear(param.getVal());
        iie = new ImpInfoErrmsg();
        iie.setImpId(ii.getId());
        iie.setDataId(phe.getId());
        iie.setColname(param.getIdx() + "");

        if (!StringUtil.isEmpty(param.getVal())) {
            if (!ImpDataService.checkYear(param.getVal())) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("项目年份格式不正确");
            } else if ((param.getVal()).length() > 4) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("最多4个字符");
                phe.setApprovingYear(null);
            }
        }
        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
            param.getIes().save(iie);
        }

        return param;
    }
}

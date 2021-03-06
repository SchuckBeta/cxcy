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
 * 检验省（区、市）.
 * @author chenhao
 *
 */
public class ItCkArea implements IitCheck<ItCparamPm>{
    public static Logger logger = Logger.getLogger(ItCkArea.class);

    @Override
    public String key() {
        return "省（区、市）";
    }

    @Override
    public boolean validateKey(ItCparamPm param) {
        String keyName = ExcelUtils.getStringByCell(param.getXs().getRow(ImpDataService.descHeadRow).getCell(param.getIdx()), param.getXs());
        if ((key()).equals(keyName)){
            return true;
        }
        return false;
    }

    @Override
    public ItCparamPm validate(ItCparamPm param, IitCheckEetyExt pe, IitCheckEetyExt pev) {
        if(!(param.check() && validateKey(param) && (param.getActyw() != null))){
            return param;
        }
        ImpInfo ii = param.getInfo();
        ImpInfoErrmsg iie = param.getIe();

        ProjectError phe = (ProjectError)pe;
        ProjectError validinfo = (ProjectError)pev;

        phe.setProvince(param.getVal());
        validinfo.setProvince(param.getVal());
        iie = new ImpInfoErrmsg();
        iie.setImpId(ii.getId());
        iie.setDataId(phe.getId());
        iie.setColname(param.getIdx() + "");

        if (StringUtil.isEmpty(param.getVal())) {
            param.setTag(param.getTag() + 1);
            iie.setErrmsg("必填信息");
        } else if ((param.getVal()).length() > 64) {
            param.setTag(param.getTag() + 1);
            iie.setErrmsg("最多64个字符");
            phe.setProvince(null);
        }

        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
            param.getIes().save(iie);
        }

        return param;
    }
}

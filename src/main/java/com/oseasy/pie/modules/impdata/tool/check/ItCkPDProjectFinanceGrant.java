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
import com.oseasy.pie.modules.impdata.entity.ProjectError;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.tool.IitCheck;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 大创-检验项目财政拨款(元).
 * @author chenhao
 *
 */
public class ItCkPDProjectFinanceGrant implements IitCheck<ItCparamPm>{
    public static Logger logger = Logger.getLogger(ItCkPDProjectFinanceGrant.class);

    @Override
    public String key() {
        return "财政拨款(元)";
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

        phe.setFinanceGrant(param.getVal());
        validinfo.setFinanceGrant(param.getVal());
        iie = new ImpInfoErrmsg();
        iie.setImpId(ii.getId());
        iie.setDataId(phe.getId());
        iie.setColname(param.getIdx() + "");

        if (StringUtil.isEmpty(param.getVal())) {
            param.setTag(param.getTag() + 1);
            iie.setErrmsg("必填信息");
        }else if ((param.getVal()).length() > 20) {
            param.setTag(param.getTag() + 1);
            iie.setErrmsg("最多20个字符");
            phe.setFinanceGrant(null);
        } else if (!Pattern.matches(RegexUtils.grantRegex, param.getVal())) {
            param.setTag(param.getTag() + 1);
            iie.setErrmsg("请输入0或0以上的数，最多可有2位小数");
        }
        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
            param.getIes().save(iie);
        }
        return param;
    }
}

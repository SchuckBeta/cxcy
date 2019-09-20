/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import org.apache.log4j.Logger;

import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.entity.OfficeError;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.tool.IitCheck;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;
import com.oseasy.pie.modules.impdata.tool.param.ItRpOffice;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 检验专业名称.
 * @author chenhao
 *
 */
public class ItCkOfficeRemark implements IitCheck<ItCparamPm>{
    public static Logger logger = Logger.getLogger(ItCkOfficeRemark.class);

    @Override
    public String key() {
        return "专业名称";
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
        if(!(param.check() && validateKey(param))){
            return param;
        }

        ItRpOffice rparam;
        if(param.getRparam() == null){
            param.setRparam(new ItRpOffice());
        }
        rparam = (ItRpOffice) param.getRparam();

        ImpInfo ii = param.getInfo();
        ImpInfoErrmsg iie = param.getIe();

        OfficeError phe = (OfficeError)pe;
        OfficeError validinfo = (OfficeError)pev;

        phe.setRemarks(param.getVal());
        validinfo.setRemarks(param.getVal());
        iie = new ImpInfoErrmsg();
        iie.setImpId(ii.getId());
        iie.setDataId(phe.getId());
        iie.setColname(param.getIdx() + "");

        if (rparam.getCur() == null) {
            param.setTag(param.getTag() + 1);
            iie.setErrmsg("当前数据不存在无法修改备注");
            phe.setRemarks(null);
        }

        if (StringUtil.isNotEmpty(param.getVal()) && (param.getVal()).length() > 255) {
            param.setTag(param.getTag() + 1);
            iie.setErrmsg("最多255个字符");
            phe.setRemarks(null);
        }

        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
            param.getIes().save(iie);
        } else {
            if (StringUtil.isNotEmpty(param.getVal())) {
                rparam.getCur().setRemarks(param.getVal());
            }
        }
        return param;
    }
}

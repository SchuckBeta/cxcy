/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import org.apache.log4j.Logger;

import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.utils.OfficeUtils;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.entity.OfficeError;
import com.oseasy.pie.modules.impdata.enums.OffiGrade;
import com.oseasy.pie.modules.impdata.enums.OffiType;
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
public class ItCkOfficeZy implements IitCheck<ItCparamPm>{
    public static Logger logger = Logger.getLogger(ItCkOfficeZy.class);

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

        phe.setProfessional(param.getVal());
        validinfo.setProfessional(param.getVal());
        iie = new ImpInfoErrmsg();
        iie.setImpId(ii.getId());
        iie.setDataId(phe.getId());
        iie.setColname(param.getIdx() + "");

        if (rparam.getSchool() == null) {
            param.setTag(param.getTag() + 1);
            iie.setErrmsg("当前数据学校不能为空");
        }

        if (rparam.getOffice() == null) {
            param.setTag(param.getTag() + 1);
            iie.setErrmsg("该专业所在学院不存在");
        }

        if (StringUtil.isNotEmpty(param.getVal())) {
            if (rparam.getOffice() != null) {
                Office curOffice = OfficeUtils.getProfessionalByName(rparam.getOffice().getName(), param.getVal());
                if (curOffice != null) {
                    param.setTag(param.getTag() + 1);
                    iie.setErrmsg("该专业已存在");
                    rparam.setCur(curOffice);
                    rparam.setOrg(curOffice);
                }
            }
        }

        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
            param.getIes().save(iie);
        } else {
            if (StringUtil.isNotEmpty(param.getVal())) {
                Office org = new Office();
                org.setType(OffiType.OT_5.getId());
                org.setGrade(OffiGrade.OG_3.getId());
                org.setParent(rparam.getOffice());
                org.setName(param.getVal());
                rparam.setCur(org);
                rparam.setOrg(org);
            }
        }
        return param;
    }
}

/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import org.apache.log4j.Logger;

import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.entity.ProModelError;
import com.oseasy.pie.modules.impdata.entity.ProModelGcontestError;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.tool.IitCheck;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;
import com.oseasy.sys.common.config.SysSval;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 检验项目成员学历列.
 * @author chenhao
 *
 */
public class ItCkProModelTeamEducation implements IitCheck<ItCparamPm>{
    public final static Logger logger = Logger.getLogger(ItCkProModelTeamEducation.class);

    public ItCkProModelTeamEducation() {
        super();
    }

    @Override
    public String key() {
        return "学历|学历层次";
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
//            ProModelError phe = (ProModelError)pe;
//            ProModelError validinfo = (ProModelError)pev;
        }else if((pe instanceof ProModelGcontestError) && (pev instanceof ProModelGcontestError)){
            ProModelGcontestError phe = (ProModelGcontestError)pe;
            ProModelGcontestError validinfo = (ProModelGcontestError)pev;

            Dict d = null;
            phe.setXueli(param.getVal());
            validinfo.setXueli(param.getVal());
            iie = new ImpInfoErrmsg();
            iie.setImpId(ii.getId());
            iie.setDataId(phe.getId());
            iie.setColname(param.getIdx() + "");

            if (StringUtil.isNotEmpty(param.getVal())) {
                if ((d = DictUtils.getDictByLabel(SysSval.DICT_ENDUCATION_LEVEL, param.getVal())) == null) {
                    param.setTag(param.getTag() + 1);
                    iie.setErrmsg("学历不存在");
                }
            }

            if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                param.getIes().save(iie);
            } else {
                validinfo.setXueli(d.getValue());
            }
        }

        return param;
    }
}

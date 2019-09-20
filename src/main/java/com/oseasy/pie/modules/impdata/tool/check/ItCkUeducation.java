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
import com.oseasy.pie.modules.impdata.entity.StudentError;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.tool.IitCheck;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;
import com.oseasy.sys.common.config.SysSval;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 学生导入.
 * @author chenhao
 */
public class ItCkUeducation implements IitCheck<ItCparamUser>{
    public final static Logger logger = Logger.getLogger(ItCkUeducation.class);

    public ItCkUeducation() {
        super();
    }

    @Override
    public String key() {
        return "学历|学历层次";
    }

    @Override
    public boolean validateKey(ItCparamUser param) {
        if ((key()).contains(ExcelUtils.getStringByCell(param.getXs().getRow(ImpDataService.descHeadRow).getCell(param.getIdx()), param.getXs()))){
            return true;
        }
        return false;
    }

    @Override
    public ItCparamUser validate(ItCparamUser param, IitCheckEetyExt pe, IitCheckEetyExt pev) {
        if(!(param.check() && validateKey(param))){
            return param;
        }

        ImpInfo ii = param.getInfo();
        ImpInfoErrmsg iie = param.getIe();
        StudentError phe = (StudentError)pe;
        StudentError validinfo = (StudentError)pev;

        phe.setEducation(param.getVal());
        validinfo.setEducation(param.getVal());
        iie = new ImpInfoErrmsg();
        iie.setImpId(ii.getId());
        iie.setDataId(phe.getId());
        iie.setColname(param.getIdx() + "");

        Dict d = null;
        if (StringUtil.isNotEmpty(param.getVal())) {
            if ((d = DictUtils.getDictByLabel(SysSval.DICT_ENDUCATION_LEVEL, param.getVal())) == null) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("学历不存在");
            }
        }

        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
            param.getIes().save(iie);
        }else{
            if (d != null){
                param.getUser().setEducation(d.getValue());
            }
        }
        return param;
    }
}
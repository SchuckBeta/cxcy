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
import com.oseasy.pie.modules.impdata.entity.ProModelError;
import com.oseasy.pie.modules.impdata.entity.ProModelGcontestError;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.tool.IitCheck;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 检验项目名称列.
 * @author chenhao
 *
 */
public class ItCkProModelTeamProfes implements IitCheck<ItCparamPm>{
    public final static Logger logger = Logger.getLogger(ItCkProModelTeamProfes.class);

    private Office office;

    public ItCkProModelTeamProfes(Office office) {
        super();
        this.office = office;
    }


    @Override
    public String key() {
        return "专业|专业名称";
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
            ProModelError phe = (ProModelError)pe;
            ProModelError validinfo = (ProModelError)pev;

            Office profes = null;
            phe.setProfes(param.getVal());
            iie = new ImpInfoErrmsg();
            iie.setImpId(ii.getId());
            iie.setDataId(phe.getId());
            iie.setColname(param.getIdx() + "");
            if (StringUtil.isNotEmpty(param.getVal())) {
                if((this.office != null)){
                    if ((profes = OfficeUtils.getProfessionalByName(office.getName(), param.getVal())) == null) {
                        param.setTag(param.getTag() + 1);
                        iie.setErrmsg("专业不存在");
                    }
                }else{
                    if ((profes = OfficeUtils.getProfessionalByName(param.getVal())) == null) {
                        param.setTag(param.getTag() + 1);
                        iie.setErrmsg("专业不存在");
                    }
                }
//            }else{
//                param.setTag(param.getTag() + 1);
//                iie.setErrmsg(key() + "必填信息");
            }
            if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                param.getIes().save(iie);
            } else {
                if (profes != null)
                    validinfo.setProfes(profes.getId());
            }
        }else if((pe instanceof ProModelGcontestError) && (pev instanceof ProModelGcontestError)){
            ProModelGcontestError phe = (ProModelGcontestError)pe;
            ProModelGcontestError validinfo = (ProModelGcontestError)pev;

            Office profes = null;
            phe.setProfes(param.getVal());
            iie = new ImpInfoErrmsg();
            iie.setImpId(ii.getId());
            iie.setDataId(phe.getId());
            iie.setColname(param.getIdx() + "");
            if (StringUtil.isEmpty(param.getVal())) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("必填信息");
            } else if ((profes = OfficeUtils.getProfessionalByName(param.getVal())) == null) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("专业不存在");
            }
            if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                param.getIes().save(iie);
            } else {
                if (profes != null)
                    validinfo.setProfes(profes.getId());
            }
        }

        return param;
    }
}

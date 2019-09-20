/**
 * .
 */

package com.oseasy.pie.modules.iep.tool.check;

import java.util.List;

import org.apache.log4j.Logger;

import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.entity.ProModelError;
import com.oseasy.pie.modules.impdata.entity.ProModelGcontestError;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.tool.IitCheck;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.util.common.utils.SpSteel;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 检验项目名称列.
 * @author chenhao
 *
 */
public class ItCkGgjProName implements IitCheck<ItCparamGgj>{
    public final static Logger logger = Logger.getLogger(ItCkGgjProName.class);
    List<String> filters = null;
    private ProModelService proModelService;

    public ItCkGgjProName(ProModelService proModelService) {
        super();
        this.proModelService = proModelService;
    }

    @Override
    public String key() {
        return "项目名称";
    }

    @Override
    public boolean validateKey(ItCparamGgj param) {
        if ((key()).contains(param.getColName())){
            return true;
        }

        String keyName = ExcelUtils.getStringByCell(param.getXs().getRow(ImpDataService.descHeadRow).getCell(param.getIdx()), param.getXs());
        if ((key()).equals(keyName)){
            return true;
        }
        return false;
    }

    @Override
    public ItCparamGgj validate(ItCparamGgj param, IitCheckEetyExt pe, IitCheckEetyExt pev) {
        if(!(param.check() && validateKey(param) && (param.getActyw() != null) && (this.proModelService != null))){
            return param;
        }
        ImpInfo ii = param.getInfo();
        ImpInfoErrmsg iie = param.getIe();

        filters = DictUtils.getDictVsByType(SpSteel.SP_STEEL_DKEY);
        if((pe instanceof ProModelError) && (pev instanceof ProModelError)){
            ProModelError phe = (ProModelError)pe;
            ProModelError validinfo = (ProModelError)pev;

            param.setVal(SpSteel.replaceAll(param.getVal(), filters));
            phe.setName(param.getVal());
            validinfo.setName(param.getVal());
            iie = new ImpInfoErrmsg(param, ii.getId(), phe.getId());
            if (StringUtil.isEmpty(param.getVal())) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("必填信息");
            } else if (param.getVal().length() > 128) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("最多128个字符");
                phe.setName(null);
            } else if (proModelService.existProName(param.getActyw(), param.getVal())) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("项目名称已存在");
            }

            if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                param.getIes().save(iie);
                validinfo.setValidName(false);
            }else{
                validinfo.setValidName(true);
            }
        }else if((pe instanceof ProModelGcontestError) && (pev instanceof ProModelGcontestError)){
            ProModelGcontestError phe = (ProModelGcontestError)pe;
            ProModelGcontestError validinfo = (ProModelGcontestError)pev;

            System.out.println("ItCkGgjProName--->name="+param.getVal());
            param.setVal(SpSteel.replaceAll(param.getVal(), filters));
            System.out.println("ItCkGgjProName--->name="+param.getVal());
            phe.setName(param.getVal());
            validinfo.setName(param.getVal());
            iie = new ImpInfoErrmsg(param, ii.getId(), phe.getId());
            if (StringUtil.isEmpty(param.getVal())) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("必填信息");
            } else if (param.getVal().length() > 128) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("最多128个字符");
                phe.setName(null);
            } else if (proModelService.existProName(param.getActyw(), param.getVal())) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("大赛名称已存在");
            }

            if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                param.getIes().save(iie);
                validinfo.setValidName(false);
            }else{
                validinfo.setValidName(true);
            }
        }else{
            logger.warn("PE 或 PEV 未定义！");
        }
        return param;
    }
}

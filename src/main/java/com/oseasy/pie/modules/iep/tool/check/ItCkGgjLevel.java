/**
 * .
 */

package com.oseasy.pie.modules.iep.tool.check;

import org.apache.log4j.Logger;

import com.oseasy.act.modules.actyw.tool.process.vo.FlowPlevelType;
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
import com.oseasy.util.common.utils.StringUtil;

/**
 * 检验参赛组别列.
 * @author chenhao
 *
 */
public class ItCkGgjLevel implements IitCheck<ItCparamGgj>{
    public final static Logger logger = Logger.getLogger(ItCkGgjLevel.class);

    @Override
    public String key() {
        return "组别";
    }

    @Override
    public boolean validateKey(ItCparamGgj param) {
        if ((key()).contains(param.getColName())){
            return true;
        }

        if ((key()).equals(ExcelUtils.getStringByCell(param.getXs().getRow(ImpDataService.descHeadRow).getCell(param.getIdx()), param.getXs()))){
            return true;
        }
        return false;
    }

    @Override
    public ItCparamGgj validate(ItCparamGgj param, IitCheckEetyExt pe, IitCheckEetyExt pev) {
        if(!(param.check() && validateKey(param))){
            return param;
        }

        ImpInfo ii = param.getInfo();
        ImpInfoErrmsg iie = param.getIe();
        if((pe instanceof ProModelError) && (pev instanceof ProModelError)){
            ProModelError phe = (ProModelError)pe;
            ProModelError validinfo = (ProModelError)pev;

            Dict d = null;
            phe.setLevel(param.getVal());
            iie = new ImpInfoErrmsg(param, ii.getId(), phe.getId());
            if (StringUtil.isEmpty(param.getVal())) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg(key() + "必填信息");
            } else if ((param.getVal()).length() > 64) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("最多64个字符");
                phe.setType(null);
            } else if ((d = DictUtils.getDictByLabel(FlowPlevelType.PLT_DASAI.getKey(), param.getVal())) == null) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("参赛组别不存在");
            }
            if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                param.getIes().save(iie);
            } else {
                //phe.setLevel(d.getValue());
                validinfo.setLevel(d.getValue());
            }
        }else if((pe instanceof ProModelGcontestError) && (pev instanceof ProModelGcontestError)){
            ProModelGcontestError phe = (ProModelGcontestError)pe;
            ProModelGcontestError validinfo = (ProModelGcontestError)pev;

            Dict d = null;
            phe.setGroups(param.getVal());
            iie = new ImpInfoErrmsg(param, ii.getId(), phe.getId());
            if (StringUtil.isEmpty(param.getVal())) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg(key() + "必填信息");
            } else if ((param.getVal()).length() > 64) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("最多64个字符");
                phe.setType(null);
            } else if ((d = DictUtils.getDictByLabel(FlowPlevelType.PLT_DASAI.getKey(), param.getVal())) == null) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("参赛组别不存在");
            }
            if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                param.getIes().save(iie);
            } else {
                //phe.setGroups(d.getValue());
                validinfo.setGroups(d.getValue());
            }
        }
        return param;
    }
}

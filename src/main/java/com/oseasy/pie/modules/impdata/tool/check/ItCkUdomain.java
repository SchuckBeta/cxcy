/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.entity.User;
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
public class ItCkUdomain implements IitCheck<ItCparamUser>{
    public final static Logger logger = Logger.getLogger(ItCkUdomain.class);

    public ItCkUdomain() {
        super();
    }

    @Override
    public String key() {
        return "擅长技术领域";
    }

    @Override
    public boolean validateKey(ItCparamUser param) {
        if ((key()).equals(ExcelUtils.getStringByCell(param.getXs().getRow(ImpDataService.descHeadRow).getCell(param.getIdx()), param.getXs()))){
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

        phe.setDomain(param.getVal());
        validinfo.setDomain(param.getVal());
        iie = new ImpInfoErrmsg();
        iie.setImpId(ii.getId());
        iie.setDataId(phe.getId());
        iie.setColname(param.getIdx() + "");

        Dict d = null;
        List<String> list = new ArrayList<String>();
        if (StringUtil.isNotEmpty(param.getVal())) {
            if ((param.getVal()).length() > 1024) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("擅长技术领域内容过长");
                phe.setDomain(null);
                validinfo.setDomain(null);
            } else {
                String[] vs = (param.getVal()).split(",");
                for (String v : vs) {
                    if ((d = DictUtils.getDictByLabel(SysSval.DICT_TECHNOLOGY_FIELD, v)) == null) {
                        param.setTag(param.getTag() + 1);
                        iie.setErrmsg("擅长技术领域不存在");
                        break;
                    } else {
                        list.add(d.getValue());
                    }
                }
            }
        }

        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
            param.getIes().save(iie);
        }else{
            if (d != null){
                param.getUser().setDomain(StringUtil.join(list.toArray(), StringUtil.DOTH));
            }
        }
        return param;
    }
}
/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.com.pcore.modules.sys.utils.RegexUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.entity.StudentError;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.tool.IitCheck;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 学生导入.
 * @author chenhao
 */
public class ItCkUemail implements IitCheck<ItCparamUser>{
    public final static Logger logger = Logger.getLogger(ItCkUemail.class);

    public ItCkUemail() {
        super();
    }

    @Override
    public String key() {
        return "邮箱";
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

        phe.setEmail(param.getVal());
        validinfo.setEmail(param.getVal());
        iie = new ImpInfoErrmsg();
        iie.setImpId(ii.getId());
        iie.setDataId(phe.getId());
        iie.setColname(param.getIdx() + "");
        if (StringUtil.isNotEmpty(param.getVal())) {
            if (!Pattern.matches(RegexUtils.emailRegex, param.getVal())) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("邮箱格式不正确");
            } else if (param.getVal().length() > 200) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("最多200个字符");
                phe.setEmail(null);
                validinfo.setEmail(null);
            }
        }

        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
            param.getIes().save(iie);
        }else{
            param.getUser().setEmail(param.getVal());
        }
        return param;
    }
}
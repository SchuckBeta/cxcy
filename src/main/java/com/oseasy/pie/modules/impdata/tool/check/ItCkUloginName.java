/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import org.apache.log4j.Logger;

import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.entity.StudentError;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.tool.IitCheck;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 学生导入-用户名.
 * @author chenhao
 */
public class ItCkUloginName implements IitCheck<ItCparamUser>{
    public final static Logger logger = Logger.getLogger(ItCkUloginName.class);

    public ItCkUloginName() {
        super();
    }

    @Override
    public String key() {
        return "用户名";
    }

    @Override
    public boolean validateKey(ItCparamUser param) {
        if ((key()).equals(ExcelUtils.getStringByCell(param.getXs().getRow(ImpDataService.descHeadRow).getCell(param.getIdx()), param.getXs()))){
            return true;
        }
        if(param.getUser() == null){
            logger.warn("ItCparamUser.user 属性不能为空！");
            return false;
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

        phe.setLoginName(param.getVal());
        validinfo.setLoginName(param.getVal());
        iie = new ImpInfoErrmsg();
        iie.setImpId(ii.getId());
        iie.setDataId(phe.getId());
        iie.setColname(param.getIdx() + "");
        if (StringUtil.isNotEmpty(param.getVal())) {
//            if (UserUtils.getByLoginNameOrNo(param.getVal()) != null) {
//                param.setTag(param.getTag() + 1);
//                iie.setErrmsg("用户名已存在");
//            } else if (param.getVal().length() > 100) {
//                param.setTag(param.getTag() + 1);
//                iie.setErrmsg("最多100个字符");
//                phe.setLoginName(null);
//                validinfo.setLoginName(null);
//            }
            if (param.getVal().length() > 100) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("最多100个字符");
                phe.setLoginName(null);
                validinfo.setLoginName(null);
            }
        }
        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
            param.getIes().save(iie);
        }else{
            param.getUser().setLoginName(param.getVal());
        }
        return param;
    }
}
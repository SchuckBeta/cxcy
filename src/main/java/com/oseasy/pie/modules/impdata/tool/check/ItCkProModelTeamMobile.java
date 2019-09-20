/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.RegexUtils;
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
public class ItCkProModelTeamMobile implements IitCheck<ItCparamPm>{
    public final static Logger logger = Logger.getLogger(ItCkProModelTeamMobile.class);

    private UserService userService;

    public ItCkProModelTeamMobile(UserService userService) {
        super();
        this.userService = userService;
    }

    @Override
    public String key() {
        return "联系电话|负责人手机号|手机号码";
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
        if(!(param.check() && validateKey(param) && (this.userService != null))){
            return param;
        }

        ImpInfo ii = param.getInfo();
        ImpInfoErrmsg iie = param.getIe();
        if((pe instanceof ProModelError) && (pev instanceof ProModelError)){
            ProModelError phe = (ProModelError)pe;
            ProModelError validinfo = (ProModelError)pev;

            phe.setMobile(param.getVal());
            validinfo.setMobile(param.getVal());
            iie = new ImpInfoErrmsg();
            iie.setImpId(ii.getId());
            iie.setDataId(phe.getId());
            iie.setColname(param.getIdx() + "");
            if (!StringUtil.isEmpty(param.getVal())) {
                if (!Pattern.matches(RegexUtils.mobileRegex, param.getVal())) {
                    param.setTag(param.getTag() + 1);
                    iie.setErrmsg("手机号格式不正确");
                } else {
                    User u = new User();
                    u.setMobile(param.getVal());
                    User temu = userService.getByMobile(u);
                    if (temu != null && phe.getNo() != null && !phe.getNo().equals(temu.getNo())) {
                        param.setTag(param.getTag() + 1);
                        iie.setErrmsg("手机号已存在");
                    }
                }
            }
            if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                param.getIes().save(iie);
            }
        }else if((pe instanceof ProModelGcontestError) && (pev instanceof ProModelGcontestError)){
            ProModelGcontestError phe = (ProModelGcontestError)pe;
            ProModelGcontestError validinfo = (ProModelGcontestError)pev;

            phe.setMobile(param.getVal());
            validinfo.setMobile(param.getVal());
            iie = new ImpInfoErrmsg();
            iie.setImpId(ii.getId());
            iie.setDataId(phe.getId());
            iie.setColname(param.getIdx() + "");
            if (!StringUtil.isEmpty(param.getVal())) {
                if (!Pattern.matches(RegexUtils.mobileRegex, param.getVal())) {
                    param.setTag(param.getTag() + 1);
                    iie.setErrmsg("手机号格式不正确");
                } else {
                    User u = new User();
                    u.setMobile(param.getVal());
                    User temu = userService.getByMobile(u);
                    if (temu != null && phe.getNo() != null && !phe.getNo().equals(temu.getNo())) {
                        param.setTag(param.getTag() + 1);
                        iie.setErrmsg("手机号已存在");
                    }
                }
            }
            if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                param.getIes().save(iie);
            }
        }
        return param;
    }
}

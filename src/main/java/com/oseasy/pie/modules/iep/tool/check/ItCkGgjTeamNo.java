/**
 * .
 */

package com.oseasy.pie.modules.iep.tool.check;

import org.apache.log4j.Logger;

import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.entity.ProModelGJError;
import com.oseasy.pie.modules.impdata.entity.ProModelGcontestError;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.tool.IitCheck;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;
import com.oseasy.sys.common.utils.SysUserUtils;
import com.oseasy.sys.modules.sys.enums.RoleBizTypeEnum;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 检验项目名称列.
 * @author chenhao
 *
 */
public class ItCkGgjTeamNo implements IitCheck<ItCparamGgj>{
    public final static Logger logger = Logger.getLogger(ItCkGgjTeamNo.class);

    private UserService userService;

    public ItCkGgjTeamNo(UserService userService) {
        super();
        this.userService = userService;
    }

    @Override
    public String key() {
        return "学号|负责人学号";
    }

    @Override
    public boolean validateKey(ItCparamGgj param) {
        if ((key()).contains(param.getColName())){
            return true;
        }
        if ((key()).contains(ExcelUtils.getStringByCell(param.getXs().getRow(ImpDataService.descHeadRow).getCell(param.getIdx()), param.getXs()))){
            return true;
        }
        return false;
    }

    @Override
    public ItCparamGgj validate(ItCparamGgj param, IitCheckEetyExt pe, IitCheckEetyExt pev) {
        if(!(param.check() && validateKey(param) && (this.userService != null))){
            return param;
        }

        ImpInfo ii = param.getInfo();
        ImpInfoErrmsg iie = param.getIe();
        if((pe instanceof ProModelGJError) && (pev instanceof ProModelGJError)){
            ProModelGJError phe = (ProModelGJError)pe;
            ProModelGJError validinfo = (ProModelGJError)pev;

            phe.setNo(param.getVal());
            validinfo.setNo(param.getVal());
            iie = new ImpInfoErrmsg();
            iie.setImpId(ii.getId());
            iie.setDataId(phe.getId());
            iie.setColname(param.getIdx() + "");
            if (StringUtil.isEmpty(param.getVal())) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("必填信息");
            } else if ((param.getVal()).length() > 100) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("最多100个字符");
                phe.setNo(null);
            } else {
                User u = userService.getByNo(param.getVal());
                if((u == null) && StringUtil.isNotEmpty(phe.getName()) && StringUtil.isNotEmpty(param.getVal())){
                    u = userService.getUserByMobileAndName(phe.getName(), param.getVal());
                }
                if (u != null && !SysUserUtils.checkHasRole(u, RoleBizTypeEnum.XS)) {
                    param.setTag(param.getTag() + 1);
                    iie.setErrmsg("找到该学号人员，但不是学生");
                } else if (u != null && phe.getLeader() != null && !phe.getLeader().equals(u.getName())) {
                    param.setTag(param.getTag() + 1);
                    iie.setErrmsg("负责人学号和姓名不一致");
                }
            }
            if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                param.getIes().save(iie);
            }
        }else if((pe instanceof ProModelGcontestError) && (pev instanceof ProModelGcontestError)){
            ProModelGcontestError phe = (ProModelGcontestError)pe;
            ProModelGcontestError validinfo = (ProModelGcontestError)pev;

            phe.setNo(param.getVal());
            validinfo.setNo(param.getVal());
            iie = new ImpInfoErrmsg();
            iie.setImpId(ii.getId());
            iie.setDataId(phe.getId());
            iie.setColname(param.getIdx() + "");
            if (StringUtil.isEmpty(param.getVal())) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("必填信息");
            } else if (param.getVal().length() > 100) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("最多100个字符");
                phe.setNo(null);
            } else {
                User u = userService.getByNo(param.getVal());
                if(u == null){
                    u = userService.getUserByMobileAndName(phe.getName(), param.getVal());
                }
                if (u != null && !SysUserUtils.checkHasRole(u, RoleBizTypeEnum.XS)) {
                    param.setTag(param.getTag() + 1);
                    iie.setErrmsg("找到该学号人员，但不是学生");
                } else if (u != null && phe.getLeader() != null && !phe.getLeader().equals(u.getName())) {
                    param.setTag(param.getTag() + 1);
                    iie.setErrmsg("负责人学号和姓名不一致");
                }
            }
            if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                param.getIes().save(iie);
            }
        }

        return param;
    }
}

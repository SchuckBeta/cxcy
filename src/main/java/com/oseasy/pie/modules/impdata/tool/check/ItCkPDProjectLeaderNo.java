/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import java.util.List;

import org.apache.log4j.Logger;

import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.entity.ProjectError;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.tool.IitCheck;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 大创-检验项目负责人学号.
 * @author chenhao
 *
 */
public class ItCkPDProjectLeaderNo implements IitCheck<ItCparamPm>{
    public static Logger logger = Logger.getLogger(ItCkPDProjectLeaderNo.class);
    private List<User> leader;

    public ItCkPDProjectLeaderNo(List<User> leader) {
        super();
        this.leader = leader;
    }

    @Override
    public String key() {
        return "项目负责人学号";
    }

    @Override
    public boolean validateKey(ItCparamPm param) {
        if ((key()).equals(ExcelUtils.getStringByCell(param.getXs().getRow(ImpDataService.descHeadRow).getCell(param.getIdx()), param.getXs()))){
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
        ProjectError phe = (ProjectError)pe;
        ProjectError validinfo = (ProjectError)pev;

        phe.setLeaderNo(param.getVal());
        validinfo.setLeaderNo(param.getVal());
        iie = new ImpInfoErrmsg();
        iie.setImpId(ii.getId());
        iie.setDataId(phe.getId());
        iie.setColname(param.getIdx() + "");


        if (StringUtil.isEmpty(param.getVal())) {
            param.setTag(param.getTag() + 1);
            iie.setErrmsg("必填信息");
        }else if ((param.getVal()).length() > 64) {
            param.setTag(param.getTag() + 1);
            iie.setErrmsg("最多64个字符");
            phe.setLeaderNo(null);
        } else if (leader == null || leader.isEmpty()) {
            param.setTag(param.getTag() + 1);
            iie.setErrmsg("系统中未找到该项目负责人，请确认姓名和学号无误或先在系统中录入该项目负责人");
        } else if (leader != null && leader.size() > 1) {
            param.setTag(param.getTag() + 1);
            iie.setErrmsg("系统中存在多个与此相同姓名、学号的学生，请联系管理员处理");
        }
        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
            param.getIes().save(iie);
        } else {
            validinfo.setLeaderNo(leader.get(0).getId());
        }
        return param;
    }
}

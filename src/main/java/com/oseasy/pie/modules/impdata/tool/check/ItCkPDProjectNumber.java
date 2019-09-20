/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import java.util.List;

import org.apache.log4j.Logger;

import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.entity.ProjectError;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.tool.IitCheck;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;
import com.oseasy.pro.modules.project.entity.ProjectDeclare;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 大创-检验项目编号.
 * @author chenhao
 *
 */
public class ItCkPDProjectNumber implements IitCheck<ItCparamPm>{
    public static Logger logger = Logger.getLogger(ItCkPDProjectNumber.class);

    private ProjectDeclareService projectDeclareService;

    public ItCkPDProjectNumber(ProjectDeclareService projectDeclareService) {
        super();
        this.projectDeclareService = projectDeclareService;
    }

    @Override
    public String key() {
        return "项目编号";
    }

    @Override
    public boolean validateKey(ItCparamPm param) {
        String keyName = ExcelUtils.getStringByCell(param.getXs().getRow(ImpDataService.descHeadRow).getCell(param.getIdx()), param.getXs());
        if ((key()).equals(keyName)){
            return true;
        }
        return false;
    }

    @Override
    public ItCparamPm validate(ItCparamPm param, IitCheckEetyExt pe, IitCheckEetyExt pev) {
        if(!(param.check() && validateKey(param) && (param.getActyw() != null))){
            return param;
        }
        ImpInfo ii = param.getInfo();
        ImpInfoErrmsg iie = param.getIe();

        ProjectError phe = (ProjectError)pe;
        ProjectError validinfo = (ProjectError)pev;

        phe.setNumber(param.getVal());
        validinfo.setNumber(param.getVal());
        iie = new ImpInfoErrmsg();
        iie.setImpId(ii.getId());
        iie.setDataId(phe.getId());
        iie.setColname(param.getIdx() + "");

        if (StringUtil.isEmpty(param.getVal())) {
            param.setTag(param.getTag() + 1);
            iie.setErrmsg("必填信息");
        } else if ((param.getVal()).length() > 64) {
            param.setTag(param.getTag() + 1);
            iie.setErrmsg("最多64个字符");
            phe.setNumber(null);
        } else {
            List<ProjectDeclare> plist = projectDeclareService.getProjectByCdn(param.getVal(), null, null);
            if (plist != null && !plist.isEmpty()) {
              param.setTag(param.getTag() + 1);
              iie.setErrmsg("该项目编号已经存在");
            }
        }

        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
            param.getIes().save(iie);
        }

        return param;
    }
}

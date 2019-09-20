/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;

import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
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
public class ItCkProModelTeacherNameReq implements IitCheck<ItCparamPm>{
    public final static Logger logger = Logger.getLogger(ItCkProModelTeacherNameReq.class);

    private XSSFRow rowData;

    public ItCkProModelTeacherNameReq(XSSFRow rowData) {
        super();
        this.rowData = rowData;
    }

    @Override
    public String key() {
        return "指导教师姓名";
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
        if(!(param.check() && validateKey(param) && (this.rowData != null))){
            return param;
        }

        ImpInfo ii = param.getInfo();
        ImpInfoErrmsg iie = param.getIe();
        if((pe instanceof ProModelError) && (pev instanceof ProModelError)){
            ProModelError phe = (ProModelError)pe;
            ProModelError validinfo = (ProModelError)pev;

            phe.setTeachers(param.getVal());
            validinfo.setTeachers(param.getVal());
            iie = new ImpInfoErrmsg();
            iie.setImpId(ii.getId());
            iie.setDataId(phe.getId());
            iie.setColname(param.getIdx() + "");
            if (StringUtil.isEmpty(param.getVal())) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("必填信息(第一导师)");
            } else if ((param.getVal()).length() > 128) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("最多128个字符");
                phe.setTeachers(null);
            } else {
                String temval = ExcelUtils.getStringByCell(this.rowData.getCell(param.getIdx() + 1), param.getXs());
                if (temval != null) {// 去掉所有空格
                    temval = temval.replaceAll(" ", "");
                }
                String s = checkTeaName(param.getVal(), temval);
                if (s != null) {
                    param.setTag(param.getTag() + 1);
                    iie.setErrmsg(s);
                }
            }
            if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                param.getIes().save(iie);
            }
        }else if((pe instanceof ProModelGcontestError) && (pev instanceof ProModelGcontestError)){
            ProModelGcontestError phe = (ProModelGcontestError)pe;
            ProModelGcontestError validinfo = (ProModelGcontestError)pev;
            //TODO CHENHAO
        }

        return param;
    }

    public static String checkTeaName(String teaName, String teaNo) {
        if (StringUtil.isEmpty(teaNo)) {
            return null;
        }
        List<String> lname = new ArrayList<String>();
        List<String> lno = new ArrayList<String>();
        for (String s : teaName.split("、")) {
            if (StringUtil.isNotEmpty(s)) {
                lname.add(s);
            }
        }
        for (String s : teaNo.split("、")) {
            if (StringUtil.isNotEmpty(s)) {
                lno.add(s);
            }
        }
        if (lname.size() < lno.size()) {
            return "请填写导师姓名";
        }
        return null;
    }
}

/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.com.pcore.modules.sys.service.UserService;
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
public class ItCkProModelTeacherTitle implements IitCheck<ItCparamPm>{
    public final static Logger logger = Logger.getLogger(ItCkProModelTeacherTitle.class);

    private UserService userService;

    public ItCkProModelTeacherTitle(UserService userService) {
        super();
        this.userService = userService;
    }

    @Override
    public String key() {
        return "职称";
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
        if((pe instanceof ProModelError) && (pev instanceof ProModelError)){
            ProModelError phe = (ProModelError)pe;
            ProModelError validinfo = (ProModelError)pev;

            phe.setTeaTitle(param.getVal());
            validinfo.setTeaTitle(param.getVal());
            iie = new ImpInfoErrmsg();
            iie.setImpId(ii.getId());
            iie.setDataId(phe.getId());
            iie.setColname(param.getIdx() + "");
            if (!StringUtil.isEmpty(param.getVal())) {
                if ((param.getVal()).length() > 100) {
                    param.setTag(param.getTag() + 1);
                    iie.setErrmsg("最多100个字符");
                    phe.setTeaTitle(null);
                }else{
                    String s = checkTeaTitle(userService, phe.getTeachers(), param.getVal());
                    if (s != null) {
                        param.setTag(param.getTag() + 1);
                        iie.setErrmsg(s);
                    }
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

    public static String checkTeaTitle(UserService userService, String teaName, String teaTitle) {
        if (StringUtil.isEmpty(teaName)) {
            return null;
        }
        List<String> lname = new ArrayList<String>();
        List<String> lti = new ArrayList<String>();
        for (String s : teaName.split(StringUtil.Z_DUN)) {
            if (StringUtil.isNotEmpty(s)) {
                lname.add(s);
            }
        }
        if (StringUtil.isNotEmpty(teaTitle)) {
            for(String s:teaTitle.split(StringUtil.Z_DUN)) {
                if (StringUtil.isNotEmpty(s)) {
                    lti.add(s);
                }
            }
        }

//        if (lname.size() != lti.size()) {
//            return "指导教师姓名、职称必须数量一致！";
//        }
        return null;
    }
}

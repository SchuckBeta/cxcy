/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.entity.ProModelError;
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
public class ItCkProModelTeacherNo implements IitCheck<ItCparamPm>{
    public final static Logger logger = Logger.getLogger(ItCkProModelTeacherNo.class);

    private UserService userService;

    public ItCkProModelTeacherNo(UserService userService) {
        super();
        this.userService = userService;
    }

    @Override
    public String key() {
        return "指导教师工号";
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
        if(!(param.check() && validateKey(param) && (this.userService != null))){
            return param;
        }

        ImpInfo ii = param.getInfo();
        ImpInfoErrmsg iie = param.getIe();
        if((pe instanceof ProModelError) && (pev instanceof ProModelError)){
            ProModelError phe = (ProModelError)pe;
            ProModelError validinfo = (ProModelError)pev;

            phe.setTeaNo(param.getVal());
            validinfo.setTeaNo(param.getVal());
            iie = new ImpInfoErrmsg();
            iie.setImpId(ii.getId());
            iie.setDataId(phe.getId());
            iie.setColname(param.getIdx() + "");
            if (StringUtil.isNotEmpty(param.getVal())) {
                if ((param.getVal()).length() > 128) {
                    param.setTag(param.getTag() + 1);
                    iie.setErrmsg("最多128个字符");
                    phe.setTeaNo(null);
                } else {
                    String s = checkTeaNo(userService, phe.getTeachers(), param.getVal());
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

    public static String checkTeaNo(UserService userService, String teaName, String teaNo) {
        if (StringUtil.isEmpty(teaName)) {
            return null;
        }
        List<String> lname = new ArrayList<String>();
        List<String> lno = new ArrayList<String>();
        List<String> lti = new ArrayList<String>();
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

        if((lno.size() != lti.size()) || (lno.size() != lname.size())){
        }

        if (lname.size() != lno.size()) {
            return "指导教师编号、姓名必须数量一致！";
        } else if (lname.size() == lno.size()) {
            Map<String, String> map = new HashMap<String, String>();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < lname.size(); i++) {
                User u = userService.getByNo(lno.get(i));
                if (u != null && !SysUserUtils.checkHasRole(u, RoleBizTypeEnum.DS)) {
                    sb.append(lname.get(i)).append(lno.get(i)).append("找到该工号人员，但不是导师;");
                } else if (u != null && !lname.get(i).equals(StringUtil.trim(u.getName()))) {
                    sb.append(lname.get(i)).append(lno.get(i)).append("姓名工号不一致;");
                } else if (map.get(lno.get(i)) != null) {
                    sb.append(lno.get(i)).append("工号重复;");
                } else {
                    map.put(lno.get(i), lno.get(i));
                }
            }
            if (StringUtil.isNotEmpty(sb)) {
                return sb.toString();
            }
        }
        return null;
    }
}

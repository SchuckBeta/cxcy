/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.entity.ProjectError;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.service.TeacherErrorService;
import com.oseasy.pie.modules.impdata.tool.IitCheck;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;
import com.oseasy.sys.common.config.SysIds;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.vo.TeacherType;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 大创-检验项目指导教师姓名.
 * @author chenhao
 *
 */
public class ItCkPDProjectTeacherName implements IitCheck<ItCparamPm>{
    public static Logger logger = Logger.getLogger(ItCkPDProjectTeacherName.class);

    private UserService userService;
    private TeacherErrorService teacherErrorService;

    public ItCkPDProjectTeacherName(UserService userService, TeacherErrorService teacherErrorService) {
        super();
        this.userService = userService;
        this.teacherErrorService = teacherErrorService;
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
        if(!(param.check() && validateKey(param))){
            return param;
        }

        ImpInfo ii = param.getInfo();
        ImpInfoErrmsg iie = param.getIe();
        ProjectError phe = (ProjectError)pe;
        ProjectError validinfo = (ProjectError)pev;

        phe.setTeacher(param.getVal());
        validinfo.setTeacher(param.getVal());
        iie = new ImpInfoErrmsg();
        iie.setImpId(ii.getId());
        iie.setDataId(phe.getId());
        iie.setColname(param.getIdx() + "");


        if (StringUtil.isEmpty(param.getVal())) {
            param.setTag(param.getTag() + 1);
            iie.setErrmsg("必填信息");
        }else {
            Map<String, String> map = new HashMap<String, String>();
            List<String> temlist = new ArrayList<String>();
            String[] mebs = param.getVal().split(StringUtil.DOTH);
            for (String meb : mebs) {
                String[] info = meb.split(StringUtil.LINE);
                if (info.length == 1) {
                    List<User> mlist = userService.getTeaByCdn(null, info[0]);
                    if (mlist == null || mlist.isEmpty()) {
//                        param.setTag(param.getTag() + 1);
//                        iie.setErrmsg(info[0] + "，系统中未找到该导师，请确认姓名无误或先在系统中录入该导师");
//                        break;

                        User u=new User();
                        BackTeacherExpansion tea=new BackTeacherExpansion();
                        tea.setTeachertype(TeacherType.TY_XY.getKey());
                        u.setOffice(new Office(CoreIds.NCE_SYS_OFFICE_TOP.getId()));
                        u.setProfessional(SysIds.SYS_OFFICE_ZY_QT.getId());
                        u.setName(info[0]);
                        u.setNo(IdGen.randomBase62(10));
                        tea.setUser(u);
                        teacherErrorService.saveTeacher(tea);
                        temlist.add(u.getId());
                    } else if (mlist != null && mlist.size() > 1) {
                        param.setTag(param.getTag() + 1);
                        iie.setErrmsg(info[0] + "，系统中存在多个与此相同姓名的导师，请以姓名/工号的格式添加工号，多个导师以英文输入法逗号分隔");
                        break;
                    } else if (map.get(info[0]) != null) {
                        param.setTag(param.getTag() + 1);
                        iie.setErrmsg(info[0] + "姓名重复");
                        break;
                    } else {
                        map.put(info[0], info[0]);
                        temlist.add(mlist.get(0).getId());
                    }
                } else if (info.length == 2) {
                    List<User> mlist = userService.getTeaByCdn(info[1], info[0]);
                    if (mlist == null || mlist.isEmpty()) {
//                        param.setTag(param.getTag() + 1);
//                        iie.setErrmsg(info[0] + StringUtil.LINE + info[1] + "，系统中未找到该导师，请确认姓名、工号无误或先在系统中录入该导师");
//                        break;
                        User u=new User();
                        BackTeacherExpansion tea=new BackTeacherExpansion();
                        tea.setTeachertype(TeacherType.TY_XY.getKey());
                        u.setOffice(new Office(CoreIds.NCE_SYS_OFFICE_TOP.getId()));
                        u.setProfessional(SysIds.SYS_OFFICE_ZY_QT.getId());
                        u.setName(info[0]);
                        u.setNo(info[1]);
                        tea.setUser(u);
                        teacherErrorService.saveTeacher(tea);
                        temlist.add(u.getId());
                    } else if (mlist != null && mlist.size() > 1) {
                        param.setTag(param.getTag() + 1);
                        iie.setErrmsg(info[0] + StringUtil.LINE + info[1] + "，系统中存在多个与此相同姓名、工号的导师，请联系管理员处理");
                        break;
                    } else if (map.get(info[0]) != null) {
                        param.setTag(param.getTag() + 1);
                        iie.setErrmsg(info[0] + "姓名重复");
                        break;
                    } else if (map.get(info[1]) != null) {
                        param.setTag(param.getTag() + 1);
                        iie.setErrmsg(info[1] + "工号重复");
                        break;
                    } else {
                        map.put(info[0], info[0]);
                        map.put(info[1], info[1]);
                        temlist.add(mlist.get(0).getId());
                    }
                }
            }
            validinfo.setTeacher(StringUtil.join(temlist.toArray(), StringUtil.DOTH));
        }

        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
            param.getIes().save(iie);
        }
        return param;
    }
}

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
import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.entity.ProjectError;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.service.StudentErrorService;
import com.oseasy.pie.modules.impdata.tool.IitCheck;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;
import com.oseasy.sys.common.config.SysIds;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;
import com.oseasy.sys.modules.sys.enums.RoleBizTypeEnum;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 大创-检验项目其他成员信息.
 * @author chenhao
 *
 */
public class ItCkPDProjectTeamMembers implements IitCheck<ItCparamPm>{
    public static Logger logger = Logger.getLogger(ItCkPDProjectTeamMembers.class);

    private UserService userService;
    private StudentErrorService studentErrorService;

    public ItCkPDProjectTeamMembers(UserService userService, StudentErrorService studentErrorService) {
        super();
        this.userService = userService;
        this.studentErrorService = studentErrorService;
    }

    @Override
    public String key() {
        return "项目其他成员信息";
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

        phe.setTeamStuInfo(param.getVal());
        validinfo.setTeamStuInfo(param.getVal());
        iie = new ImpInfoErrmsg();
        iie.setImpId(ii.getId());
        iie.setDataId(phe.getId());
        iie.setColname(param.getIdx() + "");

        if (!StringUtil.isEmpty(param.getVal())) {
            List<String> temlist = new ArrayList<String>();
            String[] mebs = param.getVal().split(StringUtil.DOTH);
            Map<String, String> map = new HashMap<String, String>();
            for (String meb : mebs) {
                String[] info = meb.split(StringUtil.LINE);
                if (info == null || info.length != 2) {
                    param.setTag(param.getTag() + 1);
                    iie.setErrmsg("格式有误");
                    break;
                } else {
                    List<User> mlist = userService.getStuByCdn(info[1], info[0]);
                    if (mlist == null || mlist.isEmpty()) {
//                        param.setTag(param.getTag() + 1);
//                        iie.setErrmsg(info[0] + StringUtil.LINE + info[1] + ",系统中未找到该项目成员，请确认姓名和学号无误或先在系统中录入该项目成员");
//                        break;
                        User lr = new User();
                        StudentExpansion leaderStu=new StudentExpansion();
                        lr.setOffice(new Office(CoreIds.NCE_SYS_OFFICE_TOP.getId()));
                        lr.setProfessional(SysIds.SYS_OFFICE_ZY_QT.getId());
                        lr.setUserType(RoleBizTypeEnum.XS.getValue());
                        lr.setName(info[0]);
                        lr.setNo(info[1]);
                        leaderStu.setUser(lr);
                        studentErrorService.saveStudent(leaderStu);
                        temlist.add(leaderStu.getUser().getId());
                    } else if (mlist != null && mlist.size() > 1) {
                        param.setTag(param.getTag() + 1);
                        iie.setErrmsg(info[0] + StringUtil.LINE + info[1] + ",系统中存在多个与此相同姓名、学号的学生，请联系管理员处理");
                        break;
                    } else if (info[1].equals(phe.getLeaderNo())) {
                        param.setTag(param.getTag() + 1);
                        iie.setErrmsg(info[1] + "学号和负责人重复");
                        break;
                    } else if (map.get(info[1]) != null) {
                        param.setTag(param.getTag() + 1);
                        iie.setErrmsg(info[1] + "成员学号重复");
                        break;
                    } else {
                        map.put(info[1], info[1]);
                        temlist.add(mlist.get(0).getId());
                    }
                }
            }
            validinfo.setTeamStuInfo(StringUtil.join(temlist.toArray(), StringUtil.DOTH));
        }

        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
            param.getIes().save(iie);
        }
        return param;
    }
}

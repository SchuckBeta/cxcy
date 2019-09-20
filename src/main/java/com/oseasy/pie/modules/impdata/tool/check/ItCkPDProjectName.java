/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;

import com.google.common.collect.Lists;
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
import com.oseasy.pro.modules.project.entity.ProjectDeclare;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.sys.common.config.SysIds;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;
import com.oseasy.sys.modules.sys.enums.RoleBizTypeEnum;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 大创-检验项目名称列.
 * @author chenhao
 *
 */
public class ItCkPDProjectName implements IitCheck<ItCparamPm>{
    public static Logger logger = Logger.getLogger(ItCkPDProjectName.class);

    private XSSFRow rowData;
    private List<User> leader;
    private UserService userService;
    private StudentErrorService studentErrorService;
    private ProjectDeclareService projectDeclareService;

    public ItCkPDProjectName(UserService userService, XSSFRow rowData, ProjectDeclareService projectDeclareService, StudentErrorService studentErrorService) {
        super();
        this.rowData = rowData;
        this.userService = userService;
        this.studentErrorService = studentErrorService;
        this.projectDeclareService = projectDeclareService;
    }

    @Override
    public String key() {
        return "项目名称";
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
        if(!(param.check() && validateKey(param) && (this.userService != null))){
            return param;
        }
        ImpInfo ii = param.getInfo();
        ImpInfoErrmsg iie = param.getIe();

        ProjectError phe = (ProjectError)pe;
        ProjectError validinfo = (ProjectError)pev;

        phe.setName(param.getVal());
        validinfo.setName(param.getVal());
        iie = new ImpInfoErrmsg();
        iie.setImpId(ii.getId());
        iie.setDataId(phe.getId());
        iie.setColname(param.getIdx() + "");

        String no = ExcelUtils.getStringByCell(this.rowData.getCell(param.getIdx() + 3), param.getXs());
        String name = ExcelUtils.getStringByCell(this.rowData.getCell(param.getIdx() + 2), param.getXs());
        if (StringUtil.isEmpty(no) || StringUtil.isEmpty(name)) {
            param.setTag(param.getTag() + 1);
            iie.setErrmsg("项目Leader的姓名和学号不能为空！");
        }
        this.leader = this.userService.getStuByCdn(no, name);
        if ((this.leader == null) || (this.leader.size() <= 0)) {
            //param.setTag(param.getTag() + 1);
            //iie.setErrmsg("项目Leader不存在");
            this.leader = Lists.newArrayList();
            User lr = new User();
            StudentExpansion leaderStu=new StudentExpansion();
            lr.setOffice(new Office(CoreIds.NCE_SYS_OFFICE_TOP.getId()));
            lr.setProfessional(SysIds.SYS_OFFICE_ZY_QT.getId());
            lr.setUserType(RoleBizTypeEnum.XS.getValue());
            lr.setName(name);
            lr.setNo(no);
            leaderStu.setUser(lr);
            studentErrorService.saveStudent(leaderStu);
            this.leader.add(lr);
        }

        if (StringUtil.isEmpty(param.getVal())) {
            param.setTag(param.getTag() + 1);
            iie.setErrmsg("必填信息");
        } else if ((param.getVal()).length() > 128) {
            param.setTag(param.getTag() + 1);
            iie.setErrmsg("最多128个字符");
            phe.setName(null);
        } else if (this.leader != null && this.leader.size() == 1) {
            List<ProjectDeclare> plist = projectDeclareService.getProjectByCdn(null, param.getVal(), this.leader.get(0).getId());
            if (plist != null && !plist.isEmpty()) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("该项目负责人下已存在相同项目名称的项目");
            }
        }

        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
            param.getIes().save(iie);
        }

        return param;
    }

    public List<User> getLeader() {
        return this.leader;
    }

    public void setLeader(List<User> leader) {
        this.leader = leader;
    }
}

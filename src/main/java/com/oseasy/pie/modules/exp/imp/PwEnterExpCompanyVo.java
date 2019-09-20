/**
 * .
 */

package com.oseasy.pie.modules.exp.imp;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.pw.modules.pw.entity.PwEnter;
import com.oseasy.pw.modules.pw.entity.PwEnterDetail;
import com.oseasy.pw.modules.pw.entity.PwEnterRoom;
import com.oseasy.util.common.utils.StringUtil;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

/**
 * .
 * @author chenhao
 *
 */
@ExcelTarget("pwEnterExpCompanyVo")
public class PwEnterExpCompanyVo implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    @Excel(name = "企业名称", orderNum = "1", needMerge= true, mergeRely = {2}, width = 30, height = 12)
    private String companyName; //公司名称
    @Excel(name = "所属场地", orderNum = "2", needMerge= true, mergeRely = {2}, width = 100)
    private String rooms; //所属场地
    @ExcelCollection(name = "项目", orderNum = "3")
    private List<PwEnterExpProject> projects; //项目
    @Excel(name = "团队负责人", orderNum = "4", needMerge= true, mergeRely = {2},  width = 30, height = 12)
    private String appName; //申请人姓名
    @Excel(name = "成员", orderNum = "5", needMerge= true, mergeRely = {2},  width = 30, height = 12)
    private String teamStuName; //团队成员名称
    @Excel(name = "导师", orderNum = "6", needMerge= true, mergeRely = {2},  width = 30, height = 12)
    private String teamTeaName; //团队导师名称

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRooms() {
        return rooms;
    }

    public void setRooms(String rooms) {
        this.rooms = rooms;
    }

    public List<PwEnterExpProject> getProjects() {
        return projects;
    }

    public void setProjects(List<PwEnterExpProject> projects) {
        this.projects = projects;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTeamStuName() {
        return teamStuName;
    }

    public void setTeamStuName(String teamStuName) {
        this.teamStuName = teamStuName;
    }

    public String getTeamTeaName() {
        return teamTeaName;
    }

    public void setTeamTeaName(String teamTeaName) {
        this.teamTeaName = teamTeaName;
    }

    public static PwEnterExpCompanyVo convert(PwEnter pwEnter) {
        PwEnterExpCompanyVo expVo = new PwEnterExpCompanyVo();
        if((pwEnter.getEcompany() != null) && (pwEnter.getEcompany().getPwCompany() != null) && StringUtil.isNotEmpty(pwEnter.getEcompany().getPwCompany().getName())){
            expVo.setCompanyName(pwEnter.getEcompany().getPwCompany().getName());
        }else{
            expVo.setCompanyName(StringUtil.EMPTY);
        }

        if((pwEnter.getEteam() != null) && (pwEnter.getEteam().getTeam() != null)){
            if(StringUtil.isNotEmpty(pwEnter.getEteam().getSnames())){
                expVo.setTeamStuName(pwEnter.getEteam().getSnames());
            }else{
                expVo.setTeamStuName(StringUtil.EMPTY);
            }

            if(StringUtil.isNotEmpty(pwEnter.getEteam().getTnames())){
                expVo.setTeamTeaName(pwEnter.getEteam().getTnames());
            }else{
                expVo.setTeamTeaName(StringUtil.EMPTY);
            }
        }else{
            expVo.setTeamStuName(StringUtil.EMPTY);
            expVo.setTeamTeaName(StringUtil.EMPTY);
        }

        if((pwEnter.getApplicant() != null)){
            if(StringUtil.isNotEmpty(pwEnter.getApplicant().getName())){
                expVo.setAppName(pwEnter.getApplicant().getName());
            }else{
                expVo.setAppName(StringUtil.EMPTY);
            }
        }else{
            expVo.setAppName(StringUtil.EMPTY);
        }

        if(StringUtil.checkNotEmpty(pwEnter.getErooms())){
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < pwEnter.getErooms().size(); i++) {
                PwEnterRoom eroom = pwEnter.getErooms().get(i);
                if(i == 0){
                    buffer.append(eroom.getPwRoom().getPwSpace().genNames() + StringUtil.LINE+ eroom.getPwRoom().getName());
                }else{
                    buffer.append(StringUtil.FMT_N);
                    buffer.append(eroom.getPwRoom().getPwSpace().genNames() + StringUtil.LINE+ eroom.getPwRoom().getName());
                }
            }
            expVo.setRooms(buffer.toString());
        }else{
            expVo.setRooms(StringUtil.EMPTY);
        }

        expVo.setProjects(Lists.newArrayList());
        if(StringUtil.checkNotEmpty(pwEnter.getEprojects())){
            for (PwEnterDetail pro : pwEnter.getEprojects()) {
                expVo.getProjects().add(new PwEnterExpProject(pro.getProject()));
            }
        }
        return expVo;
    }

    public static List<PwEnterExpCompanyVo> converts(List<PwEnter> pwEnters) {
        if(StringUtil.checkEmpty(pwEnters)){
            return null;
        }

        List<PwEnterExpCompanyVo> expVos = Lists.newArrayList();
        for (PwEnter pwEnter : pwEnters) {
            expVos.add(PwEnterExpCompanyVo.convert(pwEnter));
        }
        return expVos;
    }
}

package com.oseasy.pro.modules.workflow.vo;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowProjectType;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.workflow.IWorkDaoety;
import com.oseasy.pro.modules.workflow.IWorkRes;
import com.oseasy.pro.modules.workflow.impl.WorkFetyPm;
import com.oseasy.util.common.utils.StringUtil;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

/**
 * 自定义流程：项目、大赛通用导出模板Entity.
 * @author zy
 * @version 2018-06-05
 */
@ExcelTarget("ExpProModelVo")
public class ExpProModelVo extends WorkFetyPm<ExpProModelVo> implements IWorkRes, IWorkDaoety, Serializable {

	private static final long serialVersionUID = 1L;

	@Excel(name = "项目名称", orderNum = "1",  width = 50, height = 15)
	private String pName;
	@Excel(name = "项目年份", orderNum = "2",  width = 10, height = 15)
	private String year;
	@Excel(name = "项目阶段", orderNum = "3",  width = 10, height = 15)
	private String stage;

    private String proType;
    private String type;

	@Excel(name="类别", orderNum = "4",  width = 30, height = 15)
	private String proCategoryName;        //项目类别：创新,创业//项目类别：project_type；大赛类别：competition_net_type

	private String proCategory;        //项目类别：创新,创业//项目类别：project_type；大赛类别：competition_net_type

	@Excel(name="级别/组别", orderNum = "5",  width = 10, height = 15)
	private String levelName;

	private String level;

	@Excel(name="项目简称", orderNum = "6",  width = 30, height = 15)
	private String shortName;
	@Excel(name="是否有附件", replace = { "是_1", "否_0"}, orderNum = "7", height = 15)
	private String hasfile;
	private String teamId;
	private String teamName;
	private String officeName;

	@Excel(name = "项目负责人", orderNum = "8",  width = 20, height = 15)
	private String teamLeaderss;
	@Excel(name = "项目其他成员信息", orderNum = "9",  width = 100, height = 15)
	private String teamOtherss;
	@Excel(name = "指导教师信息", orderNum = "10",  width = 100, height = 15)
    private String teacherVoss;

//	@ExcelCollection(name = "项目负责人", orderNum = "8")
	private List<TeamVo> teamLeader;
//	@ExcelCollection(name = "项目其他成员信息", orderNum = "9")
	private List<TeamVo> teamOthers;
//	@ExcelCollection(name = "指导教师信息", orderNum = "10")
	private List<TeacherVo> teacherVos;

    @Excel(name = "简介", orderNum = "10",  width = 100, height = 15)
    private String introduction;

    private String id;
    private String finalStatus;
    private String resultType;
    private String resultTypeName;
    private String resultContent;
    private String budget;
    private String innovation;
    private String planStep;
    private String planContent;
    private String planStartDate;
    private String planEndDate;

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFinalStatus() {
        return finalStatus;
    }

    public void setFinalStatus(String finalStatus) {
        this.finalStatus = finalStatus;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getResultTypeName() {
        return resultTypeName;
    }

    public void setResultTypeName(String resultTypeName) {
        this.resultTypeName = resultTypeName;
    }

    public String getResultContent() {
        return resultContent;
    }

    public void setResultContent(String resultContent) {
        this.resultContent = resultContent;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getInnovation() {
        return innovation;
    }

    public void setInnovation(String innovation) {
        this.innovation = innovation;
    }

    public String getPlanStep() {
        return planStep;
    }

    public void setPlanStep(String planStep) {
        this.planStep = planStep;
    }

    public String getPlanContent() {
        return planContent;
    }

    public void setPlanContent(String planContent) {
        this.planContent = planContent;
    }

    public String getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(String planStartDate) {
        this.planStartDate = planStartDate;
    }

    public String getPlanEndDate() {
        return planEndDate;
    }

    public void setPlanEndDate(String planEndDate) {
        this.planEndDate = planEndDate;
    }

    public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getStage() {
	    if(StringUtil.isEmpty(this.stage)){
            return "";
        }
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getProType() {
        return proType;
    }

    public void setProType(String proType) {
        this.proType = proType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProCategoryName() {
        if(StringUtil.isNotEmpty(this.proCategory) && StringUtil.isNotEmpty(this.proType)){
            if((FlowProjectType.PMT_XM.getKey()).equals(StringUtil.removeLastDotH(this.proType))){
                this.proCategoryName =  DictUtils.getDictLabel(this.proCategory, FlowProjectType.PMT_XM.getCategory().getKey(), "");
            }else if((FlowProjectType.PMT_DASAI.getKey()).equals(StringUtil.removeLastDotH(this.proType))){
//                this.proCategoryName = DictUtils.getDictLabel(this.proCategory, "gcontest_level", "");
              this.proCategoryName = DictUtils.getDictLabel(this.proCategory, FlowProjectType.PMT_DASAI.getCategory().getKey(), "");
            }
        }
        return proCategoryName;
    }

    public void setProCategoryName(String proCategoryName) {
        this.proCategoryName = proCategoryName;
    }

    public String getLevelName() {
        if(StringUtil.isNotEmpty(this.level) && StringUtil.isNotEmpty(this.proType)){
            if((FlowProjectType.PMT_XM.getKey()).equals(StringUtil.removeLastDotH(this.proType))){
                this.levelName = DictUtils.getDictLabel(this.level, FlowProjectType.PMT_XM.getLevel().getKey(), "");
            }else if((FlowProjectType.PMT_DASAI.getKey()).equals(StringUtil.removeLastDotH(this.proType))){
                this.levelName = DictUtils.getDictLabel(this.level, "gcontest_level", "");
//                this.levelName = DictUtils.getDictLabel(this.level, FlowProjectType.PMT_DASAI.getLevel().getKey(), "");
            }
        }
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getProCategory() {
		return proCategory;
	}

	public void setProCategory(String proCategory) {
		this.proCategory = proCategory;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getHasfile() {
		return hasfile;
	}

	public void setHasfile(String hasfile) {
		this.hasfile = hasfile;
	}

	public List<TeamVo> getTeamLeader() {
		return teamLeader;
	}

	public void setTeamLeader(List<TeamVo> teamLeader) {
		this.teamLeader = teamLeader;
	}

	public List<TeamVo> getTeamOthers() {
		return teamOthers;
	}

	public void setTeamOthers(List<TeamVo> teamOthers) {
		this.teamOthers = teamOthers;
	}

	public List<TeacherVo> getTeacherVos() {
		return teacherVos;
	}

	public void setTeacherVos(List<TeacherVo> teacherVos) {
		this.teacherVos = teacherVos;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getTeamLeaderss() {
	    if(StringUtil.isEmpty(this.teamLeaderss) && StringUtil.checkNotEmpty(this.teamLeader)){
//	        List<String> vos = this.teamLeader.stream().map(e -> e.getName() + e.getNo()).collect(Collectors.toList());
            List<String> vos = Lists.newArrayList();
            for (TeamVo vo : this.teamLeader) {
                if(vo == null){
                    continue;
                }
                String cur = vo.getName();
                if(StringUtil.isNotEmpty(vo.getNo())){
                    cur += StringUtil.PREF + vo.getNo();
                }
                if(!vos.contains(cur)){
                    vos.add(cur);
                }
            }
            if(!vos.isEmpty()){
                this.teamLeaderss = StringUtils.join(vos, StringUtil.POST);
            }
	    }
        return teamLeaderss;
    }

    public void setTeamLeaderss(String teamLeaderss) {
        this.teamLeaderss = teamLeaderss;
    }

    public String getTeamOtherss() {
        if(StringUtil.isEmpty(this.teamOtherss) && StringUtil.checkNotEmpty(this.teamOthers)){
//            List<String> vos = this.teamOthers.stream().map(e -> e.getName() + e.getNo()).collect(Collectors.toList());
            List<String> vos = Lists.newArrayList();
            for (TeamVo vo : this.teamOthers) {
                if(vo == null){
                    continue;
                }
                String cur = vo.getName() + StringUtil.PREF + vo.getNo();
                if(!vos.contains(cur)){
                    vos.add(cur);
                }
            }
            if(!vos.isEmpty()){
                this.teamOtherss = StringUtils.join(vos, StringUtil.POST);
            }
        }
        return teamOtherss;
    }

    public void setTeamOtherss(String teamOtherss) {
        this.teamOtherss = teamOtherss;
    }

    public String getTeacherVoss() {
        if(StringUtil.isEmpty(this.teacherVoss) && StringUtil.checkNotEmpty(this.teacherVos)){
//            List<String> vos = teacherVos.stream().map(e -> e.getName() + e.getNo()).collect(Collectors.toList());
            List<String> vos = Lists.newArrayList();
            for (TeacherVo vo : this.teacherVos) {
                if(vo == null){
                    continue;
                }
                String cur = vo.getName() + StringUtil.PREF + vo.getNo();
                if(!vos.contains(cur)){
                    vos.add(cur);
                }
            }
            if(!vos.isEmpty()){
                this.teacherVoss = StringUtils.join(vos, StringUtil.POST);
            }
        }
        return teacherVoss;
    }

    public void setTeacherVoss(String teacherVoss) {
        this.teacherVoss = teacherVoss;
    }

    /**
	 * ProModel转换成ProModelMdGcVo.
	 * @param pmodels
	 * @return List
	 */
	public static List<ExpProModelVo> converts(List<ProModel> pmodels){
	    List<ExpProModelVo> list = Lists.newArrayList();
	    if(StringUtil.checkEmpty(pmodels)){
            return list;
	    }
	    for (ProModel pmodel : pmodels) {
	        ExpProModelVo vo = new ExpProModelVo();
	        vo.setpName(pmodel.getPName());
	        vo.setYear(pmodel.getYear());
	        vo.setStage(pmodel.getStage());
	        vo.setProCategory(pmodel.getProCategory());
	        vo.setLevel(pmodel.getLevel());
	        vo.setShortName(pmodel.getShortName());
	        vo.setTeamId(pmodel.getTeamId());
            vo.setOfficeName(pmodel.getOfficeName());
//	        vo.setTeamLeader(Arrays.asList(new TeamVo[]{pmodel.getDeuser()}));
//          vo.setTeamOthers(pmodel.getStudentList());
//          vo.setTeacherVos(pmodel.getStudentList());
	        list.add(vo);
        }
	    return list;
	}
}
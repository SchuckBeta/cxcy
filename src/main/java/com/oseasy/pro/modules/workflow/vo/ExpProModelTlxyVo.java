package com.oseasy.pro.modules.workflow.vo;

import java.io.Serializable;
import java.util.Date;
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
 * 互联网+大赛模板Entity.
 * @author zy
 * @version 2018-06-05
 */
@ExcelTarget("proModelTlxy")
public class ExpProModelTlxyVo extends WorkFetyPm<ExpProModelTlxyVo> implements IWorkRes, IWorkDaoety, Serializable {
	private static final long serialVersionUID = 1L;

    @Excel(name="学院", orderNum = "1", height = 15)
    private String officeName;

    @Excel(name = "项目名称", orderNum = "2",  width = 50, height = 15)
    private String pName;
    @Excel(name = "项目编号", orderNum = "3",  width = 0, height = 15)
    private String number;

    @Excel(name="项目类型", orderNum = "4",  width = 30, height = 15)
    private String proCategoryName;        //项目类别：创新,创业//项目类别：project_type；大赛类别：competition_net_type

    @Excel(name = "负责人姓名", orderNum = "5",  width = 20, height = 15)
    private String teamLeaderss;

    @Excel(name = "学号", orderNum = "6",  width = 20, height = 15)
    private String no;
    @Excel(name = "团队ID", orderNum = "7",  width = 0, height = 15)
    private String teamId;
    @Excel(name = "团队名称", orderNum = "8",  width = 10, height = 15)
    private String teamName;
    @Excel(name = "联系电话", orderNum = "9",  width = 20, height = 15)
    private String mobile;

    @Excel(name = "电子邮箱", orderNum = "10",  width = 20, height = 15)
    private String email;

    @Excel(name = "专业", orderNum = "11",  width = 20, height = 15)
    private String profes;

    @Excel(name = "团队成员及学号", orderNum = "12",  width = 100, height = 15)
    private String teamOtherss;

    @Excel(name = "指导教师姓名", orderNum = "13",  width = 100, height = 15)
    private String teacherNames;
    @Excel(name = "指导教师工号", orderNum = "14",  width = 100, height = 15)
    private String teacherNos;
    @Excel(name = "职称", orderNum = "15",  width = 100, height = 15)
    private String teacherTecTitles;

	@Excel(name = "项目年份", orderNum = "16",  width = 10, height = 15)
	private String year;

	@Excel(name = "项目结果", orderNum = "17",  width = 10, height = 15)
	private String finalStatus;

    @Excel(name="是否有附件", replace = { "是_1", "否_0"}, orderNum = "18", height = 15)
    private String hasfile;

    @Excel(name = "项目简介", orderNum = "19",  width = 100, height = 15)
    private String introduction;

    @Excel(name="级别/组别", orderNum = "20",  width = 10, height = 15)
    private String levelName;

    @Excel(name="项目简称", orderNum = "21",  width = 30, height = 15)
    private String shortName;

	@Excel(name = "项目阶段", orderNum = "22",  width = 10, height = 15)
	private String stage;

	@Excel(name = "项目来源", orderNum = "23",  width = 10, height = 15)
	private String sourceName;
	private String source;
    @Excel(name = "成果形式", orderNum = "24",  width = 10, height = 15)
    private String resultTypeName;
    private String resultType;
	@Excel(name = "成果说明", orderNum = "25",  width = 10, height = 15)
	private String resultContent;
    @Excel(name = "项目经费预算", orderNum = "26",  width = 10, height = 15)
    private String budgetDollar;
	@Excel(name = "经费预算明细", orderNum = "27",  width = 10, height = 15)
	private String budget;
	@Excel(name = "前期调研准备", orderNum = "28",  width = 10, height = 15)
	private String innovation;
	@Excel(name = "保障措施 ", orderNum = "29",  width = 10, height = 15)
	private String planStep;
	@Excel(name = "实施预案", orderNum = "30",  width = 10, height = 15)
	private String planContent;
	@Excel(name = "项目预案时间起始", orderNum = "31",  width = 10, height = 15, databaseFormat = "yyyy-MM-dd HH:mm:ss", format = "yyyy-MM-dd HH:mm:ss")
	private Date planStartDate;
	@Excel(name = "项目预案时间截止", orderNum = "32",  width = 10, height = 15, databaseFormat = "yyyy-MM-dd HH:mm:ss", format = "yyyy-MM-dd HH:mm:ss")
	private Date planEndDate;


    private String level;
    private String proType;
    private String type;
	private String proCategory;        //项目类别：创新,创业//项目类别：project_type；大赛类别：competition_net_type

//	@ExcelCollection(name = "指导教师信息", orderNum = "8")
	private String teacherVoss;

//	@ExcelCollection(name = "项目负责人", orderNum = "8")
	private List<TeamVo> teamLeader;
//	@ExcelCollection(name = "项目其他成员信息", orderNum = "9")
	private List<TeamVo> teamOthers;
//	@ExcelCollection(name = "指导教师信息", orderNum = "10")
	private List<TeacherVo> teacherVos;

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
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

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getResultTypeName() {
        return resultTypeName;
    }

    public void setResultTypeName(String resultTypeName) {
        this.resultTypeName = resultTypeName;
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
        if(StringUtil.isEmpty(this.levelName) && StringUtil.isNotEmpty(this.level) && StringUtil.isNotEmpty(this.proType)){
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
//                String cur = vo.getName() + StringUtil.PREF + vo.getNo();
//                String cur = vo.getName() + StringUtil.PREF;
                if(!vos.contains(vo.getName())){
                    vos.add(vo.getName());
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
	public static List<ExpProModelTlxyVo> converts(List<ProModel> pmodels){
	    List<ExpProModelTlxyVo> list = Lists.newArrayList();
	    if(StringUtil.checkEmpty(pmodels)){
            return list;
	    }
	    for (ProModel pmodel : pmodels) {
	        ExpProModelTlxyVo vo = new ExpProModelTlxyVo();
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


    public String getNo() {
        if(StringUtil.isEmpty(this.no) && ((this.teamLeader != null) && (teamLeader.size() == 1)) && StringUtil.isNotEmpty(this.teamLeader.get(0).getNo())){
            this.no = this.teamLeader.get(0).getNo();
        }
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getMobile() {
        if(StringUtil.isEmpty(this.mobile) && ((this.teamLeader != null) && (teamLeader.size() == 1)) && StringUtil.isNotEmpty(this.teamLeader.get(0).getMobile())){
            this.mobile = this.teamLeader.get(0).getMobile();
        }
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        if(StringUtil.isEmpty(this.email) && ((this.teamLeader != null) && (teamLeader.size() == 1)) && StringUtil.isNotEmpty(this.teamLeader.get(0).getEmail())){
            this.email = this.teamLeader.get(0).getEmail();
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfes() {
        if(StringUtil.isEmpty(this.profes) && ((this.teamLeader != null) && (teamLeader.size() == 1)) && StringUtil.isNotEmpty(this.teamLeader.get(0).getProfes())){
            this.profes = this.teamLeader.get(0).getProfes();
        }
        return profes;
    }

    public void setProfes(String profes) {
        this.profes = profes;
    }

    public String getTeacherNames() {
        if(StringUtil.isEmpty(this.teacherNames) && StringUtil.checkNotEmpty(this.teacherVos)){
          List<String> vos = Lists.newArrayList();
          for (TeacherVo vo : this.teacherVos) {
              if(vo == null){
                  continue;
              }
              if(!vos.contains(vo.getName())){
                  vos.add(vo.getName());
              }
          }
          if(!vos.isEmpty()){
              this.teacherNames = StringUtils.join(vos, StringUtil.Z_DUN);
          }
        }
        return teacherNames;
    }

    public void setTeacherNames(String teacherNames) {
        this.teacherNames = teacherNames;
    }

    public String getTeacherNos() {
        if(StringUtil.isEmpty(this.teacherNos) && StringUtil.checkNotEmpty(this.teacherVos)){
            List<String> vos = Lists.newArrayList();
            for (TeacherVo vo : this.teacherVos) {
                if(vo == null){
                    continue;
                }
                if(!vos.contains(vo.getNo())){
                    vos.add(vo.getNo());
                }
            }
            if(!vos.isEmpty()){
                this.teacherNos = StringUtils.join(vos, StringUtil.Z_DUN);
            }
          }
        return teacherNos;
    }

    public void setTeacherNos(String teacherNos) {
        this.teacherNos = teacherNos;
    }

    public String getTeacherTecTitles() {
        if(StringUtil.isEmpty(this.teacherTecTitles) && StringUtil.checkNotEmpty(this.teacherVos)){
            List<String> vos = Lists.newArrayList();
            for (TeacherVo vo : this.teacherVos) {
                if(vo == null){
                    continue;
                }
                if(!vos.contains(vo.getZhicheng())){
                    vos.add(vo.getZhicheng());
                }
            }
            if(!vos.isEmpty()){
                this.teacherTecTitles = StringUtils.join(vos, StringUtil.Z_DUN);
            }
          }
        return teacherTecTitles;
    }

    public void setTeacherTecTitles(String teacherTecTitles) {
        this.teacherTecTitles = teacherTecTitles;
    }

    public String getFinalStatus() {
        return finalStatus;
    }

    public void setFinalStatus(String finalStatus) {
        this.finalStatus = finalStatus;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getResultContent() {
        return resultContent;
    }

    public void setResultContent(String resultContent) {
        this.resultContent = resultContent;
    }

    public String getBudgetDollar() {
        return budgetDollar;
    }

    public void setBudgetDollar(String budgetDollar) {
        this.budgetDollar = budgetDollar;
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

    public Date getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(Date planStartDate) {
        this.planStartDate = planStartDate;
    }

    public Date getPlanEndDate() {
        return planEndDate;
    }

    public void setPlanEndDate(Date planEndDate) {
        this.planEndDate = planEndDate;
    }
}
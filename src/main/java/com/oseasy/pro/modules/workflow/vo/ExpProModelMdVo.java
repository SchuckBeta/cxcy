package com.oseasy.pro.modules.workflow.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowProjectType;
import com.oseasy.com.pcore.modules.sys.entity.Area;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.OfficeUtils;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.util.common.utils.StringUtil;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

/**
 * 自定义流程：.
 * @author zy
 * @version 2018-06-05
 */
@ExcelTarget("ExpProModelMdVo")
public class ExpProModelMdVo implements Serializable {

	/**
     * .
     */
    private static final String DICT_SCHOOL_NAME = "0000000170";

    private static final long serialVersionUID = 1L;

    @Excel(name = "立项年份", orderNum = "0",  width = 10, height = 15)
    private String year;
    @Excel(name = "省市", orderNum = "1",  width = 10, height = 15)
    private String officeArea;
    private String officeAname;
    @Excel(name = "高校代码", orderNum = "1",  width = 10, height = 15)
    private String officeNo;
    @Excel(name = "机构名称", orderNum = "2",  width = 10, height = 15)
    private String officeName;
    @Excel(name = "高校名称", orderNum = "2",  width = 10, height = 15)
    private String scName;
	@Excel(name = "项目编号", orderNum = "3",  width = 50, height = 15)
	private String pno;
	@Excel(name = "项目名称", orderNum = "4",  width = 50, height = 15)
	private String pname;
    @Excel(name = "项目类型", orderNum = "5",  width = 30, height = 15)
    private String proCategoryName;        //项目类别：创新,创业//项目类别：project_type；大赛类别：competition_net_type

    @Excel(name = "项目负责人", orderNum = "990",  width = 20, height = 15)
    private String teamLeaderss;
    @Excel(name = "项目负责人名称", orderNum = "6",  width = 20, height = 15)
    private String teamLeaderName;
    @Excel(name = "项目负责人学号", orderNum = "7",  width = 20, height = 15)
    private String teamLeaderNo;
    @Excel(name = "项目负责人手机号", orderNum = "8",  width = 20, height = 15)
    private String teamLeaderMobile;

    @Excel(name = "项目其他成员信息", orderNum = "9",  width = 100, height = 15)
    private String teamOtherss;

    @Excel(name = "指导教师信息", orderNum = "991",  width = 100, height = 15)
    private String teacherVoss;
    @Excel(name = "指导教师姓名", orderNum = "10",  width = 20, height = 15)
    private String teacherNames;
    @Excel(name = "指导教师工号", orderNum = "11",  width = 20, height = 15)
    private String teacherNos;
    @Excel(name = "指导教师职称", orderNum = "12",  width = 20, height = 15)
    private String teacherZcs;
    @Excel(name = "指导教师手机号", orderNum = "992",  width = 20, height = 15)
    private String teacherMobiles;

    @Excel(name = "财政拨款(元)", orderNum = "13",  width = 20, height = 15)
    private String czAmount;
    @Excel(name = "校拨(元)", orderNum = "14",  width = 20, height = 15)
    private String xxAmount;
    @Excel(name = "总经费(元)", orderNum = "15",  width = 20, height = 15)
    private String totalAmount;

    @Excel(name = "学科代码", orderNum = "16",  width = 20, height = 15)
    private String course;
    @Excel(name = "项目简介(200字以内)", orderNum = "17",  width = 100, height = 15)
    private String introduction;
    @Excel(name = "省级编号", orderNum = "18",  width = 20, height = 15)
    private String sjNo;
    @Excel(name = "校级编号", orderNum = "19",  width = 20, height = 15)
    private String xxNo;

	@Excel(name = "项目阶段", orderNum = "994",  width = 10, height = 15)
	private String stage;
    private String proType;
    private String type;

	private String proCategory;        //项目类别：创新,创业//项目类别：project_type；大赛类别：competition_net_type

	@Excel(name="级别/组别", orderNum = "995",  width = 10, height = 15)
	private String levelName;

	private String level;

	@Excel(name="项目简称", orderNum = "996",  width = 30, height = 15)
	private String shortName;
	@Excel(name="是否有附件", replace = { "是_1", "否_0"}, orderNum = "997", height = 15)
	private String hasfile;
	private String teamId;
	private String teamName;
	private String id;


//	@ExcelCollection(name = "项目负责人", orderNum = "8")
	private List<TeamVo> teamLeader;
//	@ExcelCollection(name = "项目其他成员信息", orderNum = "9")
	private List<TeamVo> teamOthers;
//	@ExcelCollection(name = "指导教师信息", orderNum = "10")
	private List<TeacherVo> teacherVos;

    public void setOfficeArea(String officeArea) {
        this.officeArea = officeArea;
    }

    public String getOfficeArea() {
        if(OfficeUtils.getTop() != null){
            this.officeArea = OfficeUtils.getTop().getCityCode();
        }
        return officeArea;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public void setOfficeNo(String officeNo) {
        this.officeNo = officeNo;
    }

    public void setPno(String pno) {
        this.pno = pno;
    }

    public void setTeacherZcs(String teacherZcs) {
        this.teacherZcs = teacherZcs;
    }

    public void setCzAmount(String czAmount) {
        this.czAmount = czAmount;
    }

    public void setXxAmount(String xxAmount) {
        this.xxAmount = xxAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setSjNo(String sjNo) {
        this.sjNo = sjNo;
    }

    public void setXxNo(String xxNo) {
        this.xxNo = xxNo;
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

    public String getOfficeNo() {
        if(OfficeUtils.getTop() != null){
            this.officeNo = OfficeUtils.getTop().getSchoolCode();
        }
        return officeNo;
    }

    public String getPno() {
        return pno;
    }

    public String getTeacherZcs() {
        if(StringUtil.isEmpty(this.teacherZcs) && StringUtil.checkNotEmpty(this.teacherVos)){
          List<String> voNames = Lists.newArrayList();
          Map<String, List<String>> vos = Maps.newHashMap();
          for (TeacherVo vo : this.teacherVos) {
              if(vo == null){
                  continue;
              }
              String curName = vo.getName();
              String cur = vo.getZhicheng();
              if(vos.containsKey(curName)){
                  vos.get(curName).add(cur);
              }else{
                  vos.put(curName, Lists.newArrayList());
                  vos.get(curName).add(cur);
              }
              voNames.add(curName);
          }
          if(!voNames.isEmpty()){
              this.teacherZcs = "";
              for (int i= 0; i < voNames.size(); i++) {
                  if(i != 0){
                      this.teacherZcs += StringUtil.DOTH;
                  }
                  if(vos.containsKey(voNames.get(i))){
                      this.teacherZcs += StringUtils.join(vos.get(voNames.get(i)), StringUtil.LINE);
                  }
              }
          }
      }
      return teacherZcs;
    }

    public String getCzAmount() {
        return czAmount;
    }

    public String getXxAmount() {
        return xxAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getSjNo() {
        return sjNo;
    }

    public String getXxNo() {
        return xxNo;
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

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getProCategoryName() {
        if(StringUtil.isNotEmpty(this.proCategory) && StringUtil.isNotEmpty(this.proType)){
            if((FlowProjectType.PMT_XM.getKey()).equals(StringUtil.removeLastDotH(this.proType))){
                this.proCategoryName =  DictUtils.getDictLabel(this.proCategory, FlowProjectType.PMT_XM.getCategory().getKey(), "");
            }else if((FlowProjectType.PMT_DASAI.getKey()).equals(StringUtil.removeLastDotH(this.proType))){
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
//                this.levelName = DictUtils.getDictLabel(this.level, "gcontest_level", "");
                this.levelName = DictUtils.getDictLabel(this.level, FlowProjectType.PMT_DASAI.getLevel().getKey(), "");
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

	public String getOfficeAname() {
	    if(StringUtil.isEmpty(this.officeArea) && (OfficeUtils.getTop() != null)){
            this.officeArea = OfficeUtils.getTop().getCityCode();
	    }
        if(StringUtil.isEmpty(this.officeAname)){
            for (Area area : CoreUtils.getAreaList()) {
                if((this.officeArea).equals(area.getCode())){
                    this.officeAname =  area.getName();
                }
            }
        }
        if(StringUtil.isEmpty(this.officeAname)){
            this.officeAname =  "";
        }
        return officeAname;
    }

    public void setOfficeAname(String officeAname) {
        this.officeAname = officeAname;
    }

    public void setScName(String scName) {
        this.scName = scName;
    }

    public String getScName() {
	    if(StringUtil.isEmpty(this.scName) && StringUtil.isNotEmpty(this.officeNo)){
	        for (Dict dict : DictUtils.getDictListByType(DICT_SCHOOL_NAME)) {
                if((this.officeNo).equals(dict.getValue())){
                    this.scName =  dict.getLabel();
                }
            }
	    }
	    if(StringUtil.isEmpty(this.scName)){
            this.scName =  "";
	    }
	    return scName;
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
                    cur += StringUtil.LINE + vo.getNo();
                }
                if(!vos.contains(cur)){
                    vos.add(cur);
                }
            }
            if(!vos.isEmpty()){
                this.teamLeaderss = StringUtils.join(vos, StringUtil.DOTH);
            }
	    }
        return teamLeaderss;
    }
	public String getTeamLeaderName() {
	    if(StringUtil.isEmpty(this.teamLeaderName) && StringUtil.checkNotEmpty(this.teamLeader)){
	        List<String> vos = Lists.newArrayList();
	        for (TeamVo vo : this.teamLeader) {
	            if(vo == null){
	                continue;
	            }
	            String cur = vo.getName();
	            if(!vos.contains(cur)){
	                vos.add(cur);
	            }
	        }
	        if(!vos.isEmpty()){
	            this.teamLeaderName = StringUtils.join(vos, StringUtil.DOTH);
	        }
	    }
	    return teamLeaderName;
	}

	public String getTeamLeaderNo() {
	    if(StringUtil.isEmpty(this.teamLeaderNo) && StringUtil.checkNotEmpty(this.teamLeader)){
	        List<String> vos = Lists.newArrayList();
	        for (TeamVo vo : this.teamLeader) {
	            if(vo == null){
	                continue;
	            }
	            String cur = vo.getNo();
	            if(!vos.contains(cur)){
	                vos.add(cur);
	            }
	        }
	        if(!vos.isEmpty()){
	            this.teamLeaderNo = StringUtils.join(vos, StringUtil.DOTH);
	        }
	    }
	    return teamLeaderNo;
	}

	public String getTeamLeaderMobile() {
	    if(StringUtil.isEmpty(this.teamLeaderMobile) && StringUtil.checkNotEmpty(this.teamLeader)){
	        List<String> vos = Lists.newArrayList();
	        for (TeamVo vo : this.teamLeader) {
	            if(vo == null){
	                continue;
	            }
	            String cur = vo.getMobile();
	            if(!vos.contains(cur)){
	                vos.add(cur);
	            }
	        }
	        if(!vos.isEmpty()){
	            this.teamLeaderMobile = StringUtils.join(vos, StringUtil.DOTH);
	        }
	    }
	    return teamLeaderMobile;
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
                String cur = vo.getName() + StringUtil.LINE + vo.getNo();
                if(!vos.contains(cur)){
                    vos.add(cur);
                }
            }
            if(!vos.isEmpty()){
                this.teamOtherss = StringUtils.join(vos, StringUtil.DOTH);
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
                String cur = vo.getName() + StringUtil.LINE + vo.getNo();
                if(!vos.contains(cur)){
                    vos.add(cur);
                }
            }
            if(!vos.isEmpty()){
                this.teacherVoss = StringUtils.join(vos, StringUtil.DOTH);
            }
        }
        return teacherVoss;
    }

    public String getTeacherNames() {
        if(StringUtil.isEmpty(this.teacherNames) && StringUtil.checkNotEmpty(this.teacherVos)){
//            List<String> vos = teacherVos.stream().map(e -> e.getName() + e.getNo()).collect(Collectors.toList());
            List<String> vos = Lists.newArrayList();
            for (TeacherVo vo : this.teacherVos) {
                if(vo == null){
                    continue;
                }
                String cur = vo.getName();
                if(!vos.contains(cur)){
                    vos.add(cur);
                }
            }
            if(!vos.isEmpty()){
                this.teacherNames = StringUtils.join(vos, StringUtil.DOTH);
            }
        }
        return teacherNames;
    }

    public String getTeacherNos() {
        if(StringUtil.isEmpty(this.teacherNos) && StringUtil.checkNotEmpty(this.teacherVos)){
//            List<String> vos = teacherVos.stream().map(e -> e.getName() + e.getNo()).collect(Collectors.toList());
            List<String> vos = Lists.newArrayList();
            for (TeacherVo vo : this.teacherVos) {
                if(vo == null){
                    continue;
                }
                String cur = vo.getNo();
                if(!vos.contains(cur)){
                    vos.add(cur);
                }
            }
            if(!vos.isEmpty()){
                this.teacherNos = StringUtils.join(vos, StringUtil.DOTH);
            }
        }
        return teacherNos;
    }

    public String getTeacherMobiles() {
        if(StringUtil.isEmpty(this.teacherMobiles) && StringUtil.checkNotEmpty(this.teacherVos)){
//            List<String> vos = teacherVos.stream().map(e -> e.getName() + e.getNo()).collect(Collectors.toList());
            List<String> vos = Lists.newArrayList();
            for (TeacherVo vo : this.teacherVos) {
                if(vo == null){
                    continue;
                }
                String cur = vo.getMobile();
                if(!vos.contains(cur)){
                    vos.add(cur);
                }
            }
            if(!vos.isEmpty()){
                this.teacherMobiles = StringUtils.join(vos, StringUtil.LINE);
            }
        }
        return teacherMobiles;
    }

    public void setTeacherVoss(String teacherVoss) {
        this.teacherVoss = teacherVoss;
    }

    public void setTeamLeaderName(String teamLeaderName) {
        this.teamLeaderName = teamLeaderName;
    }

    public void setTeamLeaderNo(String teamLeaderNo) {
        this.teamLeaderNo = teamLeaderNo;
    }

    public void setTeamLeaderMobile(String teamLeaderMobile) {
        this.teamLeaderMobile = teamLeaderMobile;
    }

    public void setTeacherNames(String teacherNames) {
        this.teacherNames = teacherNames;
    }

    public void setTeacherNos(String teacherNos) {
        this.teacherNos = teacherNos;
    }

    public void setTeacherMobiles(String teacherMobiles) {
        this.teacherMobiles = teacherMobiles;
    }

    /**
	 * ProModel转换成ProModelMdGcVo.
	 * @param pmodels
	 * @return List
	 */
	public static List<ExpProModelMdVo> converts(List<ProModel> pmodels){
	    List<ExpProModelMdVo> list = Lists.newArrayList();
	    if(StringUtil.checkEmpty(pmodels)){
            return list;
	    }
	    for (ProModel pmodel : pmodels) {
	        ExpProModelMdVo vo = new ExpProModelMdVo();
	        vo.setPname(pmodel.getPName());
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
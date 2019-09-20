package com.oseasy.pro.modules.workflow.entity;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.pro.modules.project.entity.ProjectPlan;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.workflow.IWorkDaoety;
import com.oseasy.pro.modules.workflow.IWorkRes;
import com.oseasy.pro.modules.workflow.impl.WorkFetyPm;
import com.oseasy.util.common.utils.IidEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/*
 * 华师大创模板Entity.
 * @author zy
 * @version 2018-06-05
 */
public class ProvinceProModel extends WorkFetyPm<ProvinceProModel> implements IWorkRes, IWorkDaoety, IidEntity {

	private static final long serialVersionUID = 1L;
	private ProModel proModel;		// model_id
	private String modelId;		// model_id
	private String procInsId;        // 流程实例id
	private String finalResult;  //项目结果
	private String queryStr;  //模糊查询字段
	private String state;      //状态是否结束 1是结束 0是未结束
	private String competitionNumber;        // 编号
	private String year; //项目年份
	private String schoolTenantId;//来源租户
	private ActYw actYw;        // 最终结果节点id
	private String endGnodeId;        // 最终结果节点id
	private List<String> proCategoryList;
	private List<String> projectLevelList;

	private List<String> officeIdList ;
	/**
	 * 省平台项目状态
	 */
	private Integer sendState;
	/**
	 * 下发资金
	 */
	private BigDecimal capital;

	private String gnodeIdList;

	private String actYwId;

	private String gnodeId;

	private String officeId;		//查询条件 学院id

	private String schoolName;
	/**
	 * 学校性质
	 */
	private String schoolType;
	/**
	 * 所属领域
	 */
	private String belongsField;

	/**
	 * 参赛赛道:1.高教主赛道2.青年红色筑梦之旅3.职教赛道
	 */
	private String gcTrack;
	/**
	 * 城市
	 */
	private String schoolCity;
	/**
	 * 【创新训练 ，创业训练，创业实践】或大赛级别'
	 */
	private String proCategory;


	private String finalStatus;

	private String step;

	/**
	 * 地区
	 */
	private String  areaCode;

	private String level;

	private String beginDate;

	private String endDate;

	private String ideaStage;
	/**
	 * 角色标记
	 */
	private String roleFlag;

	/**
	 * 专家Id
	 */
	private String professor;

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	@Override
	public String getActYwId() {
		return actYwId;
	}

	@Override
	public void setActYwId(String actYwId) {
		this.actYwId = actYwId;
	}

	public String getGnodeId() {
		return gnodeId;
	}

	public void setGnodeId(String gnodeId) {
		this.gnodeId = gnodeId;
	}

	public HashMap<String,Object> getVars() {
		HashMap<String,Object> vars=new HashMap<String, Object>();
		vars.put("number",competitionNumber);  //编号
		vars.put("id",id);  //id
		vars.put("actYwId",actYwId);             //项目评级
		return vars;
	}

	public ActYw getActYw() {
		return actYw;
	}

	public void setActYw(ActYw actYw) {
		this.actYw = actYw;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public ProvinceProModel(ProModel proModel) {
		super();
		this.proModel = proModel;
	}

	public ProvinceProModel() {

	}

	public String getEndGnodeId() {
		return endGnodeId;
	}

	public void setEndGnodeId(String endGnodeId) {
		this.endGnodeId = endGnodeId;
	}

	public String getCompetitionNumber() {
		return competitionNumber;
	}

	public void setCompetitionNumber(String competitionNumber) {
		this.competitionNumber = competitionNumber;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	@Override
	public String getProcInsId() {
		return procInsId;
	}

	@Override
	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}

	public String getFinalResult() {
		return finalResult;
	}

	public void setFinalResult(String finalResult) {
		this.finalResult = finalResult;
	}

	public String getQueryStr() {
		return queryStr;
	}

	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public ProModel getProModel() {
		return proModel;
	}

	@Override
	public void setProModel(ProModel proModel) {
		this.proModel = proModel;
	}

	@Override
	public String getModelId() {
		return modelId;
	}

	@Override
	public void setModelId(String modelId) {
		this.modelId = modelId;
	}



	public List<String> getProCategoryList() {
		return proCategoryList;
	}

	public void setProCategoryList(List<String> proCategoryList) {
		this.proCategoryList = proCategoryList;
	}

	public List<String> getProjectLevelList() {
		return projectLevelList;
	}

	public void setProjectLevelList(List<String> projectLevelList) {
		this.projectLevelList = projectLevelList;
	}

	public List<String> getOfficeIdList() {
		return officeIdList;
	}

	public void setOfficeIdList(List<String> officeIdList) {
		this.officeIdList = officeIdList;
	}

	/* (non-Javadoc)
		 * @see com.oseasy.pro.modules.workflow.IWorkRes#getOfficeName()
		 */
    @Override
    public String getOfficeName() {
        if(this.getProModel() == null){
            return "";
        }
        return this.getProModel().getOfficeName();
    }

	public String getSchoolTenantId() {
		return schoolTenantId;
	}

	public void setSchoolTenantId(String schoolTenantId) {
		this.schoolTenantId = schoolTenantId;
	}

	public Integer getSendState() {
		return sendState;
	}

	public void setSendState(Integer sendState) {
		this.sendState = sendState;
	}

	public BigDecimal getCapital() {
		return capital;
	}

	public void setCapital(BigDecimal capital) {
		this.capital = capital;
	}

	public String getGnodeIdList() {
		return gnodeIdList;
	}

	public void setGnodeIdList(String gnodeIdList) {
		this.gnodeIdList = gnodeIdList;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getSchoolType() {
		return schoolType;
	}

	public void setSchoolType(String schoolType) {
		this.schoolType = schoolType;
	}

	public String getBelongsField() {
		return belongsField;
	}

	public void setBelongsField(String belongsField) {
		this.belongsField = belongsField;
	}

	public String getGcTrack() {
		return gcTrack;
	}

	public void setGcTrack(String gcTrack) {
		this.gcTrack = gcTrack;
	}

	public String getSchoolCity() {
		return schoolCity;
	}

	public void setSchoolCity(String schoolCity) {
		this.schoolCity = schoolCity;
	}

	public String getProCategory() {
		return proCategory;
	}

	public void setProCategory(String proCategory) {
		this.proCategory = proCategory;
	}

	public String getFinalStatus() {
		return finalStatus;
	}

	public void setFinalStatus(String finalStatus) {
		this.finalStatus = finalStatus;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getIdeaStage() {
		return ideaStage;
	}

	public void setIdeaStage(String ideaStage) {
		this.ideaStage = ideaStage;
	}

	public String getRoleFlag() {
		return roleFlag;
	}

	public void setRoleFlag(String roleFlag) {
		this.roleFlag = roleFlag;
	}

	public String getProfessor() {
		return professor;
	}

	public void setProfessor(String professor) {
		this.professor = professor;
	}
}
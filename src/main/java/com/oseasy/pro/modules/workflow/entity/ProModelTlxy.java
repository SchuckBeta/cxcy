package com.oseasy.pro.modules.workflow.entity;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.oseasy.pro.modules.project.entity.ProjectPlan;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.workflow.IWorkDaoety;
import com.oseasy.pro.modules.workflow.IWorkRes;
import com.oseasy.pro.modules.workflow.impl.WorkFetyPm;

/**
 * 互联网+大赛模板Entity.
 * @author zy
 * @version 2018-06-05
 */
public class ProModelTlxy extends WorkFetyPm<ProModelTlxy> implements IWorkRes, IWorkDaoety{

	private static final long serialVersionUID = 1L;
	private ProModel proModel;		// model_id
	private String modelId;		// model_id
	private String xCompetitionNumber;		// 校赛编号
	private String gCompetitionNumber;		// 国赛编号
	private String pCompetitionNumber;		// 省赛编号
	private String  source;            //项目来源
	private String  resultType;  //成果形式
	private String  innovation;  //前期调研准备
	private String  resultContent;  //成果说明
	private String   budgetDollar;  //项目经费预算
	private String   budget;  //经费预算明细
	private Date planStartDate;		// 项目预案时间起始
	private Date planEndDate;		// 项目预案时间截止
	private String   planContent;  //实施预案
	private String   planStep;  //保障措施

	private List<ProjectPlan> planList;

	private List<String> proCategoryList;

	private List<String> projectLevelList;

	private List<String> officeIdList ;

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

	public String getxCompetitionNumber() {
		return xCompetitionNumber;
	}

	public void setxCompetitionNumber(String xCompetitionNumber) {
		this.xCompetitionNumber = xCompetitionNumber;
	}

	public String getgCompetitionNumber() {
		return gCompetitionNumber;
	}

	public void setgCompetitionNumber(String gCompetitionNumber) {
		this.gCompetitionNumber = gCompetitionNumber;
	}

	public String getpCompetitionNumber() {
		return pCompetitionNumber;
	}

	public void setpCompetitionNumber(String pCompetitionNumber) {
		this.pCompetitionNumber = pCompetitionNumber;
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

	public String getPlanContent() {
		return planContent;
	}

	public void setPlanContent(String planContent) {
		this.planContent = planContent;
	}

	public String getPlanStep() {
		return planStep;
	}

	public void setPlanStep(String planStep) {
		this.planStep = planStep;
	}

	public List<ProjectPlan> getPlanList() {
		return planList;
	}

	public void setPlanList(List<ProjectPlan> planList) {
		this.planList = planList;
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}


	public ProModelTlxy() {
		super();
	}

	public ProModelTlxy(ProModel proModel) {
        super();
        this.proModel = proModel;
    }
	public ProModelTlxy(String id, ProModel proModel) {
	    super(id);
	    this.proModel = proModel;
	}

    public ProModelTlxy(String id){
		super(id);
	}

	public ProModelTlxy(List<String> ids) {
        super(ids);
    }

    public ProModelTlxy(List<String> ids, String actYwId) {
        super(ids, actYwId);
    }

	public ProModel getProModel() {
		return proModel;
	}

	public void setProModel(ProModel proModel) {
		this.proModel = proModel;
	}

	@Length(min=0, max=64, message="model_id长度必须介于 0 和 64 之间")
	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
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
}
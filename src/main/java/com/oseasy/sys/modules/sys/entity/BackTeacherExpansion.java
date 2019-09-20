package com.oseasy.sys.modules.sys.entity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.entity.TeamUserRelation;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 导师信息表Entity
 * @author l
 * @version 2017-03-31
 */
public class BackTeacherExpansion extends DataEntity<BackTeacherExpansion> {
	public static String isTeacher ="0";
	public static String isExpert ="1";

	private static final long serialVersionUID = 1L;
	private User user;		// 人员基本信息ID
	private String category;//导师类型
	private String workUnitType;//工作单位类别
	private String arrangement;		// 层次
	private Integer discipline;		// 学科门类
	private String industry;		// 行业
	private String technicalTitle;		// 职称
	private String serviceIntentionStr;		// 服务意向列表显示
	private String serviceIntention;		// 服务意向
	private List<String> serviceIntentionIds;//服务意向ids list
	private String workUnit;		// 工作单位
	private String address;		// 联系地址
	private String resume;		// 工作简历
	private String recommendedUnits;		// 推荐单位
	private String result;		// 成果名称
	private String award;		// 获奖名称
	private Integer level;		// 级别
	private String reviewName;		// 评审项目名称
	private String joinReviewTime;		// 参与评审年份
	private String firstBank;		// 开户银行
	private String bankAccount;		// 银行账号
	private String teachertype;		// 导师来源 1：校园导师  2：企业导师
	private String expertType;	   //专家来源 字典表 type为 expert_source

	private List<String> expertTypeList;	   //专家来源 字典表 type为 expert_source
	private String isOpen="1";		// 是否公开  0:不公开 1:公开
	private String educationType;  //学历类别
 	private Team  team;
 	private String currentId;  //当前用户Id
 	private TeamUserRelation teamUserRelation;
 	private OaNotify oaNotify;
 	private String msg;
 	private Date awardTime;//获奖年份
 	private String awardLevel;//获奖级别
 	private String isFront;//是否是前台查询 0或空：否 1：是
 	private String mainExp;//主要经历
	private String postTitle;//职务

	private String roleType;//'角色类型 0导师 1专家 0,1导师和专家'

	private List<String> roleIdList;  //添加专家角色

	private String nowProject;   //是否在研项目  1是 0否
	private String topPrise; // 最高奖项

	private String firstShow;   //是否首页显示  1是专家显示  0是导师显示 -1 或者 null 是不显示
	private String siteShow; // 是否栏目显示 1是专家显示  0是导师显示 -1 或者 null 是不显示
	private String topShow; // 是否栏目显示 1是专家显示 0是导师显示 -1 或者 null 是不显示
	private List<String> keywords; //关键字
	private String queryStr; //后台关键字
	private List<String> proNameList;//当前审核项目
	private Boolean canInvite;//用于页面显示是否可被邀请
	private String canInviteTeamIds;//用于列表页面显示可被邀请进入的团队id
	private String teamLeaderId;//用于查询条件，团队负责人id

	private String curJoin;   		// 当前在研，显示

	private String isLike;  		//是否被当前用户点赞 1：是 0：否
	private List<Map<String,String>> curJoinParam;//当前在研查询条件
	private List<String> curJoinStr;//当前在研查询条件,接收页面传值

	private List<String> userIdList;//页面接受参数userIdList

	private String expertId;//expertId专家 查询条件,

	private String officeId;//officeId 查询条件,

	private String roleId;

    public List<Map<String, String>> getCurJoinParam() {
		if (curJoinParam!=null) {
			return curJoinParam;
		}
		if (curJoinStr==null||curJoinStr.size()==0) {
			return null;
		}
		List<Map<String,String>> l=new ArrayList<Map<String,String>>();
		for(String s:curJoinStr) {
			if (StringUtil.isNotEmpty(s)) {
				Map<String,String> map=new HashMap<String,String>();
				String[] ss=s.split("-");
				map.put("pType", ss[0]);
				map.put("psType", ss[1]);
				l.add(map);
			}
		}
		if (l.size()==0) {
			return null;
		}
		return l;
	}

//	public List<String> getExpertTypeList() {
//		return expertTypeList;
//	}

//	public void setExpertTypeList(List<String> expertTypeList) {
//		this.expertTypeList = expertTypeList;
//	}


	public List<String> getExpertTypeList() {
		if (StringUtils.isNotBlank(expertType)) {
			String[] domainArray = StringUtils.split(expertType, StringUtil.DOTH);
			expertTypeList = Lists.newArrayList();
			for (String id : domainArray) {
				expertTypeList.add(id);
			}
		}
		return expertTypeList;
	}

	public void setExpertTypeList(List<String> expertTypeList) {
		// this.domainIdList = domainIdList;
		// domainIdList = Lists.newArrayList();
		if (expertTypeList != null && expertTypeList.size() > 0) {
			StringBuffer strbuff = new StringBuffer();
			for (String dictValue : expertTypeList) {
				strbuff.append(dictValue);
				strbuff.append(StringUtil.DOTH);
			}
			String expertTypes = strbuff.substring(0, strbuff.lastIndexOf(StringUtil.DOTH));
			setExpertType(expertTypes);
		}
	}



	public String getExpertType() {
		return expertType;
	}

	public void setExpertType(String expertType) {
		this.expertType = expertType;
	}

	public List<String> getUserIdList() {
		return userIdList;
	}

	public void setUserIdList(List<String> userIdList) {
		this.userIdList = userIdList;
	}

	public List<String> getProNameList() {
		return proNameList;
	}

	public void setProNameList(List<String> proNameList) {
		this.proNameList = proNameList;
	}

	public String getIsLike() {
		return isLike;
	}

	public void setIsLike(String isLike) {
		this.isLike = isLike;
	}

	public void setCurJoinParam(List<Map<String, String>> curJoinParam) {
		this.curJoinParam = curJoinParam;
	}

	public List<String> getCurJoinStr() {
		return curJoinStr;
	}

	public void setCurJoinStr(List<String> curJoinStr) {
		this.curJoinStr = curJoinStr;
	}

	public String getCurJoin() {
		return curJoin;
	}

	public void setCurJoin(String curJoin) {
		this.curJoin = curJoin;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public String getFirstShow() {
		return firstShow;
	}

	public void setFirstShow(String firstShow) {
		this.firstShow = firstShow;
	}

	public Boolean getCanInvite() {
		return canInvite;
	}

	public void setCanInvite(Boolean canInvite) {
		this.canInvite = canInvite;
	}

	public String getCanInviteTeamIds() {
		return canInviteTeamIds;
	}

	public void setCanInviteTeamIds(String canInviteTeamIds) {
		this.canInviteTeamIds = canInviteTeamIds;
	}

	public String getServiceIntentionStr() {
		return serviceIntentionStr;
	}

	public void setServiceIntentionStr(String serviceIntentionStr) {
		this.serviceIntentionStr = serviceIntentionStr;
	}

	public String getTeamLeaderId() {
		return teamLeaderId;
	}

	public void setTeamLeaderId(String teamLeaderId) {
		this.teamLeaderId = teamLeaderId;
	}

	public String getExpertId() {
		return expertId;
	}

	public void setExpertId(String expertId) {
		this.expertId = expertId;
	}

	public String getSiteShow() {
		return siteShow;
	}

	public void setSiteShow(String siteShow) {
		this.siteShow = siteShow;
	}

	public String getTopShow() {
		return topShow;
	}

	public void setTopShow(String topShow) {
		this.topShow = topShow;
	}

	public String getQueryStr() {
		return queryStr;
	}

	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}

	public String getTopPrise() {
		return topPrise;
	}

	public void setTopPrise(String topPrise) {
		this.topPrise = topPrise;
	}

	public String getNowProject() {
		return nowProject;
	}

	public void setNowProject(String nowProject) {
		this.nowProject = nowProject;
	}

	private String myFind; //前台关键字查询

	private String keyWords; //后台关键字

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public String getMyFind() {
		return myFind;
	}

	public void setMyFind(String myFind) {
		this.myFind = myFind;
	}

	public String getPostTitle() {
		return postTitle;
	}

	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}



	public String getIsFront() {
		return isFront;
	}
	public void setIsFront(String isFront) {
		this.isFront = isFront;
	}
	public boolean getInviteState() {
		if (teamUserRelation != null) {
			if ("2".equals(teamUserRelation.getState())||"0".equals(teamUserRelation.getState())) {
				return false;
			}else{
				return true;
			}
		}else{
			return true;
		}
	}
 	public OaNotify getOaNotify() {
		return oaNotify;
	}

	public void setOaNotify(OaNotify oaNotify) {
		this.oaNotify = oaNotify;
	}



	public String getMainExp() {
		return mainExp;
	}
	public void setMainExp(String mainExp) {
		this.mainExp = mainExp;
	}
	//获取导师年龄
 	public int getAgeNow() {
 		if (user!=null && user.getBirthday()!=null)  {
 			Date birthDay = user.getBirthday();
 	 		Calendar cal = Calendar.getInstance();

 	        int yearNow = cal.get(Calendar.YEAR);
 	        int monthNow = cal.get(Calendar.MONTH);
 	        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
 	        cal.setTime(birthDay);

 	        int yearBirth = cal.get(Calendar.YEAR);
 	        int monthBirth = cal.get(Calendar.MONTH);
 	        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

 	        int age = yearNow - yearBirth;

 	        if (monthNow <= monthBirth) {
 	            if (monthNow == monthBirth) {
 	                if (dayOfMonthNow < dayOfMonthBirth) age--;
 	            }else{
 	                age--;
 	            }
 	        }
 	        return age;
 		}
 		return 0;
 	}

	public String getRoleType() { return roleType;}

	public void setRoleType(String roleType) { this.roleType = roleType;}

	public TeamUserRelation getTeamUserRelation() {
		return teamUserRelation;
	}

	public void setTeamUserRelation(TeamUserRelation teamUserRelation) {
		this.teamUserRelation = teamUserRelation;
	}

	public String getCurrentId() {
		return currentId;
	}

	public void setCurrentId(String currentId) {
		this.currentId = currentId;
	}
 	public String getEducationType() {
		return educationType;
	}

	public void setEducationType(String educationType) {
		this.educationType = educationType;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public BackTeacherExpansion() {
		super();
	}

	public BackTeacherExpansion(String id) {
		super(id);
	}

	public BackTeacherExpansion(User user) {
        super();
        this.user = user;
    }

    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Length(min=0, max=64, message="层次长度必须介于 0 和 64 之间")
	public String getArrangement() {
		return arrangement;
	}

	public void setArrangement(String arrangement) {
		this.arrangement = arrangement;
	}

	public Integer getDiscipline() {
		return discipline;
	}

	public void setDiscipline(Integer discipline) {
		this.discipline = discipline;
	}

	@Length(min=0, max=32, message="行业长度必须介于 0 和 32 之间")
	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	@Length(min=0, max=20, message="职称长度必须介于 0 和 20 之间")
	public String getTechnicalTitle() {
		return technicalTitle;
	}

	public void setTechnicalTitle(String technicalTitle) {
		this.technicalTitle = technicalTitle;
	}

	public String getServiceIntention() {
		return serviceIntention;
	}

	public void setServiceIntention(String serviceIntention) {
		this.serviceIntention = serviceIntention;
		if (StringUtils.isNotBlank(serviceIntention)) {
			serviceIntentionIds=new ArrayList<String>();
			for (String s : StringUtils.split(serviceIntention, StringUtil.DOTH)) {
				serviceIntentionIds.add(s);
			}
		}
	}

	@Length(min=0, max=128, message="工作单位长度必须介于 0 和 128 之间")
	public String getWorkUnit() {
		return workUnit;
	}

	public void setWorkUnit(String workUnit) {
		this.workUnit = workUnit;
	}

	@Length(min=0, max=128, message="联系地址长度必须介于 0 和 128 之间")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Length(min=0, max=512, message="工作简历长度必须介于 0 和 512 之间")
	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

	@Length(min=0, max=128, message="推荐单位长度必须介于 0 和 128 之间")
	public String getRecommendedUnits() {
		return recommendedUnits;
	}

	public void setRecommendedUnits(String recommendedUnits) {
		this.recommendedUnits = recommendedUnits;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getAward() {
		return award;
	}

	public void setAward(String award) {
		this.award = award;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Length(min=0, max=128, message="评审项目名称长度必须介于 0 和 128 之间")
	public String getReviewName() {
		return reviewName;
	}

	public void setReviewName(String reviewName) {
		this.reviewName = reviewName;
	}

	@Length(min=0, max=128, message="参与评审年份长度必须介于 0 和 128 之间")
	public String getJoinReviewTime() {
		return joinReviewTime;
	}

	public void setJoinReviewTime(String joinReviewTime) {
		this.joinReviewTime = joinReviewTime;
	}

	@Length(min=0, max=128, message="开户银行长度必须介于 0 和 128 之间")
	public String getFirstBank() {
		return firstBank;
	}

	public void setFirstBank(String firstBank) {
		this.firstBank = firstBank;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		if (StringUtil.isEmpty(bankAccount)) {
			this.bankAccount =null;
		}else{
			this.bankAccount = bankAccount;
		}
	}

	@Length(min=0, max=20, message="导师类型长度必须介于 0 和 20 之间")
	public String getTeachertype() {
		return teachertype;
	}

	public void setTeachertype(String teachertype) {
		this.teachertype = teachertype;
	}

	@Length(min=0, max=11, message="是否公开长度必须介于 0 和 11 之间")
	public String getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Date getAwardTime() {
		return awardTime;
	}
	public void setAwardTime(Date awardTime) {
		this.awardTime = awardTime;
	}
	public String getAwardLevel() {
		return awardLevel;
	}
	public void setAwardLevel(String awardLevel) {
		this.awardLevel = awardLevel;
	}

	public List<String> getServiceIntentionIds() {
		return serviceIntentionIds;
	}

	public void setServiceIntentionIds(List<String> serviceIntentionIds) {
		this.serviceIntentionIds = serviceIntentionIds;
		if (serviceIntentionIds!=null&&serviceIntentionIds.size()!=0) {
			StringBuffer strbuff = new StringBuffer();
			for (String s : serviceIntentionIds) {
				strbuff.append(s);
				strbuff.append(",");
			}
			serviceIntention= strbuff.substring(0, strbuff.lastIndexOf(","));
		}else{
			serviceIntention=null;
		}
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getWorkUnitType() {
		return workUnitType;
	}

	public void setWorkUnitType(String workUnitType) {
		this.workUnitType = workUnitType;
	}


	public List<String> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<String> roleIdList) {
		this.roleIdList = roleIdList;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
}
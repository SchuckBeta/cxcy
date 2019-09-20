package com.oseasy.pie.modules.impdata.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pie.modules.impdata.dao.ProjectHsErrorDao;
import com.oseasy.pie.modules.impdata.entity.ProjectHsError;
import com.oseasy.pro.common.service.CommonService;
import com.oseasy.pro.modules.excellent.entity.ExcellentShow;
import com.oseasy.pro.modules.excellent.service.ExcellentShowService;
import com.oseasy.pro.modules.project.dao.ProjectDeclareDao;
import com.oseasy.pro.modules.project.entity.ProjectDeclare;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.project.vo.ProjectNodeVo;
import com.oseasy.sys.common.utils.IdUtils;
import com.oseasy.sys.modules.sys.dao.BackTeacherExpansionDao;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;
import com.oseasy.sys.modules.sys.vo.TeacherType;
import com.oseasy.sys.modules.team.dao.TeamDao;
import com.oseasy.sys.modules.team.dao.TeamUserRelationDao;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.entity.TeamUserRelation;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 导入项目错误数据表（华师）Service.
 * @author 9527
 * @version 2017-10-10
 */
@Service
@Transactional(readOnly = true)
public class ProjectHsErrorService extends CrudService<ProjectHsErrorDao, ProjectHsError> {
	@Autowired
	private StudentErrorService studentErrorService;
	@Autowired
	private TeacherErrorService teacherErrorService;
	@Autowired
	private TeamDao teamDao;
	@Autowired
	private ProjectDeclareDao projectDeclareDao;
	@Autowired
	private ProjectDeclareService projectDeclareService;
	@Autowired
	private ExcellentShowService excellentShowService;

	@Autowired
	private BackTeacherExpansionDao backTeacherExpansionDao;
	@Autowired
	private TeamUserRelationDao teamUserRelationDao;
	@Autowired
	private CommonService commonService;
	@Autowired
	private UserService userService;
	public ProjectHsError get(String id) {
		return super.get(id);
	}
	public List<Map<String,String>> getListByImpId(String impid) {
		return dao.getListByImpId(impid);
	}
	public List<ProjectHsError> findList(ProjectHsError projectHsError) {
		return super.findList(projectHsError);
	}

	public Page<ProjectHsError> findPage(Page<ProjectHsError> page, ProjectHsError projectHsError) {
		return super.findPage(page, projectHsError);
	}
	private List<String> getTeas(String teaName,String teaNo,String teaTitle) {
		List<String> lname=new ArrayList<String>();
		List<String> lno=new ArrayList<String>();
		List<String> lti=new ArrayList<String>();
		for(String s:teaName.split("、")) {
			if (StringUtil.isNotEmpty(s)) {
				lname.add(s);
			}
		}
		for(String s:teaNo.split("、")) {
			if (StringUtil.isNotEmpty(s)) {
				lno.add(s);
			}
		}
		if (StringUtil.isNotEmpty(teaTitle)) {
			for(String s:teaTitle.split("、")) {
				if (StringUtil.isNotEmpty(s)) {
					lti.add(s);
				}
			}
		}
		List<String> ret=new ArrayList<String>();
		for(int i=0;i<lno.size();i++) {
			User u=userService.getByNo(lno.get(i));
			if (u==null) {
				u=new User();
				BackTeacherExpansion tea=new BackTeacherExpansion();
				tea.setTeachertype(TeacherType.TY_XY.getKey());
				u.setName(lname.get(i));
				u.setNo(lno.get(i));
				if (i<lti.size()) {
					tea.setTechnicalTitle(lti.get(i));
				}
				tea.setUser(u);
				teacherErrorService.saveTeacher(tea);
			}
			ret.add(u.getId());
		}
		return ret;
	}
	private List<String> getMems(String mems) {
		List<String[]> list=ImpDataService.getMembersData(mems);
		if (list!=null&&list.size()>0) {
			List<String> ret=new ArrayList<String>();
			for(String[] ss:list) {
				User u=userService.getByNo(ss[1]);
				if (u==null) {
					u=new User();
					StudentExpansion leaderStu=new StudentExpansion();
					u.setName(ss[0]);
					u.setNo(ss[1]);
					leaderStu.setUser(u);
					studentErrorService.saveStudent(leaderStu);
				}
				ret.add(u.getId());
			}
			return ret;
		}else{
			return null;
		}
	}
	@Transactional(readOnly = false)
	public void saveProject(ProjectHsError pe) {
		//负责人
		User leader=userService.getByNo(pe.getNo());
		if (leader==null) {
			leader=new User();
			StudentExpansion leaderStu=new StudentExpansion();
			leader.setOffice(new Office(pe.getOffice()));
			if (StringUtil.isNotEmpty(pe.getProfes())) {
				leader.setProfessional(pe.getProfes());
			}
			leader.setName(pe.getLeader());
			leader.setNo(pe.getNo());
			leader.setMobile(pe.getMobile());
			leader.setEmail(pe.getEmail());
			try {
				if (StringUtil.isNotEmpty(pe.getGrade())) {
					leaderStu.setEnterdate(DateUtil.parseDate(pe.getGrade()+"-01-01", "yyyy-MM-dd"));
				}
			} catch (ParseException e) {
			}
			leaderStu.setUser(leader);
			studentErrorService.saveStudent(leaderStu);
		}
		//处理团队成员
		List<String> mems=getMems(pe.getMembers());
		//处理导师
		List<String> teas=getTeas(pe.getTeachers(), pe.getTeaNo(), pe.getTeaTitle());
		/*保存团队信息*/
		Team team=new Team();
		team.setId(IdGen.uuid());
        team.setNumber(IdUtils.getTeamNumberByDb());
        if(StringUtil.isNotEmpty(pe.getNumber())){
            team.setName("项目"+pe.getNumber()+"的团队");
        }else{
            team.setName("项目"+team.getNumber()+"的团队");
        }
		team.setSponsor(leader.getId());
		team.setState("1");
		String localCollege="";
		if (leader.getOffice()!=null) {
			localCollege=leader.getOffice().getId();
		}
		if (StringUtil.isNotEmpty(leader.getProfessional())) {
			localCollege=leader.getProfessional();
		}
		team.setLocalCollege(localCollege);
		team.setProjectName(pe.getName());
		int member_num=1;//成员人数
		int enterprise_teacher_num=0;//企业导师人数
		int school_teacher_num=0;//校园导师人数
		for(String teaid:teas) {
			BackTeacherExpansion tea=backTeacherExpansionDao.findTeacherByUserId(teaid);
			/*保存团队导师信息*/
			TeamUserRelation teamur=new TeamUserRelation();
			teamur.setId(IdGen.uuid());
			teamur.setTeamId(team.getId());
			teamur.setUser(UserUtils.get(teaid));
			teamur.setUpdateBy(leader);
			teamur.setCreateBy(leader);
			teamur.setUpdateDate(new Date());
			teamur.setCreateDate(teamur.getUpdateDate());
			teamur.setState("0");
			teamur.setUserType("2");
			teamUserRelationDao.insert(teamur);
			if ((TeacherType.TY_QY.getKey()).equals(tea.getTeachertype())) {
				enterprise_teacher_num++;
			}else{
				school_teacher_num++;
			}
		}
		team.setEnterpriseTeacherNum(enterprise_teacher_num);
		team.setSchoolTeacherNum(school_teacher_num);
		if (mems!=null) {
			member_num=member_num+mems.size();
		}
		team.setMemberNum(member_num);

		team.setUpdateBy(leader);
		team.setCreateBy(leader);
		team.setUpdateDate(new Date());
		team.setCreateDate(team.getUpdateDate());
		teamDao.insert(team);
		/*保存团队组员信息*/
		TeamUserRelation teamurLeader=new TeamUserRelation();
		teamurLeader.setId(IdGen.uuid());
		teamurLeader.setTeamId(team.getId());
		teamurLeader.setUser(leader);
		teamurLeader.setUpdateBy(leader);
		teamurLeader.setCreateBy(leader);
		teamurLeader.setUpdateDate(new Date());
		teamurLeader.setCreateDate(teamurLeader.getUpdateDate());
		teamurLeader.setState("0");
		teamurLeader.setUserType("1");
		teamUserRelationDao.insert(teamurLeader);
		if (mems!=null) {
			for(String mebid:mems) {
				TeamUserRelation teamur=new TeamUserRelation();
				teamur.setId(IdGen.uuid());
				teamur.setTeamId(team.getId());
				teamur.setUser(UserUtils.get(mebid));
				teamur.setUpdateBy(leader);
				teamur.setCreateBy(leader);
				teamur.setUpdateDate(new Date());
				teamur.setCreateDate(teamur.getUpdateDate());
				teamur.setState("0");
				teamur.setUserType("1");
				teamUserRelationDao.insert(teamur);
			}
		}
		/*保存项目信息*/
		ProjectDeclare pd=new ProjectDeclare();
		pd.setId(IdGen.uuid());
		pd.setTeamId(team.getId());
		pd.setUpdateBy(leader);
		pd.setCreateBy(leader);
		pd.setUpdateDate(new Date());
		pd.setCreateDate(pd.getUpdateDate());
		pd.setLevel(pe.getLevel());
		pd.setApplyTime(new Date());
		pd.setNumber(pe.getNumber());
		pd.setName(pe.getName());
		pd.setType(pe.getType());
		pd.setLeader(leader.getId());
		pd.setIntroduction(pe.getRemarks());
		pd.setStatus("0");
		pd.setActywId(ProjectNodeVo.YW_ID);
		pd.setYear(pe.getYear());
		projectDeclareDao.insert(pd);
		commonService.copyTeamUserHistoryFromTUR(pd.getActywId(), pd.getTeamId(), pd.getId(), "0",pd.getYear());
		excellentShowService.saveExcellentShow(pd.getIntroduction(), pd.getTeamId(),ExcellentShow.Type_Project,pd.getId(),pd.getActywId());//保存优秀展示
		projectDeclareService.threeStepSave(pd);
	}
	@Transactional(readOnly = false)
	public void save(ProjectHsError projectHsError) {
		super.save(projectHsError);
	}

	@Transactional(readOnly = false)
	public void delete(ProjectHsError projectHsError) {
		super.delete(projectHsError);
	}
	@Transactional(readOnly = false)
	public void insert(ProjectHsError projectError) {
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())) {
			projectError.setUpdateBy(user);
			projectError.setCreateBy(user);
		}
		projectError.setUpdateDate(new Date());
		projectError.setCreateDate(projectError.getUpdateDate());
		dao.insert(projectError);
	}
}
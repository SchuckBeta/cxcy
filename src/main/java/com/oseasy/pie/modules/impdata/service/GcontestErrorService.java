package com.oseasy.pie.modules.impdata.service;

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
import com.oseasy.pie.modules.impdata.dao.GcontestErrorDao;
import com.oseasy.pie.modules.impdata.entity.GcontestError;
import com.oseasy.pro.common.service.CommonService;
import com.oseasy.pro.modules.excellent.entity.ExcellentShow;
import com.oseasy.pro.modules.excellent.service.ExcellentShowService;
import com.oseasy.pro.modules.gcontest.dao.GContestDao;
import com.oseasy.pro.modules.gcontest.entity.GContest;
import com.oseasy.pro.modules.gcontest.vo.GContestNodeVo;
import com.oseasy.sys.common.utils.IdUtils;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;
import com.oseasy.sys.modules.team.dao.TeamDao;
import com.oseasy.sys.modules.team.dao.TeamUserRelationDao;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.entity.TeamUserRelation;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 导入互联网+大赛错误数据Service.
 * @author 奔波儿灞
 * @version 2017-12-07
 */
@Service
@Transactional(readOnly = true)
public class GcontestErrorService extends CrudService<GcontestErrorDao, GcontestError> {
	@Autowired
	private StudentErrorService studentErrorService;
	@Autowired
	private TeamDao teamDao;
	@Autowired
	private GContestDao gContestDao;
	@Autowired
	private ExcellentShowService excellentShowService;
	@Autowired
	private TeacherErrorService teacherErrorService;
	@Autowired
	private TeamUserRelationDao teamUserRelationDao;
	@Autowired
	private CommonService commonService;

	@Autowired
	private UserService userService;
	private List<String> getTeas(String teas,String type) {
		List<String> ret=new ArrayList<String>();
		if (StringUtil.isNotEmpty(teas)) {
			String[] stss=teas.split("、");
			for(int i=0;i<stss.length;i++) {
				String[] steas=stss[i].split("/");
				User u=userService.getByNo(steas[1]);
				if (u==null) {
					u=new User();
					BackTeacherExpansion tea=new BackTeacherExpansion();
					tea.setTeachertype(type);
					u.setName(steas[0]);
					u.setNo(steas[1]);
					tea.setUser(u);
					teacherErrorService.saveTeacher(tea);
				}
				ret.add(u.getId());
			}
		}
		return ret;
	}
	@Transactional(readOnly = false)
	public void saveGcontest(GcontestError pe) {
		String gnumber=IdUtils.getGContestNumberByDb();
		//负责人
		String leadername=pe.getLeader().split("/")[0];
		String leaderno=pe.getLeader().split("/")[1];
		User leader=userService.getByNo(leaderno);
		if (leader==null) {
			leader=new User();
			StudentExpansion leaderStu=new StudentExpansion();
			leader.setOffice(new Office(pe.getOffice()));
			if (StringUtil.isNotEmpty(pe.getProfes())) {
				leader.setProfessional(pe.getProfes());
			}
			leader.setName(leadername);
			leader.setNo(leaderno);
			leader.setMobile(pe.getMobile());
			leaderStu.setUser(leader);
			studentErrorService.saveStudent(leaderStu);
		}
		//处理导师
		List<String> steas=getTeas(pe.getSteachers(),"1");
		List<String> eteas=getTeas(pe.getEteachers(),"2");
		/*保存团队信息*/
		Team team=new Team();
		team.setId(IdGen.uuid());
		team.setName("大赛"+gnumber+"的团队");
		team.setSponsor(leader.getId());
		team.setState("1");
		team.setNumber(IdUtils.getTeamNumberByDb());
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
		for(String teaid:steas) {
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
			school_teacher_num++;
		}
		for(String teaid:eteas) {
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
			enterprise_teacher_num++;
		}
		team.setEnterpriseTeacherNum(enterprise_teacher_num);
		team.setSchoolTeacherNum(school_teacher_num);
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
		/*保存大赛信息*/
		GContest pd=new GContest();
		pd.setId(IdGen.uuid());
		pd.setTeamId(team.getId());
		pd.setUpdateBy(leader);
		pd.setCreateBy(leader);
		pd.setUpdateDate(new Date());
		pd.setCreateDate(pd.getUpdateDate());
		if(StringUtil.isEmpty(pe.getGroups())){
	        pd.setLevel("4");//校级
		}else{
	        pd.setLevel(pe.getGroups());
		}
		pd.setSubTime(new Date());
		pd.setCompetitionNumber(gnumber);
		pd.setpName(pe.getName());
		pd.setType(pe.getType());
		pd.setDeclareId(leader.getId());
		pd.setRemarks(pe.getRemarks());
		pd.setAuditState("7");
		pd.setProcInsId("import");
		pd.setSchoolendResult(pe.getHuojiang());
		pd.setUniversityId(pe.getOffice());
		pd.setCurrentSystem("校赛");
		pd.setActywId(GContestNodeVo.YW_ID);
		pd.setYear(DateUtil.getYear());
		gContestDao.insert(pd);
		commonService.copyTeamUserHistoryFromTUR(pd.getActywId(), pd.getTeamId(), pd.getId(), "1",pd.getYear());
		excellentShowService.saveExcellentShow(pd.getIntroduction(), pd.getTeamId(),ExcellentShow.Type_Gcontest,pd.getId(),pd.getActywId());//保存优秀展示
	}
	public List<Map<String,String>> getListByImpId(String impid) {
		return dao.getListByImpId(impid);
	}
	public GcontestError get(String id) {
		return super.get(id);
	}

	public List<GcontestError> findList(GcontestError gcontestError) {
		return super.findList(gcontestError);
	}

	public Page<GcontestError> findPage(Page<GcontestError> page, GcontestError gcontestError) {
		return super.findPage(page, gcontestError);
	}

	@Transactional(readOnly = false)
	public void save(GcontestError gcontestError) {
		super.save(gcontestError);
	}

	@Transactional(readOnly = false)
	public void delete(GcontestError gcontestError) {
		super.delete(gcontestError);
	}
	@Transactional(readOnly = false)
	public void insert(GcontestError gcontestError) {
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())) {
			gcontestError.setUpdateBy(user);
			gcontestError.setCreateBy(user);
		}
		gcontestError.setUpdateDate(new Date());
		gcontestError.setCreateDate(gcontestError.getUpdateDate());
		dao.insert(gcontestError);
	}
}
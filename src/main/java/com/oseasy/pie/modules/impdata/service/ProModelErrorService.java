package com.oseasy.pie.modules.impdata.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.tool.process.vo.FormTheme;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pie.modules.impdata.dao.ProModelErrorDao;
import com.oseasy.pie.modules.impdata.entity.ProModelError;
import com.oseasy.pro.common.service.CommonService;
import com.oseasy.pro.modules.excellent.entity.ExcellentShow;
import com.oseasy.pro.modules.excellent.service.ExcellentShowService;
import com.oseasy.pro.modules.promodel.dao.ProModelDao;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.pro.modules.promodel.tool.oper.ItOper;
import com.oseasy.pro.modules.promodel.vo.ItReqParam;
import com.oseasy.pro.modules.workflow.dao.ProModelTlxyDao;
import com.oseasy.pro.modules.workflow.entity.ProModelTlxy;
import com.oseasy.pro.modules.workflow.impl.SpiltPost;
import com.oseasy.pro.modules.workflow.impl.SpiltPref;
import com.oseasy.pro.modules.workflow.service.ProModelHsxmService;
import com.oseasy.pro.modules.workflow.service.ProModelMdGcService;
import com.oseasy.pro.modules.workflow.service.ProModelTlxyService;
import com.oseasy.sys.common.utils.IdUtils;
import com.oseasy.sys.modules.sys.dao.BackTeacherExpansionDao;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;
import com.oseasy.sys.modules.sys.entity.SysNumberRule;
import com.oseasy.sys.modules.sys.enums.RoleBizTypeEnum;
import com.oseasy.sys.modules.sys.service.SysNumberRuleService;
import com.oseasy.sys.modules.sys.utils.NumRuleUtils;
import com.oseasy.sys.modules.sys.vo.TeacherType;
import com.oseasy.sys.modules.team.dao.TeamDao;
import com.oseasy.sys.modules.team.dao.TeamUserRelationDao;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.entity.TeamUserRelation;
import com.oseasy.sys.modules.team.service.TeamUserHistoryService;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 导入项目错误数据表Service.
 * @author 奔波儿灞
 * @version 2018-03-13
 */
@Service
@Transactional(readOnly = true)
public class ProModelErrorService extends CrudService<ProModelErrorDao, ProModelError> {
	@Autowired
	private TeacherErrorService teacherErrorService;
	@Autowired
	private StudentErrorService studentErrorService;

	@Autowired
	private TeamUserHistoryService teamUserHistoryService;
	@Autowired
	private TeamDao teamDao;
	@Autowired
	private ProModelDao proModelDao;
	@Autowired
	private ProModelTlxyDao proModelTlxyDao;
	@Autowired
	private ProModelService proModelService;

	@Autowired
	private ProModelMdGcService proModelMdGcService;
	@Autowired
	private ProModelHsxmService proModelHsxmService;
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
    @Autowired
    private PieSysAttachmentService pieSysAttachmentService;
	@Autowired
	private SysNumberRuleService sysNumberRuleService;
	public ProModelError get(String id) {
		return super.get(id);
	}
	public List<Map<String,String>> getListByImpId(String impid) {
		return dao.getListByImpId(impid);
	}
	public List<ProModelError> findList(ProModelError proModelError) {
		return super.findList(proModelError);
	}

	public Page<ProModelError> findPage(Page<ProModelError> page, ProModelError proModelError) {
		return super.findPage(page, proModelError);
	}

	@Transactional(readOnly = false)
	public void save(ProModelError proModelError) {
		super.save(proModelError);
	}

	@Transactional(readOnly = false)
	public void delete(ProModelError proModelError) {
		super.delete(proModelError);
	}

  	@Transactional(readOnly = false)
  	public void updatePL(List<? extends ProModelError> entitys) {
  	  dao.updatePL(entitys);
  	}
  	@Transactional(readOnly = false)
  	public void deleteWL(ProModelError proModelError) {
  	    dao.deleteWL(proModelError);
  	}
	@Transactional(readOnly = false)
	public void insert(ProModelError projectError) {
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())) {
			projectError.setUpdateBy(user);
			projectError.setCreateBy(user);
		}
		projectError.setUpdateDate(new Date());
		projectError.setCreateDate(projectError.getUpdateDate());
		dao.insert(projectError);
	}
	@Transactional(readOnly = false)
	public void update(ProModelError projectError) {
	    User user = UserUtils.getUser();
	    if (StringUtils.isNotBlank(user.getId())) {
	        projectError.setUpdateBy(user);
	        projectError.setCreateBy(user);
	    }
	    projectError.setUpdateDate(new Date());
	    projectError.setCreateDate(projectError.getUpdateDate());
	    dao.update(projectError);
	}

    @Transactional(readOnly = false)
    public void saveProject(ProModelError pe, ActYw ay, ItOper impVo) {
        saveProject(pe, ay, impVo, true);
    }
	@Transactional(readOnly = false)
	public void saveProject(ProModelError pe, ActYw ay, ItOper impVo, boolean hasSave) {
		//负责人
		User leader=userService.getByNo(pe.getNo());
		if((leader == null) && StringUtil.isNotEmpty(pe.getLeader()) && StringUtil.isNotEmpty(pe.getMobile())){
		    leader = userService.getUserByMobileAndName(pe.getLeader(), pe.getMobile());
        }
		String pnum=pe.getNumber();
		if(StringUtil.isEmpty(pnum)){
			SysNumberRule sysNumberRule=sysNumberRuleService.getRuleByAppType(ay.getId(),"");
			if(sysNumberRule!=null){
				try {
					String year= DateUtil.getYear();
					if(pe.getYear()!=null){
						year=pe.getYear();
					}
					pnum = NumRuleUtils.getNumberText(ay.getId(),year);
				}catch(Exception e){
					pnum=IdUtils.getProjectNumberByDb();
				}
			}else{
				pnum=IdUtils.getProjectNumberByDb();
			}
			pe.setNumber(pnum);
		}
		if (leader==null) {
			leader=new User();
			StudentExpansion leaderStu=new StudentExpansion();
			leader.setOffice(new Office(pe.getOffice()));
	        if(!hasSave){
	            leader.setDelFlag(Const.YHIDE);
	        }
	        if(leader.getOffice() != null){
	            leader.setProfessional(leader.getOffice().getId());
	        }
			if (StringUtil.isNotEmpty(pe.getProfes())) {
	            leader.setProfessionalName(pe.getProfes());
			}
			leader.setUserType(RoleBizTypeEnum.XS.getValue());
			leader.setName(pe.getLeader());
			leader.setNo(pe.getNo());
			leader.setLoginName(pe.getNo());
			leader.setMobile(pe.getMobile());
			leader.setEmail(pe.getEmail());
			leader.setSchool(pe.getSchool());
			leaderStu.setUser(leader);
			leaderStu = studentErrorService.saveStudent(leaderStu);
			leader = leaderStu.getUser();
		}

		List<String> mems = Lists.newArrayList();
		List<String> teas = Lists.newArrayList();
		if(hasSave){
    		//处理团队成员
    		mems = getMems(hasSave, pe.getMembers());
    		if(StringUtil.checkEmpty(mems)){
    		    mems = Lists.newArrayList();
            }
    		//处理导师
    		teas = getTeas(pe.getTeachers(), pe.getTeaNo(), pe.getTeaTitle());
		    //teas = getTeas(pe.getTeachers());
    		if(StringUtil.checkEmpty(teas)){
    		    teas = Lists.newArrayList();
    		}
        }

        /*保存团队信息*/
		Team team = null;
		if(StringUtil.isNotEmpty(pe.getTeamId())){
            team = teamDao.get(pe.getTeamId());
        }
		if((team == null) && StringUtil.isNotEmpty(pe.getTeamName())){
            List<Team> curteams = teamDao.selectTeamByName(pe.getTeamName());
            if(StringUtil.checkNotEmpty(curteams) && (curteams.size() == 1)){
                team = curteams.get(0);
            }
        }
		/**
		 * 如果团队不存在，作为新的创建.
		 */
		if(team == null){
		    team = new Team();
            if(!hasSave){
                team.setDelFlag(Const.YHIDE);
            }
	        team.setId(IdGen.uuid());
	        if(StringUtil.isNotEmpty(pe.getTeamName())){
	            team.setName(pe.getTeamName());
	        }else{
	            team.setName(pnum+"的团队");
	        }
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
	        TeamUserRelation pteamur = new TeamUserRelation();
            if(hasSave){
    	        for(String teaid:teas) {
                    if(StringUtil.isEmpty(team.getFirstTeacher())){
                        team.setFirstTeacher(teaid);
                    }

    	            BackTeacherExpansion tea=backTeacherExpansionDao.findTeacherByUserId(teaid);
    	            if(tea == null){
    	                continue;
    	            }

                    if ((TeacherType.TY_QY.getKey()).equals(tea.getTeachertype())) {
                        enterprise_teacher_num++;
                    }else{
                        school_teacher_num++;
                    }

                    pteamur.setTeamId(team.getId());
                    pteamur.setUser(UserUtils.get(teaid));
                    TeamUserRelation  ctuRelation = teamUserRelationDao.getByTeamAndUserNo(pteamur);
                    if(ctuRelation != null){
                        continue;
                    }

                    /*保存团队导师信息*/
                    TeamUserRelation teamur = new TeamUserRelation();
                    teamur.setId(IdGen.uuid());
                    teamur.setTeamId(team.getId());
                    teamur.setUser(UserUtils.get(teaid));
                    teamur.setUpdateBy(leader);
                    teamur.setCreateBy(leader);
                    teamur.setUpdateDate(new Date());
                    teamur.setCreateDate(teamur.getUpdateDate());
                    teamur.setState("0");
                    teamur.setUserType("2");
                    teamur.setUserType(RoleBizTypeEnum.DS.getValue());
                    teamUserRelationDao.insert(teamur);
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
		}

        TeamUserRelation pteleader = new TeamUserRelation();
        pteleader.setTeamId(team.getId());
        pteleader.setUser(leader);
        TeamUserRelation  ctuRelationLeader = teamUserRelationDao.getByTeamAndUserNo(pteleader);
        if(ctuRelationLeader == null){
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
            teamurLeader.setUserType(RoleBizTypeEnum.XS.getValue());
            teamUserRelationDao.insert(teamurLeader);
        }

        if(hasSave){
    		if (mems!=null) {
    	        TeamUserRelation pmems = new TeamUserRelation();
    			for(String mebid:mems) {
    		        pmems.setTeamId(team.getId());
    		        pmems.setUser(UserUtils.get(mebid));
    		        if((teamUserRelationDao.getByTeamAndUserNo(pmems)) != null){
    		            continue;
    		        }

    				TeamUserRelation teamur=new TeamUserRelation();
    				teamur.setId(IdGen.uuid());
    				teamur.setTeamId(team.getId());
    				teamur.setUser(UserUtils.get(mebid));
                    teamur.setUserType(RoleBizTypeEnum.XS.getValue());
    				teamur.setUpdateBy(leader);
    				teamur.setCreateBy(leader);
    				teamur.setUpdateDate(new Date());
    				teamur.setCreateDate(teamur.getUpdateDate());
    				teamur.setState("0");
    				teamur.setUserType("1");
    				teamUserRelationDao.insert(teamur);
    			}
    		}
        }
		/*保存项目信息*/
		ProModel pd = new ProModel();
        pd.setId(IdGen.uuid());
		pd.setTeamId(team.getId());
		pd.setUpdateBy(leader);
		pd.setCreateBy(leader);
		pd.setUpdateDate(new Date());
		pd.setCreateDate(pd.getUpdateDate());
		pd.setSubTime(new Date());
		pd.setCompetitionNumber(pe.getNumber());
		pd.setPName(pe.getName());
		pd.setType(pe.getType());
		pd.setDeclareId(leader.getId());
		//判断是否问节点导入，且是否为首节点导入.
        pd.setImpdata(Const.YES);
        pd.setSubStatus(Const.YES);
		if(impVo.getIsImpFirstData()){
            pd.setState(Const.NO);
		}else{
            pd.setState(Const.YES);
		}
        pd.setImpdata(Const.YES);
		pd.setYear(pe.getYear());
		pd.setProCategory(pe.getType());
		pd.setFinalResult(pe.getResult());
		pd.setActYwId(ay.getId());
		pd.setIntroduction(pe.getIntroduction());
        pd.setRemarks(pe.getIntroduction());
		pd.setProType(ay.getProProject().getProType());
		pd.setType(ay.getProProject().getType());
		pd.setLevel(pe.getLevel());
		pd.setMembers(pe.getMembers());
		pd.setTeachers(pe.getTeachers());
		pd.setBusinfos(pe.getBusinfos());
		/**
         * 拓展表处理.
         */
        if((ay.getFtheme() != null)){
            if((FormTheme.F_TLXY).equals(ay.getFtheme())){
                pd.setFinalStatus(ProModelTlxyService.TLXY_PRO_LEVEL_0000000264);
            }
        }
		proModelDao.insert(pd);
		commonService.copyTeamUserHistoryFromTUR(ay.getId(), pd.getTeamId(), pd.getId(), "1",pd.getYear());
		excellentShowService.saveExcellentShow(pd.getIntroduction(), pd.getTeamId(),ExcellentShow.Type_Project,pd.getId(),ay.getId());//保存优秀展示
		//根据gnodeId判断是否为最后一步导入。 没有gnodeId为最后一步导入
//		if(StringUtil.isEmpty(impVo.getReqParam().getGnodeId())){
	    if(!impVo.getIsImpFirstData()){
			// 标志流程结束
		    pd.setState(Const.YES);
			//团队完成记录
			teamUserHistoryService.updateFinishAsClose(pd.getId());
			proModelDao.update(pd);
		}

		/**
		 * 拓展表处理.
		 */
        if((ay.getFtheme() != null)){
            if((FormTheme.F_TLXY).equals(ay.getFtheme())){
                logger.warn("铜陵学院拓展表保存！");
                ProModelTlxy pentity = pe.getPmTlxy();
                if(pentity == null){
                    pentity = new ProModelTlxy(IdGen.uuid(), pd);
                }else{
                    pentity.setId(IdGen.uuid());
                    pentity.setProModel(pd);
                }
                pentity.setModelId(pd.getId());
                pentity.setCreateBy(UserUtils.getUser());
                pentity.setCreateDate(new Date());
                pentity.setUpdateBy(UserUtils.getUser());
                pentity.setUpdateDate(new Date());
                pentity.setDelFlag(Const.NO);
                proModelTlxyDao.insert(pentity);
            }
			/**
			* 拓展表处理.
			*/
			if((FormTheme.F_HSXM).equals(ay.getFtheme())){
				proModelHsxmService.saveAddPro(pd);
			}else if((FormTheme.F_MD_GC).equals(ay.getFtheme())){
		        proModelMdGcService.saveAddPro(pd);
		    }
        }
		/**
		 * 判断是否有附件，如果有附件处理附件.
		 */
		if(impVo.getIsImpFileData()){
		    pieSysAttachmentService.saveFileForImp(pd, ay, FileTypeEnum.S11,FileStepEnum.S1102);
        }

		/**
         * 判断是否第一个节点导入，如果是启动流程.
         */
		if(impVo.getIsImpFirstData()){
		    try {
	            proModelService.actStartForImp(pd);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
        }
	}

	private ItOper getTeasOper(){
	    ItOper oper =  new ItOper();
//      oper.setReqParam(new ItReqParam());
//      oper.getReqParam().setPostfix(StringUtil.Z_DUN);

        oper.setReqParam(new ItReqParam());
        oper.getReqParam().setPrefix(SpiltPref.SPREF_LINE.getRemark());
        oper.getReqParam().setSpre(SpiltPref.SPREF_LINE);
        oper.getReqParam().setPostfix(SpiltPost.SPOST_DUN.getRemark());
        oper.getReqParam().setSpost(SpiltPost.SPOST_DUN);
        return oper;
	}
    /**
     * [姓名, 学号, 省市, 高校, 部门, 手机, 邮箱, 职称].
     */
    private List<String> getTeas(String teachers){
	    ItOper oper =  getTeasOper();
        List<String[]> list = StringUtil.splitByPF(teachers, oper.getReqParam().getPrefix(), oper.getReqParam().getPostfix());
        if (StringUtil.checkNotEmpty(list)) {
            List<String> ret = new ArrayList<String>();
            for(String[] ss:list) {
                User u=userService.getByNo(ss[1]);
                if((u == null) && StringUtil.isNotEmpty(ss[0]) && StringUtil.isNotEmpty(ss[5])){
                    u = userService.getUserByMobileAndName(ss[0], ss[5]);
                }
                if (u==null) {
                    u=new User();
                    u.setName(ss[0]);
                    u.setNo(ss[1]);
                    u.setArea(ss[2]);
                    //u.setNo(ss[3]);
                    //u.setSubject(ss[4]);
                    u.setMobile(ss[5]);
                    u.setEmail(ss[6]);
                    BackTeacherExpansion tea = new BackTeacherExpansion(u);
                    tea.setTeachertype(TeacherType.TY_XY.getKey());
                    tea.setTechnicalTitle(ss[7]);
                    teacherErrorService.saveTeacher(tea);
                }
                ret.add(u.getId());
            }
            return ret;
        }
        return null;
	}


	private List<String> getTeas(String teaName,String teaNo,String teaTitle){
	    return getTeas(teaName, teaNo, teaTitle, getTeasOper());
	}
    private List<String> getTeas(String teaName,String teaNo,String teaTitle, ItOper impVo){
        if((impVo == null) || (impVo.getReqParam() == null)){
            impVo =  getTeasOper();
        }

		List<String> lname=new ArrayList<String>();
		List<String> lno=new ArrayList<String>();
		List<String> lti=new ArrayList<String>();
		if (StringUtil.isNotEmpty(teaName)) {
			for(String s:teaName.split(impVo.getReqParam().getPostfix())) {
				if (StringUtil.isNotEmpty(s)) {
					lname.add(s);
				}
			}
		}
		if (StringUtil.isNotEmpty(teaNo)) {
			for(String s:teaNo.split(impVo.getReqParam().getPostfix())) {
				if (StringUtil.isNotEmpty(s)) {
					lno.add(s);
				}
			}
		}
		if (StringUtil.isNotEmpty(teaTitle)) {
			for(String s:teaTitle.split(impVo.getReqParam().getPostfix())) {
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
				if(lname.size() > i){
	                u.setName(lname.get(i));
				}else{
				    if(lname.size() > 0){
	                    u.setName(lname.get(0));
				    }
				}
				u.setNo(lno.get(i));
				if(lti.size() > i){
                    tea.setTechnicalTitle(lti.get(i));
                }else{
                    if(lti.size() > 0){
                        tea.setTechnicalTitle(lti.get(0));
                    }
                }
				tea.setUser(u);
				teacherErrorService.saveTeacher(tea);
			}
			ret.add(u.getId());
		}
		return ret;
	}


	private ItOper getMemsOper() {
	    ItOper oper =  new ItOper();
        oper.setReqParam(new ItReqParam());
        oper.getReqParam().setPrefix(SpiltPref.SPREF_LINE.getRemark());
        oper.getReqParam().setSpre(SpiltPref.SPREF_LINE);
        oper.getReqParam().setPostfix(SpiltPost.SPOST_FH.getRemark());
        oper.getReqParam().setSpost(SpiltPost.SPOST_FH);
	    return oper;
	}
	/**
	 * [姓名, 学号, 省市, 高校, 专业, 手机, 邮箱, 入学年份, 毕业年份].
	 */
	private List<String> getMems(boolean hasSave, String mems) {
	    return getMems(hasSave, mems, getMemsOper());
	}
    private List<String> getMems(boolean hasSave, String mems, ItOper impVo) {
        if((impVo == null) || (impVo.getReqParam() == null)){
            impVo =  getMemsOper();
        }

        List<String[]> list = null;
        if(hasSave){
            list = ImpDataService.getMembersData(mems, impVo.getReqParam());
            if (StringUtil.checkNotEmpty(list)) {
                List<String> ret=new ArrayList<String>();
                for(String[] ss:list) {
                    User u = userService.getByNo(ss[1]);
                    if (u==null) {
                        u=new User();
                        u.setName(ss[0]);
                        u.setNo(ss[1]);
//                        u.setArea(ss[2]);
//                        u.setSchool(ss[3]);
//                        u.setProfessional(ss[4]);
//                        u.setMobile(ss[5]);
//                        u.setEmail(ss[6]);
                        StudentExpansion leaderStu = new StudentExpansion(u);
//                        if(StringUtil.isNotEmpty(ss[7]) && StringUtil.isNumeric(ss[7])){
//                            leaderStu.setEnterdate(DateUtil.formatToDate(DateUtil.FMT_YYYYMM_ZG, DateUtil.formatToDstr(ss[7], "9")));
//                        }
//                        if(StringUtil.isNotEmpty(ss[8]) && StringUtil.isNumeric(ss[8])){
//                            leaderStu.setGraduation(DateUtil.formatToDate(DateUtil.FMT_YYYYMM_ZG, DateUtil.formatToDstr(ss[8], "6")));
//                        }
                        studentErrorService.saveStudent(leaderStu);
                    }
                    ret.add(u.getId());
                }
                return ret;
            }
        }else{
            list = StringUtil.splitByPF(mems, impVo.getReqParam().getPrefix(), impVo.getReqParam().getPostfix());
            if (StringUtil.checkNotEmpty(list)) {
                List<String> ret=new ArrayList<String>();
                for(String[] ss:list) {
                    User u = userService.getByNo(ss[1]);
                    if((u == null) && StringUtil.isNotEmpty(ss[0]) && StringUtil.isNotEmpty(ss[5])){
                        u = userService.getUserByMobileAndName(ss[0], ss[5]);
                    }
                    if (u==null) {
                        u=new User();
                        u.setName(ss[0]);
                        u.setNo(ss[1]);
                        u.setArea(ss[2]);
                        u.setSchool(ss[3]);
                        u.setProfessional(ss[4]);
                        u.setMobile(ss[5]);
                        u.setEmail(ss[6]);
                        StudentExpansion leaderStu = new StudentExpansion(u);
                        if(StringUtil.isNotEmpty(ss[7]) && StringUtil.isNumeric(ss[7])){
                            leaderStu.setEnterdate(DateUtil.formatToDate(DateUtil.FMT_YYYYMM_ZG, DateUtil.formatToDstr(ss[7], "9")));
                        }
                        if(StringUtil.isNotEmpty(ss[8]) && StringUtil.isNumeric(ss[8])){
                            leaderStu.setGraduation(DateUtil.formatToDate(DateUtil.FMT_YYYYMM_ZG, DateUtil.formatToDstr(ss[8], "6")));
                        }
                        studentErrorService.saveStudent(leaderStu);
                    }
                    ret.add(u.getId());
                }
                return ret;
            }
        }
		return null;
	}
}
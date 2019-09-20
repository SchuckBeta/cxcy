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

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.tool.process.vo.FormTheme;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.DictService;
import com.oseasy.com.pcore.modules.sys.service.OfficeService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pie.modules.impdata.dao.ProModelGcontestErrorDao;
import com.oseasy.pie.modules.impdata.entity.PmgMemsError;
import com.oseasy.pie.modules.impdata.entity.PmgTeasError;
import com.oseasy.pie.modules.impdata.entity.ProModelGcontestError;
import com.oseasy.pie.modules.impdata.tool.IitCheckService;
import com.oseasy.pro.common.service.CommonService;
import com.oseasy.pro.modules.excellent.entity.ExcellentShow;
import com.oseasy.pro.modules.excellent.service.ExcellentShowService;
import com.oseasy.pro.modules.promodel.dao.ProModelDao;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.pro.modules.promodel.tool.oper.ItOper;
import com.oseasy.pro.modules.workflow.entity.ProModelGzsmxx;
import com.oseasy.pro.modules.workflow.service.ProModelGzsmxxService;
import com.oseasy.pro.modules.workflow.service.ProModelMdGcService;
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
import com.oseasy.util.common.utils.SpSteel;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 自定义大赛信息错误数据Service.
 * @author 自定义大赛信息错误数据
 * @version 2018-05-14
 */
@Service
@Transactional(readOnly = true)
public class ProModelGcontestErrorService extends CrudService<ProModelGcontestErrorDao, ProModelGcontestError> implements IitCheckService{
	@Autowired
	private PmgMemsErrorService pmgMemsErrorService;
	@Autowired
	private PmgTeasErrorService pmgTeasErrorService;
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
	private ProModelService proModelService;
	@Autowired
	private OfficeService officeService;

	@Autowired
	private BackTeacherExpansionDao backTeacherExpansionDao;
	@Autowired
	private TeamUserRelationDao teamUserRelationDao;
	@Autowired
	private CommonService commonService;
	@Autowired
	private UserService userService;
	@Autowired
	private DictService dictService;
    @Autowired
    private PieSysAttachmentService pieSysAttachmentService;
	@Autowired
	private  SysNumberRuleService sysNumberRuleService;

	@Autowired
	private  ProModelMdGcService proModelMdGcService;
	@Autowired
	private  ProModelGzsmxxService proModelGzsmxxService;
	@Autowired
	private  ExcellentShowService excellentShowService;

	public List<Map<String, String>> getMapByImpId(String impid) {
		return dao.getMapByImpId(impid);
	}
	public List<ProModelGcontestError> getListByImpId(String impid) {
	    return dao.getListByImpId(impid);
	}
	@Transactional(readOnly = false)
	public void insert(ProModelGcontestError projectError) {
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())) {
			projectError.setUpdateBy(user);
			projectError.setCreateBy(user);
		}
		projectError.setUpdateDate(new Date());
		projectError.setCreateDate(projectError.getUpdateDate());
		dao.insert(projectError);
		if(projectError.getPmes().size()>0){
			for(PmgMemsError p:projectError.getPmes()){
				pmgMemsErrorService.insert(p);
			}
		}
		if(projectError.getPtes().size()>0){
			for(PmgTeasError p:projectError.getPtes()){
				pmgTeasErrorService.insert(p);
			}
		}
	}
	public ProModelGcontestError get(String id) {
		return super.get(id);
	}

	public List<ProModelGcontestError> findList(ProModelGcontestError proModelGcontestError) {
		return super.findList(proModelGcontestError);
	}

	public Page<ProModelGcontestError> findPage(Page<ProModelGcontestError> page, ProModelGcontestError proModelGcontestError) {
		return super.findPage(page, proModelGcontestError);
	}

	@Transactional(readOnly = false)
	public void save(ProModelGcontestError proModelGcontestError) {
		super.save(proModelGcontestError);
	}

	@Transactional(readOnly = false)
	public void delete(ProModelGcontestError proModelGcontestError) {
		super.delete(proModelGcontestError);
	}

    @Transactional(readOnly = false)
    public void updatePL(List<ProModelGcontestError> entitys) {
      dao.updatePL(entitys);
    }

  	@Transactional(readOnly = false)
  	public void deleteWL(ProModelGcontestError proModelGcontestError) {
  	  dao.deleteWL(proModelGcontestError);
  	}
  	private void setEnterYear(StudentExpansion s,PmgMemsError pe) throws ParseException{
  		if(StringUtil.isNotEmpty(pe.getEnter())){
  			s.setEnterdate(DateUtil.parseDate(pe.getEnter()+"-09-01", "yyyy-MM-dd"));
  		}
  		if(StringUtil.isNotEmpty(pe.getEnter())&&StringUtil.isNotEmpty(pe.getOut())){
  			int y=Integer.parseInt(pe.getOut())-Integer.parseInt(pe.getEnter());
  			Dict d=DictUtils.getDictByLabel("0000000262", y+"");
  			if(d==null){
  				d=new Dict();
  				d.setType("0000000262");
  				d.setLabel(y+"");
  				d.setValue(IdUtils.getDictNumberByDb());
  				d.setParentId("7ee814c754ba432982a40eb5f8468d90");
  				dictService.save(d);
  			}
  			s.setCycle(d.getValue());
  		}
  	}
  	private void setEnterYear(StudentExpansion s,ProModelGcontestError pe) throws ParseException{
  		if(StringUtil.isNotEmpty(pe.getEnter())){
  			s.setEnterdate(DateUtil.parseDate(pe.getEnter()+"-09-01", "yyyy-MM-dd"));
  		}
  		if(StringUtil.isNotEmpty(pe.getEnter())&&StringUtil.isNotEmpty(pe.getOut())){
  			int y=Integer.parseInt(pe.getOut())-Integer.parseInt(pe.getEnter());
  			Dict d=DictUtils.getDictByLabel("0000000262", y+"");
  			if(d==null){
  				d=new Dict();
  				d.setType("0000000262");
  				d.setLabel(y+"");
  				d.setValue(IdUtils.getDictNumberByDb());
  				d.setParentId("7ee814c754ba432982a40eb5f8468d90");
  				d.setIsSys("0");
  				dictService.save(d);
  			}
  			s.setCycle(d.getValue());
  		}
  	}
  	@Transactional(readOnly = false)
	public void updateProModelGcontest(ProModelGcontestError pmer, ProModel pm, ActYw ay, ItOper impVo) throws Exception {
  	    if(pm == null){
  	        return;
  	    }
  	    ProModel proModel = ProModelGcontestError.convertToProModel(pm, pmer);
  	    proModelService.save(ProModelGcontestError.convertToProModel(pm, pmer));
        userService.updateUser(ProModelGcontestError.convertToUser(pm.getDeuser(), pmer));
        /**
         * 判断是否有附件，如果有附件处理附件.
         */
        if(impVo.getHasFile()){
            proModel.setPName(SpSteel.replaceAll(proModel.getPName(), DictUtils.getDictVsByType(SpSteel.SP_STEEL_DKEY)));
            pieSysAttachmentService.saveFileForImp(proModel, ay, FileTypeEnum.S11,FileStepEnum.S1102);
        }
  	}
    @Transactional(readOnly = false)
    public void saveProModelGcontest(ProModelGcontestError pe,ActYw ay, ItOper impVo) throws Exception {
  		String pnum=IdUtils.getGContestNumberByDb();
		SysNumberRule sysNumberRule=sysNumberRuleService.getRuleByAppType(ay.getId(),"");
		if(sysNumberRule!=null){
			// TODO: 2018/8/8 0008 陈浩改后期模板 添加year必填项
			try {
				String year= DateUtil.getYear();
				if(pe.getYear()!=null){
					year=pe.getYear();
				}
				pnum = NumRuleUtils.getNumberText(ay.getId(),year);
			}catch(Exception e){
				pnum=IdUtils.getProjectNumberByDb();
			}
		}

		//负责人
		User leader=userService.getByNo(pe.getNo());
		Office p=officeService.get(pe.getProfes());
		if (leader==null) {
			leader=new User();
			StudentExpansion leaderStu=new StudentExpansion();

			leader.setOffice(new Office(p.getParentId()));
			leader.setProfessional(pe.getProfes());
			leader.setName(pe.getLeader());
			leader.setNo(pe.getNo());
			leader.setUserType(RoleBizTypeEnum.XS.getValue());
			setEnterYear(leaderStu, pe);
			leader.setEducation(pe.getXueli());
			leader.setIdType("1");
			leader.setIdNumber(pe.getIdnum());
			leader.setMobile(pe.getMobile());
			leader.setEmail(pe.getEmail());
			leaderStu.setUser(leader);
			studentErrorService.saveStudent(leaderStu);
		}
		//处理团队成员
		List<String> mems=getMems(pe.getPmes());
		//处理导师
		List<String> teas=getTeas(pe.getPtes());
		/*保存团队信息*/
		Team team=new Team();
		team.setId(IdGen.uuid());
		team.setName("大赛"+pnum+"的团队");
		team.setSponsor(leader.getId());
		team.setState("1");
		team.setNumber(IdUtils.getTeamNumberByDb());
		team.setLocalCollege(p.getParentId());
		team.setProjectName(pe.getName());
		int member_num=1;//成员人数
		int enterprise_teacher_num=0;//企业导师人数
		int school_teacher_num=0;//校园导师人数
		if(teas!=null){
			for(String teaid:teas) {
				BackTeacherExpansion tea=backTeacherExpansionDao.findTeacherByUserId(teaid);
				/*保存团队导师信息*/
				TeamUserRelation teamur=new TeamUserRelation();
				teamur.setId(IdGen.uuid());
				teamur.setTeamId(team.getId());
				teamur.setUser(UserUtils.get(teaid));
				teamur.setUpdateBy(leader);
				teamur.setCreateBy(leader);
				teamur.setUserType(RoleBizTypeEnum.DS.getValue());
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
		teamurLeader.setUserType(RoleBizTypeEnum.XS.getValue());
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
				teamur.setUserType(RoleBizTypeEnum.XS.getValue());
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
		/*保存大赛信息*/
		ProModel pd=new ProModel();
		pd.setId(IdGen.uuid());
		pd.setTeamId(team.getId());
		pd.setUpdateBy(leader);
		pd.setCreateBy(leader);
		pd.setUpdateDate(new Date());
		pd.setCreateDate(pd.getUpdateDate());
		pd.setSubStatus(Const.NO);
		pd.setCompetitionNumber(pnum);
		pd.setPName(pe.getName());
		pd.setDeclareId(leader.getId());
        pd.setImpdata(Const.YES);
        pd.setSubStatus(Const.YES);
        if(impVo.getIsImpFirstData()){
            pd.setState(Const.NO);
        }else{
            pd.setState(Const.YES);
        }
		pd.setYear(pe.getYear());
		pd.setProCategory(pe.getType());
		pd.setLevel(pe.getGroups());
		pd.setActYwId(ay.getId());
		pd.setProType(ay.getProProject().getProType());
		pd.setType(ay.getProProject().getType());
		pd.setYear(DateUtil.getYear());
		pd.setIntroduction(pe.getIntroduction());
		pd.setRemarks(pe.getIntroduction());
		pd.setStage(pe.getStage());
		proModelDao.insert(pd);
		commonService.copyTeamUserHistoryFromTUR(ay.getId(), pd.getTeamId(), pd.getId(), "0",pd.getYear());
        excellentShowService.saveExcellentShow(pd.getIntroduction(), pd.getTeamId(),ExcellentShow.Type_Gcontest,pd.getId(),ay.getId());//保存优秀展示
		//根据gnodeId判断是否为最后一步导入。 没有gnodeId为最后一步导入
		if(StringUtil.isEmpty(impVo.getReqParam().getGnodeId())){
			// 标志流程结束
			pd.setState("1");
			//团队完成记录
			teamUserHistoryService.updateFinishAsClose(pd.getId());
			proModelDao.update(pd);
		}
        /**
         * 拓展表处理.
         */
		if((ay.getFtheme() != null)){
		    if((FormTheme.F_MD_GC).equals(ay.getFtheme())){
		        proModelMdGcService.saveAddPro(pd);
            }else if((FormTheme.F_GZSMXX).equals(ay.getFptype())){
                ProModelGzsmxx curProModelGzsmxx = proModelGzsmxxService.getByProModelId(pd.getId());
                if(curProModelGzsmxx == null){
                    ProModelGzsmxx proModelGzsmxx = new ProModelGzsmxx();
                    logger.warn("桂子山梦想秀的 领域和领域分组 字段为空！");
                    proModelGzsmxx.setModelId(pd.getId());
                    proModelGzsmxxService.save(proModelGzsmxx);
                }else{
                    proModelGzsmxxService.save(curProModelGzsmxx);
                }
            }else{
                logger.warn("通用大赛！");
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
	private List<String> getTeas(List<PmgTeasError> teas) {
		if (teas!=null&&teas.size()>0) {
			List<String> ret=new ArrayList<String>();
			for(PmgTeasError pe:teas) {
				User leader=userService.getByNo(pe.getNo());
				if (leader==null) {
					leader=new User();
					BackTeacherExpansion leaderStu=new BackTeacherExpansion();
					leaderStu.setTeachertype(TeacherType.TY_XY.getKey());
					leader.setOffice(new Office(pe.getOffice()));
					leader.setName(pe.getName());
					leader.setNo(pe.getNo());
					leader.setMobile(pe.getMobile());
					leader.setEmail(pe.getEmail());
					leaderStu.setTechnicalTitle(pe.getZhicheng());
					leaderStu.setUser(leader);
					teacherErrorService.saveTeacher(leaderStu);
				}
				ret.add(leader.getId());
			}
			return ret;
		}else{
			return null;
		}
	}
	private List<String> getMems(List<PmgMemsError> mems) throws ParseException {
		if (mems!=null&&mems.size()>0) {
			List<String> ret=new ArrayList<String>();
			for(PmgMemsError pe:mems) {
				User leader=userService.getByNo(pe.getNo());
				if (leader==null) {
					leader=new User();
					StudentExpansion leaderStu=new StudentExpansion();
					Office p=officeService.get(pe.getProfes());
					leader.setOffice(new Office(p.getParentId()));
					leader.setProfessional(pe.getProfes());
					leader.setName(pe.getName());
					leader.setNo(pe.getNo());
					setEnterYear(leaderStu, pe);
					leader.setEducation(pe.getXueli());
					leader.setIdType("1");
					leader.setIdNumber(pe.getIdnum());
					leader.setMobile(pe.getMobile());
					leader.setEmail(pe.getEmail());
					leaderStu.setUser(leader);
					studentErrorService.saveStudent(leaderStu);
				}
				ret.add(leader.getId());
			}
			return ret;
		}else{
			return null;
		}
	}
}
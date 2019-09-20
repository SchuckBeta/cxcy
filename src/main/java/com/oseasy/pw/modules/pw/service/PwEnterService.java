package com.oseasy.pw.modules.pw.service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowProjectType;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.fileserver.common.vsftp.VsftpUtils;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.fileserver.modules.attachment.vo.SysAttachmentVo;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotifyRecord;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.com.mqserver.modules.oa.vo.OaNotifySendType;
import com.oseasy.com.mqserver.modules.oa.vo.OaNotifyTypeStatus;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.config.CoreSval.PassNot;
import com.oseasy.com.pcore.common.mapper.JsonMapper;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.pcore.modules.sys.vo.SysNoType;
import com.oseasy.com.pcore.modules.sys.vo.SysNodeTool;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.pw.common.config.RoomUseStatus;
import com.oseasy.pw.modules.pw.dao.PwEnterDao;
import com.oseasy.pw.modules.pw.entity.PwApplyRecord;
import com.oseasy.pw.modules.pw.entity.PwCompany;
import com.oseasy.pw.modules.pw.entity.PwEnter;
import com.oseasy.pw.modules.pw.entity.PwEnterDetail;
import com.oseasy.pw.modules.pw.entity.PwEnterRoom;
import com.oseasy.pw.modules.pw.entity.PwProject;
import com.oseasy.pw.modules.pw.vo.DtypeTerm;
import com.oseasy.pw.modules.pw.vo.PwEnterAtype;
import com.oseasy.pw.modules.pw.vo.PwEnterAuditEnum;
import com.oseasy.pw.modules.pw.vo.PwEnterBgremarks;
import com.oseasy.pw.modules.pw.vo.PwEnterEvo;
import com.oseasy.pw.modules.pw.vo.PwEnterInfo;
import com.oseasy.pw.modules.pw.vo.PwEnterRoomVo;
import com.oseasy.pw.modules.pw.vo.PwEnterStatus;
import com.oseasy.pw.modules.pw.vo.PwEnterType;
import com.oseasy.pw.modules.pw.vo.PwEroomParam;
import com.oseasy.pw.modules.pw.vo.PwEroomStatus;
import com.oseasy.pw.modules.pw.vo.SvalPw;
import com.oseasy.pw.modules.pw.vo.TempTypeEnum;
import com.oseasy.sys.common.config.SysSval;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.service.BackTeacherExpansionService;
import com.oseasy.sys.modules.sys.vo.TeacherType;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.entity.TeamUserHistory;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.sys.modules.team.service.TeamUserChangeService;
import com.oseasy.sys.modules.team.service.TeamUserHistoryService;
import com.oseasy.sys.modules.team.service.TeamUserRelationService;
import com.oseasy.sys.modules.team.vo.TeamStateType;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.DateUtil.Dtype;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.json.JsonAliUtils;

/**
 * 入驻申报Service.
 *
 * @author chenh
 * @version 2017-11-26
 */
@Service
@Transactional(readOnly = true)
public class PwEnterService extends CrudService<PwEnterDao, PwEnter> {
	@Autowired
	private TeamService teamService;
	@Autowired
	private ProModelService proModelService;
	@Autowired
	private SysAttachmentService sysAttachmentService;
	@Autowired
	PwEnterRelService pwEnterRelService;
	@Autowired
	PwEnterDetailService pwEnterDetailService;
	@Autowired
	PwEnterRoomService pwEnterRoomService;
	@Autowired
	PwEnterXqhistoryService pwEnterXqhistoryService;
	@Autowired
	PwCompanyService pwCompanyService;
	@Autowired
	PwProjectService pwProjectService;
	@Autowired
	TeamUserHistoryService teamUserHistoryService;
	@Autowired
	TeamUserRelationService teamUserRelationService;
	@Autowired
	TeamUserChangeService teamUserChangeService;
	@Autowired
	OaNotifyService oaNotifyService;
	@Autowired
	PwApplyRecordService pwApplyRecordService;
    @Autowired
    BackTeacherExpansionService backTeacherExpansionService;
    @Autowired
    SystemService systemService;
    @Autowired
    UserService userService;
	/*******************************************************************************
     ***已处理的方法******************************************************************
     ******************************************************************************/
    public PwEnter get(String id) {
        return super.get(id);
    }

    /**
     * 查找所有的变更申请记录.
     * @param entitys
     * @param pwEnter
     * @return List
     */
    public List<PwEnter> findAllBgsqByg(List<PwEnter> entitys, PwEnter pwEnter){
        return findAllListByg(entitys, pwEnter, true, false, true, false, true, true, true);
    }

    /**
     * 查找所有的分配场地记录.
     * @param entitys
     * @param pwEnter
     * @return List
     */
    public List<PwEnter> findAllFpcdByg(List<PwEnter> entitys, PwEnter pwEnter){
        return findAllListByg(entitys, pwEnter, true, false, true, true, true, false, true);
    }

    /**
     * 查找所有的续期入驻记录.
     * @param entitys
     * @param pwEnter
     * @param hasRecord 是否根据类型取记录
     * @return List
     */
    public List<PwEnter> findAllXqrzByg(List<PwEnter> entitys, PwEnter pwEnter, Boolean hasRecord){
        if(hasRecord){
            PwApplyRecord applyRecord =  new PwApplyRecord();
            applyRecord.setType(PwEnterBgremarks.R3.getKey());
            pwEnter.setApplyRecord(applyRecord);
        }
        return findAllListByg(entitys, pwEnter, true, false, true, true, true, false, false);
    }

    /**
     * 查找所有的续期入驻记录.
     * @param entitys
     * @param pwEnter
     * @return List
     */
    public List<PwEnter> findAllXqrzByg(List<PwEnter> entitys, PwEnter pwEnter){
        return findAllXqrzByg(entitys, pwEnter, false);
    }

    /**
     * 查找所有的取消入驻记录.
     * @param entitys
     * @param pwEnter
     * @param hasRecord 是否根据类型取记录
     * @return List
     */
    public List<PwEnter> findAllQxrzByg(List<PwEnter> entitys, PwEnter pwEnter, Boolean hasRecord){
        if(hasRecord){
            PwApplyRecord applyRecord =  new PwApplyRecord();
            applyRecord.setType(PwEnterBgremarks.R4.getKey());
            pwEnter.setApplyRecord(applyRecord);
        }
        return findAllListByg(entitys, pwEnter, true, false, true, false, false, false, false);
    }
    /**
     * 查找所有的取消入驻记录.
     * @param entitys
     * @param pwEnter
     * @return List
     */
    public List<PwEnter> findAllQxrzByg(List<PwEnter> entitys, PwEnter pwEnter){
        return findAllQxrzByg(entitys, pwEnter, false);
    }

    /**
     * 查找所有的入驻已分配的房间记录.
     * @param entitys
     * @param pwEnter
     * @return List
     */
    public List<PwEnter> findAllotAllListByg(List<PwEnter> entitys, PwEnter pwEnter){
        return findAllListByg(entitys, pwEnter, true, false, true, true, false, false, false);
    }

    /**
     * 查找所有的入驻团队记录.
     * @param entitys
     * @param pwEnter
     * @return List
     */
    public List<PwEnter> findAllTeamListByg(List<PwEnter> entitys, PwEnter pwEnter){
        return findAllListByg(entitys, pwEnter, true, true, false, false, false, false, false);
    }

    /**
     * 查询列表.
     * @param entitys
     * @param pwEnter
     * @return List
     */
    public List<PwEnter> findAllQueryListByg(List<PwEnter> entitys, PwEnter pwEnter){
        return findAllListByg(entitys, pwEnter, true, true, true, true, false, false, false);
    }

    public List<PwEnter> findAllListByg(List<PwEnter> entitys, PwEnter pwEnter){
        return findAllListByg(entitys, pwEnter, true, true, true, false, false, false, false);
    }

    public List<PwEnter> findFrontListByg(List<PwEnter> entitys, PwEnter pwEnter){
        return findAllListByg(entitys, pwEnter, true, false, true, true, false, false, false);
    }


    public List<PwEnter> findAllListByg(List<PwEnter> entitys, PwEnter pwEnter, Boolean addTeam, Boolean addProject, Boolean addCompany, Boolean addRoom, Boolean addApply, Boolean addApplyRs, Boolean addApplyRmax){
        if(StringUtil.checkEmpty(entitys)){
            return entitys;
        }

        List<PwEnter> entityTeams = null;
        List<PwEnter> entityProjects = null;
        List<PwEnter> entityCompanys = null;
        List<PwEnter> entityRooms = null;
        List<PwEnter> entityApplys = null;
        List<PwEnter> entityApplyRs = null;
        List<PwEnter> entityApplyRmax = null;
        if(addTeam){
            entityTeams = dao.findAllListTeamByg_NoCount(pwEnter);
        }
        if(addProject){
            entityProjects = dao.findAllListProjectByg_NoCount(pwEnter);
        }
        if(addCompany){
            entityCompanys = dao.findAllListCompanyByg_NoCount(pwEnter);
        }
        if(addRoom){
            entityRooms = dao.findAllListRoomByg_NoCount(pwEnter);
        }
        if(addApply){
            entityApplys = dao.findAllListApplyByg_NoCount(pwEnter);
        }
        if(addApplyRs){
            entityApplyRs = dao.findAllListApplyRsByg_NoCount(pwEnter);
        }
        if(addApplyRmax){
            entityApplyRmax = dao.findAllListApplyRmaxByg_NoCount(pwEnter);
        }

        for (PwEnter cur : entitys) {
            if(addTeam && StringUtil.checkNotEmpty(entityTeams)){
                for (PwEnter curu : entityTeams) {
                    if((cur.getId()).equals(curu.getId())){
                        cur.setEteam(curu.getEteam());
                        break;
                    }
                }
            }

            if(addProject && StringUtil.checkNotEmpty(entityProjects)){
                for (PwEnter curr : entityProjects) {
                    if((cur.getId()).equals(curr.getId())){
                        cur.setEprojects(curr.getEprojects());
                        break;
                    }
                }
            }

            if(addCompany && StringUtil.checkNotEmpty(entityCompanys)){
                for (PwEnter curs : entityCompanys) {
                    if((cur.getId()).equals(curs.getId())){
                        cur.setEcompany(curs.getEcompany());
                        break;
                    }
                }
            }

            if(addRoom && StringUtil.checkNotEmpty(entityRooms)){
                for (PwEnter curf : entityRooms) {
                    if((cur.getId()).equals(curf.getId())){
                        cur.setErooms(curf.getErooms());
                        break;
                    }
                }
            }

            if(addApply && StringUtil.checkNotEmpty(entityApplys)){
                for (PwEnter curc : entityApplys) {
                    if((cur.getId()).equals(curc.getId())){
                        cur.setApply(curc.getApply());
                        break;
                    }
                }
            }

            if(addApplyRs && StringUtil.checkNotEmpty(entityApplyRs)){
                for (PwEnter curc : entityApplyRs) {
                    if((cur.getId()).equals(curc.getId())){
                        cur.setApplyRecords(curc.getApplyRecords());
                        break;
                    }
                }
            }

            if(addApplyRmax && StringUtil.checkNotEmpty(entityApplyRmax)){
                for (PwEnter curc : entityApplyRmax) {
                    if((cur.getId()).equals(curc.getId())){
                        cur.setApplyRecord(curc.getApplyRecord());
                        break;
                    }
                }
            }
        }
        return entitys;
    }

    /**
     * 获取入驻项目、团队、企业.
     * @param pwEnter
     * @return
     */
    public PwEnter getByGroup(PwEnter pwEnter) {
        if(pwEnter != null && StringUtil.isNotEmpty(pwEnter.getId())){
            List<PwEnter> entitys =  findAllListByg(Arrays.asList(new PwEnter[]{dao.get(pwEnter)}), pwEnter);
            if(StringUtil.checkUnion(entitys)){
                return entitys.get(0);
            }
         }
         return null;
    }

    /**
     * 获取入驻项目.
     * @param pwEnter
     * @return
     */
    public PwEnter getProjects(PwEnter pwEnter) {
        if(pwEnter != null && StringUtil.isNotEmpty(pwEnter.getId())){
           List<PwEnter> entitys =  dao.findAllListProjectByg(pwEnter);
           if(StringUtil.checkUnion(entitys)){
               return entitys.get(0);
           }
        }
        return null;
    }

    /**
     * 获取入驻企业.
     * @param pwEnter
     * @return
     */
    public PwEnter getCompany(PwEnter pwEnter) {
        if(pwEnter != null && StringUtil.isNotEmpty(pwEnter.getId())){
            List<PwEnter> entitys =  dao.findAllListCompanyByg(pwEnter);
            if(StringUtil.checkUnion(entitys)){
                return entitys.get(0);
            }
        }
        return null;
    }

    /**
     * 获取入驻场地.
     * @param pwEnter
     * @return
     */
    public PwEnter getRoom(PwEnter pwEnter) {
        if(pwEnter != null && StringUtil.isNotEmpty(pwEnter.getId())){
            List<PwEnter> entitys =  dao.findAllListRoomByg(pwEnter);
            if(StringUtil.checkUnion(entitys)){
                return entitys.get(0);
            }
        }
        return null;
    }

    /**
     * 获取入驻详情.
     * @param pwEnter
     * @return
     */
    public PwEnter getTeam(PwEnter pwEnter) {
        if(pwEnter != null && StringUtil.isNotEmpty(pwEnter.getId())){
            List<PwEnter> entitys =  dao.findAllListTeamByg(pwEnter);
            if(StringUtil.checkUnion(entitys)){
                return entitys.get(0);
            }
        }
        return null;
    }

    /**
     * 获取入驻申报.
     * @param pwEnter
     * @return
     */
    public PwEnter getApply(PwEnter pwEnter) {
        if(pwEnter != null && StringUtil.isNotEmpty(pwEnter.getId())){
            List<PwEnter> entitys =  dao.findAllListApplyByg(pwEnter);
            if(StringUtil.checkUnion(entitys)){
                return entitys.get(0);
            }
        }
        return null;
    }

    public List<PwEnter> findListBgsqByGroup(PwEnter pwEnter) {
        return findAllBgsqByg(dao.findListKeys(pwEnter), pwEnter);
    }
    public Page<PwEnter> findPageBgsqByGroup(Page<PwEnter> page, PwEnter pwEnter) {
        pwEnter.setPage(page);
        page.setList(findListBgsqByGroup(pwEnter));
        return page;
    }

    public List<PwEnter> findListFpcdByGroup(PwEnter pwEnter) {
        return findAllFpcdByg(dao.findListKeys(pwEnter), pwEnter);
    }
    public Page<PwEnter> findPageFpcdByGroup(Page<PwEnter> page, PwEnter pwEnter) {
        pwEnter.setPage(page);
        page.setList(findListFpcdByGroup(pwEnter));
        return page;
    }

    public Page<PwEnter> findTeamPageByGroup(Page<PwEnter> page, PwEnter pwEnter) {
        pwEnter.setPage(page);
        page.setList(findTeamListByGroup(pwEnter));
        return page;
    }

    public Page<PwEnter> findQueryPageByGroup(Page<PwEnter> page, PwEnter pwEnter) {
        pwEnter.setPage(page);
        page.setList(findQueryListByGroup(pwEnter));
        return page;
    }
    public List<PwEnter> findQueryListByGroup(PwEnter pwEnter) {
        return findAllQueryListByg(dao.findListKeys(pwEnter), pwEnter);
    }
    public List<PwEnter> findTeamListByGroup(PwEnter pwEnter) {
        return findAllTeamListByg(dao.findListKeys(pwEnter), pwEnter);
    }

    /**
     * 已分配房间查询.
     * @param page
     * @param pwEnter
     * @return
     */
    public Page<PwEnter> findAllotPageByGroup(Page<PwEnter> page, PwEnter pwEnter,String queryType) {
        pwEnter.setPage(page);
        List<PwEnter> list = findAllotListByGroup(pwEnter,queryType);
        page.setList(list);
        return page;
    }
    public List<PwEnter> findAllotListByGroup(PwEnter pwEnter,String queryType) {
        if(PwRoomService.NOWDATA.equals(queryType)){
            List<PwEnter> list = dao.findListRoomKeys(pwEnter);
            for(PwEnter pwE : list){
                pwE.setQuerystatus(RoomUseStatus.USED.getStatus());
            }
            return findAllotAllListByg(list, pwEnter);
        }else if((PwRoomService.HISTORYDATA.equals(queryType))){
            List<PwEnter> list =dao.findListRoomYTFKeys(pwEnter);
            for(PwEnter e : list){
                e.setQuerystatus(RoomUseStatus.OTHER_ROOM.getStatus());
                if(e.getIsTF() >0){
                    e.setQuerystatus(RoomUseStatus.EXITROOM.getStatus());
                }
                if(e.getIsYGH() > 0){
                    e.setQuerystatus(RoomUseStatus.CHANGED_ROOM.getStatus());
                }
            }
            return findAllotAllListByg(list, pwEnter);
        }
        return findAllotAllListByg(null, pwEnter);
    }

    /**
     * 续期入驻.
     * @param pwEnter
     * @return List
     */
    public List<PwEnter> findListXqrzByGroup(PwEnter pwEnter) {
        return findListXqrzByGroup(pwEnter, false);
    }
    public List<PwEnter> findListXqrzByGroup(PwEnter pwEnter, Boolean hasRecored) {
        return findAllXqrzByg(dao.findListKeys(pwEnter), pwEnter, hasRecored);
    }
    public Page<PwEnter> findPageXqrzByGroup(Page<PwEnter> page, PwEnter pwEnter) {
        return findPageXqrzByGroup(page, pwEnter, false);
    }
    public Page<PwEnter> findPageXqrzByGroup(Page<PwEnter> page, PwEnter pwEnter, Boolean hasRecored) {
        pwEnter.setPage(page);
        page.setList(findListXqrzByGroup(pwEnter, hasRecored));
        return page;
    }

    public List<PwEnter> findListXqrzRecoredByGroup(PwEnter pwEnter, Boolean hasRecored) {
        return findAllXqrzByg(dao.findAllListAppRecordRmaxByg(pwEnter), pwEnter, hasRecored);
    }
    public Page<PwEnter> findPageXqrzRecoredByGroup(Page<PwEnter> page, PwEnter pwEnter) {
        return findPageXqrzRecoredByGroup(page, pwEnter, false);
    }
    public Page<PwEnter> findPageXqrzRecoredByGroup(Page<PwEnter> page, PwEnter pwEnter, Boolean hasRecored) {
        pwEnter.setPage(page);
        page.setList(findListXqrzRecoredByGroup(pwEnter, hasRecored));
        return page;
    }

    /**
     * 取消入驻.
     * @param pwEnter
     * @return List
     */
    public List<PwEnter> findListQxrzByGroup(PwEnter pwEnter) {
        return findListQxrzByGroup(pwEnter, false);
    }
    public List<PwEnter> findListQxrzByGroup(PwEnter pwEnter, Boolean hasRecored) {
        return findAllQxrzByg(dao.findListKeys(pwEnter), pwEnter, hasRecored);
    }
    public Page<PwEnter> findPageQxrzByGroup(Page<PwEnter> page, PwEnter pwEnter) {
        return findPageQxrzByGroup(page, pwEnter, false);
    }
    public Page<PwEnter> findPageQxrzByGroup(Page<PwEnter> page, PwEnter pwEnter, Boolean hasRecored) {
        pwEnter.setPage(page);
        page.setList(findListQxrzByGroup(pwEnter, hasRecored));
        return page;
    }

    public List<PwEnter> findListQxrzRecoredByGroup(PwEnter pwEnter, Boolean hasRecored) {
        return findAllQxrzByg(dao.findAllListAppRecordRmaxByg(pwEnter), pwEnter, hasRecored);
    }
    public Page<PwEnter> findListQxrzRecoredByGroup(Page<PwEnter> page, PwEnter pwEnter) {
        return findPageQxrzRecoredByGroup(page, pwEnter, false);
    }
    public Page<PwEnter> findPageQxrzRecoredByGroup(Page<PwEnter> page, PwEnter pwEnter, Boolean hasRecored) {
        pwEnter.setPage(page);
        page.setList(findListQxrzRecoredByGroup(pwEnter, hasRecored));
        return page;
    }

    /*******************************************************************************
     ***待处理的方法******************************************************************
     ******************************************************************************/
	@Transactional(readOnly = false)
	public Long getCountToAudit() {
		Subject s=SecurityUtils.getSubject();
    	if(s!=null){
    		PwEnter p=new PwEnter();
    		p.setStatus(PwEnterStatus.PES_DSH.getKey());
    		Long c=dao.getCount(p);
    		return c==null?0L:c;
    	}else{
    		return 0L;
    	}
	}

	@Transactional(readOnly = false)
	public Long getCountToFPCD() {
	    Subject s=SecurityUtils.getSubject();
	    if(s!=null){
	        PwEnter p=new PwEnter();
//	        p.setRestatus(PwEroomStatus.PER_DFP.getKey());
	        p.setPrestatuss(Arrays.asList(new String[]{PwEroomStatus.PER_DBGFP.getKey(), PwEroomStatus.PER_DFP.getKey()}));
            p.setPstatus(PwEnterStatus.getKeyByFPCD());
	        Long c=dao.getCountRstatus(p);
	        return c==null?0L:c;
	    }else{
	        return 0L;
	    }
	}

    @Transactional(readOnly = false)
    public Long getCountToBGSH() {
        Subject s=SecurityUtils.getSubject();
        if(s!=null){
            PwEnter p=new PwEnter();
            p.setAppType(PwEnterAtype.PAT_BGSQ.getKey());
            p.setStatus(PwEnterStatus.PES_DSH.getKey());
            Long c=dao.getCountBGSH(p);
            return c==null?0L:c;
        }else{
            return 0L;
        }
    }

	public List<PwEnter> findList(PwEnter pwEnter) {
		return super.findList(pwEnter);
	}

	public Page<PwEnter> findPage(Page<PwEnter> page, PwEnter pwEnter) {
		return super.findPage(page, pwEnter);
	}

	public List<PwEnter> findListByGroup(PwEnter pwEnter) {
		return findAllListByg(dao.findListKeys(pwEnter), pwEnter);
	}

    public List<PwEnter> findFrontListByGroup(PwEnter pwEnter) {
        return findFrontListByg(dao.findFrontList(pwEnter), pwEnter);
    }

    public Page<PwEnter> findFrontPageByGroup(Page<PwEnter> page, PwEnter pwEnter) {
        pwEnter.setPage(page);
        page.setList(findFrontListByGroup(pwEnter));
        return page;
    }

	public Page<PwEnter> findPageByGroup(Page<PwEnter> page, PwEnter pwEnter) {
		pwEnter.setPage(page);
		page.setList(findListByGroup(pwEnter));
		return page;
	}

	@Transactional(readOnly = false)
	public void save(PwEnter pwEnter) {
		if (pwEnter.getIsNewRecord()) {
			if (StringUtil.isEmpty(pwEnter.getNo())) {
				pwEnter.setNo(SysNodeTool.genByKeyss(SysNoType.NO_PW_ER));
			}
			if (StringUtil.isEmpty(pwEnter.getIsCopy())) {
			    pwEnter.setIsCopy(Const.NO);
			}
			if (StringUtil.isEmpty(pwEnter.getIsTemp())) {
			    pwEnter.setIsTemp(Const.YES);
			}
			if ((pwEnter.getTermNum() == null)) {
			    pwEnter.setTermNum(0);
			}
		}
		//将临时用户图片挪到正式图片地址
		if(StringUtil.isNotEmpty(pwEnter.getDeclarePhoto())) {
        String newUrl = null;
        newUrl = VsftpUtils.moveFile(pwEnter.getDeclarePhoto());
        if (StringUtil.isNotEmpty(newUrl)) {
            pwEnter.setDeclarePhoto(newUrl);
        }
    }
      super.save(pwEnter);
	}

	@Transactional(readOnly = false)
	public void delete(PwEnter pwEnter) {
		super.delete(pwEnter);
	}

	@Transactional(readOnly = false)
	public void deleteApply(PwEnter pwEnter) {
		dao.delete(pwEnter);// 删除入驻
	}

	/**
	 * 入驻审核.
	 * @param id 审核ID
	 * @param atype 审核结果类型
	 * @param term 周期
	 * @param remarks 说明
	 * @return ActYwRstatus
	 */
	@Transactional(readOnly = false)
	public ApiTstatus<PwEnter> auditEnter(String id, String atype, Integer term, String remarks) {
		if (StringUtil.isEmpty(id)) {
			return new ApiTstatus<PwEnter>(false, "入驻申请ID不能为空");
		}

		if (StringUtil.isEmpty(atype)) {
			return new ApiTstatus<PwEnter>(false, "入驻申请审核类型不能为空");
		}

		if ((PassNot.PASS.getKey()).equals(atype) && (term == null)) {
			return new ApiTstatus<PwEnter>(false, "入驻有效期不能为空");
		}

		if ((PassNot.NOT.getKey()).equals(atype) && StringUtil.isEmpty(remarks)) {
			return new ApiTstatus<PwEnter>(false, "入驻申请审核拒绝通过需要说明原因");
		}

		PwEnter per = getApply(new PwEnter(id));
		if (per == null) {
			return new ApiTstatus<PwEnter>(false, "入驻申请不存在,ID = " + id);
		}

        return saveEnterByDFP(per, atype, term, remarks);
	}

	/**
	 * 入驻审核-待分配.
	 *
	 * @param per
	 *            入驻
	 * @param atype
	 *            审核结果类型
	 * @return ActYwRstatus
	 */
	@Transactional(readOnly = false)
	public ApiTstatus<PwEnter> saveEnterByDFP(PwEnter per, String atype, Integer term, String remarks) {
		if ((PassNot.PASS.getKey()).equals(atype)) {
			per.setStatus(PwEnterStatus.PES_RZCG.getKey());
			per.setStartDate(DateUtil.getCurDateYMD000());

			/**
			 * 天数转换.
			 */
			Dtype dtype = Dtype.getByNum(term);
			if (dtype != null) {
				per.setTerm(DateUtil.addDayByType(dtype, 1, per.getStartDate()));
			}else{
	            per.setTerm(term);
			}
			per.setEndDate(DateUtil.getCurDateYMD000(DateUtil.addDay(per.getStartDate(), per.getTerm())));
		} else {
			per.setStatus(PwEnterStatus.PES_RZSB.getKey());
		}
		if (StringUtil.isNotEmpty(remarks)) {
			per.setRemarks(remarks);
		}
		save(per);
		if((PwEnterType.PET_TEAM.getKey()).equals(per.getType())){
	        pwApplyRecordService.auditRecord(per.getId(), PwEnterBgremarks.R1.getKey(), atype, per.getRemarks());
		}else if((PwEnterType.PET_QY.getKey()).equals(per.getType())){
	        pwApplyRecordService.auditRecord(per.getId(), PwEnterBgremarks.R2.getKey(), atype, per.getRemarks());
		}
		return new ApiTstatus<PwEnter>(true, "入驻团队申请审核完成", per);
	}

	/**
	 * 入驻-即将到期状态变更（定时任务专用）.
	 * @return ActYwRstatus
	 */
	@Transactional(readOnly = false)
	public ApiTstatus<PwEnterEvo> enterAllByExpire(Date curDate) {
		PwEnterEvo pwEnterEvo = new PwEnterEvo();
		PwEnter pwEnter = new PwEnter();
		/**
		 * 获取即将到期的符合状态.
		 */
        pwEnter.setPstatus(PwEnterStatus.getKeyByJobExpire());
		List<PwEnter> pwEnters = findListByGroup(pwEnter);
		pwEnterEvo.setTotalNum(pwEnters.size());
		for (PwEnter pwer : pwEnters) {
			pwEnterEvo = enterByExpire(pwer, pwEnterEvo, curDate);
		}

		pwEnterEvo.setLogFile(SvalPw.getEnterExpireLogFile() + StringUtil.LINE + SvalPw.getEnterExpireKey()
				+ DateUtil.formatDate(curDate, DateUtil.FMT_YYYY_MM_DD_HHmmss) + StringUtil.LOG);
		if (pwEnterEvo.getFailNum() > 0) {
			JsonAliUtils.writeFile(pwEnterEvo.getLogFile(), JsonMapper.toJsonString(pwEnterEvo));
		}
		return new ApiTstatus<PwEnterEvo>(true, "处理完成,总数量:[" + pwEnterEvo.getTotalNum() + "],成功数量:["
				+ pwEnterEvo.getSuccNum() + "],失败数量:[" + pwEnterEvo.getFailNum() + "]", pwEnterEvo);
	}

	/**
	 * 入驻-即将到期状态变更.
	 *
	 * @return ActYwRstatus
	 */
	@Transactional(readOnly = false)
	public PwEnterEvo enterByExpire(PwEnter pwEnter, PwEnterEvo pwEnterEvo, Date curDate) {
		if (pwEnterEvo == null) {
			pwEnterEvo = new PwEnterEvo();
		}

		if ((pwEnter != null) && (pwEnter.getEndDate() != null)) {
			// Date curDate = DateUtil.getCurDateYMD000();
			curDate = DateUtil.getCurDateYMD000(curDate);
			long pdays = DateUtil.pastDays(curDate, pwEnter.getEndDate(), true);
			System.out.println("[Expire]" + pwEnter.getNo() + "时间："
					+ DateUtil.formatDate(pwEnter.getEndDate(), DateUtil.FMT_YYYY_MM_DD_HH) + ">>>0<=" + pdays + "<="
					+ SvalPw.getEnterExpireMaxDay() + " | "
					+ ((pdays >= 0) && (pdays <= SvalPw.getEnterExpireMaxDay())));
			if ((pdays >= 0) && (pdays <= SvalPw.getEnterExpireMaxDay())) {
				pwEnter.setStatus(PwEnterStatus.PES_DXQ.getKey());
				pwEnter.setEndDate(
						DateUtil.getCurDateYMD000(DateUtil.addDay(pwEnter.getStartDate(), pwEnter.getTerm())));
				pwEnter.setExitDate(null);
				save(pwEnter);
				pwEnterEvo.getSuccEnters().add(new ApiTstatus<PwEnter>(true, "处理待续期成功", pwEnter));
			} else {
				pwEnterEvo.getIgnorEnters().add(new ApiTstatus<PwEnter>(true, "未处理", pwEnter));
			}
		} else {
			pwEnterEvo.getFailEnters().add(new ApiTstatus<PwEnter>(false, "入驻申请不存在", pwEnter));
			pwEnterEvo.setFailSqlInIds(((pwEnterEvo.getFailSqlInIds() == null) ? "" : pwEnterEvo.getFailSqlInIds())
					+ StringUtil.DANYH + pwEnter.getId() + StringUtil.DANYH + StringUtil.DOTH);
		}
		return pwEnterEvo;
	}

	/**
	 * 入驻-已到期状态变更（定时任务专用）.
	 *
	 * @return ActYwRstatus
	 */
	@Transactional(readOnly = false)
	public ApiTstatus<PwEnterEvo> enterAllByYdq(Date curDate) {
		PwEnterEvo pwEnterEvo = new PwEnterEvo();
		PwEnter pwEnter = new PwEnter();
		/**
		 * 获取已到期的符合状态.
		 */
		pwEnter.setPstatus(PwEnterStatus.getKeyByJobYDQ());
		List<PwEnter> pwEnters = findListByGroup(pwEnter);
		pwEnterEvo.setTotalNum(pwEnters.size());
		for (PwEnter pwer : pwEnters) {
			pwEnterEvo = enterByYdq(pwer, pwEnterEvo, curDate);
		}

		pwEnterEvo.setLogFile(SvalPw.getEnterLogFile() + StringUtil.LINE + SvalPw.getEnterYdqKey()
				+ DateUtil.formatDate(curDate, DateUtil.FMT_YYYY_MM_DD_HHmmss) + StringUtil.LOG);
		if (pwEnterEvo.getFailNum() > 0) {
			JsonAliUtils.writeFile(pwEnterEvo.getLogFile(), JsonMapper.toJsonString(pwEnterEvo));
		}
		return new ApiTstatus<PwEnterEvo>(true, "处理完成,总数量:[" + pwEnterEvo.getTotalNum() + "],成功数量:["
				+ pwEnterEvo.getSuccNum() + "],失败数量:[" + pwEnterEvo.getFailNum() + "]", pwEnterEvo);
	}

	/**
	 * 入驻-已到期状态变更.
	 *
	 * @return ActYwRstatus
	 */
	@Transactional(readOnly = false)
	public PwEnterEvo enterByYdq(PwEnter pwEnter, PwEnterEvo pwEnterEvo, Date curDate) {
		if (pwEnterEvo == null) {
			pwEnterEvo = new PwEnterEvo();
		}

		if ((pwEnter != null) && (pwEnter.getEndDate() != null)) {
			curDate = DateUtil.getCurDateYMD000(curDate);
			long pdays = DateUtil.pastDays(curDate, pwEnter.getEndDate(), false);
			System.out.println("[Ydq]" + pwEnter.getNo() + "时间："
					+ DateUtil.formatDate(pwEnter.getEndDate(), DateUtil.FMT_YYYY_MM_DD_HH) + ">>>" + pdays + ">="
					+ SvalPw.getEnterYdqMaxDay() + " | " + ((pdays >= SvalPw.getEnterYdqMaxDay())));
			if (pdays >= SvalPw.getEnterYdqMaxDay()) {
				pwEnter.setStatus(PwEnterStatus.PES_YDQ.getKey());
				//pwEnter.setEndDate(DateUtil.getCurDateYMD000(DateUtil.addDay(pwEnter.getStartDate(), pwEnter.getTerm())));
				pwEnter.setExitDate(null);
				save(pwEnter);
				pwEnterEvo.getSuccEnters().add(new ApiTstatus<PwEnter>(true, "处理到期成功", pwEnter));
			} else {
				pwEnterEvo.getIgnorEnters().add(new ApiTstatus<PwEnter>(true, "未处理", pwEnter));
			}
		} else {
			pwEnterEvo.getFailEnters().add(new ApiTstatus<PwEnter>(false, "入驻申请不存在", pwEnter));
			pwEnterEvo.setFailSqlInIds(((pwEnterEvo.getFailSqlInIds() == null) ? "" : pwEnterEvo.getFailSqlInIds())
					+ StringUtil.DANYH + pwEnter.getId() + StringUtil.DANYH + StringUtil.DOTH);
		}

		return pwEnterEvo;
	}

	/**
	 * 入驻-退孵状态变更（定时任务专用）.
	 *
	 * @return ActYwRstatus
	 */
	@Transactional(readOnly = false)
	public ApiTstatus<PwEnterEvo> enterAllByExit(Date curDate) {
		PwEnterEvo pwEnterEvo = new PwEnterEvo();
		PwEnter pwEnter = new PwEnter();
		/**
		 * 获取退孵的符合状态.
		 */
		pwEnter.setPstatus(PwEnterStatus.getKeyByJobExit());
		List<PwEnter> pwEnters = findListByGroup(pwEnter);
		pwEnterEvo.setTotalNum(pwEnters.size());
		for (PwEnter pwer : pwEnters) {
			pwEnterEvo = enterByExit(pwer, pwEnterEvo, curDate);
		}
		pwEnterEvo.setLogFile(SvalPw.getEnterLogFile() + StringUtil.LINE + SvalPw.getEnterExitKey()
				+ DateUtil.formatDate(curDate, DateUtil.FMT_YYYY_MM_DD_HHmmss) + StringUtil.LOG);
		if (pwEnterEvo.getFailNum() > 0) {
			JsonAliUtils.writeFile(pwEnterEvo.getLogFile(), JsonMapper.toJsonString(pwEnterEvo));
		}
		return new ApiTstatus<PwEnterEvo>(true, "处理完成,总数量:[" + pwEnterEvo.getTotalNum() + "],成功数量:["
				+ pwEnterEvo.getSuccNum() + "],失败数量:[" + pwEnterEvo.getFailNum() + "]", pwEnterEvo);
	}

	/**
	 * 入驻-退孵状态变更.
	 *
	 * @return ActYwRstatus
	 */
	@Transactional(readOnly = false)
	public PwEnterEvo enterByExit(PwEnter pwEnter, PwEnterEvo pwEnterEvo, Date curDate) {
		if (pwEnterEvo == null) {
			pwEnterEvo = new PwEnterEvo();
		}

		if ((pwEnter != null) && (pwEnter.getEndDate() != null)) {
			curDate = DateUtil.getCurDateYMD000(curDate);
			long pdays = DateUtil.pastDays(curDate, pwEnter.getEndDate(), false);
			System.out.println("[Exit]" + pwEnter.getNo() + "时间："
					+ DateUtil.formatDate(pwEnter.getEndDate(), DateUtil.FMT_YYYY_MM_DD_HH) + ">>>" + pdays + "<="
					+ SvalPw.getEnterExitMaxDay() + " | " + ((pdays >= SvalPw.getEnterExitMaxDay())));
			if (pdays >= SvalPw.getEnterExitMaxDay()) {
				pwEnter.setStatus(PwEnterStatus.PES_YTF.getKey());
				//pwEnter.setEndDate(DateUtil.getCurDateYMD000(DateUtil.addDay(pwEnter.getStartDate(), pwEnter.getTerm())));
				pwEnter.setExitDate(DateUtil.newDate());
				save(pwEnter);
				pwEnterEvo.getSuccEnters().add(new ApiTstatus<PwEnter>(true, "处理退孵成功", pwEnter));
			} else {
				pwEnterEvo.getIgnorEnters().add(new ApiTstatus<PwEnter>(true, "未处理", pwEnter));
			}
		} else {
			pwEnterEvo.getFailEnters().add(new ApiTstatus<PwEnter>(false, "入驻申请不存在", pwEnter));
			pwEnterEvo.setFailSqlInIds(((pwEnterEvo.getFailSqlInIds() == null) ? "" : pwEnterEvo.getFailSqlInIds())
					+ StringUtil.DANYH + pwEnter.getId() + StringUtil.DANYH + StringUtil.DOTH);
		}

		return pwEnterEvo;
	}

    /**
     * 入驻-批量续期.
     * @return ActYwRstatus
     * @param ids
     * @param term
     * @return
     */
    @Transactional(readOnly = false)
    public ApiTstatus<PwEnter> enterByXqs(String ids, Integer term) {
        if(StringUtil.isEmpty(ids)){
            return new ApiTstatus<PwEnter>(false, "入驻申请不能为空");
        }

        if ((term == null) || (term <= 0)) {
            return new ApiTstatus<PwEnter>(false, "入驻续期期限不能为空，且应大于 0");
        }

        for (String id : StringUtil.split(ids, StringUtil.DOTH)) {
            enterByXq(id, term);
        }
        return new ApiTstatus<PwEnter>(true, "入驻续期完成");
    }

	/**
	 * 入驻-续期.
	 *
	 * @return ActYwRstatus
	 */
	@Transactional(readOnly = false)
	public ApiTstatus<PwEnter> enterByXq(String id, Integer term) {
	    return enterByXq(id, term, true);
	}
    @Transactional(readOnly = false)
    public ApiTstatus<PwEnter> enterByXq(String id, Integer term, Boolean isAddRecord) {
		if ((term == null) || (term <= 0)) {
			return new ApiTstatus<PwEnter>(false, "入驻续期期限不能为空，且应大于 0");
		}
		if (StringUtil.isEmpty(id)) {
			return new ApiTstatus<PwEnter>(false, "入驻申请不能为空");
		}
		PwEnter pwEnter = get(id);
		if (pwEnter != null) {
			/**
			 * 记录续期历史.
			 */
			pwEnterXqhistoryService.save(pwEnter);

			Integer addTerm = DtypeTerm.addDayByType(term, pwEnter.getEndDate());

			/**
			 * 天数转换.
			 */
			Dtype dtype = Dtype.getByNum(term);
			if (dtype != null) {
				addTerm = DateUtil.addDayByType(dtype, 1, pwEnter.getEndDate());
			}

			if (addTerm == null) {
                addTerm = term;
			}

			pwEnter.setStatus(PwEnterStatus.PES_YXQ.getKey());
			pwEnter.setTerm(pwEnter.getTerm() + addTerm);
			pwEnter.setEndDate(DateUtil.getCurDateYMD000(DateUtil.addDay(pwEnter.getStartDate(), pwEnter.getTerm())));
			pwEnter.setTermNum(pwEnter.getTermNum() == null ? 1 : pwEnter.getTermNum() + 1);
			pwEnter.setReDate(DateUtil.newDate());
			pwEnter.setExitDate(null);
			save(pwEnter);
			if(isAddRecord){
                //添加一条退孵审核记录
                pwApplyRecordService.saveFailRecored(pwEnter, PwEnterBgremarks.R3.getKey());
            }
			return new ApiTstatus<PwEnter>(true, "续期成功");
		}
		return new ApiTstatus<PwEnter>(false, "入驻申请不存在");
	}

	/**
	 * 入驻-退孵.
	 *
	 * @return ActYwRstatus
	 */
	@Transactional(readOnly = false)
	public ApiTstatus<PwEnter> enterByExit(String id) {
	    return enterByExit(id, true);
	}
    @Transactional(readOnly = false)
    public ApiTstatus<PwEnter> enterByExit(String id, Boolean isAddRecord) {
		if (StringUtil.isEmpty(id)) {
			return new ApiTstatus<PwEnter>(false, "入驻申请不能为空");
		}
		PwEnter pwEnter = get(id);
		if (pwEnter != null) {
		    List<PwEnterRoom> pwEnterRooms = pwEnterRoomService.findList(new PwEnterRoom(new PwEnter(pwEnter.getId())));
		    if(StringUtil.checkNotEmpty(pwEnterRooms)){
	            pwEnterRoomService.cancelEnter(PwEroomParam.converts(pwEnter.getId(), pwEnterRooms), true);
		    }

			pwEnter.setStatus(PwEnterStatus.PES_YTF.getKey());
			pwEnter.setExitDate(DateUtil.newDate());
			save(pwEnter);

			//变更申请更改为已退孵状态.
			PwEnter parent = getByParentId(pwEnter.getId());
            if(parent != null){
                parent.setStatus(PwEnterStatus.PES_YTF.getKey());
                updateStatus(parent);
            }

            if(isAddRecord){
                //添加一条退孵审核记录
                pwApplyRecordService.saveFailRecored(pwEnter, PwEnterBgremarks.R120.getKey());
            }

			//处理所有的申请为失败状态.
			pwApplyRecordService.updateFailByEid(pwEnter.getId());
			return new ApiTstatus<PwEnter>(true, "退孵成功");
		}
		return new ApiTstatus<PwEnter>(false, "入驻申请不存在");
	}

	/**
     * 入驻-批量退孵.
     * @param ids
     * @return ActYwRstatus
     */
    @Transactional(readOnly = false)
    public ApiTstatus<PwEnter> enterByExits(String ids) {
        if(StringUtil.isEmpty(ids)){
            return new ApiTstatus<PwEnter>(false, "入驻申请不能为空");
        }
        for (String id : StringUtil.split(ids, StringUtil.DOTH)) {
            enterByExit(id);
        }
        return new ApiTstatus<PwEnter>(true, "处理完成");
    }


	/**
	 * 入驻-删除（会删除附件）.
	 *
	 * @return ActYwRstatus
	 */
	@Transactional(readOnly = false)
	public ApiTstatus<PwEnter> deleteWLEnter(String id, HttpServletRequest request, HttpServletResponse response) {
		ApiTstatus<PwEnter> rstatus = deleteWLEnter(id);
		if (rstatus.getStatus()) {
			sysAttachmentService.ajaxDelete(null, id, request, response);
			rstatus = new ApiTstatus<PwEnter>(true, "删除成功");
		}
		return rstatus;
	}

	/**
	 * 入驻-批量删除（会删除附件）.
	 *
	 * @return ActYwRstatus
	 */
	@Transactional(readOnly = false)
	public ApiTstatus<PwEnter> deletePLEnter(String ids, HttpServletRequest request, HttpServletResponse response) {
	    if(StringUtil.isEmpty(ids)){
            return new ApiTstatus<PwEnter>(false, "入驻申请不能为空");
        }
	    dao.deletePL(Arrays.asList(StringUtil.split(ids, StringUtil.DOTH)));
        return new ApiTstatus<PwEnter>(true, "删除完成");
	}

	/**
	 * 入驻-删除.
	 *
	 * @return ActYwRstatus
	 */
	@Transactional(readOnly = false)
	public ApiTstatus<PwEnter> deleteWLEnter(String id) {
		if (StringUtil.isEmpty(id)) {
			return new ApiTstatus<PwEnter>(false, "入驻申请ID不能为空");
		}
		PwEnter pwEnter = get(id);
		if (pwEnter != null) {
			PwEnterDetail ppwEnterDetail = new PwEnterDetail();
			ppwEnterDetail.setPwEnter(new PwEnter(pwEnter.getId()));
			List<PwEnterDetail> perDetails = pwEnterDetailService.findList(ppwEnterDetail);
			/**
			 * 处理所有待审核的数据.
			 */
			for (PwEnterDetail perDetail : perDetails) {
				pwEnterDetailService.deleteWL(perDetail);
			}
			deleteWL(pwEnter);
			return new ApiTstatus<PwEnter>(true, "删除成功");
		}
		return new ApiTstatus<PwEnter>(false, "入驻申请不存在");
	}

	/**
	 * 更新忽略状态.
	 *            审核结果类型
	 */
	@Transactional(readOnly = false)
	public void updateIsShow(String id, String isShow) {
	    dao.updateIsShow(new PwEnter(id, isShow));
	}

	/**
	 * 物理删除.
	 *
	 * @param pwEnter
	 *            审核结果类型
	 */
	@Transactional(readOnly = false)
	public void deleteWL(PwEnter pwEnter) {
	    dao.deleteWL(pwEnter);
	}

	/**
	 * 状态批量删除.
	 * @param ids 审核结果类型
	 */
	@Transactional(readOnly = false)
	public void deletePL(List<String> ids) {
	    dao.deletePL(ids);
	}

	/**
	 * 发送入驻站内信.
	 *
	 * @param user
	 *            发送用户
	 */
	@Transactional(readOnly = false)
	public ApiTstatus<PwEnter> sendMsg(String id, User user, String type) {
		if (StringUtil.isEmpty(id)) {
			return new ApiTstatus<PwEnter>(false, "发送参数不能为空！");
		}

		if (StringUtil.isEmpty(type)) {
			return new ApiTstatus<PwEnter>(false, "发送参数错误,消息类型");
		}

		PwEnter pwEnter = get(id);
		OaNotify.Type_Enum oanType = OaNotify.Type_Enum.getByValue(type);
		if ((OaNotify.Type_Enum.TYPE61).equals(oanType)) {
			return sendOaNotify(pwEnter, user, OaNotify.Type_Enum.TYPE61, "入驻完善资料通知",
					String.format("您入驻申请的资料不完整，编号[%s]，请及时更新，否则将会被清理。", pwEnter.getNo()));
		} else if ((OaNotify.Type_Enum.TYPE62).equals(oanType)) {
			return sendOaNotify(pwEnter, user, OaNotify.Type_Enum.TYPE62, "入驻申请警告通知", "您入驻申请过于频繁，请珍惜申请权限，否则将承担相应惩罚。");
		} else {
			return new ApiTstatus<PwEnter>(false, "发送参数消息类型未定义");
		}
	}

	/**
	 * 发送入驻站内信.
	 *
	 * @param user
	 *            发送用户
	 */
	@Transactional(readOnly = false)
	public ApiTstatus<PwEnter> sendOaNotify(PwEnter pwEnter, User user, OaNotify.Type_Enum type, String title,
			String content) {
		if ((pwEnter == null)) {
			return new ApiTstatus<PwEnter>(false, "发送入驻参数不正确！");
		}

		if ((user == null)) {
			return new ApiTstatus<PwEnter>(false, "发送人资料不正确！");
		}

		if (StringUtil.isEmpty(title) || StringUtil.isEmpty(content) || (type == null)) {
			return new ApiTstatus<PwEnter>(false, "发送参数不完整[主题|内容|消息类型]");
		}

		OaNotifyRecord oaNotifyRecord = new OaNotifyRecord();
		OaNotify oaNotify = new OaNotify();
		oaNotify.setTitle(title);
		oaNotify.setContent(content);
		oaNotify.setType(type.getValue());
		oaNotify.setsId(pwEnter.getId());
		oaNotify.setCreateBy(user);
		oaNotify.setCreateDate(new Date());
		oaNotify.setUpdateBy(user);
		oaNotify.setUpdateDate(oaNotify.getCreateDate());
		oaNotify.setEffectiveDate(oaNotify.getCreateDate());
		oaNotify.setStatus(OaNotifyTypeStatus.DEPLOY.getKey());
		oaNotify.setSendType(OaNotifySendType.DIRECRIONAL.getVal());

		List<OaNotifyRecord> recList = new ArrayList<>();
		oaNotifyRecord.setId(IdGen.uuid());
		oaNotifyRecord.setOaNotify(oaNotify);
		oaNotifyRecord.setUser(pwEnter.getApplicant());
		oaNotifyRecord.setReadFlag(Const.NO);
		oaNotifyRecord.setOperateFlag(Const.NO);
		recList.add(oaNotifyRecord);

		oaNotify.setOaNotifyRecordList(recList);
		oaNotifyService.save(oaNotify);
		return new ApiTstatus<PwEnter>(true, "通知发送成功！");
	}

	@Transactional(readOnly = false)
	public void updateStatus(PwEnter pwEnter) {
		dao.updateStatus(pwEnter);
	}

	@Transactional(readOnly = false)
	public void saveSysAttachment(String id, String ftype, String fileStep, List<SysAttachmentVo> list) {
		SysAttachment sadel = new SysAttachment();
		if (id != null) {
			FileTypeEnum fileTypeEnum = FileTypeEnum.getByValue(ftype);
			FileStepEnum fileStepEnum = FileStepEnum.getByValue(fileStep);
			sadel.setUid(id);
			sadel.setType(fileTypeEnum);
			sadel.setFileStep(fileStepEnum);
			sysAttachmentService.deleteByStep(sadel);
		}

		for (int i = 0; i < list.size(); i++) {
        SysAttachment sa = new SysAttachment();

        if (StringUtil.isNotEmpty(list.get(i).getTitle())) {
            sa.setName(list.get(i).getTitle());
        } else if (StringUtil.isNotEmpty(list.get(i).getName())) {
            sa.setName(list.get(i).getName());
        }
        if (StringUtil.isNotEmpty(id)) {
            sa.setUid(id);
        } else {
            sa.setUid(IdGen.uuid());
        }
        SysAttachmentVo sysAtt = list.get(i);
        if ((sysAtt != null) && StringUtil.isNotEmpty(sysAtt.getFtpUrl())) {
            if (StringUtil.isNotEmpty(sysAtt.getId())) {
                String url = VsftpUtils.moveFile(sysAtt.getUrl());
                sa.setSuffix(sysAtt.getSuffix());
                sa.setUrl(url);
            } else {
                String url = VsftpUtils.moveFile(sysAtt.getFtpUrl());
                sa.setSuffix(sysAtt.getType());
                sa.setUrl(url);
            }
        }
        FileTypeEnum fileTypeEnum = FileTypeEnum.getByValue(ftype);
        FileStepEnum fileStepEnum = FileStepEnum.getByValue(fileStep);
        sa.setType(fileTypeEnum);
        sa.setFileStep(fileStepEnum);
        sysAttachmentService.save(sa);
    }
  }

	/**根据userid查询入驻信息
	 * @param uid
	 */
	public PwEnterInfo getPwEnterInfo(String uid) {
		PwEnterInfo pe=new PwEnterInfo();
		Set<String> enters=new HashSet<String>();
		Date maxend=null;
		List<String> types=new ArrayList<String>();
		//查询企业入驻信息
		PwEnterInfo p1=dao.getMaxComEndDate(uid);
		if(p1!=null){
			if(p1.getEnterEndTime()!=null){
				maxend=p1.getMaxEndTime();
			}
			if(StringUtil.isNotEmpty(p1.getEnterIds())){
				for(String id:p1.getEnterIds().split(",")){
					enters.add(id);
				}
				types.add(PwEnterType.PET_QY.getName());
			}
		}
		//查询团队入驻信息
		PwEnterInfo p2=dao.getMaxTeamEndDate(uid);
		if(p2!=null){
			if(maxend==null||maxend.before(p2.getMaxEndTime())){
				maxend=p2.getMaxEndTime();
			}
			if(StringUtil.isNotEmpty(p2.getEnterIds())){
				for(String id:p2.getEnterIds().split(",")){
					enters.add(id);
				}
				types.add(PwEnterType.PET_TEAM.getName());
			}
		}
		//查询项目入驻信息
		PwEnterInfo p3=dao.getMaxProEndDate(uid);
		if(p3!=null){
			if(maxend==null||maxend.before(p3.getMaxEndTime())){
				maxend=p3.getMaxEndTime();
			}
			if(StringUtil.isNotEmpty(p3.getEnterIds())){
				for(String id:p3.getEnterIds().split(",")){
					enters.add(id);
				}
				types.add(PwEnterType.PET_XM.getName());
			}
		}
		if(maxend!=null){
			pe.setMaxEndTime(maxend);
			pe.setEnterEndTime(DateUtil.formatDate(maxend, "yyyy-MM-dd"));
		}
		if(types.size()!=0){
			pe.setEnterTypeStr(Joiner.on("/").join(types));
		}
		if(enters.size()>0){
			List<PwEnterRoomVo> l=dao.getPwRoomInfo(enters);
			pe.setEnterIds(Joiner.on(",").join(enters));
			pe.setRooms(l);
		}
		return pe;
	}

    /**
     * 保存 提交入驻
     */
	@Transactional(readOnly = false)
	public ApiResult savePwEnterApply(PwEnter pwEnter) {
		if (StringUtil.isNotEmpty(pwEnter.getId())//判断变更申请 复制变更数据
				&& StringUtil.isNotEmpty(pwEnter.getStatus())//已经审核过
                && !(pwEnter.getStatus().equals(PwEnterStatus.PES_DSH.getKey()))//已经审核过
                && !(pwEnter.getStatus().equals(PwEnterStatus.PES_RZSB.getKey()))//已经审核过
				&& pwEnter.getIsTemp().equals(TempTypeEnum.R1.getValue())) {// 提交
			pwEnter.setParentId(pwEnter.getId());
			pwEnter.setId("");
			pwEnter.setStatus(PwEnterStatus.PES_DSH.getKey());
			pwEnter.setStartDate(new Date());
			pwEnter.setIsCopy(Const.YES);
		}
        if(pwEnter.getIsCopy()!=null && Const.YES.equals(pwEnter.getIsCopy())){//变更申请
			updateCopyDetail(pwEnter);//记录变更状态
            if((!pwEnter.isCompanyIsChange()) &&//判断是否变更
                    (!pwEnter.isTeamIsChange()) &&
                    (!pwEnter.isProjectIsChange()) &&
                    (!pwEnter.isSpaceIsChange())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,"没有数据变更");
            }
            save(pwEnter);//保存入驻数据
			pwApplyRecordService.saveChangeRecord(pwEnter);//保存变更记录
            //发消息给管理员
            List roles= getAdminList();
            User user=UserUtils.getUser();
            oaNotifyService.sendOaNotifyByTypeAndUser(user, roles,  OaNotify.Type_Enum.TYPE26.getName(), user.getName() + "申报"+ OaNotify.Type_Enum.TYPE26.getName()+"，请您审核。",
                    OaNotify.Type_Enum.TYPE26.getValue(), pwEnter.getId());
            saveCopyDetail(pwEnter);
		}else { //申请
            pwEnter.setStatus(PwEnterStatus.PES_DSH.getKey());
            pwEnter.setRestatus(PwEroomStatus.PER_DFP.getKey());
            pwEnter.setStartDate(new Date());
            pwEnter.setIsCopy(Const.NO);
			if(StringUtil.isNotEmpty(pwEnter.getId())) {
				removeOldDetail(pwEnter.getId());//删除详情
			}
            save(pwEnter);//保存入驻数据
            if(pwEnter.getIsTemp().equals(TempTypeEnum.R1.getValue())){
                pwApplyRecordService.saveRecord(pwEnter);
                //发消息给管理员
                List roles= getAdminList();
                User user=UserUtils.getUser();
                oaNotifyService.sendOaNotifyByTypeAndUser(user, roles,  OaNotify.Type_Enum.TYPE25.getName(), user.getName() + "申报入驻申请，请您审核。",
                        OaNotify.Type_Enum.TYPE25.getValue(), pwEnter.getId());
            }
            saveAllDetail(pwEnter);
		}
		return ApiResult.success(pwEnter);
	}

    private List getAdminList() {
        List<String> roles= new ArrayList<String>();
        Role role = systemService.getRole(CoreIds.NSC_SYS_ROLE_ADMIN.getId());
        roles = userService.findListByRoleId(role.getId());
        return roles;
    }

    private void saveCopyDetail(PwEnter pwEnter){
        if (pwEnter.getPwCompany()!=null && StringUtil.isNotEmpty(pwEnter.getPwCompany().getNo())) {  //保存企业
            saveCopyPwCompanyByPwEnter(pwEnter);
        }
        //保存团队信息
        if (pwEnter.getTeam() != null && StringUtil.isNotEmpty(pwEnter.getTeam().getId())) {
            saveCopyTeamByPwEnter(pwEnter);
        }
        //保存项目
        List<PwProject> pwProjectList = pwEnter.getPwProjectList();
        if (StringUtil.checkNotEmpty(pwProjectList)) {
            for (PwProject pwProject : pwProjectList) {
                //保存项目
                pwProject.setId("");
                pwProjectService.save(pwProject);
                //保存申报与项目关系
                saveDetail(pwEnter.getId(), PwEnterType.PET_XM.getKey(), pwProject.getId());
                //保存项目附件
                if (StringUtil.checkNotEmpty(pwProject.getSysAttachmentList())) {
                    sysAttachmentService.saveBySysAttachmentVo(pwProject.getSysAttachmentList(), pwProject.getId(), FileTypeEnum.S13, FileStepEnum.S1301);
                }
            }
        }
    }

    private void removeOldDetail(String id) {
        PwEnter oldPwEnter=getPwEnterById(id);
        //删除旧申报记录
        pwEnterDetailService.deleteByEid(id);
        //删除企业
        if(oldPwEnter.getPwCompany()!=null && oldPwEnter.getPwCompany().getId() != null){
            if(StringUtil.checkNotEmpty(oldPwEnter.getPwCompany().getFileInfo())){
                //pwCompanyService.delete(oldPwEnter.getPwCompany());
                List<SysAttachment> sysAttachmentList=oldPwEnter.getPwCompany().getFileInfo();
                for(SysAttachment sysAttachment:sysAttachmentList){
                    sysAttachmentService.delete(sysAttachment);
                }
            }
        }

        //删除团队关系
        if(oldPwEnter.getTeam()!=null && oldPwEnter.getTeam().getId()!= null){
            teamUserHistoryService.deleteByProIdAndTeamId(id,oldPwEnter.getTeam().getId());
        }
        //删除项目
        if(StringUtil.checkNotEmpty(oldPwEnter.getPwProjectList())){
            for(PwProject pwProject:oldPwEnter.getPwProjectList()){
                pwProjectService.delete(pwProject);
                List<SysAttachment> sysAttachmentList=pwProject.getFileInfo();
                if(StringUtil.checkNotEmpty(sysAttachmentList)){
                    for (SysAttachment sysAttachment : sysAttachmentList) {
                        sysAttachmentService.delete(sysAttachment);
                    }
                }
            }
        }

    }

	@Transactional(readOnly = false)
	public void delPwEnterAndDetail(String id) {
		PwEnter pwEnter=getPwEnterById(id);
		//删除旧申报记录
		pwEnterDetailService.deleteByEid(pwEnter.getId());
		//删除企业
		if(pwEnter.getPwCompany()!=null && pwEnter.getPwCompany().getId() != null){
			if(StringUtil.checkNotEmpty(pwEnter.getPwCompany().getFileInfo())){
                pwCompanyService.delete(pwEnter.getPwCompany());
				List<SysAttachment> sysAttachmentList=pwEnter.getPwCompany().getFileInfo();
				for(SysAttachment sysAttachment:sysAttachmentList){
					sysAttachmentService.delFile(sysAttachment.getId(),sysAttachment.getUrl());
				}
			}
		}

		//删除团队关系
		if(pwEnter.getTeam()!=null && pwEnter.getTeam().getId()!= null){
			teamUserHistoryService.deleteByProIdAndTeamId(id,pwEnter.getTeam().getId());
		}
		//删除项目
		if(StringUtil.checkNotEmpty(pwEnter.getPwProjectList())){
			for(PwProject pwProject:pwEnter.getPwProjectList()){
                pwProjectService.delete(pwProject);
				List<SysAttachment> sysAttachmentList=pwProject.getFileInfo();
                if(StringUtil.checkNotEmpty(sysAttachmentList)){
                    for(SysAttachment sysAttachment:sysAttachmentList){
                        sysAttachmentService.delFile(sysAttachment.getId(),sysAttachment.getUrl());
                    }
                }
			}
		}
		delete(pwEnter);
	}

	private void updateALLDetail(PwEnter pwEnter){
		if(StringUtil.isNotEmpty(pwEnter.getId())){
			PwEnter oldPwEnter=getPwEnterById(pwEnter.getId());

			//判断企业变更
            if(PwEnterType.PET_TEAM.getKey().equals(pwEnter.getType())|| pwEnter.getPwCompany()==null){
                pwEnter.setCompanyIsChange(false);
            }else {
                if (pwEnter.getPwCompany() != null && oldPwEnter.getPwCompany() != null &&
                        pwEnter.getPwCompany().equals(oldPwEnter.getPwCompany())) {
                    pwEnter.setCompanyIsChange(false);
                }
            }
			//判断团队变更
            if(pwEnter.getTeam()==null){
                pwEnter.setTeamIsChange(false);
            }else {
                if (pwEnter.getTeam() != null && oldPwEnter.getTeam() != null
                        && pwEnter.getTeam().getId().equals(oldPwEnter.getTeam().getId())) {
                    List<TeamUserHistory> stus = pwEnter.getStus();
                    List<TeamUserHistory> teas = pwEnter.getTeas();
                    List<TeamUserHistory> oldStus = oldPwEnter.getStus();
                    List<TeamUserHistory> oldteas = oldPwEnter.getTeas();
                    if (checkList(stus, oldStus) && checkList(teas, oldteas)) {
                        pwEnter.setTeamIsChange(false);
                    }
                }
            }
			//判断项目变更
            if(StringUtil.checkEmpty(pwEnter.getPwProjectList())
                    && StringUtil.checkEmpty(oldPwEnter.getPwProjectList())){
                pwEnter.setProjectIsChange(false);
            }else {
                if (StringUtil.checkNotEmpty(pwEnter.getPwProjectList())
                        && StringUtil.checkNotEmpty(oldPwEnter.getPwProjectList())) {
                    List<PwProject> pwProjectList = pwEnter.getPwProjectList();
                    List<PwProject> oldPwProjectList = oldPwEnter.getPwProjectList();
                    if (checkPwProjectList(pwProjectList, oldPwProjectList)) {
                        pwEnter.setProjectIsChange(false);
                    }
                }
            }

			//判断场地变更
            if(pwEnter.getExpectWorkNum()==null){
                pwEnter.setSpaceIsChange(false);
            }else {
                if (pwEnter.getExpectWorkNum().equals(oldPwEnter.getExpectWorkNum())
                        && pwEnter.getExpectTerm().equals(oldPwEnter.getExpectTerm())) {
                    pwEnter.setSpaceIsChange(false);
                }
            }
		}
	}


    private void updateCopyDetail(PwEnter pwEnter){
        if(StringUtil.isNotEmpty(pwEnter.getParentId())){
            PwEnter oldPwEnter=getPwEnterById(pwEnter.getParentId());
            //判断企业变更  团队申请 和 企业申请并且数据一样的是 则没有变更
            if (PwEnterType.PET_TEAM.getKey().equals(pwEnter.getType()) ||
                    ( PwEnterType.PET_QY.getKey().equals(pwEnter.getType()) &&
                    pwEnter.getPwCompany() != null && oldPwEnter.getPwCompany() != null &&
                    pwEnter.getPwCompany().equals(oldPwEnter.getPwCompany())
                    )
            ){
                pwEnter.setCompanyIsChange(false);
            }

            //判断团队变更
            if(pwEnter.getTeam()!=null&&oldPwEnter.getTeam()!=null
                && pwEnter.getTeam().getId().equals(oldPwEnter.getTeam().getId())){
                List<TeamUserHistory> stus=pwEnter.getStus();
                List<TeamUserHistory> teas=pwEnter.getTeas();
                List<TeamUserHistory> oldStus=oldPwEnter.getStus();
                List<TeamUserHistory> oldteas=oldPwEnter.getTeas();
                if(checkList(stus,oldStus) &&checkList(teas,oldteas)){
                    pwEnter.setTeamIsChange(false);
                }
            }
            //判断项目变更
            if(StringUtil.checkNotEmpty(pwEnter.getPwProjectList())
                    &&StringUtil.checkNotEmpty(oldPwEnter.getPwProjectList())){
                List<PwProject> pwProjectList=pwEnter.getPwProjectList();
                List<PwProject> oldPwProjectList=oldPwEnter.getPwProjectList();

                if(checkPwProjectList(pwProjectList,oldPwProjectList)){
                    pwEnter.setProjectIsChange(false);
                }
            }

            //判断场地变更
            if((pwEnter.getExpectWorkNum()==null &&  oldPwEnter.getExpectWorkNum()==null)||
                    (pwEnter.getExpectWorkNum()!=null  && oldPwEnter.getExpectWorkNum()!=null &&
                pwEnter.getExpectWorkNum().equals(oldPwEnter.getExpectWorkNum())&&
                pwEnter.getExpectRemark().equals(oldPwEnter.getExpectRemark())
                )
            ){
                pwEnter.setSpaceIsChange(false);
                pwEnter.setRestatus(PwEroomStatus.PER_DFP.getKey());
                pwEnter.setIsShow(Const.SHOW);
            }
        }
    }

	private boolean checkPwProjectList(List<PwProject> pwProjectList, List<PwProject> oldPwProjectList) {
		if(pwProjectList.size()!=oldPwProjectList.size()){
            return false;
        }
        for(PwProject pwProject:pwProjectList){
            if(!oldPwProjectList.contains(pwProject)){
                return false;
            }
        }
		return true;
	}

	private boolean checkList(List<TeamUserHistory> stus, List<TeamUserHistory> oldStus) {
		if(stus.size()!=oldStus.size()){
            return false;
        }
        for(TeamUserHistory teamUserHistory:stus){
            //找出 用户id和责任人 不全相等
            if(!oldStus.contains(teamUserHistory)){
                return false;
            }
        }
		return true;
	}

	private void saveAllDetail(PwEnter pwEnter) {
		//保存企业
		if (pwEnter.getPwCompany()!=null && StringUtil.isNotEmpty(pwEnter.getPwCompany().getNo())) {
			savePwCompanyByPwEnter(pwEnter);
		}
		//保存团队信息
		if(pwEnter.getTeam()!=null && StringUtil.isNotEmpty(pwEnter.getTeam().getId())){
			saveTeamByPwEnter(pwEnter);
		}
		//保存项目
        if(StringUtil.isNotEmpty(pwEnter.getId())){    //删除旧的项目
            delPwProjectListByPwEnter(pwEnter.getId());
        }
		List<PwProject> pwProjectList = pwEnter.getPwProjectList();
		if(StringUtil.checkNotEmpty(pwProjectList)){
            savePwProjectListByPwEnter(pwEnter,pwProjectList);
		}
	}

    private void delPwProjectListByPwEnter(String id) {
        //删除旧申报记录
        //删除项目
        List<PwEnterDetail> pwEnterProjectDetail=pwEnterDetailService.getByPwEnterXmIdAndType(id,PwEnterType.PET_XM.getKey());
        for(PwEnterDetail pwEnterDetail:pwEnterProjectDetail){
            if(StringUtil.isNotEmpty(pwEnterDetail.getRid())){
                pwProjectService.delete(new PwProject(pwEnterDetail.getRid()));
            }
        }
        pwEnterDetailService.deleteByEidAndType(id, PwEnterType.PET_XM.getKey());
    }

    private void savePwProjectListByPwEnter(PwEnter pwEnter,List<PwProject> pwProjectList) {
        for (PwProject pwProject : pwProjectList) {
            pwProject.setId("");
            pwProjectService.save(pwProject);
            //保存申报与项目关系
            saveDetail(pwEnter.getId(),PwEnterType.PET_XM.getKey(),pwProject.getId());
            //保存项目附件
            if(StringUtil.checkNotEmpty(pwProject.getSysAttachmentList())) {
                sysAttachmentService.saveBySysAttachmentVo(pwProject.getSysAttachmentList(), pwProject.getId(), FileTypeEnum.S13, FileStepEnum.S1301);
            }
        }
    }

    @Transactional(readOnly = false)
	public void savePwCompanyByPwEnter(PwEnter pwEnter) {
		PwCompany pwCompany = pwEnter.getPwCompany();
		//保存申报与企业关系
        if(StringUtil.isNotEmpty(pwCompany.getId())) {
            sysAttachmentService.deleteByUid(pwCompany.getId());
            pwCompanyService.save(pwCompany);
        }else{
            pwCompanyService.save(pwCompany);
        }
        saveDetail(pwEnter.getId(), PwEnterType.PET_QY.getKey(), pwCompany.getId());
		//保存企业附件
		if(StringUtil.checkNotEmpty(pwCompany.getSysAttachmentList())) {
			sysAttachmentService.saveBySysAttachmentVo(pwCompany.getSysAttachmentList(), pwCompany.getId(), FileTypeEnum.S14, FileStepEnum.S1401);
		}
	}

    @Transactional(readOnly = false)
    public void savePwCompanyChange(PwEnter pwEnter) {
        PwCompany pwCompany = pwEnter.getPwCompany();
        pwCompanyService.save(pwCompany);
        //保存企业附件
        if(StringUtil.checkNotEmpty(pwCompany.getSysAttachmentList())) {
            sysAttachmentService.deleteByUid(pwCompany.getId());
            sysAttachmentService.saveBySysAttachmentVo(pwCompany.getSysAttachmentList(), pwCompany.getId(), FileTypeEnum.S14, FileStepEnum.S1401);
        }
    }

    @Transactional(readOnly = false)
    public void saveCopyPwCompanyByPwEnter(PwEnter pwEnter) {
        PwCompany pwCompany = pwEnter.getPwCompany();
        pwCompany.setId("");
        pwCompanyService.save(pwCompany);
        //保存申报与企业关系
        saveDetail(pwEnter.getId(),PwEnterType.PET_QY.getKey(),pwCompany.getId());
        //保存企业附件
        if(StringUtil.checkNotEmpty(pwCompany.getSysAttachmentList())) {
            sysAttachmentService.saveBySysAttachmentVo(pwCompany.getSysAttachmentList(), pwCompany.getId(), FileTypeEnum.S14, FileStepEnum.S1401);
        }
    }


	@Transactional(readOnly = false)
	public void saveTeamByPwEnter(PwEnter pwEnter) {
		//判断团队是否在做大赛或者项目 保存团队历史记录
		Team team=teamService.get(pwEnter.getTeam().getId());
		boolean isHaveOther=teamUserHistoryService.getOtherHistory(team.getId());
		if(isHaveOther){
			//有做大赛或者项目 重新建立团队和 保存团队历史
			Team newTeam=team;
			int flag = new Random().nextInt(999999);
			if (flag < 100000) {
				flag += 100000;
			}
            newTeam.setId("");
			//新团队名字为 旧团队名称+6位随机数
			newTeam.setName(team.getName()+flag);
			//建设完毕
			newTeam.setState(TeamStateType.TM_JSWB.getKey());
			newTeam.setSponsorId(UserUtils.getUser().getId());
            newTeam.setSponsor(UserUtils.getUser().getId());

            newTeam.setMemberNum(pwEnter.getStus().size());
            getTeamTeacherNum(newTeam, pwEnter.getTeas());//算出导师人数
			teamService.save(newTeam);
			team=newTeam;
			//保存团队历史
			List<TeamUserHistory> stus=pwEnter.getStus();
            stus.addAll(pwEnter.getTeas());
			for(TeamUserHistory teamUserHistory:stus){
                buildTeamUserHistory(pwEnter, newTeam, teamUserHistory);
			}
            teamUserHistoryService.insertAllPw(stus);
			teamUserRelationService.saveAll(stus);	//  保存关联关系
		}else{   //没有有做大赛或者项目 直接保存团队历史
            //添加团队人数
            team = teamService.get(pwEnter.getTeam().getId());
            team.setMemberNum(pwEnter.getStus().size());
            getTeamTeacherNum(team, pwEnter.getTeas());//算出导师人数
            teamService.updateAllInfo(team);

            List<TeamUserHistory> stus=pwEnter.getStus();
            stus.addAll(pwEnter.getTeas());
            for(TeamUserHistory teamUserHistory:stus){
                buildTeamUserHistory(pwEnter, team, teamUserHistory);
                teamUserHistory.setProType(FlowProjectType.PMT_ENTER.getKey());
            }
			teamUserHistoryService.insertAllPw(stus);
            teamUserRelationService.deleteByTeamId(pwEnter.getTeam().getId());
            teamUserRelationService.saveAll(stus);
		}
		saveDetail(pwEnter.getId(),PwEnterType.PET_TEAM.getKey(),team.getId());	//保存申报与团队关系
	}

    private void buildTeamUserHistory(PwEnter pwEnter, Team team, TeamUserHistory teamUserHistory) {
        teamUserHistory.setId(IdGen.uuid());
        teamUserHistory.setProId(pwEnter.getId());
        teamUserHistory.setTeamId(team.getId());
        teamUserHistory.setCreateDate(new Date());
        teamUserHistory.setUpdateDate(new Date());
        teamUserHistory.setUser(new User(teamUserHistory.getUserId()));
        teamUserHistory.setDelFlag("0");
    }

    @Transactional(readOnly = false)
    public void saveCopyTeamByPwEnter(PwEnter pwEnter) {
        //判断团队是否在做大赛或者项目 保存团队历史记录
        Team team=pwEnter.getTeam();

        //没有有做大赛或者项目 直接保存团队历史
        List<TeamUserHistory> stus=pwEnter.getStus();
        List<TeamUserHistory> teas=pwEnter.getTeas();
        stus.addAll(teas);
        for(TeamUserHistory teamUserHistory:stus){
            teamUserHistory.setId(IdGen.uuid());
            teamUserHistory.setProId(pwEnter.getId());
            teamUserHistory.setProType(FlowProjectType.PMT_ENTER.getKey());
            teamUserHistory.setTeamId(team.getId());
            teamUserHistory.setUser(new User(teamUserHistory.getUserId()));
            teamUserHistory.setCreateDate(new Date());
            teamUserHistory.setUpdateDate(new Date());
            teamUserHistory.setDelFlag("0");
        }
        teamUserHistoryService.insertAllPw(stus);
//        teamUserRelationService.saveAll(stus);
        //保存申报与团队关系
        saveDetail(pwEnter.getId(),PwEnterType.PET_TEAM.getKey(),team.getId());
    }


	private void saveDetail(String pwEnterId, String type, String typeId) {
		PwEnterDetail pwCompanyPwEnterDetail = new PwEnterDetail();
		pwCompanyPwEnterDetail.setEid(pwEnterId);
		pwCompanyPwEnterDetail.setType(type);
		pwCompanyPwEnterDetail.setRid(typeId);
		pwEnterDetailService.save(pwCompanyPwEnterDetail);
	}

	private void savePwEnterType(PwEnter pwEnter) {
		dao.updatePwEnterType(pwEnter);
	}

	//根据id查找对应 团队 企业 项目
	public PwEnter getPwEnterById(String id) {
		PwEnter pwEnter=get(id);
		//查找团队信息
		PwEnterDetail pwEnterDetail=pwEnterDetailService.getByPwEnterIdAndType(id,PwEnterType.PET_TEAM.getKey());
		if(pwEnterDetail!=null){
			Team team=teamService.get(pwEnterDetail.getRid());
			pwEnter.setTeam(team);
			List<TeamUserHistory> userList =teamUserHistoryService.getByProId(id,team.getId());
			List<TeamUserHistory> stus=new ArrayList<TeamUserHistory>();
			List<TeamUserHistory> teas=new ArrayList<TeamUserHistory>();
			for(TeamUserHistory teamUserHistory:userList){
				if(SysSval.XS.equals(teamUserHistory.getUserType())){
					if(teamUserHistory.getUser().getId().equals(pwEnter.getDeclareId())){
						teamUserHistory.setUserzz(SysSval.FZR);
					}else{
						teamUserHistory.setUserzz(SysSval.ZY);
					}
					stus.add(teamUserHistory);
				}else{
					teas.add(teamUserHistory);
				}
			}
		    pwEnter.setStus(stus);
		    pwEnter.setTeas(teas);

		}
		//查找企业信息
		PwCompany pwCompany=pwCompanyService.getByEid(id);
		if(pwCompany!=null){
			SysAttachment sysAttachment=new SysAttachment();
			sysAttachment.setUid(pwCompany.getId());
			List<SysAttachment> sysAttachmentList= sysAttachmentService.getFiles(sysAttachment);
			if(StringUtil.checkNotEmpty(sysAttachmentList)){
				pwCompany.setFileInfo(sysAttachmentList);
			}
			pwEnter.setPwCompany(pwCompany);
		}
		//查找项目信息
		List<PwProject> pwProjectList=pwProjectService.getPwProjectListByEid(id);
		if(StringUtil.checkNotEmpty(pwProjectList)){
			for(PwProject pwProject:pwProjectList){
				SysAttachment sysAttachment=new SysAttachment();
				sysAttachment.setUid(pwProject.getId());
				List<SysAttachment> sysAttachmentList= sysAttachmentService.getFiles(sysAttachment);
				if(StringUtil.checkNotEmpty(sysAttachmentList)){
					pwProject.setFileInfo(sysAttachmentList);
				}
			}
			pwEnter.setPwProjectList(pwProjectList);
		}
		return pwEnter;
	}

	public List<Team> getTeams() {
		User user = UserUtils.getUser();
		return teamService.findTeams(user.getId());
	}

	public List<ProModel> getProjects() {
		User user = UserUtils.getUser();
        List<ProModel> proModelList=proModelService.findProjects(user.getId());
        if(StringUtil.checkNotEmpty(proModelList)){
            for(ProModel proModel:proModelList){
                SysAttachment sysAttachment=new SysAttachment();
                sysAttachment.setUid(proModel.getId());
                List<SysAttachment> sysAttachmentList=sysAttachmentService.findList(sysAttachment);
                if(StringUtil.checkNotEmpty(sysAttachmentList)){
                    proModel.setFileInfo(sysAttachmentList);
                }
            }
        }
		return proModelList;
	}

	public Map<String,Object> findPwEnterTeamById(String pwEnterId) {
		Map<String,Object> map= Maps.newHashMap();
		PwEnterDetail pwEnterDetail=pwEnterDetailService.getByPwEnterIdAndType(pwEnterId,PwEnterType.PET_TEAM.getKey());
		if(pwEnterDetail!=null){
			PwEnter pwEnter=get(pwEnterId);
			Team team=teamService.get(pwEnterDetail.getRid());
			map.put("team",team);
            List<Map<String, String>> stuList = teamUserHistoryService.findTeamStudent(pwEnterId,team.getId());
            List<Map<String, String>> teaList = teamUserHistoryService.findTeamTeacher(pwEnterId,team.getId());

			if(StringUtil.checkNotEmpty(stuList)){
				map.put("stus",stuList);
			}
			if(StringUtil.checkNotEmpty(teaList)){
				map.put("teas",teaList);
			}
		}
		return map;
	}

	//变更审核 1:审核通过 0：审核失败
	@Transactional(readOnly = false)
	public PwEnter changeAudit(String changePwEnterId,String isPass,String remarks){

		if((PwEnterAuditEnum.SHTG.getValue()).equals(isPass) ){
            return saveChangePwEnter(changePwEnterId,remarks);
		}else{
			delChangePwEnter(changePwEnterId,remarks);
			return null;
		}
	}
	//变更成功
	@Transactional(readOnly = false)
	public PwEnter saveChangePwEnter(String changePwEnterId,String remarks) {
		PwEnter changePwEnter=getPwEnterById(changePwEnterId);
		PwEnter pwEnter=getPwEnterById(changePwEnter.getParentId());
		//更换公司关联关系
		PwEnterDetail pwEnterCompanyDetail=pwEnterDetailService.getByPwEnterIdAndType(changePwEnterId,PwEnterType.PET_QY.getKey());
		List<PwEnterDetail> detailList=Lists.newArrayList();
		if(pwEnterCompanyDetail!=null){
            pwEnterCompanyDetail.setId(IdGen.uuid());
			pwEnterCompanyDetail.setEid(pwEnter.getId());
			detailList.add(pwEnterCompanyDetail);
		}

		//更换团队关联关系
		PwEnterDetail pwEnterTeamDetail=pwEnterDetailService.getByPwEnterIdAndType(changePwEnterId,PwEnterType.PET_TEAM.getKey());
		if(pwEnterTeamDetail!=null){
            pwEnterTeamDetail.setId(IdGen.uuid());
			pwEnterTeamDetail.setEid(pwEnter.getId());
            detailList.add(pwEnterTeamDetail);
            List<TeamUserHistory> teamUserHistoryList=teamUserHistoryService.getByProId(changePwEnterId,pwEnterTeamDetail.getRid());
            String fzr = null;
            for (TeamUserHistory tuh : teamUserHistoryList) {
                tuh.setId(IdGen.uuid());
                tuh.setUser(new User(tuh.getUserId()));
                tuh.setProId(changePwEnter.getParentId());
                if ("0".equals(tuh.getUserzz())) {
                    fzr = tuh.getUserId();
                }
            }
            List<TeamUserHistory> stus=new ArrayList<TeamUserHistory>();
            List<TeamUserHistory> teas=new ArrayList<TeamUserHistory>();
            for(TeamUserHistory teamUserHistory:teamUserHistoryList) {
                if (SysSval.XS.equals(teamUserHistory.getUserType())) {
                    if (teamUserHistory.getUser().getId().equals(pwEnter.getDeclareId())) {
                        teamUserHistory.setUserzz(SysSval.FZR);
                    } else {
                        teamUserHistory.setUserzz(SysSval.ZY);
                    }
                    stus.add(teamUserHistory);
                } else {
                    teas.add(teamUserHistory);
                }
            }
            //添加团队人数
            Team team = teamService.get(pwEnter.getTeam().getId());
            team.setSponsor(fzr);
            team.setMemberNum(stus.size());
            getTeamTeacherNum(team, teas);//算出导师人数
            teamService.updateAllInfo(team);

            //变更成功后删除多余的团队历史记录

            teamUserRelationService.deleteByTeamId(pwEnterTeamDetail.getRid());
            teamUserRelationService.saveAll(teamUserHistoryList);
            teamUserHistoryService.deleteByProId(changePwEnter.getParentId());
            teamUserHistoryService.deleteByProId(changePwEnterId);
            teamUserHistoryService.insertAllPw(teamUserHistoryList);
		}
		//更换项目关联关系
		List<PwEnterDetail> pwEnterProjectDetail=pwEnterDetailService.getByPwEnterXmIdAndType(changePwEnterId,PwEnterType.PET_XM.getKey());
		for(PwEnterDetail pwEnterDetail:pwEnterProjectDetail){
            pwEnterDetail.setId(IdGen.uuid());
			pwEnterDetail.setEid(pwEnter.getId());
			detailList.add(pwEnterDetail);
        }
        //变更成功后删除多余的项目
        List<PwEnterDetail> pwEnterProjectDetailOld=pwEnterDetailService.getByPwEnterXmIdAndType(changePwEnter.getParentId(),PwEnterType.PET_XM.getKey());
        for(PwEnterDetail pwEnterDetail:pwEnterProjectDetailOld){
            if(StringUtil.isNotEmpty(pwEnterDetail.getRid())){
                pwProjectService.delete(new PwProject(pwEnterDetail.getRid()));
            }
        }
		//跟换场地信息
		pwEnter.setExpectWorkNum(changePwEnter.getExpectWorkNum());
		pwEnter.setExpectTerm(changePwEnter.getExpectTerm());
		pwEnter.setExpectRemark(changePwEnter.getExpectRemark());
        //pwEnter.setRestatus(changePwEnter.getRestatus());
		pwEnter.setRestatus(PwEroomStatus.PER_DBGFP.getKey());
        pwEnter.setIsShow(changePwEnter.getIsShow());

        pwEnter.setDeclarePhoto(changePwEnter.getDeclarePhoto());
        pwEnter.setType(changePwEnter.getType());
		//更新审核代码
		dao.update(pwEnter);
		//删除变更数据
		dao.delete(changePwEnter);
		//删除替换入驻信息
		pwEnterDetailService.deleteByEid(pwEnter.getId());
		pwEnterDetailService.deleteByEid(changePwEnter.getId());
		pwEnterDetailService.saveAll(detailList);
		//审核操作记录
		PwApplyRecord pwApplyRecord =pwApplyRecordService.getLastByEid(pwEnter.getId());
        if(pwApplyRecord!=null){
            pwApplyRecord.setEid(pwEnter.getId());
            //pwApplyRecord.setType(PwEnterBgremarks.R9.getKey());
            pwApplyRecord.setAuditId(UserUtils.getUser().getId());
            pwApplyRecord.setAuditTime(new Date());
            pwApplyRecord.setRemarks(remarks);
            pwApplyRecord.setStatus(PwEnterAuditEnum.SHTG.getValue());
            pwApplyRecordService.save(pwApplyRecord);
        }
		return pwEnter;
	}

	//变更失败
	@Transactional(readOnly = false)
	public void delChangePwEnter(String changePwEnterId,String remarks) {
		PwEnter changePwEnter=getPwEnterById(changePwEnterId);
		delete(changePwEnter);
	    PwEnterDetail pwEnterCompanyDetail=pwEnterDetailService.getByPwEnterIdAndType(changePwEnterId,PwEnterType.PET_QY.getKey());
        //删除企业
        if(pwEnterCompanyDetail!=null){
            if(StringUtil.isNotEmpty(pwEnterCompanyDetail.getRid())) {
                pwCompanyService.delete(new PwCompany(pwEnterCompanyDetail.getRid()));
                SysAttachment sysAttachment=new SysAttachment();
                sysAttachment.setUid(pwEnterCompanyDetail.getRid());
                sysAttachmentService.delete(sysAttachment);
            }
        }
        //删除项目
        List<PwEnterDetail> pwEnterProjectDetail=pwEnterDetailService.getByPwEnterXmIdAndType(changePwEnterId,PwEnterType.PET_XM.getKey());
        for(PwEnterDetail pwEnterDetail:pwEnterProjectDetail){
            if(StringUtil.isNotEmpty(pwEnterDetail.getRid())){
                pwProjectService.delete(new PwProject(pwEnterDetail.getRid()));
            }
        }
        pwEnterDetailService.deleteByEid(changePwEnter.getId());
        teamUserHistoryService.deleteByProIdAndTeamId(changePwEnterId,changePwEnter.getTeam().getId());

		//审核操作记录
        PwApplyRecord pwApplyRecord =pwApplyRecordService.getLastAuditByEid(changePwEnterId);
        if(pwApplyRecord!=null){
            pwApplyRecord.setStatus(PwEnterAuditEnum.SHBTG.getValue());
            pwApplyRecord.setAuditId(UserUtils.getUser().getId());
            pwApplyRecord.setAuditTime(new Date());
            pwApplyRecord.setRemarks(remarks);
            pwApplyRecordService.save(pwApplyRecord);
        }
	}

	public Map<String,PwCompany> findPwEnterCompanyById(String pwEnterId) {
		Map<String,PwCompany> map= Maps.newHashMap();
		//查找企业信息
		PwCompany pwCompany=pwCompanyService.getByEid(pwEnterId);
		if(pwCompany!=null){
			SysAttachment sysAttachment=new SysAttachment();
			sysAttachment.setUid(pwCompany.getId());
			List<SysAttachment> sysAttachmentList= sysAttachmentService.getFiles(sysAttachment);
			if(StringUtil.checkNotEmpty(sysAttachmentList)){
				pwCompany.setFileInfo(sysAttachmentList);
			}

		}
		map.put("pwCompany",pwCompany);
		return map;
	}

	public Map<String,List<PwProject>> findPwEnterProjectsById(String pwEnterId) {
		Map<String,List<PwProject>> map= Maps.newHashMap();
		List<PwProject> pwProjectList=pwProjectService.getPwProjectListByEid(pwEnterId);
		if(StringUtil.checkNotEmpty(pwProjectList)){
			for(PwProject pwProject:pwProjectList){
				SysAttachment sysAttachment=new SysAttachment();
				sysAttachment.setUid(pwProject.getId());
				List<SysAttachment> sysAttachmentList= sysAttachmentService.getFiles(sysAttachment);
				if(StringUtil.checkNotEmpty(sysAttachmentList)){
					pwProject.setFileInfo(sysAttachmentList);
				}
			}
		}
		map.put("pwProjectList",pwProjectList);
		return map;
	}

	@Transactional(readOnly = false)
	public void savePwEnterResultApply(PwEnter pwEnter) {
        //删除原来成果物
        sysAttachmentService.deleteByUid(pwEnter.getId());
		//保存成果物附件
		sysAttachmentService.saveBySysAttachmentVo(pwEnter.getSysAttachmentList(), pwEnter.getId(), FileTypeEnum.S15, FileStepEnum.S1301);
	}

	@Transactional(readOnly = false)
	//续期申请
	public void savePwEnterRenewalApply(String id,Integer term) {
		PwApplyRecord pwApplyRecord=new PwApplyRecord();
		pwApplyRecord.setEid(id);
		//续期时间
		pwApplyRecord.setTerm(term);
		pwApplyRecord.setType(PwEnterBgremarks.R3.getKey());
		pwApplyRecord.setStatus(PwEnterAuditEnum.DSH.getValue());
        pwApplyRecord.setDeclareId(UserUtils.getUser().getId());
        pwApplyRecord.setDeclareTime(new Date());
		pwApplyRecordService.save(pwApplyRecord);

        List roles= getAdminList();
        User user=UserUtils.getUser();
        oaNotifyService.sendOaNotifyByTypeAndUser(user, roles,  OaNotify.Type_Enum.TYPE27.getName(), user.getName() + "申报"+ OaNotify.Type_Enum.TYPE27.getName()+"，请您审核。",
                OaNotify.Type_Enum.TYPE27.getValue(), id);

	}

	@Transactional(readOnly = false)
	//退孵申请
	public void savePwEnterHatchApply(String id) {
		PwApplyRecord pwApplyRecord=new PwApplyRecord();
		pwApplyRecord.setEid(id);
		pwApplyRecord.setType(PwEnterBgremarks.R4.getKey());
		pwApplyRecord.setStatus(PwEnterAuditEnum.DSH.getValue());
        pwApplyRecord.setDeclareId(UserUtils.getUser().getId());
        pwApplyRecord.setDeclareTime(new Date());
		pwApplyRecordService.save(pwApplyRecord);
        List roles= getAdminList();
        User user=UserUtils.getUser();
        oaNotifyService.sendOaNotifyByTypeAndUser(user, roles,  OaNotify.Type_Enum.TYPE28.getName(), user.getName() + "申报"+OaNotify.Type_Enum.TYPE28.getName()+"，请您审核。",
               OaNotify.Type_Enum.TYPE28.getValue(), id);

	}


	public boolean checkPwEnterTeam(PwEnter pwEnter) {
		boolean isHas=false;
        String pwEnterId="";
        if(pwEnter.getId()!=null){
            pwEnterId=pwEnter.getId();
        }
        if(pwEnter.getTeam()==null){
            return  false;
        }
		List<PwEnterDetail> pwEnterTeamDetailList=pwEnterDetailService.checkPwEnterTeam(pwEnterId,pwEnter.getTeam().getId());
		if(StringUtil.checkNotEmpty(pwEnterTeamDetailList)){
			isHas=true;
		}
		return isHas;
	}

    public boolean checkPwEnterPwCompanyName(PwEnter pwEnter) {
        boolean isHas=false;
        if(pwEnter.getPwCompany()==null){
            return false;
        }
        if(pwEnter.getId()!=null){
            return false;
        }
        //校验公司名称
        if(StringUtil.isEmpty(pwEnter.getPwCompany().getName())){
            isHas = false;
        }else {
            PwCompany pwCompany = new PwCompany();
            pwCompany.setName(pwEnter.getPwCompany().getName());
            List<PwCompany> pwCompanyList = pwCompanyService.findListByPwCompany(pwCompany);
            if (StringUtil.checkNotEmpty(pwCompanyList)) {
                return true;
            }
        }
        return isHas;
    }

    public boolean checkPwEnterPwCompanyNo(PwEnter pwEnter) {
        boolean isHas=false;
        if(pwEnter.getPwCompany()==null){
            return false;
        }
        if(pwEnter.getId()!=null){
            return false;
        }
        //校验公司工商号
        if(StringUtil.isEmpty(pwEnter.getPwCompany().getNo())){
            isHas = false;
        }else {
            PwCompany pwCompanyNo = new PwCompany();
            pwCompanyNo.setNo(pwEnter.getPwCompany().getNo());
            List<PwCompany> pwCompanyNoList = pwCompanyService.findListByPwCompany(pwCompanyNo);
            if (StringUtil.checkNotEmpty(pwCompanyNoList)) {
                return true;
            }
        }
        return isHas;
    }

	public boolean checkPwEnterPwCompanyHas(PwEnter pwEnter) {
		boolean isHas=false;
		if(pwEnter.getPwCompany()==null){
            return false;
        }
        if(pwEnter.getId()!=null){
            return false;
        }
        //校验公司名称
        if(StringUtil.isEmpty(pwEnter.getPwCompany().getName())){
            isHas = false;
        }else {
            PwCompany pwCompany = new PwCompany();
            pwCompany.setName(pwEnter.getPwCompany().getName());
            List<PwCompany> pwCompanyList = pwCompanyService.findListByPwCompany(pwCompany);
            if (StringUtil.checkNotEmpty(pwCompanyList)) {
                return true;
            }
        }
        //校验公司工商号
        if(StringUtil.isEmpty(pwEnter.getPwCompany().getNo())){
            isHas = false;
        }else {
            PwCompany pwCompanyNo = new PwCompany();
            pwCompanyNo.setNo(pwEnter.getPwCompany().getNo());
            List<PwCompany> pwCompanyNoList = pwCompanyService.findListByPwCompany(pwCompanyNo);
            if (StringUtil.checkNotEmpty(pwCompanyNoList)) {
                return true;
            }
        }
		return isHas;
	}

    //发起变更申请时 不能进行后台变更
    public boolean checkBackChange(String pwEnterId) {
   		boolean isHas=false;
        if(StringUtil.isEmpty(pwEnterId)){
            return isHas;
        }
        PwApplyRecord pwApplyRecord=pwApplyRecordService.getChangeAppByEid(pwEnterId);
   		if(pwApplyRecord!=null){
   			isHas=true;
   		}
   		return isHas;
   	}

	public boolean checkPwEnterChange(String pwEnterId) {
		boolean isHas=false;
		//List<PwEnter> pwEnterList=getByParentId(pwEnter.getId());
        //申报记录永远只有一条待审核
        if(StringUtil.isEmpty(pwEnterId)){
            return isHas;
        }
        PwApplyRecord pwApplyRecord=pwApplyRecordService.getLastByEid(pwEnterId);
		if(pwApplyRecord!=null && PwEnterAuditEnum.DSH.getValue().equals(pwApplyRecord.getStatus())){
			isHas=true;
		}
		return isHas;
	}

	private PwEnter getByParentId(String parentId) {
		return dao.getByParentId(parentId);
	}

	@Transactional(readOnly = false)
    public void update(PwEnter pwEnter) {
        dao.update(pwEnter);
    }

	@Transactional(readOnly = false)
	public void saveTeamChange(PwEnter pwEnter) {
        List<TeamUserHistory> stus=pwEnter.getStus();
        List<TeamUserHistory> teas=pwEnter.getTeas();

        String fzr = null;
        for (TeamUserHistory t : stus) {
            if (SysSval.FZR.equals(t.getUserzz())) {
                fzr = t.getUserId();
                break;
            }
        }
        //添加团队人数
        Team team = teamService.get(pwEnter.getTeam().getId());
        team.setSponsor(fzr);
        team.setMemberNum(stus.size());
        getTeamTeacherNum(team, teas);//算出导师人数
        teamService.updateAllInfo(team);
        //添加变更记录
        teamUserChangeService.changeBySave(pwEnter.getStus(),pwEnter.getTeas(),
        				pwEnter.getTeam().getId(),pwEnter.getId(),pwEnter.getApplicant().getId());
        stus.addAll(teas);
        if(!pwEnter.getApplicant().getId().equals(fzr)){
            changePwenterApplicant(pwEnter.getId(),fzr);
        }
        //删除旧的历史成员 添加新的历史成员
        teamUserHistoryService.deleteByProIdAndTeamId(pwEnter.getId(),pwEnter.getTeam().getId());
        for(TeamUserHistory teamUserHistory:stus){
            teamUserHistory.setId(IdGen.uuid());
            teamUserHistory.setProId(pwEnter.getId());
            teamUserHistory.setProType(FlowProjectType.PMT_ENTER.getKey());
            teamUserHistory.setTeamId(pwEnter.getTeam().getId());
            teamUserHistory.setUser(new User(teamUserHistory.getUserId()));
            teamUserHistory.setCreateDate(new Date());
            teamUserHistory.setUpdateDate(new Date());
            teamUserHistory.setDelFlag("0");
        }

        teamUserRelationService.deleteByTeamId(pwEnter.getTeam().getId());
        teamUserRelationService.saveAll(stus);
        teamUserHistoryService.insertAllPw(stus);
	}

    private void changePwenterApplicant(String id, String fzr) {
        dao.updateApplicant(id,fzr);
    }

    private void getTeamTeacherNum(Team teamNums, List<TeamUserHistory> teas) {
        int s = 0;
        int e = 0;
        if (teas != null && teas.size() > 0) {
            for (TeamUserHistory t : teas) {
                BackTeacherExpansion b = backTeacherExpansionService.getByUserId(t.getUserId());
                if (b != null) {
                    if ((TeacherType.TY_XY.getKey()).equals(b.getTeachertype())) {
                        s++;
                    }
                    if ((TeacherType.TY_QY.getKey()).equals(b.getTeachertype())) {
                        e++;
                    }
                }
            }
        }
        teamNums.setSchoolTeacherNum(s);
        teamNums.setEnterpriseTeacherNum(e);
    }

    public Map<String,List<SysAttachment>> findPwEnterResultList(String pwEnterId) {
        Map<String,List<SysAttachment>> map= Maps.newHashMap();
        SysAttachment sysAttachment=new SysAttachment();
        sysAttachment.setUid(pwEnterId);
        List<SysAttachment> sysAttachmentList=sysAttachmentService.getFiles(sysAttachment);
        map.put("sysAttachmentList",sysAttachmentList);
        return map;
    }

    @Transactional(readOnly = false)
    public ApiResult savePwEnterChange(PwEnter pwEnter) {
        updateALLDetail(pwEnter);
        if(pwEnter.isCompanyIsChange()&&pwEnter.isTeamIsChange()&&pwEnter.isProjectIsChange()
                &&pwEnter.isSpaceIsChange()){
            return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,"没有数据变更");
        }
        //保存数据
        save(pwEnter);
        //保留管理变更记录
        pwApplyRecordService.saveAdminChangeRecord(pwEnter);
        //管理 保存 详情
        saveChangeDetail(pwEnter);
        return ApiResult.success(pwEnter);
    }

    private void saveChangeDetail(PwEnter pwEnter) {
        //if (pwEnter.isCompanyIsChange()) { //保存企业
        if (pwEnter.getPwCompany()!=null){
            savePwCompanyChange(pwEnter);
        }
        //}
        if (pwEnter.isTeamIsChange()) {  //变更团队 记录变更记录
            saveTeamChange(pwEnter);
        }
        if (!pwEnter.isProjectIsChange()) {
            return;
        }

        if(StringUtil.checkNotEmpty(pwEnter.getPwProjectList())){
            delPwProjectListByPwEnter(pwEnter.getId());
            for (PwProject pwProject : pwEnter.getPwProjectList()) {
                //保存项目
                pwProject.setId("");
                pwProjectService.save(pwProject);
                saveDetail(pwEnter.getId(), PwEnterType.PET_XM.getKey(), pwProject.getId());    //保存申报与项目关系
                if (StringUtil.checkNotEmpty(pwProject.getSysAttachmentList())) {
                    sysAttachmentService.saveBySysAttachmentVo(pwProject.getSysAttachmentList(), pwProject.getId(),     //保存项目附件
                            FileTypeEnum.S13, FileStepEnum.S1301);
                }
            }
        }
    }

    public Boolean checkPwEnterProject(PwProject pwProject) {

        List<PwProject> pwProjectList = pwProjectService.findListByPwProject(pwProject);
        if (StringUtil.checkNotEmpty(pwProjectList)) {
            return false;
        }
        return true;
    }

    public List<ProModel> getBackProjects(String pwEnterId) {
        PwEnter pw=get(pwEnterId);
        List<ProModel> proModelList=proModelService.findProjects(pw.getDeclareId());
        if(StringUtil.checkNotEmpty(proModelList)){
            for(ProModel proModel:proModelList){
                SysAttachment sysAttachment=new SysAttachment();
                sysAttachment.setUid(proModel.getId());
                List<SysAttachment> sysAttachmentList=sysAttachmentService.findList(sysAttachment);
                if(StringUtil.checkNotEmpty(sysAttachmentList)){
                    proModel.setFileInfo(sysAttachmentList);
                }
            }
        }
        return proModelList;
    }
}

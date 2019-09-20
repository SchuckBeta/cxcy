package com.oseasy.pro.modules.workflow.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import com.oseasy.act.modules.act.dao.ActDao;
import com.oseasy.act.modules.act.entity.Act;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwAuditInfo;
import com.oseasy.act.modules.actyw.entity.ActYwGform;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.service.ActYwAuditInfoService;
import com.oseasy.act.modules.actyw.service.ActYwGnodeService;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.actyw.tool.process.vo.FormPageType;
import com.oseasy.act.modules.actyw.tool.process.vo.RegType;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.AttachMentEntity;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.common.service.CommonService;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.project.vo.ProjectDeclareListVo;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProActTaskService;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.pro.modules.tpl.vo.IWparam;
import com.oseasy.pro.modules.tpl.vo.Wtype;
import com.oseasy.pro.modules.workflow.IWorkFlow;
import com.oseasy.pro.modules.workflow.WorkFlowService;
import com.oseasy.pro.modules.workflow.dao.ProModelGzsmxxDao;
import com.oseasy.pro.modules.workflow.entity.ProModelGzsmxx;
import com.oseasy.pro.modules.workflow.vo.ExpProModelVo;
import com.oseasy.sys.common.utils.IdUtils;
import com.oseasy.sys.modules.team.dao.TeamUserHistoryDao;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.entity.TeamUserHistory;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * proProjectMdService.
 *
 * @author zy
 * @version 2017-09-18
 */
@Service
@Transactional(readOnly = false)
public class ProModelGzsmxxService extends WorkFlowService<ProModelGzsmxxDao, ProModelGzsmxx, ExpProModelVo> implements IWorkFlow<ProModelGzsmxx, ProModel, ExpProModelVo> {
    public final static Logger logger = Logger.getLogger(ProModelGzsmxxService.class);

    @Autowired
    ActYwAuditInfoService actYwAuditInfoService;
    @Autowired
    private ProModelGzsmxxDao proModelGzsmxxDao;
    @Autowired
    ProModelService proModelService;
    @Autowired
    ActYwGnodeService actYwGnodeService;
    @Autowired
    ActTaskService actTaskService;
    @Autowired
    ProActTaskService proActTaskService;
    @Autowired
    ActYwService actYwService;
    @Autowired
    UserService userService;
    @Autowired
    SystemService systemService;
    @Autowired
    IdentityService identityService;
    @Autowired
    TaskService taskService;
    @Autowired
    ActDao actDao;
    @Autowired
    private SysAttachmentService sysAttachmentService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private ProjectDeclareService projectDeclareService;
    @Autowired
    TeamUserHistoryDao teamUserHistoryDao;

    /* (non-Javadoc)
     * @see com.oseasy.pro.modules.workflow.IWorkFlow#getTClass()
     */
    @Override
    public Class<ProModelGzsmxx> getTClass() {
        return ProModelGzsmxx.class;
    }

    /* (non-Javadoc)
     * @see com.oseasy.pro.modules.workflow.WorkFlowService#setIWorkFlow()
     */
    @Override
    public IWorkFlow<ProModelGzsmxx, ProModel, ExpProModelVo> setIWorkFlow() {
        return this;
    }

    @Override
    public WorkFlowService<ProModelGzsmxxDao, ProModelGzsmxx, ExpProModelVo> setWorkService() {
        return this;
    }

    @Transactional(readOnly = false)
    public JSONObject saveStep1(ProModelGzsmxx proModelGzsmxx) {
        JSONObject json = new JSONObject();

        ProModel proModel = proModelGzsmxx.getProModel();
        ActYw actYw = actYwService.get(proModel.getActYwId());
        if (actYw != null) {
            ProProject proProject = actYw.getProProject();
            if (proProject != null) {
                proModel.setProType(proProject.getProType());
                proModel.setType(proProject.getType());
            }
        }
        if (StringUtil.isEmpty(proModelGzsmxx.getProModel().getCompetitionNumber())) {
            proModelGzsmxx.getProModel().setCompetitionNumber(IdUtils.getProjectNumberByDb());
        }
        proModelService.save(proModel);
        proModelGzsmxx.setModelId(proModel.getId());

        List<TeamUserHistory> stus = proModel.getStudentList();
        List<TeamUserHistory> teas = proModel.getTeacherList();
        if ((stus != null && stus.size() > 0) || (teas != null && teas.size() > 0)) {
            commonService.disposeTeamUserHistoryOnSave(stus, teas, proModel.getActYwId(), proModel.getTeamId(), proModel.getId(), proModel.getYear());
        }
        super.save(proModelGzsmxx);

        json.put("ret", 1);
        json.put("msg", "保存成功");
        return json;
    }

    @Transactional(readOnly = false)
    public JSONObject saveStep2(ProModelGzsmxx proModelGzsmxx) {
        JSONObject json = new JSONObject();
        ProModel proModel = proModelGzsmxx.getProModel();
        if (StringUtil.isEmpty(proModel.getPName())) {
            json.put("ret", 0);
            json.put("msg", "保存失败，项目名称为必填项");
            return json;
        }

        if (proModelGzsmxxDao.checkMdProName(proModel.getPName(), proModel.getId(), proModel.getType()) > 0) {
            json.put("ret", 0);
            json.put("msg", "保存失败，该项目名称已经存在");
            return json;
        }
        ProModel model = proModelService.get(proModel.getId());
        model.setpName(proModel.getpName());
        model.setIntroduction(proModel.getIntroduction());
        proModelService.save(model);
        super.save(proModelGzsmxx);
        json.put("ret", 1);
        json.put("msg", "保存成功");
        return json;
    }

    @Transactional(readOnly = false)
    public void submit(ProModelGzsmxx proModelGzsmxx) {
        sysAttachmentService.saveByVo(proModelGzsmxx.getProModel().getAttachMentEntity(), proModelGzsmxx.getProModel().getId(), FileTypeEnum.S11,
                FileStepEnum.S1102);
        teamUserHistoryDao.updateFinishAsStart(proModelGzsmxx.getProModel().getId());
        ActYw actYw = actYwService.get(proModelGzsmxx.getProModel().getActYwId());
        proModelGzsmxx.getProModel().setSubTime(new Date());
        super.start(proModelGzsmxx, actYw, "pro_model");
    }

    //判断是否需要重新保存附件,true 需要保存
    private boolean checkFile(String pid, AttachMentEntity a) {
        SysAttachment s = new SysAttachment();
        s.setUid(pid);
        List<SysAttachment> list = sysAttachmentService.getFiles(s);
        if (list == null || list.size() == 0 || list.size() > 1) {
            return true;
        } else {
            if (a != null && a.getFielFtpUrl() != null && a.getFielFtpUrl().size() == 1 && a.getFielFtpUrl().get(0).equals(list.get(0).getUrl())) {
                return false;
            } else {
                return true;
            }
        }

    }


    /**
     * ???
     * @param page 查询参数
     * @param actywId
     * @param gnodeId
     * @param actYw
     * @param act
     * @param model
     * @return
     */
    @Override
    public Page<ProModelGzsmxx> findDataPage(Page<ProModelGzsmxx> page, Model model, String actywId, String gnodeId, ActYw actYw, Act act, ProModelGzsmxx proModelGzsmxx) {
        List<ActYwGnode> actYwGnodes = proActTaskService.getSubGnodeList(gnodeId, actYw.getGroupId());
        List<String> gnodeIdList = actYwGnodes.stream().map(e -> e.getId()).collect(Collectors.toList());
        List<String> recordIds = actTaskService.recordIds(act, gnodeIdList, actywId);
        ProModel proModel = null;
        if (StringUtils.isEmpty(proModelGzsmxx.getProModel())) {
            proModel = new ProModel();
        } else {
            proModel = proModelGzsmxx.getProModel();
        }
        proModelGzsmxx.setIds(recordIds);
        proModelGzsmxx.setPage(page);
        proModelGzsmxx.setProModel(proModel);
        boolean isScoreNode=false;
        ActYwGnode lastNode =null;
        if (recordIds.isEmpty()) {
            page.setList(new ArrayList<>(0));
        } else {
            List<ProModelGzsmxx> list = dao.findListByIds(proModelGzsmxx);
            //证书
            int i=0;
            for (ProModelGzsmxx gzsmxx : list) {
                // 查询团队指导老师的名称，多个以逗号分隔
                ProModel gzsmxxProModel = gzsmxx.getProModel();
                List<Team> team = teamService.findTeamUserName(gzsmxxProModel.getTeamId());
                List<String> names = team.stream().filter(e -> "2".equals(e.getTeamUserType())).map(e -> e.getuName())
                        .collect(Collectors.toList());
                if (!names.isEmpty()) {
                    gzsmxxProModel.getTeam().setuName(org.apache.commons.lang3.StringUtils.join(names, ","));
                }
                //上一个审核节点类型是否为评分
                if(i==0){
                    ActYwAuditInfo actYwAuditInfoIn=new ActYwAuditInfo();
                    actYwAuditInfoIn.setPromodelId(gzsmxxProModel.getId());
                    actYwAuditInfoIn.setGnodeId(actYwGnodes.get(0).getId());
                    ActYwAuditInfo lastActYwAuditInfoIn=actYwAuditInfoService.getGnodeByNextGnode(actYwAuditInfoIn);
                    if(lastActYwAuditInfoIn==null){
                        i++;
                    }else {
                        lastNode = actYwGnodeService.getByg(lastActYwAuditInfoIn.getGnodeId());
                        List<ActYwGform> formList=lastNode.getGforms();
                        for(ActYwGform actYwGform:formList){
                            if(RegType.RT_GE.getId().equals(actYwGform.getForm().getSgtype())){
                                isScoreNode = true;
                                model.addAttribute("isScore", "1");
                                break;
                            }
                        }
//
//                        if (lastNode != null && GnodeTaskType.GTT_PARALLEL.getKey().equals(lastNode.getTaskType())) {
//                            isScoreNode = true;
//                            model.addAttribute("isScore", "1");
//                        }
                        i++;
                    }
                }

                if(isScoreNode){
                    ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
                    actYwAuditInfo.setPromodelId(gzsmxxProModel.getId());
                    actYwAuditInfo.setGnodeId(lastNode.getId());
                    String gscore = String.valueOf(actYwAuditInfoService.getAuditAvgInfo(actYwAuditInfo));
                    gzsmxxProModel.setgScore(gscore);
                }
                // 项目结果
                if (org.apache.commons.lang3.StringUtils.isNotBlank(gzsmxxProModel.getEndGnodeId()) && Const.YES.equals(gzsmxxProModel.getState())) {// 流程已结束
                    ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
                    actYwAuditInfo.setGnodeId(gzsmxxProModel.getEndGnodeId());
                    actYwAuditInfo.setPromodelId(gzsmxxProModel.getId());
                    ActYwGnode endNode = actYwGnodeService.get(gzsmxxProModel.getEndGnodeId());
                    proModel.setFinalResult((endNode != null ? endNode.getName() : "")
                            + proModelService.getStateByAuditInfo(actYwAuditInfoService.findList(actYwAuditInfo)));
                }

            }
            if(isScoreNode) {
                Collections.sort(list, new Comparator<ProModelGzsmxx>() {
                    @Override
                    public int compare(ProModelGzsmxx o1, ProModelGzsmxx o2) {
                        //会把集合里面的对象两两传进方法里面比较，这里比较Score，降序就O2-O1，升序就O1-O2
                        String score1 = o1.getProModel().getgScore();
                        String score2 = o2.getProModel().getgScore();
                        if (StringUtils.isEmpty(score1)) {
                            score1 ="0";
                            o1.getProModel().setgScore(score1);
                        }
                        if (StringUtils.isEmpty(score2)) {
                            score2 = "0";
                            o2.getProModel().setgScore(score2);
                        }
                        return Integer.parseInt(score2) - Integer.parseInt(score1);
                    }
                });
            }
            page.setList(list);
        }
        model.addAttribute("proModelGzsmxx", proModelGzsmxx);
        return page;
    }

    @Override
    public Page<ProModelGzsmxx> findQueryPage(Page<ProModelGzsmxx> page, Model model, String actywId, ActYw actYw, ProModelGzsmxx proModelGzsmxx) {
        String key = ActYw.getPkey(actYw);
        Act act= new Act();
        act.setProcDefKey(key);  //流程标识
        List<String> recordIds = actTaskService.queryRecordIds(act, actywId);
        //增加导入的数据
        ProModel proModel = null;
        if (StringUtils.isEmpty(proModelGzsmxx.getProModel())) {
            proModel = new ProModel();
        } else {
            proModel = proModelGzsmxx.getProModel();
        }
        proModel.setActYwId(actywId);
        proModelGzsmxx.setProModel(proModel);
        model.addAttribute("proModelGzsmxx", proModelGzsmxx);
        List<ProModel> importList = proModelService.findImportList(proModelGzsmxx.getProModel());
        if(!importList.isEmpty()){
            List<String> importIds = importList.stream().map(e -> e.getId()).collect(Collectors.toList());
            recordIds.addAll(importIds);
        }
        return super.findPage(page, proModelGzsmxx, recordIds);
    }

    @Override
    public Page<ProModelGzsmxx> findAssignPage(Page<ProModelGzsmxx> page, Model model, String actywId, ActYw actYw, ProModelGzsmxx proModelGzsmxx) {
        String gnodeId = super.getGnodeId(proModelGzsmxx, actywId);
        String key = ActYw.getPkey(actYw);
        Act act= new Act();
        act.setProcDefKey(key);  //流程标识
        List<String> gnodeIdList = new ArrayList<String>();
        gnodeIdList.add(gnodeId);
        List<String> recordIds = actTaskService.recordIdsWithoutUser(act, gnodeIdList, actywId);
        ProModel proModel = null;
        if (StringUtils.isEmpty(proModelGzsmxx.getProModel())) {
            proModel = new ProModel();
        } else {
            proModel = proModelGzsmxx.getProModel();
        }
        proModel.setActYwId(actywId);
        proModel.setGnodeId(gnodeId);
        proModelGzsmxx.setProModel(proModel);
        model.addAttribute("proModelGzsmxx", proModelGzsmxx);
        return super.findAssignPage(page, proModelGzsmxx, recordIds);
    }

    @Override
    public String applayForm(FormPageType fpageType, Model model, HttpServletRequest request, HttpServletResponse response, ProModel proModel, ProProject proProject, ActYw actYw) {
        User user = UserUtils.getUser();
        ProModelGzsmxx proModelGzsmxx = new ProModelGzsmxx();
        if (!StringUtils.isEmpty(proModel.getId())) {
            proModelGzsmxx = super.getByProModelId(proModel.getId());
        }
        model.addAttribute("proModelGzsmxx", proModelGzsmxx);
        model.addAttribute("teams", projectDeclareService.findTeams(user.getId(), ""));
        model.addAttribute("cuser", user);
        model.addAttribute("isSubmit", 0);
        model.addAttribute("wprefix", IWparam.getFileTplPreFix());
        model.addAttribute("wtypes", Wtype.toJson());
        return super.applayForm(fpageType, model, request, response, proModel, proProject, actYw);
    }

    @Override
    public void audit(String gnodeId,String proModelId, Model model) {
        ProModelGzsmxx proModelGzsmxx = super.getByProModelId(proModelId);
        SysAttachment sa = new SysAttachment();
        sa.setUid(proModelGzsmxx.getProModel().getId());
        sa.setType(FileTypeEnum.S11);
        sa.setFileStep(FileStepEnum.S1102);
        super.audit(proModelGzsmxx, model, sa);
    }

    @Override
    public void saveAddPro(ProModel proModel) {

    }

    @Override
    public void auditByGateWay(ProModel proModel, String gnodeId, HttpServletRequest request) {
        proModelService.auditWithGateWay(proModel, gnodeId);
    }

    @Override
    public void gcontestEdit(ProModel proModel, HttpServletRequest request, Model model) {
        ProModelGzsmxx proModelGzsmxx = super.getByProModelId(proModel.getId());
        model.addAttribute("regionName",proModelGzsmxx.getRegionName()) ;
        model.addAttribute("regionGroupName",proModelGzsmxx.getRegionGroupName()) ;
    }

    @Override
    public void projectEdit(ProModel proModel, HttpServletRequest request, Model model) {

    }

    @Override
   	public void saveFormData(ProModel proModel, HttpServletRequest request) {

   	}

    @Override
    public List<ExpProModelVo> exportData(Page<ProModelGzsmxx> page, ProModelGzsmxx proModelGzsmxx) {
        proModelGzsmxx.setPage(page);
        try {
            return dao.export(proModelGzsmxx);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    @Override
    public void exportData(HttpServletRequest request, HttpServletResponse response, String tempPath, ActYw actyw, ActYwGnode gnode, List<String> pids, ProModelGzsmxx proModelGzsmxx) {

    }

    /* (non-Javadoc)
     * @see com.oseasy.pro.modules.workflow.IWorkFlow#exportDataQuery(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.String, com.oseasy.act.modules.actyw.entity.ActYw, com.oseasy.act.modules.actyw.entity.ActYwGnode, java.util.List, com.oseasy.pro.modules.workflow.IWorkRes)
     */
    @Override
    public void exportDataQuery(HttpServletRequest request, HttpServletResponse response, ProModelGzsmxx t) {
        // TODO Auto-generated method stub
    }

    @Override
    public JSONObject submit(ProModel proModel, JSONObject js) {
        js=proModelService.submit(proModel,js);
        return js;
    }

    @Override
    @Transactional(readOnly = false)
    public JSONObject saveProjectEdit(ProModel proModel, HttpServletRequest request) throws Exception {
        return proModelService.saveProjectEdit(proModel,request);
    }

    @Override
    public void reportForm(Model model, HttpServletRequest request, HttpServletResponse response, String proModelId) {

    }

    @Override
    public void addFrontParam(ProjectDeclareListVo v) {

    }

    @Override
    public void indexTime(Map<String, String> lastpro) {

    }
}
package com.oseasy.pro.modules.workflow;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.google.common.collect.Maps;
import com.oseasy.act.modules.act.dao.ActDao;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwAuditInfo;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.service.ActYwAuditInfoService;
import com.oseasy.act.modules.actyw.service.ActYwGnodeService;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowProjectType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormPageType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormTheme;
import com.oseasy.act.modules.actyw.tool.process.vo.GnodeTaskType;
import com.oseasy.act.modules.actyw.utils.ActYwUtils;
import com.oseasy.act.modules.actyw.vo.ActYwRuntimeException;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.com.pcore.common.config.CorePages;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.AttachMentEntity;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.modules.cert.dao.SysCertInsDao;
import com.oseasy.pro.modules.cert.vo.SysCertInsVo;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.pro.modules.promodel.tool.process.vo.FormPage;
import com.oseasy.pro.modules.proprojectmd.entity.ProModelMd;
import com.oseasy.pro.modules.proprojectmd.service.ProModelMdService;
import com.oseasy.pro.modules.workflow.entity.ProModelGzsmxx;
import com.oseasy.pro.modules.workflow.entity.ProModelHsxm;
import com.oseasy.pro.modules.workflow.entity.ProModelMdGc;
import com.oseasy.pro.modules.workflow.entity.ProModelTlxy;
import com.oseasy.pro.modules.workflow.impl.WorkFetyPm;
import com.oseasy.pro.modules.workflow.service.ProModelGzsmxxService;
import com.oseasy.pro.modules.workflow.service.ProModelHsxmService;
import com.oseasy.pro.modules.workflow.service.ProModelMdGcService;
import com.oseasy.pro.modules.workflow.service.ProModelTlxyService;
import com.oseasy.sys.common.config.SysIds;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

public abstract class WorkFlowService<D extends WorkFlowDao<T, V>, T extends WorkFetyPm<T>, V extends IWorkDaoety> extends CrudService<D, T> {
    public final static Logger logger = Logger.getLogger(WorkFlowService.class);
    @Autowired
    ProModelService proModelService;
    @Autowired
    ActYwGnodeService actYwGnodeService;
    @Autowired
    ActTaskService actTaskService;
    @Autowired
    ActYwService actYwService;
    @Autowired
    UserService userService;
    @Autowired
    SystemService systemService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    IdentityService identityService;
    @Autowired
    TaskService taskService;
    @Autowired
    ActDao actDao;
    @Autowired
    private SysAttachmentService sysAttachmentService;
    @Autowired
    private SysCertInsDao sysCertInsDao;

    @Autowired
    ActYwAuditInfoService actYwAuditInfoService;
    @Autowired
    TeamService teamService;
    @Autowired
    OaNotifyService oaNotifyService;
    @Autowired
    private CoreService coreService;
    private WorkFlowService workFlowService = setWorkService();
    private IWorkFlow<? extends IWorkRes, ? extends IWorkFety, ? extends IWorkRes> iworkFlow = setIWorkFlow();

    @Autowired
    private D flowDao;

    private Map<String,String> getTaskAssigns(List<T> subList, String gnodeid){
        Map<String,String> map=new HashMap<String,String>();
        List<String> param=new ArrayList<String>();
        if(subList.size()==0){
            return map;
        }
        for(T p:subList){
            param.add(p.getProModel().getId());
        }
        List<Map<String,String>> list=actYwGnodeService.getAssignUserNames(param, gnodeid);
        if(list!=null&&list.size()>0){
            for(Map<String,String> s:list){
                map.put(s.get("promodel_id"), s.get("anames"));
            }
        }
        return map;
    }

    private Map<String,List<SysCertInsVo>> getSysCertIns(List<T> subList){
        Map<String,List<SysCertInsVo>> map=new HashMap<String,List<SysCertInsVo>>();
        List<String> param=new ArrayList<String>();
        if(subList.size()==0){
            return map;
        }
        for(T p:subList){
            param.add(p.getId());
        }
        List<SysCertInsVo> list=sysCertInsDao.getSysCertIns(param);
        if(list!=null&&list.size()>0){
            for(SysCertInsVo s:list){
                List<SysCertInsVo> tem=map.get(s.getProid());
                if(tem==null){
                    tem=new ArrayList<SysCertInsVo>();
                    map.put(s.getProid(), tem);
                }
                tem.add(s);
            }
        }
        return map;
    }

    public void getFinalResult(T model) {
        ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
        actYwAuditInfo.setGnodeId(model.getProModel().getEndGnodeId());
        actYwAuditInfo.setPromodelId(model.getProModel().getId());
        ActYwGnode endNode = actYwGnodeService.get(model.getProModel().getEndGnodeId());
        model.getProModel().setFinalResult((endNode != null ? endNode.getName() : "")
                + proModelService.getStateByAuditInfo(endNode, actYwAuditInfoService.findList(actYwAuditInfo)));
    }

    public Page<T> findPage(Page<T> page, T t, List<String> recordIds) {
        t.setIds(recordIds);
        t.setPage(page);
        if (recordIds.isEmpty()) {
            page.setList(new ArrayList<>(0));
        } else {
            List<T> list = flowDao.findListByIds(t);
            //证书
            Map<String,List<SysCertInsVo>> map=getSysCertIns(list);
            for (T model : list) {
                if(map!=null&&!map.isEmpty()){
                    model.getProModel().setScis(map.get(model.getId()));
                }
                // 查询团队指导老师的名称，多个以逗号分隔
                List<Team> team = teamService.findTeamUserName(model.getProModel().getTeamId());
                List<String> names = team.stream().filter(e -> "2".equals(e.getTeamUserType())).map(e -> e.getuName())
                        .collect(Collectors.toList());
                if (!names.isEmpty()) {
                    model.getProModel().getTeam().setuName(StringUtils.join(names, ","));
                }
                // 项目结果
                if (StringUtils.isNotBlank(model.getProModel().getEndGnodeId()) && Const.YES.equals(model.getProModel().getState())) {// 流程已结束
                    getFinalResult(model);
                }
            }
            page.setList(list);
        }
        return page;
    }

    public String getGnodeId(T t, String actywId) {
        String gnodeId = null;
        if (t.getProModel() != null) {
            gnodeId = t.getProModel().getGnodeId();
        }
        if (StringUtil.isEmpty(gnodeId)) {
            List<ActYwGnode> l = ActYwUtils.getAssignNodes(actywId);
            if (l != null && l.size() > 0) {
                gnodeId = l.get(0).getId();
            }
        }
        return gnodeId;
    }

    public Page<T> findAssignPage(Page<T> page, T t, List<String> recordIds) {

        t.setIds(recordIds);
        t.setPage(page);
        if (recordIds.isEmpty()) {
            page.setList(new ArrayList<>(0));
        } else {
            List<T> list = flowDao.findListByIdsUnAudit(t);
            //证书
            Map<String,String> ass=getTaskAssigns(list, t.getProModel().getGnodeId());
            Map<String,List<SysCertInsVo>> map=getSysCertIns(list);
            for (T model : list) {
                //证书
                if(map!=null&&!map.isEmpty()){
                    model.getProModel().setScis(map.get(model.getId()));
                }
                //指派人
                if(ass!=null&&!ass.isEmpty()){
                    model.getProModel().setTaskAssigns(ass.get(model.getProModel().getId()));
                }
                // 查询团队指导老师的名称，多个以逗号分隔
                List<Team> team = teamService.findTeamUserName(model.getProModel().getTeamId());
                List<String> names = team.stream().filter(e -> "2".equals(e.getTeamUserType())).map(e -> e.getuName())
                        .collect(Collectors.toList());
                if (!names.isEmpty()) {
                    model.getProModel().getTeam().setuName(StringUtils.join(names, ","));
                }
                // 项目结果
                if (StringUtils.isNotBlank(model.getProModel().getEndGnodeId()) && Const.YES.equals(model.getProModel().getState())) {// 流程已结束
                    getFinalResult(model);
                }
            }
            page.setList(list);
        }
        return page;
    }

    @Transactional(readOnly = false)
    public JSONObject saveModel(T t) {
        JSONObject js = new JSONObject();
        ProModel proModel = t.getProModel();
        if (StringUtil.isEmpty(proModel.getPName())) {
            js.put("ret", 0);
            js.put("msg", "保存失败，项目名称为必填项");
            return js;
        }
        if (flowDao.checkMdProName(proModel.getPName(), proModel.getId(), proModel.getType()) > 0) {
            js.put("ret", 0);
            js.put("msg", "保存失败，该项目名称已经存在");
            return js;
        }
        workFlowService.save(t);
        t.setModelId(proModel.getId());
        super.save(t);
        js.put("ret", 1);
        js.put("msg", "保存成功");
        return js;
    }

    @Transactional(readOnly = false)
    public void uploadFile(T t) {
        sysAttachmentService.saveByVo(t.getAttachMentEntity(), t.getProModel().getId(), FileTypeEnum.S11,
                FileStepEnum.S1102);
    }

    public void start(T t, ActYw actYw, String businessKey) {
        if (actYw == null) {
            throw new ActYwRuntimeException("流程配置故障（审核流程不存在），请联系管理员!");
        }
        ActYwGnode actYwNextGnode = actTaskService.getStartNextGnode(ActYw.getPkey(actYw));

        List<Role> roleList=actYwNextGnode.getRoles();

        List<String> roles =new ArrayList<String>();
        String nodeRoleId = null;
        if(StringUtil.checkNotEmpty(roleList)){
            nodeRoleId = StringUtil.listIdToStr(roleList, StringUtil.LINE_D);
        }

        if(actYwNextGnode.getIsAssign()!=null && actYwNextGnode.getIsAssign()){
            roles.clear();
            roles.add("assignUser");
        }else {
           if(StringUtil.checkNotEmpty(roleList)){
               roles = proModelService.getUsersByRoleList(t.getModelId(),roleList);

           }else{
               throw new ActYwRuntimeException("找不到流程下一步审核人员，请联系管理员!");
           }
        }
        if (roles.size() <= 0) {
            throw new ActYwRuntimeException("找不到流程下一步审核人员，请联系管理员!");
        }
        Map<String, Object> vars = new HashMap<String, Object>();
        vars = t.getProModel().getVars();
        if (actYwNextGnode != null && GnodeTaskType.GTT_NONE.getKey().equals(actYwNextGnode.getTaskType())) {
            vars.put(ActYwTool.FLOW_ROLE_ID_PREFIX + nodeRoleId, roles);
        } else {
            vars.put(ActYwTool.FLOW_ROLE_ID_PREFIX + nodeRoleId + ActYwTool.FLOW_ROLE_POSTFIX_S, roles);
        }
        String key = ActYw.getPkey(actYw);
        User user = UserUtils.getUser();
        identityService.setAuthenticatedUserId(user.getId());
        //TenantConfig.getCacheTenant()
        ProcessInstance procIns = runtimeService.startProcessInstanceByKeyAndTenantId(key, businessKey + StringUtil.MAOH + t.getId(), vars, TenantConfig.getCacheTenant());
        // 流程id返写业务表
        if (procIns == null) {
            throw new ActYwRuntimeException("流程配置故障（审核流程未启动），请联系管理员!");
        }

        ProModel proModel = proModelService.get(t.getProModel().getId());
        proModel.setProcInsId(procIns.getId());
        proModel.setSubStatus(Const.YES);
        proModelService.save(proModel);
        //添加申报消息
        //User rec_User = UserUtils.get(SysIds.);
        if(!(actYwNextGnode.getIsAssign()!=null && actYwNextGnode.getIsAssign())) {
            oaNotifyService.sendOaNotifyByTypeAndUser(user, roles, "项目申报", user.getName() + "申报" + actYw.getProProject().getProjectName(),
                    OaNotify.Type_Enum.TYPE18.getValue(), proModel.getId());
        }
        boolean isTeacher=false;
        for(Role role:roleList){
            if(role.getId().equals(coreService.getByRtype(CoreSval.Rtype.TEACHER.getKey()).getId())){
                isTeacher=true;
                break;
            }
        }
        if (isTeacher) {
            String taskId = actTaskService.getTaskidByProcInsId(proModel.getProcInsId());
            taskService.claim(taskId, roles.get(0));
        }
    }


    public void audit(T t, Model model, SysAttachment attachment) {

        List<SysAttachment> fileListMap = sysAttachmentService.getFiles(attachment);
        if (fileListMap != null) {
            model.addAttribute("sysAttachments", fileListMap);
        }
        String attrName = StringUtil.initialToLowCase(t.getClass().getSimpleName());
        model.addAttribute(attrName, t);
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

//    @Override
    public String applayForm(FormPageType fpageType, Model model, HttpServletRequest request, HttpServletResponse response, ProModel proModel, ProProject proProject, ActYw actYw) {
        FormTheme formTheme = actYw.getFtheme();
        if (formTheme != null) {
            FormPage fpage = FormPage.getByKey(formTheme, actYw.getFptype().getKey(), fpageType.getKey());
            if (FlowProjectType.PMT_XM.equals(actYw.getFptype())) {
                // 参数实现已经移动至实现类FppMd
                fpage.getParam().init(model, request, response, new Object[]{});
                fpage.getParam().initSysAttachment(model, request, response,
                        new Object[]{proModel, sysAttachmentService, workFlowService});
                return FormPage.getAbsUrl(actYw, fpageType, null);
            } else {
                fpage.getParam().init(model, request, response, new Object[]{});
                fpage.getParam().initSysAttachment(model, request, response,
                        new Object[]{proModel, sysAttachmentService});
                return FormPage.getAbsUrl(actYw, fpageType, null);
            }
        }
        return CorePages.ERROR_404.getIdxUrl();
    }

    public String viewForm(FormPageType fpageType, Model model, HttpServletRequest request, HttpServletResponse response, ProModel proModel, ActYw actYw) {
        FormTheme formTheme = actYw.getFtheme();
        T t = getByProModelId(proModel.getId());
        String attrName = StringUtil.initialToLowCase(t.getClass().getSimpleName());
        model.addAttribute(attrName, t);
        if (formTheme != null) {
            FormPage fpage = FormPage.getByKey(formTheme, actYw.getFptype().getKey(), fpageType.getKey());
            //参数实现已经移动至实现类FppMd
            fpage.getParam().init(model, request, response, new Object[]{});
            fpage.getParam().initSysAttachment(model, request, response, new Object[]{proModel, sysAttachmentService, workFlowService});
            return FormPage.getAbsUrl(actYw, fpageType, null);
        }
        return CorePages.ERROR_404.getIdxUrl();
    }

    public T getByProModelId(String proModelId) {
        return (T) flowDao.getByProModelId(proModelId);
    }

    /**
     * 自定义流程导出Excel和附件生成目录结构的方法.
     * @param request
     * @param response
     * @param tempPath
     * @param gnode
     * @param pids
     * @param tm
     * @return
     */
    public Map<String, List<IWorkRes>> exportDataMap(HttpServletRequest request, HttpServletResponse response, String tempPath, ActYw actyw, ActYwGnode gnode, List<String> pids, IWorkRes tm) {
        if(iworkFlow instanceof ProModelMdService){
            //民大
            ProModelMdService workFlow = ((ProModelMdService)iworkFlow);
            Page<ProModelMd> page = new Page<ProModelMd>(request, response);
            page.setPageSize(Page.MAX_PAGE_SIZE);
            ProModelMd t = (ProModelMd) tm;
            t.setIds(pids);
            return IWorkFlow.exportDataMap(workFlow.exportData(page, t));
        }else if(iworkFlow instanceof ProModelMdGcService){
            //互联网+大赛
            ProModelMdGcService workFlow = ((ProModelMdGcService)iworkFlow);
            Page<ProModelMdGc> page = new Page<ProModelMdGc>(request, response, Page.MAX_PAGE_SIZE);
            ProModelMdGc t = (ProModelMdGc) tm;
            t.setIds(pids);
            return IWorkFlow.exportDataMap(workFlow.exportData(page, t));
        }else if(iworkFlow instanceof ProModelGzsmxxService){
            //桂子山
            ProModelGzsmxxService workFlow = ((ProModelGzsmxxService)iworkFlow);
            Page<ProModelGzsmxx> page = new Page<ProModelGzsmxx>(request, response, Page.MAX_PAGE_SIZE);
            ProModelGzsmxx t = (ProModelGzsmxx) tm;
            t.setIds(pids);
            return IWorkFlow.exportDataMap(workFlow.exportData(page, t));
        }else if(iworkFlow instanceof ProModelTlxyService){
            //铜陵学院
            ProModelTlxyService workFlow = ((ProModelTlxyService)iworkFlow);
            Page<ProModelTlxy> page = new Page<ProModelTlxy>(request, response, Page.MAX_PAGE_SIZE);
            ProModelTlxy t = (ProModelTlxy) tm;
            t.setIds(pids);
            return IWorkFlow.exportDataMap(workFlow.exportData(page, t));
        }else if(iworkFlow instanceof ProModelHsxmService){
            //铜陵学院
            ProModelHsxmService workFlow = ((ProModelHsxmService)iworkFlow);
            Page<ProModelHsxm> page = new Page<ProModelHsxm>(request, response, Page.MAX_PAGE_SIZE);
            ProModelHsxm t = (ProModelHsxm) tm;
            t.setIds(pids);
            return IWorkFlow.exportDataMap(workFlow.exportData(page, t));        }
        logger.error("当前服务未定义");
        return Maps.newHashMap();
    }

    public Class<? extends IWorkRes> getTClass() {
        Class<? extends IWorkRes> tClass = (Class<? extends IWorkRes>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        return tClass;
    }

    public abstract WorkFlowService<?, ?, ?> setWorkService();
    public abstract IWorkFlow<? extends IWorkRes, ? extends IWorkFety, ? extends IWorkRes> setIWorkFlow();
}

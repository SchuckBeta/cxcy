package com.oseasy.auy.modules.act.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Lists;
import com.oseasy.act.common.config.ActTypes;
import com.oseasy.act.modules.actyw.entity.ActYwStep;
import com.oseasy.act.modules.actyw.service.*;
import com.oseasy.auy.modules.cms.service.AuyProProjectService;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.pcore.modules.syt.entity.SysTenant;
import com.oseasy.com.pcore.modules.syt.service.SysTenantService;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import org.activiti.engine.RepositoryService;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.act.service.ActModelService;
import com.oseasy.act.modules.actyw.dao.ActYwGroupDao;
import com.oseasy.act.modules.actyw.dao.ActYwGtimeDao;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.act.modules.actyw.tool.apply.IAconfig;
import com.oseasy.act.modules.actyw.tool.apply.impl.Aconfig;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowProjectType;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowType;
import com.oseasy.act.modules.actyw.tool.project.ActProParamVo;
import com.oseasy.act.modules.actyw.tool.project.ActProRunner;
import com.oseasy.act.modules.actyw.tool.project.ActProStatus;
import com.oseasy.act.modules.actyw.tool.project.IActProDeal;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.act.modules.pro.service.ProProjectService;
import com.oseasy.auy.modules.act.tool.project.impl.ActProAppointment;
import com.oseasy.auy.modules.act.tool.project.impl.ActProEnter;
import com.oseasy.auy.modules.act.tool.project.impl.ActProModel;
import com.oseasy.auy.modules.act.tool.project.impl.ActProModelGcontest;
import com.oseasy.auy.modules.act.tool.project.impl.ActProScore;
import com.oseasy.cms.modules.cms.entity.Category;
import com.oseasy.cms.modules.cms.service.CategoryService;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.scr.modules.scr.entity.ScoRapply;
import com.oseasy.sys.modules.sys.entity.SysNumberRule;
import com.oseasy.sys.modules.sys.service.SysNumberRuleService;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 项目流程关联Service.
 *
 * @author chenhao
 * @version 2017-05-23
 */
@Service
@Transactional(readOnly = true)
public class AuyActYwService{
	protected static final Logger logger = Logger.getLogger(AuyActYwService.class);
	@Autowired
	ActYwGroupDao actYwGroupDao;
	@Autowired
	ActYwGtimeDao actYwGtimeDao;
	@Autowired
    ActYwService actYwService;
    @Autowired
    private CoreService coreService;
	@Autowired
	ActYwYearService actYwYearService;
    @Autowired
	private ProModelService proModelService;
    @Autowired
    private SysNumberRuleService sysNumberRuleService;
    @Autowired
    private ActYwGtimeService actYwGtimeService;
    @Autowired
    private ProProjectService proProjectService;
    @Autowired
    ActYwGnodeService actYwGnodeService;
	@Autowired
	private CategoryService categoryService;
    @Autowired
    private ActYwGroupService actYwGroupService;
    @Autowired
    private AuyProProjectService auyProProjectService;
    @Autowired
    private ActYwGroupRelationService actYwGroupRelationService;
    @Autowired
    private SysAttachmentService sysAttachmentService;
    @Autowired
    private ActYwStepService actYwStepService;
    @Autowired
    private SysTenantService sysTenantService;
    /**
     * 流程发布.
     *
     * @param actYw
     * @param repositoryService
     * @param request
     * @return
     */
    @Transactional(readOnly = false)
    public Boolean saveDeployTime(ActYw actYw, RepositoryService repositoryService,HttpServletRequest request) {
        return saveDeployTime(actYw, repositoryService, null, request);
    }

    /**
     * 流程发布和部署.
     *
     * @param actYw
     * @param repositoryService 流程服务
     * @param isUpdateYw        标识是否更新到业务表
     * @param request
     * @return
     */
    @Transactional(readOnly = false)
    public Boolean saveDeployTime(ActYw actYw, RepositoryService repositoryService, Boolean isUpdateYw, HttpServletRequest request) {
        // 新建
        if (actYw.getIsNewRecord()) {
            if (actYw.getIsDeploy() == null) {
                actYw.setIsDeploy(false);
            }
            actYw.setId(IdGen.uuid());
            actYw.setIsNewRecord(true);
            // 生成类型
            if ((actYw.flowType() != null) && ((actYw.getProProject() != null)) && StringUtil.isEmpty(actYw.getProProject().getType())) {
                 Aconfig aconfig = IAconfig.genAconfig(actYw.flowType(), proProjectService);
                 if((!aconfig.getHasPtype()) && StringUtil.isNotEmpty(aconfig.getDefPtypeks())){
                     actYw.getProProject().setType(aconfig.getDefPtypeks());
                 }
            }

            proProjectService.save(actYw.getProProject());
            actYw.setRelId(actYw.getProProject().getId());
            actYwService.save(actYw);
        } else {
            // 修改
            ActYw actOld = actYwService.get(actYw.getId());
            // 修改发布
            if (actYw.getIsDeploy()) {
                if (actYw.getGroupId() != null) {
                    deleteModel(actOld);
                    saveActRunner(actYw);
                    proProjectService.save(actYw.getProProject());
                    actYwService.deploy(actYw, repositoryService, isUpdateYw);// 执行流程模型发布并部署
                }
            } else {
                deleteModel(actOld);
                //删除数据
                proModelService.clearPreReleaseData(actYw.getId());
                //hideModelCategory(actOld);
                // 项目无法修改
                proProjectService.save(actYw.getProProject());
                actYwService.save(actYw);
            }
        }
        if (StringUtil.isNotEmpty(actYw.getShowTime()) && ((actYw.getShowTime()).equals(Const.SHOW))) {
            actYwService.addGtimeNewYear(actYw.getProProject().getYear(),actYw, request);
        }
        return true;
    }

    @Transactional(readOnly = false)
    public Boolean saveJsonActYw(ActYw actYw) {
        // 新建
        if (actYw.getIsNewRecord()) {
            if ((actYw.getProProject() == null)) {
                return false;
            }
            if (actYw.getIsDeploy() == null) {
                actYw.setIsDeploy(false);
            }
            actYw.setId(IdGen.uuid());
            actYw.setIsNewRecord(true);
            // 生成类型
            if ((actYw.flowType() != null) && ((actYw.getProProject() != null)) && StringUtil.isEmpty(actYw.getProProject().getType())) {
                 Aconfig aconfig = IAconfig.genAconfig(actYw.flowType(), proProjectService);
                 if((!aconfig.getHasPtype()) && StringUtil.isNotEmpty(aconfig.getDefPtypeks())){
                     actYw.getProProject().setType(aconfig.getDefPtypeks());
                 }
            }
            proProjectService.save(actYw.getProProject());
            actYw.setRelId(actYw.getProProject().getId());
            actYwService.save(actYw);
        }
        if (StringUtil.isNotEmpty(actYw.getShowTime()) && ((actYw.getShowTime()).equals(Const.SHOW))) {
            actYwService.addJsonGtime(actYw.getProProject().getYear(),actYw);
        }
        return true;
    }

    //保存学校模板项目
    @Transactional(readOnly = false)
    public Boolean saveJsonNcActYw(ActYw actYw) {
        // 新建
        if (actYw.getIsNewRecord()) {
            if ((actYw.getProProject() == null)) {
                return false;
            }
            if (actYw.getIsDeploy() == null) {
                actYw.setIsDeploy(false);
            }
            actYw.setId(IdGen.uuid());
            actYw.setIsNewRecord(true);
            // 生成类型
            if ((actYw.flowType() != null) && ((actYw.getProProject() != null)) && StringUtil.isEmpty(actYw.getProProject().getType())) {
                 Aconfig aconfig = IAconfig.genAconfig(actYw.flowType(), proProjectService);
                 if((!aconfig.getHasPtype()) && StringUtil.isNotEmpty(aconfig.getDefPtypeks())){
                     actYw.getProProject().setType(aconfig.getDefPtypeks());
                 }
            }

            proProjectService.save(actYw.getProProject());
            actYw.setRelId(actYw.getProProject().getId());
            actYwService.saveNc(actYw);
        }
        if (StringUtil.isNotEmpty(actYw.getShowTime()) && ((actYw.getShowTime()).equals(Const.SHOW))) {
            actYwService.addJsonGtime(actYw.getProProject().getYear(),actYw);
        }
        return true;
    }


    public Menu getMenuRid(ActYw actYw) {
        String tenantId = TenantConfig.getCacheTenant();
        Menu npMune = null;
        String flowType = actYw.getGroup().getFlowType();
        if (tenantId.equals(CoreIds.NPR_SYS_TENANT.getId())){
            if (flowType.equals(ActTypes.DASAI)){
                //省大赛
                npMune = auyProProjectService.getActMenu(ActTypes.SITE_MENU_NPGCONTEST_ROOT);
            }else{
                npMune = auyProProjectService.getActMenu(ActTypes.SITE_MENU_NPPROJECT_ROOT);
            }
        }
        else{
            if (flowType.equals(ActTypes.DASAI)){
                //系统大赛
                npMune = auyProProjectService.getActMenu(ActTypes.SITE_MENU_GCONTEST_ROOT);
            }else{
                npMune = auyProProjectService.getActMenu(ActTypes.SITE_MENU_PROJECT_ROOT);
            }
        }
        return npMune;
    }

    @Transactional(readOnly = false)
    public void deleteAll(ActYw actYw) {
        beforeDelete(actYw);
        delActRunner(actYw);
        // 删除项目
        proProjectService.delete(actYw.getProProject());
        //删除编号规则
        SysNumberRule sysNumberRule=sysNumberRuleService.getRuleByAppType(actYw.getId(),"");
        if(sysNumberRule!=null){
            sysNumberRuleService.delete(sysNumberRule);
        }

        actYwService.delete(actYw);
    }

    /**
     * 删除自定义项目约束性校验
     * 1，有项目数据，不可删除
     * 2，无项目数据，
     * @param actYw
     */
    public void beforeDelete(ActYw actYw){
        ProModel proModel = new ProModel();
        proModel.setActYwId(actYw.getId());
        List<ProModel> list = proModelService.findList(proModel);
        if(!list.isEmpty()){
            throw new RuntimeException("有项目数据，无法删除项目");
        }
    }

    @Transactional(readOnly = false)
    public void deleteProject(ActYw actYw) {
        ProProject proProject = actYw.getProProject();
        // 删除菜单
        if (proProject.getMenuRid() != null) {
            Menu menu = coreService.getMenu(proProject.getMenuRid());
            if (menu != null) {
                coreService.deleteMenu(menu);
            }
        }
        // 删除栏目
        if (proProject.getCategoryRid() != null) {
            Category category = categoryService.get(proProject.getCategoryRid());
            if (category != null) {
                categoryService.delete(category);
            }
        }
    }

    @Transactional(readOnly = false)
    public void saveActywRel(ActYw actYw) {
        actYw.setIsNewRecord(true);
        if ((actYw.getGroup() != null) && StringUtil.isNotEmpty(actYw.getGroupId())) {
            actYw.setGroup(actYwGroupService.get(actYw.getGroupId()));
        }
        if ((actYw.getGroup() != null) && StringUtil.isNotEmpty(actYw.getGroup().getFlowType())) {
            FlowType flowType = FlowType.getByKey(actYw.getGroup().getFlowType());
            StringBuffer proType = new StringBuffer();
            for (FlowProjectType fptype : flowType.getType().getTypes()) {
                proType.append(fptype.getKey());
                proType.append(StringUtil.DOTH);
            }
            actYw.getProProject().setProType(proType.toString());
        }
        //保存省项目
        Boolean isTrue = saveJsonActYw(actYw);

        //申报附件 时间轴附件
        if(isTrue){
            List<SysAttachment> sysList=actYw.getFileInfo();
            if(sysList!=null){
                List<SysAttachment> appList= Lists.newArrayList();
                List<SysAttachment> reportList= Lists.newArrayList();
                for(SysAttachment sysAttachment:sysList){
                    if(sysAttachment.getFtype().equals(FileTypeEnum.S18.getValue())){
                        appList.add(sysAttachment);
                    }
                    if(sysAttachment.getFtype().equals(FileTypeEnum.S19.getValue())){
                        reportList.add(sysAttachment);
                    }
                }
                sysAttachmentService.saveBySysAttachmentVo(appList,actYw.getId(), FileTypeEnum.S18, FileStepEnum.S1801);
                sysAttachmentService.saveBySysAttachmentVo(reportList,actYw.getId(), FileTypeEnum.S19, FileStepEnum.S1901);
            }
        }
        if(isTrue && StringUtil.checkNotEmpty(actYw.getChildActYwGtimeList())){
            String modelGroupId=actYwGroupRelationService.getModelActYwGroupIdByProv(actYw.getGroupId());
            if (modelGroupId != null) {
                ActYw modelActYw=new ActYw();
                modelActYw.setIsNewRecord(true);
                modelActYw.setGroupId(modelGroupId);
                ProProject proProject=actYw.getProProject();
                proProject.setId("");
                modelActYw.setProProject(proProject);
                if (StringUtil.isNotEmpty(modelActYw.getGroupId())) {
                    modelActYw.setGroup(actYwGroupService.get(modelActYw.getGroupId()));
                }
                if ((modelActYw.getGroup() != null) && StringUtil.isNotEmpty(modelActYw.getGroup().getFlowType())) {
                    FlowType flowType = FlowType.getByKey(modelActYw.getGroup().getFlowType());
                    StringBuffer proType = new StringBuffer();
                    for (FlowProjectType fptype : flowType.getType().getTypes()) {
                        proType.append(fptype.getKey());
                        proType.append(",");
                    }
                    modelActYw.getProProject().setProType(proType.toString());
                    modelActYw.setActYwGtimeList(actYw.getChildActYwGtimeList());
                }
                //保存模板项目
                Boolean modelSave = saveJsonNcActYw(modelActYw);
                //保存流程步骤
                ActYwStep actYwStep =actYwStepService.getActYwStepByGroupId(actYw.getGroupId());
                actYwStep.setStep(ActYwStep.StepEnmu.STEP5.getValue());
                actYwStep.setModelActywId(modelActYw.getId());
                actYwStepService.save(actYwStep);
            }
        }
    }

    @Transactional(readOnly = false)
    public void deleteModel(ActYw actYw) {
        deleteProject(actYw);
//      ProProject proProject = actYw.getProProject();
//      // 删除菜单
//      if (proProject.getMenuRid() != null) {
//          Menu menu = systemService.getMenu(proProject.getMenuRid());
//          if (menu != null) {
//              systemService.deleteMenu(menu);
//          }
//      }
//      // 删除栏目
//      if (proProject.getCategoryRid() != null) {
//          Category category = categoryService.get(proProject.getCategoryRid());
//          if (category != null) {
//              categoryService.delete(category);
//          }
//      }
        //查找已经发布同类的项目(删除栏目和菜单)
        ProProject proProject = actYw.getProProject();
        List<ActYw> relActYw= actYwService.findActYwListByRelIdAndState(proProject.getProType(),proProject.getType(),actYw.getTenantId());
        if(StringUtil.checkNotEmpty(relActYw)){
            for(int i=0;i<relActYw.size();i++ ){
                ActYw delActYw=relActYw.get(i);
                deleteProject(delActYw);
            }
        }

    }

    public ApiTstatus<ActYw> ajaxCheckDeploy(ActYw actYw) {
        ApiTstatus<ActYw> rstatus = new ApiTstatus<ActYw>();
        if (StringUtil.isNotEmpty(actYw.getId()) && (actYw.getIsDeploy() != null)) {
            if(actYw.getIsDeploy()){
                if((actYw.getGroup() != null) && ((ActYwGroup.GROUP_DEPLOY_0).equals(actYw.getGroup().getStatus()))){
                    rstatus.setStatus(false);
                    rstatus.setMsg("流程未发布，请先发布流程.");
                    return rstatus;
                }

                //List<ActYwGtime> timeList=actYwGtimeService.checkTimeByActYw(actYw.getId());
                int yearIndex=actYwYearService.checkYearTimeByActYw(actYw.getId());
                int timeIndex=actYwGtimeService.checkTimeIndex(actYw.getId());


                ActYwGnode actYwGnode = new ActYwGnode(new ActYwGroup(actYw.getGroupId()));
                List<ActYwGnode> sourcelist = actYwGnodeService.findListByMenu(actYwGnode);
                int yearNum=actYwYearService.checkYearByActYw(actYw.getId());
                if((yearIndex+timeIndex) != (sourcelist.size()+1)*yearNum){
                    rstatus.setStatus(false);
                    rstatus.setMsg("流程时间填写不完整，发布失败");
                    return rstatus;
                }

                SysNumberRule sysNumberRule = sysNumberRuleService.getRuleByAppType(actYw.getId(),"");
                if((sysNumberRule == null) && ActYw.checkNeedNum(actYw)){
                    rstatus.setStatus(false);
                    rstatus.setMsg("该类型项目没有设置编号规则，发布失败");
                    return rstatus;
                }
                String relId=actYw.getRelId();
                ProProject proProject=proProjectService.getWithId(relId);
                if(proProject == null){
                    rstatus.setStatus(false);
                    rstatus.setMsg("流程配置获取失败");
                    return rstatus;
                }
                //查找已经发布的项目
                List<ActYw> actYwList = actYwService.findActYwListByProProject(proProject.getProType(),proProject.getType(),actYw.getTenantId());
                if(StringUtil.checkNotEmpty(actYwList) && ((FlowProjectType.PMT_XM.getKey()).equals(actYw.getGroup().getType()) || (FlowProjectType.PMT_DASAI.getKey()).equals(actYw.getGroup().getType()))){
                    rstatus.setStatus(false);
                    rstatus.setMsg("同一类型项目只能发布一个，该类型项目已经发布一个，发布失败");
                    return rstatus;
                }else{
                    rstatus.setStatus(true);
                }
            }else {
                if(actYw.getIsPreRelease()!=null && actYw.getIsPreRelease()){
                    rstatus.setStatus(true);
                }else {
                    //流程状态
                    String res = findStateHaveData(actYw.getId());
                    if ("3".equals(res)) {
                        rstatus.setStatus(false);
                        rstatus.setMsg("流程中含有未完成项目,取消发布失败");
                        return rstatus;
                    } else {
                        rstatus.setStatus(true);
                    }
                }
            }
         }
         return rstatus;
    }


    public ApiTstatus<ActYw> ajaxCheckDelete(ActYw actYw) {
        ApiTstatus<ActYw> rstats = new ApiTstatus<ActYw>();
//        JSONObject js=new  JSONObject();
        if (StringUtil.isNotEmpty(actYw.getId()) && (actYw.getIsDeploy() != null)) {
            //流程状态
            String res = findStateHaveData(actYw.getId());
            actYw = actYwService.get(actYw);
            if((actYw == null)){
                rstats.setStatus(false);
                rstats.setMsg("该数据不存在,无法删除");
                rstats.setDatas(actYw);
                return rstats;
            }

            if((actYw.getIsDeploy())){
                rstats.setStatus(false);
                rstats.setMsg("该数据已发布无法删除");
                rstats.setDatas(actYw);
                return rstats;
            }

            if((actYw.getIsPreRelease())){
                rstats.setStatus(false);
                rstats.setMsg("该数据正式发布无法删除");
                rstats.setDatas(actYw);
                return rstats;
            }

            if ("1".equals(res)) {
                rstats.setMsg("删除成功");
            }else{
                rstats.setStatus(false);
                rstats.setMsg("流程中含有未完成项目");
            }
        }
        rstats.setDatas(actYw);
        return rstats;
    }

    public boolean findIsHaveData(ActYw actYw) {
        List<ProModel> list=proModelService.findIsHaveData(actYw.getId());
        if(StringUtil.checkNotEmpty(list)){//查找未完结数据
            List<ProModel> list2=proModelService.findIsOverHaveData(actYw.getId(),"1");
            if(StringUtil.checkNotEmpty(list2)){
                return true;
            }
        }
        return false;
    }

    //1：项目没有数据 2：项目有数据都已经结项 3：项目有数据还未结项
    public String  findStateHaveData(String actYwId) {
        List<ProModel> list=proModelService.findIsHaveData(actYwId);
        if(StringUtil.checkNotEmpty(list)){//查找未完结数据
            List<ProModel> list2=proModelService.findIsOverHaveData(actYwId,"1");
            if(StringUtil.checkNotEmpty(list2)){
                //项目有数据还未结项
                return "3";
            }else{
                //项目有数据都已经结项
                return "2";
            }
        }else{
            //项目没有数据
            return "1";
        }
    }

    /**
     * 流程部署方法的重写,需要继续维护.
     * @param actYw
     * @return
     */
    @Transactional(readOnly = false)
    public ActProStatus saveActRunner(ActYw actYw) {
        ActProRunner<IActProDeal> proRunner = new ActProRunner<IActProDeal>();
        return proRunner.execute(initActProParamVo(actYw, proRunner));
    }

    /**
     * 流程删除方法的重写,需要继续维护.
     * @param actYw
     * @return
     */
    @Transactional(readOnly = false)
    public ActProStatus delActRunner(ActYw actYw) {
        ActProRunner<IActProDeal> proRunner = new ActProRunner<IActProDeal>();
        ActProParamVo proParamVo = initActProParamVo(actYw, proRunner);
        if ((FlowType.FWT_DASAI).equals(proParamVo.getFlowType())
                || (FlowType.FWT_XM).equals(proParamVo.getFlowType())
                || (FlowType.FWT_APPOINTMENT).equals(proParamVo.getFlowType())
                || (FlowType.FWT_TECHNOLOGY).equals(proParamVo.getFlowType())
                || (FlowType.FWT_ENTER).equals(proParamVo.getFlowType())) {
            ProProject proProject = actYw.getProProject();
            Menu menu = coreService.getMenu(proProject.getMenuRid());
            // 删除菜单
            if (menu != null) {
                coreService.deleteMenu(menu);
            }

            // 删除栏目
            Category category = categoryService.get(proProject.getCategoryRid());
            if (category != null) {
                categoryService.delete(category);
            }
        }else{
            return proRunner.executeDel(proParamVo);
        }
        return null;
    }

    private ActProParamVo initActProParamVo(ActYw actYw, ActProRunner<IActProDeal> proRunner) {
        if((actYw == null) || (actYw.getGroup() == null)){
              return null;
        }

        if (StringUtil.isNotEmpty(actYw.getGroupId())) {
              actYw.setGroup(actYwGroupDao.get(actYw.getGroupId()));
        }
        ActProParamVo actProParamVo = new ActProParamVo(actYw);
        actProParamVo = ActProParamVo.delFlowType(actProParamVo);
        if (actProParamVo.getFlowType() == null) {
            return null;
        }

        if ((actProParamVo.getFlowType()).equals(FlowType.FWT_SCORE)) {
            ActProScore actProScore = new ActProScore();
            proRunner.setActProDeal(actProScore);
            proRunner.setFlowType(actProParamVo.getFlowType());
            actProParamVo.setActYw(actYw);
            actProParamVo.setProProject(actYw.getProProject());
            actProParamVo.setApply(new ScoRapply());
        } else if ((actProParamVo.getFlowType()).equals(FlowType.FWT_XM)) {
            ActProModel actProModel = new ActProModel();
            proRunner.setActProDeal(actProModel);
            proRunner.setFlowType(actProParamVo.getFlowType());
            actProParamVo.setActYw(actYw);
            actProParamVo.setProProject(actYw.getProProject());
            actProParamVo.setApply(new ProModel());
        } else if ((actProParamVo.getFlowType()).equals(FlowType.FWT_DASAI)) {
            // ActProProject actProProject=new ActProProject();
            ActProModelGcontest actProModelGcontest = new ActProModelGcontest();
            proRunner.setActProDeal(actProModelGcontest);
            proRunner.setFlowType(actProParamVo.getFlowType());
            actProParamVo.setActYw(actYw);
            actProParamVo.setProProject(actYw.getProProject());
            actProParamVo.setApply(new ProModel());
        } else if ((actProParamVo.getFlowType()).equals(FlowType.FWT_ENTER)) {
            // ActProProject actProProject=new ActProProject();
            ActProEnter actProEnter = new ActProEnter();
            proRunner.setActProDeal(actProEnter);
            proRunner.setFlowType(actProParamVo.getFlowType());
            actProParamVo.setActYw(actYw);
            actProParamVo.setProProject(actYw.getProProject());
        } else if ((actProParamVo.getFlowType()).equals(FlowType.FWT_APPOINTMENT)) {
            ActProAppointment actProAppointment = new ActProAppointment();
            proRunner.setActProDeal(actProAppointment);
            proRunner.setFlowType(actProParamVo.getFlowType());
            actProParamVo.setActYw(actYw);
            actProParamVo.setProProject(actYw.getProProject());
        } else {
            logger.warn("流程类型未定义!!!");
        }
        return actProParamVo;
    }

    @Transactional(readOnly = false)
    public void saveRule(SysNumberRule sysNumberRule) {
        sysNumberRuleService.save(sysNumberRule);

        SysTenant sysTenant=sysTenantService.get(TenantConfig.getCacheTenant());
        if (sysTenant!=null && (Sval.EmPn.NPROVINCE.getPrefix()).equals(sysTenant.getType())) {
            ActYw actYw=actYwService.get(sysNumberRule.getAppType());
            ActYwStep actYwStep = actYwStepService.getActYwStepByGroupId(actYw.getGroupId());
            actYwStep.setStep(ActYwStep.StepEnmu.STEP6.getValue());
            actYwStepService.save(actYwStep);
        }
    }

    /**********************************************************************************
     * IActYw流程接口实现方法
     **********************************************************************************/
}
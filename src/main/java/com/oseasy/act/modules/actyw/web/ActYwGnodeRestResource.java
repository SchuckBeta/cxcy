package com.oseasy.act.modules.actyw.web;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.oseasy.act.modules.actyw.entity.ActYwClazz;
import com.oseasy.act.modules.actyw.entity.ActYwForm;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.act.modules.actyw.entity.ActYwGstatus;
import com.oseasy.act.modules.actyw.entity.ActYwNode;
import com.oseasy.act.modules.actyw.entity.ActYwSgtype;
import com.oseasy.act.modules.actyw.entity.ActYwStatus;
import com.oseasy.act.modules.actyw.service.ActYwClazzService;
import com.oseasy.act.modules.actyw.service.ActYwFormService;
import com.oseasy.act.modules.actyw.service.ActYwGformService;
import com.oseasy.act.modules.actyw.service.ActYwGnodeService;
import com.oseasy.act.modules.actyw.service.ActYwGroleService;
import com.oseasy.act.modules.actyw.service.ActYwGroupService;
import com.oseasy.act.modules.actyw.service.ActYwGstatusService;
import com.oseasy.act.modules.actyw.service.ActYwGtimeService;
import com.oseasy.act.modules.actyw.service.ActYwGuserService;
import com.oseasy.act.modules.actyw.service.ActYwNodeService;
import com.oseasy.act.modules.actyw.service.ActYwSgtypeService;
import com.oseasy.act.modules.actyw.service.ActYwStatusService;
import com.oseasy.act.modules.actyw.tool.process.cmd.ActYwEcoper;
import com.oseasy.act.modules.actyw.tool.process.cmd.ActYwRunner;
import com.oseasy.act.modules.actyw.tool.process.cmd.Gpnode;
import com.oseasy.act.modules.actyw.tool.process.cmd.vo.ActYwGparam;
import com.oseasy.act.modules.actyw.tool.process.cmd.vo.ActYwPgnode;
import com.oseasy.act.modules.actyw.tool.process.impl.ActYwEngineImpl;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormClientType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormStyleType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormTheme;
import com.oseasy.act.modules.actyw.tool.process.vo.FormType;
import com.oseasy.act.modules.actyw.tool.process.vo.GnodeTaskType;
import com.oseasy.act.modules.actyw.tool.process.vo.GnodeType;
import com.oseasy.act.modules.actyw.tool.process.vo.NodeEtype;
import com.oseasy.act.modules.actyw.tool.process.vo.RegType;
import com.oseasy.act.modules.actyw.tool.process.vo.RtOutgoing;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

@RestController
@RequestMapping(value = "${adminPath}/actyw/gnode")
public class ActYwGnodeRestResource {
  @Autowired
  private ActYwGroupService actYwGroupService;
  @Autowired
  private ActYwGnodeService actYwGnodeService;
  @Autowired
  private ActYwNodeService actYwNodeService;
  @Autowired
  private ActYwGstatusService actYwGstatusService;
    @Autowired
    private ActYwStatusService actYwStatusService;
    @Autowired
    private ActYwClazzService actYwClazzService;
  @Autowired
  private ActYwGformService actYwGformService;
  @Autowired
  private ActYwFormService actYwFormService;
  @Autowired
  private ActYwGroleService actYwGroleService;
  @Autowired
  private ActYwGuserService actYwGuserService;
  @Autowired
  private ActYwGtimeService actYwGtimeService;
  @Autowired
  private ActYwSgtypeService actYwSgtypeService;
  @Autowired
  private SystemService systemService;
  @Autowired
  private ActYwRunner runner;
  @Autowired
  RepositoryService repositoryService;
  @Autowired
  RuntimeService runtimeService;

  private ActYwEngineImpl initEngine() {
    return new ActYwEngineImpl(actYwGnodeService,
            actYwNodeService, actYwFormService, actYwGstatusService,
            actYwGroleService, actYwGformService, actYwGuserService, actYwGtimeService);
  }

  @ModelAttribute
  public ActYwGnode get(@RequestParam(required=false) String id) {
    ActYwGnode entity = null;
    if (StringUtil.isNotBlank(id)) {
      entity = actYwGnodeService.get(id);
    }
    if (entity == null) {
      entity = new ActYwGnode();
    }
    return entity;
  }

    /****************************************************************************************************************
     * 新修改的接口.
     ***************************************************************************************************************/
    /**
     * 初始化流程基础数据.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/init", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ApiTstatus<ActYwGnode> init() {
        runner.setEngine(initEngine());
        ActYwGnode cur = new ActYwGnode();
        cur.setIsNewRecord(true);
        cur.setId(IdGen.uuid());
        cur.setName(GnodeType.GT_ROOT.getRemark() + IdGen.uuid());
        cur.setType(GnodeType.GT_ROOT.getId());
        cur.setNode(new ActYwNode(CoreIds.NCE_SYS_TREE_PROOT.getId()));
        cur.setParent(new ActYwGnode(CoreIds.NCE_SYS_TREE_PROOT.getId()));
        cur.setDelFlag(Const.YES);
        return runner.callExecute(ActYwEcoper.ECR_ADD, new ActYwPgnode(cur));
    }

    /**
     * 初始化流程基础数据.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/queryByGroup/{groupId}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ApiTstatus<Map<String, Object>> queryByGroup(@PathVariable("groupId") String groupId) {
        Map<String, Object> rmap = new HashMap<String, Object>();
        List<ActYwGnode> gnodes = actYwGnodeService.findListBygGroup(new ActYwGnode(new ActYwGroup(groupId)));
        if(StringUtil.checkNotEmpty(gnodes)){
            rmap.put(CoreJkey.JK_LIST, gnodes);
            rmap.put(ActYwGroup.JK_GROUP, actYwGroupService.get(groupId));
            return new ApiTstatus<Map<String, Object>>(true, "查询成功！", rmap);
        }
        return new ApiTstatus<Map<String, Object>>(false, "查询失败或结果为空！");
    }

    /**
     * 根据Ywid获取流程第一个任务节点.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxGetFirstTaskByYwid/{ywid}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ApiTstatus<ActYwGnode> ajaxGetFirstTaskByYwid(@PathVariable("ywid") String ywid) {
        ActYwGnode task = actYwGnodeService.getFirstTaskByYwid(ywid);
        if(task != null){
            return new ApiTstatus<ActYwGnode>(true, "查询成功！", task);
        }
        return new ApiTstatus<ActYwGnode>(false, "查询失败或结果为空！");
    }

    /**
     * 根据Ywid获取流程最后一个任务节点.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxGetLastTaskByYwid/{ywid}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ApiTstatus<ActYwGnode> ajaxGetLastTaskByYwid(@PathVariable("ywid") String ywid) {
        ActYwGnode task = actYwGnodeService.getLastTaskByYwid(ywid);
        if(task != null){
            return new ApiTstatus<ActYwGnode>(true, "查询成功！", task);
        }
        return new ApiTstatus<ActYwGnode>(false, "查询失败或结果为空！");
    }

    /**
     * 保存节点数据.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/saveAll", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<List<ActYwGnode>> saveAll(@RequestBody JSONObject gps) {
//        Date date1 = new Date();
//        logger.info("saveAll 开始，时间：[" + DateUtil.formatDate(date1, DateUtil.FMT_YYYY_MM_DD_HHmmss) + "]");
        Map<String, Class> classMap = new HashMap<String, Class>();
        classMap.put(CoreJkey.JK_LIST, Gpnode.class);
        classMap.put("preIds", RtOutgoing.class);
        classMap.put("roleIds", Role.class);
        classMap.put("userIds", User.class);
        classMap.put("formIds", ActYwForm.class);
        classMap.put("formListIds", ActYwForm.class);
        classMap.put("formNlistIds", ActYwForm.class);
        classMap.put("statusIds", ActYwStatus.class);
        classMap.put("clazzIds", ActYwClazz.class);
        classMap.put("outgoing", RtOutgoing.class);
        classMap.put("uiJson", JSONObject.class);
        classMap.put("uiHtml", String.class);
        classMap.put("temp", Boolean.class);

        ActYwGparam gparam = (ActYwGparam) JSONObject.toBean(gps, ActYwGparam.class, classMap);
        gparam.getUiJson().put(CoreJkey.JK_LIST, gps.getJSONArray(CoreJkey.JK_LIST).toString());
        runner.setEngine(initEngine());
//        Date date2 = new Date();
//        logger.info("saveAll Json->ActYwGparam 处理时间：[" + DateUtil.formatDate(date2, DateUtil.FMT_YYYY_MM_DD_HHmmss) + "]总耗时->" + (date2.getTime() - date1.getTime()));
//        ActYwRstatus<List<ActYwGnode>> ss = actYwGnodeService.savePl(runner, gparam);
//        Date date12 = new Date();
//        logger.info("saveAll 结束,返回结果，时间：[" + DateUtil.formatDate(date12, DateUtil.FMT_YYYY_MM_DD_HHmmss) + "]总耗时->" + (date12.getTime() - date1.getTime()));
//        return ss;

        return actYwGnodeService.savePl(runner, gparam);
    }

    /**
     * 获取节点状态类型数据.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxDictStatusType", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ApiTstatus<List<ActYwSgtype>> ajaxDictStatusType() {
        ActYwSgtype actYwSgtype=new ActYwSgtype();
        List<ActYwSgtype> list=actYwSgtypeService.findList(actYwSgtype);
        if(StringUtil.checkNotEmpty(list)){
            return new ApiTstatus<List<ActYwSgtype>>(true, "查询成功！",list);
        }else {
            return new ApiTstatus<List<ActYwSgtype>>(false, "查询失败！");
        }
    }

    /**
     * 获取节点元素类型数据.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxNodeEtypes", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ApiTstatus<List<NodeEtype>> ajaxNodeEtypes(Boolean isShow) {
        if(isShow != null){
            return new ApiTstatus<List<NodeEtype>>(true, "查询成功！", NodeEtype.getAll(isShow));
        }
        return new ApiTstatus<List<NodeEtype>>(true, "查询成功！", NodeEtype.getAll());
    }

    /**
     * 获取节点类别数据.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxGnodeTypes", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ApiTstatus<String> ajaxGnodeTypes() {
        return new ApiTstatus<String>(true, "查询成功！", (Arrays.asList(GnodeType.values())).toString());
    }

    /**
     * 获取任务节点类型.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxGnodeTaskTypes", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ApiTstatus<String> ajaxGnodeTaskTypes(Boolean isShow) {
        return new ApiTstatus<String>(true, "查询成功！", (GnodeTaskType.getAll(true)).toString());
    }

    /**
     * 获取节点对应的角色.
     * @return List
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxGnodeRole", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public List<Role> ajaxGnodeRole() {
        return systemService.findAllRole();
    }

//    @RequestMapping(value="getFlowTypes", method = RequestMethod.GET, produces = "application/json")
//    @ResponseBody
//    public List<Object> getFlowTypes(){
//
//    }


    /**
     * 获取表单表单组类型.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxFormThemes", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ApiTstatus<String> ajaxFormThemes() {
        return new ApiTstatus<String>(true, "查询成功！", (FormTheme.getAll()).toString());
    }

    /**
         * 获取节点对应的表单.
         * @param flowType 流程类型
         * @param hasList 是否有列表
         * @param theme 主题
         * @param proType 项目类型
         * @param type 节点类型
         * @return List
         */
        @ResponseBody
        @RequestMapping(value = "/ajaxGnodeForm/{flowType}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
        public List<ActYwForm> ajaxGnodeForm(@PathVariable("flowType") String flowType,
                @RequestParam(required = true) Boolean hasList,
                @RequestParam(required = false) Integer theme,
                @RequestParam(required = false) String proType,
                @RequestParam(required = false) String type) {
            ActYwForm pactYwForm = new ActYwForm();
            if (StringUtil.isNotEmpty(type)) {
                pactYwForm.setType(type);
            }
            if (StringUtil.isNotEmpty(proType)) {
                pactYwForm.setProType(proType);
            }
            if (StringUtil.isNotEmpty(flowType)) {
                pactYwForm.setFlowType(flowType);
            }
            if (theme == null) {
                pactYwForm.setTheme(FormTheme.F_MR.getId());
            }else{
                pactYwForm.setTheme(theme);
            }

            List<ActYwForm> actYwForms = null;
            if(hasList) {
                actYwForms = actYwFormService.findListByInStyleList(pactYwForm);
            }else{
                actYwForms = actYwFormService.findListByInStyleNoList(pactYwForm);
            }
            return actYwForms;
        }

    /**
     * 获取节点ID对应的节点状态
     * @param gtype 状态类型
     * @return List
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxQueryStates", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public List<ActYwStatus> ajaxQueryStates(@RequestParam(required = false) String gtype) {
        ActYwStatus pactYwStatus = new ActYwStatus();
        pactYwStatus.setGtype(gtype);
        return  actYwStatusService.findList(pactYwStatus);
    }

    /**
     * 获取网关节点对应可使用的监听
     * @param theme 主题
     * @return List
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxQueryClazz", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ApiTstatus<List<ActYwClazz>> ajaxQueryClazz(@RequestParam(required = true) String theme, @RequestParam(required = false) String type) {
        ActYwClazz pactYwClazz = new ActYwClazz();
        pactYwClazz.setTheme(theme);
        pactYwClazz.setType(type);
        List<ActYwClazz> actYwClazzs =  actYwClazzService.findList(pactYwClazz);
        if(StringUtil.checkNotEmpty(actYwClazzs)){
            return new ApiTstatus<List<ActYwClazz>>(true, "查询成功！", actYwClazzs);
        }
        return new ApiTstatus<List<ActYwClazz>>(false, "查询失败！");
    }

    /**
     * 获取正则类型数据.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxRegTypes", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ApiTstatus<String> ajaxRegTypes(@RequestParam(required = false) Boolean isAll, @RequestParam(required = false) String id) {
        if(isAll == null){
            isAll = false;
        }

        //查询所有
        if(isAll){
            return new ApiTstatus<String>(true, "查询成功！", (Arrays.asList(RegType.values()).toString()));
        }

        if(StringUtil.isEmpty(id)){
            RegType regType = RegType.getById(id);
            if(regType == null){
                return new ApiTstatus<String>(false, "查询失败,查询记录不存在或未定义！");
            }
            return new ApiTstatus<String>(true, "查询成功！", (Arrays.asList(regType).toString()));
        }
        return new ApiTstatus<String>(false, "查询失败,参数不正确！");
    }

    /**
     * 获取节点ID对应的节点状态
     * @param groupId 流程ID
     * @param gnodeId 节点ID
     * @return List
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxQueryGstates/{groupId}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public List<ActYwGstatus> ajaxQueryGstates(@PathVariable("groupId") String groupId,  @RequestParam(required = false) String gnodeId) {
        ActYwGstatus pctYwGstatus = new ActYwGstatus();
        pctYwGstatus.setGroup(new ActYwGroup(groupId));
        pctYwGstatus.setGnode(new ActYwGnode(gnodeId));
        return  actYwGstatusService.findList(pctYwGstatus);
    }

    /**
     * 保存所有网关节点与流程状态
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxSaveGstate", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus ajaxSaveGstate(@RequestBody JSONObject gps) {
        Map<String, Class> classMap = new HashMap<String, Class>();
        classMap.put("ActYwGroup", ActYwGroup.class);
        classMap.put("ActYwGnode", ActYwGnode.class);
        classMap.put("ActYwStatus", ActYwStatus.class);
        List<ActYwGstatus> actYwGstatusList = (List<ActYwGstatus>) JSONObject.toBean(gps, ActYwGstatus.class, classMap);

        boolean res=actYwGstatusService.saveAll(actYwGstatusList);
        if(res){
            return new ApiTstatus(res, "保存成功！");
        }else{
            return new ApiTstatus(res, "保存失败！");
        }
    }

    /**
     * 保存所有节点与流程状态
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxDelGstatus/{groupId}", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<ActYwGstatus> ajaxDelGstatus(@PathVariable("groupId") String groupId,  @RequestParam(required = false) String gnodeId) {
        boolean res=actYwGstatusService.deleteByGroupIdAndGnodeId(groupId,gnodeId);
        if(res){
            return new ApiTstatus<ActYwGstatus>(res, "删除成功！");
        }else{
            return new ApiTstatus<ActYwGstatus>(res, "删除失败！");
        }
    }

    /**
     * 获取流程类型.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxFlowTypes", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ApiTstatus<String> ajaxFlowTypes(@RequestParam(required = false) Boolean isAll, @RequestParam(required = false) String key) {
        if(isAll == null){
            isAll = false;
        }

        //查询所有
        if(isAll){
            return new ApiTstatus<String>(true, "查询成功！", (Arrays.asList(FlowType.values()).toString()));
        }

        if(StringUtil.isNotEmpty(key)){
            FlowType flowType = FlowType.getByKey(key);
            if(flowType == null){
                return new ApiTstatus<String>(false, "查询失败,查询记录不存在或未定义！");
            }
            return new ApiTstatus<String>(true, "查询成功！", (Arrays.asList(flowType).toString()));
        }
        return new ApiTstatus<String>(false, "查询失败,参数不正确！");
    }

    /**
     * 获取表单类型.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxFormTypes", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ApiTstatus<String> ajaxFormTypes(@RequestParam(required = false) Boolean isAll, @RequestParam(required = false) String key) {
        if(isAll == null){
            isAll = false;
        }

        //查询所有
        if(isAll){
            return new ApiTstatus<String>(true, "查询成功！", (Arrays.asList(FormType.values()).toString()));
        }

        if(StringUtil.isNotEmpty(key)){
            FormType formType = FormType.getByKey(key);
            if(formType == null){
                return new ApiTstatus<String>(false, "查询失败,查询记录不存在或未定义！");
            }
            return new ApiTstatus<String>(true, "查询成功！", (Arrays.asList(formType).toString()));
        }
        return new ApiTstatus<String>(false, "查询失败,参数不正确！");
    }

    /**
     * 获取表单样式类型.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxFormStyleTypes", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ApiTstatus<String> ajaxFormStyleTypes(@RequestParam(required = false) Boolean isAll, @RequestParam(required = false) String key) {
        if(isAll == null){
            isAll = false;
        }

        //查询所有
        if(isAll){
            return new ApiTstatus<String>(true, "查询成功！", (Arrays.asList(FormStyleType.values()).toString()));
        }

        if(StringUtil.isNotEmpty(key)){
            FormStyleType formStyleType = FormStyleType.getByKey(key);
            if(formStyleType == null){
                return new ApiTstatus<String>(false, "查询失败,查询记录不存在或未定义！");
            }
            return new ApiTstatus<String>(true, "查询成功！", (Arrays.asList(formStyleType).toString()));
        }
        return new ApiTstatus<String>(false, "查询失败,参数不正确！");
    }

    /**
     * 获取表单客户端类型.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxFormClientTypes", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ApiTstatus<String> ajaxFormClientTypes(@RequestParam(required = false) Boolean isAll, @RequestParam(required = false) String key) {
        if(isAll == null){
            isAll = false;
        }

        //查询所有
        if(isAll){
            return new ApiTstatus<String>(true, "查询成功！", (Arrays.asList(FormClientType.values()).toString()));
        }

        if(StringUtil.isNotEmpty(key)){
            FormClientType formClientType = FormClientType.getByKey(key);
            if(formClientType == null){
                return new ApiTstatus<String>(false, "查询失败,查询记录不存在或未定义！");
            }
            return new ApiTstatus<String>(true, "查询成功！", (Arrays.asList(formClientType).toString()));
        }
        return new ApiTstatus<String>(false, "查询失败,参数不正确！");
    }
}

/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.oseasy.auy.modules.menu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.modules.workflow.entity.ProvinceProModel;
import com.oseasy.pro.modules.workflow.service.ProvinceProModelService;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.act.modules.actyw.service.ActYwGassignService;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowType;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.common.service.BaseService;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProActTaskService;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.scr.modules.scr.entity.ScoRapply;
import com.oseasy.scr.modules.scr.service.ScoRapplyService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 流程定义相关Service
 */
@Service
@Transactional(readOnly = true)
public class AuyActTaskService extends BaseService {
    protected static final Logger logger = Logger.getLogger(AuyActTaskService.class);
    @Autowired
    private ActTaskService actTaskService;
    @Autowired
    private ProActTaskService proActTaskService;
    @Autowired
    private ScoRapplyService scoRapplyService;
	@Autowired
	private ActYwService actYwService;
	@Autowired
	ActYwGassignService actYwGassignService;
	@Autowired
	private ProModelService proModelService;

	@Autowired
	private ProvinceProModelService provinceProModelService;

	@Autowired
	private CoreService coreService;

    /**
     * 菜单上待办记录数据
     * @param actywId
     * @param gnodeId 大节点ID
     * @return
     */
    public int todoCount(String actywId, String gnodeId){
        ActYw actYw = actYwService.get(actywId);
        if (actYw != null) {
            TaskQuery taskQuery = //taskService.createTaskQuery().taskCandidateOrAssigned(userName)
                    actTaskService.getTaskQueryByAssigneeOrCandidateUser()
                    .includeProcessVariables().active();
            String key = ActYw.getPkey(actYw);
            taskQuery.processDefinitionKey(key);
            List<Task> list = taskQuery.list();
            List<ActYwGnode> actYwGnodes = proActTaskService.getSubGnodeList(gnodeId, actYw.getGroupId());
            //工作流中待办数据
            int actNum=0;
            if (StringUtil.checkNotEmpty(list)) {
                //校验工作流查询出来的数据，是否存在于promodel表中，解决工作流和业务表数据不一致的情况
                List<String> proModelIds = list.stream().map(e -> (String) e.getProcessVariables().get(CoreJkey.JK_ID)).collect(Collectors.toList());

                List<String> existIds = null;
                if (actYw.flowType() != null) {
                    if ((FlowType.FWT_XM).equals(actYw.flowType()) || (FlowType.FWT_DASAI).equals(actYw.flowType())) {
                        if (actYw.getTenantId().equals(CoreIds.NPR_SYS_TENANT.getId())){
                            ProvinceProModel provinceProModel = new ProvinceProModel();
                            provinceProModel.setIds(proModelIds);
                            existIds = provinceProModelService.findListByIdsWithoutJoin(provinceProModel);
                        }else{
                            ProModel proModel = new ProModel();
                            proModel.setIds(proModelIds);
                            List<Role> roleList = UserUtils.getUser().getRoleList();
                            //校验学院秘书，只显示本学院待办任务数
                            Role roleMinister = coreService.getByRtype(CoreSval.Rtype.MINISTER.getKey());
                            List<Role> checkRole = new ArrayList<>();
                            if((roleMinister != null) && StringUtil.isNotEmpty(roleMinister.getId())){
                                checkRole = roleList.stream().filter(item -> item.getId().equals(roleMinister.getId())).collect(Collectors.toList());
                            }
                            if (checkRole.size() > 0){
                                proModel.setOfficeId(UserUtils.getUser().getOfficeId());
                                proModel.setRoleFlag("1");
                            }
                            existIds = proModelService.findListByIdsWithoutJoin(proModel);
                        }

                    } else if ((FlowType.FWT_SCORE).equals(actYw.flowType())) {
                        List<ScoRapply> models = scoRapplyService.ifindTodoByIds(proModelIds);
                        existIds = models.stream().map(e -> e.getId()).collect(Collectors.toList());
                    } else {
                        logger.error("当前流程主题未定义！");
                    }
                }

                if(StringUtil.checkNotEmpty(existIds)){
                    final List<String> existIdss = existIds;
                    List<String> ids = actYwGnodes.stream().map(e -> e.getId()).collect(Collectors.toList());
                    list = list.stream().filter(e -> ids.contains(e.getTaskDefinitionKey().replace(
                            ActYwTool.FLOW_ID_PREFIX, StringUtil.EMPTY))
                            && actywId.equals(e.getProcessVariables().get(ActYwGroup.ACT_YW_ID))
                            && existIdss.contains(e.getProcessVariables().get(CoreJkey.JK_ID))
                    ).collect(Collectors.toList());
                    actNum= list.size();
                }
            }
            int isDelegate=0;
            List<String> gnodeIdList = actYwGnodes.stream().map(e -> e.getId()).collect(Collectors.toList());
            List<String> delegateList = actYwGassignService.todoDelegateIds(gnodeIdList, actywId);
            if(StringUtil.checkNotEmpty(delegateList)) {
                isDelegate = delegateList.size();
            }
            return actNum+isDelegate;
        }
        return 0;
    }
}
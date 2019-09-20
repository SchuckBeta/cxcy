package com.oseasy.act.modules.actyw.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.act.common.config.ActSval;
import com.oseasy.act.common.config.ActSval.ActEmskey;
import com.oseasy.act.modules.actyw.entity.ActYwGform;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.entity.ActYwGrole;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.act.modules.actyw.entity.ActYwGstatus;
import com.oseasy.act.modules.actyw.service.ActYwGformService;
import com.oseasy.act.modules.actyw.service.ActYwGnodeService;
import com.oseasy.act.modules.actyw.service.ActYwGroleService;
import com.oseasy.act.modules.actyw.service.ActYwGroupService;
import com.oseasy.act.modules.actyw.service.ActYwGstatusService;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.act.modules.actyw.tool.process.vo.GnodeType;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.pcore.modules.sys.web.LoginController;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 项目流程Controller
 *
 * @author chenhao
 * @version 2017-05-23
 */
@Controller
@RequestMapping(value = "${adminPath}/actyw/actYwGnode")
public class ActYwGnodeController extends BaseController {
    @Autowired
    private ActYwGroupService actYwGroupService;
    @Autowired
    private ActYwGnodeService actYwGnodeService;
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    RuntimeService runtimeService;

    @Autowired
    private ActYwGroleService actYwGroleService;

    @Autowired
    private ActYwGformService actYwGformService;

    @Autowired
    private ActYwGstatusService actYwGstatusService;

    @ModelAttribute
    public ActYwGnode get(@RequestParam(required = false) String id) {
        ActYwGnode entity = null;
        if (StringUtil.isNotBlank(id)) {
            entity = actYwGnodeService.get(id);
        }
        if (entity == null) {
            entity = new ActYwGnode();
        }
        return entity;
    }

    /**
     * 获取已发布流程节点JSON数据（项目、大赛首页）.
     *
     * @param extId
     *            排除的ID
     * @param level
     *            是否显示
     * @param isYw
     *            是否业务节点,默认为true
     *
     * @param response
     * @return
     */
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "treeDataByYwId")
    public List<Map<String, Object>> treeDataByYwId(@RequestParam(required = false) String extId,
            @RequestParam(required = true) String ywId, @RequestParam(required = false) String level,
            @RequestParam(required = false) String parentId, @RequestParam(required = false) Boolean isAll, HttpServletResponse response) {
        return actYwGnodeService.treeDataByYwId(extId, ywId, parentId, isAll);
    }

    /****************************************************************************************************************
     * 新修改的接口.
     ***************************************************************************************************************/
    @RequestMapping(value = "delete")
    public String delete(ActYwGnode actYwGnode, RedirectAttributes redirectAttributes) {
        actYwGnodeService.delete(actYwGnode);
        addMessage(redirectAttributes, "删除自定义流程成功");
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/actyw/actYwGnode/?repage";
    }

    /**
     * 获取流程节点JSON数据。
     *
     * @param extId
     *            排除的ID
     * @param isShowHide
     *            是否显示
     * @param response
     * @return
     */
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required = false) String extId,
            @RequestParam(required = false) String isShowHide, @RequestParam(required = false) Boolean isAll,
            HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<ActYwGnode> list = actYwGnodeService.findAllList();
        for (int i = 0; i < list.size(); i++) {
            ActYwGnode e = list.get(i);
            Boolean isExt = StringUtil.isBlank(extId)
                    || (extId != null && !extId.equals(e.getId()) && e.getParentIds().indexOf("," + extId + ",") == -1);
            if (isExt) {
                if ((isShowHide != null) && isShowHide.equals(Const.HIDE) && e.getIsShow()) {
                    continue;
                }
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", e.getId());
                map.put("pId", e.getParentId());
                map.put("name", e.getName());
                mapList.add(map);
            }
        }
        return mapList;
    }

    /**
     * 跳转到流程跟踪页面 . 根据流程实例ID定位流程跟踪(自定义的项目、大赛使用).
     *
     * @param groupId
     *            流程ID
     * @param proInsId
     *            流程实例ID
     * @param model
     *            模型
     * @param request
     *            请求
     * @return
     */
    @RequestMapping(value = "designView")
    public String designView(String groupId, String proInsId, String grade, Model model, HttpServletRequest request) {
        if(UserUtils.checkToLogin()){
            return LoginController.LOGIN_ADM_REDIRECT;
        }

        if (StringUtil.isNotEmpty(grade)) {
            model.addAttribute(ActYwTool.FLOW_PROP_GATEWAY_STATE, grade);
        }

        if (StringUtil.isNotEmpty(groupId)) {
            model.addAttribute(ActYwGroup.JK_GROUP, actYwGroupService.get(groupId));
            model.addAttribute(ActYwGroup.JK_GROUP_ID, groupId);
        }

        if (StringUtil.isNotEmpty(proInsId)) {
            model.addAttribute("proInsId", proInsId);
        }

        model.addAttribute("faUrl", "a");
        return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwGnodeDesignAdminView";
    }

    @RequestMapping(value = { "list", "" })
    public String list(ActYwGnode actYwGnode, String groupId, Boolean isYw, HttpServletRequest request,
                       HttpServletResponse response, Model model) {
        List<ActYwGnode> list = Lists.newArrayList();
        List<ActYwGroup> actYwGroups = actYwGroupService.findList(new ActYwGroup());
        if (actYwGnode.getGroup() == null || StringUtils.isBlank(actYwGnode.getGroup().getId())) {
            actYwGnode.setGroup(actYwGroups.get(0));
        }
        ActYwGnode pactYwGnode = new ActYwGnode();
        pactYwGnode.setGroup(actYwGnode.getGroup());
        pactYwGnode.setIsShow(actYwGnode.getIsShow());

        isYw = ((isYw == null) ? true : isYw);
        if (isYw) {
            List<String> types = new ArrayList<>(3);
            types.add(GnodeType.GT_ROOT_TASK.getId());
            types.add(GnodeType.GT_PROCESS.getId());
            types.add(GnodeType.GT_PROCESS_TASK.getId());
            pactYwGnode.setTypes(types);
        }
        List<ActYwGnode> sourcelist = actYwGnodeService.findList(pactYwGnode);
        ActYwGnode.sortList(list, sourcelist, ActYwGnode.getRootId(), true);


        if (!list.isEmpty()) {
            ActYwGroup actYwGroup = new ActYwGroup(actYwGnode.getGroup().getId());
            ActYwGrole actYwGrole = new ActYwGrole();
            actYwGrole.setGroup(actYwGroup);

            ActYwGform actYwGform = new ActYwGform();
            actYwGform.setGroup(actYwGroup);
            for (ActYwGnode ywGnode : list) {
                //角色
                actYwGrole.setGnode(new ActYwGnode(ywGnode.getId()));
                List<ActYwGrole> roles = actYwGroleService.findList(actYwGrole);
                ywGnode.setGroles(roles);

                //表单
                actYwGform.setGnode(new ActYwGnode(ywGnode.getId()));
                List<ActYwGform> forms = actYwGformService.findList(actYwGform);
                ywGnode.setGforms(forms);
            }
        }

        model.addAttribute("list", list);
        model.addAttribute("isYw", isYw);
        model.addAttribute("actYwGroups", actYwGroups);
        return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwGnodeList";
    }


    @RequestMapping(value = { "/view" })
    public String view(ActYwGnode actYwGnode, Model model) {
        String preId = actYwGnode.getPreId();
        if(StringUtils.isNotBlank(preId)){
            String[] split = preId.split(",");
            String[] preNodeNames = new String[split.length];
            for (int i = 0; i < split.length; i++) {
                ActYwGnode preNode = actYwGnodeService.get(split[0]);
                if (preNode != null) {
                    preNodeNames[i] = preNode.getName();
                }
            }
            model.addAttribute("preNodeNames", StringUtils.join(preNodeNames, ","));
        }
        List<ActYwGnode> nextNodes = actYwGnodeService.getNextNode(actYwGnode);
        String[] nextNodeNames = new String[nextNodes.size()];
        if(!nextNodes.isEmpty()){
            for (int i = 0; i < nextNodes.size(); i++) {
                nextNodeNames[i] = nextNodes.get(0).getName();
            }
            model.addAttribute("nextNodeNames", StringUtils.join(nextNodeNames, ","));
        }

        //角色
        ActYwGrole actYwGrole = new ActYwGrole();
        actYwGrole.setGroup(actYwGnode.getGroup());
        actYwGrole.setGnode(new ActYwGnode(actYwGnode.getId()));
        List<ActYwGrole> roles = actYwGroleService.findList(actYwGrole);
        actYwGnode.setGroles(roles);

        //表单
        ActYwGform actYwGform = new ActYwGform();
        actYwGform.setGroup(actYwGnode.getGroup());
        actYwGform.setGnode(new ActYwGnode(actYwGnode.getId()));
        List<ActYwGform> forms = actYwGformService.findList(actYwGform);
        actYwGnode.setGforms(forms);

        //状态
        ActYwGstatus actYwGstatus = new ActYwGstatus();
        actYwGstatus.setGroup(actYwGnode.getGroup());
        actYwGstatus.setGnode(new ActYwGnode(actYwGnode.getId()));
        List<ActYwGstatus> status = actYwGstatusService.findList(actYwGstatus);
        actYwGnode.setGstatuss(status);

        return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwGnodeFormProp";
    }


}
package com.oseasy.auy.modules.act.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.pcore.modules.sys.service.CoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.act.common.config.ActSval;
import com.oseasy.act.common.config.ActSval.ActEmskey;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.act.modules.actyw.service.ActYwGnodeService;
import com.oseasy.act.modules.actyw.service.ActYwGroupService;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.pro.modules.promodel.service.ProActYwGnodeService;
import com.oseasy.pro.modules.promodel.tool.process.vo.GnodeView;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 项目流程Controller
 *
 * @author chenhao
 * @version 2017-05-23
 */
@Controller
@RequestMapping(value = "${adminPath}/actyw/actYwGnode")
public class AuyActYwGnodeController extends BaseController {
    @Autowired
    private ActTaskService actTaskService;
    @Autowired
    private ActYwGroupService actYwGroupService;
    @Autowired
    private ActYwGnodeService actYwGnodeService;
    @Autowired
    private ProActYwGnodeService proActYwGnodeService;
    @Autowired
    private CoreService coreService;

    /**
     * 跳转到设计页面 .
     *
     * @param actYwGnode
     * @param model
     *            模型
     * @param redirectAttributes
     *            重定向
     * @return String 字符
     */
    @RequestMapping(value = "designNew")
    public String designNew(ActYwGnode actYwGnode, Model model, RedirectAttributes redirectAttributes) {
        if ((actYwGnode.getGroup() == null) || StringUtil.isEmpty(actYwGnode.getGroup().getId())) {
            addMessage(redirectAttributes, "自定义流程为空，操作失败！");
            return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/actyw/actYwGnode/?repage";
        }
        model.addAttribute(ActYwGroup.JK_GROUP, actYwGroupService.get(actYwGnode.getGroup().getId()));
        model.addAttribute(CoreJkey.JK_ROOT, CoreIds.NCE_SYS_TREE_ROOT.getId());

        Role exportRole = coreService.getByRtype(CoreSval.Rtype.EXPORT.getKey());
        Role studentRole = coreService.getByRtype(CoreSval.Rtype.STUDENT.getKey());
        Role teacheRole = coreService.getByRtype(CoreSval.Rtype.TEACHER.getKey());
        model.addAttribute(CoreSval.Rtype.EXPORT.getPkey(), (exportRole != null)?exportRole.getId():StringUtil.EMPTY);
        model.addAttribute(CoreSval.Rtype.STUDENT.getPkey(), (studentRole != null)?studentRole.getId():StringUtil.EMPTY);
        model.addAttribute(CoreSval.Rtype.TEACHER.getPkey(),  (teacheRole != null)?teacheRole.getId():StringUtil.EMPTY);
        model.addAttribute(CoreJkey.JK_ROOT_START, actYwGnodeService.getStart(actYwGnode.getGroup().getId()));
        return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwGnodeDesignNew";
    }

    /**
     * 流程预览页面-已经去掉了页面.
     */
    @RequestMapping(value = { "/{groupId}/view" })
    public String view(@PathVariable String groupId, ActYwGnode actYwGnode, HttpServletRequest request,
            HttpServletResponse response, Model model) {
        if (StringUtil.isEmpty(groupId)) {
            return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/actyw/actYwGnode/?repage";
        }
//        List<ActYwGnode> sourcelist = Lists.newArrayList();
//        List<ActYwGnode> list = Lists.newArrayList();
//
//        actYwGnode.setGroup(actYwGroupService.get(new ActYwGroup(groupId)));
//        sourcelist = actYwGnodeService.findListByYw(actYwGnode);
//        ActYwGnode.sortList(list, sourcelist, ActYwGnode.getRootId(), true);
//        model.addAttribute("list", list);

        ActYwGroup actYwGroup = actYwGroupService.get(groupId);
        System.out.println(actYwGroup);
        model.addAttribute(ActYwGroup.JK_GROUP, actYwGroupService.get(groupId));

        return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwGnodeView";
    }

    /**
     * 根据流程标识查询带状态业务节点(旧的项目、大赛使用).
     * 1、得到ActYwGnode对应的节点
     * 2、根据findListByYwGroupAndPreIdss方法找到对应的已执行的节点列表（一级节点）
     * 3、根据findListByYwGroupAndPreIdss方法找到对应的全部的节点列表（一级节点）
     *
     * @param groupId
     *            流程标识
     * @param gnodeId
     *            流程节点标识
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/queryStatusTreeByGnode/{groupId}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ApiTstatus<List<GnodeView>> queryStatusTreeByGnode(@PathVariable String groupId, @RequestParam(required = true) String gnodeId, @RequestParam(required = false) String grade) {
        return proActYwGnodeService.queryStatusTreeByGnode(groupId, gnodeId, grade);
    }

    /**
     * 根据流程标识查询带状态业务节点(流程设计图运行状态查看).
     * 1、根据Activity的History接口查询流程进度，获取正在执行的节点
     * 2、得到ActYwGnode对应的节点 3、根据findListByYwGroupAndPreIdss方法找到对应的已执行的节点列表（一级节点）
     * 4、根据findListByYwGroupAndPreIdss方法找到对应的全部的节点列表（一级节点）
     *
     * @param groupId
     *            流程标识
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/queryStatusTree/{groupId}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ApiTstatus<List<GnodeView>> queryStatusTree(@PathVariable String groupId,
            @RequestParam(required = false) String proInsId, @RequestParam(required = false) String grade) {
        return proActYwGnodeService.queryStatusTree(groupId, proInsId, grade, actTaskService);
    }

    @ResponseBody
    @RequestMapping(value = "/queryGnodeUser/{groupId}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public JSONObject queryGnodeUser(@PathVariable String groupId, @RequestParam(required = false) String proInsId, @RequestParam(required = false) String grade) {
       return proActYwGnodeService.queryGnodeUser(groupId, proInsId, grade, actTaskService);
    }
}
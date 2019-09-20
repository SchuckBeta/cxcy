package com.oseasy.pro.modules.promodel.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oseasy.act.common.config.ActSval;
import com.oseasy.act.common.config.ActSval.ActEmskey;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.act.modules.actyw.service.ActYwGroupService;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.pcore.modules.sys.web.LoginController;
import com.oseasy.pro.modules.promodel.service.ProActYwGnodeService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 项目流程Controller
 *
 * @author chenhao
 * @version 2017-05-23
 */
@Controller
@RequestMapping(value = "${adminPath}/actyw/actYwGnode")
public class ProActYwGnodeController extends BaseController {
    @Autowired
    private ActYwGroupService actYwGroupService;
    @Autowired
    private ProActYwGnodeService proActYwGnodeService;

    /**
     * 根据状态和流程ID 定位流程跟踪(旧的项目、大赛使用).
     *
     * @param idx
     *            状态
     * @param groupId
     *            流程ID
     * @param model
     *            模型
     * @return String
     */
    @RequestMapping(value = "designView/{idx}")
    public String designView(@PathVariable String idx, @RequestParam(required = true) String groupId, String grade,
            Model model) {
        if(UserUtils.checkToLogin()){
            return LoginController.LOGIN_ADM_REDIRECT;
        }

        if (StringUtil.isNotEmpty(grade)) {
            model.addAttribute(ActYwTool.FLOW_PROP_GATEWAY_STATE, grade);
        }

        model.addAttribute(ActYwGroup.JK_GROUP_ID, groupId);
        model.addAttribute(ActYwGroup.JK_GROUP, actYwGroupService.get(groupId));
        model.addAttribute(ActYwGroup.JK_GNODE, proActYwGnodeService.getGnodeByStatus(idx, groupId, model));
        model.addAttribute("faUrl", "a");
        return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwGnodeDesignAdminViewPro";
    }
}
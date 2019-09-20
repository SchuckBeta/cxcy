package com.oseasy.act.modules.actyw.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oseasy.act.common.config.ActSval;
import com.oseasy.act.common.config.ActSval.ActEmskey;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.act.modules.actyw.service.ActYwGroupService;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.util.common.utils.StringUtil;
/**
 *前台流程节点接口控制器.
 * @author chenhao
 */
@Controller
@RequestMapping(value = "${frontPath}/actyw/actYwGnode")
public class FrontActYwGnodeController extends BaseController {
  @Autowired
  private ActYwGroupService actYwGroupService;

  /**
   * 根据流程实例ID定位流程跟踪(自定义的项目、大赛使用).
   * @param groupId 流程ID
   * @param proInsId 流程实例ID
   * @param model 模型
   * @param request 请求
   * @return
   */
  @RequestMapping(value = "designView")
  public String designView(@RequestParam(required=true)String groupId, @RequestParam(required=true)String proInsId, String grade, Model model, HttpServletRequest request) {
      if(UserUtils.checkToLogin()){
          return CoreSval.LOGIN_REDIRECT;
      }

      if (StringUtil.isNotEmpty(grade)) {
        model.addAttribute(ActYwTool.FLOW_PROP_GATEWAY_STATE, grade);
      }

      if (StringUtil.isNotEmpty(proInsId)) {
        model.addAttribute("proInsId", proInsId);
      }
      model.addAttribute(ActYwGroup.JK_GROUP, actYwGroupService.get(groupId));
      model.addAttribute(ActYwGroup.JK_GROUP_ID, groupId);
      model.addAttribute("faUrl", "f");
      return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwGnodeDesignFrontView";
  }
}
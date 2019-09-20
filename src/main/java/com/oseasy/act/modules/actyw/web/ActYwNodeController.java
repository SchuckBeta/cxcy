package com.oseasy.act.modules.actyw.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.act.common.config.ActSval;
import com.oseasy.act.common.config.ActSval.ActEmskey;
import com.oseasy.act.modules.actyw.entity.ActYwForm;
import com.oseasy.act.modules.actyw.entity.ActYwNode;
import com.oseasy.act.modules.actyw.service.ActYwFormService;
import com.oseasy.act.modules.actyw.service.ActYwNodeService;
import com.oseasy.act.modules.actyw.tool.process.ActYwSten;
import com.oseasy.act.modules.actyw.tool.process.vo.NodeEtype;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 项目流程节点Controller
 * @author chenhao
 * @version 2017-05-23
 */
@Controller
@RequestMapping(value = "${adminPath}/actyw/actYwNode")
public class ActYwNodeController extends BaseController {

	@Autowired
	private ActYwNodeService actYwNodeService;
	@Autowired
	private ActYwFormService actYwFormService;
	@Autowired
	private SystemService systemService;

	@ModelAttribute
	public ActYwNode get(@RequestParam(required=false) String id) {
		ActYwNode entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = actYwNodeService.get(id);
		}
		if (entity == null) {
			entity = new ActYwNode();
		}
		return entity;
	}

	@RequiresPermissions("actyw:actYwNode:view")
	@RequestMapping(value = {"list", ""})
	public String list(ActYwNode actYwNode, HttpServletRequest request, HttpServletResponse response, Model model) {
        if(actYwNode.getIsVisible() == null){
            actYwNode.setIsVisible(true);
        }
		Page<ActYwNode> page = actYwNodeService.findPage(new Page<ActYwNode>(request, response), actYwNode);
		model.addAttribute("page", page);
		model.addAttribute("nodeEtypes", NodeEtype.getAll());
		return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwNodeList";
	}

	@RequiresPermissions("actyw:actYwNode:view")
	@RequestMapping(value = "form")
	public String form(ActYwNode actYwNode, Model model) {
		model.addAttribute("actYwStenStencils", ActYwSten.genActYwStenStencils());

        ActYwForm pactYwForm = new ActYwForm();
        List<ActYwForm> actYwForms = actYwFormService.findList(pactYwForm);
        model.addAttribute("actYwForms", actYwForms);

        List<Role>  roleList = systemService.findAllRole();
        model.addAttribute("roleList",roleList);
		model.addAttribute("actYwNode", actYwNode);
		return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwNodeForm";
	}

	@RequiresPermissions("actyw:actYwNode:edit")
	@RequestMapping(value = "save")
	public String save(ActYwNode actYwNode, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, actYwNode)) {
			return form(actYwNode, model);
		}
		actYwNodeService.save(actYwNode);
		addMessage(redirectAttributes, "保存流程节点成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/actyw/actYwNode/?repage";
	}

	@RequiresPermissions("actyw:actYwNode:edit")
	@RequestMapping(value = "delete")
	public String delete(ActYwNode actYwNode, RedirectAttributes redirectAttributes) {
		actYwNodeService.delete(actYwNode);
		addMessage(redirectAttributes, "删除流程节点成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/actyw/actYwNode/?repage";
	}

	  /**
       * 查询所有元素节点.
	   * @param type
       * @return ActYwRstatus
	   */
	  @ResponseBody
	  @RequestMapping(value = "/query", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	  public ApiTstatus<List<ActYwNode>> queryAll(@RequestParam(required=false) Boolean isVisible, @RequestParam(required=false) String type) {
	      ActYwNode pnode = new ActYwNode();
	      if(StringUtil.isNotEmpty(type)){
	          pnode.setType(type);
	      }
	      if(isVisible != null){
	          pnode.setIsVisible(isVisible);
	      }
	      return new ApiTstatus<List<ActYwNode>>(true, "查询成功", actYwNodeService.findList(pnode));
	  }
}
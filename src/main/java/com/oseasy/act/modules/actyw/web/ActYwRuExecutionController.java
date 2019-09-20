package com.oseasy.act.modules.actyw.web;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oseasy.act.common.config.ActSval;
import com.oseasy.act.common.config.ActSval.ActEmskey;
import com.oseasy.act.modules.actyw.entity.ActYwRuExecution;
import com.oseasy.act.modules.actyw.service.ActYwRuExecutionService;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 流程运行实例Controller.
 *
 * @author chenhao
 * @version 2017-06-08
 */
@Controller
@RequestMapping(value = "${adminPath}/query/actYwRuExecution")
public class ActYwRuExecutionController extends BaseController {

  @Autowired
  private ActYwRuExecutionService actYwRuExecutionService;

  @ModelAttribute
  public ActYwRuExecution get(@RequestParam(required = false) String id) {
    ActYwRuExecution entity = null;
    if (StringUtil.isNotBlank(id)) {
      entity = actYwRuExecutionService.get(id);
    }
    if (entity == null) {
      entity = new ActYwRuExecution();
    }
    return entity;
  }

  @RequestMapping(value = {"list", ""})
  public String list(ActYwRuExecution actYwRuExecution, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<ActYwRuExecution> page = actYwRuExecutionService.findPage(new Page<ActYwRuExecution>(request, response), actYwRuExecution);
    model.addAttribute(Page.PAGE, page);
    return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwRuExecutionList";
  }
}
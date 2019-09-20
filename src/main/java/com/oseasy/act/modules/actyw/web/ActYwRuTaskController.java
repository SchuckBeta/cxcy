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
import com.oseasy.act.modules.actyw.entity.ActYwRuTask;
import com.oseasy.act.modules.actyw.service.ActYwRuTaskService;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 流程运行任务Controller.
 *
 * @author chenhao
 * @version 2017-06-08
 */
@Controller
@RequestMapping(value = "${adminPath}/query/actYwRuTask")
public class ActYwRuTaskController extends BaseController {

  @Autowired
  private ActYwRuTaskService actYwRuTaskService;

  @ModelAttribute
  public ActYwRuTask get(@RequestParam(required = false) String id) {
    ActYwRuTask entity = null;
    if (StringUtil.isNotBlank(id)) {
      entity = actYwRuTaskService.get(id);
    }
    if (entity == null) {
      entity = new ActYwRuTask();
    }
    return entity;
  }

  @RequestMapping(value = { "list", "" })
  public String list(ActYwRuTask actYwRuTask, HttpServletRequest request,
      HttpServletResponse response, Model model) {
    Page<ActYwRuTask> page = actYwRuTaskService.findPage(new Page<ActYwRuTask>(request, response),
        actYwRuTask);
    model.addAttribute(Page.PAGE, page);
    return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwRuTaskList";
  }
}
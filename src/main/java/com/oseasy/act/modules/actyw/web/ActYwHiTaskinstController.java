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
import com.oseasy.act.modules.actyw.entity.ActYwHiTaskinst;
import com.oseasy.act.modules.actyw.service.ActYwHiTaskinstService;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 流程历史任务Controller.
 *
 * @author chenhao
 * @version 2017-06-08
 */
@Controller
@RequestMapping(value = "${adminPath}/query/actYwHiTaskinst")
public class ActYwHiTaskinstController extends BaseController {

  @Autowired
  private ActYwHiTaskinstService actYwHiTaskinstService;

  @ModelAttribute
  public ActYwHiTaskinst get(@RequestParam(required = false) String id) {
    ActYwHiTaskinst entity = null;
    if (StringUtil.isNotBlank(id)) {
      entity = actYwHiTaskinstService.get(id);
    }
    if (entity == null) {
      entity = new ActYwHiTaskinst();
    }
    return entity;
  }

  @RequestMapping(value = { "list", "" })
  public String list(ActYwHiTaskinst actYwHiTaskinst, HttpServletRequest request,
      HttpServletResponse response, Model model) {
    Page<ActYwHiTaskinst> page = actYwHiTaskinstService
        .findPage(new Page<ActYwHiTaskinst>(request, response), actYwHiTaskinst);
    model.addAttribute(Page.PAGE, page);
    return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwHiTaskinstList";
  }
}
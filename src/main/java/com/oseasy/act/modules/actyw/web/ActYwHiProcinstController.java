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
import com.oseasy.act.modules.actyw.entity.ActYwHiProcinst;
import com.oseasy.act.modules.actyw.service.ActYwHiProcinstService;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 流程历史实例Controller.
 *
 * @author chenhao
 * @version 2017-06-08
 */
@Controller
@RequestMapping(value = "${adminPath}/query/actYwHiProcinst")
public class ActYwHiProcinstController extends BaseController {

  @Autowired
  private ActYwHiProcinstService actYwHiProcinstService;

  @ModelAttribute
  public ActYwHiProcinst get(@RequestParam(required = false) String id) {
    ActYwHiProcinst entity = null;
    if (StringUtil.isNotBlank(id)) {
      entity = actYwHiProcinstService.get(id);
    }
    if (entity == null) {
      entity = new ActYwHiProcinst();
    }
    return entity;
  }

  @RequestMapping(value = { "list", "" })
  public String list(ActYwHiProcinst actYwHiProcinst, HttpServletRequest request,
      HttpServletResponse response, Model model) {
    Page<ActYwHiProcinst> page = actYwHiProcinstService
        .findPage(new Page<ActYwHiProcinst>(request, response), actYwHiProcinst);
    model.addAttribute(Page.PAGE, page);
    return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwHiProcinstList";
  }
}
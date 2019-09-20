package com.oseasy.pw.modules.pw.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pw.common.config.PwSval;
import com.oseasy.pw.common.config.PwSval.PwEmskey;
import com.oseasy.pw.modules.pw.entity.PwEnterRoomRecord;
import com.oseasy.pw.modules.pw.service.PwEnterRoomRecordService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 场地分配记录Controller.
 * @author chenh
 * @version 2018-12-10
 */
@Controller
@RequestMapping(value = "${adminPath}/pw/pwEnterRoomRecord")
public class PwEnterRoomRecordController extends BaseController {

	@Autowired
	private PwEnterRoomRecordService entityService;

	@ModelAttribute
	public PwEnterRoomRecord get(@RequestParam(required=false) String id) {
		PwEnterRoomRecord entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = entityService.get(id);
		}
		if (entity == null){
			entity = new PwEnterRoomRecord();
		}
		return entity;
	}

	@RequiresPermissions("pw:pwEnterRoomRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(PwEnterRoomRecord entity, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PwEnterRoomRecord> page = entityService.findPage(new Page<PwEnterRoomRecord>(request, response), entity);
		model.addAttribute(Page.PAGE, page);
		return PwSval.path.vms(PwEmskey.PW.k()) + "pwEnterRoomRecordList";
	}

	@RequiresPermissions("pw:pwEnterRoomRecord:view")
	@RequestMapping(value = "form")
	public String form(PwEnterRoomRecord entity, Model model) {
		model.addAttribute("pwEnterRoomRecord", entity);
		return PwSval.path.vms(PwEmskey.PW.k()) + "pwEnterRoomRecordForm";
	}

	@RequiresPermissions("pw:pwEnterRoomRecord:edit")
	@RequestMapping(value = "save")
	public String save(PwEnterRoomRecord entity, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, entity)){
			return form(entity, model);
		}
		entityService.save(entity);
		addMessage(redirectAttributes, "保存场地分配记录成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwEnterRoomRecord/?repage";
	}

	@RequiresPermissions("pw:pwEnterRoomRecord:edit")
	@RequestMapping(value = "delete")
	public String delete(PwEnterRoomRecord entity, RedirectAttributes redirectAttributes) {
		entityService.delete(entity);
		addMessage(redirectAttributes, "删除场地分配记录成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwEnterRoomRecord/?repage";
	}

}
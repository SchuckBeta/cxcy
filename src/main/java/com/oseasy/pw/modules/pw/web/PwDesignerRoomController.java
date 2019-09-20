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
import com.oseasy.pw.modules.pw.entity.PwDesignerRoom;
import com.oseasy.pw.modules.pw.service.PwDesignerRoomService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 房间设计表Controller.
 * @author zy
 * @version 2017-12-18
 */
@Controller
@RequestMapping(value = "${adminPath}/pw/pwDesignerRoom")
public class PwDesignerRoomController extends BaseController {

	@Autowired
	private PwDesignerRoomService pwDesignerRoomService;

	@ModelAttribute
	public PwDesignerRoom get(@RequestParam(required=false) String id) {
		PwDesignerRoom entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = pwDesignerRoomService.get(id);
		}
		if (entity == null){
			entity = new PwDesignerRoom();
		}
		return entity;
	}

	@RequiresPermissions("pw:pwDesignerRoom:view")
	@RequestMapping(value = {"list", ""})
	public String list(PwDesignerRoom pwDesignerRoom, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PwDesignerRoom> page = pwDesignerRoomService.findPage(new Page<PwDesignerRoom>(request, response), pwDesignerRoom);
		model.addAttribute("page", page);
		return PwSval.path.vms(PwEmskey.PW.k()) + "pwDesignerRoomList";
	}

	@RequiresPermissions("pw:pwDesignerRoom:view")
	@RequestMapping(value = "form")
	public String form(PwDesignerRoom pwDesignerRoom, Model model) {
		model.addAttribute("pwDesignerRoom", pwDesignerRoom);
		return PwSval.path.vms(PwEmskey.PW.k()) + "pwDesignerRoomForm";
	}

	@RequiresPermissions("pw:pwDesignerRoom:edit")
	@RequestMapping(value = "save")
	public String save(PwDesignerRoom pwDesignerRoom, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, pwDesignerRoom)){
			return form(pwDesignerRoom, model);
		}
		pwDesignerRoomService.save(pwDesignerRoom);
		addMessage(redirectAttributes, "保存房间设计表成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwDesignerRoom/?repage";
	}

	@RequiresPermissions("pw:pwDesignerRoom:edit")
	@RequestMapping(value = "delete")
	public String delete(PwDesignerRoom pwDesignerRoom, RedirectAttributes redirectAttributes) {
		pwDesignerRoomService.delete(pwDesignerRoom);
		addMessage(redirectAttributes, "删除房间设计表成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwDesignerRoom/?repage";
	}

}
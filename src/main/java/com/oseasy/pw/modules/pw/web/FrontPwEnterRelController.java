package com.oseasy.pw.modules.pw.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.annotation.CheckToken;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pw.common.config.PwSval;
import com.oseasy.pw.common.config.PwSval.PwEmskey;
import com.oseasy.pw.modules.pw.entity.PwEnter;
import com.oseasy.pw.modules.pw.entity.PwEnterRel;
import com.oseasy.pw.modules.pw.service.PwEnterRelService;
import com.oseasy.pw.modules.pw.service.PwEnterRoomService;
import com.oseasy.pw.modules.pw.service.PwEnterService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 入驻申报关联Controller.
 * @author chenh
 * @version 2017-11-26
 */
@Controller
@RequestMapping(value = "${frontPath}/pw/pwEnterRel")
public class FrontPwEnterRelController extends BaseController {

	@Autowired
   	private PwEnterService pwEnterService;
   	@Autowired
   	private PwEnterRelService pwEnterRelService;
   	@Autowired
   	private PwEnterRoomService pwEnterRoomService;

	@ModelAttribute
	public PwEnterRel get(@RequestParam(required=false) String id) {
		PwEnterRel entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = pwEnterRelService.get(id);
		}
		if (entity == null){
			entity = new PwEnterRel();
		}
		return entity;
	}

	@CheckToken(value = Const.NO)
	@RequestMapping(value = {"list"})
	public String list(PwEnterRel pwEnterRel, HttpServletRequest request, HttpServletResponse response, Model model) {
	    User user = UserUtils.getUser();
        if (StringUtil.isEmpty(user.getId())) {
            return UserUtils.toLogin();
        }
		return PwSval.path.vms(PwEmskey.PW.k()) + "frontPwEnterRelList";
	}

	@RequestMapping(value = "form")
	public String form(PwEnterRel pwEnterRel, Model model) {
		if((pwEnterRel == null) || (pwEnterRel.getActYwApply() == null) || StringUtil.isEmpty(pwEnterRel.getActYwApply().getRelId())){
		  return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwEnterRel/?repage";
		}

		PwEnter pwEnter = pwEnterService.get(pwEnterRel.getActYwApply().getRelId());
		if(pwEnter == null){
		  return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwEnterRel/?repage";
		}
		model.addAttribute("pwEnter", pwEnter);
		model.addAttribute("pwEnterRel", pwEnterRel);
		model.addAttribute("root", CoreIds.NCE_SYS_TREE_ROOT.getId());
		return PwSval.path.vms(PwEmskey.PW.k()) + "frontPwEnterRelForm";
  	}

	@RequestMapping(value = "view")
	public String view(String id, Model model) {
		PwEnter pwEnter = pwEnterService.get(id);
		if(pwEnter == null){
		  return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwEnterRel/?repage";
		}
		model.addAttribute("pwEnter", pwEnter);
		return PwSval.path.vms(PwEmskey.PW.k()) + "frontPwEnterRelView";
	}
}
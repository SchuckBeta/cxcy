package com.oseasy.pro.modules.promodel.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.act.modules.actyw.entity.ActYwGassign;
import com.oseasy.act.modules.actyw.service.ActYwGnodeService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.modules.promodel.service.ProActYwGassignService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 业务指派表Controller.
 * @author zy
 * @version 2018-04-03
 */
@Controller
@RequestMapping(value = "${adminPath}/actyw/actYwGassign")
public class ProActYwGassignController extends BaseController {
    @Autowired
    private ProActYwGassignService proActYwGassignService;
    @Autowired
    private ActYwGnodeService actYwGnodeService;

	@RequestMapping(value = "saveAssign")
	public String saveAssign(ActYwGassign actYwGassign, Model model,HttpServletRequest request, RedirectAttributes redirectAttributes) {

		//指派的项目id
		String promodelIds=request.getParameter("promodelIds");
		//指派的人员id
		String userIds=request.getParameter("userIds");
		if(StringUtil.isEmpty(actYwGassign.getGnodeId())){
		    addMessage(redirectAttributes, "指派失败.");
		}
		actYwGassign = ActYwGassign.initType(actYwGassign, actYwGnodeService.get(actYwGassign.getGnodeId()));
		proActYwGassignService.saveAssign(actYwGassign,promodelIds,userIds);
		addMessage(redirectAttributes, "指派完成");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/cms/form/taskAssignList/?actywId="+actYwGassign.getYwId();
	}

}
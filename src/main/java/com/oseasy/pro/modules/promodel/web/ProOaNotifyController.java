/**
 *
 */
package com.oseasy.pro.modules.promodel.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.com.mqserver.common.config.MqsrSval;
import com.oseasy.com.mqserver.common.config.MqsrSval.MqsrEmskey;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.com.mqserver.modules.oa.vo.OaNotifySendType;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.modules.project.entity.ProjectAnnounce;
import com.oseasy.pro.modules.project.service.ProjectAnnounceService;
import com.oseasy.sys.modules.sys.service.SysOaNotifyService;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 通知通告Controller
 * @version 2014-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/oaNotify")
public class ProOaNotifyController extends BaseController {
	@Autowired
	private OaNotifyService oaNotifyService;
	@Autowired
	TeamService teamService;
	@Autowired
	SysOaNotifyService  sysOaNotifyService;
	@Autowired
	private ProjectAnnounceService projectAnnounceService;
	@Autowired
	ActYwService actYwService;

    @RequiresPermissions("oa:oaNotify:view")
    @RequestMapping(value = "form")
    public String form(OaNotify oaNotify, Model model) {
        if (StringUtil.isNotBlank(oaNotify.getId())) {
            oaNotify = oaNotifyService.getRecordList(oaNotify);
        }
        model.addAttribute("oaNotify", oaNotify);
        if ("1".equals(oaNotify.getType())) {  //团建通知
            String teamId=oaNotify.getContent();
            Team team = teamService.get(teamId);
            model.addAttribute("team", team);
            return MqsrSval.path.vms(MqsrEmskey.OA.k()) + "oaNotifyTeam";
        }

        return MqsrSval.path.vms(MqsrEmskey.OA.k()) + "oaNotifyForm";
    }

    @RequiresPermissions("oa:oaNotify:edit")
    @RequestMapping(value = "saveAssign")
    public String saveAssign(OaNotify oaNotify, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, oaNotify)) {
            return form(oaNotify, model);
        }
        // 如果是修改，则状态为已发布，则不能再进行操作
        if (StringUtil.isNotBlank(oaNotify.getId())) {
            OaNotify e = oaNotifyService.get(oaNotify.getId());
            if ("1".equals(e.getStatus())) {
                addMessage(redirectAttributes, "已发布，不能操作！");
                return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/oa/oaNotify/formAssign?id="+oaNotify.getId();
            }
        }
        oaNotify.setSendType(OaNotifySendType.DIRECRIONAL.getVal());//定向的发送类型定为2
        oaNotifyService.save(oaNotify);
        addMessage(redirectAttributes, "保存通知'" + oaNotify.getTitle() + "'成功");
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/oa/oaNotify/assignList?repage";
    }

    @RequiresPermissions("oa:oaNotify:edit")
    @RequestMapping(value = "save")
    public String save(OaNotify oaNotify, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, oaNotify)) {
            return form(oaNotify, model);
        }
        // 如果是修改，则状态为已发布，则不能再进行操作
        if (StringUtil.isNotBlank(oaNotify.getId())) {
            OaNotify e = oaNotifyService.get(oaNotify.getId());
            if ("1".equals(e.getStatus())) {
                addMessage(redirectAttributes, "已发布，不能操作！");
                return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/oa/oaNotify/form?id="+oaNotify.getId();
            }
        }
        oaNotifyService.save(oaNotify);
        addMessage(redirectAttributes, "保存通知'" + oaNotify.getTitle() + "'成功");
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/oa/oaNotify/?repage";
    }

    @RequiresPermissions("oa:oaNotify:edit")
    @RequestMapping(value = "saveBroadcast")
    public String saveBroadcast(OaNotify oaNotify, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) {

        String sId = oaNotify.getsId();
        String protype =  oaNotify.getProtype();

        if ("1".equals(oaNotify.getSendType())) {//通知公告
            oaNotify.setType("3");
        }
        //如果是定向发送，则跳转到定向发送的保存方法
        if ("2".equals(oaNotify.getSendType())) {
            String returnStr = saveAssignBySendType(oaNotify,model,redirectAttributes);
            return returnStr;
        }


        oaNotifyService.saveCollegeBroadcast(oaNotify);
        addMessage(redirectAttributes, "保存通知'" + oaNotify.getTitle() + "'成功");

        if (StringUtil.isNoneBlank(sId)) {
            if ("'1'".equals(protype)) {
                ProjectAnnounce projectAnnounce = new ProjectAnnounce();
                projectAnnounce = projectAnnounceService.get(sId);
                projectAnnounce.setProjectState("1");
                projectAnnounceService.save(projectAnnounce);
                return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/project/projectAnnounce?repage";
            }else if ("3".equals(protype)) {  //actYw
                ActYw actYw = actYwService.get(sId);
                actYw.setStatus("1");
                actYwService.save(actYw);
                return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/actyw/actYw?repage";
            } else{
                return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/gcontest/gContestAnnounce?repage";
            }
        }
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/oa/oaNotify/broadcastList?repage";
    }

    public String saveAssignBySendType(OaNotify oaNotify, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, oaNotify)) {
            return form(oaNotify, model);
        }
        if (StringUtil.isNoneBlank(oaNotify.getsId())) {
            oaNotifyService.saveCollegeBroadcast(oaNotify);
            addMessage(redirectAttributes, "保存通知'" + oaNotify.getTitle() + "'成功");
            if ("'1'".equals(oaNotify.getProtype())) {
                return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/project/projectAnnounce?repage";
            }else{
                return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/gcontest/gContestAnnounce?repage";
            }
        }
        // 如果是修改，则状态为已发布，则不能再进行操作
        if (StringUtil.isNotBlank(oaNotify.getId())) {
            OaNotify e = oaNotifyService.get(oaNotify.getId());
            if ("1".equals(e.getStatus())) {
                addMessage(redirectAttributes, "已发布，不能操作！");
                return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/oa/oaNotify/formBroadcast?id="+oaNotify.getId();
            }
        }
        oaNotify.setSendType(OaNotifySendType.DIRECRIONAL.getVal());//定向的发送类型定为2
        sysOaNotifyService.saveCollege(oaNotify);
        addMessage(redirectAttributes, "保存通知'" + oaNotify.getTitle() + "'成功");
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/oa/oaNotify/broadcastList?repage";
    }
}
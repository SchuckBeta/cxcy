/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/oseasy/initiate">JeeSite</a> All rights reserved.
 */
package com.oseasy.sys.modules.team.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotifyRecord;
import com.oseasy.com.mqserver.modules.oa.vo.OaNotifySendType;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.sys.common.config.SysSval;
import com.oseasy.sys.common.config.SysSval.SysEmskey;
import com.oseasy.sys.modules.sys.enums.RoleBizTypeEnum;
import com.oseasy.sys.modules.sys.service.SysOaNotifyService;
import com.oseasy.sys.modules.sys.service.SysSystemService;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.entity.TeamUserRelation;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.sys.modules.team.service.TeamUserRelationService;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 团队人员关系表Controller
 *
 * @author zhangzheng
 * @version 2017-03-06
 */
@Controller
@RequestMapping(value = "${frontPath}/team/teamUserRelation")
public class TeamUserRelationFrontController extends BaseController {

    @Autowired
    private TeamUserRelationService teamUserRelationService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private SysSystemService sysSystemService;
    @Autowired
    SysOaNotifyService sysOaNotifyService;

    @ModelAttribute
    public TeamUserRelation get(@RequestParam(required = false) String id) {
        TeamUserRelation entity = null;
        if (StringUtil.isNotBlank(id)) {
            entity = teamUserRelationService.get(id);
        }
        if (entity == null) {
            entity = new TeamUserRelation();
        }
        return entity;
    }

    //执行同意团建
    @RequestMapping(value = "changeState")
    public String changeState(TeamUserRelation teamUserRelation,
                              HttpServletRequest request,
                              HttpServletResponse response,
                              RedirectAttributes redirectAttributes, Model model) {
        User user = UserUtils.getUser();
        teamUserRelation.setUser(user);
        //改变teamUserRelation 的state状态 改变team 的state状态
        teamUserRelationService.updateStateInTeam(teamUserRelation);
        addMessage(redirectAttributes, "成功");
        return CoreSval.REDIRECT + adminPath + "/oa/oaNotify/self";
    }


    @RequestMapping(value = {"list", ""})
    public String list(TeamUserRelation teamUserRelation, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<TeamUserRelation> page = teamUserRelationService.findPage(new Page<TeamUserRelation>(request, response), teamUserRelation);
        model.addAttribute("page", page);
        return SysSval.path.vms(SysEmskey.TEAM.k()) + "teamUserRelationList";
    }

    @RequestMapping(value = "form")
    public String form(TeamUserRelation teamUserRelation, Model model) {
        model.addAttribute("teamUserRelation", teamUserRelation);
        return SysSval.path.vms(SysEmskey.TEAM.k()) + "teamUserRelationForm";
    }

    @RequestMapping(value = "save")
    public String save(TeamUserRelation teamUserRelation, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, teamUserRelation)) {
            return form(teamUserRelation, model);
        }
        teamUserRelationService.save(teamUserRelation);
        addMessage(redirectAttributes, "保存团队人员关系成功");
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/team/teamUserRelation/?repage";
    }

    @RequestMapping(value = "delete")
    public String delete(TeamUserRelation teamUserRelation, RedirectAttributes redirectAttributes) {
        teamUserRelationService.delete(teamUserRelation);
        addMessage(redirectAttributes, "删除团队人员关系成功");
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/team/teamUserRelation/?repage";
    }

    //团队负责人发布
    @RequestMapping(value = "batInTeamUser")
    @ResponseBody
    public JSONObject batInTeamUser(String offices, String userIds, String teamId, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        JSONObject json = new JSONObject();
        String officeIds = userIds;
        try {
            //现在的userIds里面存放的是专业的ids，因为团队建设的是userIds,所以不修改
            //查询专业下面的所有学生
            if (StringUtil.isNotBlank(userIds)) {
                StringBuffer proSbff = new StringBuffer();
                for (String professionId : userIds.split(",")) {
                    professionId = professionId.trim();
                    proSbff.append("'");
                    proSbff.append(professionId);
                    proSbff.append("'");
                    proSbff.append(",");
                }
                userIds = proSbff.substring(0, proSbff.lastIndexOf(","));
                List<User> userListTmp = sysSystemService.findUserByProfessionIdAndRoleType(userIds, RoleBizTypeEnum.XS);
                if (userListTmp != null && userListTmp.size() > 0) {
                    StringBuffer userIdsBuff = new StringBuffer();
                    for (User us : userListTmp) {
                        userIdsBuff.append(us.getId());
                        userIdsBuff.append(",");
                    }
                    userIds = userIdsBuff.substring(0, userIdsBuff.lastIndexOf(","));
                }
            }
            //查询所有用户
            List<String> userList = teamUserRelationService.findAllUserId("1", offices, userIds);
//			User userParam = new User();
//			userParam.setUserType("2");
//			userParam.setTeacherType(TeacherType.TY_QY.getKey());
//			List<User> entTeacherList = userDao.findListTree(userParam);
//
//				if (entTeacherList!=null&&entTeacherList.size()>0) {
//					if (userList==null) {
//						userList = new ArrayList<String>();
//					}
//						for(User et:entTeacherList) {
//							userList.add(et.getId());
//						}
//				}

            //删除久通知  不稳定先注释
/*			OaNotify oaNotify=new OaNotify();
            oaNotify.setsId(teamId);
			oaNotify.setType("7");
			logger.info("teamId:"+teamId);
			List<OaNotify> oanList= oaNotifyService.findList(oaNotify);

			for (OaNotify oaNotify2 : oanList) {
				//oaNotifyRecordDao.deleteByOaNotifyId(oaNotify2.getId());
				oaNotifyService.delete(oaNotify2);
			}*/

            //插入发布通知

            if (officeIds != null && StringUtil.isNotEmpty(officeIds)) {
                User TeamSponsor = UserUtils.getUser();
                Team team = teamService.get(teamId);
                OaNotify oaNotify=new OaNotify();
                oaNotify.setTitle(team.getName()+(team.getName()!=null&&team.getName().endsWith("团队")?"":"团队")+"创建成功");
                User  userTmp = systemService.getUser(team.getSponsor());
                oaNotify.setContent(userTmp.getName()+"的团队已发布。");
                oaNotify.setType("7");
                oaNotify.setsId(team.getId());
                oaNotify.setCreateBy(TeamSponsor);
                oaNotify.setCreateDate(new Date());
                oaNotify.setEffectiveDate(new Date());
                oaNotify.setUpdateBy(TeamSponsor);
                oaNotify.setUpdateDate(new Date());
                oaNotify.setSendType(OaNotifySendType.DIRECRIONAL.getVal());
                oaNotify.setStatus("1");

                List<OaNotifyRecord>  recList=new ArrayList<OaNotifyRecord>();
                //for (String userId : inseTeamUser) {
                    //if (!userId.equals(team.getSponsor())) {
                        OaNotifyRecord oaNotifyRecord=new OaNotifyRecord();
                        //User user=new User();
                        //user.setId(userId);
                        //oaNotifyRecord.setUser(user);
                        Office office = new Office();
                        office.setId(officeIds);
                        oaNotifyRecord.setOffice(office);
                        oaNotifyRecord.setOaNotify(oaNotify);
                        oaNotifyRecord.setReadFlag("0");
                        oaNotifyRecord.setId(IdGen.uuid());
                        oaNotifyRecord.setOperateFlag("0");
                        recList.add(oaNotifyRecord);
                    //}
                //}

                oaNotify.setOaNotifyRecordList(recList);
                sysOaNotifyService.save(oaNotify,userList);
            }
            json.put("success", true);
        } catch (Exception e) {
            json.put("success", false);
            logger.error(e.getMessage());
        }

        return json;
    }
    //团队负责人发布
    @RequestMapping(value = "batInTeamOffice")
    @ResponseBody
    public JSONObject batInTeamOffice(String offices, String userIds, String teamId, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        JSONObject json = new JSONObject();
        try {
            //现在的userIds里面存放的是专业的ids，因为团队建设的是userIds,所以不修改
            //查询专业下面的所有学生
            List<String> officeList = Lists.newArrayList();
            if (StringUtil.isNotBlank(userIds)) {
                officeList.addAll(Arrays.asList(StringUtil.split(userIds, StringUtil.DOTH)));
            }

            //插入发布通知
            if (officeList != null && officeList.size() > 0) {
                User teamUser = UserUtils.getUser();
                Team team = teamService.get(teamId);
                teamUserRelationService.inseRelOaNoOffice(team, teamUser, officeList);
            }
            json.put("success", true);
        } catch (Exception e) {
            json.put("success", false);
            logger.error(e.getMessage());
        }

        return json;
    }


    //团队负责人批量邀请
    @RequestMapping(value = "toInvite")
    @ResponseBody
    public JSONObject toInvite(String offices, String userIds, String userType, String teamId, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        return teamUserRelationService.frontToInvite(offices, userIds, userType, teamId);
    }


    //团队负责人直接拉入
    @RequestMapping(value = "pullIn")
    @ResponseBody
    public JSONObject pullIn(String offices, String userIds, String userType, String teamId, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        return teamUserRelationService.pullIn(offices, userIds, userType, teamId);
    }


}
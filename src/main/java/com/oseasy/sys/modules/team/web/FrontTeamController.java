package com.oseasy.sys.modules.team.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.act.modules.act.utils.ThreadUtils;
import com.oseasy.auy.modules.team.AuyTeamService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotifyRecord;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.persistence.PageMap;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.OfficeService;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.pcore.modules.sys.vo.UserVo;
import com.oseasy.sys.common.config.SysSval;
import com.oseasy.sys.common.config.SysSval.SysEmskey;
import com.oseasy.sys.common.utils.SysUserUtils;
import com.oseasy.sys.modules.sys.enums.RoleBizTypeEnum;
import com.oseasy.sys.modules.sys.utils.SysConfigUtil;
import com.oseasy.sys.modules.sys.vo.ConfRange;
import com.oseasy.sys.modules.sys.vo.SysConfigVo;
import com.oseasy.sys.modules.sys.vo.TeamConf;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.entity.TeamDetails;
import com.oseasy.sys.modules.team.entity.TeamUserRelation;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.sys.modules.team.service.TeamUserRelationService;
import com.oseasy.sys.modules.team.vo.TeamMtype;
import com.oseasy.util.common.utils.Msg;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 团队管理Controller
 *
 * @author 刘波
 * @version 2017-03-30
 */
@Controller
@RequestMapping(value = "${frontPath}/team")
public class FrontTeamController extends BaseController {
    @Autowired
    private OaNotifyService oaNotifyService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private UserService userService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private TeamUserRelationService teamUserRelationService;
    @Autowired
    private AuyTeamService auyTeamService;
    @ModelAttribute
    public Team get(@RequestParam(required = false) String id) {
        Team entity = null;
        if (StringUtil.isNotBlank(id)) {
            entity = teamService.get(id);
        }
        if (entity == null) {
            entity = new Team();
        }
        return entity;
    }


    @RequestMapping(value = "form")
    public String form(Team team, Model model) {
        if (team.getId() != null) {
            team = teamService.get(team.getId());
        }
        model.addAttribute("team", team);

        return SysSval.path.vms(SysEmskey.TEAM.k()) + "teamForm";
    }

    @RequiresPermissions("team:team:edit")
    @RequestMapping(value = "save")
    public String save(Team team, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, team)) {
            return form(team, model);
        }
        teamService.save(team);
        addMessage(redirectAttributes, "保存团队成功");
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/team/team/?repage";
    }

    @RequestMapping(value = "delete")
    public String delete(Team team, RedirectAttributes redirectAttributes) {
        teamService.delete(team);
        addMessage(redirectAttributes, "删除团队成功");
        return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/team/?repage";

    }

    @RequestMapping(value = "batchDelete")
    public String batchDelete(Team team, RedirectAttributes redirectAttributes) {
        if (StringUtil.isNotEmpty(team.getId())) {
            teamService.batchDis(team);
            addMessage(redirectAttributes, "删除团队成功");
        } else {
            addMessage(redirectAttributes, "团队为空，删除团队失败");
        }
        return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/team/?repage";

    }

    // 解散团队不可见
    @RequestMapping(value = "hiddenDelete")
    @ResponseBody
    public Msg hiddenDelete(Team team, RedirectAttributes redirectAttributes) {
        User curUser = UserUtils.getUser();// 获取当前用户的信息
        return teamService.hiddenDelete(team.getTeamIds(), curUser);
    }


    @RequestMapping(value = "batchDis")
    public String batchDis(Team team, RedirectAttributes redirectAttributes) {
        teamService.batchDis(team);
        addMessage(redirectAttributes, "解散团队成功");
        return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/team/?repage";

    }

    // 项目负责人组建项目团队
    @RequestMapping(value = "buildTeam")
    public String buildTeam(Team team, Model model) {
        model.addAttribute("team", team);
        // 找到导师List
        User master = new User();
        List<String> rbts = new ArrayList<String>();
        rbts.add(RoleBizTypeEnum.DS.getValue());
        master.setRoleBizTypes(rbts);
        List<User> masterList = userService.findByRoleBizType(master);
        model.addAttribute("masterList", masterList);

        // 找到学生List
        User stu = new User();
        List<String> srbts = new ArrayList<String>();
        srbts.add(RoleBizTypeEnum.XS.getValue());
        stu.setRoleBizTypes(srbts);
        stu.setId(UserUtils.getUser().getId());
        List<User> studentList = userService.findByRoleBizType(stu);
        model.addAttribute("studentList", studentList);

        return SysSval.path.vms(SysEmskey.TEAM.k()) + "buildTeam";
    }

    // 首页我的团队
    @RequestMapping(value = "ajaxIndexMyTeamList" , method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxIndexMyTeamList(Team team, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            HashMap<String, Object> hashMap = new HashMap<>();
            String msg = request.getParameter("msg");
            hashMap.put("msg", msg);
            String opType = request.getParameter("opType");
            hashMap.put("opType", opType);
            String proType = request.getParameter("proType");
            if (proType != null) {
                if (proType.equals("1") || proType.equals("2")) {
                    team.setMemberNum(5);
                }
                hashMap.put("proType", proType);
            }
            User user = UserUtils.getUser();
            team.setUser(user);
            if (user != null) {
                hashMap.put("curUserId", user.getId());
            } else {
                hashMap.put("curUserId", null);
            }
    //		Page<Team> page = teamService.findPage(new Page<Team>(request, response), team);

    //		model.addAttribute("page", page);
//            User curUser = UserUtils.getUser();
            String utype = null;
            if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XS)) {
                utype = RoleBizTypeEnum.XS.getValue();
            } else if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.DS)) {
                utype = RoleBizTypeEnum.DS.getValue();
            }
            hashMap.put("userType", utype);
            hashMap.put("qryForm", team);
            SysConfigVo scv = SysConfigUtil.getSysConfigVo();
            if (scv != null) {
                TeamConf tc = scv.getTeamConf();
                if (tc != null) {
                    hashMap.put("teamCheckOnOff", tc.getTeamCheckOnOff());
                    if ("1".equals(scv.getTeamConf().getIntramuralValiaOnOff())) {
                        ConfRange cr = scv.getTeamConf().getIntramuralValia();
                        int min = Integer.valueOf(cr.getMin());
                        int max = Integer.valueOf(cr.getMax());
                        hashMap.put("teacherMin", min);
                        hashMap.put("teacherMax", max);
                    }
                    String maxms = scv.getTeamConf().getMaxMembers();
                    hashMap.put("studentMax", maxms);
                }
            }
            return ApiResult.success(hashMap);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value = "/ajaxTeamList", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiTstatus<HashMap<String, Object>> ajaxTeamList(Team team, HttpServletRequest request, HttpServletResponse response) {
        ApiTstatus<HashMap<String, Object>> actYwRstatus = new ApiTstatus(false, "查询失败");
        User user = UserUtils.getUser();
        team.setUser(user);
        Page<Team> page = auyTeamService.findPage(new Page<Team>(request, response), team);
        Map<String, Object> teamListMap = new PageMap().getPageMap(page);
        actYwRstatus.setStatus(true);
        actYwRstatus.setMsg("查询成功");
        actYwRstatus.setDatas((HashMap<String, Object>) teamListMap);
        return actYwRstatus;
    }


    @RequestMapping(value = "findByTeamId")
    public String teamDetails(Model model, String id, HttpServletRequest request) {
        String cuid = UserUtils.getUser().getId();
        String stuType = "1";
        List<TeamDetails> teamInfo = teamService.findTeamInfo(id, stuType);// 查询学生list
        String masterType = "2";
        List<TeamDetails> teamTeacherInfo = teamService.findTeamInfo(id, masterType);// 查询导师list
        TeamDetails teamDetails = teamService.findTeamDetails(id);// 查询团队详情
        if (teamDetails.getSponsor()!=null && !teamDetails.getSponsor().equals(cuid)) {
            model.addAttribute("notSponsor", true);
        } else {
            model.addAttribute("notSponsor", false);
        }
        if (teamUserRelationService.findUserHasJoinTeam(cuid, id) != null) {
            model.addAttribute("hasJoin", true);
        } else {
            model.addAttribute("hasJoin", false);
        }

        User user = systemService.getUser(teamDetails.getSponsor());
        teamDetails.setLeaderid(teamDetails.getSponsor());
        teamDetails.setSponsor(user.getName());
        Office officeTeam = officeService.get(teamDetails.getLocalCollege());
        if (officeTeam != null) {
            teamDetails.setLocalCollege(officeTeam.getName());
        }
        model.addAttribute("teamDetails", teamDetails);// 查询团队详情信息
        model.addAttribute("teamInfo", teamInfo);
        model.addAttribute("teamTeacherInfo", teamTeacherInfo);
        String from = request.getParameter("from");
        String notifyId = request.getParameter("notifyId");
        if ("notify".equals(from)) {
            OaNotify on = oaNotifyService.get(notifyId);
            if(on.getType().equals("7")){
                OaNotifyRecord oaNotifyRecord = oaNotifyService.getTeamOaNotify(on);
                if(null != oaNotifyRecord && oaNotifyRecord.getDelFlag().equals(Const.YES)){
                    model.addAttribute("notifyError", "消息已被撤回");
                }
            }
            TeamUserRelation param = new TeamUserRelation();
            param.setUser(UserUtils.getUser());
            param.setTeamId(id);
            if (on != null && teamUserRelationService.findUserHasJoinTeam(param) == null) {
                model.addAttribute("notifyType", on.getType());
                model.addAttribute("from", from);
                model.addAttribute("notifyId", notifyId);
            }
        }
        return SysSval.path.vms(SysEmskey.TEAM.k()) + "teamDetails";
    }

    @RequestMapping(value = "findById")
    @ResponseBody
    public Team findById(Team team, HttpServletRequest request, HttpServletResponse response, Model model, String id)
            throws IOException {
        Team teamInfo = teamService.get(id);// 根据id获取单条信息
        return teamInfo;

    }

    /**
     * 申请加入
     *
     * @param model
     * @param teamId
     * @return
     */
    @RequestMapping(value = "applyJoin")
    @ResponseBody
    public String applyJoin(HttpServletRequest request, HttpServletResponse response,
                            Model model, String teamId, RedirectAttributes redirectAttributes) {
        return teamService.applyJoin(teamId);
    }

    @RequestMapping(value = "relTeam")
    public String fbTeam(Team team, HttpServletRequest request, HttpServletResponse response, Model model) {

        return SysSval.path.vms(SysEmskey.TEAM.k()) + "relTeam";
    }

    @RequestMapping(value = {"index"})
    public String index(User user, Model model) {
        return SysSval.path.vms(SysEmskey.SYS.k()) + "userIndex";

    }

    // 查询团队建设人员
    @RequestMapping(value = "teambuild")
    public String teambuild(Model model, String id, HttpServletRequest request) {
        String stuType = "1";
        List<TeamDetails> teamInfo = teamService.findTeamByTeamId(id, stuType);// 查询学生list
        String masterType = "2";
        List<TeamDetails> teamTeacherInfo = teamService.findTeamByTeamId(id, masterType);// 查询导师list
        String teamId = request.getParameter("id");
        model.addAttribute("teamId", teamId);// 查询团队组员
        Team team = teamService.get(teamId);
        if(team != null){
            model.addAttribute(Team.FIRST_TEACHER, team.getFirstTeacher());// 查询团队组员
        }else{
            model.addAttribute(Team.FIRST_TEACHER, "");// 查询团队组员
        }
//        model.addAttribute("teamInfo", teamInfo);// 查询团队组员
//        model.addAttribute("teamTeacherInfo", teamTeacherInfo);// 查询团队组员
        return SysSval.path.vms(SysEmskey.TEAM.k()) + "foundTeam";
    }

    @ResponseBody
    @RequestMapping(value = "/ajaxFirstTeacher/{id}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ApiTstatus<?> ajaxFirstTeacher(@PathVariable("id") String id, String teamId) {
        ApiTstatus<?> actYwRstatus = new ApiTstatus();
        Team team = teamService.get(teamId);
        if(team != null){
            team.setFirstTeacher(id);
            teamService.updateAllInfo(team);
            actYwRstatus.setMsg("查询成功");
            actYwRstatus.setStatus(true);
        }else{
            actYwRstatus.setMsg("团队不存在");
            actYwRstatus.setStatus(false);
        }
        return actYwRstatus;
    }

    //查询组队情况 by 王清腾
    @RequestMapping(value = "/ajaxTeamMember/{id}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiTstatus<HashMap<String, Object>> ajaxTeamList(@PathVariable("id") String id, String userType, HttpServletRequest request) {
        ApiTstatus<HashMap<String, Object>> actYwRstatus = new ApiTstatus<HashMap<String, Object>>();
        List teamMember = TeamMtype.getAll();
        List<TeamDetails> studentList = null;
        List<TeamDetails> teacherList = null;
        switch (userType) {
            case "1":
                studentList = teamService.findTeamByTeamId(id, TeamMtype.TM_STU.getKey());
                break;
            case "2":
                teacherList = teamService.findTeamByTeamId(id, TeamMtype.TM_TEACH.getKey());
                break;
            default:
                studentList = teamService.findTeamByTeamId(id, TeamMtype.TM_STU.getKey());
                teacherList = teamService.findTeamByTeamId(id, TeamMtype.TM_TEACH.getKey());
        }

        HashMap<String, Object> memberMap = new HashMap<String, Object>();
        memberMap.put("teamId", id);
        memberMap.put("teamMember", teamMember.toString());
        memberMap.put("studentList", studentList);
        memberMap.put("teacherList", teacherList);
        actYwRstatus.setMsg("查询成功");
        actYwRstatus.setStatus(true);
        actYwRstatus.setDatas(memberMap);
        return actYwRstatus;
    }

    @RequestMapping(value = "/ajaxDeleteTeamMember/{id}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiTstatus<String> ajaxDeleteTeamMember(@PathVariable("id") String id, String userId, String teamId, String turId) {
        TeamUserRelation tt = teamUserRelationService.get(turId);
        if (tt != null) {
            if ("0".equals(tt.getState()) || "4".equals(tt.getState())) {
                int projectCount = teamService.checkTeamIsInProject(teamId);// 根据teamid查询项目是否正在进行中
                if (projectCount > 0) {
                    return new ApiTstatus<String>(false, "删除失败");
                }
            }
            teamService.deleteTeamUserInfo(tt);
        }
        return new ApiTstatus<String>(true, "删除成功");
    }


    // 查询团队建设人员
    @RequestMapping(value = "/foundTeamStu", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public JSONObject foundTeamStu(String teamId, String type, HttpServletRequest request) {
        JSONObject js = new JSONObject();
        js.put("ret", "0");
        List<TeamDetails> teamInfo = teamService.findTeamByTeamId(teamId, type);// 查询学生list
//		String masterType = "2";
//		List<TeamDetails> teamTeacherInfo = teamService.findTeamByTeamId(teamId, type);// 查询导师list
        if (StringUtil.checkNotEmpty(teamInfo)) {
            JSONArray json = new JSONArray();
            for (TeamDetails teamDetail : teamInfo) {
                JSONObject jsonTeam = new JSONObject();
                String userId = teamDetail.getUserId();
                jsonTeam.put("id", userId);
                jsonTeam.put("name", teamDetail.getuName());
                jsonTeam.put("userType", teamDetail.getUserType());
                jsonTeam.put("no", teamDetail.getNo());
                jsonTeam.put("curJoin", teamDetail.getCurJoin());
                jsonTeam.put("curState", teamDetail.getCurrState());
                jsonTeam.put("state", teamDetail.getState());
                jsonTeam.put("officeId", teamDetail.getOfficeId());
                jsonTeam.put("professional", teamDetail.getProfessional());
                jsonTeam.put("domainlt", teamDetail.getDomainlt());
                json.add(jsonTeam);
            }

            js.put("data", json);
            js.put("ret", "1");
        }

        return js;
    }

    // 删除团队信息
    @RequestMapping(value = "deleteTeamUserInfo")
    public String deleteTeamUserInfo(String userId, String teamId, String turId, RedirectAttributes redirectAttributes) {
        TeamUserRelation tt = teamUserRelationService.get(turId);
        if (tt != null) {
            if ("0".equals(tt.getState()) || "4".equals(tt.getState())) {
                int projectCount = teamService.checkTeamIsInProject(teamId);// 根据teamid查询项目是否正在进行中
                if (projectCount > 0) {
                    addMessage(redirectAttributes, "删除失败,该团队有正在进行的项目或者大赛!");
                    return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/team/teambuild?id=" + teamId;
                }
            }
            teamService.deleteTeamUserInfo(tt);
        }
        return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/team/teambuild?id=" + teamId;
    }

    // 同意
    @RequestMapping(value = "checkInfo")
    public String checkInfo(String userId, String teamId, TeamUserRelation teamUserRelation,
                            RedirectAttributes redirectAttributes) {
        teamUserRelation.setTeamId(teamId);
        User user = new User();
        user.setId(userId);
        teamUserRelation.setUser(user);
        teamService.acceptJoinTeam(teamUserRelation);
        return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/team/teambuild?id=" + teamId;
    }

    // 解散团队不可见
    @RequestMapping(value = "disTeam") // 解散disTeam
    @ResponseBody
    public Msg disTeam(Team team, HttpServletRequest request, HttpServletResponse response) {
//        User curUser = UserUtils.getUser();// 获取当前用户的信息
       //return teamService.disTeam(team.getTeamIds());
        return teamService.disTeamById(team);
    }

    // 团队组建情况拒绝申请
    @RequestMapping(value = "refuseInviation")
    public String refuseInviation(HttpServletRequest request) {
        String turId = request.getParameter("turId");
        String teamId = request.getParameter("teamId");
        teamService.refuseInviation(turId, teamId);
        return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/team/teambuild?id=" + teamId;
    }

    // 团队组建情况接受申请
    @RequestMapping(value = "acceptInviation")
    public String acceptInviation(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String teamId = request.getParameter("teamId");
        String userId = request.getParameter("userId");
        JSONObject ret = teamService.acceptInviation(userId, teamId);
        if (ret != null) {
            addMessage(redirectAttributes, ret.getString("msg"));
        }
        return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/team/teambuild?id=" + teamId;

    }

    //从消息框接受邀请
    @RequestMapping(value = "acceptInviationByNotify")
    @ResponseBody
    public JSONObject acceptInviationByNotify(HttpServletRequest request) {
        String oaNotifyId = request.getParameter("send_id");
        return teamService.acceptInviationByNotify(oaNotifyId);
    }

    //从消息框接受邀请 新
    @RequestMapping(value = "acceptNotify")
    @ResponseBody
    public ApiResult acceptNotify(String send_id) {
        try {
            JSONObject jsonObject = teamService.acceptInviationByNotify(send_id);
            if(jsonObject.getString("ret").equals("0")){
                return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR) + ":" + jsonObject.getString("msg"));
            }
            return ApiResult.success(jsonObject);
        } catch (Exception e) {
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR) + ":" + e.getMessage());
        }
    }

    //从消息框拒绝邀请
    @RequestMapping(value = "refuseInviationByNotify", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JSONObject refuseInviationByNotify(HttpServletRequest request) {
        String oaNotifyId = request.getParameter("send_id");
        return teamService.refuseInviationByNotify(oaNotifyId);
    }

    //从消息框接受拒绝 新
    @RequestMapping(value = "refuseNotify", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult refuseNotify(String send_id) {
        try {
            JSONObject jsonObject = teamService.refuseInviationByNotify(send_id);
            if(jsonObject.getString("ret").equals("0")){
                return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR) + ":" + jsonObject.getString("msg"));
            }
            return ApiResult.success(jsonObject);
        } catch (Exception e) {
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR) + ":" + e.getMessage());
        }
    }


    //同意加入团队 by王清腾
    @RequestMapping(value = "acceptUserInvitation/{teamId}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiTstatus<HashMap<String, Object>> acceptUserInviting(@PathVariable("teamId") String teamId, String userId, TeamUserRelation teamUserRelation) {
        ApiTstatus<HashMap<String, Object>> actYwRstatus = new ApiTstatus<HashMap<String, Object>>(false, "加入团队失败");
        teamUserRelation.setTeamId(teamId);
        User user = new User();
        user.setId(userId);
        teamUserRelation.setUser(user);
        JSONObject joinTeam = teamService.acceptJoinTeam(teamUserRelation);
        actYwRstatus.setMsg(joinTeam.getString("msg"));
        actYwRstatus.setStatus((joinTeam.getString("ret").equals("1")));
        return actYwRstatus;
    }

    //拒绝加入团队 by王清腾
    @RequestMapping(value = "refuseUserInvitation/{teamId}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiTstatus<HashMap<String, Object>> refuseUserInviting(@PathVariable("teamId") String teamId, String userId, String turId) {
        ApiTstatus<HashMap<String, Object>> actYwRstatus = new ApiTstatus<HashMap<String, Object>>(false, "不同意失败");
        JSONObject result = teamService.refuseInviation(turId, teamId);
        actYwRstatus.setMsg(result.getString("msg"));
        actYwRstatus.setStatus((result.getString("ret").equals("1")));
        return actYwRstatus;
    }

//	//从消息框接受邀请
//	@RequestMapping(value="acceptInviationByNotify")
//	@ResponseBody
//	public JSONObject acceptInviationByNotify(HttpServletRequest request) {
//		String oaNotifyId = request.getParameter("send_id");
//		return teamService.acceptInviationByNotify(oaNotifyId);
//	}
//	//从消息框拒绝邀请
//	@RequestMapping(value="refuseInviationByNotify")
//	@ResponseBody
//	public JSONObject refuseInviationByNotify(HttpServletRequest request) {
//		String oaNotifyId = request.getParameter("send_id");
//		return teamService.refuseInviationByNotify(oaNotifyId);
//	}

    /**
     * 校验团队是否处于正常状态.
     *
     * @param id 团队ID
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "ajaxUsable/{id}")
    public ApiTstatus<Team> ajaxUsable(@PathVariable(value = "id") String id) {
        Team team = teamService.getByUsable(id);
        if ((team == null)) {
            return new ApiTstatus<Team>(false, "团队不存在或非建设完毕状态");
        }
        return new ApiTstatus<Team>(true, "查询成功", team);
    }

      //获得团队接口
    @RequestMapping(value = "findTeamPerson" , method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxFindTeamPerson(String teamId ,HttpServletRequest request, HttpServletResponse response) {
        ApiResult  result = new ApiResult();
        try {
            Map<String,Object> map=  new HashMap<>();
            CompletableFuture<List<Map<String, String>>> studentFuture = CompletableFuture.supplyAsync(() -> teamUserRelationService.findTeamStudentTeamId(teamId), ThreadUtils.newFixedThreadPool());
            CompletableFuture<List<Map<String, String>>> teacherFuture = CompletableFuture.supplyAsync(() -> teamUserRelationService.findTeamTeacherTeamId(teamId),ThreadUtils.newFixedThreadPool());
            CompletableFuture.allOf(studentFuture,teacherFuture).join();
            List<Map<String, String>> stuList = null;
            List<Map<String, String>> teaList = null;
            try {
                stuList = studentFuture.get();
                teaList = teacherFuture.get();
            } catch (InterruptedException e) {
                logger.info("查询线程中断,ajaxFindTeamPerson");
                e.printStackTrace();
            } catch (ExecutionException e) {
                logger.info("执行异常,ajaxFindTeamPerson");
                e.printStackTrace();
            }finally {
                ThreadUtils.shutdown();
            }
            map.put("stuList",stuList);
            map.put("teaList",teaList);
            result.setData(map);
            result.setStatus(ApiConst.STATUS_SUCCESS);
            result.setCode(ApiConst.CODE_REQUEST_SUCCESS);
            return result;
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value="getUserStudentTeam", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult getUserStudentTeam(@RequestBody String[] userIds){
        try {
            List<UserVo> list = userService.getStudentInfo(userIds);
            return ApiResult.success(list);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value="getUserTeacherTeam", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult getUserTeacherTeam(@RequestBody String[] userIds){
        try {
            List<UserVo> list = userService.getTeaInfo(userIds);
            return ApiResult.success(list);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

}

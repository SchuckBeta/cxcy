package com.oseasy.sys.modules.team.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.OfficeService;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.vo.UserVo;
import com.oseasy.sys.common.config.SysSval;
import com.oseasy.sys.common.config.SysSval.SysEmskey;
import com.oseasy.sys.modules.sys.utils.SysConfigUtil;
import com.oseasy.sys.modules.sys.vo.SysConfigVo;
import com.oseasy.sys.modules.sys.vo.TeamConf;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.entity.TeamDetails;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.sys.modules.team.service.TeamUserRelationService;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

import net.sf.json.JSONObject;


/**
 * 团队管理Controller
 *
 * @author 刘波
 * @version 2017-03-30
 */
@Controller
@RequestMapping(value = "${adminPath}/team")
public class TeamController extends BaseController {
    @Autowired
    private TeamService teamService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private TeamUserRelationService teamUserRelationService;
    @Autowired
    private UserService userService;
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

    @RequestMapping(value = "getTeamCountToAudit")
    @ResponseBody
    public Long getTeamCountToAudit() {
        return teamService.getTeamCountToAudit();
    }

    @RequiresPermissions("team:audit:edit")
    @RequestMapping(value = "checkTeam")
    @ResponseBody
    public JSONObject checkTeam(HttpServletRequest request) {
        String teamId = request.getParameter("teamId");
        String res = request.getParameter("res");
        return teamService.checkTeam(teamId, res);
    }

    @RequiresPermissions("team:audit:edit")
    @RequestMapping(value = "unAutoCheck")
    @ResponseBody
    public JSONObject unAutoCheck() {
        return teamService.unAutoCheck();
    }

    @RequiresPermissions("team:audit:edit")
    @RequestMapping(value = "autoCheck")
    @ResponseBody
    public JSONObject autoCheck() {
        return teamService.autoCheck();
    }

    @RequestMapping(value = {"list", ""})
    public String list(Team team, HttpServletRequest request, HttpServletResponse response, Model model) {
//        Page<Team> page = teamService.findPage(new Page<Team>(request, response), team);
//        model.addAttribute("page", page);
//        model.addAttribute("org", UserUtils.getOffice(request.getParameter("localCollege")));
        SysConfigVo scv = SysConfigUtil.getSysConfigVo();
        if (scv != null) {
            TeamConf tc = scv.getTeamConf();
            if (tc != null) {
                model.addAttribute("teamCheckOnOff", tc.getTeamCheckOnOff());
            }
        }
        return SysSval.path.vms(SysEmskey.TEAM.k()) + "teamList";
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
    @RequestMapping(value="getTeamList", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getTeamList(Team team, HttpServletRequest request, HttpServletResponse response){
        try {
            Page<Team> page = auyTeamService.findPage(new Page<Team>(request, response), team);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value="getTeamCheckOnOff", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getTeamCheckOnOff(){
        try {
            SysConfigVo scv = SysConfigUtil.getSysConfigVo();
            if (scv != null) {
                TeamConf tc = scv.getTeamConf();
                if (tc != null) {
                    return ApiResult.success(tc.getTeamCheckOnOff());
                }
            }
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":团队没有配置");
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    //	@RequiresPermissions("team:team:edit")
    @RequestMapping(value = "delete")
    public String delete(Team team, RedirectAttributes redirectAttributes) {
        // 根据teamid判断团队状态
        Team teamTmp = teamService.get(team);
        if (!"2".equals(teamTmp.getState())) {// 如果不是解散状态
            addMessage(redirectAttributes, "团队未解散,不可删除");
        } else {
            team.setDelFlag("1");
            teamService.delete(team);
            addMessage(redirectAttributes, "删除团队成功");
        }
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/team/?repage";

    }




    @RequestMapping(value = "batchDelete")
    public String batchDelete(Team team, RedirectAttributes redirectAttributes, HttpServletRequest request,
                              HttpServletResponse response) {
        String idsre = request.getParameter("ids");
        String endString = teamService.batchDelete(team, idsre);
        addMessage(redirectAttributes, endString);
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/team/?repage";
    }

    @RequestMapping(value = "delTeam", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult delTeam(@RequestBody Team team){
        try {
            if(!"2".equals(team.getState())){
              return  ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":团队未解散,不可删除");
            }
            team.setDelFlag("1");
            teamService.delete(team);
            return  ApiResult.success(team);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value = "delTeams", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult delTeams(@RequestBody List<Team> teamList){
        try {
            HashMap<String, Object> hashMap = new HashMap<>();
            List<Team> teamDelSuccess = new ArrayList<>();
            List<Team> teamDelError = new ArrayList<>();
            for (Team team : teamList){
                Team nTeam = teamService.get(team);
                if(!"2".equals(nTeam.getState())){
                    teamDelError.add(nTeam);
                    continue;
                }
                team.setDelFlag("1");
                teamService.delete(nTeam);
                teamDelSuccess.add(nTeam);
            }
            hashMap.put("success", teamDelSuccess);
            hashMap.put("error", teamDelError);
            return ApiResult.success(hashMap);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }



    @RequestMapping(value = "disTeam") // 解散
    public String disTeam(Team team, RedirectAttributes redirectAttributes, HttpServletRequest request,
                          HttpServletResponse response, Model model) {
        // 根据teamid判断团队状态
        Team teamTmp = teamService.get(team);
        if ("2".equals(teamTmp.getState())) {// 如果为解散状态
            addMessage(redirectAttributes, "团队已是解散状态");
            return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/team/?repage";
        } else {
            int projectCount = teamService.checkTeamIsInProject(team.getId());// 根据teamid查询项目是否正在进行中
            if (projectCount > 0) {
                addMessage(redirectAttributes, "该团队有未完成的项目或者大赛,不可解散");
                return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/team/?repage";
            }
        }
        teamService.disTeam(team);// 修改团队状态为解散状态
        addMessage(redirectAttributes, "解散团队成功");
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/team/?repage";

    }

    @RequestMapping(value="dissolveTeam", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult dissolveTeam(@RequestBody Team team){
        try {
            return teamService.dissolveTeam(team);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value="dissolveTeams", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult dissolveTeams(@RequestBody List<Team> teamList){
        try {
            List<Team> disTeamsSuccess = new ArrayList<>();
            List<Team> disTeamsError = new ArrayList<>();
            HashMap<String, Object> hashMap = new HashMap<>();
            for (Team team : teamList) {
                Team nTeam = teamService.get(team);
                ApiResult apiResult = teamService.dissolveTeam(nTeam);
                if(apiResult.getStatus().equals(ApiConst.STATUS_SUCCESS)){
                    disTeamsSuccess.add(nTeam);
                }else {
                    disTeamsError.add(nTeam);
                }
            }
            hashMap.put("success", disTeamsSuccess);
            hashMap.put("error", disTeamsError);
            return ApiResult.success(hashMap);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }



    @RequestMapping(value = "batchDis")
    public String batchDis(Team team, RedirectAttributes redirectAttributes) {
        if (StringUtil.isNotEmpty(team.getId())) {
            teamService.batchDis(team);
            addMessage(redirectAttributes, "解散团队成功");
        } else {
            addMessage(redirectAttributes, "团队为空，解散团队失败");
        }
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/team/?repage";

    }

    @RequestMapping(value = "toTeamAudit")
    @RequiresPermissions("team:audit:edit")
    public String toTeamAudit(Model model, String id) {
        String stuType = "1";
        List<TeamDetails> teamInfo = teamService.findTeamInfo(id, stuType);// 查询学生list
        String masterType = "2";
        List<TeamDetails> teamTeacherInfo = teamService.findTeamInfo(id, masterType);// 查询导师list
        TeamDetails teamDetails = teamService.findTeamDetails(id);// 查询团队详情
        User user = systemService.getUser(teamDetails.getSponsor());
        if (user != null) {

            if (StringUtil.isNotEmpty(user.getName())) {
                teamDetails.setSponsor(user.getName());
            }
        }
        Office officeTeam = officeService.get(teamDetails.getLocalCollege());
        if (officeTeam != null) {
            teamDetails.setLocalCollege(officeTeam.getName());
            model.addAttribute("teamDetails", teamDetails);// 查询团队详情信息
        }
        model.addAttribute("teamInfo", teamInfo);
        model.addAttribute("teamTeacherInfo", teamTeacherInfo);
        return SysSval.path.vms(SysEmskey.TEAM.k()) + "teamAudit";
    }

    @RequestMapping(value = "findByTeamId")
    public String teamDetails(Model model, String id) {
        String stuType = "1";
        List<TeamDetails> teamInfo = teamService.findTeamInfo(id, stuType);// 查询学生list
        String masterType = "2";
        List<TeamDetails> teamTeacherInfo = teamService.findTeamInfo(id, masterType);// 查询导师list
        TeamDetails teamDetails = teamService.findTeamDetails(id);// 查询团队详情
        User user = systemService.getUser(teamDetails.getSponsor());
        if (user != null) {

            if (StringUtil.isNotEmpty(user.getName())) {
                teamDetails.setSponsor(user.getName());
            }
        }
        Office officeTeam = officeService.get(teamDetails.getLocalCollege());
        if (officeTeam != null) {
            teamDetails.setLocalCollege(officeTeam.getName());
//            model.addAttribute("teamDetails", teamDetails);// 查询团队详情信息
        }
        model.addAttribute("teamDetails", teamDetails);// 查询团队详情信息
//        List<ProjectExpVo> projectExpVo = teamService.findProjectByTeamId(id);//查询项目经历
//        List<GContestUndergo> gContest = teamService.findGContestByTeamId(id);//查询大赛经历

//        model.addAttribute("projectExpVo", projectExpVo);
//        model.addAttribute("gContestExpVo", gContest);
        model.addAttribute("teamInfo", teamInfo);
        model.addAttribute("teamTeacherInfo", teamTeacherInfo);
        return SysSval.path.vms(SysEmskey.TEAM.k()) + "teamDetails_admin";
    }

    @RequestMapping(value = "findById")
    public @ResponseBody
    Team findById(Team team, HttpServletRequest request, HttpServletResponse response, Model model,
                  String id) {
        Team teamInfo = teamService.get(id);// 根据id获取单条信息
        return teamInfo;
    }

    //获得团队接口
    @RequestMapping(value = "findTeamPerson" , method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxFindTeamPerson(String teamId ,HttpServletRequest request, HttpServletResponse response) {
        ApiResult  result = new ApiResult();
        try {
            Map<String,Object> map=  new HashMap<>();
            List<Map<String, String>> stuList = teamUserRelationService.findTeamStudent(teamId);
            List<Map<String, String>> teaList = teamUserRelationService.findTeamTeacher(teamId);
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

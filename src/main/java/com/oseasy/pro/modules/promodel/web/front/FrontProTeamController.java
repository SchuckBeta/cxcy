package com.oseasy.pro.modules.promodel.web.front;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.act.modules.actyw.tool.process.vo.FlowPcategoryType;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.promodel.service.ProStudentExpansionService;
import com.oseasy.sys.common.config.SysSval;
import com.oseasy.sys.common.config.SysSval.SysEmskey;
import com.oseasy.sys.common.utils.SysUserUtils;
import com.oseasy.sys.modules.sys.enums.RoleBizTypeEnum;
import com.oseasy.sys.modules.sys.utils.SysConfigUtil;
import com.oseasy.sys.modules.sys.vo.ConfRange;
import com.oseasy.sys.modules.sys.vo.PersonNumConf;
import com.oseasy.sys.modules.sys.vo.SysConfigVo;
import com.oseasy.sys.modules.sys.vo.TeamConf;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.sys.modules.team.vo.TeamStudentVo;
import com.oseasy.sys.modules.team.vo.TeamTeacherVo;
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
@RequestMapping(value = "${frontPath}/team")
public class FrontProTeamController extends BaseController {
    @Autowired
    private TeamService teamService;
    @Autowired
    private ProStudentExpansionService proStudentExpansionService;
    @Autowired
    private ProjectDeclareService projectDeclareService;

    @RequestMapping(value = "indexSave")
    public String indexSave(Team team, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request,
                            HttpServletResponse response) {

        try {
            String p1 = team.getName();
            Integer p4 = team.getMemberNum();
            Integer p5 = team.getSchoolTeacherNum();
            Integer p6 = team.getEnterpriseTeacherNum();
            String p7 = team.getMembership();
            String p8 = team.getProjectIntroduction();
            String p9 = team.getSummary();
            String type = request.getParameter("type");
            String proType = request.getParameter("proType");
            SysConfigVo scv = SysConfigUtil.getSysConfigVo();
            if (StringUtil.isNotEmpty(proType)) {//从项目页面跳转
                PersonNumConf pc = null;
                if (StringUtil.isNotEmpty(type) && StringUtil.isNotEmpty(proType)) {
                    if (scv != null) {
                        pc = SysConfigUtil.getProPersonNumConf(scv, type, proType);
                    }
                }
                if (pc != null) {
                    // 是否校验 1是有限制 0是无限制
                    if (pc != null && "1".equals(pc.getTeamNumOnOff())) {
                        ConfRange cr = pc.getTeamNum();
                        int min = Integer.valueOf(cr.getMin());
                        int max = Integer.valueOf(cr.getMax());
                        if (p4 < min || p4 > max) {
                            model.addAttribute("message", DictUtils.getDictLabel(proType, FlowPcategoryType.PCT_XM.getKey(), "") + "项目人数为" + min + "-" + max + "人!");
                            model.addAttribute("opType", "2");
                            return toTeamListPage(team, request, response, model);
                        }
                    }

                }
            }
            model.addAttribute("p1", p1);
            model.addAttribute("p4", p4);
            model.addAttribute("p5", p5);
            model.addAttribute("p6", p6);
            model.addAttribute("p7", p7);
            model.addAttribute("p8", p8);
            model.addAttribute("p9", p9);
            User curUser = UserUtils.getUser();
            if (SysUserUtils.checkHasRole(curUser, RoleBizTypeEnum.DS)) {
                redirectAttributes.addFlashAttribute("message", "导师暂时无法创建团队!");
                redirectAttributes.addFlashAttribute("opType", "1");
                return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/team/indexMyTeamList";

            }
            if (scv != null && scv.getTeamConf().getEnterYear() != null) {//入学N年创建团队限制
                Integer enterYear = null;
                try {
                    enterYear = Integer.valueOf(scv.getTeamConf().getEnterYear());
                } catch (NumberFormatException e) {
                    logger.error(ExceptionUtil.getStackTrace(e));
                }
                if (enterYear != null) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
                    Date date = proStudentExpansionService.getByUserId(curUser.getId()).getEnterdate();
                    if (date != null) {
                        Date newDate = new Date();
                        Integer newda = Integer.valueOf(formatter.format(newDate));
                        Integer dateString = Integer.valueOf(formatter.format(date));
                        if (dateString + enterYear > newda) {
                            redirectAttributes.addFlashAttribute("message", dateString + "级学生不能创建团队!");
                            redirectAttributes.addFlashAttribute("opType", "1");
                            return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/team/indexMyTeamList";
                        }
                    } else {
                        redirectAttributes.addFlashAttribute("message", "请先完善个人信息入学年份!");
                        redirectAttributes.addFlashAttribute("opType", "1");
                        return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/team/indexMyTeamList";
                    }
                }
            }
            if (p4 == 0) {
                model.addAttribute("message", "要求必须有一位团队成员!");
                model.addAttribute("opType", "2");
                return toTeamListPage(team, request, response, model);
            }
            if (scv != null) {
                if ("1".equals(scv.getTeamConf().getIntramuralValiaOnOff())) {
                    ConfRange cr = scv.getTeamConf().getIntramuralValia();
                    int min = Integer.valueOf(cr.getMin());
                    int max = Integer.valueOf(cr.getMax());
                    if (p5 < min || p5 > max) {
                        if (min == max) {
                            model.addAttribute("message", "校内导师人数为" + min + "人!");
                        } else {
                            model.addAttribute("message", "校内导师人数为" + min + "-" + max + "人!");
                        }
                        model.addAttribute("opType", "2");
                        return toTeamListPage(team, request, response, model);
                    }
                }
                String maxms = scv.getTeamConf().getMaxMembers();
                if (StringUtil.isNotEmpty(maxms) && p4 > Integer.valueOf(maxms)) {
                    model.addAttribute("message", "所需组员人数不能超过" + maxms + "人!");
                    model.addAttribute("opType", "2");
                    return toTeamListPage(team, request, response, model);
                }
            }
            if (StringUtil.isNotBlank(p7) && p7.length() > 200) {
                model.addAttribute("message", "组员要求字数不能超过200!");
                model.addAttribute("opType", "2");
                return toTeamListPage(team, request, response, model);
            }
            if (StringUtil.isNotBlank(p8) && p8.length() > 200) {
                model.addAttribute("message", "项目简介字数不能超过200!");
                model.addAttribute("opType", "2");
                return toTeamListPage(team, request, response, model);
            }
            if (StringUtil.isNotBlank(p9) && p9.length() > 500) {
                model.addAttribute("message", "团队介绍字数不能超过500!");
                model.addAttribute("opType", "2");
                return toTeamListPage(team, request, response, model);
            }

            if (StringUtil.isNotEmpty(team.getId())) {
                int projectCount = teamService.checkTeamIsInProject(team.getId());// 根据teamid查询项目是否正在进行中
                if (projectCount > 0) {
                    redirectAttributes.addFlashAttribute("message", "该团队有正在进行的项目或者大赛，不能修改团队信息!");
                    redirectAttributes.addFlashAttribute("opType", "1");
                    return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/team/indexMyTeamList";
                }
                teamService.editTeam(team);
                redirectAttributes.addFlashAttribute("message", "编辑团队信息成功!");
                redirectAttributes.addFlashAttribute("opType", "1");
                return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/team/indexMyTeamList";
            } else {
                Long countValid = teamService.countBuildByUserId(curUser);
                if (countValid == null) {
                    countValid = 0L;
                }
                Long teamMax = null;
                if (scv != null && "1".equals(scv.getTeamConf().getMaxOnOff())) {//团队创建数量限制
                    teamMax = Long.valueOf(scv.getTeamConf().getMax());
                    if (countValid >= teamMax) {
                        redirectAttributes.addFlashAttribute("message", "你创建的团队已经达到上限，无法继续创建！");
                        redirectAttributes.addFlashAttribute("opType", "1");
                        return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/team/indexMyTeamList";
                    }
                }
                team.setSponsor(curUser.getId());
                String officeId = curUser != null && curUser.getOffice() != null ? curUser.getOffice().getId() : null;
                team.setLocalCollege(officeId);
                team.setState("0");
                if (team.getEnterpriseTeacherNum() == null) {
                    team.setEnterpriseTeacherNum(0);
                }
                teamService.saveTeam(team, curUser);
                String msg = "";
                if (Team.state3.equals(team.getState())) {
                    msg = "需要等待管理员审核通过后再建设该团队！";
                }
//                else {
//                    msg = "你可以开始建设该团队！";
//                }
                if (teamMax != null) {
                    redirectAttributes.addFlashAttribute("message", "创建团队成功,你还能再创建" + (teamMax - countValid - 1) + "个团队!" + msg);
                } else {
                    redirectAttributes.addFlashAttribute("message", "创建团队成功!" + msg);
                }
                redirectAttributes.addFlashAttribute("opType", "1");
                redirectAttributes.addFlashAttribute("msg", "1");
                return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/team/indexMyTeamList";
            }
        } catch (Exception e) {
            logger.error("团队保存出错", e.getMessage());
            redirectAttributes.addFlashAttribute("message", "团队保存出现系统错误");
            redirectAttributes.addFlashAttribute("opType", "1");
            return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/team/indexMyTeamList";
        }
    }


    // 首页我的团队
    @RequestMapping(value = "indexMyTeamList")
    public String indexMyTeamList(Team team, HttpServletRequest request, HttpServletResponse response, Model model) {
        String msg = request.getParameter("msg");
        model.addAttribute("msg", msg);
        String opType = request.getParameter("opType");
        model.addAttribute("opType", opType);
        String proType = request.getParameter("proType");
        if (proType != null) {
            if (proType.equals("1") || proType.equals("2")) {
                team.setMemberNum(5);
            }
            model.addAttribute("proType", proType);
        }
        return toTeamListPage(team, request, response, model);
    }

    private String toTeamListPage(Team team, HttpServletRequest request, HttpServletResponse response, Model model) {
        User user = UserUtils.getUser();
        team.setUser(user);
        if (user != null) {
            model.addAttribute("curUserId", user.getId());
        } else {
            model.addAttribute("curUserId", null);
        }
//      Page<Team> page = teamService.findPage(new Page<Team>(request, response), team);

//      model.addAttribute("page", page);
        User curUser = UserUtils.getUser();
        String utype = null;
        if (SysUserUtils.checkHasRole(curUser, RoleBizTypeEnum.XS)) {
            utype = RoleBizTypeEnum.XS.getValue();
        } else if (SysUserUtils.checkHasRole(curUser, RoleBizTypeEnum.DS)) {
            utype = RoleBizTypeEnum.DS.getValue();
        }
        model.addAttribute("userType", utype);
        model.addAttribute("qryForm", team);
        SysConfigVo scv = SysConfigUtil.getSysConfigVo();
        if (scv != null) {
            TeamConf tc = scv.getTeamConf();
            if (tc != null) {
                model.addAttribute("teamCheckOnOff", tc.getTeamCheckOnOff());
                if ("1".equals(scv.getTeamConf().getIntramuralValiaOnOff())) {
                    ConfRange cr = scv.getTeamConf().getIntramuralValia();
                    int min = Integer.valueOf(cr.getMin());
                    int max = Integer.valueOf(cr.getMax());
                    model.addAttribute("teacherMin", min);
                    model.addAttribute("teacherMax", max);
                }
                String maxms = scv.getTeamConf().getMaxMembers();
                model.addAttribute("studentMax", maxms);

            }
        }
        return SysSval.path.vms(SysEmskey.TEAM.k()) + "indexTeam";
    }

    @RequestMapping(value = "ajaxIndexSave" , method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxIndexSave(@RequestBody Team team, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request,
                            HttpServletResponse response) {

        try {
            HashMap<String, Object> hashMap = new HashMap<>();
            String p1 = team.getName();
            Integer p4 = team.getMemberNum();
            Integer p5 = team.getSchoolTeacherNum();
            Integer p6 = team.getEnterpriseTeacherNum();
            String p7 = team.getMembership();
            String p8 = team.getProjectIntroduction();
            String p9 = team.getSummary();
            String type = request.getParameter("type");
            String proType = request.getParameter("proType");
            SysConfigVo scv = SysConfigUtil.getSysConfigVo();
            if (StringUtil.isNotEmpty(proType)) {//从项目页面跳转
                PersonNumConf pc = null;
                if (StringUtil.isNotEmpty(type) && StringUtil.isNotEmpty(proType)) {
                    if (scv != null) {
                        pc = SysConfigUtil.getProPersonNumConf(scv, type, proType);
                    }
                }
                if (pc != null) {
                    // 是否校验 1是有限制 0是无限制
                    if (pc != null && "1".equals(pc.getTeamNumOnOff())) {
                        ConfRange cr = pc.getTeamNum();
                        int min = Integer.valueOf(cr.getMin());
                        int max = Integer.valueOf(cr.getMax());
                        if (p4 < min || p4 > max) {
                            hashMap.put("message", DictUtils.getDictLabel(proType, FlowPcategoryType.PCT_XM.getKey(), "") + "项目人数为" + min + "-" + max + "人!");
                            hashMap.put("opType", "2");
                            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,(String)hashMap.get("message"));
                        }
                    }

                }
            }
            model.addAttribute("p1", p1);
            model.addAttribute("p4", p4);
            model.addAttribute("p5", p5);
            model.addAttribute("p6", p6);
            model.addAttribute("p7", p7);
            model.addAttribute("p8", p8);
            model.addAttribute("p9", p9);
            User curUser = UserUtils.getUser();
            if (SysUserUtils.checkHasRole(curUser, RoleBizTypeEnum.DS)) {
                hashMap.put("message", "导师暂时无法创建团队!");
                hashMap.put("opType", "1");
                return ApiResult.failed(ApiConst.CODE_INNER_ERROR,(String)hashMap.get("message"));
            }
            if (scv != null && scv.getTeamConf().getEnterYear() != null) {//入学N年创建团队限制
                Integer enterYear = null;
                try {
                    enterYear = Integer.valueOf(scv.getTeamConf().getEnterYear());
                } catch (NumberFormatException e) {
                    logger.error(ExceptionUtil.getStackTrace(e));
                }
                if (enterYear != null) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
                    Date date = proStudentExpansionService.getByUserId(curUser.getId()).getEnterdate();
                    if (date != null) {
                        Date newDate = new Date();
                        Integer newda = Integer.valueOf(formatter.format(newDate));
                        Integer dateString = Integer.valueOf(formatter.format(date));
                        if (dateString + enterYear > newda) {
                            hashMap.put("message", dateString + "级学生不能创建团队!");
                            hashMap.put("opType", "1");
                            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,(String)hashMap.get("message"));
                        }
                    } else {
                        hashMap.put("message", "请先完善个人信息入学年份!");
                        hashMap.put("opType", "1");
                        return ApiResult.failed(ApiConst.CODE_INNER_ERROR,(String)hashMap.get("message"));
                    }
                }
            }
            if (p4 == 0) {
                hashMap.put("message", "要求必须有一位团队成员!");
                hashMap.put("opType", "2");
                return ApiResult.failed(ApiConst.CODE_INNER_ERROR,(String)hashMap.get("message"));
            }
            if (scv != null) {
                if ("1".equals(scv.getTeamConf().getIntramuralValiaOnOff())) {
                    ConfRange cr = scv.getTeamConf().getIntramuralValia();
                    int min = Integer.valueOf(cr.getMin());
                    int max = Integer.valueOf(cr.getMax());
                    if (p5 < min || p5 > max) {
                        if (min == max) {
                            hashMap.put("message", "校内导师人数为" + min + "人!");
                        } else {
                            hashMap.put("message", "校内导师人数为" + min + "-" + max + "人!");
                        }
                        hashMap.put("opType", "2");
                        return ApiResult.failed(ApiConst.CODE_INNER_ERROR,(String)hashMap.get("message"));
                    }
                }
                String maxms = scv.getTeamConf().getMaxMembers();
                if (StringUtil.isNotEmpty(maxms) && p4 > Integer.valueOf(maxms)) {
                    hashMap.put("message", "所需组员人数不能超过" + maxms + "人!");
                    hashMap.put("opType", "2");
                    return ApiResult.failed(ApiConst.CODE_INNER_ERROR,(String)hashMap.get("message"));
                }
            }
            if (StringUtil.isNotBlank(p7) && p7.length() > 200) {
                hashMap.put("message", "组员要求字数不能超过200!");
                hashMap.put("opType", "2");
                return ApiResult.failed(ApiConst.CODE_INNER_ERROR,(String)hashMap.get("message"));
            }
            if (StringUtil.isNotBlank(p8) && p8.length() > 200) {
                hashMap.put("message", "项目简介字数不能超过200!");
                hashMap.put("opType", "2");
                return ApiResult.failed(ApiConst.CODE_INNER_ERROR,(String)hashMap.get("message"));
            }
            if (StringUtil.isNotBlank(p9) && p9.length() > 500) {
                hashMap.put("message", "团队介绍字数不能超过500!");
                hashMap.put("opType", "2");
                return ApiResult.failed(ApiConst.CODE_INNER_ERROR,(String)hashMap.get("message"));
            }

            String officeId = curUser != null && curUser.getOffice() != null ? curUser.getOffice().getId() : null;
            if (StringUtil.isNotEmpty(team.getId())) {
                int projectCount = teamService.checkTeamIsInProject(team.getId());// 根据teamid查询项目是否正在进行中
                if (projectCount > 0) {
                    hashMap.put("message", "该团队有正在进行的项目或者大赛，不能修改团队信息!");
                    hashMap.put("opType", "1");
                    return ApiResult.failed(ApiConst.CODE_INNER_ERROR,(String)hashMap.get("message"));
                }
                team.setLocalCollege(officeId);
                teamService.editTeam(team);
                hashMap.put("message", "编辑团队信息成功!");
                hashMap.put("opType", "1");
                return ApiResult.success(hashMap);
            } else {
                Long countValid = teamService.countBuildByUserId(curUser);
                if (countValid == null) {
                    countValid = 0L;
                }
                Long teamMax = null;
                if (scv != null && "1".equals(scv.getTeamConf().getMaxOnOff())) {//团队创建数量限制
                    teamMax = Long.valueOf(scv.getTeamConf().getMax());
                    if (countValid >= teamMax) {
                        hashMap.put("message", "你创建的团队已经达到上限，无法继续创建！");
                        hashMap.put("opType", "1");
                        return ApiResult.failed(ApiConst.CODE_INNER_ERROR,(String)hashMap.get("message"));
                    }
                }
                team.setSponsor(curUser.getId());
                team.setLocalCollege(officeId);
                team.setState("0");
                if (team.getEnterpriseTeacherNum() == null) {
                    team.setEnterpriseTeacherNum(0);
                }
                teamService.saveTeam(team, curUser);
                String msg = "";
                if (Team.state3.equals(team.getState())) {
                    msg = "需要等待管理员审核通过后再建设该团队！";
                }
                if (teamMax != null) {
                    hashMap.put("message", "创建团队成功,你还能再创建" + (teamMax - countValid - 1) + "个团队!" + msg);
                } else {
                    hashMap.put("message", "创建团队成功!" + msg);
                }
                return ApiResult.success(hashMap);
            }
        } catch (Exception e) {
            logger.error("团队保存出错", e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,"团队保存出现系统错误");
        }

    }


    @RequestMapping(value = "checkTeamCreateCdn")
    @ResponseBody
    public JSONObject checkTeamCreateCdn() {
        JSONObject js = new JSONObject();
        js.put("ret", 0);
        User curUser = UserUtils.getUser();
        if (StringUtil.isEmpty(curUser.getId())) {
            js.put("msg", "会话已失效，请重新登录");
            return js;
        }
        SysConfigVo scv = SysConfigUtil.getSysConfigVo();
        if (SysUserUtils.checkHasRole(curUser, RoleBizTypeEnum.DS)) {
            js.put("msg", "导师暂时无法创建团队");
            return js;
        }
        if (SysUserUtils.checkInfoPerfect(UserUtils.getUser())) {
            js.put("ret", 2);
            js.put("msg", "个人信息未完善，立即完善个人信息？");
            return js;
        }
        if (scv != null && scv.getTeamConf().getEnterYear() != null) {//入学N年创建团队限制
            Integer enterYear = null;
            try {
                enterYear = Integer.valueOf(scv.getTeamConf().getEnterYear());
            } catch (NumberFormatException e) {
                logger.error(ExceptionUtil.getStackTrace(e));
            }
            if (enterYear != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
                Date date = proStudentExpansionService.getByUserId(curUser.getId()).getEnterdate();
                if (date != null) {
                    Date newDate = new Date();
                    Integer newda = Integer.valueOf(formatter.format(newDate));
                    Integer dateString = Integer.valueOf(formatter.format(date));
                    if (dateString + enterYear > newda) {
                        js.put("msg", dateString + "级学生不能创建团队");
                        return js;
                    }
                }
            }
        }
        Long countValid = teamService.countBuildByUserId(curUser);
        if (countValid == null) {
            countValid = 0L;
        }
        Long teamMax = null;
        if (scv != null && "1".equals(scv.getTeamConf().getMaxOnOff())) {//团队创建数量限制
            teamMax = Long.valueOf(scv.getTeamConf().getMax());
            if (countValid >= teamMax) {
                js.put("msg", "你创建的团队已经达到上限，无法继续创建");
                return js;
            }
        }
        js.put("ret", 1);
        return js;
    }

    /**
     * 查询团队学生成员.
     *
     * @param teamid 团队ID
     * @param proId  项目ID
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "ajaxTeamStudent")
    public ApiTstatus<List<TeamStudentVo>> ajaxTeamStudent(@RequestParam(required = true) String teamid, @RequestParam(required = false) String proId) {
        List<TeamStudentVo> list = projectDeclareService.findTeamStudentByTeamId(teamid, proId);
        if ((list == null) || (list.size() <= 0)) {
            return new ApiTstatus<List<TeamStudentVo>>(true, "查询结果为空！");
        }
        return new ApiTstatus<List<TeamStudentVo>>(true, "查询成功", list);
    }

    /**
     * 查询团队导师成员.
     *
     * @param teamid 团队ID
     * @param proId  项目ID
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "ajaxTeamTeacher")
    public ApiTstatus<List<TeamTeacherVo>> ajaxTeamTeacher(@RequestParam(required = true) String teamid, @RequestParam(required = false) String proId) {
        List<TeamTeacherVo> list = projectDeclareService.findTeamTeacherByTeamId(teamid, proId);
        if ((list == null) || (list.size() <= 0)) {
            return new ApiTstatus<List<TeamTeacherVo>>(true, "查询结果为空！");
        }
        return new ApiTstatus<List<TeamTeacherVo>>(true, "查询成功", list);
    }
}

package com.oseasy.pro.modules.gcontest.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.fileserver.modules.vsftp.service.FtpService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.authorize.service.AuthorizeService;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.config.ProSval.ProEmskey;
import com.oseasy.pro.common.service.CommonService;
import com.oseasy.pro.modules.gcontest.entity.GAuditInfo;
import com.oseasy.pro.modules.gcontest.entity.GContest;
import com.oseasy.pro.modules.gcontest.entity.GContestAward;
import com.oseasy.pro.modules.gcontest.service.GAuditInfoService;
import com.oseasy.pro.modules.gcontest.service.GContestAwardService;
import com.oseasy.pro.modules.gcontest.service.GContestService;
import com.oseasy.pro.modules.gcontest.vo.GContestListVo;
import com.oseasy.pro.modules.gcontest.vo.GContestVo;
import com.oseasy.pro.modules.project.entity.ProjectDeclare;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.sys.modules.sys.entity.SysStudentExpansion;
import com.oseasy.sys.modules.sys.service.SysStudentExpansionService;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.sys.modules.team.service.TeamUserHistoryService;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 大赛信息Controller
 *
 * @author zy
 * @version 2017-03-11
 */
@Controller
@RequestMapping(value = "${frontPath}/gcontest/gContest")
public class FrontGContestController extends BaseController {

    @Autowired
    private TeamService teamService;
    @Autowired
    private ProjectDeclareService projectDeclareService;
    @Autowired
    private UserService userService;
    @Autowired
    private FtpService ftpService;
    @Autowired
    private SysAttachmentService sysAttachmentService;
    @Autowired
    private GContestService gContestService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private GAuditInfoService gAuditInfoService;

    @Autowired
    private SysStudentExpansionService sysStudentExpansionService;

    @Autowired
    GContestAwardService gContestAwardService;
//    @Autowired
//    ScoAllotRatioService scoAllotRatioService;
    @Autowired
    TeamUserHistoryService teamUserHistoryService;
    @Autowired
    AuthorizeService authorizeService;
    @Autowired
    private ProModelService proModelService;

    @ModelAttribute
    public GContest get(@RequestParam(required = false) String id) {
        GContest entity = null;
        if (StringUtil.isNotBlank(id)) {
            entity = gContestService.get(id);
        }
        if (entity == null) {
            entity = new GContest();
        }
        return entity;
    }


    @RequestMapping(value = {"list", ""})
    public String list(GContest gContest, HttpServletRequest request, HttpServletResponse response, Model model) {
        /*Page<GContest> page = gContestService.findPage(new Page<GContest>(request, response), gContest);
		model.addAttribute("page", page);*/
        Map<String, Object> param = new HashMap<String, Object>();
        User user = UserUtils.getUser();
        param.put("userid", user.getId());
        GContestListVo vo = new GContestListVo();
        vo.setUserid(user.getId());
        Page<GContestListVo> page = gContestService.getMyProjectListPlus(new Page<GContestListVo>(request, response), vo);
        model.addAttribute("page", page);
        model.addAttribute("user", user);
        return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "gContestList";
    }

    @RequestMapping(value="getGContestList", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getGContestList(GContest gContest, HttpServletRequest request, HttpServletResponse response){
        try {
            User user = UserUtils.getUser();
            GContestListVo vo = new GContestListVo();
            vo.setUserid(user.getId());
            Page<GContestListVo> page = gContestService.getMyProjectListPlus(new Page<GContestListVo>(request, response), vo);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }


    @ResponseBody
    @RequestMapping(value = "findTeamPerson")
    public List<Map<String, String>> findTeamPerson(@RequestParam(required = true) String id, @RequestParam(required = false) String actywId) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        List<Map<String, String>> list1 = projectDeclareService.findTeamStudent(id);
        List<Map<String, String>> list2 = projectDeclareService.findTeamTeacher(id);
        for (Map<String, String> map : list1) {
            list.add(map);
        }
        for (Map<String, String> map : list2) {
            list.add(map);
        }
        return list;
    }


    @RequestMapping(value = "form")
    public String form(GContest gContest, Model model, HttpServletRequest request) {
        User user = UserUtils.getUser();
        model.addAttribute("user", user);
        model.addAttribute("studentExpansion", sysStudentExpansionService.getByUserId(user.getId()));

        if (StringUtil.isEmpty(gContest.getActywId())) {
            gContest.setActywId(request.getParameter("actywId"));
        }
        if (StringUtil.isNotBlank(gContest.getId())) {
            gContest = gContestService.get(gContest.getId());
            user = userService.findUserById(gContest.getDeclareId());
            SysStudentExpansion sse = new SysStudentExpansion();
            sse.setName(user.getName());
            sse.setEmail(user.getEmail());
            sse.setMobile(user.getMobile());
            if (user.getOffice() != null) {
                sse.setOffice(user.getOffice());
            }
            sse.setCompany(user.getCompany());
            sse.setProfessional(user.getProfessional());
            sse.setNo(user.getNo());
            model.addAttribute("sse", sse);
            model.addAttribute("gContest", gContest);
            model.addAttribute("competitionNumber", gContest.getCompetitionNumber());
            //关联附件
            SysAttachment sysAttachment = new SysAttachment();
            sysAttachment.setUid(gContest.getId());
//			Map<String,String> map=new HashMap<String,String>();
//			map.put("uid", gContest.getId());
//			map.put("type",FileTypeEnum.S2.getValue());
            SysAttachment sa = new SysAttachment();
            sa.setUid(gContest.getId());
            sa.setFileStep(FileStepEnum.S300);
            sa.setType(FileTypeEnum.S2);
            List<SysAttachment> sysAttachments = sysAttachmentService.getFiles(sa);
            //List<Map<String, String>>   sysAttachments=sysAttachmentService.getFileInfo(map);
            model.addAttribute("sysAttachments", sysAttachments);
            //model.addAttribute("studentExpansion", sysStudentExpansionService.getByUserId(user.getId()));
            GContestVo vo = new GContestVo();
            vo.setTeamStudent(projectDeclareService.findTeamStudentFromTUH(gContest.getTeamId(), gContest.getId()));
            vo.setTeamTeacher(projectDeclareService.findTeamTeacherFromTUH(gContest.getTeamId(), gContest.getId()));
            model.addAttribute("gContestVo", vo);
            //model.addAttribute("gContest", gContest);
            //关联团队
			/*Team team=new Team();
			team.setSponsor(user.getId());
			List<Team> teams=	teamService.findList(team);*/
            //model.addAttribute("teams", teams);
            model.addAttribute("sysdate", DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
        } else {
            SysStudentExpansion sse = new SysStudentExpansion();
            sse.setName(user.getName());
            sse.setEmail(user.getEmail());
            sse.setMobile(user.getMobile());
            sse.setCompany(user.getCompany());
            if (user.getOffice() != null) {
                sse.setOffice(user.getOffice());
            }
            sse.setProfessional(user.getProfessional());
            sse.setNo(user.getNo());
            gContest.setDeclareId(user.getId());
            model.addAttribute("gContest", gContest);
            model.addAttribute("sse", sse);
            model.addAttribute("isHave", true);
        }
        //关联团队
        model.addAttribute("teams", projectDeclareService.findTeams(user.getId(), gContest.getTeamId()));
        //关联项目
        ProjectDeclare projectDeclare = new ProjectDeclare();
        projectDeclare.setLeader(user.getId());
        List<ProjectDeclare> projects = projectDeclareService.getCurProjectInfoByLeader(user.getId());
        model.addAttribute("projects", projects);

        return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "gContestForm";
    }

    @RequestMapping(value = "viewForm")
    public String viewForm(GContest gContest, Model model) {
        User user = UserUtils.getUser();
        if (StringUtil.isNotBlank(gContest.getId())) {
            gContest = gContestService.get(gContest.getId());
            user = userService.findUserById(gContest.getDeclareId());
            SysStudentExpansion sse = new SysStudentExpansion();
            sse.setName(user.getName());
            sse.setEmail(user.getEmail());
            sse.setMobile(user.getMobile());
            sse.setCompany(user.getCompany());
            sse.setOffice(user.getOffice());
            sse.setProfessional(user.getProfessional());
            sse.setNo(user.getNo());
            gContest.setDeclareId(user.getId());
            model.addAttribute("sse", sse);
            model.addAttribute("gContest", gContest);
            model.addAttribute("competitionNumber", gContest.getCompetitionNumber());
            if (gContest.getAuditState().equals("2")) {
                model.addAttribute("isHave", true);
            }
        }
        SysAttachment sysAttachment = new SysAttachment();
        sysAttachment.setUid(gContest.getId());
        List<SysAttachment> sysAttachments = sysAttachmentService.findList(sysAttachment);
        model.addAttribute("sysAttachments", sysAttachments);
        GContestVo vo = new GContestVo();
        model.addAttribute("studentExpansion", sysStudentExpansionService.getByUserId(user.getId()));
        vo.setTeamStudent(projectDeclareService.findTeamStudentFromTUH(gContest.getTeamId(), gContest.getId()));
        vo.setTeamTeacher(projectDeclareService.findTeamTeacherFromTUH(gContest.getTeamId(), gContest.getId()));
        model.addAttribute("gContestVo", vo);
        //关联团队
        if (gContest.getTeamId() != null) {
            Team team = teamService.get(gContest.getTeamId());
            model.addAttribute("team", team);
        }
        //关联项目
        ProjectDeclare projectDeclare = new ProjectDeclare();
        projectDeclare.setLeader(user.getId());
        if (gContest.getpId() != null) {
            ProjectDeclare pd = projectDeclareService.get(gContest.getpId());
            if (pd != null) {
                model.addAttribute("relationProject", pd.getName());
            } else {
                ProModel pm = proModelService.get(gContest.getpId());
                model.addAttribute("relationProject", pm.getpName());
            }
        }
        //审核记录
        List<GAuditInfo> collegeinfos = getSortInfo(gContest.getId(), "2");
        model.addAttribute("collegeinfos", collegeinfos);
        List<GAuditInfo> wpinfos = getSortInfo(gContest.getId(), "4");
        model.addAttribute("wpinfos", wpinfos);
        List<GAuditInfo> lyinfos = getSortInfo(gContest.getId(), "5");
        model.addAttribute("lyinfos", lyinfos);
        GContestAward gca = gContestAwardService.getByGid(gContest.getId());
        model.addAttribute("gca", gca);
        return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "gContestDetail";
    }

    @RequestMapping(value = "viewPro")
    public String viewPro(GContest gContest, Model model) {
        User user = UserUtils.getUser();
        if (StringUtil.isNotBlank(gContest.getId())) {
            gContest = gContestService.get(gContest.getId());
            user = userService.findUserById(gContest.getDeclareId());
            SysStudentExpansion sse = new SysStudentExpansion();
            sse.setName(user.getName());
            sse.setEmail(user.getEmail());
            sse.setMobile(user.getMobile());
            sse.setCompany(user.getCompany());
            sse.setOffice(user.getOffice());
            sse.setProfessional(user.getProfessional());
            sse.setNo(user.getNo());
            gContest.setDeclareId(user.getId());
            model.addAttribute("sse", sse);
            model.addAttribute("gContest", gContest);
            model.addAttribute("competitionNumber", gContest.getCompetitionNumber());
            if (gContest.getAuditState().equals("2")) {
                model.addAttribute("isHave", true);
            }
        }
        SysAttachment sysAttachment = new SysAttachment();
        sysAttachment.setUid(gContest.getId());
        List<SysAttachment> sysAttachments = sysAttachmentService.findList(sysAttachment);
        model.addAttribute("sysAttachments", sysAttachments);
        GContestVo vo = new GContestVo();
        model.addAttribute("studentExpansion", sysStudentExpansionService.getByUserId(user.getId()));
        model.addAttribute("teamStudents", projectDeclareService.findTeamStudentFromTUH(gContest.getTeamId(), gContest.getId()));
        model.addAttribute("teamTeachers", projectDeclareService.findTeamTeacherFromTUH(gContest.getTeamId(), gContest.getId()));
//	    vo.setTeamStudent(projectDeclareService.findTeamStudentFromTUH(gContest.getTeamId(),gContest.getId()));
//	    vo.setTeamTeacher(projectDeclareService.findTeamTeacherFromTUH(gContest.getTeamId(),gContest.getId()));
        model.addAttribute("gContestVo", vo);
        //关联团队
        if (gContest.getTeamId() != null) {
            Team team = teamService.get(gContest.getTeamId());
            model.addAttribute("team", team);
        }
        //关联项目
        ProjectDeclare projectDeclare = new ProjectDeclare();
        projectDeclare.setLeader(user.getId());
        if (gContest.getpId() != null) {
            ProjectDeclare pd = projectDeclareService.get(gContest.getpId());
            if (pd != null) {
                model.addAttribute("relationProject", pd.getName());
            } else {
                ProModel pm = proModelService.get(gContest.getpId());
                if (pm != null) {
                    model.addAttribute("relationProject", pm.getpName());
                }
            }
        }
        //审核记录
        List<GAuditInfo> collegeinfos = getSortInfo(gContest.getId(), "2");
        model.addAttribute("collegeinfos", collegeinfos);
        List<GAuditInfo> wpinfos = getSortInfo(gContest.getId(), "4");
        model.addAttribute("wpinfos", wpinfos);
        List<GAuditInfo> lyinfos = getSortInfo(gContest.getId(), "5");
        model.addAttribute("lyinfos", lyinfos);
        GContestAward gca = gContestAwardService.getByGid(gContest.getId());
        model.addAttribute("gca", gca);
        return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "gContestViewPro";
    }

    private List<GAuditInfo> getSortInfo(String gId, String auditStep) {
        GAuditInfo pai = new GAuditInfo();
        pai.setGId(gId);
        pai.setAuditLevel(auditStep);
        List<GAuditInfo> infos = gAuditInfoService.getSortInfo(pai);
        return infos;
    }
	/*@RequestMapping(value = "save")
	public String save(GContest gContest, Model model, HttpServletRequest request,  RedirectAttributes redirectAttributes) {
		gContestService.save(gContest);
		addMessage(redirectAttributes, "保存大赛信息成功");
		return CoreSval.REDIRECT+CoreSval.getFrontPath()+"/gcontest/gContest/?repage";
	}*/

    @RequestMapping(value = "save")
    @ResponseBody
    public JSONObject save(GContest gContest, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        JSONObject js = new JSONObject();
        if (StringUtil.isEmpty(gContest.getYear())) {
            gContest.setYear(commonService.getApplyYear(gContest.getActywId()));
        }
        js = commonService.checkGcontestApplyOnSave(gContest.getId(), gContest.getActywId(), gContest.getTeamId(), gContest.getYear());
        if ("0".equals(js.getString("ret"))) {
            return js;
        }

        User user = UserUtils.getUser();
        gContest.setCreateBy(user);
        gContestService.save(gContest);
        js.put("id", gContest.getId());
        js.put("ret", 1);
        js.put("msg", "保存成功");
        return js;
    }

    //ajax 请求，页面传入snumber（团队学生人数），返回后台的学分配比比例
    @RequestMapping("findRatio")
    @ResponseBody
    public String findRatio(int snumber) {
        Boolean bl = authorizeService.checkMenuByNum(5);//TODO
        //是否授权
//        if (bl) {
//            ScoRatioVo scoRatioVo = new ScoRatioVo();
//            scoRatioVo.setType("0000000125"); //设置查询的学分类型（素质学分）
//            scoRatioVo.setItem("0000000129"); //双创大赛
//            scoRatioVo.setCategory("1"); //互联网+大赛
//            //		scoRatioVo.setSubdivision("");
//            scoRatioVo.setNumber(snumber);
//            ScoRatioVo ratioResult = scoAllotRatioService.findRatio(scoRatioVo);
//            if (ratioResult != null) {
//                return ratioResult.getRatio();
//            }
//        }
        return "";
    }

    @RequestMapping(value = "delFile")
    @ResponseBody
    public JSONObject delFile(HttpServletRequest request) {
        JSONObject js = new JSONObject();
        js.put("ret", 1);
        js.put("msg", "删除成功");
        String arrUrl = request.getParameter("arrUrl");
        String id = request.getParameter("id");
        try {
            if (id != null && !"null".equals(id)) sysAttachmentService.delete(new SysAttachment(id));
            ftpService.del(arrUrl);
        } catch (Exception e) {
            js.put("ret", 0);
            js.put("msg", "删除失败");
        }
        return js;
    }

    @RequestMapping(value = {"viewList"})
    public String viewList(GContest gContest, HttpServletRequest request, HttpServletResponse response, Model model) {
//        User user = UserUtils.getUser();
//        //得到有项目的大赛类型
//        List<Map<String, String>> gcNameList = gContestService.getInGcontestNameList(user.getId());
//        model.addAttribute("gcNameList", gcNameList);
        //得到当前大赛有多少正在进行


        return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "front/calender";
        //return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "front/gcontestCalendar";
    }

    @RequestMapping(value = "getGcontestTimeIndexData")
    @ResponseBody
    public JSONObject viewLit(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        JSONObject js = new JSONObject();
        GContest gContest = new GContest();
        User user = UserUtils.getUser();
        List<GContest> glist = gContestService.getGcontestInfo(user.getId());
        if (glist.size() > 0) {
            gContest = glist.get(0);
            gContest.setId(gContest.getId());
            gContest = gContestService.get(gContest);
        } else {
            gContest = gContestService.getLastGcontestInfo(user.getId());
            if (gContest != null) {
                gContest.setId(gContest.getId());
                gContest = gContestService.get(gContest);
            }
        }
        //后台做判断
        js = gContestService.getListData(gContest);
        return js;
    }

    @RequestMapping(value = "submit")
    @ResponseBody
    public JSONObject submit(GContest gContest, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        JSONObject js = new JSONObject();
        if (StringUtil.isEmpty(gContest.getYear())) {
            gContest.setYear(commonService.getApplyYear(gContest.getActywId()));
        }
        js = commonService.checkGcontestApplyOnSubmit(gContest.getId(), gContest.getActywId(), gContest.getTeamId(), gContest.getYear());
        if ("0".equals(js.getString("ret"))) {
            return js;
        }
        if (gContest.getpName() != null) {
            if (gContest.getId() != null) {
                List<GContest> gname = gContestService.getGcontestByNameNoId(gContest.getId(), gContest.getpName());
                if (gname != null && gname.size() != 0) {
                    js.put("ret", 0);
                    js.put("msg", "提交失败，存在名字相同项目");
                    return js;
                }
            } else {
                List<GContest> gname = gContestService.getGcontestByName(gContest.getpName());
                if (gname != null && gname.size() != 0) {
                    js.put("ret", 0);
                    js.put("msg", "提交失败，存在名字相同项目");
                    return js;
                }
            }
        }
        int res = gContestService.submit(gContest);
        if (res == 0) {
            js.put("ret", 0);
            js.put("msg", "提交失败，该学院无学院专家，请联系管理员");
            return js;
        }
        if (res == 2) {
            js.put("ret", 0);
            js.put("msg", "未设置编号规则，请联系管理员设置编号规则!");
            return js;
        }
        sysAttachmentService.saveByVo(gContest.getAttachMentEntity(), gContest.getId(), FileTypeEnum.S2, FileStepEnum.S300);
        js.put("ret", 1);
        js.put("msg", "恭喜您，大赛申报成功！");

        return js;
    }


    @RequestMapping(value = "delete")
    public String delete(GContest gContest, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String ftb = request.getParameter("ftb");
        gContestService.delete(gContest, ftb);
        addMessage(redirectAttributes, "删除大赛信息成功");
        return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/gcontest/gContest/?repage";
    }

    @RequestMapping("onGcontestApply")
    @ResponseBody
    public JSONObject onGcontestApply(String actywId, String pid) {
        GContest p = null;
        ProModel m = null;
        if (StringUtil.isNotEmpty(pid)) {
            p = gContestService.get(pid);
        }
        if (p == null) {
            m = proModelService.get(pid);
        }
        String year = null;
        ;
        if (p != null) {
            year = p.getYear();
        } else if (m != null) {
            year = m.getYear();
        } else {
            year = commonService.getApplyYear(actywId);
        }
        return commonService.onGcontestApply(actywId, pid, year);
    }

    @RequestMapping("checkGcontestTeam")
    @ResponseBody
    public JSONObject checkGcontestTeam(String proid, String actywId, String teamid) {
        GContest p = null;
        ProModel m = null;
        if (StringUtil.isNotEmpty(proid)) {
            p = gContestService.get(proid);
        }
        if (p == null) {
            m = proModelService.get(proid);
        }
        String year = null;
        ;
        if (p != null) {
            year = p.getYear();
        } else if (m != null) {
            year = m.getYear();
        } else {
            year = commonService.getApplyYear(actywId);
        }
        return commonService.checkGcontestTeam(proid, actywId, teamid, year);
    }

}
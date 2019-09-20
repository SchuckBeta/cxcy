package com.oseasy.pro.modules.promodel.web.front;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.oseasy.pro.modules.interactive.service.SysViewsService;
import com.oseasy.sys.modules.sys.service.StudentExpansionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.OfficeService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.pro.modules.project.vo.ProjectExpVo;
import com.oseasy.pro.modules.promodel.entity.GContestUndergo;
import com.oseasy.pro.modules.promodel.service.ProStudentExpansionService;
import com.oseasy.sys.common.config.SysSval;
import com.oseasy.sys.common.config.SysSval.SysEmskey;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 学生信息表Controller
 *
 * @author zy
 * @version 2017-03-27
 */
@Controller
@RequestMapping(value = "${frontPath}/sys/frontStudentExpansion")
public class FrontProStudentExpansionController extends BaseController {
    @Autowired
    private TeamService  teamService;
    @Autowired
    private OfficeService  officeService;
    @Autowired
    private ProStudentExpansionService proStudentExpansionService;
    @Autowired
    private StudentExpansionService studentExpansionService;


    @ModelAttribute
    public StudentExpansion get(@RequestParam(required = false) String id, Model model) {
        StudentExpansion entity = null;
        if (StringUtil.isNotBlank(id)) {
            entity = studentExpansionService.get(id);
        }
        if (entity == null) {
            entity = new StudentExpansion();
        }
        return entity;
    }

    @RequestMapping(value = "form")
    public String form(StudentExpansion studentExpansion, Model model,HttpServletRequest request) {
//        List<ProjectExpVo> projectExpVo = proStudentExpansionService.findProjectByStudentId(studentExpansion.getUser().getId());//查询项目经历
//        List<GContestUndergo> gContest = proStudentExpansionService.findGContestByStudentId(studentExpansion.getUser().getId()); //查询大赛经历
//      model.addAttribute("projectExpVo", projectExpVo);
//      model.addAttribute("gContestExpVo", gContest);
        model.addAttribute("cuser", studentExpansion.getUser().getId());
        String mobile=studentExpansion.getUser().getMobile();
        String email=studentExpansion.getUser().getEmail();
        //判断studentExpansion与当前登录人是否在一个团队,如果不在一个团队，则电话号码隐藏中间四位
        String studentId =  studentExpansion.getUser().getId();
        String userId = UserUtils.getUser().getId();
        if (!teamService.findTeamByUserId(userId,studentId)) {
            if(StringUtil.isNotEmpty(mobile)) {
                mobile = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            }
            if(StringUtil.isNotEmpty(email)) {
                email = email.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+([.-]?\\w+)*\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4");
            }
        }
        model.addAttribute("email", email);
        model.addAttribute("mobile", mobile);
//      if (StringUtil.isEmpty(studentExpansion.getUser().getViews())) {
//          studentExpansion.getUser().setViews("0");
//      }
//      if (StringUtil.isEmpty(studentExpansion.getUser().getLikes())) {
//          studentExpansion.getUser().setLikes("0");
//      }
        /*记录浏览量*/
        User user= UserUtils.getUser();
        if (user!=null&&StringUtil.isNotEmpty(user.getId())&&!user.getId().equals(studentExpansion.getUser().getId())) {
            SysViewsService.updateViews(studentExpansion.getUser().getId(), request,CacheUtils.USER_VIEWS_QUEUE);
        }
        /*记录浏览量*/
        /*查询谁看过它*/
//      model.addAttribute("visitors", sysViewsService.getVisitors(studentExpansion.getUser().getId()));
        model.addAttribute("currentUser", UserUtils.getUser());
        return SysSval.path.vms(SysEmskey.SYS.k()) + "front/frontStudentExpansionForm";
    }

    /**
     * 获取当前登录 学生基本信息.
     * @return
     */
    @ResponseBody
    @RequestMapping(value="ajaxGetUserInfoById")
    public ApiTstatus<StudentExpansion> ajaxGetUserInfoById(String userId) {
        if(StringUtil.isEmpty(userId)){
            userId = UserUtils.getUser().getId();
        }
        StudentExpansion studentExpansion = proStudentExpansionService.getByUserId(String.valueOf(userId));//查出用户基本信息
        if (studentExpansion!=null) {
            studentExpansion.setUser(UserUtils.get(studentExpansion.getUser().getId()));
            if (studentExpansion.getUser() !=null) {
                if (StringUtil.isEmpty(studentExpansion.getUser().getSex())) {
                    studentExpansion.getUser().setSex(Const.YES);
                }
            }

            if (studentExpansion.getUser()!=null && studentExpansion.getUser().getOffice()!=null&&StringUtil.isNotEmpty(studentExpansion.getUser().getOffice().getId())) {
                Office office = officeService.get(studentExpansion.getUser().getOffice().getId());
                if(office != null){
                    studentExpansion.getUser().setOffice(office);
                }
            }
        }
        return new ApiTstatus<StudentExpansion>(studentExpansion);
    }

    /**
     * 获取当前登录 学生基本信息，查询大赛经历.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value="ajaxGetUserProjectById")
    public ApiTstatus<List<ProjectExpVo>> ajaxGetUserProjectById(String userId) {
        if(StringUtil.isEmpty(userId)){
            userId = UserUtils.getUser().getId();
        }
        List<ProjectExpVo> projectExpVos = proStudentExpansionService.findProjectByStudentId(userId);
        if(StringUtil.checkEmpty(projectExpVos)){
            return new ApiTstatus<List<ProjectExpVo>>(true, "暂无记录");
        }
        return new ApiTstatus<List<ProjectExpVo>>(projectExpVos);
    }

    /**
     * 获取当前登录 学生基本信息，查询项目经历.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value="ajaxGetUserGContestById")
    public ApiTstatus<List<GContestUndergo>> ajaxGetUserGContestById(String userId) {
        if(StringUtil.isEmpty(userId)){
            userId = UserUtils.getUser().getId();
        }
        List<GContestUndergo> gcontestUndergos = proStudentExpansionService.findGContestByStudentId(userId);
        if(StringUtil.checkEmpty(gcontestUndergos)){
            return new ApiTstatus<List<GContestUndergo>>(true, "暂无记录");
        }
        return new ApiTstatus<List<GContestUndergo>>(gcontestUndergos);
    }
}
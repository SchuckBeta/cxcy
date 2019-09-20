package com.oseasy.sys.modules.sys.web.front;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.CoreEmskey;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.persistence.PageMap;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.sys.common.utils.SysUserUtils;
import com.oseasy.sys.modules.sys.service.SysSystemService;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.util.common.utils.StringUtil;

@Controller
@RequestMapping(value = "${frontPath}/sys/user")
public class FrontSysUserController extends BaseController {
    @Autowired
    CoreService coreService;
    @Autowired
    SystemService systemService;
    @Autowired
    SysSystemService sysSystemService;
    @Autowired
    private TeamService teamService;


    //查询学校成员列表 by 王清腾
    @RequestMapping(value = "/ajaxUserListTree", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiTstatus<HashMap<String, Object>> ajaxUserListTree(User user, HttpServletRequest request, HttpServletResponse response) {
        ApiTstatus<HashMap<String, Object>> actYwRstatus = new ApiTstatus<HashMap<String, Object>>(false, "查询失败");
        String userName = request.getParameter("userName");
        String teacherType = request.getParameter("teacherType");
        String userType = request.getParameter("userType");
        String allTeacher = request.getParameter("allTeacher");
        String grade = request.getParameter("grade");
        String professionId = request.getParameter("professionId");
        HashMap<String, Object> userListTreeMap = new HashMap<String, Object>();

        if (StringUtil.isNotBlank(userName)) {
            user.setName(userName);
        }

        if (StringUtil.isNotBlank(teacherType)) {
            user.setTeacherType(teacherType);
        }

        if ("1".equals(allTeacher)) {
            user.setTeacherType(null);
        }
        if (StringUtil.isNotBlank(grade) && "3".equals(grade)) {
            user.setProfessional(professionId);
        }

        userListTreeMap.put("userName", userName);
        userListTreeMap.put("teacherType", teacherType);

        if (StringUtil.isNotEmpty(userType)) {
            Page<User> page = null;
            user.setUserType(userType);
            switch ((userType)) {
                case "1":
                    page = sysSystemService.findListTreeByStudent(new Page<User>(request, response), user);
                    break;
                case "2":
                    page = sysSystemService.findListTreeByTeacher(new Page<User>(request, response), user);
                    break;
                default:
                    page = sysSystemService.findListTreeByUser(new Page<User>(request, response), user);
                    break;
            }
            Map<String, Object> membersMap = new PageMap().getPageMap(page);
            userListTreeMap.putAll(membersMap);
            actYwRstatus.setMsg("查询成功");
            actYwRstatus.setStatus(true);
            actYwRstatus.setDatas(userListTreeMap);
            return actYwRstatus;
        }
        return actYwRstatus;
    }

    @RequestMapping("userListTreePublish")
    public String userListTreePublish(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
        String userType = request.getParameter("userType");
        Page<User> page = null;
        if (StringUtil.isNotEmpty(userType)) {
            user.setUserType(userType);
            if ((userType).equals("1")) {
                page = sysSystemService.findListTreeByStudent(new Page<User>(request, response), user);
            } else if ((userType).equals("2")) {
                page = sysSystemService.findListTreeByTeacher(new Page<User>(request, response), user);
            } else {
                page = sysSystemService.findListTreeByUser(new Page<User>(request, response), user);
            }
        }

        if (page!=null) {
            List<User> userList = page.getList();
            if (userList!=null&&userList.size()>0) {
                for(User usertmp:userList) {
                    List<Role> roleList = coreService.findListByUserId(usertmp.getId());
                    usertmp.setRoleList(roleList);
                }
            }
        }

        List<Role>  roleList = systemService.findAllRole();

        model.addAttribute("roleList",roleList);

        model.addAttribute("page", page);
        model.addAttribute("userType", userType);
        //  return CoreSval.path.vms(CoreEmskey.SYS.k()) + "userList";
        return CoreSval.path.vms(CoreEmskey.SYS.k()) + "userListTreePublish";
    }

    @RequestMapping("userListTree")
    public String userListTree(User user, String grade, String professionId,String allTeacher,HttpServletRequest request, HttpServletResponse response, Model model) {
        String userType = request.getParameter("userType");
        String teacherType = request.getParameter("teacherType");
        String userName = request.getParameter("userName");
        if (StringUtil.isNotBlank(userName)) {
            user.setName(userName);
            model.addAttribute("userName", userName);
        }
        if (StringUtil.isNotBlank(teacherType)) {
            user.setTeacherType(teacherType);
            model.addAttribute("teacherType", teacherType);
        }
        if ("1".equals(allTeacher)) {
            user.setTeacherType(null);
        }
        if (StringUtil.isNotBlank(grade) && "3".equals(grade)) {
            user.setProfessional(professionId);
        }

    Page<User> page = null;
    if (StringUtil.isNotEmpty(userType)) {
      user.setUserType(userType);

      if ((userType).equals("1")) {
        page = sysSystemService.findListTreeByStudent(new Page<User>(request, response), user);
      }else if ((userType).equals("2")) {
        page = sysSystemService.findListTreeByTeacher(new Page<User>(request, response), user);
      }else{
        page = sysSystemService.findListTreeByUser(new Page<User>(request, response), user);
      }
    }

        model.addAttribute("page", page);
        model.addAttribute("userType", userType);
        return CoreSval.path.vms(CoreEmskey.SYS.k()) + "userListTree";
    }

	@ResponseBody
	@RequestMapping(value = "ifTeamNameExist")
	public String ifTeamNameExist(String name,String teamId) {
		logger.info("name:"+name);
		logger.info("teamId:"+teamId);
		List<Team> teamList  = teamService.selectTeamByName(name);
		if (teamList !=null && teamList.size()>0) {
			if (StringUtil.isNotBlank(teamId)) {
				for(Team team:teamList){
					if (teamId.equals(team.getId())) {
						return  "true";
					}
				}
			}
			return "false";
		}
		return "true";
	}

	   @ResponseBody
	    @RequestMapping(value="checkUserInfoPerfect")
	    public boolean checkUserInfoPerfect() {
	        if (SysUserUtils.checkInfoPerfect(UserUtils.getUser())) {
	            return true;
	        }else{
	            return false;
	        }
	    }

	    /**
	     * 修改密码 addBy 王清腾
	     * @return
	     */
	    @RequestMapping(value="ajaxUpdatePassWord/{id}",  method = RequestMethod.GET, produces = "application/json" )
	    @ResponseBody
	    public ApiTstatus<Object> ajaxUpdatePassWord(@PathVariable String id, String oldPassword, String newPassword) {
	        ApiTstatus<Object> actYwRstatus = new ApiTstatus<Object>(false, "修改密码失败，旧密码错误");
	        User user = UserUtils.getUser();
	        if (StringUtil.isNotBlank(oldPassword) && StringUtil.isNotBlank(newPassword)) {
	            if (CoreUtils.validatePassword(oldPassword, user.getPassword())) {
	                if (oldPassword.equals(newPassword)) {
	                    actYwRstatus.setStatus(false);
	                    actYwRstatus.setMsg("修改密码失败，新密码不能与原密码一致");
	                    return actYwRstatus;
	                }
	                coreService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
	                if (SysUserUtils.checkInfoPerfect(user)) {
	                    actYwRstatus.setStatus(true);
	                    actYwRstatus.setMsg("修改密码成功");
	                    actYwRstatus.setDatas(user);
	                }else{
	                    actYwRstatus.setDatas(user);
	                    actYwRstatus.setStatus(true);
	                    actYwRstatus.setMsg("修改密码成功");
	                }
	                return actYwRstatus;
	            }
	        }
	        return actYwRstatus;
	    }
}

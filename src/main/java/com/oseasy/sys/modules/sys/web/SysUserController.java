/**
 *
 */
package com.oseasy.sys.modules.sys.web;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.CoreEmskey;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.persistence.PageMap;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.enums.RoleBizTypeEnum;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.service.OfficeService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.sys.common.config.SysSval;
import com.oseasy.sys.common.utils.SysUserUtils;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.service.BackTeacherExpansionService;
import com.oseasy.sys.modules.sys.service.SysSystemService;
import com.oseasy.sys.modules.team.dao.TeamDao;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.service.TeamUserHistoryService;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 用户Controller
 *
 * @version 2013-8-29
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user")
public class SysUserController extends BaseController {
    @Autowired
    private SysSystemService sysSystemService;
    @Autowired
    private CoreService coreService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private UserService userService;
    @Autowired
    private BackTeacherExpansionService backTeacherExpansionService;
    @Autowired
    private TeamUserHistoryService teamUserHistoryService;
    @Autowired
    private TeamDao teamDao;

    @ModelAttribute
    public User get(@RequestParam(required = false) String id) {
        if (StringUtil.isNotBlank(id)) {
            User user = sysSystemService.getUser(id);
            if (CoreUtils.checkHasRole(user, RoleBizTypeEnum.DS.getValue())) {
                String teacherType = sysSystemService.getTeacherTypeByUserId(id);
                user.setTeacherType(teacherType);
            }
            return user;
        } else {
            return new User();
        }
    }



    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = "form")
    public String form(User user, Model model, HttpServletRequest request) {
        if (user.getCompany() == null || user.getCompany().getId() == null) {
            user.setCompany(UserUtils.getUser().getCompany());
        }
        model.addAttribute("user", user);
        model.addAttribute("cuser", UserUtils.getUser());
        List<Role> rs = sysSystemService.findAllRole();
        model.addAttribute("allRoles", rs);
        if (rs != null & rs.size() > 0) {
            JSONObject js = new JSONObject();
            for (Role r : rs) {
                js.put(r.getId(), r.getBizType());
            }
            model.addAttribute("rolesjs", js);
        }

        List<Dict> dictList = DictUtils.getDictList(SysSval.DICT_TECHNOLOGY_FIELD);
        model.addAttribute("allDomains", dictList);
        String secondName = request.getParameter("secondName");
        if (StringUtil.isNotEmpty(secondName)) {
            model.addAttribute("secondName", secondName);
        }
        return CoreSval.path.vms(CoreEmskey.SYS.k()) + "userForm";
    }

    @RequestMapping(value="getUserList", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getUserList(User user, HttpServletRequest request, HttpServletResponse response, Model model){
        try {
            Page<User> page = sysSystemService.findPage(new Page<User>(request, response), user);
            if (page != null) {
                Role rteacher = coreService.getByRtype(CoreSval.Rtype.TEACHER.getKey());
                List<User> userList = page.getList();
                if (StringUtil.checkNotEmpty(userList)) {
                    List<Role> roles = coreService.findListByUserIds(StringUtil.sqlInByListIdss(userList));
                    for (User usertmp : userList) {
                        List<Role> curroles = Lists.newArrayList();
                        for (Role currole : roles) {
                            if((usertmp.getId()).equals(currole.getUser().getId())){
                                curroles.add(currole);
                            }
                        }
                        usertmp.setRoleList(curroles);

                        for(Role role: curroles){
                            //角色含有导师信息
                            if((rteacher != null) && (role.getId()).equals(rteacher.getId())){
                                BackTeacherExpansion backTeacherExpansion=backTeacherExpansionService.getByUserId(usertmp.getId());
                                if(backTeacherExpansion!=null && StringUtil.isNotEmpty(backTeacherExpansion.getTeachertype())){
                                    usertmp.setTeacherType(backTeacherExpansion.getTeachertype());
                                }
                            }

                            if(StringUtil.isEmpty(usertmp.getAuth())){
                                usertmp.setAuth(role.getRtype());
                            }else if(Integer.valueOf(usertmp.getAuth()) > Integer.valueOf(role.getRtype())){
                                usertmp.setAuth(role.getRtype());
                            }
                        }
                    }
                }
            }

            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value = "save")
    public String save(User user, HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
        String[] str = request.getParameterValues("domainIdList");
        if (str == null) {
            user.setDomainIdList(null);
            user.setDomain(null);
        }
        String oldLoginName = UserUtils.getUser().getLoginName();
        if (CoreSval.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return CoreSval.REDIRECT + adminPath + "/sys/user/list?repage";
        }
        // 修正引用赋值问题，不知道为何，Company和Office引用的一个实例地址，修改了一个，另外一个跟着修改。
        // user.setCompany(new Office(request.getParameter("company.id")));
        user.setOffice(new Office(request.getParameter("office.id")));
        String companyId = officeService.selelctParentId(user.getOffice().getId());
        user.setCompany(new Office());
        user.getCompany().setId((StringUtil.isNotEmpty(companyId)) ? companyId : CoreIds.NCE_SYS_OFFICE_TOP.getId());

        // 如果新密码为空，则不更换密码
        if (StringUtil.isNotBlank(user.getNewPassword())) {
            user.setPassword(CoreUtils.entryptPassword(user.getNewPassword()));
        }
        if (!beanValidator(model, user)) {
            return form(user, model, request);
        }
        if (StringUtil.isNotEmpty(user.getId())) {// 修改时有加入的团队
            List<Team> tel = teamDao.findTeamListByUserId(user.getId());
            User old = UserUtils.get(user.getId());
            if (old != null && StringUtil.isNotEmpty(old.getId())) {
                String checkRole = SysUserUtils.checkRoleChange(user, old);
                if (tel != null && tel.size() > 0 && checkRole != null) {// 用户类型变化了
                    addMessage(model, "保存失败，该用户已加入团队，" + checkRole);
                    return form(user, model, request);
                }
            }
        }
        if (StringUtil.isNotEmpty(user.getId()) && teamUserHistoryService.getBuildingCountByUserId(user.getId()) > 0) {// 修改时有正在进行的项目大赛
            User old = UserUtils.get(user.getId());
            if (old != null && StringUtil.isNotEmpty(old.getId())) {
                String checkRole = SysUserUtils.checkRoleChange(user, old);
                if (checkRole != null) {// 用户类型变化了
                    addMessage(model, "保存失败，该用户有正在进行的项目或大赛，" + checkRole);
                    return form(user, model, request);
                } else if (CoreUtils.checkHasRole(old, RoleBizTypeEnum.DS.getValue())) {// 导师类型
                    BackTeacherExpansion bte = backTeacherExpansionService.getByUserId(old.getId());
                    if (bte != null && bte.getTeachertype() != null
                            && !bte.getTeachertype().equals(user.getTeacherType())) {// 导师类型的用户导师来源发生变化
                        addMessage(model, "保存失败，该用户有正在进行的项目或大赛，不能修改导师来源");
                        return form(user, model, request);
                    }
                }
            }
        }

        if (!"true".equals(checkLoginName(user.getLoginName(), user.getId()))) {
            addMessage(model, "保存用户'" + user.getLoginName() + "'失败，登录名已存在");
            return form(user, model, request);
        }
        // 角色数据有效性验证，过滤不在授权内的角色
        List<Role> roleList = Lists.newArrayList();
        List<String> roleIdList = user.getRoleIdList();
        for (Role r : sysSystemService.findAllRole()) {
            if (roleIdList.contains(r.getId())) {
                roleList.add(r);
            }
        }
        user.setRoleList(roleList);

        if (roleList == null || roleList.size() == 0) {
            addMessage(model, "保存失败，未选择角色");
            return form(user, model, request);
        }
        boolean hasStu = false;//是否有学生角色
        boolean hasTea = false;//是否有导师角色
        for (Role r : roleList) {
            if (StringUtil.isEmpty(r.getBizType())) {
                r.setBizType(CoreUtils.getRoleBizType(r.getId()));
            }
            if (RoleBizTypeEnum.XS.getValue().equals(r.getBizType())) {
                hasStu = true;
            }
            if (RoleBizTypeEnum.DS.getValue().equals(r.getBizType())) {
                hasTea = true;
            }
        }
        if (hasStu && hasTea) {
            addMessage(model, "保存失败，不能同时选择学生和导师角色");
            return form(user, model, request);
        }
        if (hasStu) {
            user.setUserType(RoleBizTypeEnum.XS.getValue());
        } else if (hasTea) {
            user.setUserType(RoleBizTypeEnum.DS.getValue());
        } else {
            user.setUserType(null);
        }
        // 保存用户信息
        // logger.info("============user.domain:"+user.getDomain());

        String returnMessage = sysSystemService.saveUser(user);
        if ("1".equals(returnMessage)) {
            addMessage(redirectAttributes, "添加用户成功！初始密码设置为：123456");

        } else if ("2".equals(returnMessage)) {
            addMessage(redirectAttributes, "修改用户成功！");
        }
        // 清除当前用户缓存
        if (user.getLoginName().equals(UserUtils.getUser().getLoginName())) {
            UserUtils.clearCache();
            // CoreUtils.getCacheMap().clear();
        }
        if (user.getId().equals(UserUtils.getUser().getId()) && !user.getLoginName().equals(oldLoginName)) {
//            CoreUtils.getSubject().logout();
            CoreUtils.logout(CoreUtils.getSubject(), request, response);
        }
        // addMessage(redirectAttributes, "保存用户'" + user.getLoginName() +
        // "'成功");
        return CoreSval.REDIRECT + adminPath + "/sys/user/list?repage";
    }

    /**
     * 检查loginName 登录名不能与其他人的登录名相同，不能与其他人的no相同
     *
     * @param loginName
     * @param userid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "checkLoginName")
    public Boolean checkLoginName(String loginName, String userid) {
        return userService.getByLoginNameOrNo(loginName, userid) == null;
    }

    @RequestMapping(value = "ajaxSaveUser", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult ajaxSaveUser(User user, HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
        try {
                HashMap<String, Object> hashMap = new HashMap<>();
        String[] str = request.getParameterValues("domainIdList");
        if (str == null) {
            user.setDomainIdList(null);
            user.setDomain(null);
        }
        String oldLoginName = UserUtils.getUser().getLoginName();
        if (CoreSval.isDemoMode()) {
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+"演示模式，不允许操作！");

        }
        // 修正引用赋值问题，不知道为何，Company和Office引用的一个实例地址，修改了一个，另外一个跟着修改。
        // user.setCompany(new Office(request.getParameter("company.id")));
            String officeId = request.getParameter("officeId");
            if(StringUtil.isNotEmpty(officeId)){
                user.setOffice(officeService.get(request.getParameter("officeId")));
            }else {
                user.setOffice(new Office());
            }
            String companyId = officeService.selelctParentId(user.getOffice().getId());
            user.setCompany(new Office());
            user.getCompany().setId((StringUtil.isNotEmpty(companyId)) ? companyId : CoreIds.NCE_SYS_OFFICE_TOP.getId());


        // 如果新密码为空，则不更换密码
        if (StringUtil.isNotBlank(user.getNewPassword())) {
            user.setPassword(CoreUtils.entryptPassword(user.getNewPassword()));
        }
        if (!beanValidator(model, user)) {
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+"数据格式不对");

        }
        if (StringUtil.isNotEmpty(user.getId())) {// 修改时有加入的团队
            List<Team> tel = teamDao.findTeamListByUserId(user.getId());
            User old = UserUtils.get(user.getId());
            if (old != null && StringUtil.isNotEmpty(old.getId())) {
                String checkRole = SysUserUtils.checkRoleChange(user, old);
                if (tel != null && tel.size() > 0 && checkRole != null) {// 用户类型变化了
                    addMessage(model, "保存失败，该用户已加入团队，" + checkRole);
                    return ApiResult.failed(ApiConst.CODE_INNER_ERROR,"保存失败，该用户已加入团队，" + checkRole);

                }
            }
        }
        if (StringUtil.isNotEmpty(user.getId()) && teamUserHistoryService.getBuildingCountByUserId(user.getId()) > 0) {// 修改时有正在进行的项目大赛
            User old = UserUtils.get(user.getId());
            if (old != null && StringUtil.isNotEmpty(old.getId())) {
                String checkRole = SysUserUtils.checkRoleChange(user, old);
                if (checkRole != null) {// 用户类型变化了
                    return ApiResult.failed(ApiConst.CODE_INNER_ERROR,"保存失败，该用户有正在进行的项目或大赛，" + checkRole);

                } else if (CoreUtils.checkHasRole(old, RoleBizTypeEnum.DS.getValue())) {// 导师类型
                    BackTeacherExpansion bte = backTeacherExpansionService.getByUserId(old.getId());
                    if (bte != null && bte.getTeachertype() != null
                            && !bte.getTeachertype().equals(user.getTeacherType())) {// 导师类型的用户导师来源发生变化
                        return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+"保存失败，该用户有正在进行的项目或大赛，不能修改导师来源");
                    }
                }
            }
        }

//        if (!equals(checkLoginName(user.getLoginName(), user.getId()))) {
//            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+"保存用户'" + user.getLoginName() + "'失败，登录名已存在");
//        }
        // 角色数据有效性验证，过滤不在授权内的角色
        List<Role> roleList = Lists.newArrayList();
        List<String> roleIdList = user.getRoleIdList();
        for (Role r : sysSystemService.findAllRole()) {
            if (roleIdList.contains(r.getId())) {
                roleList.add(r);
            }
        }
        user.setRoleList(roleList);

        if (roleList == null || roleList.size() == 0) {
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,"保存失败，未选择角色");
        }
        boolean hasStu = false;//是否有学生角色
        boolean hasTea = false;//是否有导师角色
        boolean hasTeaOrExp = false;//是否有专家角色
        for (Role r : roleList) {
            if (StringUtil.isEmpty(r.getBizType())) {
                r.setBizType(CoreUtils.getRoleBizType(r.getId()));
            }
            if (RoleBizTypeEnum.XS.getValue().equals(r.getBizType()) || (CoreSval.Rtype.STUDENT.getKey()).equals(r.getRtype())) {
                hasStu = true;
            }
            if (RoleBizTypeEnum.DS.getValue().equals(r.getBizType()) || (CoreSval.Rtype.TEACHER.getKey()).equals(r.getRtype())) {
                hasTea = true;
            }
            if ((CoreSval.Rtype.EXPORT.getKey()).equals(r.getRtype())) {
                hasTea = true;
            }
        }
        if (hasStu && hasTea) {
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+"保存失败，不能同时选择学生、导师角色");
        }
        if (hasStu && hasTeaOrExp) {
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+"保存失败，不能同时选择学生、专家角色");
        }
        if (hasTeaOrExp && hasTea) {
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+"保存失败，不能同时选择导师、专家角色");
        }
        if (hasStu) {
            user.setUserType(RoleBizTypeEnum.XS.getValue());
        } else if (hasTea) {
            user.setUserType(RoleBizTypeEnum.DS.getValue());
        } else if (hasTeaOrExp) {
            user.setUserType(RoleBizTypeEnum.XXZJ.getValue());
        } else {
            user.setUserType(null);
        }
        // 保存用户信息
        // logger.info("============user.domain:"+user.getDomain());

        String returnMessage = sysSystemService.saveUser(user);
        String msg ="";
        if ("1".equals(returnMessage)) {
            //addMessage(redirectAttributes, "添加用户成功！初始密码设置为：123456");
            msg="添加用户成功！初始密码设置为：123456";

        } else if ("2".equals(returnMessage)) {
            //addMessage(redirectAttributes, "修改用户成功！");
            msg="修改用户成功！";
        }
        // 清除当前用户缓存
        if (user.getLoginName().equals(UserUtils.getUser().getLoginName())) {
            UserUtils.clearCache();
            CoreUtils.removeUserAll(user);
            // UserUtils.getCacheMap().clear();
        }
        if (user.getId().equals(UserUtils.getUser().getId()) && !user.getLoginName().equals(oldLoginName)) {
//            CoreUtils.getSubject().logout();
            CoreUtils.logout(CoreUtils.getSubject(), request, response);
        }
        return  ApiResult.success(hashMap,msg);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }


    @RequestMapping("backUserListTree")
    public String backUserListTree(User user, String grade, String professionId, String allTeacher,
                                   HttpServletRequest request, HttpServletResponse response, Model model) {
        String userType = request.getParameter("userType");
        String teacherType = request.getParameter("teacherType");
        String userName = request.getParameter("userName");
        String ids = request.getParameter("ids");
        if (StringUtil.isNotBlank(userName)) {
            user.setIds(Arrays.asList(StringUtil.split(ids, StringUtil.DOTH)));
            model.addAttribute("ids", ids);
        }
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
            } else if ((userType).equals("2")) {
                page = sysSystemService.findListTreeByTeacher(new Page<User>(request, response), user);
            } else {
                page = sysSystemService.findListTreeByUser(new Page<User>(request, response), user);
            }
        }

        model.addAttribute("page", page);
        model.addAttribute("userType", userType);
        return CoreSval.path.vms(CoreEmskey.SYS.k()) + "backUserListTree";
    }



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

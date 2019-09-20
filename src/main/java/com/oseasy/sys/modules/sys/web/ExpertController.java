package com.oseasy.sys.modules.sys.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.pcore.common.persistence.UserEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CorePages;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.service.OfficeService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.sys.common.config.SysIds;
import com.oseasy.sys.common.config.SysSval;
import com.oseasy.sys.common.config.SysSval.SysEmskey;
import com.oseasy.sys.modules.sys.service.BackTeacherExpansionService;
import com.oseasy.sys.modules.sys.service.SysSystemService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 专家信息表Controller
 *
 * @author zy
 * @version 2017-03-27
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/expert")
public class ExpertController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private CoreService coreService;
    @Autowired
    private SysSystemService sysSystemService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    BackTeacherExpansionService backTeacherExpansionService ;

    @ModelAttribute
    public User get(@RequestParam(required = false) String id, Model model) {
        User entity = null;
        if (StringUtil.isNotBlank(id)) {
            entity = UserUtils.get(id);
        }
        if (entity == null) {
            entity = new User();
        }
        return entity;
    }

    @RequiresPermissions("sys:studentExpansion:view")
    @RequestMapping(value = {"list", ""})
    public String list(User user, HttpServletRequest request, HttpServletResponse response,
                       Model model) {
//        Page<User> page = systemService.findUserByExpert(new Page<User>(request, response), user);
//        model.addAttribute("page", page);
        return SysSval.path.vms(SysEmskey.SYS.k()) + "backExpertList";
    }

    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = "form")
    public String form(User user, Model model) {
        if (user == null) {
            return CorePages.ERROR_404.getIdxUrl();
        }
        if (user.getCompany() == null || user.getCompany().getId() == null) {
            user.setCompany(UserUtils.getUser().getCompany());
        }
        model.addAttribute("user", user);
        model.addAttribute("cuser", UserUtils.getUser());
        List<Role> list = new ArrayList<Role>();
        list.add(sysSystemService.getRole(SysIds.SYS_COLLEGE_EXPERT.getId()));
        list.add(sysSystemService.getRole(SysIds.SYS_SCHOOL_EXPERT.getId()));
        model.addAttribute("allRoles", list);

        List<Dict> dictList = DictUtils.getDictList(SysSval.DICT_TECHNOLOGY_FIELD);
        model.addAttribute("allDomains", dictList);

        return SysSval.path.vms(SysEmskey.SYS.k()) + "backExpertUserForm";
    }

    @RequiresPermissions("sys:user:edit")
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
            return CoreSval.REDIRECT + adminPath + "/sys/expert/list?repage";
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
            return form(user, model);
        }

        if (!"true".equals(checkLoginName(user.getLoginName(), user.getId()))) {
            addMessage(model, "保存用户'" + user.getLoginName() + "'失败，登录名已存在");
            return form(user, model);
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
        // 保存用户信息
        // logger.info("============user.domain:"+user.getDomain());
        sysSystemService.saveUser(user);
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
        return CoreSval.REDIRECT + adminPath + "/sys/expert/list?repage";
    }


    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "delete")
    public String delete(User user, RedirectAttributes redirectAttributes) {
        if (UserUtils.getUser().getId().equals(user.getId())) {
            addMessage(redirectAttributes, "删除用户失败, 不允许删除当前用户");
        } else if (UserEntity.isSupUser(user.getId())) {
            addMessage(redirectAttributes, "删除用户失败, 不允许删除超级管理员用户");
        } else {
            coreService.deleteUser(user); // 删除用户
            addMessage(redirectAttributes, "删除用户成功");
        }
        return CoreSval.REDIRECT + adminPath + "/sys/expert/list?repage";
    }

    public String checkLoginName(String loginName, String userid) {
        if (userService.getByLoginNameOrNo(loginName, userid) == null) {
            return "true";
        }
        return "false";
    }

}
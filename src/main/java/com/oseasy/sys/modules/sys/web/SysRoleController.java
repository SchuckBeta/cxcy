/**
 *
 */
package com.oseasy.sys.modules.sys.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.sys.modules.sys.service.SysSystemService;

/**
 * 角色Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/role")
public class SysRoleController extends BaseController {
    @Autowired
    private SysSystemService sysSystemService;

    /**
     * 角色分配 -- 从角色中移除用户
     * @param userId
     * @param roleId
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("sys:role:edit")
    @RequestMapping(value = "outrole")
    public String outrole(String userId, String roleId, RedirectAttributes redirectAttributes) {
        if (CoreSval.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return CoreSval.REDIRECT + adminPath + "/sys/role/assign?id="+roleId;
        }
        Role role = sysSystemService.getRole(roleId);
        User user = sysSystemService.getUser(userId);
        if (UserUtils.getUser().getId().equals(userId)) {
            addMessage(redirectAttributes, "无法从角色【" + role.getName() + "】中移除用户【" + user.getName() + "】自己！");
        }else {
            if (user.getRoleList().size() <= 1) {
                addMessage(redirectAttributes, "用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除失败！这已经是该用户的唯一角色，不能移除。");
            }else{
                Boolean flag = sysSystemService.outUserInRole(role, user);
                if (!flag) {
                    addMessage(redirectAttributes, "用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除失败！");
                }else {
                    addMessage(redirectAttributes, "用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除成功！");
                }
            }
        }
        return CoreSval.REDIRECT + adminPath + "/sys/role/assign?id="+role.getId();
    }

    /**
     * 角色分配
     * @param role
     * @param idsArr
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("sys:role:edit")
    @RequestMapping(value = "assignrole")
    public String assignRole(Role role, String[] idsArr, RedirectAttributes redirectAttributes) {
        if (CoreSval.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return CoreSval.REDIRECT + adminPath + "/sys/role/assign?id="+role.getId();
        }
        StringBuilder msg = new StringBuilder();
        int newNum = 0;
        for (int i = 0; i < idsArr.length; i++) {
            User user = sysSystemService.assignUserToRole(role, sysSystemService.getUser(idsArr[i]));
            if (null != user) {
                msg.append("<br/>新增用户【" + user.getName() + "】到角色【" + role.getName() + "】！");
                newNum++;
            }
        }
        addMessage(redirectAttributes, "已成功分配 "+newNum+" 个用户"+msg);
        return CoreSval.REDIRECT + adminPath + "/sys/role/assign?id="+role.getId();
    }
}

/**
 *
 */
package com.oseasy.act.modules.actyw.web;

import java.util.Arrays;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.act.modules.actyw.entity.ActYwGrole;
import com.oseasy.act.modules.actyw.service.ActYwGroleService;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.mapper.JsonMapper;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;

/**
 * 角色Controller


 */
@Controller
@RequestMapping(value = "${adminPath}/sys/role")
public class ActRoleController extends BaseController {
	@Autowired
	private CoreService coreService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private ActYwGroleService actYwGroleService;

    @RequiresPermissions("sys:role:edit")
    @RequestMapping(value = "delete")
    public String delete(Role role, RedirectAttributes redirectAttributes) {
//      Integer cc= coreService.getRoleUserCount(role.getId());
//      if(cc!=null&&cc>0){
//          addMessage(redirectAttributes, "该角色已分配用户，不能删除！");
//          return CoreSval.REDIRECT + adminPath + "/sys/role/?repage";
//      }
        if (!(UserUtils.getUser().getAdmin() || UserUtils.getUser().getSysAdmin()) && role.getSysData().equals(Const.YES)) {
            addMessage(redirectAttributes, "越权操作，只有超级管理员才能修改此数据！");
            return CoreSval.REDIRECT + adminPath + "/sys/role/?repage";
        }
        if (CoreSval.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return CoreSval.REDIRECT + adminPath + "/sys/role/?repage";
        }

        List<ActYwGrole> groles = actYwGroleService.checkUseByRole(Arrays.asList(new String[]{role.getId()}));
        if (((groles != null) && (groles.size() > 0))) {
            addMessage(redirectAttributes, "当前角色被使用！使用流程为：[" + groles.get(0).getGroup().getName() + "],禁止被删除");
            logger.warn("当前角色被使用,禁止被删除,使用详情，请查看JSON:");
            logger.warn(JsonMapper.toJsonString(groles));
            return CoreSval.REDIRECT + adminPath + "/sys/role/?repage";
        }
        coreService.deleteRole(role);
        addMessage(redirectAttributes, "删除角色成功");
        return CoreSval.REDIRECT + adminPath + "/sys/role/?repage";
    }

    @RequestMapping(value = "ajaxDelete", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult ajaxDelete(@RequestBody Role role) {
        try{
            {
//              Integer cc= coreService.getRoleUserCount(role.getId());
                role=systemService.getRole(role.getId());
//              if(cc!=null&&cc>0){
//                  return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+"该角色已分配用户，不能删除！");
//              }
                if (!(UserUtils.getUser().getAdmin() || UserUtils.getUser().getSysAdmin()) && role.getSysData().equals(Const.YES)) {
                    return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+"越权操作，只有超级管理员才能修改此数据！");
                }
                if (CoreSval.isDemoMode()) {
                    return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+"演示模式，不允许操作");
                }
                List<ActYwGrole> groles = actYwGroleService.checkUseByRole(Arrays.asList(new String[]{role.getId()}));
                if (((groles != null) && (groles.size() > 0))) {
                    return ApiResult.failed(ApiConst.CODE_INNER_ERROR,"当前角色被使用,禁止被删除");
                }
                List<String> userList = coreService.findListByRoleId(role.getId());
                if (((userList != null) && (userList.size() > 0))) {
                    return ApiResult.failed(ApiConst.CODE_INNER_ERROR,"当前角色下有用户,禁止被删除");
                }
                coreService.deleteRole(role);
            }
            return ApiResult.success();
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }
}

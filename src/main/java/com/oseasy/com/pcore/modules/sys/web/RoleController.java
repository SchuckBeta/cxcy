/**
 *
 */
package com.oseasy.com.pcore.modules.sys.web;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.CoreEmskey;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.service.OfficeService;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.util.common.utils.Collections3;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 角色Controller


 */
@Controller
@RequestMapping(value = "${adminPath}/sys/role")
public class RoleController extends BaseController {
	@Autowired
	private CoreService coreService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private OfficeService officeService;
	@ModelAttribute("role")
	public Role get(@RequestParam(required=false) String id) {
		if (StringUtil.isNotBlank(id)) {
			return systemService.getRole(id);
		}else{
			return new Role();
		}
	}

//	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = {"list", ""})
	public String list(Role role, Model model) {
//		List<Role> list = systemService.findAllRole();
//		model.addAttribute("list", list);
//		if(UserUtils.getUser().getAdmin() || UserUtils.getUser().getSysAdmin()){
//			model.addAttribute("admin", true);
//		}else{
//			model.addAttribute("admin", false);
//		}
		return CoreSval.path.vms(CoreEmskey.SYS.k()) + "roleList";
	}

	@RequestMapping(value = "getRoleList", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult getRoleList(){
		try {
			List<Role> list = systemService.findAllRole();
			Integer maxAuth = CoreUtils.getUserRtype(UserUtils.getUser());
			Integer cur = null;
			List<Role> roles = Lists.newArrayList();
			for (Role role: list) {
				cur = Integer.parseInt(role.getRtype());
				if(maxAuth <= cur){
					roles.add(role);
				}
			}
			return ApiResult.success(roles);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequestMapping(value = "getRoleAuth", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult getRoleAuth(){
		try {
			return ApiResult.success(CoreSval.Rtype.getRtype());
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequestMapping(value = "getUserRoleAuth", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult getUserRoleAuth(@RequestBody User user){
		try {
			//得到用户最高权限
			List<Role> roleList =coreService.findListByUserId(user.getId());
			for(Role role:roleList){
				if(StringUtil.isEmpty(role.getRtype())){
					continue;
				}

				if(StringUtil.isEmpty(user.getAuth())){
					user.setAuth(role.getRtype());
				}else if(Integer.valueOf(user.getAuth())> Integer.valueOf(role.getRtype())){
					user.setAuth(role.getRtype());
				}
			}
			if(StringUtil.isEmpty(user.getAuth())){
				user.setAuth(CoreSval.Rtype.OTHER.getKey());
			}
			return ApiResult.success(user);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}


//	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "form")
	public String form(Role role, Model model, HttpServletRequest request) {
		if (role.getOffice()==null) {
			role.setOffice(UserUtils.getUser().getOffice());
		}
		//当前查看角色菜单
		List<Menu> roleMenuList=systemService.getMenuByRoleId(role.getId());
		//model.addAttribute("roleMenuList", roleMenuList);

		model.addAttribute("role", role);
		//当前用户角色菜单
		List<Menu> userMenuList=systemService.findAllMenu();
		model.addAttribute("menuList", userMenuList);
		List<Menu> inRoleList=Lists.newArrayList();
		List<Menu> outRoleList=Lists.newArrayList();
		for(Menu menu:roleMenuList){
			if(userMenuList.contains(menu)){
				inRoleList.add(menu);
			}else{
				outRoleList.add(menu);
			}
		}
		model.addAttribute("inRoleList", inRoleList);
		model.addAttribute("outRoleList", outRoleList);
		User cuser = UserUtils.getUser();
		model.addAttribute("rtypeList", CoreSval.Rtype.toList(CoreSval.getCurrpntplTenantByType(cuser), CoreUtils.getUserRtype(cuser)));
		if(UserUtils.getUser().getAdmin() || UserUtils.getUser().getSysAdmin()){
			model.addAttribute("admin", true);
		}else{
			model.addAttribute("admin", false);
		}
		return CoreSval.path.vms(CoreEmskey.SYS.k()) + "roleForm";
	}

	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "save")
	public String save(Role role, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
//		if (!(UserUtils.getUser().getAdmin() || UserUtils.getUser().getSysAdmin()) && role.getSysData().equals(CoreSval.YES)) {
//			addMessage(redirectAttributes, "越权操作，只有超级管理员才能修改此数据！");
//			return CoreSval.REDIRECT + adminPath + "/sys/role/?repage";
//		}
		if (CoreSval.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return CoreSval.REDIRECT + adminPath + "/sys/role/?repage";
		}
		if (!beanValidator(model, role)) {
			return form(role, model,request);
		}
		if (!"true".equals(checkName(role.getOldName(), role.getName()))) {
			addMessage(model, "保存角色" + role.getName() + "失败, 角色名已存在");
			return form(role, model,request);
		}
		if (!"true".equals(checkEnname(role.getOldEnname(), role.getEnname()))) {
			addMessage(model, "保存角色" + role.getName() + "失败, 英文名已存在");
			return form(role, model,request);
		}
//		if(StringUtil.isNotEmpty(role.getId())){
//			Integer cc= coreService.getRoleUserCount(role.getId());
//			if(cc!=null&&cc>0){
//				Role old=systemService.getRole(role.getId());
//				if(old!=null&&StringUtil.isNotEmpty(old.getBizType())&&!old.getBizType().equals(role.getBizType())){
//					addMessage(redirectAttributes, "保存失败，该角色已分配用户，不能修改角色业务类型！");
//					return CoreSval.REDIRECT + adminPath + "/sys/role/?repage";
//				}
//			}
//		}
		coreService.saveRole(role);
		addMessage(redirectAttributes, "保存角色" + role.getName() + "成功");
		return CoreSval.REDIRECT + adminPath + "/sys/role/?repage";
	}


//	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "saveRole", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult saveRole(@RequestBody Role role){
		try {
			if (CoreSval.isDemoMode()) {
				return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":演示模式，不允许操作！");
			}
//			if(StringUtil.isNotEmpty(role.getId())){
//				Integer cc= coreService.getRoleUserCount(role.getId());
//				if(cc!=null&&cc>0){
//					Role old=systemService.getRole(role.getId());
//					if(old!=null&&StringUtil.isNotEmpty(old.getBizType())&&!old.getBizType().equals(role.getBizType())){
//						return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":保存失败，该角色已分配用户，不能修改角色业务类型！");
//					}
//				}
//			}
			//默认添加为4
			if(StringUtil.isEmpty(role.getBizType())){
				role.setBizType("4");
			}
			coreService.saveRole(role);
			return ApiResult.success();
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	/**
	 * 角色分配页面
	 * @param role
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "assign")
	public String assign(Role role, Model model, HttpServletRequest request) {
		List<User> userList = coreService.findUser(new User(new Role(role.getId())));
		model.addAttribute("userList", userList);
		String secondName=request.getParameter("secondName");
		if(StringUtil.isNotEmpty(secondName)){
			model.addAttribute("secondName",secondName);
		}
		return CoreSval.path.vms(CoreEmskey.SYS.k()) + "roleAssign";
	}

	/**
	 * 角色分配 -- 打开角色分配对话框
	 * @param role
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "usertorole")
	public String selectUserToRole(Role role, Model model) {
		List<User> userList = coreService.findUser(new User(new Role(role.getId())));
		model.addAttribute("role", role);
		model.addAttribute("userList", userList);
		model.addAttribute("selectIds", Collections3.extractToString(userList, "name", ","));
		model.addAttribute("officeList", officeService.findAll());
		return CoreSval.path.vms(CoreEmskey.SYS.k()) + "selectUserToRole";
	}

	/**
	 * 角色分配 -- 根据部门编号获取用户列表
	 * @param officeId
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:role:view")
	@ResponseBody
	@RequestMapping(value = "users")
	public List<Map<String, Object>> users(String officeId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		User user = new User();
		user.setOffice(new Office(officeId));
		Page<User> page = systemService.findUser(new Page<User>(1, -1), user);
		for (User e : page.getList()) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", 0);
			map.put("name", e.getName());
			mapList.add(map);
		}
		return mapList;
	}

	/**
	 * 验证角色名是否有效
	 * @param oldName
	 * @param name
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "checkName")
	public String checkName(String oldName, String name) {
		if (name!=null && name.equals(oldName)) {
			return "true";
		} else if (name!=null && systemService.getRoleByName(name) == null) {
			return "true";
		}
		return "false";
	}

	@RequestMapping(value="checkRoleName", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Boolean checkRoleName(Role role){
		return systemService.checkRoleName(role);
	}

	@RequestMapping(value="checkRoleEnName", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Boolean checkRoleEnName(Role role){
		return systemService.checkRoleEnName(role);
	}

	/**
	 * 验证角色英文名是否有效
	 * @param oldEnname
	 * @param enname
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "checkEnname")
	public String checkEnname(String oldEnname, String enname) {
		if (enname!=null && enname.equals(oldEnname)) {
			return "true";
		} else if (enname!=null && systemService.getRoleByEnname(enname) == null) {
			return "true";
		}
		return "false";
	}

	/**
	 * 获取角色类型.
	 * @return ApiTstatus
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxRtypes", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public ApiTstatus<String> ajaxRtypes(@RequestParam(required = false) Boolean isAll, @RequestParam(required = false) String key) {
		if(isAll == null){
			isAll = false;
		}

		//查询所有
		if(isAll){
			return new ApiTstatus<String>(true, "查询成功！", (Arrays.asList(CoreSval.Rtype.values()).toString()));
		}

		if(StringUtil.isNotEmpty(key)){
			CoreSval.Rtype rtype = CoreSval.Rtype.getByKey(key);
			if(rtype == null){
				return new ApiTstatus<String>(false, "查询失败,查询记录不存在或未定义！");
			}
			return new ApiTstatus<String>(true, "查询成功！", (Arrays.asList(rtype).toString()));
		}
		return new ApiTstatus<String>(false, "查询失败,参数不正确！");
	}
}

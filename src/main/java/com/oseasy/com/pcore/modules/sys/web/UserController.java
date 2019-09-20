/**
 *
 */
package com.oseasy.com.pcore.modules.sys.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.pcore.common.persistence.UserEntity;
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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.CoreEmskey;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.utils.excel.ExportExcel;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

/**
 * 用户Controller
 *
 * @version 2013-8-29
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user")
public class UserController extends BaseController {
    @Autowired
    private SystemService systemService;
    @Autowired
    private CoreService coreService;
    @Autowired
    UserService userService;
    @ModelAttribute
    public User get(@RequestParam(required = false) String id) {
        if (StringUtil.isNotBlank(id)) {
            return systemService.getUser(id);
        } else {
            return new User();
        }
    }

    @RequestMapping(value = {"index"})
    public String index(User user, Model model) {
        return CoreSval.path.vms(CoreEmskey.SYS.k()) + "userIndex";

    }

    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = {"list", ""})
    public String list(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
//        Page<User> page = systemService.findUser(new Page<User>(request, response), user);
//        if (page != null) {
//            List<User> userList = page.getList();
//            if (StringUtil.checkNotEmpty(userList)) {
//                List<Role> roles = coreService.findListByUserIds(StringUtil.sqlInByListIdss(userList));
//                for (User usertmp : userList) {
//                    List<Role> curroles = Lists.newArrayList();
//                    for (Role currole : roles) {
//                        if((usertmp.getId()).equals(currole.getUser().getId())){
//                            curroles.add(currole);
//                        }
//                    }
//                    usertmp.setRoleList(curroles);
//                }
//            }
//        }

//        List<Role> roleList = systemService.findAllRole();
//        if (roleList != null & roleList.size() > 0) {
//           JSONObject js = new JSONObject();
//           for (Role r : roleList) {
//               js.put(r.getId(), r.getBizType());
//           }
//           model.addAttribute("rolesjs", js);
//       }
//
//        model.addAttribute("roleList", roleList);

//        List<Dict> dictList = DictUtils.getDictList(User.DICT_TECHNOLOGY_FIELD);
//        model.addAttribute("allDomains", dictList);
//        model.addAttribute("page", page);
        // return CoreSval.path.vms(CoreEmskey.SYS.k()) + "userList";
        return CoreSval.path.vms(CoreEmskey.SYS.k()) + "userListReDefine";
    }

    @RequestMapping(value="checkUserIsAdmin", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Boolean checkUserIsAdmin(){
        return UserEntity.checkCurpnSuper(UserUtils.getUser());
    }

    @RequestMapping(value="updateUserLoginName/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult updateUserLoginName(@PathVariable String id, String loginName, String password, Model model){
        try {
            User user1 = UserUtils.get(id);
            User nUser = new User();
            nUser.setId(id);
            nUser.setLoginName(loginName);
//            nUser.setPassword(password);
            if(user1 == null){
                return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":用户为空");
            }
//            if(!beanValidator(model, nUser)){
//                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":请检查");
//            }
//            if(CoreUtils.validatePassword(nUser.getPassword(), user1.getPassword())){
                coreService.updateUserLoginName(nUser);
                return ApiResult.success(nUser);
//            }
//            return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":确认密码错误");
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    // @RequiresPermissions("sys:user:view")
    @RequestMapping("userListTree")
    public String userListTree(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<User> page = systemService.findUser(new Page<User>(request, response), user);
        if (page != null) {
            List<User> userList = page.getList();
            if (userList != null && userList.size() > 0) {
                for (User usertmp : userList) {
                    List<Role> roleList = coreService.findListByUserId(usertmp.getId());
                    usertmp.setRoleList(roleList);
                }
            }
        }

        List<Role> roleList = systemService.findAllRole();

        model.addAttribute("roleList", roleList);

        model.addAttribute("page", page);
        // return CoreSval.path.vms(CoreEmskey.SYS.k()) + "userList";
        return CoreSval.path.vms(CoreEmskey.SYS.k()) + "userListTree";
    }

    // @RequiresPermissions("sys:user:view")
    @RequestMapping("userQyListTree")
    public String userQyListTree(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<User> page = systemService.findUser(new Page<User>(request, response), user);
        if (page != null) {
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
                }
            }
        }
        List<Role> roleList = systemService.findAllRole();
        model.addAttribute("roleList", roleList);
        model.addAttribute("page", page);
        // return CoreSval.path.vms(CoreEmskey.SYS.k()) + "userList";
        return CoreSval.path.vms(CoreEmskey.SYS.k()) + "userListTree";
    }

    @ResponseBody
    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = {"listData"})
    public Page<User> listData(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
        return systemService.findUser(new Page<User>(request, response), user);
    }


    /**
     * addby zhangzheng 检查输入的手机号是否已经注册过
     * @param mobile
     * @return true:没注册，允许修改
     */
    @RequestMapping(value = "checkMobileExist")
    @ResponseBody
    public Boolean checkMobileExist(String mobile, String id) {
        User userForSearch=new User();
        userForSearch.setMobile(mobile);
        User cuser=UserUtils.get(id);
        if (cuser==null||StringUtil.isEmpty(cuser.getId())) {
            return false;
        }
        userForSearch.setId(cuser.getId());
        User user = userService.getByMobileWithId(userForSearch);
        return user==null;
    }

    //重置密码
    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "resetpwd")
    public String resetpwd(User user, RedirectAttributes redirectAttributes) {

        coreService.updatePasswordById(user.getId(), UserUtils.getUser().getLoginName(), CoreUtils.USER_PSW_DEFAULT);
        addMessage(redirectAttributes, "重置密码成功！密码已重置为：123456");
        return CoreSval.REDIRECT + adminPath + "/sys/user/list?repage";
    }


    @RequestMapping(value="resetPassword", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult resetPassword(@RequestBody User user){
        try {
            coreService.updatePasswordById(user.getId(), UserUtils.getUser().getLoginName(), CoreUtils.USER_PSW_DEFAULT);
            return ApiResult.success();
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 导出用户数据
     *
     * @param user
     * @param request
     * @param response
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public String exportFile(User user, HttpServletRequest request, HttpServletResponse response,
                             RedirectAttributes redirectAttributes) {
        try {
            String fileName = "用户数据" + DateUtil.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<User> page = systemService.findUser(new Page<User>(request, response, -1), user);
            new ExportExcel("用户数据", User.class).setDataList(page.getList()).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导出用户失败！失败信息：" + e.getMessage());
        }
        return CoreSval.REDIRECT + adminPath + "/sys/user/list?repage";
    }

    /**
     * 导入用户数据
     *
     * @param file
     * @param redirectAttributes
     * @return
     */
    /*
	 * @RequiresPermissions("sys:user:edit")
	 *
	 * @RequestMapping(value = "import", method=RequestMethod.POST) public
	 * String importFile(MultipartFile file, RedirectAttributes
	 * redirectAttributes) { if (CoreSval.isDemoMode()) {
	 * addMessage(redirectAttributes, "演示模式，不允许操作！"); return CoreSval.REDIRECT +
	 * adminPath + "/sys/user/list?repage"; } try { int successNum = 0; int
	 * failureNum = 0; StringBuilder failureMsg = new StringBuilder();
	 * ImportExcel ei = new ImportExcel(file, 1, 0); List<User> list =
	 * ei.getDataList(User.class); for (User user : list) { try{ if
	 * ("true".equals(checkLoginName("", user.getLoginName(),""))) {
	 * user.setPassword(SystemService.entryptPassword(SystemService.USER_PSW_DEFAULT));
	 * BeanValidators.validateWithException(validator, user);
	 * systemService.saveUser(user); successNum++; }else{ failureMsg.append(
	 * "<br/>登录名 "+user.getLoginName()+" 已存在; "); failureNum++; }
	 * }catch(ConstraintViolationException ex) { failureMsg.append("<br/>登录名 "
	 * +user.getLoginName()+" 导入失败："); List<String> messageList =
	 * BeanValidators.extractPropertyAndMessageAsList(ex, ": "); for (String
	 * message : messageList) { failureMsg.append(message+"; "); failureNum++; }
	 * }catch (Exception ex) { failureMsg.append("<br/>登录名 "
	 * +user.getLoginName()+" 导入失败："+ex.getMessage()); } } if (failureNum>0) {
	 * failureMsg.insert(0, "，失败 "+failureNum+" 条用户，导入信息如下："); }
	 * addMessage(redirectAttributes, "已成功导入 "+successNum+" 条用户"+failureMsg); }
	 * catch (Exception e) { addMessage(redirectAttributes,
	 * "导入用户失败！失败信息："+e.getMessage()); } return CoreSval.REDIRECT + adminPath +
	 * "/sys/user/list?repage"; }
	 */

    /**
     * 下载导入用户数据模板
     *
     * @param response
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "用户数据导入模板.xlsx";
            List<User> list = Lists.newArrayList();
            list.add(UserUtils.getUser());
            new ExportExcel("用户数据", User.class, 2).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return CoreSval.REDIRECT + adminPath + "/sys/user/list?repage";
    }

    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "checkLoginNameOrNo", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Boolean checkLoginNameOrNo(@RequestBody User user){
        String checkStr = UserUtils.getCheckParam(user);
        return userService.getByLoginNameOrNo(checkStr, user.getId()) == null;
    }


    /**
     * 用户信息显示及保存
     *
     * @param user
     * @param model
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "info")
    public String info(User user, HttpServletResponse response, Model model) {
        User currentUser = UserUtils.getUser();
        if (StringUtil.isNotBlank(user.getName())) {
            if (CoreSval.isDemoMode()) {
                model.addAttribute("message", "演示模式，不允许操作！");
                return CoreSval.path.vms(CoreEmskey.SYS.k()) + "userInfo";
            }
            currentUser.setEmail(user.getEmail());
            currentUser.setPhone(user.getPhone());
            currentUser.setMobile(user.getMobile());
            currentUser.setRemarks(user.getRemarks());
            currentUser.setPhoto(user.getPhoto());
            coreService.updateUserInfo(currentUser);
            model.addAttribute("message", "保存用户信息成功");
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("Global", new CoreSval());
        return CoreSval.path.vms(CoreEmskey.SYS.k()) + "userInfo";
    }

    /**
     * 返回用户信息
     *
     * @return
     */
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "infoData")
    public User infoData() {
        return UserUtils.getUser();
    }

    /**
     * 修改个人用户密码
     *
     * @param oldPassword
     * @param newPassword
     * @param model
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "modifyPwd")
    public String modifyPwd(String oldPassword, String newPassword, Model model) {
        User user = UserUtils.getUser();
        if (StringUtil.isNotBlank(oldPassword) && StringUtil.isNotBlank(newPassword)) {
            if (CoreSval.isDemoMode()) {
                model.addAttribute("message", "演示模式，不允许操作！");
                return CoreSval.path.vms(CoreEmskey.SYS.k()) + "userModifyPwd";
            }
            if (CoreUtils.validatePassword(oldPassword, user.getPassword())) {
                coreService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
                model.addAttribute("message", "修改密码成功");
            } else {
                model.addAttribute("message", "修改密码失败，旧密码错误");
            }
        }
        model.addAttribute("user", user);
        return CoreSval.path.vms(CoreEmskey.SYS.k()) + "userModifyPwd";
    }

    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required = false) String officeId,
                                              HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<User> list = coreService.findUserByOfficeId(officeId);
        for (int i = 0; i < list.size(); i++) {
            User e = list.get(i);
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", "u_" + e.getId());
            map.put("pId", officeId);
            map.put("name", StringUtil.replace(e.getName(), " ", ""));
            mapList.add(map);
        }
        return mapList;
    }

    @ResponseBody
    @RequestMapping(value = "checkMobile")
    public String checkMobile(HttpServletRequest request) {
        String mobile=request.getParameter("mobile");
        String id=request.getParameter("id");
        if (mobile != null && coreService.getUserByMobile(mobile, id) == null) {
            return "true";
        }
        return "false";
    }

    /**
     * 检查学号，学号不能与其他人的学号相同，不能与其他人的loginName相同
     *
     * @param userid
     * @param no
     * @return
     */
    @RequestMapping(value = "checkNo")
    @ResponseBody
    public Boolean checkNo(String no, String userid) {
       return  userService.getByLoginNameOrNo(no, userid) == null;
    }

    @ResponseBody
    @RequestMapping(value = "uploadPhoto")
    public boolean uploadFTP(HttpServletRequest request, User user) {
        String arrUrl = request.getParameter("arrUrl");
        if (user != null) {
            user.setPhoto(arrUrl);
            userService.updateUser(user);
        }
        return true;
    }

    /**
     * 修复学生导师用户没有角色.
     *
     * @param rid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "repaireStudentRole/{rid}")
    public ApiTstatus<List<String>> repaireStudentRole(@PathVariable("rid") String rid) {
        if (StringUtil.isNotEmpty(rid)) {
            return coreService.insertPLUserRole(rid, userService.findUserByRepair());
        }
        return new ApiTstatus<List<String>>(false, "修复失败,角色ID为空!");
    }

    @RequestMapping(value="checkUserNoUnique", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Boolean checkUserNoUnique(@RequestBody User user){
        return userService.checkUserNoUnique(user);
    }
}

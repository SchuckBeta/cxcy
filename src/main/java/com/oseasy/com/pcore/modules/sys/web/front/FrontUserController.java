package com.oseasy.com.pcore.modules.sys.web.front;

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
import com.oseasy.com.fileserver.common.vsftp.VsftpUtils;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.CoreEmskey;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

@Controller
@RequestMapping(value = "${frontPath}/sys/user")
public class FrontUserController extends BaseController {
	@Autowired
	private OaNotifyService oaNotifyService;
	@Autowired
	UserService userService;
	@Autowired
	SystemService systemService;
	@Autowired
	CoreService coreService;

	@ModelAttribute
	public User get(@RequestParam(required=false) String id) {
		if (StringUtil.isNotBlank(id)) {
			User user = coreService.getUser(id);
			return user;
		}else{
			return new User();
		}
	}

	@RequestMapping(value = {"index"})
	public String index(User user, Model model,HttpServletRequest request) {
		String teamId = request.getParameter("teamId");
		String opType = request.getParameter("opType");
		String userType = request.getParameter("userType");
		model.addAttribute("teamId", teamId);
		model.addAttribute("opType", opType);
		model.addAttribute("userType", userType);
		model.addAttribute("user", user);
		return CoreSval.path.vms(CoreEmskey.SYS.k()) + "userIndex";

	}

	@RequestMapping(value="getUserDetail", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult getUserDetail(User user){
		try {
			return ApiResult.success(user);
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequestMapping(value="updateUserLoginName/{id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult updateUserLoginName(@PathVariable String id, String loginName, String password, Model model){
		try {
			User user1 = UserUtils.get(id);
			User nUser = new User();
			nUser.setId(id);
			nUser.setLoginName(loginName);
//			nUser.setPassword(password);
			if(user1 == null){
				return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":用户为空");
			}
//			if(!beanValidator(model, nUser)){
//				return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":请检查");
//			}
//			if(CoreUtils.validatePassword(nUser.getPassword(), user1.getPassword())){
				coreService.updateUserLoginName(nUser);
				return ApiResult.success(nUser);
//			}
//			return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":确认密码错误");
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequestMapping(value = {"indexPublish"})
	public String indexPublish(User user, Model model,HttpServletRequest request) {
		String teamId = request.getParameter("teamId");
		String opType = request.getParameter("opType");
		String userType = request.getParameter("userType");
		model.addAttribute("teamId", teamId);
		model.addAttribute("opType", opType);
		model.addAttribute("userType", userType);
		return CoreSval.path.vms(CoreEmskey.SYS.k()) + "userIndexPublish";

	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String officeId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<User> list = coreService.findUserByOfficeId(officeId);
		for (int i=0; i<list.size(); i++) {
			User e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", "u_"+e.getId());
			map.put("pId", officeId);
			map.put("name", StringUtil.replace(e.getName(), " ", ""));
			mapList.add(map);
		}
		return mapList;
	}

	@RequestMapping(value = "delete")
	public String delete(OaNotify oaNotify, RedirectAttributes redirectAttributes) {
		oaNotifyService.delete(oaNotify);
		addMessage(redirectAttributes, "删除发送通知成功");
		return CoreSval.REDIRECT + frontPath + "/sys/user/indexMySendNoticeList/?repage";
	}

	@ResponseBody
	@RequestMapping(value = "isExistMobile")
	public String isExistMobile(String mobile) {
		if (StringUtil.isNotEmpty(mobile) && UserUtils.isExistMobile(mobile)) {
			return "true";
		}
		return "false";
	}

	@ResponseBody
	@RequestMapping(value = "checkMobile")
	public String checkMobile(@RequestParam(value="user.mobile")String mobile,@RequestParam(value="id")String id) {
		if (mobile !=null && coreService.getUserByMobile(mobile,id) == null) {
			return "true";
		}
		return "false";
	}
	/**
	 * 检查loginName 登录名不能与其他人的登录名相同，不能与其他人的no相同
	 * @param userid
	 * @param loginName
	 * @return
	 */
	@RequestMapping(value = "checkLoginName")
	@ResponseBody
	public Boolean checkLoginName(String loginName,String userid) {
		return userService.getByLoginNameOrNo(loginName, userid)== null;
	}


	/**
	 * 检查学号，学号不能与其他人的学号相同，不能与其他人的loginName相同
	 * @param userid
	 * @param no
	 * @return
	 */
	@RequestMapping(value = "checkNo")
	@ResponseBody
	public Boolean checkNo(String no,String userid) {
		return userService.getByLoginNameOrNo(no, userid)== null;
	}

	/**
	 * addby zhangzheng 检查输入的手机号是否已经注册过
	 * @param mobile
	 * @return true:没注册，允许修改
	 */
	@RequestMapping("checkMobileExist")
	@ResponseBody
	public Boolean checkMobileExist(String mobile) {
		User userForSearch=new User();
		userForSearch.setMobile(mobile);
		User cuser=UserUtils.getUser();
		if (cuser==null||StringUtil.isEmpty(cuser.getId())) {
			return false;
		}
		userForSearch.setId(cuser.getId());
		User user = userService.getByMobileWithId(userForSearch);
		if (user==null) {
			return true;
		}else{
			return false;
		}

	}

	@ResponseBody
	@RequestMapping(value="uploadPhoto")
	public boolean uploadFTP(HttpServletRequest request,User user) {
		String arrUrl = request.getParameter("arrUrl");
		if (user!=null) {
			user.setPhoto(arrUrl);
			userService.updateUser(user);
		}
		return true;
	}

	//修改头像信息
    @ResponseBody
    @RequestMapping(value = "/ajaxUpdatePhoto", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<User> ajaxUpdatePhoto(@RequestParam(required = false) String userId, @RequestParam(required = true) String photo) {
			if (StringUtil.isEmpty(userId)) {
				userId = UserUtils.getUser().getId();
			}
			if (StringUtil.isEmpty(photo)) {
				userId = UserUtils.getUser().getId();
			}
			User user = userService.findUserById(userId);

			//替换头像地址
			if (StringUtil.isNotEmpty(photo)) {
				String newUrl = null;
				newUrl = VsftpUtils.moveFile(photo);
				if (StringUtil.isNotEmpty(newUrl)) {
					user.setPhoto(newUrl);
				}
			}
			//user.setPhoto(photo);
			userService.updateUser(user);

			if (user != null) {
				CacheUtils.remove(CoreUtils.USER_CACHE, CoreUtils.USER_CACHE_ID_ + user.getId());
				CacheUtils
						.remove(CoreUtils.USER_CACHE, CoreUtils.USER_CACHE_LOGIN_NAME_ + user.getLoginName());
			}
			return new ApiTstatus<User>(true, "修改头像成功", user);
		}

	@RequestMapping(value="checkUserNoUnique", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Boolean checkUserNoUnique(@RequestBody User user){
		return userService.checkUserNoUnique(user);
	}

	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "checkLoginNameOrNo", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Boolean checkLoginNameOrNo(@RequestBody User user){
		String checkStr = UserUtils.getCheckParam(user);
		return userService.getByLoginNameOrNo(checkStr, user.getId()) == null;
	}
}

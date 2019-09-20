/**
 *
 */
package com.oseasy.sys.modules.team.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.vo.UserVo;
import com.oseasy.sys.common.config.SysSval;
import com.oseasy.sys.common.config.SysSval.SysEmskey;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 用户Controller

 * @version 2013-8-29
 */
@Controller
public class TeamUserController extends BaseController {
	@Autowired
	private UserService userService;
	@RequestMapping(value="${adminPath}/backUserSelect")
	public String selectUser(Model model,UserVo userVo, HttpServletRequest request, HttpServletResponse response) {
		model.addAttribute("user", userVo);
		model.addAttribute("ids", userVo.getIds());
		model.addAttribute("userType", userVo.getUserType());
		return SysSval.path.vms(SysEmskey.TEAM.k()) + "backUserSelect";
	}
	@RequestMapping(value="${adminPath}/selectUser/getPage")
	@ResponseBody
	public Page<UserVo> getPage(Model model,UserVo userVo, HttpServletRequest request, HttpServletResponse response) {
		Page<UserVo> page = userService.findPageByVo(new Page<UserVo>(request, response), userVo);
		return page;
	}
	@RequestMapping(value="${adminPath}/selectUser/getStudentInfo")
	@ResponseBody
	public List<UserVo>  getStudentInfo(Model model,String ids, HttpServletRequest request, HttpServletResponse response) {
		String[] idsArr=null;
		if (StringUtil.isNotEmpty(ids)) {
			idsArr=ids.split(",");
		}
		return userService.getStudentInfo(idsArr);
	}
	@RequestMapping(value="${adminPath}/selectUser/getTeaInfo")
	@ResponseBody
	public List<UserVo>  getTeaInfo(Model model,String ids, HttpServletRequest request, HttpServletResponse response) {
		String[] idsArr=null;
		if (StringUtil.isNotEmpty(ids)) {
			idsArr=ids.split(",");
		}
		return userService.getTeaInfo(idsArr);
	}
}

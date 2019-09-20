package com.oseasy.sys.modules.sys.web;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.pcore.modules.sys.service.OfficeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.fileserver.common.vsftp.VsftpUtils;
import com.oseasy.com.fileserver.modules.vsftp.service.FtpService;
import com.oseasy.com.pcore.common.config.CorePages;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.sys.common.config.SysSval;
import com.oseasy.sys.common.config.SysSval.SysEmskey;
import com.oseasy.sys.common.utils.SysUserUtils;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;
import com.oseasy.sys.modules.sys.service.StudentExpansionService;
import com.oseasy.sys.modules.sys.utils.SysConfigUtil;
import com.oseasy.sys.modules.sys.vo.SysConfigVo;
import com.oseasy.sys.modules.team.entity.TeamUserRelation;
import com.oseasy.sys.modules.team.service.TeamUserRelationService;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

import net.sf.json.JSONObject;

/**
 * 学生信息表Controller
 *
 * @author zy
 * @version 2017-03-27
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/studentExpansion")
public class StudentExpansionController extends BaseController {

	@Autowired
	private StudentExpansionService studentExpansionService;
	@Autowired
	private UserService  userService;
	@Autowired
	private FtpService ftpService;
	@Autowired
	private TeamUserRelationService teamUserRelationService;
	@Autowired
	private OfficeService officeService;
	@ModelAttribute
	public StudentExpansion get(@RequestParam(required = false) String id,Model model) {
		StudentExpansion entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = studentExpansionService.get(id);
		}
		if (entity == null) {
			entity = new StudentExpansion();
		}
		return entity;
	}

	@RequiresPermissions("sys:studentExpansion:view")
	@RequestMapping(value = { "list", "" })
	public String list(StudentExpansion studentExpansion, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<StudentExpansion> page = studentExpansionService.findPage(new Page<StudentExpansion>(request, response),studentExpansion);
		model.addAttribute("page", page);

		return SysSval.path.vms(SysEmskey.SYS.k()) + "studentExpansionList";
	}

	//学生列表查询学生接口
	@RequestMapping(value="getStudentList", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult getStudentList(StudentExpansion studentExpansion, HttpServletRequest request, HttpServletResponse response){
		try {
			Page<StudentExpansion> page = studentExpansionService.findPage(new Page<StudentExpansion>(request, response),studentExpansion);
			return ApiResult.success(page);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}


	@RequiresPermissions("sys:studentExpansion:view")
	@RequestMapping(value = "form")
	public String form(StudentExpansion studentExpansion, Model model,HttpServletRequest request) {
//		if(StringUtil.isNotEmpty(studentExpansion.getId()) || studentExpansionService.get(studentExpansion.getId()) == null){
//			return CorePages.ERROR_404.getIdxUrl();
//		}
//		model.addAttribute("studentExpansion", studentExpansion);
//		hashMap.put("studentExpansion", studentExpansion);
//		List<Dict> dictList = DictUtils.getDictList(SysSval.DICT_TECHNOLOGY_FIELD);
//		model.addAttribute("allDomains", dictList);
//		model.addAttribute("userName", studentExpansion.getUser()==null?null:studentExpansion.getUser().getName());
//		hashMap.put("allDomains", dictList);
//		hashMap.put("userName", studentExpansion.getUser()==null?null:studentExpansion.getUser().getName());
//		SysConfigVo scv=SysConfigUtil.getSysConfigVo();
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//		if(scv!=null&&StringUtil.isNotEmpty(scv.getGraduateMonth())){
//			try {
//				sdf.parse("2018-"+scv.getGraduateMonth()+"-01");//校验用
//				model.addAttribute("graduateMonth", scv.getGraduateMonth());
//				hashMap.put("graduateMonth",  scv.getGraduateMonth());
//			} catch (ParseException e) {
//				logger.error("毕业月份参数错误");
//			}
//		}
//		model.addAttribute("curDate", sdf.format(new Date()));
//		hashMap.put("curDate",  sdf.format(new Date()));
//		model.addAttribute("studentExpVo", hashMap);
		model.addAttribute("studentId", studentExpansion.getId());
		return SysSval.path.vms(SysEmskey.SYS.k()) + "studentForm";
	}


	@RequestMapping(value = "view")
	public String view(StudentExpansion studentExpansion, Model model,HttpServletRequest request) {
		HashMap<String, Object> hashMap = new HashMap<>();
		User user=null;
		if (studentExpansion.getUser()!=null) {
			user = studentExpansion.getUser();
			if (user!=null) {
				if (StringUtil.isNotBlank(user.getPhoto())) {
					model.addAttribute("user", user);
					hashMap.put("user", user);
				}
			model.addAttribute("cuser", studentExpansion.getUser().getId());hashMap.put("cuser", studentExpansion.getUser().getId());
			}
		}else{
			if(StringUtil.isNotEmpty(studentExpansion.getId())){
			    return CorePages.ERROR_404.getIdxUrl();
			}
		}
		model.addAttribute("studentExpansion", studentExpansion);
		hashMap.put("studentExpansion", studentExpansion);
		List<Dict> dictList = DictUtils.getDictList(SysSval.DICT_TECHNOLOGY_FIELD);
		model.addAttribute("allDomains", dictList);
		model.addAttribute("userName", studentExpansion.getUser()==null?null:studentExpansion.getUser().getName());
		hashMap.put("allDomains", dictList);
		hashMap.put("userName", studentExpansion.getUser()==null?null:studentExpansion.getUser().getName());
		SysConfigVo scv=SysConfigUtil.getSysConfigVo();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if(scv!=null&&StringUtil.isNotEmpty(scv.getGraduateMonth())){
			try {
				sdf.parse("2018-"+scv.getGraduateMonth()+"-01");//校验用
				model.addAttribute("graduateMonth", scv.getGraduateMonth());
				hashMap.put("graduateMonth",  scv.getGraduateMonth());
			} catch (ParseException e) {
				logger.error("毕业月份参数错误");
			}
		}
		model.addAttribute("curDate", sdf.format(new Date()));
		hashMap.put("curDate",  sdf.format(new Date()));
		model.addAttribute("studentExpVo", hashMap);
		return SysSval.path.vms(SysEmskey.SYS.k()) + "studentView";
	}

	//修改头像信息
	@ResponseBody
	@RequestMapping(value = "/ajaxUpdatePhoto", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public ApiTstatus<User> ajaxUpdatePhoto(@RequestParam(required = false) String userId, @RequestParam(required = true) String photo) {
		if(StringUtil.isEmpty(userId)){
			userId = UserUtils.getUser().getId();
		}
		if(StringUtil.isEmpty(photo)){
			userId = UserUtils.getUser().getId();
		}
		User user = userService.findUserById(userId);

		//替换头像地址
		if(StringUtil.isNotEmpty(photo)){
			String newUrl= null;
			newUrl = VsftpUtils.moveFile(photo);
			if(StringUtil.isNotEmpty(newUrl)){
				user.setPhoto(newUrl);
			}
		}

		//user.setPhoto(photo);
		userService.updateUser(user);

		if (user!=null) {
			CacheUtils.remove(CoreUtils.USER_CACHE, CoreUtils.USER_CACHE_ID_ + user.getId());
			CacheUtils.remove(CoreUtils.USER_CACHE, CoreUtils.USER_CACHE_LOGIN_NAME_ + user.getLoginName());
		}
		return new ApiTstatus<User>(true, "修改头像成功", user);
	}



	//修改头像信息
	@ResponseBody
	@RequestMapping(value = "/ajaxSavePhoto", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public ApiTstatus<String> ajaxSavePhoto(@RequestParam(required = true) String photo) {
		String newUrl= null;
		//替换头像地址
		if(StringUtil.isNotEmpty(photo)){
			newUrl = VsftpUtils.moveFile(photo);
		}
		return new ApiTstatus<String>(true, "上传头像成功", newUrl);
	}


	@RequiresPermissions("sys:studentExpansion:edit")
	@RequestMapping(value = "save")
	public String save(StudentExpansion studentExpansion,HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		String enterdatedate=(String)request.getParameter("enterdate");
		DateFormat df = new SimpleDateFormat("yyyy-MM");
		try {
			Date enDate = df.parse(enterdatedate);
			studentExpansion.setEnterdate(enDate);
		} catch (ParseException e) {
			logger.error(ExceptionUtil.getStackTrace(e));
			addMessage(redirectAttributes, "保存失败");
		}
		if (StringUtil.isNotBlank(studentExpansion.getId())) {
			studentExpansionService.updateAll(studentExpansion);
			addMessage(redirectAttributes,  "保存成功");
		}else {
			studentExpansionService.saveAll(studentExpansion);
			addMessage(redirectAttributes,  "添加用户成功！初始密码设置为：123456");
		}
		return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/sys/studentExpansion/?repage";
	}

	@RequestMapping(value="saveStudentExp", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult saveStudentExp(@RequestBody StudentExpansion studentExpansion){
		try {
			StudentExpansion studentExpansion1 = new StudentExpansion();
			User user1 = studentExpansion.getUser();
			String no = user1.getNo();
			String userid = user1.getId();
			user1.setOffice(officeService.get(user1.getOfficeId()));
			if(userService.getByLoginNameOrNo(no, userid) != null){
				return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":学号已经存在");
			}
			if (StringUtil.isNotBlank(studentExpansion.getId())) {
				studentExpansionService.updateAll(studentExpansion);
				studentExpansion1 = studentExpansionService.get(studentExpansion.getId());
			}else {
				studentExpansionService.saveAll(studentExpansion);
				User user = UserUtils.getByLoginNameOrNo(studentExpansion.getUser().getNo());
				if(user != null){
					studentExpansion1 = studentExpansionService.getByUserId(user.getId());
				}
			}
			return ApiResult.success(studentExpansion1);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}




	@RequiresPermissions("sys:studentExpansion:edit")
	@RequestMapping(value = "delete")
	public String delete(StudentExpansion studentExpansion, RedirectAttributes redirectAttributes) {
		TeamUserRelation teamUserRelation = new TeamUserRelation();
		teamUserRelation.setUser(studentExpansion.getUser());
		teamUserRelation = teamUserRelationService.findUserById(teamUserRelation);
		if (teamUserRelation!=null) {
			addMessage(redirectAttributes, "该学生已加入团队，不能删除!");
			return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/sys/studentExpansion/?repage";
		}
		studentExpansionService.delete(studentExpansion);
		addMessage(redirectAttributes, "删除学生信息成功");
		//删除用户表
		User user= UserUtils.get(studentExpansion.getUser().getId());
		userService.delete(user);
		return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/sys/studentExpansion/?repage";
	}

	//批量删除 返回成功删除数，失败删除数
	@RequestMapping(value = "deleteBatch", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult deleteBatch(String ids) {
		String[] idStr=ids.split(",");
		int successCount=0;
		int failCount=0;
		ApiResult result = new ApiResult();
		try {
			for (String anIdStr : idStr) {
				StudentExpansion studentExpansion = studentExpansionService.get(anIdStr);
				TeamUserRelation teamUserRelation = new TeamUserRelation();
				teamUserRelation.setUser(studentExpansion.getUser());
				teamUserRelation = teamUserRelationService.findUserById(teamUserRelation);
				if (teamUserRelation != null) {
					failCount++;
				} else {
					successCount++;
					studentExpansionService.delete(studentExpansion);
					User user = UserUtils.get(studentExpansion.getUser().getId());
					userService.delete(user);
				}
			}
			String message;
			result.setStatus(ApiConst.STATUS_SUCCESS);
			result.setCode(ApiConst.CODE_REQUEST_SUCCESS);
			if (failCount==0) {
				message="成功删除"+successCount+"个学生。";
			}else{
				message="成功删除"+successCount+"个学生。"+failCount+"个学生已加入团队，不能删除!";
			}
			result.setMsg(message);
			return result;
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}


	@RequiresPermissions("sys:studentExpansion:edit")
	@RequestMapping(value = "addStu")
	public String AddStu(StudentExpansion studentExpansion, RedirectAttributes redirectAttributes) {
		String uid = UUID.randomUUID().toString().replaceAll("-", "");
		studentExpansion.getUser().setId(uid);
		studentExpansionService.save(studentExpansion);
		return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/sys/studentExpansion/?repage";
	}
	@RequestMapping(value = "/toAdd")
	public String toAdd() {
		return  "modules/sys/addStudentExpansion";
	}

	//删除文件
	@RequestMapping(value = {"delload"})
	@ResponseBody
	public JSONObject delload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//path ftp上文件 目录
		//String path=(String)request.getParameter("path");//
		//fileName ftp上文件名
		String fileName=(String)request.getParameter("fileName");
		String studentId = request.getParameter("studentId");
		StudentExpansion studentExpansion = studentExpansionService.get(studentId);
		String userId = studentExpansion.getUser().getId();
		boolean ftpdel=ftpService.del(fileName);
		/*FtpUtil ftpUtil = new FtpUtil();
		FTPClient ftpClient=ftpUtil.getftpClient();*/
		//ftpUtil.remove(ftpClient, fileName.substring(0,fileName.lastIndexOf("/")+1), fileName.substring(fileName.lastIndexOf("/")+1));
		JSONObject obj = new JSONObject();
		User user = userService.findUserById(userId);
		user.setPhoto("");
		userService.updateUser(user);
		if (ftpdel) {
			obj.put("state",1);//删除成功
		}else{
			obj.put("state", 2);
			obj.put("msg", "文件太大");
		}
		return obj;
	}

	@RequestMapping(value="getStudentExpansion", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult getStudentExpansion(@RequestParam(required=true) String userId){
		return ApiResult.success(SysUserUtils.getStudentByUserId(userId));
	}

	@RequestMapping(value="getStuExDataByUserId/{userId}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult getStuExDataByUserId(@PathVariable String userId){
		try {
			if(StringUtil.isEmpty(userId)){
				logger.error("用户ID不存在");
				return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":用户ID不存在");
			}
			StudentExpansion studentExpansion = SysUserUtils.getStudentByUserId(userId);
			if(studentExpansion == null){
				logger.error("该学生不存在");
				return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":学生不存在");
			}
			return ApiResult.success(studentExpansion);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequestMapping(value="getStuExDataBySId/{id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult getStudentExpansionData(@PathVariable String id){
		try {
			if(StringUtil.isEmpty(id)){
				logger.error("学生扩展表ID不存在");
				return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":学生扩展表ID不存在");
			}
			StudentExpansion studentExpansion = studentExpansionService.get(id);
			if(studentExpansion == null){
				logger.error("该学生不存在");
				return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":学生不存在");
			}
			return ApiResult.success(studentExpansion);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}
}
package com.oseasy.sys.modules.sys.web.front;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
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

import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.fileserver.common.utils.FtpUtil;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.service.OfficeService;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.pcore.modules.sys.vo.SysValCode;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.sys.common.config.SysSval;
import com.oseasy.sys.common.config.SysSval.SysEmskey;
import com.oseasy.sys.common.utils.SysUserUtils;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;
import com.oseasy.sys.modules.sys.enums.RoleBizTypeEnum;
import com.oseasy.sys.modules.sys.service.BackTeacherExpansionService;
import com.oseasy.sys.modules.sys.service.StudentExpansionService;
import com.oseasy.sys.modules.sys.vo.UserBase;
import com.oseasy.sys.modules.sys.vo.UserEducation;
import com.oseasy.sys.modules.sys.vo.UserInfoVo;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 学生信息表Controller
 *
 * @author zy
 * @version 2017-03-27
 */
@Controller
@RequestMapping(value = "${frontPath}/sys/frontStudentExpansion")
public class FrontStudentExpansionController extends BaseController {
    public static final String FRONT_STUDENT = CoreSval.getFrontPath() + "/sys/frontStudentExpansion/findUserInfoById?id=";
	@Autowired
	private OfficeService officeService;
	@Autowired
	private StudentExpansionService studentExpansionService;
	@Autowired
	private UserService  userService;
	@Autowired
	private TeamService  teamService;
	@Autowired
	private BackTeacherExpansionService backTeacherExpansionService;
	@Autowired
	CoreService coreService;
	@Autowired
	SystemService systemService;

	private static final String HQYAZHENMA = "hqyazhenma";
	private final static String REG_TYPE_NO = "no";

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

	@RequestMapping(value = { "list", "" })
	public String list(StudentExpansion studentExpansion, HttpServletRequest request, HttpServletResponse response,Model model) {
//		studentExpansion.setIsFront("1");
//		studentExpansion.setIsOpen("1");
//		String currentId = UserUtils.getUser().getId();
//		studentExpansion.setTeamLeaderId(currentId);
//		Page<StudentExpansion> page = studentExpansionService.findPage(new Page<StudentExpansion>(request, response),studentExpansion);
//
//		Team team=new Team();
//		team.setSponsor(currentId);
//		team.setState("0");
//		List<Team> teams=teamService.findListByCreatorIdAndState(team);//建设中的团队
//		String stuFullTeams=getFullStarffedTeams(teams, 1);//学生满员的
//		if (StringUtil.isNotEmpty(currentId)&&teams!=null&&teams.size()>0) {
//			model.addAttribute("canInvite",true);
//        	for (StudentExpansion studentExp: page.getList()) {//每个被邀请人需单独判断
//        		studentExp.setCanInviteTeamIds(removeFullStarffedTeams(stuFullTeams, studentExp.getCanInviteTeamIds()));//剔除满员的
//        		if (StringUtil.isEmpty(studentExp.getCanInviteTeamIds())) {
//        			studentExp.setCanInvite(false);
//        		}else{
//        			studentExp.setCanInvite(true);
//        		}
//			}
//		}else{//没有可用于邀请的团队
//			model.addAttribute("canInvite", false);
//		}
//		model.addAttribute("page", page);
//		model.addAttribute("teams", teams);
		return SysSval.path.vms(SysEmskey.SYS.k()) + "front/frontStudentExpansionList";
	}

	@RequestMapping(value = "ajaxFrontStudentList", method = RequestMethod.GET, produces = "application/json")
   	@ResponseBody
   	public ApiResult ajaxFrontStudentList(StudentExpansion studentExpansion, HttpServletRequest request, HttpServletResponse response){
       try {
		   	HashMap<String, Object> hashMap = new HashMap<>();
		   	studentExpansion.setIsFront("1");
			studentExpansion.setIsOpen("1");
			String currentId = UserUtils.getUser().getId();
			studentExpansion.setTeamLeaderId(currentId);
			Page<StudentExpansion> page = studentExpansionService.findPage(new Page<StudentExpansion>(request, response),studentExpansion);

			Team team=new Team();
			team.setSponsor(currentId);
			team.setState("0");
			List<Team> teams=teamService.findListByCreatorIdAndState(team);//建设中的团队
			String stuFullTeams=getFullStarffedTeams(teams, 1);//学生满员的
			if (StringUtil.isNotEmpty(currentId)&&teams!=null&&teams.size()>0) {
				hashMap.put("canInvite",true);
				for (StudentExpansion studentExp: page.getList()) {//每个被邀请人需单独判断
					studentExp.setCanInviteTeamIds(removeFullStarffedTeams(stuFullTeams, studentExp.getCanInviteTeamIds()));//剔除满员的
					if (StringUtil.isEmpty(studentExp.getCanInviteTeamIds())) {
						studentExp.setCanInvite(false);
					}else{
						studentExp.setCanInvite(true);
					}
				}
			}else{//没有可用于邀请的团队
				hashMap.put("canInvite", false);

			}
			hashMap.put("page", page);
		   	hashMap.put("teams", teams);
           	return  ApiResult.success(hashMap);
       }catch (Exception e){
           logger.error(e.getMessage());
           return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
       }
   }

	private String removeFullStarffedTeams(String stuFullTeams,String canInviteTeamIds) {
		if (stuFullTeams==null) {
			return canInviteTeamIds;
		}
		if (StringUtil.isEmpty(canInviteTeamIds)) {
			return canInviteTeamIds;
		}
		List<String> canInviteTeamIdsList=new ArrayList<String>(Arrays.asList(canInviteTeamIds.split(",")));
		for(int i=0;i<canInviteTeamIdsList.size();i++) {
			if (stuFullTeams.contains(canInviteTeamIdsList.get(i))) {
				canInviteTeamIdsList.remove(i);
				i--;
			}
		}
		if (canInviteTeamIdsList!=null&&canInviteTeamIdsList.size()>0) {
			canInviteTeamIds=org.apache.commons.lang3.StringUtils.join(canInviteTeamIdsList,",");
		}else{
			canInviteTeamIds=null;
		}
		return canInviteTeamIds;
	}
	private String getFullStarffedTeams(List<Team> teams,int type) {
		if (teams!=null&&teams.size()>0) {
			List<String> list=new ArrayList<String>();
			for(Team t:teams) {
				if (type==1&&t.getUserCount()>=t.getMemberNum()) {//学生满
					list.add(t.getId());
				}else if (type==2&&t.getSchoolNum()>=t.getSchoolTeacherNum()) {//校内导师满
					list.add(t.getId());
				}else if (type==3&&t.getEnterpriseNum()>=t.getEnterpriseTeacherNum()) {//企业导师满
					list.add(t.getId());
				}
			}
			if (list.size()>0) {
				return org.apache.commons.lang3.StringUtils.join(list,",");
			}
		}
		return null;
	}

	@RequestMapping(value = "toInvite")
	@ResponseBody
	public JSONObject toInvite(String userIds,String userType,String teamId) {
		return studentExpansionService.toInvite(userIds, userType, teamId);
	}



	@RequestMapping(value = "invite")
	public String invite(StudentExpansion studentExpansion, Model model) {
		studentExpansionService.invite(studentExpansion);
		return CoreSval.REDIRECT+CoreSval.getFrontPath()+"/sys/frontStudentExpansion/?repage";
	}


	/**
	 * 个人信息查看和修改表单
	 * @param studentExpansion
	 * @param model
	 * @param request
	 * @return
	 */
	//@RequiresPermissions("sys:studentExpansion:view")
	@RequestMapping(value = "formPersonal")
	public String formPersonal(StudentExpansion studentExpansion, Model model,HttpServletRequest request) {
		String operateType = request.getParameter("operateType");
		model.addAttribute("operateType", operateType);
		model.addAttribute("studentExpansion", studentExpansion);
		List<Dict> dictList = DictUtils.getDictList(SysSval.DICT_TECHNOLOGY_FIELD);
		model.addAttribute("allDomains", dictList);
		return SysSval.path.vms(SysEmskey.SYS.k()) + "studentForm";
	}


	/**
	 * 个人信息保存表单
	 * @param studentExpansion
	 * @param model
	 * @param request
	 * @return
	 */
	//@RequiresPermissions("sys:studentExpansion:edit")
	@RequestMapping(value = "savePersonal")
	public String savePersonal(StudentExpansion studentExpansion,HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, studentExpansion)) {
			return formPersonal(studentExpansion, model,request);
		}
	    if (StringUtil.isNotBlank(studentExpansion.getId())) {
	    	if (StringUtil.isNotEmpty(studentExpansion.getUser().getName())) {//反转义名字
		 		studentExpansion.getUser().setName(StringEscapeUtils.unescapeHtml4(studentExpansion.getUser().getName()));
		 	}
			studentExpansionService.updateAll(studentExpansion);
		}else {

		}
	    String[] arrUrl= request.getParameterValues("arrUrl");
		if (arrUrl!=null&&arrUrl.length>0) {
			for(int i=0;i<arrUrl.length;i++) {
				 try {
					String moveEnd=FtpUtil.moveFile( arrUrl[i]);
					arrUrl[i]=moveEnd;
				} catch (IOException e) {
					e.printStackTrace();
				}
				User user=studentExpansion.getUser();
				if (user!=null) {
					user.setPhoto(arrUrl[i]);
					userService.updateUser(user);
				}
			}
		}
		return "success";

	}
	/**
	 * 查看当前登录 学生信息
	 * @return
	 */
	@RequestMapping(value="findUserInfoById")
	public String findUserInfoById(String custRedict,String okurl,String backurl,Model model,HttpServletRequest request) {
//		if (StringUtil.isEmpty(custRedict)) {
//			custRedict=(String)model.asMap().get("custRedict");
//			okurl=(String)model.asMap().get("okurl");
//			backurl=(String)model.asMap().get("backurl");
//		}
//		if ("1".equals(custRedict)) {
//			if (StringUtil.isEmpty(okurl)) {
//				String reqreferer=request.getHeader("referer");
//				if (reqreferer.contains("/infoPerfect")) {
//					okurl="/f";
//				}else{
//					okurl=reqreferer;
//				}
//				backurl=reqreferer;
//			}
//
//		}
//		model.addAttribute("custRedict",custRedict);
//		model.addAttribute("okurl",okurl);
//		model.addAttribute("backurl",backurl);
//		String reqreferer = referrer;
//		if (StringUtil.isEmpty(referrer)) {
//			reqreferer=request.getHeader("referer");
//		}else{
//			try {
//				reqreferer=URLDecoder.decode(StringEscapeUtils.unescapeHtml4(reqreferer), "utf-8");
//			} catch (UnsupportedEncodingException e) {
//				logger.error(e);
//			}
//		}
		User user=UserUtils.getUser();
//		if (UserUtils.checkHasRole(user, RoleBizTypeEnum.DS)) {
//			String teachId = backTeacherExpansionService.findTeacherIdByUser(user.getId());
//			return CoreSval.REDIRECT+CoreSval.getFrontPath()+"/sys/frontTeacherExpansion/form?id="+teachId+(custRedict==null?"":"&custRedict="+custRedict);
//		}
//		StudentExpansion studentExpansion=studentExpansionService.getByUserId(String.valueOf(user.getId()));//查出用户基本信息
//		if (studentExpansion!=null) {
//			studentExpansion.setUser(UserUtils.get(studentExpansion.getUser().getId()));
//			if (studentExpansion.getUser() !=null) {
//        if (StringUtil.isEmpty(studentExpansion.getUser().getSex())) {
//          studentExpansion.getUser().setSex(CoreSval.YES);
//        }
//      }
//			if (studentExpansion.getUser()!=null && studentExpansion.getUser().getPhoto()!=null && !studentExpansion.getUser().getPhoto().equals("")) {
//				model.addAttribute("user", studentExpansion.getUser());
//			}
//			if (studentExpansion.getUser()!=null&&
//					studentExpansion.getUser().getOffice()!=null&&StringUtil.isNotEmpty(studentExpansion.getUser().getOffice().getId())) {
//			Office office = officeService.get(studentExpansion.getUser().getOffice().getId());
//			if (office!=null) {
//				model.addAttribute("officeName", office.getName());
//				model.addAttribute("officeId", office.getId());
//			}
//			}
//		}
//		List<ProjectExpVo> projectExpVo=studentExpansionService.findProjectByStudentId(user.getId());//查询项目经历
//		List<GContestUndergo> gContest=studentExpansionService.findGContestByStudentId(user.getId()); //查询大赛经历
//		model.addAttribute("projectExpVo", projectExpVo);
//		model.addAttribute("gContestExpVo", gContest);
		model.addAttribute("cuser", user.getId());
		model.addAttribute("isEdit", request.getParameter("isEdit"));

//		model.addAttribute("studentExpansion", studentExpansion);
//
//		SysConfigVo scv=SysConfigUtil.getSysConfigVo();
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//		if(scv!=null&&StringUtil.isNotEmpty(scv.getGraduateMonth())){
//			try {
//				sdf.parse("2018-"+scv.getGraduateMonth()+"-01");//校验用
//				model.addAttribute("graduateMonth", scv.getGraduateMonth());
//			} catch (ParseException e) {
//				logger.error("毕业月份参数错误");
//			}
//		}
//		model.addAttribute("curDate", sdf.format(new Date()));
//		List<Dict> dictList = DictUtils.getDictList(ItDnStudent.DICT_TECHNOLOGY_FIELD);
//		model.addAttribute("allDomains", dictList);
//
//		if ((studentExpansion != null) && (studentExpansion.getUser() != null)) {
//	  		/*查询谁看过它*/
//	  		model.addAttribute("visitors", sysViewsService.getVisitors(user.getId()));
//	  		/*查询我看过谁*/
//	  		model.addAttribute("browse", sysViewsService.getBrowse(user.getId()));
//		}
		return SysSval.path.vms(SysEmskey.SYS.k()) + "front/frontUserInfo";
	}

	/**
	 * 修改密码跳转页面 addBy zhangzheng
	 * @return
	 */
	@RequestMapping(value="frontUserPassword")
	public String frontUserPassword(Model model) {
		User user=UserUtils.getUser();
		model.addAttribute("user", user);
//		if (UserUtils.checkHasRole(user, RoleBizTypeEnum.XS)) {
//			StudentExpansion studentExpansion=studentExpansionService.getByUserId(String.valueOf(user.getId()));//查出用户基本信息
//			if (studentExpansion!=null) {
//				model.addAttribute("user", studentExpansion.getUser());
//				if (studentExpansion.getUser().getPhoto()!=null && !studentExpansion.getUser().getPhoto().equals("")) {
//					model.addAttribute("user", studentExpansion.getUser());
//				}
//			}
//			model.addAttribute("studentExpansion", studentExpansion);
//		}
//		if (UserUtils.checkHasRole(user, RoleBizTypeEnum.DS)) {
//			BackTeacherExpansion studentExpansion=backTeacherExpansionService.getByUserId(String.valueOf(user.getId()));//查出用户基本信息
//			if (studentExpansion!=null) {
//				model.addAttribute("user", studentExpansion.getUser());
//				if (studentExpansion.getUser().getPhoto()!=null && !studentExpansion.getUser().getPhoto().equals("")) {
//					model.addAttribute("user", studentExpansion.getUser());
//				}
//			}
//			model.addAttribute("studentExpansion", studentExpansion);
//		}
		String url=SysSval.path.vms(SysEmskey.SYS.k()) + "front/frontUserPassword";
		return url;
	}

	/**
	 * 修改密码跳转页面 addBy chenh
	 * @return
	 */
	@RequestMapping(value="frontUserMobile")
	public String frontUserMobile(Model model) {
	  	User user=UserUtils.getUser();
		model.addAttribute("user", user);
//	  if (UserUtils.checkHasRole(user, RoleBizTypeEnum.XS)) {
//		  StudentExpansion studentExpansion=studentExpansionService.getByUserId(String.valueOf(user.getId()));//查出用户基本信息
//		  if (studentExpansion!=null) {
//		    if (studentExpansion.getUser().getPhoto()!=null && !studentExpansion.getUser().getPhoto().equals("")) {
//		      model.addAttribute("user", studentExpansion.getUser());
//		    }
//		  }
//		  model.addAttribute("studentExpansion", studentExpansion);
//	  }
//	  if (UserUtils.checkHasRole(user, RoleBizTypeEnum.DS)) {
//			BackTeacherExpansion studentExpansion=backTeacherExpansionService.getByUserId(String.valueOf(user.getId()));//查出用户基本信息
//			if (studentExpansion!=null) {
//				if (studentExpansion.getUser().getPhoto()!=null && !studentExpansion.getUser().getPhoto().equals("")) {
//					model.addAttribute("user", studentExpansion.getUser());
//				}
//			}
//			model.addAttribute("studentExpansion", studentExpansion);
//		}
	  return SysSval.path.vms(SysEmskey.SYS.k()) + "front/frontUserMobile";
	}

	/**
	 * 修改密码 addBy zhangzheng
	 * @return
	 */
	@RequestMapping(value="updatePassWord")
	public String updatePassWord(String oldPassword, String newPassword, Model model,RedirectAttributes redirectAttributes) {
		User user = UserUtils.getUser();
		if (StringUtil.isNotBlank(oldPassword) && StringUtil.isNotBlank(newPassword)) {
			if (CoreUtils.validatePassword(oldPassword, user.getPassword())) {
				if (oldPassword.equals(newPassword)) {
					redirectAttributes.addFlashAttribute("message","修改密码失败，新密码不能与原密码一致");
					redirectAttributes.addFlashAttribute("type","0");
					return CoreSval.REDIRECT+CoreSval.getFrontPath()+"/sys/frontStudentExpansion/frontUserPassword?repage";
				}
				coreService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
				if (SysUserUtils.checkInfoPerfect(user)) {
					redirectAttributes.addFlashAttribute("message","修改密码成功,请继续完善个人信息");
					redirectAttributes.addFlashAttribute("type","1");
				}else{
					redirectAttributes.addFlashAttribute("message","修改密码成功");
					redirectAttributes.addFlashAttribute("type","1");
				}
				if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XS)) {
					return CoreSval.REDIRECT+CoreSval.getFrontPath()+"/sys/frontStudentExpansion/findUserInfoById?repage";
				}else{
					return CoreSval.REDIRECT+CoreSval.getFrontPath()+"/sys/frontTeacherExpansion/form?id="+backTeacherExpansionService.getByUserId(user.getId()).getId();
				}
			}else{
				redirectAttributes.addFlashAttribute("message","修改密码失败，旧密码错误");
				redirectAttributes.addFlashAttribute("type","0");
			}
		}
		return CoreSval.REDIRECT+CoreSval.getFrontPath()+"/sys/frontStudentExpansion/frontUserPassword?repage";
	}

	/**
	 * 修改密码 addBy 王清腾
	 * @return
	 */
	@RequestMapping(value="ajaxUpdatePassWord/{id}",  method = RequestMethod.POST, produces = "application/json" )
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

	/**
	 * 修改邮箱 addBy wqt
	 * @return
	 */

	private String getVerCode(HttpServletRequest request, String sruyazhengma) {
		String hqyazhengma = (String) CacheUtils.get(HQYAZHENMA + request.getSession().getId(), HQYAZHENMA + request.getSession().getId());
		logger.info("VerCode get key:" + HQYAZHENMA + request.getSession().getId()  + " , " + HQYAZHENMA + request.getSession().getId() + " = " + hqyazhengma + "-->"+sruyazhengma.equals(hqyazhengma));
		return hqyazhengma;
	}

	@RequestMapping(value = "/checkEmailVerityCode", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public Boolean checkCode(HttpServletRequest request, String code, String regType) {
		if (regType.equals(REG_TYPE_NO)) {
			return request.getSession().getAttribute(SysValCode.VKEY).toString().equals(code.toUpperCase());
		} else {
			String hqyazhengma = getVerCode(request, code);
			return hqyazhengma.equals(code);
		}
	}
	/**
	 * 修改手机号 addBy wqt
	 * @return
	 */
	@RequestMapping(value="updateUserMobile", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiTstatus<Boolean> updateUserMobile(String userId, String mobile, RedirectAttributes redirectAttributes) {
		ApiTstatus<Boolean> actYwRstatus = new ApiTstatus<Boolean>(false, "修改手机号失败");
		User user = UserUtils.getUser();
		if (StringUtil.isNotBlank(mobile) ) {
			user.setMobile(mobile);
			userService.updateMobile(user);
			actYwRstatus.setStatus(true);
			actYwRstatus.setMsg("修改手机号成功");
			actYwRstatus.setDatas(true);
		}
		return actYwRstatus;
	}

	@RequestMapping(value="updateMobile")
	public String updateMobile(String mobile ,Model model,RedirectAttributes redirectAttributes) {
		User user = UserUtils.getUser();
		if (StringUtil.isNotBlank(mobile) ) {
			user.setMobile(mobile);
			userService.updateMobile(user);
			redirectAttributes.addFlashAttribute("message","修改手机号成功");
			redirectAttributes.addFlashAttribute("type","1");
		}
		if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XS)) {
			return CoreSval.REDIRECT+CoreSval.getFrontPath()+"/sys/frontStudentExpansion/findUserInfoById?repage";
		}else{
			return CoreSval.REDIRECT+CoreSval.getFrontPath()+"/sys/frontTeacherExpansion/form?id="+backTeacherExpansionService.getByUserId(user.getId()).getId();
		}
	}





	//修改信息
	@RequestMapping(value="updateUserInfo")
	public String updateUserInfo(String custRedict,String okurl,String backurl,HttpServletRequest request,Model model,StudentExpansion studentExpansion, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("custRedict", custRedict);
		redirectAttributes.addFlashAttribute("okurl", okurl);
		redirectAttributes.addFlashAttribute("backurl", backurl);
		String[] str=request.getParameterValues("user.domainIdList");
		User suser=studentExpansion.getUser();
		if(str==null){
			suser.setDomainIdList(null);
			suser.setDomain(null);
		}
		if (StringUtil.isNotBlank(studentExpansion.getId())) {
			String enterdatedate=(String)request.getParameter("enterdate");
			DateFormat df = new SimpleDateFormat("yyyy-MM");
			try {
				Date enDate = df.parse(enterdatedate);
				studentExpansion.setEnterdate(enDate);
			} catch (ParseException e) {
				logger.error(e.getMessage());
			}

			String companyId = officeService.selelctParentId(studentExpansion.getUser().getOffice().getId());
		 	studentExpansion.getUser().setCompany(new Office());
		 	studentExpansion.getUser().getCompany().setId(companyId);
		 	if (StringUtil.isNotEmpty(studentExpansion.getUser().getName())) {//反转义名字
		 		studentExpansion.getUser().setName(StringEscapeUtils.unescapeHtml4(studentExpansion.getUser().getName()));
		 	}
		 	studentExpansionService.updateAll(studentExpansion);
			addMessage(redirectAttributes, "修改学生信息成功");
		}

	 	User user=UserUtils.getUser();
	 	if (user!=null) {
	 		CacheUtils.remove(CoreUtils.USER_CACHE, CoreUtils.USER_CACHE_ID_ + user.getId());
			CacheUtils.remove(CoreUtils.USER_CACHE, CoreUtils.USER_CACHE_LOGIN_NAME_ + user.getLoginName());
		}
		return CoreSval.REDIRECT+CoreSval.getFrontPath()+"/sys/frontStudentExpansion/findUserInfoById/?repage";
	}

	@RequestMapping(value="uploadFTP")
	public String uploadFTP(HttpServletRequest request) {
		String arrUrl = request.getParameter("arrUrl");
		User user=UserUtils.getUser();
		if (user!=null) {
			user.setPhoto(arrUrl);
			userService.updateUser(user);
		}
		return "1";

	}

	/*******************************************************************************************************************************
	 * Ajax异步获取信息.
	 *******************************************************************************************************************************/
    /**
     * 修改个人信息.
//     * @param type 0标识修改全部，1标识修改基本信息，2标识修改拓展信息
     * @param gps
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxUpdateUserInfo", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<StudentExpansion> ajaxUpdateUserInfo(@RequestBody JSONObject gps) {
        Map<String, Class> classMap = new HashMap<String, Class>();
        classMap.put(UserInfoVo.UI_BASE, UserBase.class);
        classMap.put(UserInfoVo.UI_EDUCATION, UserEducation.class);
        //JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {DateUtil.FMT_YYYYMM_ZG}));
        UserInfoVo userInfoVo = (UserInfoVo) JSONObject.toBean(gps, UserInfoVo.class, classMap);
        userInfoVo = UserInfoVo.dealDate(gps, userInfoVo);
        StudentExpansion studentExpansion = null;
        if((userInfoVo != null) && (userInfoVo.getEducation() != null) && StringUtil.isNotEmpty(userInfoVo.getEducation().getId())){
            studentExpansion = studentExpansionService.get(userInfoVo.getEducation().getId());
        }

        studentExpansion = UserInfoVo.convert(studentExpansion, userInfoVo);
        if((userInfoVo.getBase() != null)){
        	if((studentExpansion.getUser().getOffice() != null) && StringUtil.isNotEmpty(studentExpansion.getUser().getOffice().getId())){
				String companyId = officeService.selelctParentId(studentExpansion.getUser().getOffice().getId());
				studentExpansion.getUser().setCompany(new Office());
				studentExpansion.getUser().getCompany().setId(companyId);
			}
            if (StringUtil.isNotEmpty(studentExpansion.getUser().getName())) {//反转义名字
                studentExpansion.getUser().setName(StringEscapeUtils.unescapeHtml4(studentExpansion.getUser().getName()));
            }
        }
//        if(userInfoVo.getEducation() != null){
//			if (studentExpansion.getEnterdate() == null) {
//				return new ActYwRstatus<StudentExpansion>(false, "入学年份不能为空");
//			}
//		}

        if((userInfoVo.getBase() != null) && (userInfoVo.getEducation() == null)){
            userService.updateUser(studentExpansion.getUser());
        }else if((userInfoVo.getBase() == null) && (userInfoVo.getEducation() != null)){
            studentExpansionService.save(studentExpansion);
        }else if((userInfoVo.getBase() != null) && (userInfoVo.getEducation() != null)){
            studentExpansionService.updateAll(studentExpansion);
        }

        User user=UserUtils.getUser();
        if (user!=null) {
            CacheUtils.remove(CoreUtils.USER_CACHE, CoreUtils.USER_CACHE_ID_ + user.getId());
            CacheUtils.remove(CoreUtils.USER_CACHE, CoreUtils.USER_CACHE_LOGIN_NAME_ + user.getLoginName());
        }
        return new ApiTstatus<StudentExpansion>(true, "修改学生信息成功", studentExpansionService.get(studentExpansion));
    }

    @RequestMapping(value="getStudentExpansion", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult getStudentExpansion(@RequestParam(required=true) String userId){
    	return ApiResult.success(SysUserUtils.getStudentByUserId(userId));
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
}
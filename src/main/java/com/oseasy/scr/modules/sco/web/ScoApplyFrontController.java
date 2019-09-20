package com.oseasy.scr.modules.sco.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.sys.modules.sys.service.StudentExpansionService;
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
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.scr.common.config.ScrSval;
import com.oseasy.scr.common.config.ScrSval.ScrEmskey;
import com.oseasy.scr.modules.sco.entity.ScoApply;
import com.oseasy.scr.modules.sco.entity.ScoAuditing;
import com.oseasy.scr.modules.sco.entity.ScoCourse;
import com.oseasy.scr.modules.sco.service.ScoApplyService;
import com.oseasy.scr.modules.sco.service.ScoAuditingService;
import com.oseasy.scr.modules.sco.service.ScoCourseService;
import com.oseasy.scr.modules.scr.vo.ScoRstatus;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 学分课程申请Controller.
 * @author zhangzheng
 * @version 2017-07-13
 */
@Controller
@RequestMapping(value = "${frontPath}/scoapply")
public class ScoApplyFrontController extends BaseController {

	@Autowired
	private ScoApplyService scoApplyService;
	@Autowired
	ScoCourseService scoCourseService;
	@Autowired
	StudentExpansionService studentExpansionService;
	@Autowired
	SysAttachmentService sysAttachmentService;
	@Autowired
	ScoAuditingService scoAuditingService;


	@ModelAttribute
	public ScoApply get(@RequestParam(required=false) String id) {
		ScoApply entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = scoApplyService.get(id);
		}
		if (entity == null) {
			entity = new ScoApply();
		}
		return entity;
	}

	//课程学分认定列表
	@RequestMapping(value = "scoApplyList")
	public String scoApplyList(ScoApply scoApply, HttpServletRequest request, HttpServletResponse response, Model model) {
		scoApply.setUserId(UserUtils.getUser().getId());
		Page<ScoApply> page = scoApplyService.findPage(new Page<ScoApply>(request, response), scoApply);
		if(UserUtils.getUser().getRoleList()!=null){
			for(Role role:UserUtils.getUser().getRoleList()){
				if(role.getRtype().equals(CoreSval.Rtype.STUDENT.getKey())){
					model.addAttribute("isStu", "1");
					break;
				}
			}
		}
		model.addAttribute("page", page);
		return ScrSval.path.vms(ScrEmskey.SCO.k()) + "scoApply/scoApplyList";
	}

	@RequestMapping(value = "getScoApplyList", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult getScoApplyList(ScoApply scoApply, HttpServletRequest request, HttpServletResponse response){
		scoApply.setUserId(UserUtils.getUser().getId());
		Page<ScoApply> page = scoApplyService.findPage(new Page<ScoApply>(request, response), scoApply);
		return ApiResult.success(page);
	}


	//申请认定课程  编辑认定课程 跳转页面
	@RequestMapping(value = "scoApplyForm")
	public String scoApplyForm(ScoApply scoApply, Model model) {
		User user= UserUtils.getUser();
        Date date =new Date();
		StudentExpansion studentExpansion = studentExpansionService.getByUserId(user.getId());
		model.addAttribute("user", user);
		model.addAttribute("studentExpansion", studentExpansion);
		model.addAttribute("date", date);
		ScoCourse scoCourse = scoCourseService.get(scoApply.getCourseId());

		model.addAttribute("scoApply", scoApply);
		model.addAttribute("scoCourse", scoCourse);

		if (StringUtil.isNotBlank(scoApply.getId())) {
			//查找附件
			SysAttachment sysAttachment = new SysAttachment();
			sysAttachment.setUid(scoApply.getId());
			List<SysAttachment> attachmentList = sysAttachmentService.findList(sysAttachment);
			scoApply.setAttachmentList(attachmentList);
			//查找审核记录
			ScoAuditing scoAuditing = new ScoAuditing();
			scoAuditing.setApplyId(scoApply.getId());
			List<ScoAuditing> scoAuditingList = scoAuditingService.findList(scoAuditing);
			model.addAttribute("scoAuditingList", scoAuditingList);
		}



		return ScrSval.path.vms(ScrEmskey.SCO.k()) + "scoApply/scoApplyForm";
	}

	//添加认定课程
	@RequestMapping(value = "scoApplyAdd")
	public String scoApplyAdd(ScoApply scoApply, Model model) {
		ScoCourse scoCourse =new ScoCourse();
		model.addAttribute("scoCourse", scoCourse);
		return ScrSval.path.vms(ScrEmskey.SCO.k()) + "scoApply/scoApplyAdd";
	}

	//根据课程名或者课程代码查询学分课程
	@RequestMapping(value = "findListByNameOrCode")
	@ResponseBody
	public List<ScoCourse> findListByNameOrCode(HttpServletRequest request) {
		String keyword = request.getParameter("keyword");
		List<ScoCourse> scoCourseList = scoCourseService.findListByNameOrCode(keyword);
		return scoCourseList;
	}
	//根据课程性质、课程类型、专业科别、课程名或者课程代码查询学分课程
	@RequestMapping(value = "findCourseList")
	@ResponseBody
	public List<ScoCourse> findCourseList(ScoCourse scoCourse) {
		List<ScoCourse> scoCourseList = scoCourseService.findCourseList(scoCourse);
		return scoCourseList;
	}

	//保存添加申请
	@RequestMapping(value = "saveAdd")
	@ResponseBody
	public Map<String,Object> saveAdd(ScoApply scoApply) {
		Map<String,Object> map=new HashMap<String,Object>();

		//保存时，先判断课程是否在后台被删除。
		String courseId = scoApply.getCourseId();
		ScoCourse scoCourse = scoCourseService.get(courseId);
		if (scoCourse==null) {
			map.put("success",false);
			map.put("msg","保存失败，该课程被删除，请重新选择");
		}else{
			//判断该课程有没有被添加
			ScoApply scoApplyForSeach =new  ScoApply();
			scoApplyForSeach.setUserId(UserUtils.getUser().getId());
			scoApplyForSeach.setCourseId(courseId);
			List<ScoApply> applyList =  scoApplyService.findList(scoApplyForSeach);
			if (applyList!=null&&applyList.size()>0) {
				map.put("success",false);
				map.put("msg","保存失败，该课程已经被添加");
			}else{
				scoApply.setUserId(UserUtils.getUser().getId());
				scoApply.setAuditStatus("1"); //待提交认定
				scoApplyService.add(scoApply);
				map.put("success",true);
				map.put("msg","添加认定课程成功，是否继续添加？");
			}
		}
		return map;
	}
	//保存认定申请
	@RequestMapping(value = "saveForm")
	@ResponseBody
	public ApiResult saveForm(@RequestBody ScoApply scoApply) {
		try{
			scoApply.setApplyDate(new Date());
			scoApply.setAuditStatus(ScoRstatus.SRS_DSH.getKey());
			scoApplyService.save(scoApply);
			return ApiResult.success();
		}
		catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
		}
	}

	//查看详情
	@RequestMapping(value = "view")
	public String view(ScoApply scoApply, Model model) {
		User user= UserUtils.getUser();

		StudentExpansion studentExpansion = studentExpansionService.getByUserId(user.getId());
//		model.addAttribute("user", user);
		model.addAttribute("studentExpansion", studentExpansion);
		ScoCourse scoCourse = scoCourseService.get(scoApply.getCourseId());

		model.addAttribute("scoApply", scoApply);
		model.addAttribute("scoCourse", scoCourse);
		//查找附件
		SysAttachment sysAttachment = new SysAttachment();
		sysAttachment.setUid(scoApply.getId());
		List<SysAttachment> attachmentList = sysAttachmentService.findList(sysAttachment);
		scoApply.setAttachmentList(attachmentList);
//		//查找审核记录
//		ScoAuditing scoAuditing = new ScoAuditing();
//		scoAuditing.setApplyId(scoApply.getId());
//		List<ScoAuditing> scoAuditingList = scoAuditingService.findList(scoAuditing);
//		model.addAttribute("scoAuditingList", scoAuditingList);
		return ScrSval.path.vms(ScrEmskey.SCO.k()) + "scoApply/scoApplyView";
	}

	@RequestMapping(value = "getCAAuditList/{id}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult getCAAuditList(@PathVariable String id){
		try {
			ScoAuditing scoAuditing = new ScoAuditing();
			scoAuditing.setApplyId(id);
			List<ScoAuditing> scoAuditingList = scoAuditingService.findList(scoAuditing);
			return ApiResult.success(scoAuditingList);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
		}
	}

	@RequestMapping(value = "delete")
	public String delete(ScoApply scoApply, RedirectAttributes redirectAttributes) {
		scoApplyService.delete(scoApply);
		addMessage(redirectAttributes, "删除成功");
		return CoreSval.REDIRECT+CoreSval.getFrontPath()+"/scoapply/scoApplyList/?repage";
	}

}
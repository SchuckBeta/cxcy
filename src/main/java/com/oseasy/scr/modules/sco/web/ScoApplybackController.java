package com.oseasy.scr.modules.sco.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;

import com.oseasy.scr.common.config.ScrSval;
import com.oseasy.scr.common.config.ScrSval.ScrEmskey;
import com.oseasy.scr.modules.sco.entity.ScoAffirmCriterionCouse;
import com.oseasy.scr.modules.sco.entity.ScoApply;
import com.oseasy.scr.modules.sco.entity.ScoAuditing;
import com.oseasy.scr.modules.sco.entity.ScoCourse;
import com.oseasy.scr.modules.sco.service.ScoAffirmCriterionCouseService;
import com.oseasy.scr.modules.sco.service.ScoApplyService;
import com.oseasy.scr.modules.sco.service.ScoAuditingService;
import com.oseasy.scr.modules.sco.service.ScoCourseService;
import com.oseasy.scr.modules.scr.entity.ScoRapply;
import com.oseasy.scr.modules.scr.service.ScoRapplyService;
import com.oseasy.scr.modules.scr.vo.ScoCreditType;
import com.oseasy.scr.modules.scr.vo.ScoQuery;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;
import com.oseasy.sys.modules.sys.service.StudentExpansionService;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 学分课程申请Controller.
 * @author zhangzheng
 * @version 2017-07-13
 */
@Controller
@RequestMapping(value = "${adminPath}/scoapply")
public class ScoApplybackController extends BaseController {

	@Autowired
	private ScoApplyService scoApplyService;
	@Autowired
	private ScoRapplyService scoRapplyService;
	@Autowired
	ScoCourseService scoCourseService;
	@Autowired
	StudentExpansionService studentExpansionService;
//	@Autowired
//	ProStudentExpansionService proStudentExpansionService;
	@Autowired
	SysAttachmentService sysAttachmentService;
	@Autowired
	ScoAuditingService scoAuditingService;
	@Autowired
	private SystemService systemService;
	@Autowired
	ScoAffirmCriterionCouseService scoAffirmCriterionCouseService;
	@RequestMapping(value = "getCountToAudit")
	@ResponseBody
	public Long getCountToAudit() {
		return scoApplyService.getCountToAudit();
	}

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
		model.addAttribute("page", page);
		return ScrSval.path.vms(ScrEmskey.SCO.k()) + "scoApply/scoApplyList";
	}

	//申请认定课程
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
	public boolean saveAdd(ScoApply scoApply) {
		scoApply.setUserId(UserUtils.getUser().getId());
		scoApply.setAuditStatus("1"); //待提交认定
		scoApplyService.save(scoApply);
		return true;
	}
	//课程审核
	@RequestMapping(value = "auditForm")
	public String auditForm(ScoApply scoApply,HttpServletRequest request) {
		String suggest=request.getParameter("suggest");
		scoApplyService.saveAudit(scoApply,suggest);
		return "redirect:/a/sco/scoreGrade/courseList/?repage";
	}

	@RequestMapping(value="saveAuditCourse", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult saveAuditCourse(@RequestBody ScoAuditing scoAuditing){
		try {
			ScoApply scoApply = scoApplyService.get(scoAuditing.getApplyId());
			scoApply.setId(scoAuditing.getApplyId());
			scoApply.setAuditStatus(scoAuditing.getAuditStatus());
			scoApply.setScore(Float.parseFloat(scoAuditing.getScoreVal()));
			scoApplyService.saveAudit(scoApply,scoAuditing.getSuggest());
			return ApiResult.success();
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
		}
	}

	//课程审核
	@RequestMapping(value = "ajaxAuditForm")
	@ResponseBody
	public JSONObject ajaxAuditForm(String  ids, HttpServletRequest request) {
		String[] idList=ids.split(",");
		JSONObject js=scoApplyService.saveAuditPl(idList);
		return js;
	}


	//查看详情
	@RequestMapping(value = "view")
	public String view(ScoApply scoApply, Model model) {
		String userId= scoApply.getUserId();
		if (userId!=null) {
			User user = systemService.getUser(userId);
			StudentExpansion studentExpansion = studentExpansionService.getByUserId(scoApply.getUserId());
			model.addAttribute("applyUser", user);
			model.addAttribute("studentExpansion", studentExpansion);
		}

		ScoCourse scoCourse = scoCourseService.get(scoApply.getCourseId());

		model.addAttribute("scoApply", scoApply);
		model.addAttribute("scoCourse", scoCourse);
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
		return ScrSval.path.vms(ScrEmskey.SCO.k()) + "grade/scoCourseGradeView";
	}

	//审核详情
	@RequestMapping(value = "auditView")
	public String auditView(ScoApply scoApply, Model model) {
		String userId= scoApply.getUserId();
		if (userId!=null) {
			User user = systemService.getUser(userId);
			StudentExpansion studentExpansion = studentExpansionService.getByUserId(scoApply.getUserId());
			model.addAttribute("applyUser", user);
			model.addAttribute("studentExpansion", studentExpansion);
		}

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
		//查询相应课程课时列表
		List<ScoAffirmCriterionCouse> couseNumList = scoAffirmCriterionCouseService.findListByFidCouseNum(scoApply.getCourseId());
		if (couseNumList!=null) {
			String autoScore=null;
			for(ScoAffirmCriterionCouse index:couseNumList) {
				int begin=Integer.valueOf(index.getStart());
				int end=Integer.valueOf(index.getEnd());
				if ((scoApply.getRealTime()<end || scoApply.getRealTime()== end) && (scoApply.getRealTime()>begin||scoApply.getRealTime()==begin)) {
					List<ScoAffirmCriterionCouse> couseScoreList = scoAffirmCriterionCouseService.findListByParentId(index.getId());
					if (couseScoreList!=null) {
						for(ScoAffirmCriterionCouse indexScore:couseScoreList) {
							int beginScore=Integer.valueOf(indexScore.getStart());
							int endScore=Integer.valueOf(indexScore.getEnd());
							if ((scoApply.getRealScore()<endScore || scoApply.getRealScore()== endScore) &&
									(scoApply.getRealScore()>beginScore|| scoApply.getRealScore()== beginScore)) {
								autoScore=indexScore.getScore();
								model.addAttribute("autoScore", autoScore);
							}
							if (StringUtil.isNotEmpty(autoScore)) {
								break;
							}

						}
					}
				}
				if (StringUtil.isNotEmpty(autoScore)) {
					break;
				}
			}
		}
		//查询课程课时分段列表


		return ScrSval.path.vms(ScrEmskey.SCO.k()) + "grade/scoCourseGradeAuditView";
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

	//删除学分申请接口
	@RequestMapping(value = "ajaxDeleteScoRapply")
	@ResponseBody
	public ApiResult ajaxDeleteScoRapply(ScoQuery scoQuery){
		try{
			//创新创业学分类型申请
			if(scoQuery.getCreditType().equals(ScoCreditType.SCO_CREDIT.getKey())){
				ScoRapply scoRapply = new ScoRapply(scoQuery.getId());
				scoRapply.setDelFlag(Const.YES);
				scoRapplyService.delete(scoRapply);
			}
			//课程学分类型申请
			if(scoQuery.getCreditType().equals(ScoCreditType.SCO_COURSE.getKey())){
				ScoApply scoApply = new ScoApply(scoQuery.getId());
				scoApply.setDelFlag(Const.YES);
				scoApplyService.delete(scoApply);
			}
			return ApiResult.success();
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
		}

	}

}
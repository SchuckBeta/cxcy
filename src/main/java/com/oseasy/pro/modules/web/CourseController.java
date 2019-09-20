package com.oseasy.pro.modules.web;

import com.oseasy.pro.modules.course.entity.Course;
import com.oseasy.pro.modules.course.entity.CourseCategory;
import com.oseasy.pro.modules.course.entity.CourseTeacher;
import com.oseasy.pro.modules.course.service.CourseCategoryService;
import com.oseasy.pro.modules.course.service.CourseService;
import com.oseasy.pro.modules.course.service.CourseTeacherService;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.config.ProSval.ProEmskey;
import com.oseasy.util.common.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 课程主表Controller.
 * @author 张正
 * @version 2017-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/course")
public class CourseController extends BaseController {

	@Autowired
	private CourseService courseService;
	@Autowired
	CourseCategoryService courseCategoryService;
	@Autowired
	CourseTeacherService courseTeacherService;
	@Autowired
	SysAttachmentService sysAttachmentService;

	@ModelAttribute
	public Course get(@RequestParam(required=false) String id) {
		Course entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = courseService.get(id);
		}
		if (entity == null) {
			entity = new Course();
		}
		return entity;
	}

	@RequestMapping(value = {"list", ""})
	public String list(Course course, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Course> page = courseService.findPage(new Page<Course>(request, response), course);
		//专业课程分类处理
		for(Course courseItem:page.getList()) {
			List<CourseCategory>  categoryList =courseCategoryService.getByCourseId(courseItem.getId());
			courseItem.setCategoryList(categoryList);
			List<CourseTeacher> teacherList =  courseTeacherService.getByCourseId(courseItem.getId());  //查找授课老师
			courseItem.setTeacherList(teacherList);
		}

		model.addAttribute("page", page);

		return ProSval.path.vms(ProEmskey.COURSE.k()) + "courseList";
	}


	@RequestMapping(value = "form")
	public String form(Course course, Model model) {
		//查找所有老师
		List<CourseTeacher> teachers= courseService.findTeacherListForCourse();
		for (CourseTeacher teacher:teachers) {
			if (null == teacher.getPostName()) {
				teacher.setPostName("");
			}
			if (teacher.getCollegeName()==null) {
				teacher.setCollegeName("");
			}
		}
		model.addAttribute("teachers", teachers);
		//查找课程专业分类 授课老师  课件
		if (StringUtil.isNotBlank(course.getId())) {
			List<CourseCategory> categoryList = courseCategoryService.getByCourseId(course.getId());  //查找课程专业分类
			course.setCategoryList(categoryList);
			List<CourseTeacher> teacherList =  courseTeacherService.getByCourseId(course.getId());  //查找授课老师
			course.setTeacherList(teacherList);
			//查找课件
			SysAttachment sa=new SysAttachment();
			sa.setUid(course.getId());
			sa.setType(FileTypeEnum.S9);
			sa.setFileStep(FileStepEnum.S900);
			List<SysAttachment> attachmentList =  sysAttachmentService.getFiles(sa);
			course.setAttachmentList(attachmentList);
		}

		model.addAttribute("course", course);
		return ProSval.path.vms(ProEmskey.COURSE.k()) + "courseForm";
	}

	@RequestMapping(value = "save")
	public String save(Course course, Model model, RedirectAttributes redirectAttributes) {
		courseService.save(course);
		addMessage(redirectAttributes, "保存课程成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/course/?repage";
	}

	@RequestMapping(value="saveCourse", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult saveCourse(@RequestBody Course course){
		try {
			courseService.save(course);
			return ApiResult.success();
		}catch (Exception e){
			e.printStackTrace();
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequestMapping(value = "saveJson")
	@ResponseBody
	public boolean saveJson(Course course) {
		courseService.save(course);
		return true;
	}

	@RequestMapping(value = "delete")
	public String delete(Course course, RedirectAttributes redirectAttributes) {
		courseService.delete(course);
		addMessage(redirectAttributes, "删除课程成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/course/list?repage";
	}

	@RequestMapping(value="getCourseList", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult getCourseList(Course course, HttpServletRequest request, HttpServletResponse response){
		try{
			Page<Course> page = courseService.findPage(new Page<Course>(request, response), course);
			//专业课程分类处理
			for(Course courseItem:page.getList()) {
				List<CourseCategory>  categoryList =courseCategoryService.getByCourseId(courseItem.getId());
				courseItem.setCategoryList(categoryList);
				List<CourseTeacher> teacherList =  courseTeacherService.getByCourseId(courseItem.getId());  //查找授课老师
				courseItem.setTeacherList(teacherList);
			}
			return ApiResult.success(page);
		}catch (Exception e){
			e.printStackTrace();
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequestMapping(value="delCourse", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult delCourse(@RequestBody Course course){
		try{
			courseService.delete(course);
			return ApiResult.success();
		}catch (Exception e){
			e.printStackTrace();
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

}
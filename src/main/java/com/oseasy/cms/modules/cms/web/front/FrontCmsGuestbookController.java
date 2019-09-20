package com.oseasy.cms.modules.cms.web.front;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.oseasy.cms.common.config.CmsSval;
import com.oseasy.cms.common.config.CmsSval.CmsEmskey;
import com.oseasy.cms.modules.cms.entity.CmsGuestbook;
import com.oseasy.cms.modules.cms.service.CmsGuestbookService;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiParam;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.fileserver.modules.attachment.vo.SysAttVo;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.persistence.AttachMentEntity;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 留言Controller.
 * @author chenh
 * @version 2018-09-04
 */
@Controller
@RequestMapping(value = "${frontPath}/cms/cmsGuestbook")
public class FrontCmsGuestbookController extends BaseController {

	@Autowired
	private CmsGuestbookService cmsGuestbookService;
	@Autowired
	private SysAttachmentService sysAttachmentService;
    @Autowired
   	private OaNotifyService oaNotifyService;
   	@Autowired
   	private SystemService systemService;
   	@Autowired
   	private UserService userService;
	@ModelAttribute
	public CmsGuestbook get(@RequestParam(required=false) String id) {
		CmsGuestbook entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = cmsGuestbookService.get(id);
		}
		if (entity == null){
			entity = new CmsGuestbook();
		}
		return entity;
	}

    //@RequiresPermissions("user")
	@RequestMapping(value = {"list"})
	public String list(CmsGuestbook cmsGuestbook, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsGuestbook> page = cmsGuestbookService.findPage(new Page<CmsGuestbook>(request, response), cmsGuestbook);
		model.addAttribute(Page.PAGE, page);
		if(UserUtils.checkToLogin()){
            return CoreSval.LOGIN_REDIRECT;
		}

//		if(StringUtil.isNotEmpty(curUser.getUserType()) && (StringUtil.isEmpty(curUser.getName()) || StringUtil.isEmpty(curUser.getEmail()) || StringUtil.isEmpty(curUser.getMobile()))){
//		    if((EuserType.UT_C_STUDENT.getType()).equals(curUser.getUserType())){
//	            return "redirect:"+ FrontStudentExpansionController.FRONT_STUDENT + curUser.getId();
//		    }else if((EuserType.UT_C_TEACHER.getType()).equals(curUser.getUserType())){
//                return "redirect:"+ FrontTeacherExpansionController.FRONT_TEACHER + curUser.getId();
//            }
//		}
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "frontGuestbookList";
	}

//	@RequiresPermissions("cms:cmsGuestbook:view")
//	@RequestMapping(value = "form")
//	public String form(CmsGuestbook cmsGuestbook, Model model) {
//		model.addAttribute("cmsGuestbook", cmsGuestbook);
//		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "cmsGuestbookForm";
//	}

    @RequestMapping(value = "ajaxList")
    @ResponseBody
    public ApiResult ajaxlist(CmsGuestbook cmsGuestbook, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            Page<CmsGuestbook> page = cmsGuestbookService.findPage(new Page<CmsGuestbook>(request, response), cmsGuestbook);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value = "ajaxSave", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxSave(@RequestBody CmsGuestbook cmsGuestbook) {
        try {
//            if (!beanValidator(model, cmsGuestbook)){
//                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败！");
//            }
            cmsGuestbookService.save(cmsGuestbook);
            List<String> roles = new ArrayList<String>();
            Role role = systemService.getRole(CoreIds.NSC_SYS_ROLE_ADMIN.getId());
            if(role!=null){
                User user=UserUtils.getUser();
                roles = userService.findListByRoleId(role.getId());
                oaNotifyService.sendOaNotifyByTypeAndUser(user, roles, "留言管理",
                        user.getName()+"添加了新的留言", OaNotify.Type_Enum.TYPE23.getValue(), cmsGuestbook.getId());
            }
            if(StringUtil.checkNotEmpty(cmsGuestbook.getFiles()) && StringUtil.isNotEmpty(cmsGuestbook.getId())){
                AttachMentEntity fileVo = new AttachMentEntity();
                for (SysAttVo sysAtt : cmsGuestbook.getFiles()) {
                    if(fileVo.getFielSize() == null){
                        fileVo.setFielSize(Lists.newArrayList());
                    }
                    if(fileVo.getFielTitle() == null){
                        fileVo.setFielTitle(Lists.newArrayList());
                    }
                    if(fileVo.getFielType() == null){
                        fileVo.setFielType(Lists.newArrayList());
                    }
                    if(fileVo.getFielFtpUrl() == null){
                        fileVo.setFielFtpUrl(Lists.newArrayList());
                    }
                    if(fileVo.getFileTypeEnum() == null){
                        fileVo.setFileTypeEnum(Lists.newArrayList());
                    }
                    if(fileVo.getFileStepEnum() == null){
                        fileVo.setFileStepEnum(Lists.newArrayList());
                    }
                    if(StringUtil.isNotEmpty(sysAtt.getFielFtpUrl())){
                        fileVo.getFielSize().add(sysAtt.getFielSize());
                        fileVo.getFielTitle().add(sysAtt.getFielTitle());
                        fileVo.getFielType().add(sysAtt.getFielType());
                        fileVo.getFielFtpUrl().add(sysAtt.getFielFtpUrl());
                        fileVo.getFileTypeEnum().add(FileTypeEnum.S_GUEST_BOOK.getValue());
                        fileVo.getFileStepEnum().add(FileStepEnum.S_GUEST_BOOK.getValue());
                    }
                }
                sysAttachmentService.saveByVo(fileVo, cmsGuestbook.getId(), FileTypeEnum.S_GUEST_BOOK, FileStepEnum.S_GUEST_BOOK);
            }
            return ApiResult.success(cmsGuestbook);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value = "ajaxUpdateRec")
    @ResponseBody
    public ApiResult ajaxUpdateRec(CmsGuestbook cmsGuestbook, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if (StringUtil.isEmpty(cmsGuestbook.getId())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，ID标识不能为空！");
            }
            if (StringUtil.isEmpty(cmsGuestbook.getReContent())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败, 回复内容不能为空！");
            }
            if (StringUtil.isEmpty(cmsGuestbook.getReContent())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败, 回复内容不能为空！");
            }
            CmsGuestbook curCmsGuestbook = cmsGuestbookService.get(cmsGuestbook.getId());
            if((curCmsGuestbook.getReUser() == null) || StringUtil.isEmpty(curCmsGuestbook.getReUser().getId())){
                curCmsGuestbook.setReUser(UserUtils.getUser());
            }
            if((curCmsGuestbook.getReDate() == null)){
                curCmsGuestbook.setReDate(DateUtil.newDate());
            }
            cmsGuestbookService.save(cmsGuestbook);
            return ApiResult.success(cmsGuestbook);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value = "ajaxUpdateRecommend")
    @ResponseBody
    public ApiResult ajaxUpdateRecommend(CmsGuestbook cmsGuestbook, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if (StringUtil.isEmpty(cmsGuestbook.getId()) || StringUtil.isEmpty(cmsGuestbook.getIsRecommend())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，ID标识和操作不能为空！");
            }

            CmsGuestbook curCmsGuestbook = cmsGuestbookService.get(cmsGuestbook.getId());
            curCmsGuestbook.setIsRecommend(cmsGuestbook.getIsRecommend());
            cmsGuestbookService.save(curCmsGuestbook);
            return ApiResult.success(curCmsGuestbook);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }


    @ResponseBody
    @RequestMapping(value = "/ajaxUpdatePLAudit", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiResult ajaxUpdatePLAudit(@RequestBody ApiParam<List<CmsGuestbook>> api, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            List<CmsGuestbook> entitys = api.getDatas();
            if(StringUtil.checkEmpty(entitys)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，"+CoreJkey.JK_DATAS+"不能为空！");
            }
            cmsGuestbookService.updatePLAudit(entitys);
            return ApiResult.success(entitys);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }


    @RequestMapping(value = "ajaxDelete")
    @ResponseBody
    public ApiResult ajaxDelete(CmsGuestbook cmsGuestbook, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if (StringUtil.isEmpty(cmsGuestbook.getId())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，ID标识和操作不能为空！");
            }
            cmsGuestbookService.delete(cmsGuestbook);
            return ApiResult.success(cmsGuestbook);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value = "ajaxDeletePL", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxDeletePL(@RequestBody ApiParam<List<String>> api) {
        try {
            List<String> entitys = api.getDatas();
            if(StringUtil.checkEmpty(entitys)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，"+ CoreJkey.JK_DATAS+"不能为空！");
            }
            CmsGuestbook cmsGuestbook = new CmsGuestbook(entitys);
            cmsGuestbookService.deletePL(cmsGuestbook);
            return ApiResult.success(cmsGuestbook);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }
}
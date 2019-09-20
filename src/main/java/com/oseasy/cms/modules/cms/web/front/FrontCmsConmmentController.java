package com.oseasy.cms.modules.cms.web.front;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import com.oseasy.cms.modules.cms.entity.Category;
import com.oseasy.cms.modules.cms.entity.CmsConmment;
import com.oseasy.cms.modules.cms.entity.CmsConmmentLikes;
import com.oseasy.cms.modules.cms.service.CategoryService;
import com.oseasy.cms.modules.cms.service.CmsConmmentLikesService;
import com.oseasy.cms.modules.cms.service.CmsConmmentService;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiParam;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 评论Controller.
 * @author chenh
 * @version 2018-09-04
 */
@Controller
@RequestMapping(value = "${frontPath}/cms/cmsConmment")
public class FrontCmsConmmentController extends BaseController {

    @Autowired
    private CategoryService categoryService;
	@Autowired
	private CmsConmmentService cmsConmmentService;
	@Autowired
	private CmsConmmentLikesService cmsConmmentLikesService;

    @Autowired
   	private OaNotifyService oaNotifyService;
   	@Autowired
   	private SystemService systemService;
   	@Autowired
   	private UserService userService;

	@ModelAttribute
	public CmsConmment get(@RequestParam(required=false) String id) {
		CmsConmment entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = cmsConmmentService.get(id);
		}
		if (entity == null){
			entity = new CmsConmment();
		}
		return entity;
	}

	@RequestMapping(value = {"list"})
	public String list(CmsConmment cmsConmment, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsConmment> page = cmsConmmentService.findPage(new Page<CmsConmment>(request, response), cmsConmment);
		model.addAttribute(Page.PAGE, page);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "frontConmmentList";
	}

    @ResponseBody
    @RequestMapping(value = "ajaxList")
    public ApiResult ajaxlist(CmsConmment cmsConmment, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            Page<CmsConmment> page = cmsConmmentService.findPage(new Page<CmsConmment>(request, response), cmsConmment);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "ajaxListByCuser")
    public ApiResult ajaxListByCuser(CmsConmment cmsConmment, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            cmsConmment.setUser(UserUtils.getUser());
            Page<CmsConmment> page = cmsConmmentService.findPage(new Page<CmsConmment>(request, response), cmsConmment);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "ajaxSave", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiResult ajaxSave(@RequestBody CmsConmment cmsConmment, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if (!beanValidator(model, cmsConmment)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败！");
            }
            if ((cmsConmment.getCnt() == null) || StringUtil.isEmpty(cmsConmment.getCnt().getId()) || (cmsConmment.getCategory() == null) || StringUtil.isEmpty(cmsConmment.getCategory().getId())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+"文章、栏目标识不能为空！");
            }
            if((cmsConmment.getUser() == null) || StringUtil.isEmpty(cmsConmment.getUser().getId())){
                cmsConmment.setUser(UserUtils.getUser());
            }
            if((cmsConmment.getUser() == null) || StringUtil.isEmpty(cmsConmment.getUser().getId())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+" 当前用户不存在或者当前用户未登录！");
            }
            Category category = categoryService.get(cmsConmment.getCategory());
            if(category != null){
                if((Const.NO).equals(category.getIsAudit())){
                    cmsConmment.setAuditstatus(Const.YES);
                }
            }
            cmsConmmentService.save(cmsConmment);
            List<String> roles = new ArrayList<String>();
            Role role = systemService.getRole(CoreIds.NSC_SYS_ROLE_ADMIN.getId());
            if(role!=null){
                User user=UserUtils.getUser();
                roles = userService.findListByRoleId(role.getId());
                oaNotifyService.sendOaNotifyByTypeAndUser(user, roles, "评论管理",
                        user.getName()+"添加了新的评论", OaNotify.Type_Enum.TYPE22.getValue(), cmsConmment.getId());
            }
            return ApiResult.success(cmsConmment);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "ajaxUpdateReply", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiResult ajaxUpdateReply(@RequestBody CmsConmment cmsConmment, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if (StringUtil.isEmpty(cmsConmment.getId())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，ID标识不能为空！");
            }
            if (StringUtil.isEmpty(cmsConmment.getReply())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败, 回复内容不能为空！");
            }
            CmsConmment curCmsConmment = cmsConmmentService.get(cmsConmment.getId());
            if((curCmsConmment == null)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，ID标识不能为空！");
            }
            curCmsConmment.setReply(cmsConmment.getReply());
            curCmsConmment.setReUser(UserUtils.getUser());
            curCmsConmment.setReDate(DateUtil.newDate());
            cmsConmmentService.save(curCmsConmment);
            return ApiResult.success(curCmsConmment);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "ajaxUpdateLikes", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiResult ajaxUpdateLikes(@RequestBody CmsConmment cmsConmment, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if (StringUtil.isEmpty(cmsConmment.getId()) || (cmsConmment.getLikes() == null)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，ID标识和操作不能为空！");
            }

            if ((Const.Z1 != cmsConmment.getLikes()) && (Const.F1 != cmsConmment.getLikes())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，Likes只能为(1 点赞)或(-1 取消点赞)！");
            }
            CmsConmment curCmsConmment = cmsConmmentService.get(cmsConmment.getId());
            curCmsConmment.setLikes(curCmsConmment.getLikes() + cmsConmment.getLikes());
            cmsConmmentService.save(curCmsConmment);

            CmsConmmentLikes curCmsConmmentLikes = new CmsConmmentLikes();
            curCmsConmmentLikes.setUser(UserUtils.getUser());
            curCmsConmmentLikes.setParent(curCmsConmment);
            List<CmsConmmentLikes> cmsConmmentLikess = cmsConmmentLikesService.findList(curCmsConmmentLikes);
            if(StringUtil.checkEmpty(cmsConmmentLikess) && (Const.Z1 == cmsConmment.getLikes())){
                cmsConmmentLikesService.save(curCmsConmmentLikes);
            }else if(StringUtil.checkNotEmpty(cmsConmmentLikess) && (Const.F1 == cmsConmment.getLikes())){
                List<String> ids = cmsConmmentLikess.stream().map(e -> e.getId()).collect(Collectors.toList());
                curCmsConmmentLikes.setIds(ids);
                cmsConmmentLikesService.deleteWLPL(curCmsConmmentLikes);
            }
            return ApiResult.success(curCmsConmment);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "ajaxUpdateRecommend", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiResult ajaxUpdateRecommend(@RequestBody CmsConmment cmsConmment, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if (StringUtil.isEmpty(cmsConmment.getId()) || StringUtil.isEmpty(cmsConmment.getIsRecommend())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，ID标识和操作不能为空！");
            }

            CmsConmment curCmsConmment = cmsConmmentService.get(cmsConmment.getId());
            curCmsConmment.setIsRecommend(cmsConmment.getIsRecommend());
            cmsConmmentService.save(curCmsConmment);
            return ApiResult.success(curCmsConmment);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/ajaxUpdatePLRecommend", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiResult ajaxUpdatePLRecommend(@RequestBody ApiParam<List<CmsConmment>> api, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            List<CmsConmment> entitys = api.getDatas();
            if(StringUtil.checkEmpty(entitys)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，"+CoreJkey.JK_DATAS+"不能为空！");
            }
            cmsConmmentService.updatePLRecommend(entitys);
            return ApiResult.success(entitys);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/ajaxUpdatePLAudit", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiResult ajaxUpdatePLAudit(@RequestBody ApiParam<List<CmsConmment>> api, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            List<CmsConmment> entitys = api.getDatas();
            if(StringUtil.checkEmpty(entitys)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，"+CoreJkey.JK_DATAS+"不能为空！");
            }

            List<CmsConmment> curentitys = Lists.newArrayList();
            for (CmsConmment cmsConmment : entitys) {
                cmsConmment.setAuditDate(DateUtil.newDate());
                cmsConmment.setAuUser(UserUtils.getUser());
                curentitys.add(cmsConmment);
            }
            cmsConmmentService.updatePLAudit(curentitys);
            return ApiResult.success(curentitys);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/ajaxUpdatePLAuditByArticle", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiResult ajaxUpdatePLAuditByArticle(@RequestBody ApiParam<List<CmsConmment>> api, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            List<CmsConmment> entitys = api.getDatas();
            if(StringUtil.checkEmpty(entitys)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，"+CoreJkey.JK_DATAS+"不能为空！");
            }
            cmsConmmentService.updatePLAuditByCntIds(entitys);
            return ApiResult.success(entitys);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "ajaxDelete")
    public ApiResult ajaxDelete(CmsConmment cmsConmment, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            if (StringUtil.isEmpty(cmsConmment.getId())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，ID标识和操作不能为空！");
            }
            cmsConmmentService.delete(cmsConmment);
            return ApiResult.success(cmsConmment);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "ajaxDeletePL", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiResult ajaxDeletePL(@RequestBody ApiParam<List<String>> api) {
        try {
            List<String> entitys = api.getDatas();
            if(StringUtil.checkEmpty(entitys)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，"+CoreJkey.JK_DATAS+"不能为空！");
            }
            CmsConmment cmsConmment = new CmsConmment(entitys);
            cmsConmmentService.deletePL(cmsConmment);
            return ApiResult.success(cmsConmment);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "ajaxDeletePLByArticle", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiResult ajaxDeletePLByArticle(@RequestBody ApiParam<List<String>> api, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            List<String> entitys = api.getDatas();
            if(StringUtil.checkEmpty(entitys)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败，"+CoreJkey.JK_DATAS+"不能为空！");
            }
            cmsConmmentService.deletePLByCntIds(entitys);
            return ApiResult.success(entitys);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }
}
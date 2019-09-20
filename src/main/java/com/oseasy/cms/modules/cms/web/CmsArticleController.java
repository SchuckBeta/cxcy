package com.oseasy.cms.modules.cms.web;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.cms.common.config.CmsSval;
import com.oseasy.cms.common.config.CmsSval.CmsEmskey;
import com.oseasy.cms.modules.cms.entity.CmsArticle;
import com.oseasy.cms.modules.cms.entity.CmsArticleData;
import com.oseasy.cms.modules.cms.enums.CategoryModel;
import com.oseasy.cms.modules.cms.service.CmsArticleService;
import com.oseasy.cms.modules.cms.vo.ParamVo;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

import net.sf.json.JSONArray;


/**
 * 一般内容管理Controller.
 * @author liangjie
 * @version 2018-09-04
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/cmsArticle")
public class CmsArticleController extends BaseController {
	@Autowired
	private CmsArticleService cmsArticleService;

	@ModelAttribute
	public CmsArticle get(@RequestParam(required=false) String id) {
		CmsArticle entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = cmsArticleService.get(id);
		}
		if (entity == null){
			entity = new CmsArticle();
		}
		return entity;
	}

	@RequiresPermissions("cms:cmsArticle:view")
	@RequestMapping(value = {"list", ""})
	public String list(CmsArticle cmsArticle, HttpServletRequest request, HttpServletResponse response, Model model) {
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "cmsArticleList";
	}

	/**
	    文章点赞接口
	 */
	/*@RequestMapping(value="cmsArticleLikes")
	@ResponseBody
	public ApiResult  cmsArticleLikes(CmsArticleData cmsArticleData){
		try{
			cmsArticleService.updateArticleLikes(cmsArticleData);
			return ApiResult.success();
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}*/

	/**
	 	一般内容列表查询接口
	 */
	@RequestMapping(value="cmsArticleList")
	@ResponseBody

	public ApiResult cmsArticleList(CmsArticle cmsArticle, HttpServletRequest request, HttpServletResponse response){
		try{
			Page<CmsArticle> page = cmsArticleService.findNormalContentPage(new Page<CmsArticle>(request, response), cmsArticle);
			return ApiResult.success(page);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	/**
	 * 栏目模块接口
	 */
	@RequestMapping(value="categoryModel")
	@ResponseBody
	public ApiResult cmsArticleList(HttpServletRequest request, HttpServletResponse response){
		JSONArray jsonArray = new JSONArray();
		if("all".equals(request.getParameter("modelParam"))){
			for(CategoryModel c :CategoryModel.getCategoryModelList()){
				HashMap<String ,String> map = new HashMap<String ,String>();
				map.put("label",c.getModelName());
				map.put("value",c.getModelCode());
				jsonArray.add(map);
			}
		}else{
			CategoryModel c = CategoryModel.getCategoryModel(request.getParameter("modelParam"));
			HashMap<String ,String> map = new HashMap<String ,String>();
			map.put("label",c.getModelName());
			map.put("value",c.getModelCode());
			jsonArray.add(map);
		}
		return ApiResult.success(jsonArray);
	}



	/**
	 	根据id获取单条文章
	 */
	@RequestMapping(value="getOneCmsArticle")
	@ResponseBody
	public ApiResult getOneCmsArticle(CmsArticle cmsArticle, HttpServletRequest request, HttpServletResponse response){
		try{
			cmsArticle = cmsArticleService.get(cmsArticle);
			return ApiResult.success(cmsArticle);
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}


	//所有文章--评论
	@RequestMapping(value="articleInCommentList")
	@ResponseBody
	public ApiResult articleInCommentList(CmsArticle cmsArticle, HttpServletRequest request, HttpServletResponse response){
		try{

			Page<CmsArticle> page = cmsArticleService.articleInCommentList(new Page<CmsArticle>(request, response), cmsArticle);
			return ApiResult.success(page);
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	/**
	 	保存、修改接口
	 */
	@RequestMapping(value="editSaveCmsArticle", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiResult editSaveCmsArticle(@RequestBody CmsArticle cmsArticle, HttpServletRequest request, HttpServletResponse response){
		try{

			cmsArticleService.save(cmsArticle);
			return ApiResult.success();
		}
		catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	//同一个栏目章里，标题唯一。
	@RequestMapping(value="validateArticleName", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public  ApiResult validateArticleName(@RequestBody  CmsArticle cmsArticle){
		try{
			List<CmsArticle> list = cmsArticleService.validateArticleName(cmsArticle);
			if((list == null) || (list.size() <= 0)){
				return ApiResult.success();
			}else{
				return ApiResult.failed(ApiConst.CODE_MORE_ERROR,"标题已存在！");
			}
		}
		catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	/**
	 	修改排序、置顶、发布状态接口
	 */
	@RequestMapping(value="updateCmsArticle")
	@ResponseBody
	public  ApiResult updateCmsArticle(String ids,String tops,String sorts,String publishstatus){
		try {
			if (StringUtil.isNotBlank(tops)) {
				cmsArticleService.udpateTop(ids, tops);
			} else if (StringUtil.isNotBlank(sorts)) {
				cmsArticleService.udpateSort(ids, sorts);
			} else if (StringUtil.isNotBlank(publishstatus)) {
				cmsArticleService.udpatePublishStatus(ids, publishstatus);
			}
			return ApiResult.success();
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

    // 置顶
	@RequestMapping(value="editTopArticle", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiResult editTopArticle(@RequestBody CmsArticle cmsArticle, HttpServletRequest request, HttpServletResponse response){
		try{
			cmsArticleService.editTopArticle(cmsArticle);
			return ApiResult.success();
		}
		catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}


	/**
	 	删除接口
	 */
	@RequestMapping(value="deleteCmsArticle")
	@ResponseBody
	public ApiResult deleteCmsArticle(String ids){
		try{

			cmsArticleService.deleteCmsArticle(ids);
			return ApiResult.success();

		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());

		}
	}



	@RequiresPermissions("cms:cmsArticle:view")
	@RequestMapping(value = "form")
	public String form(CmsArticle cmsArticle, Model model) {
		model.addAttribute("cmsArticle", cmsArticle);
		model.addAttribute("systemDate", DateUtil.getDate(DateUtil.FMT_YYYYMMDD_ZG + " "+ DateUtil.FMT_HMS000));
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "cmsArticleForm";
	}

	@RequiresPermissions("cms:cmsArticle:edit")
	@RequestMapping(value = "save")
	public String save(CmsArticle cmsArticle, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cmsArticle)){
			return form(cmsArticle, model);
		}
		cmsArticleService.save(cmsArticle);
		addMessage(redirectAttributes, "保存一般内容管理成功");
		return "redirect:"+CoreSval.getAdminPath()+"/cms/cmsArticle/?repage";
	}

	@RequiresPermissions("cms:cmsArticle:edit")
	@RequestMapping(value = "delete")
	public String delete(CmsArticle cmsArticle, RedirectAttributes redirectAttributes) {
		cmsArticleService.delete(cmsArticle);
		addMessage(redirectAttributes, "删除一般内容管理成功");
		return "redirect:"+CoreSval.getAdminPath()+"/cms/cmsArticle/?repage";
	}

	@RequestMapping(value="getRelationList")
	@ResponseBody
	public ApiResult getRelationList(String ids){
		try{
			List<CmsArticle> relationList=cmsArticleService.getCmsArticleByIds(ids);
			return ApiResult.success(relationList);
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	/**
	 发布获奖接口
	 */
	@RequestMapping(value="excellent/publishStatus" , method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiResult publishStatus(@RequestBody ParamVo paramVo, HttpServletRequest request, HttpServletResponse response){
		try{
			cmsArticleService.publishStatus(paramVo.getIds(),paramVo.getPublishStatus());
			return ApiResult.success();
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}


	/**
		优秀项目修改保存接口
	 */
	@RequestMapping(value="excellent/updateSortArticle", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiResult updateSortArticle(@RequestBody CmsArticle cmsArticle, HttpServletRequest request, HttpServletResponse response){
		try{
			cmsArticleService.updateSortArticle(cmsArticle);
			return ApiResult.success();
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	/**
	 优秀项目修改保存批量排序
	 */
	@RequestMapping(value="excellent/updateSortsArticle", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiResult updateSortArticle(@RequestBody List<CmsArticle> list, HttpServletRequest request, HttpServletResponse response){
		try{
			for (CmsArticle aList : list) {
				cmsArticleService.updateSortArticle(aList);
			}
			return ApiResult.success();
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	/**
		优秀项目修改保存接口
	 */
	@RequestMapping(value="excellent/projectSave", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiResult projectSave(@RequestBody CmsArticle cmsArticle, HttpServletRequest request, HttpServletResponse response){
		try{
			cmsArticle.setPrType("1");
			cmsArticleService.save(cmsArticle);
			return ApiResult.success();
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	/**
		优秀项目删除接口
	 */
	@RequestMapping(value="excellent/projectDel")
	@ResponseBody
	public ApiResult projectDel(String ids, HttpServletRequest request, HttpServletResponse response){
		try{
			cmsArticleService.deletePl(ids);
			return ApiResult.success();
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	/**
		获奖修改保存接口
	 */
	@RequestMapping(value="excellent/gcontestSave", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiResult gcontestSave(@RequestBody CmsArticle cmsArticle, HttpServletRequest request, HttpServletResponse response){
		try{
			cmsArticle.setPrType("7");
			cmsArticleService.save(cmsArticle);
			return ApiResult.success();
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	/**
		大赛热点修改保存接口
	 */
	@RequestMapping(value="homeGcontestSave", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiResult homeGcontestSave(@RequestBody CmsArticle cmsArticle, HttpServletRequest request, HttpServletResponse response){
		try{
			//cmsArticle.setModule("0000000277");
			if(cmsArticle != null && StringUtil.isEmpty(cmsArticle.getCategoryId())){
				return ApiResult.failed(ApiConst.CMS_CATEGORY_NULL_ERROR.getCode());
			}
			cmsArticleService.save(cmsArticle);
			return ApiResult.success();
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	//获奖修改页面
	@RequestMapping(value = "homeGcontestForm")
	public String homeGcontestForm(CmsArticle cmsArticle, Model model) {
		CmsArticleData cmsArticleData=cmsArticle.getCmsArticleData();
		if(cmsArticleData!=null){
			model.addAttribute("cmsArticleData", cmsArticleData);
			if(StringUtil.isNotEmpty(cmsArticleData.getRelation())){
				List<CmsArticle> relationList=cmsArticleService.getCmsArticleByIds(cmsArticleData.getRelation());
				model.addAttribute("relationList", relationList);
			}
		}
		model.addAttribute("cmsArticle", cmsArticle);
		model.addAttribute("systemDate", DateUtil.getDate(DateUtil.FMT_YYYYMMDD_ZG + " "+ DateUtil.FMT_HMS000));
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "homeGcontestForm";
	}

	/**
		大赛热点列表查询接口
	 */
	@RequestMapping(value="homeGcontestList")
	@ResponseBody
	public ApiResult homeGcontestList(CmsArticle cmsArticle, HttpServletRequest request, HttpServletResponse response){
		cmsArticle.setModule("0000000277");
		try{
			return ApiResult.success(cmsArticleService.findPage(new Page<CmsArticle>(request, response),cmsArticle));
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}


	//获取modules
	@RequestMapping(value="getModuleList", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult getModuleList(){
		List<Dict> moduleList = DictUtils.getDictList("0000000274");
		return ApiResult.success(moduleList);
	}

}
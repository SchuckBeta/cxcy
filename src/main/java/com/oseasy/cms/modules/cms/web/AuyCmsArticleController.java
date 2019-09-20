package com.oseasy.cms.modules.cms.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.util.common.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.cms.common.config.CmsSval;
import com.oseasy.cms.common.config.CmsSval.CmsEmskey;
import com.oseasy.cms.modules.cms.entity.CmsArticle;
import com.oseasy.cms.modules.cms.entity.CmsArticleData;
import com.oseasy.cms.modules.cms.service.CmsArticleService;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.modules.promodel.service.ProCmsArticleService;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;


/**
 * 一般内容管理Controller.
 * @author liangjie
 * @version 2018-09-04
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/cmsArticle")
public class AuyCmsArticleController extends BaseController {
    @Autowired
    private CmsArticleService cmsArticleService;
    @Autowired
    private ProCmsArticleService proCmsArticleService;
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
    @RequestMapping(value = "excellent/projectForm")
    public String projectForm(CmsArticle cmsArticle, Model model) {
        if(cmsArticle.getPrId()!=null){
            Map<String,String> proModelMap= proCmsArticleService.getPromodel(cmsArticle.getPrId());
            model.addAttribute("proModelMap", proModelMap);
        }
        CmsArticleData cmsArticleData=cmsArticle.getCmsArticleData();
        if(cmsArticleData!=null){
            model.addAttribute("cmsArticleData", cmsArticleData);
            if(cmsArticleData.getRelation()!=null){
                List<CmsArticle> relationList=cmsArticleService.getCmsArticleByIds(cmsArticleData.getRelation());
                model.addAttribute("relationList", relationList);
            }
        }
        model.addAttribute("cmsArticle", cmsArticle);
        model.addAttribute("systemDate", DateUtil.getDate(DateUtil.FMT_YYYYMMDD_ZG + " "+ DateUtil.FMT_HMS000));
        return CmsSval.path.vms(CmsEmskey.CMS.k()) + "excellent/cmsArticleProjectForm";
    }


    /**
        优秀项目列表查询接口
     */
    @RequestMapping(value="excellent/projectList")
    @ResponseBody
    public ApiResult projectList(CmsArticle cmsArticle, HttpServletRequest request, HttpServletResponse response){
        try{
            Page<CmsArticle> page = proCmsArticleService.findProjectPage(new Page<CmsArticle>(request, response), cmsArticle);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
        项目查询中发布优秀项目接口
     */
    @RequestMapping(value="excellent/projectPublish")
    @ResponseBody
    public ApiResult projectPublish(String ids, HttpServletRequest request, HttpServletResponse response){
        boolean ishave=cmsArticleService.checkIsHavePublish(ids);
        if(ishave){
            return ApiResult.failed(ApiConst.CODE_MORE_ERROR,ApiConst.getErrMsg(ApiConst.CODE_MORE_ERROR));
        }
        try{
            proCmsArticleService.projectPublish(ids);
            return ApiResult.success();
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }


    /**
     大赛查询中发布获奖接口
     */
    @RequestMapping(value="excellent/gcontestPublish")
    @ResponseBody
    public ApiResult gcontestPublish(String ids, HttpServletRequest request, HttpServletResponse response){
        boolean ishave=cmsArticleService.checkIsHavePublish(ids);
        if(ishave){
            return ApiResult.failed(ApiConst.CODE_MORE_ERROR,ApiConst.getErrMsg(ApiConst.CODE_MORE_ERROR));
        }
        try{
            proCmsArticleService.gcontestPublish(ids);
            return ApiResult.success();
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
        优秀项目列表查询接口
     */
    @RequestMapping(value="excellent/promodelList")
    @ResponseBody
    public ApiResult promodelList(CmsArticle cmsArticle, HttpServletRequest request, HttpServletResponse response){
        try{
            return ApiResult.success(proCmsArticleService.findProjectPage(new Page<CmsArticle>(request, response),cmsArticle));
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
    获奖列表查询接口
     */
    @RequestMapping(value="excellent/gcontestList")
    @ResponseBody
    public ApiResult gcontestList(CmsArticle cmsArticle, HttpServletRequest request, HttpServletResponse response){
        try{
            return ApiResult.success(proCmsArticleService.findGcontestPage(new Page<CmsArticle>(request, response),cmsArticle));
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    //获奖修改页面
    @RequestMapping(value = "excellent/gcontestForm")
    public String gcontestForm(CmsArticle cmsArticle, Model model) {
        if(cmsArticle.getPrId()!=null){
            Map<String,String> proModelMap= proCmsArticleService.getPromodel(cmsArticle.getPrId());
            model.addAttribute("proModelMap", proModelMap);
        }
        CmsArticleData cmsArticleData=cmsArticle.getCmsArticleData();
        if(cmsArticleData!=null){
            model.addAttribute("cmsArticleData", cmsArticleData);
            if(cmsArticleData.getRelation()!=null){
                List<CmsArticle> relationList=cmsArticleService.getCmsArticleByIds(cmsArticleData.getRelation());
                model.addAttribute("relationList", relationList);
            }
        }
        model.addAttribute("cmsArticle", cmsArticle);
        model.addAttribute("systemDate", DateUtil.getDate(DateUtil.FMT_YYYYMMDD_ZG + " "+ DateUtil.FMT_HMS000));
        return CmsSval.path.vms(CmsEmskey.CMS.k()) + "excellent/cmsArticleGcontestForm";
    }

}
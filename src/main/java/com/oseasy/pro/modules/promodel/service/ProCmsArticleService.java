package com.oseasy.pro.modules.promodel.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import com.oseasy.annotation.Domain;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.pro.modules.workflow.entity.ProvinceProModel;
import com.oseasy.pro.modules.workflow.service.ProvinceProModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.oseasy.cms.modules.cms.entity.Category;
import com.oseasy.cms.modules.cms.entity.CmsArticle;
import com.oseasy.cms.modules.cms.entity.Site;
import com.oseasy.cms.modules.cms.enums.CategoryModel;
import com.oseasy.cms.modules.cms.service.CategoryService;
import com.oseasy.cms.modules.cms.service.CmsArticleService;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.modules.gcontest.entity.GContest;
import com.oseasy.pro.modules.gcontest.service.GContestService;
import com.oseasy.pro.modules.project.entity.ProjectDeclare;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 一般内容管理Service.
 *
 * @author liangjie
 * @version 2018-09-04
 */
@Service
@Transactional(readOnly = true)
public class ProCmsArticleService {
    @Autowired
    private CmsArticleService cmsArticleService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProModelService proModelService;
    @Autowired
    private GContestService gContestService;
    @Autowired
    private ProjectDeclareService projectDeclareService;
    @Autowired
    private ProvinceProModelService provinceProModelService;

    public Page<CmsArticle> findProjectPage(Page<CmsArticle> cmsArticlePage, CmsArticle cmsArticle) {
        cmsArticle.setModule(CategoryModel.PROJECTMODELPARAM);
        cmsArticle.setPrType("1");
        cmsArticle.setPage(cmsArticlePage);
        List<CmsArticle> cmsArticleList = cmsArticleService.findIndexList(cmsArticle);
        for (CmsArticle cmsArticleIndex : cmsArticleList) {
            if (StringUtil.isNotEmpty(cmsArticleIndex.getPrId())) {
                ProModel promodel = proModelService.getExcellentById(cmsArticleIndex.getPrId());
                ProjectDeclare projectDeclare = projectDeclareService.getExcellentById(cmsArticleIndex.getPrId());
                if (promodel != null) {
                    addPromodelCmsArticle(cmsArticleIndex, promodel);
                } else if (projectDeclare != null) {
                    addProjectDeclareCmsArticle(cmsArticleIndex, projectDeclare);
                }
            }
        }
        cmsArticlePage.setList(cmsArticleList);
        return cmsArticlePage;
    }

    public Page<CmsArticle> findProjectFrontPage(Page<CmsArticle> cmsArticlePage, CmsArticle cmsArticle) {
        cmsArticle.setModule(CategoryModel.PROJECTMODELPARAM);
        cmsArticle.setPrType("1");
        cmsArticle.setPage(cmsArticlePage);
        List<CmsArticle> cmsArticleList = cmsArticleService.findIndexFrontList(cmsArticle);
        for (CmsArticle cmsArticleIndex : cmsArticleList) {
            if (StringUtil.isNotEmpty(cmsArticleIndex.getPrId())) {
                ProModel promodel = proModelService.getExcellentById(cmsArticleIndex.getPrId());
                ProjectDeclare projectDeclare = projectDeclareService.getExcellentById(cmsArticleIndex.getPrId());
                if (promodel != null) {
                    addPromodelCmsArticle(cmsArticleIndex, promodel);
                } else if (projectDeclare != null) {
                    addProjectDeclareCmsArticle(cmsArticleIndex, projectDeclare);
                }
            }
        }
        cmsArticlePage.setList(cmsArticleList);
        return cmsArticlePage;
    }

    public Page<CmsArticle> findGcontestPage(Page<CmsArticle> cmsArticlePage, CmsArticle cmsArticle) {
        cmsArticle.setModule(CategoryModel.PROJECTMODELPARAM);
        cmsArticle.setPrType("7");
        cmsArticle.setPage(cmsArticlePage);
        List<CmsArticle> cmsArticleList = cmsArticleService.findIndexList(cmsArticle);
        for (CmsArticle cmsArticleIndex : cmsArticleList) {
            if (StringUtil.isNotEmpty(cmsArticleIndex.getPrId())) {
                ProModel promodel = proModelService.getGcontestExcellentById(cmsArticleIndex.getPrId());
                GContest gContest = gContestService.getExcellentById(cmsArticleIndex.getPrId());
                if (promodel != null) {
                    addPromodelCmsArticle(cmsArticleIndex, promodel);
                } else if (gContest != null) {
                    addGContestCmsArticle(cmsArticleIndex, gContest);
                }
            }
        }
        cmsArticlePage.setList(cmsArticleList);
        return cmsArticlePage;
    }

    public Page<CmsArticle> findGcontestFrontPage(Page<CmsArticle> cmsArticlePage, CmsArticle cmsArticle) {
        cmsArticle.setModule(CategoryModel.PROJECTMODELPARAM);
        cmsArticle.setPrType("7");
        cmsArticle.setPage(cmsArticlePage);
        List<CmsArticle> cmsArticleList = cmsArticleService.findIndexFrontList(cmsArticle);
        for (CmsArticle cmsArticleIndex : cmsArticleList) {
            if (StringUtil.isNotEmpty(cmsArticleIndex.getPrId())) {
                ProModel promodel = proModelService.getGcontestExcellentById(cmsArticleIndex.getPrId());
                GContest gContest = gContestService.getExcellentById(cmsArticleIndex.getPrId());
                if (promodel != null) {
                    addPromodelCmsArticle(cmsArticleIndex, promodel);
                } else if (gContest != null) {
                    addGContestCmsArticle(cmsArticleIndex, gContest);
                }
            }
        }
        cmsArticlePage.setList(cmsArticleList);
        return cmsArticlePage;
    }

    public void addGContestCmsArticle(CmsArticle cmsArticleIndex, GContest gContest) {
        cmsArticleIndex.setProjectName(gContest.getpName());
        cmsArticleIndex.setSnames(gContest.getSnames());
        cmsArticleIndex.setTnames(gContest.getTnames());
        cmsArticleIndex.setSourceName(gContest.getSourceName());
        cmsArticleIndex.setProjectIntroduction(gContest.getIntroduction());
        if (UserUtils.get(gContest.getDeclareId()) != null) {
            User leader = UserUtils.get(gContest.getDeclareId());
            cmsArticleIndex.setLeaderName(leader.getName());
            cmsArticleIndex.setCollgeName(leader.getOfficeName());
        }
    }

    public void addPromodelCmsArticle(CmsArticle cmsArticleIndex, ProModel promodel) {
        cmsArticleIndex.setProjectName(promodel.getPName());
        cmsArticleIndex.setSnames(promodel.getSnames());
        cmsArticleIndex.setTnames(promodel.getTnames());
        cmsArticleIndex.setSourceName(promodel.getSourceName());
        cmsArticleIndex.setProjectIntroduction(promodel.getIntroduction());
        if (UserUtils.get(promodel.getDeclareId()) != null) {
            User leader = UserUtils.get(promodel.getDeclareId());
            cmsArticleIndex.setLeaderName(leader.getName());
            cmsArticleIndex.setCollgeName(leader.getOfficeName());
        }
    }

    public void addPromodelGcontestCmsArticle(CmsArticle cmsArticleIndex, ProModel promodel) {
        cmsArticleIndex.setProjectName(promodel.getPName());
        cmsArticleIndex.setSnames(promodel.getSnames());
        cmsArticleIndex.setTnames(promodel.getTnames());
        cmsArticleIndex.setSourceName(promodel.getSourceName());
        cmsArticleIndex.setProjectIntroduction(promodel.getIntroduction());
        if (UserUtils.get(promodel.getDeclareId()) != null) {
            User leader = UserUtils.get(promodel.getDeclareId());
            cmsArticleIndex.setLeaderName(leader.getName());
            cmsArticleIndex.setCollgeName(leader.getOfficeName());
        }
    }

    public void addProjectDeclareCmsArticle(CmsArticle cmsArticleIndex, ProjectDeclare projectDeclare) {
        cmsArticleIndex.setProjectName(projectDeclare.getName());
        cmsArticleIndex.setSnames(projectDeclare.getSnames());
        cmsArticleIndex.setTnames(projectDeclare.getTnames());
        cmsArticleIndex.setSourceName(projectDeclare.getSourceName());
        cmsArticleIndex.setProjectIntroduction(projectDeclare.getIntroduction());
        if (UserUtils.get(projectDeclare.getLeader()) != null) {
            User leader = UserUtils.get(projectDeclare.getLeader());
            cmsArticleIndex.setLeaderName(leader.getName());
            cmsArticleIndex.setCollgeName(leader.getOfficeName());
        }
    }


    @Transactional(readOnly = false)
    public void projectPublish(String ids) {
        String[] idsList = ids.split(",");
        List<CmsArticle> cmsArticleList = new ArrayList<>();
        String siteId=(String)CoreUtils.getCache(Site.SITE_ID);
        //找到相应的项目展示父栏目
        Category category =categoryService.getByIdAndType(siteId,"0000000286");
        for(int i=0;i<idsList.length;i++){
            CmsArticle cmsArticle = new CmsArticle();
            if(category!=null) {
                cmsArticle.setCategoryId(category.getId());
            }
            //设置为发布状态
            cmsArticle.setPublishStatus("1");
            //设置为优秀项目栏目
            cmsArticle.setModule("0000000276");
            cmsArticle.setPrId(idsList[i]);
            //设置类别为项目
            cmsArticle.setPrType("1");
            cmsArticle.setPublishhforever("1");

            if(TenantConfig.getCacheTenant().equals(CoreIds.NPR_SYS_TENANT.getId())){
                ProvinceProModel provinceProModel =provinceProModelService.get(cmsArticle.getPrId());
                cmsArticle.setTitle(provinceProModel.getProModel().getPName());
            }else{
                ProModel promodel=proModelService.get(cmsArticle.getPrId());
                ProjectDeclare projectDeclare=projectDeclareService.get(cmsArticle.getPrId());
                if(promodel!=null){
                    cmsArticle.setTitle(promodel.getPName());
                }if(projectDeclare!=null){
                    cmsArticle.setTitle(projectDeclare.getName());
                }
            }
            cmsArticle.preInsert();
            cmsArticleList.add(cmsArticle);
        }
        cmsArticleService.savePublishProject(cmsArticleList);
    }

    @Transactional(readOnly = false)
    public void gcontestPublish(String ids) {
        String[] idsList = ids.split(",");
        List<CmsArticle> cmsArticleList = new ArrayList<>();
        String siteId=(String)CoreUtils.getCache(Site.SITE_ID);
        //找到相应的大赛展示父栏目
        Category category =categoryService.getByIdAndType(siteId,"0000000287");
        for(int i=0;i<idsList.length;i++){
            CmsArticle cmsArticle = new CmsArticle();
            if(category!=null) {
                cmsArticle.setCategoryId(category.getId());
            }
            //设置为发布状态
            cmsArticle.setPublishStatus("1");
            //设置为获奖大赛栏目
            cmsArticle.setModule("0000000276");
            cmsArticle.setPrId(idsList[i]);
            //设置类别为项目
            cmsArticle.setPrType("7");
            cmsArticle.setPublishhforever("1");
            if(TenantConfig.getCacheTenant().equals(CoreIds.NPR_SYS_TENANT.getId())){
                ProvinceProModel provinceProModel =provinceProModelService.get(cmsArticle.getPrId());
                cmsArticle.setTitle(provinceProModel.getProModel().getPName());
            }else{
                ProModel promodel=proModelService.get(cmsArticle.getPrId());
                GContest gContest=gContestService.get(cmsArticle.getPrId());
                if(promodel!=null){
                    cmsArticle.setTitle(promodel.getPName());
                }else if(gContest!=null){
                    cmsArticle.setTitle(gContest.getpName());
                }
            }
            cmsArticle.preInsert();
            cmsArticleList.add(cmsArticle);
        }
        cmsArticleService.savePublishProject(cmsArticleList);
    }

    //根据proId找到具体的项目
    public Map<String,String> getPromodel(String prId) {

        ProModel promodel=proModelService.get(prId);
        ProjectDeclare projectDeclare=projectDeclareService.get(prId);
        GContest gContest=gContestService.get(prId);
        Map<String,String> map= Maps.newHashMap();
        map.put("id",prId);
        if(projectDeclare!=null){
            map.put("name",projectDeclare.getName());
        }else if(gContest !=null){
            map.put("name",gContest.getpName());
        }else if(promodel !=null){
            map.put("name",promodel.getPName());
        }
        return map;
    }

    public Map<String,Object> findIndexProjectList() {
        Map<String,Object> map= Maps.newHashMap();
        CmsArticle cmsArticle =new CmsArticle();
        cmsArticle.setPublishStatus("1");
        cmsArticle.setModule(CategoryModel.PROJECTMODELPARAM);
        cmsArticle.setPosid("1");
        cmsArticle.setNowDate(DateUtil.getDate(DateUtil.FMT_YYYYMMDD_ZG+" "+DateUtil.FMT_HMS000));
        cmsArticle.setPrType("1");
        cmsArticle.setTenantId(TenantConfig.getCacheTenant());
        List<CmsArticle> cmsProjectArticleList=cmsArticleService.findListByLimit(cmsArticle);
        for(CmsArticle cmsArticleIndex:cmsProjectArticleList){
            if(StringUtil.isNotEmpty(cmsArticleIndex.getPrId())){
                ProModel promodel=proModelService.getExcellentById(cmsArticleIndex.getPrId());
                ProjectDeclare projectDeclare=projectDeclareService.getExcellentById(cmsArticleIndex.getPrId());
                if(promodel!=null){
                    addPromodelCmsArticle(cmsArticleIndex,promodel);
                }else if(projectDeclare!=null){
                    addProjectDeclareCmsArticle(cmsArticleIndex,projectDeclare);
                }
            }
        }
        cmsArticle.setPrType("7");
        List<CmsArticle> cmsGcontestArticleList=cmsArticleService.findListByLimit(cmsArticle);
        for(CmsArticle cmsArticleIndex:cmsGcontestArticleList){
            if(StringUtil.isNotEmpty(cmsArticleIndex.getPrId())){
                ProModel promodel=proModelService.getGcontestExcellentById(cmsArticleIndex.getPrId());
                GContest gContest=gContestService.getExcellentById(cmsArticleIndex.getPrId());
                if(promodel!=null){
                    addPromodelCmsArticle(cmsArticleIndex,promodel);
                }else if(gContest!=null){
                    addGContestCmsArticle(cmsArticleIndex,gContest);
                }
            }
        }

        map.put("projectList", cmsProjectArticleList);
        map.put("gcontestList", cmsGcontestArticleList);
        return map;
    }


    public Page<CmsArticle> findSecondProjectList(Page<CmsArticle> cmsArticlePage, CmsArticle cmsArticle) {
        Map<String,Object> map= Maps.newHashMap();
        //cmsArticle.setPage(cmsArticlePage);
        cmsArticle.setPublishStatus("1");

        //cmsArticle.setPublishStatus("1");
        cmsArticle.setNowDate(DateUtil.getDate(DateUtil.FMT_YYYYMMDD_ZG+" "+DateUtil.FMT_HMS000));
        //查询优秀项目和大赛

        cmsArticle.setModule(CategoryModel.PROJECTMODELPARAM);
        cmsArticle.setPage(cmsArticlePage);
        List<CmsArticle> cmsProjectArticleList=cmsArticleService.findIndexFrontList(cmsArticle);
        for(CmsArticle cmsArticleIndex:cmsProjectArticleList){
            if(StringUtil.isNotEmpty(cmsArticleIndex.getPrId())){
                ProModel promodel=proModelService.getExcellentById(cmsArticleIndex.getPrId());
                GContest gContest=gContestService.getExcellentById(cmsArticleIndex.getPrId());
                ProjectDeclare projectDeclare=projectDeclareService.getExcellentById(cmsArticleIndex.getPrId());

                if(promodel!=null){
                    if("1".equals(cmsArticleIndex.getPrType())) {
                        addPromodelCmsArticle(cmsArticleIndex, promodel);
                    }else{
                        promodel=proModelService.getGcontestExcellentById(cmsArticleIndex.getPrId());
                        addPromodelGcontestCmsArticle(cmsArticleIndex, promodel);
                    }
                }else if(gContest!=null){
                    addGContestCmsArticle(cmsArticleIndex,gContest);
                }else if(projectDeclare!=null){
                    addProjectDeclareCmsArticle(cmsArticleIndex,projectDeclare);
                }
            }
        }
        cmsArticlePage.setList(cmsProjectArticleList);
//      map.put("page", cmsArticlePage);
        return cmsArticlePage;
    }
}
package com.oseasy.cms.modules.cms.vo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oseasy.cms.modules.cms.entity.Category;
import com.oseasy.cms.modules.cms.entity.CmsArticle;
import com.oseasy.cms.modules.cms.entity.CmsConmment;
import com.oseasy.cms.modules.cms.entity.Site;

import java.io.Serializable;
import java.util.List;

/**
 *
 *  内容管理系统前台数据返回接口类
 * Created by liangjie on 2018/9/14.
 */
public class FrontArticleResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private static Logger logger = LoggerFactory.getLogger(FrontArticleResult.class);
    //当前站点
    private Site site;
    //当前栏目
    private Category category;
    //文章
    private Object oneArticleOrList;
    //相关推荐
    private List<CmsArticle> cmsArticleAbout;
    //评论
    private List<CmsConmment> cmsConmmentList;

    public FrontArticleResult(Site site,Category category,Object oneArticleOrList,List<CmsArticle> cmsArticleAbout,List<CmsConmment> cmsConmmentList){
        this.site = site;
        this.category = category;
        this.oneArticleOrList = oneArticleOrList;
        this.cmsArticleAbout = cmsArticleAbout;
        this.cmsConmmentList = cmsConmmentList;
    }


    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Object getOneArticleOrList() {
        return oneArticleOrList;
    }

    public void setOneArticleOrList(Object oneArticleOrList) {
        this.oneArticleOrList = oneArticleOrList;
    }

    public List<CmsArticle> getCmsArticleAbout() {
        return cmsArticleAbout;
    }

    public void setCmsArticleAbout(List<CmsArticle> cmsArticleAbout) {
        this.cmsArticleAbout = cmsArticleAbout;
    }

    public List<CmsConmment> getCmsConmmentList() {
        return cmsConmmentList;
    }

    public void setCmsConmmentList(List<CmsConmment> cmsConmmentList) {
        this.cmsConmmentList = cmsConmmentList;
    }
}

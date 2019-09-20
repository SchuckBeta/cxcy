package com.oseasy.cms.modules.cms.dao;

import com.oseasy.cms.modules.cms.entity.CmsArticle;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 一般内容管理DAO接口.
 * @author liangjie
 * @version 2018-09-04
 */
@MyBatisDao
public interface CmsArticleDao extends CrudDao<CmsArticle> {

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(CmsArticle cmsArticle);
  @Override
  @InsertByTenant
  public int insert(CmsArticle entity);
  public void udpateTop(@Param("cmsArticleList") List<CmsArticle> cmsArticleList);
  public void udpateSort(@Param("cmsArticleList")List<CmsArticle> cmsArticleList);
  public void udpatePublishStatus(@Param("cmsArticleList")List<CmsArticle> cmsArticleList);
  public void udpateDelFlag(@Param("cmsArticleList")List<CmsArticle> cmsArticleList);
  @InsertByTenant
  void savePublishProject(@Param("cmsArticleList")List<CmsArticle> cmsArticleList);
  @Override
  @FindListByTenant
  public List<CmsArticle> findList(CmsArticle entity);
  void updateTopArticle(CmsArticle cmsArticle);
  void updateSortArticle(CmsArticle cmsArticle);
  @FindListByTenant
  List<CmsArticle> findNormalContentList(CmsArticle cmsArticle);
  @FindListByTenant
  List<CmsArticle> articleInCommentList(CmsArticle cmsArticle);

  void delPl(@Param("idList")List<String> idList);

  List<CmsArticle> getCmsArticleByIds(@Param("relationIdList")List<String> relationIdList);
  @FindListByTenant
  List<CmsArticle> findListByLimit(CmsArticle cmsArticle);

  List<CmsArticle> getCmsArticleByPrIds(@Param("idsList")List<String> idsList);
  public int updateHitsAddOne(String id);
  @FindListByTenant
  List<CmsArticle> findIndexList(CmsArticle cmsArticle);

  void updateTopPassDate();

  void updatePubLishPassDate();

  List<CmsArticle> frontArticleList(CmsArticle cmsArticle);

  @FindListByTenant
  List<CmsArticle> findIndexFrontList(CmsArticle cmsArticle);
  List<CmsArticle> validateArticleName(CmsArticle cmsArticle);
}
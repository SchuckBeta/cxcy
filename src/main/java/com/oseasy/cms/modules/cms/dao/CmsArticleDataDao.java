package com.oseasy.cms.modules.cms.dao;

import com.oseasy.cms.modules.cms.entity.CmsArticleData;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

/**
 * 文章详情表DAO接口.
 * @author liangjie
 * @version 2018-09-04
 */
@MyBatisDao
public interface CmsArticleDataDao extends CrudDao<CmsArticleData> {

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(CmsArticleData cmsArticleData);

  CmsArticleData getCmsArticleDataByContetId(String contentId);

  public void updateArticleLikes(CmsArticleData cmsArticleData);
}
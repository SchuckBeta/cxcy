package com.oseasy.cms.modules.cms.dao;

import com.oseasy.cms.modules.cms.entity.CmsConmment;
import com.oseasy.cms.modules.cms.entity.CmsConmmentLikes;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

/**
 * 评论DAO接口.
 * @author chenh
 * @version 2018-09-04
 */
@MyBatisDao
public interface CmsConmmentLikesDao extends CrudDao<CmsConmmentLikes> {

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(CmsConmmentLikes cmsConmmentLikes);

  /**
   * 批量物理删除.
   * @param entity
   */
  public void deleteWLPL(CmsConmmentLikes cmsConmmentLikes);
}
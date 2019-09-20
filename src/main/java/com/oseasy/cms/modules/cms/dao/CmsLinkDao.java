package com.oseasy.cms.modules.cms.dao;

import com.oseasy.cms.modules.cms.entity.CmsLink;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 友情链接DAO接口.
 * @author zy
 * @version 2018-08-30
 */
@MyBatisDao
public interface CmsLinkDao extends CrudDao<CmsLink> {
  @Override
  @FindListByTenant
  public List<CmsLink> findList(CmsLink entity);
  @Override
  @InsertByTenant
  public int insert(CmsLink entity);
  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(CmsLink cmsLink);

  void delPl(@Param("idList")List<String> idList);

//  void cmsLinkSaveSort(@Param("idList") List<String> idList, @Param("sortList")List<String> sortList);

  void cmsLinkSaveSort(@Param("cmsList")List<CmsLink> cmsList);
  @FindListByTenant
  List<CmsLink> findFrontList(CmsLink cmsLink);

  Integer checkLinkName(CmsLink cmsLink);
}
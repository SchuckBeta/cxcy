package com.oseasy.cms.modules.cms.dao;

import com.oseasy.cms.modules.cms.entity.CmsIndex;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 首页管理DAO接口.
 * @author zy
 * @version 2018-09-03
 */
@MyBatisDao
public interface CmsIndexDao extends CrudDao<CmsIndex> {
  @Override
  @FindListByTenant
  public List<CmsIndex> findList(CmsIndex entity);
  @Override
  @InsertByTenant
  public int insert(CmsIndex entity);
  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(CmsIndex cmsIndex);
  @FindListByTenant
  List<CmsIndex> findIndexList(CmsIndex cmsIndex);

  void cmsIndexSaveSort(@Param("cmsIndexList")List<CmsIndex> cmsIndexList);
  @FindListByTenant
  CmsIndex getByModelename(String modelename);
  @FindListByTenant
  List<CmsIndex> findInIndexList(CmsIndex cmsIndex);
  @FindListByTenant
  List<CmsIndex> findhiddenList();
  @FindListByTenant
  List<CmsIndex> findShowIndexList();
}
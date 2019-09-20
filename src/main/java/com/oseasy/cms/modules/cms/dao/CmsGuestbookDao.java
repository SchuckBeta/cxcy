package com.oseasy.cms.modules.cms.dao;

import com.oseasy.cms.modules.cms.entity.CmsConmment;
import com.oseasy.cms.modules.cms.entity.CmsGuestbook;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 留言DAO接口.
 * @author chenh
 * @version 2018-09-04
 */
@MyBatisDao
public interface CmsGuestbookDao extends CrudDao<CmsGuestbook> {
  @Override
  @FindListByTenant
  public List<CmsGuestbook> findList(CmsGuestbook entity);
  @Override
  @InsertByTenant
  public int insert(CmsGuestbook entity);
  /**
   * 批量修改Audit.
   * @param entitys
   */
  public void updatePLAudit(@Param("entitys") List<CmsGuestbook> entitys);

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(CmsGuestbook cmsGuestbook);

  /**
   * 批量状态删除.
   * @param entity
   */
  public void deletePL(CmsGuestbook cmsGuestbook);

  /**
   * 批量物理删除.
   * @param entity
   */
  public void deleteWLPL(CmsGuestbook cmsGuestbook);
}
package com.oseasy.cms.modules.cms.dao;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import org.apache.ibatis.annotations.Param;

import com.oseasy.cms.modules.cms.entity.CmsConmment;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

/**
 * 评论DAO接口.
 * @author chenh
 * @version 2018-09-04
 */
@MyBatisDao
public interface CmsConmmentDao extends CrudDao<CmsConmment> {
    @Override
    @FindListByTenant
    public List<CmsConmment> findList(CmsConmment entity);
    @Override
    @InsertByTenant
    public int insert(CmsConmment entity);
    /**
     * 批量新增.
     * @param entitys
     */
    public void insertPL(@Param("entitys") List<CmsConmment> entitys);

    /**
     * 批量修改.
     * @param entitys
     */
    public void updatePL(@Param("entitys") List<CmsConmment> entitys);

    /**
     * 批量修改Recommend.
     * @param entitys
     */
    public void updatePLRecommend(@Param("entitys") List<CmsConmment> entitys);

    /**
     * 批量修改Audit.
     * @param entitys
     */
    public void updatePLAudit(@Param("entitys") List<CmsConmment> entitys);

    /**
     * 批量修改Audit.
     * @param entitys
     */
    public void updatePLAuditByCntIds(@Param("entitys") List<CmsConmment> entitys);

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(CmsConmment cmsConmment);
    /**
     * 批量状态删除.
     * @param entity
     */
    public void deletePL(CmsConmment cmsConmment);

    /**
     * 批量状态删除.
     * @param entity
     */
    public void deletePLByCntIds(@Param("ids") List<String> ids);

  /**
   * 批量物理删除.
   * @param entity
   */
  public void deleteWLPL(CmsConmment cmsConmment);
  /**
   * 根据Cnt.ids批量物理删除.
   * @param entity
   */
  public void deleteWLPLByCntIds(@Param("ids") List<String> ids);
    /**
     * 根据文章id获取评论总数.
     * @param entity
     */
   public Integer getArticleCommentCounts(CmsConmment cmsConmment);

}
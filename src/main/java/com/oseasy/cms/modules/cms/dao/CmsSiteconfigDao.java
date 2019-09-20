package com.oseasy.cms.modules.cms.dao;

import com.oseasy.cms.modules.cms.entity.CmsSiteconfig;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 网站配置DAO接口.
 * @author zy
 * @version 2018-08-27
 */
@MyBatisDao
public interface CmsSiteconfigDao extends CrudDao<CmsSiteconfig> {

  /**
   * 物理删除.
   * @param cmsSiteconfig
   */
    public void deleteWL(CmsSiteconfig cmsSiteconfig);

    String getLastId();
    @Override
    @InsertByTenant
    public int insert(CmsSiteconfig cmsSiteconfig);
    List<CmsSiteconfig>  getBySiteId(@Param("siteId")String siteId);
    @InsertByTenant
    void savePl(@Param("cmsSiteconfigList")List<CmsSiteconfig> cmsSiteconfigList);

    void delBySiteId(String siteId);

  CmsSiteconfig getBySiteIdAndBanner(String siteId);

  CmsSiteconfig getBySiteIdAndType(@Param("siteId")String id, @Param("type")String type);
}
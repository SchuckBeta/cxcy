package com.oseasy.pro.modules.cert.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.cert.entity.SysCertPage;

/**
 * 证书模板页面DAO接口.
 * @author 奔波儿灞
 * @version 2018-02-06
 */
@MyBatisDao
public interface SysCertPageDao extends CrudDao<SysCertPage> {

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(SysCertPage sysCertPage);
  public Integer getMaxSort(String certid);
  public List<SysCertPage> getSysCertPages(String certid);
  public void savePageName(@Param("pageid")String pageid,@Param("pagename")String pagename);
}
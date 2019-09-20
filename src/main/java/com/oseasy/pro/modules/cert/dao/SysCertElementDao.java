package com.oseasy.pro.modules.cert.dao;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.cert.entity.SysCertElement;

/**
 * 证书模板元素DAO接口.
 * @author 奔波儿灞
 * @version 2018-02-06
 */
@MyBatisDao
public interface SysCertElementDao extends CrudDao<SysCertElement> {

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(SysCertElement sysCertElement);
  public void deleteByPageid(String pageid);
  public List<SysCertElement> getSysCertElement(String pageid);
}
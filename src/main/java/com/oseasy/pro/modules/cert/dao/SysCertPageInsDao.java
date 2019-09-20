package com.oseasy.pro.modules.cert.dao;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.cert.entity.SysCertPageIns;
import com.oseasy.pro.modules.cert.vo.SysCertPageInsVo;

/**
 * 证书颁发记录页面表DAO接口.
 * @author 奔波儿灞
 * @version 2018-02-09
 */
@MyBatisDao
public interface SysCertPageInsDao extends CrudDao<SysCertPageIns> {

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(SysCertPageIns sysCertPageIns);
  public List<SysCertPageInsVo> getSysCertPageIns(String sci);
}
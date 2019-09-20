package com.oseasy.pro.modules.cert.dao;

import java.util.List;
import java.util.Map;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.cert.entity.CertPage;
import com.oseasy.pro.modules.cert.entity.SysCert;
import com.oseasy.pro.modules.cert.vo.SysCertFlowVo;
import com.oseasy.pro.modules.cert.vo.SysCertPageVo;
import com.oseasy.pro.modules.cert.vo.SysCertVo;

/**
 * 证书模板DAO接口.
 * @author 奔波儿灞
 * @version 2018-02-06
 */
@MyBatisDao
public interface SysCertDao extends CrudDao<SysCert> {

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(SysCert sysCert);
  @FindListByTenant
  public List<SysCertVo> getMyPageList(SysCertVo vo);
  public List<SysCertPageVo> getCertPageVoList(List<String> ids);
  public List<SysCertFlowVo> getCertFlowVoList(List<String> ids);
  public int deleteAll(String ids);
  public CertPage getCertPage(String pageid);
  public void unrelease(String id);
  public void release(String id);
  public Map<String,String> getProjectData(String pid);
  public List<Map<String,String>> getCertListWithFlow(String flow);
  public void editSysCertName(SysCertVo sysCert);

  @InsertByTenant
  int insert(SysCert sysCert);
}
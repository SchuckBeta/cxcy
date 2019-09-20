package com.oseasy.pro.modules.cert.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.cert.entity.SysCertFlow;
import com.oseasy.pro.modules.cert.vo.SysCertFlowVo;

/**
 * 证书模板-流程节点关系DAO接口.
 * @author 奔波儿灞
 * @version 2018-02-06
 */
@MyBatisDao
public interface SysCertFlowDao extends CrudDao<SysCertFlow> {

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(SysCertFlow sysCertFlow);
  public int findByCdn(@Param(value="flow") String flow,@Param(value="node") String node);
  public List<SysCertFlow> getCertFlows(@Param(value="flow") String flow,@Param(value="node") String node);
  public List<SysCertFlow> getCertFlowsWithCid(@Param(value="flow") String flow,@Param(value="certid") String certid);
  public SysCertFlowVo getCertFlowVo(String id);
  public void deleteByFlow(SysCertFlow sysCertFlow);
}
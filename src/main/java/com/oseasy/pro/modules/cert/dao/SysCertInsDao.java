package com.oseasy.pro.modules.cert.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.cert.entity.SysCertIns;
import com.oseasy.pro.modules.cert.vo.SysCertInsVo;

/**
 * 证书信息记录DAO接口.
 * @author 奔波儿灞
 * @version 2018-02-06
 */
@MyBatisDao
public interface SysCertInsDao extends CrudDao<SysCertIns> {

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(SysCertIns sysCertIns);
  public List<String> getPidsByFlowNode(@Param("flow")String flow,@Param("node")String node);
  public List<SysCertInsVo> getSysCertIns(@Param("pids") List<String> pids);
}
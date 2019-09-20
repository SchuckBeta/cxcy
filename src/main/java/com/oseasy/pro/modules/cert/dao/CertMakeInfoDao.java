package com.oseasy.pro.modules.cert.dao;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.cert.entity.CertMakeInfo;

/**
 * 下发证书进度信息DAO接口.
 * @author 奔波儿灞
 * @version 2018-03-02
 */
@MyBatisDao
public interface CertMakeInfoDao extends CrudDao<CertMakeInfo> {

  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(CertMakeInfo certMakeInfo);
  public Integer getCertMakeingNum(@Param("actywId")String actywId,@Param("certid")String certid);
}
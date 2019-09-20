package com.oseasy.pro.modules.promodel.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.promodel.entity.ProReport;

import org.apache.ibatis.annotations.Param;

/**
 * 中期提交信息表DAO接口.
 * @author zy
 * @version 2017-12-01
 */
@MyBatisDao
public interface ProReportDao extends CrudDao<ProReport> {

	ProReport getByGnodeId(@Param("proModelId") String proModelId, @Param("gnodeId") String gnodeId);
}
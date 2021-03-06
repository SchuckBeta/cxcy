package com.oseasy.pro.modules.promodel.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.promodel.entity.ProCloseSubmit;

import org.apache.ibatis.annotations.Param;

/**
 * 结项提交信息表DAO接口.
 * @author zy
 * @version 2017-12-01
 */
@MyBatisDao
public interface ProCloseSubmitDao extends CrudDao<ProCloseSubmit> {

	ProCloseSubmit getByGnodeId(@Param("promodelId") String promodelId, @Param("gnodeId") String gnodeId);
}
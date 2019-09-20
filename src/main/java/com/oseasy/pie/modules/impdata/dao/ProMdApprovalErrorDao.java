package com.oseasy.pie.modules.impdata.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pie.modules.impdata.entity.ProMdApprovalError;

/**
 * 民大项目立项审核导入错误数据表DAO接口.
 * @author 9527
 * @version 2017-09-22
 */
@MyBatisDao
public interface ProMdApprovalErrorDao extends CrudDao<ProMdApprovalError> {
	public List<Map<String,String>> getListByImpIdAndSheet(@Param("impid")String impid,@Param("sheet")String sheet);
	public void deleteByImpId(String impid);
}
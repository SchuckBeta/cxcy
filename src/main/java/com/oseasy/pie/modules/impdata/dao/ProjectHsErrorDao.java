package com.oseasy.pie.modules.impdata.dao;

import java.util.List;
import java.util.Map;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pie.modules.impdata.entity.ProjectHsError;

/**
 * 导入项目错误数据表（华师）DAO接口.
 * @author 9527
 * @version 2017-10-10
 */
@MyBatisDao
public interface ProjectHsErrorDao extends CrudDao<ProjectHsError> {
	public List<Map<String,String>> getListByImpId(String impid);
	public void deleteByImpId(String impid);
}
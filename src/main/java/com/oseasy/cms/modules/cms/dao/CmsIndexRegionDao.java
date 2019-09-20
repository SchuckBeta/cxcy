package com.oseasy.cms.modules.cms.dao;

import com.oseasy.cms.modules.cms.entity.CmsIndexRegion;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

/**
 * 首页区域管理DAO接口
 * @author daichanggeng
 * @version 2017-04-06
 */
@MyBatisDao
public interface CmsIndexRegionDao extends CrudDao<CmsIndexRegion> {
	
}
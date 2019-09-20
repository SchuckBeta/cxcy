package com.oseasy.cms.modules.cms.dao;

import com.oseasy.cms.modules.cms.entity.CmsIndexResource;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

/**
 * 首页资源DAO接口
 * @author daichanggeng
 * @version 2017-04-07
 */
@MyBatisDao
public interface CmsIndexResourceDao extends CrudDao<CmsIndexResource> {
	
}
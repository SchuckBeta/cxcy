/**
 * 
 */
package com.oseasy.com.pcore.modules.gen.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.com.pcore.modules.gen.entity.GenTable;

/**
 * 业务表DAO接口


 */
@MyBatisDao
public interface GenTableDao extends CrudDao<GenTable> {
	
}

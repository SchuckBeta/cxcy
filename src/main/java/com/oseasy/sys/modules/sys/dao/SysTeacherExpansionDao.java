package com.oseasy.sys.modules.sys.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.sys.modules.sys.entity.SysTeacherExpansion;

/**
 * 导师信息表DAO接口
 * @author l
 * @version 2017-03-28
 */
@MyBatisDao
public interface SysTeacherExpansionDao extends CrudDao<SysTeacherExpansion> {
	
}
/**
 * 
 */
package com.oseasy.com.pcore.modules.sys.dao;

import com.oseasy.com.pcore.common.persistence.TreeDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.com.pcore.modules.sys.entity.Area;

import java.util.List;

/**
 * 区域DAO接口

 * @version 2014-05-16
 */
@MyBatisDao
public interface AreaDao extends TreeDao<Area> {

    public List<Area> findCityList(Area area);
	
}

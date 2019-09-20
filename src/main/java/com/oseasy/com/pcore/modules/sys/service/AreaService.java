/**
 *
 */
package com.oseasy.com.pcore.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.service.TreeService;
import com.oseasy.com.pcore.modules.sys.dao.AreaDao;
import com.oseasy.com.pcore.modules.sys.entity.Area;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;

/**
 * 区域Service

 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class AreaService extends TreeService<AreaDao, Area> {

	public List<Area> findAll() {
		return CoreUtils.getAreaList();
	}

	@Transactional(readOnly = false)
	public void save(Area area) {
		super.save(area);
		CoreUtils.removeCache(CoreUtils.CACHE_AREA_LIST);
	}

	@Transactional(readOnly = false)
	public void delete(Area area) {
		super.delete(area);
		CoreUtils.removeCache(CoreUtils.CACHE_AREA_LIST);
	}

	public List<Area> findCityList(Area area){
		return  dao.findCityList(area);
	}
}

package com.oseasy.cms.modules.cms.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.cms.modules.cms.dao.CmsIndexRegionDao;
import com.oseasy.cms.modules.cms.entity.Category;
import com.oseasy.cms.modules.cms.entity.CmsIndexRegion;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 首页区域管理Service
 * @author daichanggeng
 * @version 2017-04-06
 */
@Service
@Transactional(readOnly = true)
public class CmsIndexRegionService extends CrudService<CmsIndexRegionDao, CmsIndexRegion> {

	public CmsIndexRegion get(String id) {
		return super.get(id);
	}

	public List<CmsIndexRegion> findList(CmsIndexRegion cmsIndexRegion) {
		return super.findList(cmsIndexRegion);
	}

	public Page<CmsIndexRegion> findPage(Page<CmsIndexRegion> page, CmsIndexRegion cmsIndexRegion) {
		return super.findPage(page, cmsIndexRegion);
	}

	@Transactional(readOnly = false)
	public void save(CmsIndexRegion cmsIndexRegion) {
		super.save(cmsIndexRegion);
	}

	@Transactional(readOnly = false)
	public void delete(CmsIndexRegion cmsIndexRegion) {
		super.delete(cmsIndexRegion);
	}

	/**
	 * 获取区域栏目列表(支持2级节点)
	 * @param entity
	 * @return
	 */
	public List<Category> getRegionCategorys(CmsIndexRegion entity) {
		List<Category> categorys=Lists.newArrayList();
		List<CmsIndexRegion> list = dao.findAllList(entity);

		for (CmsIndexRegion tempCmsIndexRegion:list) {
			Category tempCategory = tempCmsIndexRegion.getCategory();
			if (tempCategory != null) {
				if (!(categorys.contains(tempCategory))) {
					categorys.add(tempCategory);
				}

				List<CmsIndexRegion> childRegionList = Lists.newArrayList();
				for (CmsIndexRegion tempCmsIndexRegion2:list) {
					if ((tempCmsIndexRegion2.getCategory().equals(tempCategory)) && StringUtils.equals("1",tempCmsIndexRegion.getRegionState())) {
						childRegionList.add(tempCmsIndexRegion2);
					}
				}
				tempCategory.setChildRegionList(childRegionList);
			}
		}
		return categorys;
	}


	/**
	 * 获取区域栏目列表树(支持2级节点)
	 * @param entity
	 * @return
	 */
	public List<Map<String, Object>> getRegionTrees(CmsIndexRegion entity, String extId) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Category> list = getRegionCategorys(entity);

		for (Category e : list) {
			if (extId == null || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)) {
				Map<String, Object> emap = Maps.newHashMap();
				emap.put("id", e.getId());
				emap.put("pId", e.getParent()!=null?e.getParent().getId():0);
				emap.put("name", e.getName());
				emap.put("module", e.getModule());
				mapList.add(emap);
				for (CmsIndexRegion eregion : e.getChildRegionList()) {
					Map<String, Object> ermap = Maps.newHashMap();
					ermap.put("id", eregion.getId());
					ermap.put("pId", e.getId());
					ermap.put("name", eregion.getRegionName());
//					ermap.put("module", "");
					mapList.add(ermap);
				}
			}
		}
		return mapList;
	}
}
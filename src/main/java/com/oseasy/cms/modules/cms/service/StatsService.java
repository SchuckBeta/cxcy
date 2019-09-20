/**
 *
 */
package com.oseasy.cms.modules.cms.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.cms.modules.cms.dao.ArticleDao;
import com.oseasy.cms.modules.cms.entity.Category;
import com.oseasy.cms.modules.cms.entity.Site;
import com.oseasy.com.pcore.common.service.BaseService;
import com.oseasy.com.pcore.modules.sys.entity.Office;

/**
 * 统计Service


 */
@Service
@Transactional(readOnly = true)
public class StatsService extends BaseService {

	@Autowired
	private ArticleDao articleDao;

	public List<Category> article(Map<String, Object> paramMap) {
		Category category = new Category();

		Site site = new Site();
		site.setId(Site.getCurrentSiteId());
		category.setSite(site);

		String categoryId = (String)paramMap.get("categoryId");
		if (categoryId != null && !("".equals(categoryId))) {
			category.setId(categoryId);
			category.setParentIds(categoryId);
		}

		List<Category> list = articleDao.findStats(category);
		return list;
	}

}

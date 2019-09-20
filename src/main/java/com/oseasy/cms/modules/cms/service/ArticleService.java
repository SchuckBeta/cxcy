/**
 *
 */
package com.oseasy.cms.modules.cms.service;

import java.util.Date;
import java.util.List;

import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.cms.modules.cms.dao.ArticleDao;
import com.oseasy.cms.modules.cms.dao.ArticleDataDao;
import com.oseasy.cms.modules.cms.dao.CategoryDao;
import com.oseasy.cms.modules.cms.entity.Article;
import com.oseasy.cms.modules.cms.entity.ArticleData;
import com.oseasy.cms.modules.cms.entity.Category;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.rediserver.common.utils.CacheUtils;

/**
 * 文章Service


 */
@Service
@Transactional(readOnly = true)
public class ArticleService extends CrudService<ArticleDao, Article> {

	@Autowired
	private ArticleDataDao articleDataDao;
	@Autowired
	private CategoryDao categoryDao;

	@Transactional(readOnly = false)
	public Page<Article> findPage(Page<Article> page, Article article, boolean isDataScopeFilter) {
		// 更新过期的权重，间隔为“6”个小时
		Date updateExpiredWeightDate =  (Date)CacheUtils.get("updateExpiredWeightDateByArticle");
		if (updateExpiredWeightDate == null || (updateExpiredWeightDate != null
				&& updateExpiredWeightDate.getTime() < new Date().getTime())) {
			dao.updateExpiredWeight(article);
			CacheUtils.put("updateExpiredWeightDateByArticle", DateUtils.addHours(new Date(), 6));
		}
		if (article.getCategory()!=null && StringUtil.isNotBlank(article.getCategory().getId()) && !Category.isRoot(article.getCategory().getId())) {
			Category category = categoryDao.get(article.getCategory().getId());
			if (category==null) {
				category = new Category();
			}
			category.setParentIds(category.getId());
			category.setSite(category.getSite());
			article.setCategory(category);
		}
		else{
			article.setCategory(new Category());
		}
		return super.findPage(page, article);

	}

	@Transactional(readOnly = false)
	public void save(Article article) {
		if (article.getArticleData().getContent()!=null) {
			article.getArticleData().setContent(StringEscapeUtils.unescapeHtml4(
					article.getArticleData().getContent()));
		}
		// 如果没有审核权限，则将当前内容改为待审核状态
		if (!CoreUtils.getSubject().isPermitted("cms:article:audit")) {
			article.setDelFlag(Article.DEL_FLAG_AUDIT);
		}
		// 如果栏目不需要审核，则将该内容设为发布状态
		if (article.getCategory()!=null&&StringUtil.isNotBlank(article.getCategory().getId())) {
			Category category = categoryDao.get(article.getCategory().getId());
			if (!Const.YES.equals(category.getIsAudit())) {
				article.setDelFlag(Article.DEL_FLAG_NORMAL);
			}
		}
		article.setUpdateBy(UserUtils.getUser());
		article.setUpdateDate(new Date());
        if (StringUtil.isNotBlank(article.getViewConfig())) {
            article.setViewConfig(StringEscapeUtils.unescapeHtml4(article.getViewConfig()));
        }

        ArticleData articleData = null;
		if (StringUtil.isBlank(article.getId())) {
			article.preInsert();
			articleData = article.getArticleData();
			articleData.setId(article.getId());
			dao.insert(article);
			articleDataDao.insert(articleData);
		}else{
			article.preUpdate();
			articleData = article.getArticleData();
			articleData.setId(article.getId());
			dao.update(article);
			articleDataDao.update(article.getArticleData());
		}
	}

	@Transactional(readOnly = false)
	public void delete(Article article, Boolean isRe) {
		super.delete(article);
	}

	/**
	 * 通过编号获取内容标题
	 * @return new Object[]{栏目Id,文章Id,文章标题}
	 */
	public List<Object[]> findByIds(String ids) {
		if (ids == null) {
			return Lists.newArrayList();
		}
		List<Object[]> list = Lists.newArrayList();
		String[] idss = StringUtil.split(ids,",");
		Article e = null;
		for(int i=0;(idss.length-i)>0;i++) {
			e = dao.get(idss[i]);
			list.add(new Object[]{e.getCategory().getId(),e.getId(),StringUtil.abbr(e.getTitle(),50)});
		}
		return list;
	}

	/**
	 * 点击数加一
	 */
	@Transactional(readOnly = false)
	public void updateHitsAddOne(String id) {
		dao.updateHitsAddOne(id);
	}

	/**
	 * 更新索引
	 */
	public void createIndex() {
	    /**
	     * dao.createIndex();
	     */
	}

	/**
	 * 全文检索
	 * FIXME 暂不提供检索功能
	 */
	public Page<Article> search(Page<Article> page, String q, String categoryId, String beginDate, String endDate) {
		return page;
	}

}

package com.oseasy.cms.modules.cms.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.cms.modules.cms.dao.CmsArticleDataDao;
import com.oseasy.cms.modules.cms.entity.CmsArticleData;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 文章详情表Service.
 * @author liangjie
 * @version 2018-09-04
 */
@Service
@Transactional(readOnly = true)
public class CmsArticleDataService extends CrudService<CmsArticleDataDao, CmsArticleData> {

	public CmsArticleData get(String id) {
		return super.get(id);
	}

	public List<CmsArticleData> findList(CmsArticleData cmsArticleData) {
		return super.findList(cmsArticleData);
	}

	public Page<CmsArticleData> findPage(Page<CmsArticleData> page, CmsArticleData cmsArticleData) {
		return super.findPage(page, cmsArticleData);
	}

	@Transactional(readOnly = false)
	public void save(CmsArticleData cmsArticleData) {
		super.save(cmsArticleData);
	}

	@Transactional(readOnly = false)
	public void delete(CmsArticleData cmsArticleData) {
		super.delete(cmsArticleData);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(CmsArticleData cmsArticleData) {
  	  dao.deleteWL(cmsArticleData);
  	}

	public CmsArticleData getCmsArticleDataByContetId(String contentId) {
	  	  return  dao.getCmsArticleDataByContetId(contentId);
	}
}
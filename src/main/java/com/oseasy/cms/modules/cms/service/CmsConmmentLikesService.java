package com.oseasy.cms.modules.cms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.cms.modules.cms.dao.CmsConmmentLikesDao;
import com.oseasy.cms.modules.cms.entity.CmsConmment;
import com.oseasy.cms.modules.cms.entity.CmsConmmentLikes;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 评论Service.
 * @author chenh
 * @version 2018-09-04
 */
@Service
@Transactional(readOnly = true)
public class CmsConmmentLikesService extends CrudService<CmsConmmentLikesDao, CmsConmmentLikes> {


	public CmsConmmentLikes get(String id) {
		CmsConmmentLikes cmsConmmentLikes = super.get(id);
		return cmsConmmentLikes;
	}

	public List<CmsConmmentLikes> findList(CmsConmmentLikes cmsConmmentLikes) {
		return super.findList(cmsConmmentLikes);
	}

	public Page<CmsConmmentLikes> findPage(Page<CmsConmmentLikes> page, CmsConmmentLikes cmsConmmentLikes) {
		return super.findPage(page, cmsConmmentLikes);
	}

	@Transactional(readOnly = false)
	public void save(CmsConmmentLikes cmsConmmentLikes) {
		super.save(cmsConmmentLikes);
	}

	@Transactional(readOnly = false)
	public void delete(CmsConmmentLikes cmsConmmentLikes) {
		super.delete(cmsConmmentLikes);
	}

    @Transactional(readOnly = false)
    public void deleteWL(CmsConmmentLikes cmsConmmentLikes) {
      dao.deleteWL(cmsConmmentLikes);
    }
    @Transactional(readOnly = false)
    public void deleteWLPL(CmsConmmentLikes cmsConmmentLikes) {
        dao.deleteWLPL(cmsConmmentLikes);
    }
}
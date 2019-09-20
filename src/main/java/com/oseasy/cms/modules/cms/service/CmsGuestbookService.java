package com.oseasy.cms.modules.cms.service;

import java.util.List;

import com.oseasy.util.common.utils.StringUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.cms.modules.cms.dao.CmsGuestbookDao;
import com.oseasy.cms.modules.cms.entity.CmsConmment;
import com.oseasy.cms.modules.cms.entity.CmsGuestbook;
import com.oseasy.cms.modules.cms.vo.GbookType;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;

/**
 * 留言Service.
 * @author chenh
 * @version 2018-09-04
 */
@Service
@Transactional(readOnly = true)
public class CmsGuestbookService extends CrudService<CmsGuestbookDao, CmsGuestbook> {

	public CmsGuestbook get(String id) {
		return super.get(id);
	}

	public List<CmsGuestbook> findList(CmsGuestbook cmsGuestbook) {
		return super.findList(cmsGuestbook);
	}

	public Page<CmsGuestbook> findPage(Page<CmsGuestbook> page, CmsGuestbook cmsGuestbook) {
		return super.findPage(page, cmsGuestbook);
	}

	@Transactional(readOnly = false)
	public void save(CmsGuestbook cmsGuestbook) {
		if(StringUtil.isEmpty(cmsGuestbook.getId())){
			cmsGuestbook.setIsNewRecord(true);
		}
	    if(cmsGuestbook.getIsNewRecord()){
            if(cmsGuestbook.getIsRecommend() == null){
                cmsGuestbook.setIsRecommend(Const.NO);
            }
			if(cmsGuestbook.getAuditstatus() == null){
			   cmsGuestbook.setAuditstatus(Const.NO);
		    }
			if(cmsGuestbook.getCreateBy() == null){
				cmsGuestbook.setCreateBy(UserUtils.getUser());
		   	}
        }
		super.save(cmsGuestbook);
	}

	@Transactional(readOnly = false)
 	public void updatePLAudit(List<CmsGuestbook> entitys) {
    	dao.updatePLAudit(entitys);
 	}

	@Transactional(readOnly = false)
	public void delete(CmsGuestbook cmsGuestbook) {
		super.delete(cmsGuestbook);
	}

    @Transactional(readOnly = false)
    public void deletePL(CmsGuestbook cmsGuestbook) {
        dao.deletePL(cmsGuestbook);
    }

  	@Transactional(readOnly = false)
  	public void deleteWL(CmsGuestbook cmsGuestbook) {
  	  dao.deleteWL(cmsGuestbook);
  	}

    @Transactional(readOnly = false)
    public void deleteWLPL(CmsGuestbook cmsGuestbook) {
       dao.deleteWLPL(cmsGuestbook);
    }
}
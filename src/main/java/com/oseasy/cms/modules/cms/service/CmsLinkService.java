package com.oseasy.cms.modules.cms.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.cms.modules.cms.dao.CmsLinkDao;
import com.oseasy.cms.modules.cms.entity.CmsLink;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 友情链接Service.
 * @author zy
 * @version 2018-08-30
 */
@Service
@Transactional(readOnly = true)
public class CmsLinkService extends CrudService<CmsLinkDao, CmsLink> {
	@Autowired
	private CmsLinkDao cmsLinkDao;
	public CmsLink get(String id) {
		return super.get(id);
	}

	public List<CmsLink> findList(CmsLink cmsLink) {
		return super.findList(cmsLink);
	}

	public Page<CmsLink> findPage(Page<CmsLink> page, CmsLink cmsLink) {
		return super.findPage(page, cmsLink);
	}

	@Transactional(readOnly = false)
	public void save(CmsLink cmsLink) {
		super.save(cmsLink);
	}

	@Transactional(readOnly = false)
	public void delete(CmsLink cmsLink) {
		super.delete(cmsLink);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(CmsLink cmsLink) {
  	  dao.deleteWL(cmsLink);
  	}

	@Transactional(readOnly = false)
	public void deletePl(String ids) {
		String[] idStr=ids.split(",");
		List<String> idList=Arrays.asList(idStr);
		cmsLinkDao.delPl(idList);

	}
	@Transactional(readOnly = false)
	public void cmsLinkSaveSort(String ids, String sorts) {
		String[] idStr=ids.split(",");
		List<String> idList=Arrays.asList(idStr);

		String[] sortStr=sorts.split(",");
		List<String> sortList=Arrays.asList(sortStr);
		List<CmsLink> cmsList=new ArrayList<CmsLink>();
		for(int i=0;i<idList.size();i++){
			CmsLink cmsLink=new CmsLink();
			cmsLink.setId(idList.get(i));
			cmsLink.setSort(sortList.get(i));
			cmsList.add(cmsLink);
		}
		cmsLinkDao.cmsLinkSaveSort(cmsList);
	}
	@Transactional(readOnly = false)
	public void cmsLinkSaveSort(List<CmsLink> cmsList) {
		cmsLinkDao.cmsLinkSaveSort(cmsList);
	}

	public List<CmsLink> findFrontList(CmsLink cmsLink) {
		return cmsLinkDao.findFrontList(cmsLink);
	}

	public Boolean checkLinkName(CmsLink cmsLink){
		Integer integer = cmsLinkDao.checkLinkName(cmsLink);
		return integer < 1;
	}
}
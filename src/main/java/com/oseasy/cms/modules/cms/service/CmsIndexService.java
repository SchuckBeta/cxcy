package com.oseasy.cms.modules.cms.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.cms.modules.cms.dao.CmsIndexDao;
import com.oseasy.cms.modules.cms.entity.CmsIndex;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 首页管理Service.
 * @author zy
 * @version 2018-09-03
 */
@Service
@Transactional(readOnly = true)
public class CmsIndexService extends CrudService<CmsIndexDao, CmsIndex> {

	public CmsIndex get(String id) {
		return super.get(id);
	}

	public List<CmsIndex> findList(CmsIndex cmsIndex) {
		return super.findList(cmsIndex);
	}

	public Page<CmsIndex> findPage(Page<CmsIndex> page, CmsIndex cmsIndex) {
		return super.findPage(page, cmsIndex);
	}

	@Transactional(readOnly = false)
	public void save(CmsIndex cmsIndex) {
		super.save(cmsIndex);
	}

	@Transactional(readOnly = false)
	public void delete(CmsIndex cmsIndex) {
		super.delete(cmsIndex);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(CmsIndex cmsIndex) {
  	  dao.deleteWL(cmsIndex);
  	}

	public List<CmsIndex> findIndexList() {
		CmsIndex cmsIndex = getCmsIndex();
		return  dao.findIndexList(cmsIndex);
	}

	private CmsIndex getCmsIndex() {
		CmsIndex cmsIndex = new CmsIndex();
		if(CoreSval.getTenantIsopen()) {
			cmsIndex.setTenantId(TenantConfig.getCacheTenant());
		}else{
			cmsIndex.setTenantId(CoreSval.Const.DEFAULT_SCHOOL_TENANTID);
		}
		return cmsIndex;
	}


	public void cmsIndexSaveSort(List<CmsIndex> cmsIndexList) {
		dao.cmsIndexSaveSort(cmsIndexList);
	}
	@Transactional(readOnly = false)
	public void cmsIndexSaveSort(String ids, String sorts) {
		List<String> idList=Arrays.asList(ids.split(StringUtil.DOTH));
		List<String> sortList= Arrays.asList(sorts.split(StringUtil.DOTH));
		List<CmsIndex> cmsIndexList=new ArrayList<CmsIndex>();
		for(int i=0;i<idList.size();i++){
			CmsIndex cmsLink=new CmsIndex();
			cmsLink.setId(idList.get(i));
			cmsLink.setSort(sortList.get(i));
			cmsIndexList.add(cmsLink);
		}
		dao.cmsIndexSaveSort(cmsIndexList);
	}

	public CmsIndex getByEname(String modelename) {
		return dao.getByModelename(modelename);
	}

	public List<CmsIndex> findInIndexList() {
		CmsIndex cmsIndex = getCmsIndex();
		return  dao.findInIndexList(cmsIndex);
	}

	public List<CmsIndex> findShowIndexList() {
		return  dao.findShowIndexList();
	}

	public List<CmsIndex> findhiddenList() {
		return dao.findhiddenList();
	}
	@Transactional(readOnly = false)
	public void saveIndex(CmsIndex cmsIndex) {
		if(cmsIndex.getHomeSCDT()!=null){
			super.save(cmsIndex.getHomeSCDT());
		}
		if(cmsIndex.getHomeSCTZ()!=null){
			super.save(cmsIndex.getHomeSCTZ());
		}
		if(cmsIndex.getHomeSSDT()!=null){
			super.save(cmsIndex.getHomeSSDT());
		}
		super.save(cmsIndex);
	}
}
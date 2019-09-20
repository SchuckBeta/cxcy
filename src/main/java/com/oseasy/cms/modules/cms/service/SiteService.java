/**
 *
 */
package com.oseasy.cms.modules.cms.service;

import com.oseasy.cms.modules.cms.dao.SiteDao;
import com.oseasy.cms.modules.cms.entity.Category;
import com.oseasy.cms.modules.cms.entity.Site;
import com.oseasy.cms.modules.cms.utils.CmsUtils;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.util.common.utils.StringUtil;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 站点Service


 */
@Service
@Transactional(readOnly = true)
public class SiteService extends CrudService<SiteDao, Site> {
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CategoryService cmsCategoryService;
	@Override
	public Page<Site> findPage(Page<Site> page, Site site) {
		site.getSqlMap().put("site", dataScopeFilter(site.getCurrentUser(), "o", "u"));
		return super.findPage(page, site);
	}

//	@Deprecated
//	@Transactional(readOnly = false)
//	public boolean save(Site site, Office office) {
//		if (site.getIsNewRecord()) {
//			User user = UserUtils.getUser();
//			if (user.getAdmin()) {
//				categoryService.saveNew(site, office);
//			}else{
//				categoryService.saveNew(site, user.getCompany());
//			}
//			save(site);
//			return true;
//		}else{
//      		save(site);
//			return false;
//		}
//	}
	@Override
	@Transactional(readOnly = false)
	public void save(Site site) {
		if (site.getCopyright()!=null) {
			site.setCopyright(StringEscapeUtils.unescapeHtml4(site.getCopyright()));
		}
		super.save(site);
		CmsUtils.removeCache("site_"+site.getId());
		CmsUtils.removeCache("siteList");
	}

	@Transactional(readOnly = false)
	public void saveInto(Site site,HttpServletRequest request) {
		if (site.getCopyright()!=null) {
			site.setCopyright(StringEscapeUtils.unescapeHtml4(site.getCopyright()));
		}
		String id=getLastId();
		Integer nextId=Integer.parseInt(id)+1;

		super.save(site);
		CmsUtils.removeCache("site_"+site.getId());
		CmsUtils.removeCache("siteList");
		//将站点 参数保存到栏目表中
		saveCmsCategoryBySiteId(nextId+"",request);
	}

	@Transactional(readOnly = false)
	public void saveCmsCategoryBySiteId(String nextId,HttpServletRequest request) {
		Site site = getSite();
		List<Category> cmsCategoryList=cmsCategoryService.getListBySiteId(site.getId());
		List<Category> oldCmsCategoryList=cmsCategoryList;
		for(Category cmsCategory:cmsCategoryList){
			cmsCategory.setSite(new Site(nextId));
		}
		cmsCategoryService.saveList(cmsCategoryList);
		cmsCategoryList=cmsCategoryService.getListBySiteId(nextId);

		List<Category> updateCmsCategoryList=new ArrayList<Category>();
		//兑换parentId
		for(Category category:cmsCategoryList){
			if(!"1".equals(category.getParent())){
				for(int i=0;i<oldCmsCategoryList.size();i++){
					if(category.getParentId().equals(oldCmsCategoryList.get(i).getId())){
						category.setParentId(cmsCategoryList.get(i).getId());
						updateCmsCategoryList.add(category);
					}
				}
			}
		}
		if(StringUtil.checkNotEmpty(updateCmsCategoryList)){
			cmsCategoryService.updateList(updateCmsCategoryList);
		}
	}

	private Site getSite() {
		Site site = new Site();
		if(CoreSval.getTenantIsopen()) {
			site.setTenantId(TenantConfig.getCacheTenant());
		}else{
			site.setTenantId(CoreSval.Const.DEFAULT_SCHOOL_TENANTID);
		}
		site=dao.getAutoSite(site);
		return site;
	}

	@Transactional(readOnly = false)
	public void delete(Site site, Boolean isRe) {
		site.setDelFlag(isRe!=null&&isRe?Site.DEL_FLAG_NORMAL:Site.DEL_FLAG_DELETE);
		super.delete(site);
		CmsUtils.removeCache("site_"+site.getId());
		CmsUtils.removeCache("siteList");
	}

	@Transactional(readOnly = false)
	public void saveNewId(Site site,HttpServletRequest request) {
		saveInto(site,request);
	}

	private String getLastId() {
		String id=dao.getLastId();
		if(StringUtil.isEmpty(id)){
			id="10000";
		}
		return id;
	}

	public boolean getFirstSite() {
		int i=dao.getFirstSite();
		if(i>0){
			return false;
		}
		return true;
	}
	@Transactional(readOnly = false)
	public void siteChange(Site site) {
		if(CoreSval.getTenantIsopen()) {
			site.setTenantId(TenantConfig.getCacheTenant());
		}else{
			site.setTenantId(CoreSval.Const.DEFAULT_SCHOOL_TENANTID);
		}
		Site siteAuto=dao.getAutoSite(site);
		//取消默认站点
		dao.changeAutoSite(siteAuto.getId());
		//保存新默认站点
		dao.update(site);
		//切换siteId id
		CoreUtils.putCache("siteId",site.getId());

	}


	public Site getAutoSite() {
		Site site = new Site();
		if(CoreSval.getTenantIsopen()) {
			site.setTenantId(TenantConfig.getCacheTenant());
		}else{
			site.setTenantId(CoreSval.Const.DEFAULT_SCHOOL_TENANTID);
		}
		return dao.getAutoSite(site);
	}

	@Transactional(readOnly = false)
	public void updateSite(Site site) {
		dao.update(site);
	}
	@Transactional(readOnly = false)
	public void siteDelete(Site site) {
		super.delete(site);
		cmsCategoryService.delBySiteId(site.getId());
	}
}

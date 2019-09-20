package com.oseasy.pro.modules.gcontest.service;

import java.util.List;

import com.oseasy.cms.common.config.CmsIds;
import com.oseasy.cms.modules.cms.entity.Category;
import com.oseasy.cms.modules.cms.service.CategoryService;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.sys.common.config.SysIds;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.pro.modules.gcontest.dao.GContestAnnounceDao;
import com.oseasy.pro.modules.gcontest.entity.GContestAnnounce;

/**
 * 大赛通告表Service
 * @author zdk
 * @version 2017-03-29
 */
@Service
@Transactional(readOnly = true)
public class GContestAnnounceService extends CrudService<GContestAnnounceDao, GContestAnnounce> {
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private SystemService systemService;

	public GContestAnnounce get(String id) {
		return super.get(id);
	}

	public List<GContestAnnounce> findList(GContestAnnounce gContestAnnounce) {
		return super.findList(gContestAnnounce);
	}

	public Page<GContestAnnounce> findPage(Page<GContestAnnounce> page, GContestAnnounce gContestAnnounce) {
		return super.findPage(page, gContestAnnounce);
	}

	@Transactional(readOnly = false)
	public void save(GContestAnnounce gContestAnnounce) {
		super.save(gContestAnnounce);
	}

	@Transactional(readOnly = false)
	public void delete(GContestAnnounce gContestAnnounce) {
		super.delete(gContestAnnounce);
	}

	public GContestAnnounce getGContestAnnounce(GContestAnnounce gContestAnnounce) {
		List<GContestAnnounce>  gContestAnnounces=dao.getGContestAnnounce(gContestAnnounce);
		if (gContestAnnounces!=null&&gContestAnnounces.size()>0) {
			return gContestAnnounces.get(0);
		}
		return null;
	}

	public GContestAnnounce getGContestByName(String name) {
		return dao.getGContestByName(name);
	}

	@Transactional(readOnly = false)
	public void saveGContestAnnounce(GContestAnnounce gContestAnnounce) {
		//生成栏目表
		Category category=new Category();
		//category.setParent(new Category(CmsIds.SITE_CATEGORYS_SYS_ROOT.getId()));
		Category parent = categoryService.get(CmsIds.SITE_CATEGORYS_TOP_ROOT.getId());
		category.setParent(parent);
		category.setSite(parent.getSite());
//		category.setOffice(parent.getOffice());
		category.setName(gContestAnnounce.getgName());
//		category.setInMenu(CoreSval.SHOW);
//		category.setInList(CoreSval.SHOW);
    category.setIsAudit(Const.NO);
		category.setSort(40);
		categoryService.save(category);
		//生成后台菜单
		Menu menu=new Menu();
		menu.setParent(systemService.getMenu(Menu.getRootId()));
		menu.setName(gContestAnnounce.getgName());
		menu.setIsShow("1");
		menu.setSort(10);
		systemService.saveMenu(menu);

		save(gContestAnnounce);
	}
}
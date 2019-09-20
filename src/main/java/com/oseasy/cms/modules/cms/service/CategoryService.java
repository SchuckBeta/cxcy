package com.oseasy.cms.modules.cms.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.oseasy.cms.common.config.CmsIds;
import com.oseasy.cms.common.config.SysCacheKeys;
import com.oseasy.cms.modules.cms.dao.CategoryDao;
import com.oseasy.cms.modules.cms.dao.SiteDao;
import com.oseasy.cms.modules.cms.entity.Category;
import com.oseasy.cms.modules.cms.entity.Site;
import com.oseasy.cms.modules.cms.utils.CmsUtils;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.TreeService;
import com.oseasy.com.pcore.common.utils.license.License;
import com.oseasy.com.pcore.modules.authorize.enums.MenuEnum;
import com.oseasy.com.pcore.modules.authorize.enums.MenuPlusEnum;
import com.oseasy.com.pcore.modules.authorize.enums.TenantCategory;
import com.oseasy.com.pcore.modules.authorize.service.AuthorizeService;
import com.oseasy.com.pcore.modules.authorize.vo.IAuthCheck;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.DomainUtils;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.pcore.modules.sys.vo.TenantCvtype;
import com.oseasy.com.pcore.modules.syt.service.SysTenantService;
import com.oseasy.com.rediserver.common.utils.JedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 栏目管理Service.
 * @author liangjie
 * @version 2018-08-30
 */
@Service
@Transactional(readOnly = true)
public class CategoryService extends TreeService<CategoryDao, Category> implements IAuthCheck {
    @Autowired
    private SiteDao siteDao;

    @Autowired
    private SysTenantService sysTenantService;
    @Autowired
    private AuthorizeService authorizeService;
    public static final String CACHE_CATEGORY_LIST = "categoryList";

    private Category entity = new Category();

	@Override
	public Integer checkRltype(String id) {
		Category category = super.get(id);
		if (category == null){
			return null;
		}
		return category.getLtype();
	}
	@Override
	public boolean checked(Integer id) {
		if (id == null){
			return true;
		}
		Integer ltype = id;
		String tenantId = null;
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		String domain = request.getServerName();
		if(CoreSval.getTenantIsopen()){
			//按租户存储
			try {
				tenantId = (String) JedisUtils.hashGet("tenant",domain);
				if (tenantId == null){
					tenantId =sysTenantService.getDomainByName(domain);
				}
				if (tenantId != null){
					JedisUtils.hashSetKey("tenant",domain,tenantId);
				}
			} catch (Exception e) {
				logger.error("解析域名失败",e);
				throw new RuntimeException("获取域名失败");
			}
		}
		License license = authorizeService.getLicenseInfo(tenantId);
		if (license == null || "0".equals(license.getValid())) {
			return false;
		}
		//核对栏目
		TenantCategory tenantCategory = TenantCategory.getById(ltype);
		if (tenantCategory == null){
			return false;
		}
		if (ltype<0 || license.getModules().length() <= ltype) {
			return false;
		}
		if (authorizeService.getAuthorizeResult(tenantId, license, ltype)){
			return true;
		}

		return false;
	}

	@SuppressWarnings("unchecked")
    public List<Category> find(boolean isCurrentSite, String module) {

        List<Category> list = (List<Category>)CoreUtils.getCache(CACHE_CATEGORY_LIST);
        if (list == null) {
            Category category = new Category();
            //category.setOffice(new Office());
            category.setSite(new Site());
            category.setParent(new Category());
            list = dao.findList(category);
            // 将没有父节点的节点，找到父节点
            Set<String> parentIdSet = Sets.newHashSet();
            for (Category e : list) {
                if (e.getParent()!=null && StringUtils.isNotBlank(e.getParent().getId())) {
                    boolean isExistParent = false;
                    for (Category e2 : list) {
                        if (e.getParent().getId().equals(e2.getId())) {
                            isExistParent = true;
                            break;
                        }
                    }
                    if (!isExistParent) {
                        parentIdSet.add(e.getParent().getId());
                    }
                }
            }
            if (parentIdSet.size() > 0) {
                //FIXME 暂且注释，用于测试
//              dc = dao.createDetachedCriteria();
//              dc.add(Restrictions.in("id", parentIdSet));
//              dc.add(Restrictions.eq("delFlag", Category.DEL_FLAG_NORMAL));
//              dc.addOrder(Order.asc("site.id")).addOrder(Order.asc("sort"));
//              list.addAll(0, dao.find(dc));
            }
            CoreUtils.putCache(CACHE_CATEGORY_LIST, list);
        }

        if (isCurrentSite) {
            List<Category> categoryList = Lists.newArrayList();
            for (Category e : list) {
                if (Category.isRoot(e.getId()) || (e.getSite()!=null && e.getSite().getId() !=null
                        && e.getSite().getId().equals(Site.getCurrentSiteId()))) {
                    if (StringUtils.isNotEmpty(module)) {
                        if (module.equals(e.getModule()) || "".equals(e.getModule())) {
                            categoryList.add(e);
                        }
                    }else{
                        categoryList.add(e);
                    }
                }
            }
            return categoryList;
        }
        return list;
    }

    public List<Category> findByParentId(String parentId, String siteId) {
        Category parent = new Category();
        parent.setId(parentId);
        entity.setParent(parent);
        Site site = new Site();
        site.setId(siteId);
        entity.setSite(site);
        return dao.findByParentIdAndSiteId(entity);
    }





    /**
     * 获取当前机构官网首页栏目菜单(支持2/3级节点)
     * @param parentId
     * @return
     */
    public Map<String, List<Category>> getCategoryTrees(String parentId) {
        if (StringUtils.isEmpty(parentId)) {
            return null;
        }

        Map<String, List<Category>> trees = new HashMap<String, List<Category>>();
        List<Category> firstCategory=Lists.newArrayList();
        List<Category> secondCategorys=Lists.newArrayList();

        List<Category> threeCategorys=Lists.newArrayList();

        List<Category> list = Lists.newArrayList();
        Category first = new Category();
        first.setName("首页");
        first.setId("1");
        first.setParentId("0");
        first.setIsNewtab(0);
        first.setHref("/");
        secondCategorys.add(first);
        Category m = new Category();
        m.setParentIds("0");
//        m.setInMenu(CoreSval.SHOW);
        m.setIsShow(1);
        if(CoreSval.getTenantIsopen()) {
           m.setTenantId(TenantConfig.getCacheTenant());
        }else{
            m.setTenantId(CoreSval.Const.DEFAULT_SCHOOL_TENANTID);
        }
        List<Category> sourcelist = findByParentIdsLike(m);
        Category.sortList(list, sourcelist, parentId);

        //firstCategory.add(first);
        trees.put(SysCacheKeys.SITE_CATEGORYS_INDEX_FIRST.getKey(), firstCategory);


        for (Category tempCategory:list) {
            if (tempCategory.getParentId().equals(parentId)) {
//                if (StringUtils.equals("1",tempCategory.getInMenu())) {
                    secondCategorys.add(tempCategory);
//                }
            }else{
                threeCategorys.add(tempCategory);
            }
        }


        for (Category tempCategory2:secondCategorys) {
            List<Category> children=Lists.newArrayList();
            for (Category tempCategory3:threeCategorys) {
                if (tempCategory3.getParentId().equals(tempCategory2.getId())) {
//                    if (StringUtils.equals("1",tempCategory3.getInMenu())) {
                        children.add(tempCategory3);
//                    }
                }
                List<Category> children3=Lists.newArrayList();
                for(Category tempCategory4:threeCategorys){
                    if(tempCategory4.getParentId().equals(tempCategory3.getId())){
                        children3.add(tempCategory4);

                    }
                }
                tempCategory3.setChildList(children3);
            }
            tempCategory2.setChildList(children);
        }

        trees.put(SysCacheKeys.SITE_CATEGORYS_INDEX_SENCOND.getKey(),secondCategorys);
        return trees;
    }

    public Page<Category> find(Page<Category> page, Category category) {
//      DetachedCriteria dc = dao.createDetachedCriteria();
//      if (category.getSite()!=null && StringUtils.isNotBlank(category.getSite().getId())) {
//          dc.createAlias("site", "site");
//          dc.add(Restrictions.eq("site.id", category.getSite().getId()));
//      }
//      if (category.getParent()!=null && StringUtils.isNotBlank(category.getParent().getId())) {
//          dc.createAlias("parent", "parent");
//          dc.add(Restrictions.eq("parent.id", category.getParent().getId()));
//      }
//      if (StringUtils.isNotBlank(category.getInMenu()) && Category.SHOW.equals(category.getInMenu())) {
//          dc.add(Restrictions.eq("inMenu", category.getInMenu()));
//      }
//      dc.add(Restrictions.eq(Category.FIELD_DEL_FLAG, Category.DEL_FLAG_NORMAL));
//      dc.addOrder(Order.asc("site.id")).addOrder(Order.asc("sort"));
//      return dao.find(page, dc);
//      page.setSpringPage(dao.findByParentId(category.getParent().getId(), page.getSpringPage()));
//      return page;
        category.setPage(page);
//        category.setInMenu(CoreSval.SHOW);
        page.setList(dao.findModule(category));
        return page;
    }

    @Transactional(readOnly = false)
    public void save(Category category) {
        //1
        Site site = new Site();
        if(CoreSval.getTenantIsopen()) {
            site.setTenantId(TenantConfig.getCacheTenant());
        }else{
            site.setTenantId(CoreSval.Const.DEFAULT_SCHOOL_TENANTID);
        }
        site=siteDao.getAutoSite(site);
        category.setSite(site);
//        if (StringUtils.isNotBlank(category.getViewConfig())) {
//            category.setViewConfig(StringEscapeUtils.unescapeHtml4(category.getViewConfig()));
//        }
        super.save(category);
        CoreUtils.removeCache(CACHE_CATEGORY_LIST);
        CmsUtils.removeCache("mainNavList_"+category.getSite().getId());
    }

    @Transactional(readOnly = false)
    public void delete(Category category) {
        Category c=get(category.getId());
        if(c!=null){
            super.delete(c);
            CoreUtils.removeCache(CACHE_CATEGORY_LIST);
            CmsUtils.removeCache("mainNavList_"+c.getSite().getId());
        }
    }

    @Transactional(readOnly = false)
    public Category saveNew(Site site, Office office) {
        Category root = get(CmsIds.SITE_CATEGORYS_SYS_ROOT.getId());
        Category category = new Category();
        category.setParent(root);
        category.setSite(site);
//        category.setOffice(office);
//        category.setInMenu(CoreSval.SHOW);
//        category.setInList(CoreSval.SHOW);
        category.setShowModes("0");
        category.setAllowComment(Const.NO);
        category.setIsAudit(Const.NO);
        category.setName(site.getName()+"根栏目");
        category.setRemarks("系统自动创建["+site.getName()+"根栏目],禁止删除");
        super.save(category);

        CoreUtils.removeCache(CACHE_CATEGORY_LIST);
        CmsUtils.removeCache("mainNavList_"+category.getSite().getId());
        return category;
    }

    /**
     * 通过编号获取栏目列表
     */
    public List<Category> findByIds(String ids) {
        List<Category> list = Lists.newArrayList();
        String[] idss = StringUtils.split(ids,",");
        if (idss.length>0) {
//          List<Category> l = dao.findByIdIn(idss);
//          for (String id : idss) {
//              for (Category e : l) {
//                  if (e.getId().equals(id)) {
//                      list.add(e);
//                      break;
//                  }
//              }
//          }
            for(String id : idss) {
                Category e = dao.get(id);
                if (null != e) {
                    //System.out.println("e.id:"+e.getId()+",e.name:"+e.getName());
                    list.add(e);
                }
                //list.add(dao.get(id));

            }
        }
        return list;
    }

    /**
     * 根据parentIds获取栏目分类
     * @param parentIds
     * @return
     */
    public List<Category> findByParentIdsLike(String parentIds) {
        Category m = new Category();
        m.setParentIds(parentIds);
        return findByParentIdsLike(m);
    }
    public List<Category> findByParentIdsLike(Category m) {
        return dao.findByParentIdsLike(m);
    }

    public Category findByName(String meunname) {
        return dao.getCategoryByName(meunname);
    }
    public void updateHref(String id,String href) {
        dao.updateHref(id,href);
    }


	@Autowired
	private CategoryDao cmsCategoryDao;
	public Category get(String id) {
		return super.get(id);
	}

	public List<Category> findList(Category cmsCategory) {
		return super.findList(cmsCategory);
	}

	public Page<Category> findPage(Page<Category> page, Category cmsCategory) {
		return super.findPage(page, cmsCategory);
	}


  	@Transactional(readOnly = false)
  	public void deleteWL(Category cmsCategory) {
  	  dao.deleteWL(cmsCategory);
  	}

	@Transactional(readOnly = false)
	public void updateOrder(String ids,String sorts){
		String[] idList = ids.split(",");
		String[] sortList = sorts.split(",");
		List<Category> cmsCategoryList = new ArrayList<>();
		for(int i=0;i<idList.length;i++){
			Category cmsCategory = new Category();
			cmsCategory.setId(idList[i]);
			cmsCategory.setSort(Integer.valueOf(sortList[i]));
			cmsCategoryList.add(cmsCategory);
		}

		cmsCategoryDao.updateOrder(cmsCategoryList);
	}
	@Transactional(readOnly = false)
	public void update(Category cmsCategory){
		dao.update(cmsCategory);
	}

	@Transactional(readOnly = false)
	public List<Category> getListBySiteId(String siteId) {
		return dao.getListBySiteId(siteId);
	}

	@Transactional(readOnly = false)
	public void  saveList(List<Category> cmsCategoryList) {
		dao.saveList(cmsCategoryList);
	}

	@Transactional(readOnly = false)
	public void updateShow(Category cmsCategory){dao.updateShow(cmsCategory);}
	@Transactional(readOnly = false)
	public void delBySiteId(String siteId) {
		dao.delBySiteId(siteId);
	}
	@Transactional(readOnly = false)
	public void updateList(List<Category> cmsCategoryList) {
		dao.updateList(cmsCategoryList);
	}

	public Category getByIdAndType(String siteId, String type) {
		return dao.getByIdAndType(siteId,type);
	}

    public List<Dict> getProCategoryByActywId(String actywId){
        return dao.getProCategoryByActywId(actywId);
    }

    @Transactional(readOnly = false)
	public void deleteByCheck(Category category) {
		Category c=get(category.getId());
		if(c!=null){
			if(StringUtils.isNotEmpty(c.getHref())){

			}
			super.delete(c);
			CoreUtils.removeCache(CACHE_CATEGORY_LIST);
			CmsUtils.removeCache("mainNavList_"+c.getSite().getId());
		}
	}


	public Category getByPublishCategory(Category category){
		return dao.getByPublishCategory(category);
	}
}
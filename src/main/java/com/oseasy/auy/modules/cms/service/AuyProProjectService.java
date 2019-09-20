package com.oseasy.auy.modules.cms.service;

import com.oseasy.act.common.config.ActTypes;
import com.oseasy.cms.modules.cms.service.SiteService;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.service.ActYwGtimeService;
import com.oseasy.act.modules.pro.service.ProProjectService;
import com.oseasy.auy.modules.cms.entity.AuyProProject;
import com.oseasy.cms.common.config.CmsIds;
import com.oseasy.cms.modules.cms.entity.Category;
import com.oseasy.cms.modules.cms.service.CategoryService;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 创建项目Service.
 * @author zhangyao
 * @version 2017-06-15
 */
@Service
@Transactional(readOnly = true)
public class AuyProProjectService{
    @Autowired
    private SiteService siteService;
    @Autowired
    private CategoryService categoryService;

	@Autowired
	private SystemService systemService;
	@Autowired
	ActYwGtimeService actYwGtimeService;

    //创建栏目
    @Transactional(readOnly = false)
    public void createCategory(AuyProProject proProject,ActYw actYw) {
        Category category=new Category();
        Category parent = categoryService.get(CmsIds.SITE_CATEGORYS_TOP_ROOT.getId());
        category.setParent(parent);
        category.setSite(parent.getSite());
//      category.setOffice(parent.getOffice());
        category.setName(proProject.getProjectName());
        category.setDescription(proProject.getContent());
//      category.setInMenu(CoreSval.SHOW);
//      category.setInList(CoreSval.SHOW);
        category.setIsAudit(Const.NO);
        category.setSort(40);
        categoryService.save(category);
        //默认添加申报表单
        Category categoryapp = new Category();

        categoryapp.setParent(category);
        categoryapp.setSite(category.getSite());
//      categoryapp.setOffice(category.getOffice());
        categoryapp.setName(proProject.getProjectName());
        categoryapp.setDescription(proProject.getContent());
//      categoryapp.setInMenu(CoreSval.SHOW);
//      categoryapp.setInList(CoreSval.SHOW);
        categoryapp.setIsAudit(Const.NO);
        categoryapp.setHref("/form/"+proProject.getProjectMark()+"/applyForm?id="+actYw.getId());

        categoryapp.setSort(40);
        categoryService.save(categoryapp);

        proProject.setCategory(category);
    }

//    //屏蔽以发布流程
//    @Transactional(readOnly = false)
//    public void savedis(AuyProProject proProject) {
//        Menu menu = proProject.getMenu();
//        if (StringUtil.isNotEmpty(proProject.getMenuRid())) {
//            menu = systemService.getMenu(proProject.getMenuRid());
//        }
//        Category category = proProject.getCategory();
//        if ((category == null) && StringUtil.isNotEmpty(proProject.getCategoryRid())) {
//            category = categoryService.get(proProject.getCategoryRid());
//        }
//        if (category!=null) {
////          category.setInMenu(CoreSval.HIDE);
//            category.setName(proProject.getProjectName());
//            category.setDescription(proProject.getContent());
//            categoryService.save(category);
//            proProject.setCategory(category);
//        }
//        if (menu!=null) {
//            menu.setIsShow(Const.HIDE);
//            menu.setParent(systemService.getMenu(Menu.getRootId()));
//            menu.setName(proProject.getProjectName());
//            menu.setRemarks(proProject.getContent());
//            menu.setImgUrl(proProject.getImgUrl());
//            systemService.saveMenu(menu);
//            proProject.setMenu(menu);
//        }
//        save(proProject);
//    }


    /**
     * 获取当前流程的菜单.
     * @param actType ActTypes
     * @return Menu
     */
    @Transactional(readOnly = false)
    public Menu getActMenu(ActTypes actType) {
        if(actType == null){
            return null;
        }

        User user = CoreUtils.getUser();
        /**
         * 根据租户模板获取菜单.
         */
        Menu m = new Menu();
        m.setTenantId(CoreSval.getCurrpntplTenantByType(user));
        if (StringUtil.isEmpty(m.getTenantId())) {
            return null;
        }
        m.setRid(actType.getId());
        Menu tpl = systemService.getByRid(m);
        /**
         * 判断当前租户是否为模板租户.
         */
        if(CoreIds.checkTpl(TenantConfig.getCacheTenant(user.getId())) && CoreIds.checkTpl(m.getTenantId()) ){
            return tpl;
        }

        /**
         * 获取当前租户对应的发布菜单.
         */
        Menu cur = new Menu();
        cur.setRid(tpl.getId());
        return systemService.getByRid(cur);
    }

    /**
     * 获取当前流程的栏目.
     * @param actType ActTypes
     * @return Category
     */
    @Transactional(readOnly = false)
    public Category getActCategory(ActTypes actType) {
        if(actType == null){
            return null;
        }

        /**
         * 根据租户模板获取菜单.
         */
        Category c = new Category();
        c.setSite(siteService.getAutoSite());
        c.setPublishCategory(actType.getId());
        return categoryService.getByPublishCategory(c);
    }
}
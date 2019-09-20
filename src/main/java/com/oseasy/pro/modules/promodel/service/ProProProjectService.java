package com.oseasy.pro.modules.promodel.service;

import com.oseasy.cms.common.config.CmsIds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.act.modules.pro.service.ProProjectService;
import com.oseasy.cms.modules.cms.entity.Category;
import com.oseasy.cms.modules.cms.service.CategoryService;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.sys.common.config.SysIds;

/**
 * 创建项目Service.
 * @author zhangyao
 * @version 2017-06-15
 */
@Service
@Transactional(readOnly = true)
public class ProProProjectService {
    @Autowired
    private SystemService systemService;
    @Autowired
    private ProProjectService proProjectService;
    @Autowired
    private CategoryService categoryService;

    @Transactional(readOnly = false)
    public void saveProProject(ProProject proProject) {
        //生成栏目表
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
        category.setSort(10);
        categoryService.save(category);

        //生成后台菜单
        Menu menu=new Menu();
        menu.setParent(systemService.getMenu(Menu.getRootId()));
        menu.setName(proProject.getProjectName());
        menu.setIsShow(Const.SHOW);
        menu.setRemarks(proProject.getContent());
        menu.setSort(10);
        systemService.saveMenu(menu);

        proProjectService.save(proProject);
    }
}
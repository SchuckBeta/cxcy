package com.oseasy.pro.modules.promodel.tool.process.vo;

import com.oseasy.act.modules.actyw.tool.process.vo.FlowPcms;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowPmenu;
import com.oseasy.act.modules.actyw.tool.process.vo.FormTheme;

/**
 * 表单表单组类型.
 * @author chenhao
 *
 */
public class FormThemeVo {
    private FormTheme theme;
    private FormPage page;
    private FlowPcms cms;
    private FlowPmenu menu;

    public FormThemeVo() {
        super();
    }

    public FormThemeVo(FormTheme theme, FormPage page, FlowPcms cms, FlowPmenu menu) {
        super();
        this.theme = theme;
        this.page = page;
        this.cms = cms;
        this.menu = menu;
    }

    /**
     * 根据theme获取FormThemeVo .
     * @author chenhao
     * @param ptype 项目惟一标识
     * @param pageKey 页面惟一标识
     * @param cmsKey 栏目惟一标识
     * @param menuKey 菜单惟一标识
     * @return FormThemeVo
     */
    public static FormThemeVo getByKey(FormTheme theme, String ptype, String pageKey, String cmsKey, String menuKey) {
        FormPage page = FormPage.getByKey(theme, ptype, pageKey);
        FlowPcms cms = FlowPcms.getByKey(theme, cmsKey);
        FlowPmenu menu = FlowPmenu.getByKey(theme, menuKey);
        return new FormThemeVo(theme, page, cms, menu);
    }


    public FormTheme getTheme() {
        return theme;
    }
    public void setTheme(FormTheme theme) {
        this.theme = theme;
    }
    public FormPage getPage() {
        return page;
    }
    public void setPage(FormPage page) {
        this.page = page;
    }
    public FlowPcms getCms() {
        return cms;
    }
    public void setCms(FlowPcms cms) {
        this.cms = cms;
    }
    public FlowPmenu getMenu() {
        return menu;
    }
    public void setMenu(FlowPmenu menu) {
        this.menu = menu;
    }
}

package com.oseasy.pro.modules.promodel.tool.process.vo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.oseasy.act.common.config.ActSval;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.tool.process.IpageParam;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowPcms;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowPmenu;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowProjectType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormPageType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormTheme;
import com.oseasy.com.pcore.common.config.CorePages;
import com.oseasy.pro.modules.promodel.tool.process.impl.FppCom;
import com.oseasy.pro.modules.promodel.tool.process.impl.FppHsxm;
import com.oseasy.pro.modules.promodel.tool.process.impl.FppMd;
import com.oseasy.pro.modules.promodel.tool.process.impl.FppMr;
import com.oseasy.pro.modules.promodel.tool.process.impl.FppTest;
import com.oseasy.pro.modules.promodel.tool.process.impl.FppTlxy;
import com.oseasy.pro.modules.promodel.tool.process.impl.GppGzsmxx;
import com.oseasy.pro.modules.promodel.tool.process.impl.GppMd;
import com.oseasy.pro.modules.promodel.tool.process.impl.GppMdGc;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 流程主题固定页面.
 * 对应 FlowPage .
 *
 * @author chenhao
 */
public enum FormPage {
    FPMR_APPLY(FormTheme.F_MR, FlowProjectType.PMT_XM, new FppMr(), FormPageType.FPT_APPLY.getKey(), "mr_applyForm"),
    FPMR_INDEX(FormTheme.F_MR, FlowProjectType.PMT_XM, new FppMr(), FormPageType.FPT_INDEX.getKey(), ActSval.ASD_HOME),
    FPMR_VIEWF(FormTheme.F_MR, FlowProjectType.PMT_XM, new FppMr(), FormPageType.FPT_VIEWF.getKey(), "mr_viewForm"),
    FPMR_VIEWA(FormTheme.F_MR, FlowProjectType.PMT_XM, new FppMr(), FormPageType.FPT_VIEWA.getKey(), "mr_viewAdminForm"),

    FPCM_APPLY(FormTheme.F_COM, FlowProjectType.PMT_XM, new FppCom(), FormPageType.FPT_APPLY.getKey(), "common_applyForm"),
    FPCM_INDEX(FormTheme.F_COM, FlowProjectType.PMT_XM, new FppCom(), FormPageType.FPT_INDEX.getKey(), ActSval.ASD_HOME),
    FPCM_VIEWF(FormTheme.F_COM, FlowProjectType.PMT_XM, new FppCom(), FormPageType.FPT_VIEWF.getKey(), "common_viewForm"),
    FPCM_VIEWA(FormTheme.F_COM, FlowProjectType.PMT_XM, new FppCom(), FormPageType.FPT_VIEWA.getKey(), "common_viewAdminForm"),

    FPMD_APPLY(FormTheme.F_MD, FlowProjectType.PMT_XM, new FppMd(), FormPageType.FPT_APPLY.getKey(), "md_applyForm"),
    FPMD_INDEX(FormTheme.F_MD, FlowProjectType.PMT_XM, new FppMd(), FormPageType.FPT_INDEX.getKey(), ActSval.ASD_HOME),
    FPMD_VIEWF(FormTheme.F_MD, FlowProjectType.PMT_XM, new FppMd(), FormPageType.FPT_VIEWF.getKey(), "md_viewForm"),
    FPMD_VIEWA(FormTheme.F_MD, FlowProjectType.PMT_XM, new FppMd(), FormPageType.FPT_VIEWA.getKey(), "md_viewAdminForm"),

    FPMDXM_APPLY(FormTheme.F_MD_XM, FlowProjectType.PMT_XM, new FppMd(), FormPageType.FPT_APPLY.getKey(), "mdXm_applyForm"),
    FPMDXM_INDEX(FormTheme.F_MD_XM, FlowProjectType.PMT_XM, new FppMd(), FormPageType.FPT_INDEX.getKey(), ActSval.ASD_HOME),
    FPMDXM_VIEWF(FormTheme.F_MD_XM, FlowProjectType.PMT_XM, new FppMd(), FormPageType.FPT_VIEWF.getKey(), "mdXm_viewForm"),
    FPMDXM_VIEWA(FormTheme.F_MD_XM, FlowProjectType.PMT_XM, new FppMd(), FormPageType.FPT_VIEWA.getKey(), "mdXm_viewAdminForm"),

    FGMD_APPLY(FormTheme.F_MD, FlowProjectType.PMT_DASAI, new GppMd(), FormPageType.FPT_APPLY.getKey(), "md_applyForm"),
    FGMD_INDEX(FormTheme.F_MD, FlowProjectType.PMT_DASAI, new GppMd(), FormPageType.FPT_INDEX.getKey(), ActSval.ASD_HOME),
    FGMD_VIEWF(FormTheme.F_MD, FlowProjectType.PMT_DASAI, new GppMd(), FormPageType.FPT_VIEWF.getKey(), "md_viewForm"),
    FGMD_VIEWA(FormTheme.F_MD, FlowProjectType.PMT_DASAI, new GppMd(), FormPageType.FPT_VIEWA.getKey(), "md_viewAdminForm"),

    FGMD_COM_APPLY(FormTheme.F_MD_COM, FlowProjectType.PMT_DASAI, new GppMd(), FormPageType.FPT_APPLY.getKey(), "md_applyForm"),
    FGMD_COM_INDEX(FormTheme.F_MD_COM, FlowProjectType.PMT_DASAI, new GppMd(), FormPageType.FPT_INDEX.getKey(), ActSval.ASD_HOME),
    FGMD_COM_VIEWF(FormTheme.F_MD_COM, FlowProjectType.PMT_DASAI, new GppMd(), FormPageType.FPT_VIEWF.getKey(), "md_viewForm"),
    FGMD_COM_VIEWA(FormTheme.F_MD_COM, FlowProjectType.PMT_DASAI, new GppMd(), FormPageType.FPT_VIEWA.getKey(), "md_viewAdminForm"),

    FMDGC_APPLY(FormTheme.F_MD_GC, FlowProjectType.PMT_DASAI, new GppMdGc(), FormPageType.FPT_APPLY.getKey(), "mdGc_applyForm"),
    FMDGC_INDEX(FormTheme.F_MD_GC, FlowProjectType.PMT_DASAI, new GppMdGc(), FormPageType.FPT_INDEX.getKey(), ActSval.ASD_HOME),
    FMDGC_VIEWF(FormTheme.F_MD_GC, FlowProjectType.PMT_DASAI, new GppMdGc(), FormPageType.FPT_VIEWF.getKey(), "mdGc_viewForm"),
    FMDGC_VIEWA(FormTheme.F_MD_GC, FlowProjectType.PMT_DASAI, new GppMdGc(), FormPageType.FPT_VIEWA.getKey(), "mdGc_viewAdminForm"),


    FNPGC_APPLY(FormTheme.F_NP_GC, FlowProjectType.PMT_DASAI, new GppMdGc(), FormPageType.FPT_APPLY.getKey(), "npGc_applyForm"),
    FNPGC_INDEX(FormTheme.F_NP_GC, FlowProjectType.PMT_DASAI, new GppMdGc(), FormPageType.FPT_INDEX.getKey(), ActSval.ASD_HOME),
    FNPGC_VIEWF(FormTheme.F_NP_GC, FlowProjectType.PMT_DASAI, new GppMdGc(), FormPageType.FPT_VIEWF.getKey(), "npGc_viewForm"),
    FNPGC_VIEWA(FormTheme.F_NP_GC, FlowProjectType.PMT_DASAI, new GppMdGc(), FormPageType.FPT_VIEWA.getKey(), "npGc_viewAdminForm"),

    FPTLXY_APPLY(FormTheme.F_TLXY, FlowProjectType.PMT_XM, new FppTlxy(), FormPageType.FPT_APPLY.getKey(), "tlxy_applyForm"),
    FPTLXY_INDEX(FormTheme.F_TLXY, FlowProjectType.PMT_XM, new FppTlxy(), FormPageType.FPT_INDEX.getKey(), ActSval.ASD_HOME),
    FPTLXY_VIEWF(FormTheme.F_TLXY, FlowProjectType.PMT_XM, new FppTlxy(), FormPageType.FPT_VIEWF.getKey(), "tlxy_viewForm"),
    FPTLXY_VIEWA(FormTheme.F_TLXY, FlowProjectType.PMT_XM, new FppTlxy(), FormPageType.FPT_VIEWA.getKey(), "tlxy_viewAdminForm"),

    FPNPAIM_APPLY(FormTheme.F_NP_AIM, FlowProjectType.PMT_XM, new FppHsxm(), FormPageType.FPT_APPLY.getKey(), "npaim_applyForm"),
    FPNPAIM_INDEX(FormTheme.F_NP_AIM, FlowProjectType.PMT_XM, new FppHsxm(), FormPageType.FPT_INDEX.getKey(), ActSval.ASD_HOME),
    FPNPAIM_VIEWF(FormTheme.F_NP_AIM, FlowProjectType.PMT_XM, new FppHsxm(), FormPageType.FPT_VIEWF.getKey(), "npaim_viewForm"),
    FPNPAIM_VIEWA(FormTheme.F_NP_AIM, FlowProjectType.PMT_XM, new FppHsxm(), FormPageType.FPT_VIEWA.getKey(), "npaim_viewAdminForm"),

    FPHSXM_APPLY(FormTheme.F_HSXM, FlowProjectType.PMT_XM, new FppHsxm(), FormPageType.FPT_APPLY.getKey(), "hsxm_applyForm"),
    FPHSXM_INDEX(FormTheme.F_HSXM, FlowProjectType.PMT_XM, new FppHsxm(), FormPageType.FPT_INDEX.getKey(), ActSval.ASD_HOME),
    FPHSXM_VIEWF(FormTheme.F_HSXM, FlowProjectType.PMT_XM, new FppHsxm(), FormPageType.FPT_VIEWF.getKey(), "hsxm_viewForm"),
    FPHSXM_VIEWA(FormTheme.F_HSXM, FlowProjectType.PMT_XM, new FppHsxm(), FormPageType.FPT_VIEWA.getKey(), "hsxm_viewAdminForm"),


    FPTEST_APPLY(FormTheme.F_TEST, FlowProjectType.PMT_XM, new FppTest(), FormPageType.FPT_APPLY.getKey(), "testgx_applyForm"),
    FPTEST_INDEX(FormTheme.F_TEST, FlowProjectType.PMT_XM, new FppTest(), FormPageType.FPT_INDEX.getKey(), ActSval.ASD_HOME),
    FPTEST_VIEWF(FormTheme.F_TEST, FlowProjectType.PMT_XM, new FppTest(), FormPageType.FPT_VIEWF.getKey(), "testgx_viewForm"),
    FPTEST_VIEWA(FormTheme.F_TEST, FlowProjectType.PMT_XM, new FppTest(), FormPageType.FPT_VIEWA.getKey(), "testgx_viewAdminForm"),

    FGGZSMXX_APPLY(FormTheme.F_GZSMXX, FlowProjectType.PMT_DASAI, new GppGzsmxx(), FormPageType.FPT_APPLY.getKey(), "gzsmxx_applyForm"),
    FGGZSMXX_INDEX(FormTheme.F_GZSMXX, FlowProjectType.PMT_DASAI, new GppGzsmxx(), FormPageType.FPT_INDEX.getKey(), ActSval.ASD_HOME),
    FGGZSMXX_VIEWF(FormTheme.F_GZSMXX, FlowProjectType.PMT_DASAI, new GppGzsmxx(), FormPageType.FPT_VIEWF.getKey(), "gzsmxx_viewForm"),
    FGGZSMXX_VIEWA(FormTheme.F_GZSMXX, FlowProjectType.PMT_DASAI, new GppGzsmxx(), FormPageType.FPT_VIEWA.getKey(), "gzsmxx_viewAdminForm"),
    ;

    private static Logger logger = LoggerFactory.getLogger(FormPage.class);
    public static final String TEMPLATE_FORM_ROOT = "/template/formtheme/";
    private FormTheme theme;
    private FlowProjectType ptype;
    private String key;
    private String url;
    private IpageParam param;

    private FormPage(FormTheme theme, FlowProjectType ptype, IpageParam param, String key, String url) {
        this.theme = theme;
        this.ptype = ptype;
        this.key = key;
        this.url = url;
        this.param = param;
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

    /**
     * 获取完整模板文件路径.
     * @param proval 项目类型标识,需要非空验证
     * @param page 项目类型标识,需要非空验证
     * @return String
     */
    public static String getTplUrl(String proval, FormPage page) {
        //return TEMPLATE_FORM_ROOT + proval + StringUtil.LINE + page.getUrl();
        return getTplUrl(true, proval, page);
    }

    public static String getTplUrl(Boolean noTpl, String proval, FormPage page) {
        if(noTpl){
            return proval + StringUtil.LINE + page.getUrl();
        }else{
            return TEMPLATE_FORM_ROOT + proval + StringUtil.LINE + page.getUrl();
        }
    }

    /**
     * 获取完整模板文件绝对路径.
     * @param actYw 项目流程
     * @param pageType 页面类型
     * @param params 地址参数
     * @return String
     */
    public static String getAbsUrl(Boolean noTpl, ActYw actYw, FormPageType pageType, String params) {
        if(params == null){
            params = StringUtil.EMPTY;
        }
        FormTheme formTheme = actYw.getFtheme();
        FlowProjectType flowProType = actYw.getFptype();
        if((formTheme == null) || (flowProType == null)){
            logger.error("参数为空，没有找到对应的页面！");
            return CorePages.ERROR_404.getIdxUrl();
        }
        FormPage curPage =  FormPage.getByTheme(formTheme, pageType, flowProType.getKey());
        if((curPage == null)){
            logger.error("页面可能没有定义，没有找到对应的页面！");
            return CorePages.ERROR_404.getIdxUrl();
        }
        if(noTpl == null){
            noTpl = false;
        }
        return getTplUrl(noTpl, flowProType.getValue(), curPage) + params;
    }

    public static String getAbsUrl(ActYw actYw, FormPageType pageType, String params) {
        return getAbsUrl(false, actYw, pageType, params);
    }

    /**
     * 根据主题和key获取枚举 .
     * @author chenhao
     * @param theme 主题标识
     * @param ptype 项目类型
     * @param key 页面标识
     * @return FlowPage
     */
    public static FormPage getByKey(FormTheme theme, String ptype, String key) {
        if ((theme == null) || (StringUtil.isEmpty(ptype))  || (StringUtil.isEmpty(key))) {
            return null;
        }

        FlowProjectType flowPtype = FlowProjectType.getByKey(ptype);
        if(flowPtype == null){
            return null;
        }
        FormPage[] entitys = FormPage.values();
        for (FormPage entity : entitys) {
            if ((theme).equals(entity.getTheme()) && (flowPtype).equals(entity.getPtype()) && (key).equals(entity.getKey())) {
                return entity;
            }
        }
        return null;
    }

    /**
     * 根据主题获取枚举 .
     * @author chenhao
     * @param theme 主题标识
     * @return FlowPage
     */
    public static List<FormPage> getByTheme(FormTheme theme) {
        if ((theme == null)) {
            return null;
        }
        List<FormPage> flowPages = Lists.newArrayList();
        FormPage[] entitys = FormPage.values();
        for (FormPage entity : entitys) {
            if ((theme).equals(entity.getTheme())) {
                flowPages.add(entity);
            }
        }
        return flowPages;
    }

    /**
     * 根据主题获取枚举 .
     * @author chenhao
     * @param theme 主题标识
     * @param ptype 项目类型
     * @return FlowPage
     */
    public static List<FormPage> getByTheme(FormTheme theme, String ptype) {
        if ((theme == null) || (StringUtil.isEmpty(ptype))) {
            return null;
        }
        FlowProjectType flowPtype = FlowProjectType.getByKey(ptype);
        if(flowPtype == null){
            return null;
        }
        List<FormPage> flowPages = Lists.newArrayList();
        FormPage[] entitys = FormPage.values();
        for (FormPage entity : entitys) {
            if ((theme).equals(entity.getTheme()) && (flowPtype).equals(entity.getPtype())) {
                flowPages.add(entity);
            }
        }
        return flowPages;
    }

    /**
     * 根据主题获取枚举 .
     * @author chenhao
     * @param theme 主题标识
     * @param fpType 页面类型
     * @param ptype 项目类型
     * @return FlowPage
     */
    public static FormPage getByTheme(FormTheme theme, FormPageType fpType, String ptype) {
        if ((theme == null) || (fpType == null) || (StringUtil.isEmpty(ptype))) {
            return null;
        }
        System.out.println(fpType.getName() + "-" + fpType.getKey());
        FormPage[] entitys = FormPage.values();
        for (FormPage entity : entitys) {
            if ((entity.getTheme()).equals(theme) && (entity.getKey()).equals(fpType.getKey()) && (ptype).equals(entity.getPtype().getKey())) {
                return entity;
            }
        }
        return null;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public FormTheme getTheme() {
        return theme;
    }

    public void setTheme(FormTheme theme) {
        this.theme = theme;
    }

    public IpageParam getParam() {
        return param;
    }

    public void setParam(IpageParam param) {
        this.param = param;
    }

    public FlowProjectType getPtype() {
        return ptype;
    }

    public void setPtype(FlowProjectType ptype) {
        this.ptype = ptype;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

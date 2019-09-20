package com.oseasy.com.pcore.modules.syt.manager.sub;

import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.syt.manager.ISytMvo;

/**
 * Created by Administrator on 2019/4/19 0019.
 * 初始化顶级机构必填参数.
 */
public class SytmvOffice extends SupSytmvo implements ISytMvo{
    /****************************************************************
     * 参数属性：用于执行当前操作所需要的参数.
     ****************************************************************/
    /**
     * 机构ID.
     */
    private String id;

    /**
     * 当前租户.
     */
    private SytmvTenant sytmvTenant;

    /**
     * 当前租户机构的父机构.
     */
    private Office poffice;

    /****************************************************************
     * 传递属性：用于提供给后续操作使用的参数.
     ****************************************************************/
    /**
     * 当前模板机构.
     */
    private Office officeTpl;
    /**
     * 当前机构.
     */
    private Office office;

    public SytmvOffice(SytmvTenant sytmvTenant, String id) {
        this.id = id;
        this.sytmvTenant = sytmvTenant;
    }

    public SytmvOffice(SytmvTenant sytmvTenant, String id, Office poffice) {
        this.id = id;
        this.sytmvTenant = sytmvTenant;
        this.poffice = poffice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SytmvTenant getSytmvTenant() {
        return sytmvTenant;
    }

    public void setSytmvTenant(SytmvTenant sytmvTenant) {
        this.sytmvTenant = sytmvTenant;
    }

    public Office getPoffice() {
        return poffice;
    }

    public void setPoffice(Office poffice) {
        this.poffice = poffice;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public Office getOfficeTpl() {
        return officeTpl;
    }

    public void setOfficeTpl(Office officeTpl) {
        this.officeTpl = officeTpl;
    }
}

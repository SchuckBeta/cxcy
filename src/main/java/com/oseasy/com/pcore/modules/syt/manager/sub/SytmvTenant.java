package com.oseasy.com.pcore.modules.syt.manager.sub;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.persistence.BaseEntity;
import com.oseasy.com.pcore.modules.syt.manager.ISytMvo;
import com.oseasy.com.pcore.modules.syt.entity.SysTenant;

/**
 * Created by Administrator on 2019/4/19 0019.
 */
public class SytmvTenant extends SupSytmvo implements ISytMvo{
    /****************************************************************
     * 参数属性：用于执行当前操作所需要的参数.
     ****************************************************************/
    /**
     * 租户表ID.
     */
    private String id;

    /**
     * 租户类型.
     */
    private String type;

    /**
     * 租户名称.
     */
    private String tname;

    /**
     * 租户ID.
     */
    private String tenantId;

    /**
     * 模板租户ID.
     */
    private String tenantTplId;

    /****************************************************************
     * 传递属性：用于提供给后续操作使用的参数.
     ****************************************************************/
    private SysTenant sysTenant;
    private SysTenant sysTenantTpl;

    public SytmvTenant(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantTplId() {
        return tenantTplId;
    }

    public void setTenantTplId(String tenantTplId) {
        this.tenantTplId = tenantTplId;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public SysTenant getSysTenant() {
        return sysTenant;
    }

    public void setSysTenant(SysTenant sysTenant) {
        this.sysTenant = sysTenant;
    }

    public SysTenant getSysTenantTpl() {
        return sysTenantTpl;
    }

    public void setSysTenantTpl(SysTenant sysTenantTpl) {
        this.sysTenantTpl = sysTenantTpl;
    }

    public static void setTenant(SytmvOffice sytmvOffice, BaseEntity entity) {
        setTenant(sytmvOffice.getSytmvTenant(), entity);
    }

    public static void setTenant(SytmvTenant sytmvTenant, BaseEntity entity) {
        if((Sval.EmPn.NCENTER.getPrefix()).equals(sytmvTenant.getType())){
            entity.setTenantId(CoreIds.NCE_SYS_TENANT_TPL.getId());
        }else if((Sval.EmPn.NPROVINCE.getPrefix()).equals(sytmvTenant.getType())){
            entity.setTenantId(CoreIds.NPR_SYS_TENANT_TPL.getId());
        }else if((Sval.EmPn.NSCHOOL.getPrefix()).equals(sytmvTenant.getType())){
            entity.setTenantId(CoreIds.NSC_SYS_TENANT_TPL.getId());
        }
    }
}

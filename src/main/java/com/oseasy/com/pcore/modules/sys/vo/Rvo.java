package com.oseasy.com.pcore.modules.sys.vo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.IRole;
import com.oseasy.com.pcore.common.persistence.UserEntity;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.util.common.utils.StringUtil;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/5/23 0023.
 */
public class Rvo implements Serializable{
    public static final String ROLE = "role";
    public static final String USER = "user";
    public static final String TENANT = "tenant";
    public static final String TENANT_DEL = "tenantDel";
    public static final String OFFICE = "office";

    private String isSuper;//是否为超级管理员
    private String isAdmin;//是否为系统管理员
    private String isAdmyw;//是否为运营管理员
    private String isNprAdmin;//是否为省管理员
    private String isNscAdmin;//是否为校管理员

    private Map<String, List<String>> superFilters;
    private Map<String, List<String>> adminFilters;
    private Map<String, List<String>> admywFilters;
    private Map<String, List<String>> nprAdminFilters;
    private Map<String, List<String>> nscAdminFilters;

    public Rvo() {
        this.isSuper = CoreSval.Const.NO;
        this.isAdmin = CoreSval.Const.NO;
        this.isAdmyw = CoreSval.Const.NO;
        this.isNprAdmin = CoreSval.Const.NO;
        this.isNscAdmin = CoreSval.Const.NO;
    }

    public static Rvo check(UserEntity user){
        Rvo urvo = new Rvo();
        List<String> userRidvo = Lists.newArrayList();
        List<String> roleRidvo = Lists.newArrayList();
        List<String> officeRidvo = Lists.newArrayList();
        List<String> tenantRidvo = Lists.newArrayList();
        List<String> tenantDelRidvo = Lists.newArrayList();
        if(UserEntity.isSupUser(user.getId())){
            roleRidvo.add(CoreIds.NCE_SYS_ROLE_SUPER.getId());

            tenantRidvo.add(CoreIds.NCE_SYS_TENANT_TPL.getId());
            tenantRidvo.add(CoreIds.NPR_SYS_TENANT_TPL.getId());
            tenantRidvo.add(CoreIds.NSC_SYS_TENANT_TPL.getId());
//            tenantRidvo.add(CoreIds.NCE_SYS_TENANT.getId());

            urvo.setIsSuper(CoreSval.Const.YES);

            urvo.getSuperFilters().put(OFFICE, officeRidvo);
            urvo.getSuperFilters().put(TENANT, tenantRidvo);
            urvo.getSuperFilters().put(TENANT_DEL, tenantDelRidvo);
            urvo.getSuperFilters().put(USER, userRidvo);
            urvo.getSuperFilters().put(ROLE, roleRidvo);
            return urvo;
        }

        if(StringUtil.checkNotEmpty(user.getRoleList())){
            if(UserEntity.isSupUser(user.getId())){
                urvo.setIsSuper(CoreSval.Const.YES);
            }

            IRole cur;
            for (Object or: user.getRoleList()) {
                cur  = (IRole) or;
                if(UserEntity.isSuperRole(cur.getRtype())){
                    urvo.setIsSuper(CoreSval.Const.YES);
                    continue;
                }else if(UserEntity.isAdmin(cur.getRtype())){
                    urvo.setIsAdmin(CoreSval.Const.YES);
                    continue;
                }else if(UserEntity.isAdmyw(cur.getRtype())){
                    urvo.setIsAdmyw(CoreSval.Const.YES);
                    continue;
                }else if(UserEntity.isNprAdmin(cur.getRtype())){
                    urvo.setIsNprAdmin(CoreSval.Const.YES);
                    continue;
                }else if(UserEntity.isNscAdmin(cur.getRtype())){
                    urvo.setIsNscAdmin(CoreSval.Const.YES);
                    roleRidvo.add(cur.getId());
                    continue;
                }
            }
        }

        if(!((CoreSval.Const.YES).equals(urvo.getIsSuper()))){
            tenantRidvo.add(CoreIds.NCE_SYS_TENANT_TPL.getId());
            tenantRidvo.add(CoreIds.NPR_SYS_TENANT_TPL.getId());
            tenantRidvo.add(CoreIds.NSC_SYS_TENANT_TPL.getId());

            userRidvo.add(CoreIds.NCE_SYS_USER_SUPER.getId());

            roleRidvo.add(CoreIds.NCE_SYS_ROLE_SUPER.getId());
        }

        if(!((CoreSval.Const.YES).equals(urvo.getIsSuper()) || (CoreSval.Const.YES).equals(urvo.getIsAdmin()))){
            officeRidvo.add("10");
            officeRidvo.add(CoreIds.NCE_SYS_OFFICE_TOP.getId());

            tenantRidvo.add(CoreIds.NCE_SYS_TENANT.getId());

            roleRidvo.add(CoreIds.NCE_SYS_ROLE_ADMIN.getId());
        }

        if(!((CoreSval.Const.YES).equals(urvo.getIsSuper()) || (CoreSval.Const.YES).equals(urvo.getIsAdmin()) || (CoreSval.Const.YES).equals(urvo.getIsAdmyw()))){
            officeRidvo.add(CoreIds.NCE_SYS_OFFICE_TOP.getId());

            tenantRidvo.add(CoreIds.NCE_SYS_TENANT.getId());

            roleRidvo.add(CoreIds.NCE_SYS_ROLE_ADMYW.getId());
        }

        if(!(((CoreSval.Const.YES).equals(urvo.getIsNprAdmin())) || ((CoreSval.Const.YES).equals(urvo.getIsSuper())) || ((CoreSval.Const.YES).equals(urvo.getIsAdmin())) || ((CoreSval.Const.YES).equals(urvo.getIsAdmyw())))){
            officeRidvo.add(CoreIds.NPR_SYS_OFFICE_TOP.getId());

            tenantRidvo.add(CoreIds.NPR_SYS_TENANT.getId());

            roleRidvo.add(CoreIds.NPR_SYS_ROLE_ADMIN.getId());
        }

        if(!((CoreSval.Const.YES).equals(urvo.getIsNscAdmin()))){
            officeRidvo.add(CoreIds.NSC_SYS_OFFICE_TOP.getId());

            roleRidvo.add(CoreIds.NSC_SYS_ROLE_ADMIN.getId());
        }


        tenantDelRidvo.add(CoreIds.NCE_SYS_TENANT_TPL.getId());
        tenantDelRidvo.add(CoreIds.NPR_SYS_TENANT_TPL.getId());
        tenantDelRidvo.add(CoreIds.NSC_SYS_TENANT_TPL.getId());
        tenantDelRidvo.add(CoreIds.NCE_SYS_TENANT.getId());
        tenantDelRidvo.add(CoreIds.NPR_SYS_TENANT.getId());
        if(((CoreSval.Const.YES).equals(urvo.getIsSuper()))){
            roleRidvo.add(CoreIds.NCE_SYS_ROLE_SUPER.getId());
            urvo.getSuperFilters().put(OFFICE, officeRidvo);
            urvo.getSuperFilters().put(TENANT, tenantRidvo);
            urvo.getSuperFilters().put(TENANT_DEL, tenantDelRidvo);
            urvo.getSuperFilters().put(USER, userRidvo);
            urvo.getSuperFilters().put(ROLE, roleRidvo);
        }

        if(((CoreSval.Const.YES).equals(urvo.getIsAdmin()))){
            roleRidvo.add(CoreIds.NCE_SYS_ROLE_ADMIN.getId());
            urvo.getAdminFilters().put(OFFICE, officeRidvo);
            urvo.getAdminFilters().put(TENANT, tenantRidvo);
            urvo.getAdminFilters().put(TENANT_DEL, tenantDelRidvo);
            urvo.getAdminFilters().put(USER, userRidvo);
            urvo.getAdminFilters().put(ROLE, roleRidvo);
        }

        if(((CoreSval.Const.YES).equals(urvo.getIsAdmyw()))){
            roleRidvo.add(CoreIds.NCE_SYS_ROLE_ADMYW.getId());
            urvo.getAdmywFilters().put(OFFICE, officeRidvo);
            urvo.getAdmywFilters().put(TENANT, tenantRidvo);
            urvo.getAdmywFilters().put(TENANT_DEL, tenantDelRidvo);
            urvo.getAdmywFilters().put(USER, userRidvo);
            urvo.getAdmywFilters().put(ROLE, roleRidvo);
        }

        if(((CoreSval.Const.YES).equals(urvo.getIsNprAdmin()))){
            roleRidvo.add(CoreIds.NPR_SYS_ROLE_ADMIN.getId());
            urvo.getNprAdminFilters().put(OFFICE, officeRidvo);
            urvo.getNprAdminFilters().put(TENANT, tenantRidvo);
            urvo.getNprAdminFilters().put(TENANT_DEL, tenantDelRidvo);
            urvo.getNprAdminFilters().put(USER, userRidvo);
            urvo.getNprAdminFilters().put(ROLE, roleRidvo);
        }

        if(((CoreSval.Const.YES).equals(urvo.getIsNscAdmin()))){
            roleRidvo.add(CoreIds.NSC_SYS_ROLE_ADMIN.getId());
            urvo.getNscAdminFilters().put(OFFICE, officeRidvo);
            urvo.getNscAdminFilters().put(TENANT, tenantRidvo);
            urvo.getNscAdminFilters().put(TENANT_DEL, tenantDelRidvo);
            urvo.getNscAdminFilters().put(USER, userRidvo);
            urvo.getNscAdminFilters().put(ROLE, roleRidvo);
        }
        return urvo;
    }

    public String getIsSuper() {
        return isSuper;
    }

    public void setIsSuper(String isSuper) {
        this.isSuper = isSuper;
    }

    public String getIsNscAdmin() {
        return isNscAdmin;
    }

    public void setIsNscAdmin(String isNscAdmin) {
        this.isNscAdmin = isNscAdmin;
    }

    public String getIsNprAdmin() {
        return isNprAdmin;
    }

    public void setIsNprAdmin(String isNprAdmin) {
        this.isNprAdmin = isNprAdmin;
    }

    public String getIsAdmyw() {
        return isAdmyw;
    }

    public void setIsAdmyw(String isAdmyw) {
        this.isAdmyw = isAdmyw;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Map<String, List<String>> getSuperFilters() {
        if(this.superFilters == null){
            this.superFilters = Maps.newHashMap();
        }
        return superFilters;
    }

    public void setSuperFilters(Map<String, List<String>> superFilters) {
        this.superFilters = superFilters;
    }

    public Map<String, List<String>> getAdminFilters() {
        if(this.adminFilters == null){
            this.adminFilters = Maps.newHashMap();
        }
        return adminFilters;
    }

    public void setAdminFilters(Map<String, List<String>> adminFilters) {
        this.adminFilters = adminFilters;
    }

    public Map<String, List<String>> getAdmywFilters() {
        if(this.admywFilters == null){
            this.admywFilters = Maps.newHashMap();
        }
        return admywFilters;
    }

    public void setAdmywFilters(Map<String, List<String>> admywFilters) {
        this.admywFilters = admywFilters;
    }

    public Map<String, List<String>> getNprAdminFilters() {
        if(this.nprAdminFilters == null){
            this.nprAdminFilters = Maps.newHashMap();
        }
        return nprAdminFilters;
    }

    public void setNprAdminFilters(Map<String, List<String>> nprAdminFilters) {
        this.nprAdminFilters = nprAdminFilters;
    }

    public Map<String, List<String>> getNscAdminFilters() {
        if(this.nscAdminFilters == null){
            this.nscAdminFilters = Maps.newHashMap();
        }
        return nscAdminFilters;
    }

    public void setNscAdminFilters(Map<String, List<String>> nscAdminFilters) {
        this.nscAdminFilters = nscAdminFilters;
    }
}

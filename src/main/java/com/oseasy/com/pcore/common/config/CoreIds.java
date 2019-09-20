package com.oseasy.com.pcore.common.config;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.util.common.utils.StringUtil;

import java.util.List;

/**
 * 系统固定ID
 * @author Administrator
 */
public enum CoreIds {
    NCE_SYS_TREE_ROOT(Sval.EmPn.NCENTER, "1", "系统树根节点"),
    NCE_SYS_TREE_PROOT(Sval.EmPn.NCENTER, "0", "系统树根节点父节点"),

    NCE_SYS_TENANT_TPL(Sval.EmPn.NCENTER, "1", "内置运营模板租户"),
    NCE_SYS_TENANT(Sval.EmPn.NCENTER, "10", "运营租户"),
    NCE_SYS_USER_SUPER(Sval.EmPn.NCENTER, "1", "系统超级管理员用户"),
    NCE_SYS_ROLE_SUPER(Sval.EmPn.NCENTER, "1", "超级管理员角色（数据备份）"),
    NCE_SYS_ROLE_ADMIN(Sval.EmPn.NCENTER, "10", "系统管理员角色（最高管理员）"),
    NCE_SYS_ROLE_ADMYW(Sval.EmPn.NCENTER, "11", "系统运营管理员角色（运维人员）"),
    NCE_SYS_OFFICE_TOP(Sval.EmPn.NCENTER, "1", "系统顶级机构"),

    NPR_SYS_TENANT_TPL(Sval.EmPn.NPROVINCE, "2", "内置省模板租户"),
    NPR_SYS_TENANT(Sval.EmPn.NPROVINCE, "20", "省租户"),
    NPR_SYS_ROLE_ADMIN(Sval.EmPn.NPROVINCE, "2", "省管理员角色（省最高管理员）"),
    NPR_SYS_OFFICE_TOP(Sval.EmPn.NPROVINCE, "2", "省顶级机构"),

    NSC_SYS_TENANT_TPL(Sval.EmPn.NSCHOOL, "3", "内置校模板租户"),
    NSC_SYS_TENANT(Sval.EmPn.NSCHOOL, "30", "校演示租户"),
    NSC_SYS_ROLE_ADMIN(Sval.EmPn.NSCHOOL, "3", "校管理员角色（校最高管理员）"),
    NSC_SYS_OFFICE_TOP(Sval.EmPn.NSCHOOL, "3", "校顶级机构")
    ;

    private Sval.EmPn pn;
    private String id;
    private String remark;

    private CoreIds(Sval.EmPn pn, String id, String remark) {
        this.id = id;
        this.pn = pn;
        this.remark = remark;
    }

    public String getId() {
        return id;
    }

    public Sval.EmPn getPn() {
        return pn;
    }

    public String getRemark() {
        return remark;
    }

    public static CoreIds getById(String id) {
        CoreIds[] entitys = CoreIds.values();
        for (CoreIds entity : entitys) {
            if ((id).equals(entity.getId())) {
                return entity;
            }
        }
        return null;
    }

    /**
     * 根据ID判断当前租户是否为模板租户.
     * @return boolean
     */
    public static boolean checkTpl(String id) {
        List<CoreIds> tpls = Lists.newArrayList();
        tpls.add(CoreIds.NCE_SYS_TENANT_TPL);
        tpls.add(CoreIds.NPR_SYS_TENANT_TPL);
        tpls.add(CoreIds.NSC_SYS_TENANT_TPL);
        for (CoreIds entity : tpls) {
            if ((id).equals(entity.getId())) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unused")
    @Override
    public String toString() {
        if(this != null){
            return "{\"id\":\"" + this.id + ",\"remark\":\"" + this.remark + "\"}";
        }
        return super.toString();
    }

    /**
     *  parentIds值  (0,).
     */
    public static String treePpids() {
        return NCE_SYS_TREE_PROOT.getId() + StringUtil.DOTH;
    }

    /**
     * parentIds值  (0,1,).
     */
    public static String treeRpids() {
        return NCE_SYS_TREE_PROOT.getId() + StringUtil.DOTH + NCE_SYS_TREE_ROOT.getId() + StringUtil.DOTH;
    }


    /**
     * 枚举转字符串.
     */
    public static String listToStr(List<CoreIds> cids) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < cids.size(); i++) {
            CoreIds cid = cids.get(i);
            if(i == 0){
                buffer.append(cid.getId());
            }else{
                buffer.append(StringUtil.DOTH);
                buffer.append(cid.getId());
            }
        }
        return buffer.toString();
    }

    /**
     * 枚举转字符串.
     */
    public static List<String> listId(List<CoreIds> ids) {
        List<String> rids = Lists.newArrayList();
        for (CoreIds cid : ids) {
            rids.add(cid.getId());
        }
        return rids;
    }

    /**
     * 运营管理员排除数据.
     * 1,2
     * @return
     */
    public static List<String> filterTenantByNc(User user) {
        List<CoreIds> es = Lists.newArrayList();
        if(user == null){
            es.add(CoreIds.NCE_SYS_TENANT_TPL);
            es.add(CoreIds.NPR_SYS_TENANT_TPL);
            es.add(CoreIds.NSC_SYS_TENANT_TPL);
            return CoreIds.listId(es);
        }

        if(User.isSuper(user)){
            //不过滤,显示所有
        }else if(User.isAdmin(user)){
            es.add(CoreIds.NCE_SYS_TENANT_TPL);
            es.add(CoreIds.NPR_SYS_TENANT_TPL);
            es.add(CoreIds.NSC_SYS_TENANT_TPL);
        }else if(User.isAdmyw(user)){
            es.add(CoreIds.NCE_SYS_TENANT_TPL);
            es.add(CoreIds.NPR_SYS_TENANT_TPL);
            es.add(CoreIds.NSC_SYS_TENANT_TPL);
            es.add(CoreIds.NCE_SYS_TENANT);
            //不过滤
        }
        return CoreIds.listId(es);
    }

    /**
     * 运营管理员切换排除数据.
     * 1,2
     * @return
     */
    public static List<String> filterChangeTenantByNc(User user) {
        List<CoreIds> es = Lists.newArrayList();
        if(user == null){
            es.add(CoreIds.NCE_SYS_TENANT_TPL);
            es.add(CoreIds.NPR_SYS_TENANT_TPL);
            es.add(CoreIds.NSC_SYS_TENANT_TPL);
            return CoreIds.listId(es);
        }

        if(User.isSuper(user)){
            //不过滤,显示所有
        }else if(User.isAdmin(user) || User.isAdmyw(user)){
            es.add(CoreIds.NCE_SYS_TENANT_TPL);
            es.add(CoreIds.NPR_SYS_TENANT_TPL);
            es.add(CoreIds.NSC_SYS_TENANT_TPL);
            //不过滤
        }
        return CoreIds.listId(es);
    }

    /**
     * 运营人员及以下角色排除数据.
     * 1,2,3,10
     * @return
     */
    public static List<String> filterTenantByNcAdminYw(User user) {
        List<CoreIds> es = Lists.newArrayList();
        es.add(CoreIds.NCE_SYS_TENANT_TPL);
        es.add(CoreIds.NPR_SYS_TENANT_TPL);
        es.add(CoreIds.NSC_SYS_TENANT_TPL);

        es.add(CoreIds.NCE_SYS_TENANT);
        return CoreIds.listId(es);
    }

    /**
     * 省管理员及以下角色排除数据.
     * 1,2,3,10,20
     * @return
     */
    public static List<String> filterTenantByNp(User user) {
        List<CoreIds> es = Lists.newArrayList();
        if(user == null){
            es.add(CoreIds.NCE_SYS_TENANT_TPL);
            es.add(CoreIds.NPR_SYS_TENANT_TPL);
            es.add(CoreIds.NSC_SYS_TENANT_TPL);
            es.add(CoreIds.NCE_SYS_TENANT);
            es.add(CoreIds.NPR_SYS_TENANT);
            es.add(CoreIds.NSC_SYS_TENANT);
            return CoreIds.listId(es);
        }

        if(User.isSuper(user)){
            //不过滤,显示所有
        }else if(User.isAdmin(user)){
            es.add(CoreIds.NCE_SYS_TENANT_TPL);
            es.add(CoreIds.NPR_SYS_TENANT_TPL);
            es.add(CoreIds.NSC_SYS_TENANT_TPL);
        }else if(User.isAdmyw(user)){
            es.add(CoreIds.NCE_SYS_TENANT_TPL);
            es.add(CoreIds.NPR_SYS_TENANT_TPL);
            es.add(CoreIds.NSC_SYS_TENANT_TPL);
            es.add(CoreIds.NCE_SYS_TENANT);
            //不过滤
        }
        return CoreIds.listId(es);
    }



    /**
     * 校管理员及以下角色排除数据.
     * 1,2,3,10,20,30
     * @return
     */
    public static List<String> filterTenantByNsAdmin() {
        List<CoreIds> es = Lists.newArrayList();
        es.add(CoreIds.NCE_SYS_TENANT_TPL);
        es.add(CoreIds.NPR_SYS_TENANT_TPL);
        es.add(CoreIds.NSC_SYS_TENANT_TPL);

        es.add(CoreIds.NCE_SYS_TENANT);
        es.add(CoreIds.NPR_SYS_TENANT);
        es.add(CoreIds.NSC_SYS_TENANT);
        return CoreIds.listId(es);
    }

    /**
     * 校管理员及以下角色排除数据.
     * 1,2,3,10,20,30
     * @return
     */
    public static List<String> filterTenantNoProvinceByNsAdmin() {
        List<CoreIds> es = Lists.newArrayList();
        es.add(CoreIds.NCE_SYS_TENANT_TPL);
        es.add(CoreIds.NPR_SYS_TENANT_TPL);
        es.add(CoreIds.NSC_SYS_TENANT_TPL);
        es.add(CoreIds.NCE_SYS_TENANT);
        es.add(CoreIds.NSC_SYS_TENANT);
        return CoreIds.listId(es);
    }


}

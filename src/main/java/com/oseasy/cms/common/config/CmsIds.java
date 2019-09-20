package com.oseasy.cms.common.config;

/**
 * 系统固定ID
 *
 * @author Administrator
 */
public enum CmsIds {
    SITE_TOP("1", "系统官方站点"),
    SITE_CATEGORYS_SYS_ROOT("1", "系统顶级栏目"),
    SITE_CATEGORYS_TOP_ROOT("ca46923a84ef4754b58ae89e21e97d69", "系统网站顶级栏目"),
    SITE_CATEGORYS_TOP_INDEX("3817dff6b23a408b8fe131595dfffcbc", "系统官方网站首页栏目"),
    SITE_CATEGORYS_GCONTEST_ROOT("448e7bc14f3c477fa31a7c47fe016c12", "系统大赛栏目"),
    SITE_CATEGORYS_PROJECT_ROOT("c5c65c9a80a849cfbe4a05741b78d902", "系统项目栏目"),
    CATEGORY_SCMATCH_ID("99", "双创赛事栏目ID")
    ;

    private String id;
    private String remark;

    private CmsIds(String id, String remark) {
        this.id = id;
        this.remark = remark;
    }

    public String getId() {
        return id;
    }


    public String getRemark() {
        return remark;
    }

    public static CmsIds getById(String id) {
        CmsIds[] entitys = CmsIds.values();
        for (CmsIds entity : entitys) {
            if ((id).equals(entity.getId())) {
                return entity;
            }
        }
        return null;
    }

    @SuppressWarnings("unused")
    @Override
    public String toString() {
        if(this != null){
            return "{\"id\":\"" + this.id + ",\"remark\":\"" + this.remark + "\"}";
        }
        return super.toString();
    }
}

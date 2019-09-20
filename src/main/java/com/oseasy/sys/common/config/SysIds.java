package com.oseasy.sys.common.config;

/**
 * 系统固定ID
 *
 * @author Administrator
 */
public enum SysIds {
    //SYS_ROLE_USER("13757518f4da45ecaa32a3b582e8396a", "学生角色"),
    //SYS_ROLE_TEACHER("21999752ae6049e2bc3d53e8baaac9a5", "导师角色"),
    SYS_COLLEGE_EXPERT("ef8b7924557747e2ac71fe5b52771c08", "学院专家角色Id"),
    SYS_SCHOOL_EXPERT("ecee0da215d04186bdeea0373bf8eeea", "学校专家角色Id"),
    SYS_OFFICE_XY_QT("049168153f064a8896aeeaa97c10d651", "其他"),
    SYS_OFFICE_ZY_QT("5e3bff085a0d4dfab174df37e2a6c82a", "其他"),
    SYS_NO_ROOT("0", "系统序号最大值记录"),

    EXPERT_COLLEGE_EXPERT("ef8b7924557747e2ac71fe5b52771c08", "学院专家"),
    EXPERT_SCHOOL_EXPERT("ecee0da215d04186bdeea0373bf8eeea", "学校专家"),
    EXPERT_OUTSCHOOL_EXPERT("2494442643c24193bc1aa480eddcf43f", "校外专家"),

    //EXPERT_ROLE("5989c28a34024f6f93e309d673709897", "专家"),
    ISMS("0001", "秘书"),
    ISCOLLEGE("0002", "院");



    private String id;
    private String remark;

    private SysIds(String id, String remark) {
        this.id = id;
        this.remark = remark;
    }

    public String getId() {
        return id;
    }


    public String getRemark() {
        return remark;
    }

    public static SysIds getById(String id) {
        SysIds[] entitys = SysIds.values();
        for (SysIds entity : entitys) {
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

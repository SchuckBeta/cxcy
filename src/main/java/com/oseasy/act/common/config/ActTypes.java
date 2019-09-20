package com.oseasy.act.common.config;

import com.oseasy.act.modules.actyw.tool.process.vo.FlowType;
import com.oseasy.com.common.config.Sval;

/**
 * Act固定类型(菜单、栏目)
 * @author Administrator
 */
public enum ActTypes {
    SITE_MENU_GCONTEST_ROOT(FlowType.FWT_DASAI, "10", "系统大赛"),
    SITE_MENU_NPGCONTEST_ROOT(FlowType.FWT_DASAI, "15", "省大赛"),
    SITE_MENU_PROJECT_ROOT(FlowType.FWT_XM, "20", "系统项目"),
    SITE_MENU_NPPROJECT_ROOT(FlowType.FWT_XM, "25", "省项目"),
    SITE_MENU_SCORE_ROOT(FlowType.FWT_SCORE, "30", "系统学分"),
    SITE_PW_ENTER_ROOT(FlowType.FWT_ENTER, "40", "入驻管理"),
    SITE_PW_APPOINTMENT_ROOT(FlowType.FWT_APPOINTMENT, "50", "预约管理")
    ;

    public static final String ATACTTYPES = "at";//菜单标识
    /**
     * 大赛
     */
    public static final String DASAI = "1";


    private String id;
    private FlowType ftype;
    private Sval.EmPn pn;
    private String remark;

    private ActTypes(FlowType ftype, String id, String remark) {
        this.id = id;
        this.ftype = ftype;
        this.remark = remark;
    }

    public String getId() {
        return ATACTTYPES + id;
    }

    public Sval.EmPn getPn() {
        return pn;
    }

    public String getRemark() {
        return remark;
    }

    public static ActTypes getById(String id) {
        ActTypes[] entitys = ActTypes.values();
        for (ActTypes entity : entitys) {
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

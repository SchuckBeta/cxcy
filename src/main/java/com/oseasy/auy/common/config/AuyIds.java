package com.oseasy.auy.common.config;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.Sval;
import com.oseasy.util.common.utils.StringUtil;

import java.util.List;

/**
 * Auy固定ID
 * @author Administrator
 */
public enum AuyIds {
    SITE_MENU_GCONTEST_ROOT(Sval.EmPn.NSCHOOL, "85c6095f275540b9980dde2b06d77382", "系统大赛菜单"),
    SITE_MENU_NPGCONTEST_ROOT(Sval.EmPn.NSCHOOL, "x10ac9622bc449dfbc0c8f3ceb1f6b73", "省大赛菜单"),
    SITE_MENU_PROJECT_ROOT(Sval.EmPn.NSCHOOL, "5474e38a3c8a46f590939df6a453d5f8", "系统项目菜单"),
    SITE_MENU_SCORE_ROOT(Sval.EmPn.NSCHOOL, "810ac9622bc449dfbc0c8f3ceb1f6b73", "系统学分菜单"),
    SITE_PW_ENTER_ROOT(Sval.EmPn.NSCHOOL, "7cff6f6b9b494e25a38877fc634a613a", "入驻管理"),
    SITE_PW_APPOINTMENT_ROOT(Sval.EmPn.NSCHOOL, "5b65b4e07abf4827b7d7d5f0a65f5b50", "预约管理")
    ;

    private Sval.EmPn pn;
    private String id;
    private String remark;

    private AuyIds(Sval.EmPn pn, String id, String remark) {
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

    public static AuyIds getById(String id) {
        AuyIds[] entitys = AuyIds.values();
        for (AuyIds entity : entitys) {
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

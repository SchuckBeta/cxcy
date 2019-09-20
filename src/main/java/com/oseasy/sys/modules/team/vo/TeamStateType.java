package com.oseasy.sys.modules.team.vo;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by Administrator on 2018/5/31.
 */
public enum TeamStateType {
    TM_TG("0", "通过"),
    TM_JSWB("1", "建设完毕"),
    TM_JS("2", "解散"),
    TM_DSH("3", "待审核"),
    TM_TBTG("4", "不通过");
    public static final String TEAM_MEMBER_TYPE = "teamMtype";

    private String key;
    private String name;

    private TeamStateType(String key, String name) {
        this.key = key;
        this.name = name;
    }

    /**
     * 获取卡状态.
     * @return List
     */
    public static List<TeamStateType> getAll() {
        List<TeamStateType> enty = Lists.newArrayList();
        TeamStateType[] entitys = TeamStateType.values();
        for (TeamStateType entity : entitys) {
            if ((entity.getKey() != null)) {
                enty.add(entity);
            }
        }
        return enty;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "{\"key\":\"" + this.key + "\",\"name\":\"" + this.name + "\"}";
    }
}

package com.oseasy.sys.modules.team.vo;

import com.google.common.collect.Lists;


import java.util.List;

/**
 * Created by Administrator on 2018/5/31.
 */
public enum  TeamMtype {
    TM_STU("1", "学生"),
    TM_TEACH("2", "导师");

    public static final String TEAM_MEMBER_TYPE = "teamMtype";

    private String key;
    private String name;

    private TeamMtype(String key, String name) {
        this.key = key;
        this.name = name;
    }

    /**
     * 根据key获取枚举 .
     *
     * @author chenhao
     * @param key
     *            枚举标识
     * @return TeamMtype
     */
    public static TeamMtype getByKey(String key) {
        switch (key) {
            case "1":
                return TM_STU;
            case "2":
                return TM_TEACH;
            default:
                return null;
        }
    }

    /**
     * 获取卡状态.
     * @return List
     */
    public static List<TeamMtype> getAll() {
        List<TeamMtype> enty = Lists.newArrayList();
        TeamMtype[] entitys = TeamMtype.values();
        for (TeamMtype entity : entitys) {
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

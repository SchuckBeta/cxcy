/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.vo
 * @Description [[_GnodeType_]]文件
 * @date 2017年6月27日 下午4:02:08
 *
 */

package com.oseasy.pro.modules.workflow.impl;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.util.common.utils.StringUtil;


/**
 * 字符串前置分隔符.
 * @author chenhao
 * @date 2017年6月27日 下午4:02:08
 *
 */
public enum SpiltPref {
    SPREF_LINE("0", true, false, "/"),
    SPREF_DL("1", false, false, "|"),
    SPREF_J("2", false, false, "#"),
    SPREF_DH("3", false, false, ","),
    SPREF_FH("4", false, false, ";"),
    SPREF_JH("5", false, false, "."),
    SPREF_KH("9", false, true, "()"),
    ;

    private String key;
    private Boolean isDef;
    private Boolean isTwo;
    private String remark;

    private SpiltPref(String key, Boolean isDef, Boolean isTwo, String remark) {
        this.key = key;
        this.isDef = isDef;
        this.isTwo = isTwo;
        this.remark = remark;
    }

    /**
     * 根据id获取SpiltPref .
     * @author chenhao
     * @param key 惟一标识
     * @return GnodeType
     */
    public static SpiltPref getByKey(String key) {
        if(StringUtil.isEmpty(key)){
            return null;
        }
        SpiltPref[] entitys = SpiltPref.values();
        for (SpiltPref entity : entitys) {
            if ((key).equals(entity.getKey())) {
                return entity;
            }
        }
        return null;
    }

    /**
     * 获取所有可用SpiltPref .
     * @author chenhao
     * @return List
     */
    public static List<SpiltPref> getAll() {
        List<SpiltPref> gttypes = Lists.newArrayList();
        for (SpiltPref entity : SpiltPref.values()) {
            gttypes.add(entity);
        }
        return gttypes;
    }

    /**
     * 获取默认 SpiltPref.
     * @author chenhao
     * @return List
     */
    public static SpiltPref getDef() {
        for (SpiltPref entity : SpiltPref.values()) {
            if (entity.getIsDef()) {
                return entity;
            }
        }
        return null;
    }

    /**
     * 获取默认 SpiltPref two 的分隔符.
     * @author chenhao
     * @return List
     */
    public static String[] getTwo(SpiltPref entity) {
        String[] splits = new String[2];
        splits[0] = entity.getRemark().substring(0, 1);
        splits[1] = entity.getRemark().substring(1);
        return splits;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getIsDef() {
        return isDef;
    }

    public void setIsDef(Boolean isDef) {
        this.isDef = isDef;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getIsTwo() {
        return isTwo;
    }

    public void setIsTwo(Boolean isTwo) {
        this.isTwo = isTwo;
    }

    @Override
    public String toString() {
        return "{\"key\":\"" + this.key + "\",\"isDef\":"  + this.isDef + ",\"isTwo\":"  + this.isTwo + ",\"remark\":\"" + this.remark + "\"}";
    }
}

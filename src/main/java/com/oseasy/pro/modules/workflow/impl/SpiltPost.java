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
 * 字符串后置分隔符.
 * @author chenhao
 * @date 2017年6月27日 下午4:02:08
 *
 */
public enum SpiltPost {
    SPOST_LINE("0", false, false, "/"),
    SPOST_DL("1", false, false, "|"),
    SPOST_J("2", false, false, "#"),
    SPOST_DH("3", false, false, ","),
    SPOST_FH("4", true, false, ";"),
    SPOST_JH("5", false, false, "."),
    SPOST_DUN("6", true, false, "、"),
//    SPOST_KH("9", false, true, "()"),
    ;

    private String key;
    private Boolean isDef;
    private Boolean isTwo;
    private String remark;

    private SpiltPost(String key, Boolean isDef, Boolean isTwo, String remark) {
        this.key = key;
        this.isDef = isDef;
        this.isTwo = isTwo;
        this.remark = remark;
    }

    /**
     * 根据id获取SpiltPost .
     * @author chenhao
     * @param key 惟一标识
     * @return GnodeType
     */
    public static SpiltPost getByKey(String key) {
        if(StringUtil.isEmpty(key)){
            return null;
        }
        SpiltPost[] entitys = SpiltPost.values();
        for (SpiltPost entity : entitys) {
            if ((key).equals(entity.getKey())) {
                return entity;
            }
        }
        return null;
    }

    /**
     * 获取所有可用SpiltPost .
     * @author chenhao
     * @param isDef 是否默认
     * @return List
     */
    public static List<SpiltPost> getAll() {
        List<SpiltPost> gttypes = Lists.newArrayList();
        for (SpiltPost entity : SpiltPost.values()) {
            gttypes.add(entity);
        }
        return gttypes;
    }

    /**
     * 获取默认 SpiltPost.
     * @author chenhao
     * @return List
     */
    public static SpiltPost getDef() {
        for (SpiltPost entity : SpiltPost.values()) {
            if (entity.getIsDef()) {
                return entity;
            }
        }
        return null;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getIsTwo() {
        return isTwo;
    }

    public void setIsTwo(Boolean isTwo) {
        this.isTwo = isTwo;
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

    @Override
    public String toString() {
        return "{\"key\":\"" + this.key + "\",\"isDef\":"  + this.isDef + ",\"isTwo\":"  + this.isTwo + ",\"remark\":\"" + this.remark + "\"}";
    }
}

/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.apply.impl;

import java.util.List;

import com.oseasy.act.modules.actyw.tool.apply.IAconfig;
import com.oseasy.com.pcore.modules.sys.entity.Dict;

/**
 * 流程发布页面不同类型流程的特性配置.
 * @author chenhao
 *
 */
public class Aconfig implements IAconfig{
    private String flowType;

    private Boolean hasUpProp;
    private Boolean isShowTime;

    private Boolean hasPtype;//是否有类型
    private String defPtypeks;//默认
    private String dictPtypeks;
    private String dictPtypeKey;
    private Dict dictPtype;
    private List<Dict> dictPtypes;

    private Boolean hasPctype;//是否有类别
    private String defPctypeks;//默认
    private String dictPctypeks;
    private String dictPctypeKey;
    private Dict dictPctype;
    private List<Dict> dictPctypes;

    private List<String> filterIds;//类型忽略显示的ID
    private List<String> gfilterIds;//全局忽略显示的ID

    public Aconfig(String flowType) {
        super();
        this.flowType = flowType;
    }
    public String getFlowType() {
        return flowType;
    }
    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }
    public Boolean getIsShowTime() {
        return isShowTime;
    }
    public void setIsShowTime(Boolean isShowTime) {
        this.isShowTime = isShowTime;
    }
    public Boolean getHasPtype() {
        return hasPtype;
    }
    public void setHasPtype(Boolean hasPtype) {
        this.hasPtype = hasPtype;
    }
    public String getDictPtypeks() {
        return dictPtypeks;
    }
    public void setDictPtypeks(String dictPtypeks) {
        this.dictPtypeks = dictPtypeks;
    }
    public String getDictPtypeKey() {
        return dictPtypeKey;
    }
    public void setDictPtypeKey(String dictPtypeKey) {
        this.dictPtypeKey = dictPtypeKey;
    }
    public Dict getDictPtype() {
        return dictPtype;
    }
    public void setDictPtype(Dict dictPtype) {
        this.dictPtype = dictPtype;
    }
    public Boolean getHasPctype() {
        return hasPctype;
    }
    public void setHasPctype(Boolean hasPctype) {
        this.hasPctype = hasPctype;
    }
    public String getDictPctypeks() {
        return dictPctypeks;
    }
    public void setDictPctypeks(String dictPctypeks) {
        this.dictPctypeks = dictPctypeks;
    }
    public String getDictPctypeKey() {
        return dictPctypeKey;
    }
    public void setDictPctypeKey(String dictPctypeKey) {
        this.dictPctypeKey = dictPctypeKey;
    }
    public Dict getDictPctype() {
        return dictPctype;
    }
    public void setDictPctype(Dict dictPctype) {
        this.dictPctype = dictPctype;
    }

    public List<Dict> getDictPctypes() {
        return dictPctypes;
    }

    public void setDictPctypes(List<Dict> dictPctypes) {
        this.dictPctypes = dictPctypes;
    }

    public List<Dict> getDictPtypes() {
        return dictPtypes;
    }

    public void setDictPtypes(List<Dict> dictPtypes) {
        this.dictPtypes = dictPtypes;
    }

    public List<String> getFilterIds() {
        return filterIds;
    }
    public void setFilterIds(List<String> filterIds) {
        this.filterIds = filterIds;
    }
    public List<String> getGfilterIds() {
        return gfilterIds;
    }
    public void setGfilterIds(List<String> gfilterIds) {
        this.gfilterIds = gfilterIds;
    }
    public Boolean getHasUpProp() {
        return hasUpProp;
    }
    public void setHasUpProp(Boolean hasUpProp) {
        this.hasUpProp = hasUpProp;
    }
    public String getDefPtypeks() {
        return defPtypeks;
    }
    public void setDefPtypeks(String defPtypeks) {
        this.defPtypeks = defPtypeks;
    }
    public String getDefPctypeks() {
        return defPctypeks;
    }
    public void setDefPctypeks(String defPctypeks) {
        this.defPctypeks = defPctypeks;
    }
}

/**
 * .
 */

package com.oseasy.act.modules.actyw.vo;

/**
 * 业务流程添加实体.
 * @author chenhao
 *
 */
public class ActYwVo {
    public static final String KEY_ACTYWVO = "voKey";

    private Boolean isShowTime;//是否需要配置审核时间
    private String dictPtype;//字典类型Key
    public String dictPtypeKey;//字典类型说明
    private String dictPctype;//字典类别Key
    public String dictPctypeKey;//字典类别说明

    public Boolean getIsShowTime() {
        return isShowTime;
    }
    public void setIsShowTime(Boolean isShowTime) {
        this.isShowTime = isShowTime;
    }
    public String getDictPctype() {
        return dictPctype;
    }
    public void setDictPctype(String dictPctype) {
        this.dictPctype = dictPctype;
    }
    public String getDictPctypeKey() {
        return dictPctypeKey;
    }
    public void setDictPctypeKey(String dictPctypeKey) {
        this.dictPctypeKey = dictPctypeKey;
    }
    public String getDictPtype() {
        return dictPtype;
    }
    public void setDictPtype(String dictPtype) {
        this.dictPtype = dictPtype;
    }
    public String getDictPtypeKey() {
        return dictPtypeKey;
    }
    public void setDictPtypeKey(String dictPtypeKey) {
        this.dictPtypeKey = dictPtypeKey;
    }
}

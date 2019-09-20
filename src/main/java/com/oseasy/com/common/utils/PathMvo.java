/**
 * .
 */

package com.oseasy.com.common.utils;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.Sval.Emkey;

/**
 * 模块路径Vo.
 * @author chenhao
 */
public class PathMvo implements Serializable{
    private static final long serialVersionUID = 1L;
    private String key;
    private String sub;
    private String remark;
    private List<PathMsvo> pmsvos;
    public PathMvo(Emkey emkey) {
        super();
        this.key = emkey.getKey();
        this.sub = emkey.getSub();
        this.remark = emkey.getRemark();
        this.pmsvos = Lists.newArrayList();
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getSub() {
        return sub;
    }
    public void setSub(String sub) {
        this.sub = sub;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public List<PathMsvo> getPmsvos() {
        return pmsvos;
    }
    public void setPmsvos(List<PathMsvo> pmsvos) {
        this.pmsvos = pmsvos;
    }
}

/**
 * .
 */

package com.oseasy.com.common.utils;

import java.io.Serializable;

/**
 * 模块路径子模块Vo.
 * @author chenhao
 */
public class PathMsvo implements Serializable{
    private static final long serialVersionUID = 1L;
    private String key;
    private String remark;
    public PathMsvo(String key, String remark) {
        super();
        this.key = key;
        this.remark = remark;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
}

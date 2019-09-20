/**
 * .
 */

package com.oseasy.com.common.utils;

import java.io.Serializable;

/**
 * 模块缓存标识子模块Vo.
 * @author chenhao
 */
public class CkeyMsvo implements Serializable{
    private static final long serialVersionUID = 1L;
    private String key;
    private String remark;
    public CkeyMsvo(String key, String remark) {
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

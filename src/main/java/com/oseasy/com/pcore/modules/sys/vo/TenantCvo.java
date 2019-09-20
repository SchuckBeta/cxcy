package com.oseasy.com.pcore.modules.sys.vo;

import com.oseasy.util.common.utils.StringUtil;

import java.io.Serializable;

/**
 * Created by Administrator on 2019/4/28 0028.
 */
public class TenantCvo implements Serializable{
    private String key;
    private String type;
    private String val;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVal() {
        if(StringUtil.isEmpty(val)){
            this.val = StringUtil.EMPTY;
        }
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        if(StringUtil.isNotEmpty(this.key)){
            buffer.append("{\"key\":\"" + this.key + "\"");
        }else{
            buffer.append("{\"key\":\"\"");
        }

        if(StringUtil.isNotEmpty(this.type)){
            buffer.append(",\"type\":\"" + this.type + "\"");
        }else{
            buffer.append(",\"type\":\"\"");
        }

        if(StringUtil.isNotEmpty(this.val)){
            buffer.append(",\"val\":\"" + this.val + "\"}");
        }else{
            buffer.append(",\"val\":\"\"}");
        }
        return buffer.toString();
    }
}

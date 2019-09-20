package com.oseasy.com.pcore.modules.syt.manager.sub;

import java.io.Serializable;

/**
 * Created by Administrator on 2019/4/19 0019.
 */
public class SupSytmvo implements Serializable{
    /**
     * 名称.
     */
    private String name;

    /**
     * 是否重置操作.
     */
    private boolean reset = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isReset() {
        return reset;
    }

    public void setIsReset(boolean reset) {
        this.reset = reset;
    }

}

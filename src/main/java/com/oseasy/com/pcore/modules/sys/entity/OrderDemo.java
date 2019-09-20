package com.oseasy.com.pcore.modules.sys.entity;


import com.alibaba.fastjson.JSON;

import java.io.Serializable;

public class OrderDemo  implements Serializable {
    private Long orderId;
    private Long userId;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
//        return ReflectionToStringBuilder.toString(this);
        return JSON.toJSONString(this, true);
    }
}

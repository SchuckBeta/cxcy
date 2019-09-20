package com.oseasy.com.common.config;

import java.io.Serializable;

/**
 * cms接口返回结果类
 * Created by liangjie on 2018/8/30.
 */
public class ApiParam<T>  implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
        业务数据
     */
    private T datas;

    public T getDatas() {
        return datas;
    }

    public void setDatas(T datas) {
        this.datas = datas;
    }
}

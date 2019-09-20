package com.oseasy.com.pcore.modules.syt.manager.sub;

import com.oseasy.com.common.config.ApiStatus;
import com.oseasy.com.pcore.modules.syt.manager.ISytModule;
import com.oseasy.com.pcore.modules.syt.manager.ISytMvo;

/**
 * Created by Administrator on 2019/4/19 0019.
 */
public abstract class SupSytm<T extends ISytMvo> implements ISytModule {
    public ApiStatus status = new ApiStatus();
    public T sytmvo;

    public SupSytm() {
    }

    public SupSytm(T sytmvo) {
        this.sytmvo = sytmvo;
    }

    public abstract String name();

    @Override
    public boolean resetTpl() {
        return true;
    }

    @Override
    public boolean pushTpl() {
        return true;
    }

    public boolean before() {
        return (this.sytmvo != null);
    }

    public ApiStatus getStatus() {
        return status;
    }

    public void setStatus(ApiStatus status) {
        this.status = status;
    }
}

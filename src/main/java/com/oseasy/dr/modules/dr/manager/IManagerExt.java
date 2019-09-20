package com.oseasy.dr.modules.dr.manager;

import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.dr.modules.dr.manager.impl.DrCardCparam;

public interface IManagerExt {
    /**
     * 操作执行方法.
     * @param obj 参数
     * @return Boolean
     */
    public ApiTstatus<DrCardRparam> runner(DrCardParam param);

    public Integer getOperType();
}

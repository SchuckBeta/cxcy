package com.oseasy.dr.modules.dr.manager;

import com.oseasy.dr.modules.dr.manager.impl.DrCardCparam;

public interface IManager {
    /**
     * 操作回调方法-成功.
     * @param objs 参数
     * @param sn 卡号
     */
    public void call(DrCardCparam param);

    /**
     * 操作回调方法-失败.
     * @param objs 参数
     * @param sn 卡号
     */
    public void callFail(DrCardCparam param);
}

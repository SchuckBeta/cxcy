package com.oseasy.pro.common.utils.image;

import com.oseasy.com.common.config.ApiGstatus;

/**
 * Created by Administrator on 2017/10/26 0026.
 */
public interface IWaterValidate<R> {
    public String VAL_FAIL = "校验失败！";
    public String VAL_SUCCESS = "校验成功！";
    public IWater<R> getWater();//获取验证对象
    public ApiGstatus validate();//获取验证结果
    public ApiGstatus dealSuccess();//处理验证成功
    public ApiGstatus dealFail();//处理验证失败
}

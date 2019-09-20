package com.oseasy.pro.common.utils.image.impl;

import java.util.Date;

import com.oseasy.com.common.config.ApiGstatus;
import com.oseasy.com.common.config.ApiStatus;
import com.oseasy.pro.common.utils.image.IWater;
import com.oseasy.util.common.utils.StringUtil;

/**
 * Created by Administrator on 2017/10/26 0026.
 */
public class WaterText extends Water<String> {
    private String resource;

    public WaterText() {
      super();
      this.isShow = false;
    }

    public WaterText(String resource) {
      super();
      this.isShow = false;
      this.resource = resource;
    }

    @Override
    public ApiGstatus validate() {
        ApiGstatus rsgroup = super.validate();
        if (StringUtil.isEmpty(this.resource)) {
            rsgroup.getFails().add(new ApiStatus(false, "资源不存在！"));
        }else{
            rsgroup.getSuccesss().add(new ApiStatus(true, "资源合法！"));
        }
        return rsgroup;
    }

    @Override
    public IWater<String> getWater() {
        return this;
    }

    @Override
    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
}

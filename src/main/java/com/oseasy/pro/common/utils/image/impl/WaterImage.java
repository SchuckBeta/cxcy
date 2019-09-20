package com.oseasy.pro.common.utils.image.impl;

import java.io.File;

import com.oseasy.com.common.config.ApiGstatus;
import com.oseasy.com.common.config.ApiStatus;
import com.oseasy.pro.common.utils.image.IWater;

/**
 * Created by Administrator on 2017/10/26 0026.
 */
public class WaterImage extends Water<File>{
    public File resource;

    public WaterImage() {
      super();
      this.isShow = false;
    }

    public WaterImage(File resource) {
      super();
      this.isShow = false;
      this.resource = resource;
    }

    @Override
    public ApiGstatus validate() {
        ApiGstatus rsgroup = super.validate();
        if ((resource == null)) {
            rsgroup.getFails().add(new ApiStatus(false, "资源文件不存在！"));
        }else{
            rsgroup.getSuccesss().add(new ApiStatus(true, "资源文件合法！"));
        }
        return rsgroup;
    }

    @Override
    public IWater<File> getWater() {
        return this;
    }

    @Override
    public File getResource() {
        return resource;
    }

    public void setResource(File resource) {
        this.resource = resource;
    }
}

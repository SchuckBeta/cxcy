package com.oseasy.pro.common.utils.image.impl;

import java.io.File;
import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.ApiGstatus;
import com.oseasy.com.common.config.ApiStatus;
import com.oseasy.pro.common.utils.image.IWater;

/**
 * Created by Administrator on 2017/10/26 0026.
 */
public class WaterSrc extends Water<String> {
  public String resource;

  public WaterSrc() {
    super();
    this.isShow = true;
  }

  public WaterSrc(String resource) {
    super();
    this.isShow = true;
    this.resource = resource;
  }

  @Override
  public ApiGstatus validate() {
    ApiGstatus rsgroup = super.validate();
    List<ApiStatus> rstatusSuccesss = Lists.newArrayList();
    List<ApiStatus> rstatusFails = Lists.newArrayList();

    if ((resource == null)) {
      rstatusFails.add(new ApiStatus(false, "资源文件不存在！"));
    } else {
      rstatusSuccesss.add(new ApiStatus(true, "资源文件合法！"));
    }

    rsgroup.setSuccesss(rstatusSuccesss);
    rsgroup.setFails(rstatusFails);
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

  @Override
  public void setShow(Boolean show) {
    this.isShow = true;
  }
}
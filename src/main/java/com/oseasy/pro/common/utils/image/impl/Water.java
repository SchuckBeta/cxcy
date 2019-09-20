package com.oseasy.pro.common.utils.image.impl;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.ApiGstatus;
import com.oseasy.com.common.config.ApiStatus;
import com.oseasy.pro.common.utils.image.IWater;

/**
 * Created by Administrator on 2017/10/26 0026.
 */
public abstract class Water<T> implements IWater<T>{
    private Double width;
    private Double height;
    private Integer xlt;
    private Integer ylt;
    private Float opacity;
    private Float rate;
    private Boolean hasLoop;
    public Boolean isShow;//显示校验
    private ApiGstatus rsgroup;//显示校验
    public abstract T getResource();

    @Override
    public ApiGstatus validate() {
        List<ApiStatus> rstatusSuccesss = Lists.newArrayList();
        List<ApiStatus> rstatusFails = Lists.newArrayList();

        if (!this.isShow) {
          if (this.rsgroup == null) {
            this.rsgroup = new ApiGstatus();
          }

          this.rsgroup.setSuccesss(rstatusSuccesss);
          this.rsgroup.setFails(rstatusFails);
          return this.rsgroup;
        }

        if ((width == null) || (width >= 0.0)) {
          rstatusFails.add(new ApiStatus(false, "宽度不能为空、且大于0！"));
        }else{
          rstatusSuccesss.add(new ApiStatus(true, "宽度合法！"));
        }

        if ((height == null) || (height >= 0.0)) {
            rstatusFails.add(new ApiStatus(false, "高度不能为空、且大于0！"));
        }else{
            rstatusSuccesss.add(new ApiStatus(true, "高度合法！"));
        }

        if ((xlt == null) || (xlt >= 0.0)) {
            rstatusFails.add(new ApiStatus(false, "左上角X坐标不能为空、且大于0！"));
        }else{
            rstatusSuccesss.add(new ApiStatus(true, "左上角X坐标合法！"));
        }

        if ((ylt == null) || (ylt >= 0.0)) {
            rstatusFails.add(new ApiStatus(false, "左上角Y坐标不能为空、且大于0！"));
        }else{
            rstatusSuccesss.add(new ApiStatus(true, "左上角Y坐标合法！"));
        }

        if ((opacity == null) || ((0.0 <= opacity) && (opacity <= 1.0))) {
            rstatusFails.add(new ApiStatus(false, "透明度不能为空、且大于0小于1！"));
        }else{
            rstatusSuccesss.add(new ApiStatus(true, "透明度合法！"));
        }

        if ((rate == null) || (rate < 0.0)) {
            rstatusFails.add(new ApiStatus(false, "角度不能为空、且大于0！"));
        }else{
            rstatusSuccesss.add(new ApiStatus(true, "角度合法！"));
        }

        rsgroup.setSuccesss(rstatusSuccesss);
        rsgroup.setFails(rstatusFails);
        return rsgroup;
    }

    @Override
    public ApiGstatus dealSuccess() {
        return rsgroup;
    }

    @Override
    public ApiGstatus dealFail() {
        return rsgroup;
    }

    public Boolean getShow() {
        return isShow;
    }

    public void setShow(Boolean show) {
        isShow = show;
    }

    @Override
    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    @Override
    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    @Override
    public Integer getXlt() {
        return xlt;
    }

    public void setXlt(Integer xlt) {
        this.xlt = xlt;
    }

    @Override
    public Integer getYlt() {
        return ylt;
    }

    public void setYlt(Integer ylt) {
        this.ylt = ylt;
    }

    @Override
    public Float getOpacity() {
        return opacity;
    }

    public void setOpacity(Float opacity) {
        this.opacity = opacity;
    }

    @Override
    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public Boolean getHasLoop() {
      if (hasLoop == null) {
        hasLoop = false;
      }
      return hasLoop;
    }

    public void setHasLoop(Boolean hasLoop) {
      this.hasLoop = hasLoop;
    }
}

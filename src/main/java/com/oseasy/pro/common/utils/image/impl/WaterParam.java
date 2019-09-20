package com.oseasy.pro.common.utils.image.impl;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.ApiGstatus;
import com.oseasy.com.common.config.ApiStatus;
import com.oseasy.pro.common.utils.image.IWaterRunner;
import com.oseasy.util.common.utils.image.ImagePutils;

/**
 * Created by Administrator on 2017/10/26 0026.
 */
public class WaterParam implements IWaterRunner {
    private WaterSrc src;//源文件
    private WaterText water;//公章水印
    private WaterText backIcon;//背景水印图标
    private WaterText backText;//背景水印文本
    private List<Water<String>> param;//内容参数

    @Override
    public ApiGstatus exec() {
      ApiGstatus rtRsg = new ApiGstatus();
      ApiGstatus srcRsg = this.src.validate();
      ApiGstatus waterRsg = this.water.validate();
      ApiGstatus backIconRsg = this.backIcon.validate();
      ApiGstatus backTextRsg = this.backText.validate();

      ApiGstatus paramRsg = new ApiGstatus();
      if ((param != null) && (param.size() > 0)) {
        List<ApiStatus> fails = Lists.newArrayList();
        List<ApiStatus> successs = Lists.newArrayList();

        for (Water<?> rsgroup : param) {
          if (!rsgroup.getShow()) {
            continue;
          }

          ApiGstatus curRsg = rsgroup.validate();
          fails.addAll(curRsg.getFails());
          successs.addAll(curRsg.getSuccesss());
        }
        paramRsg.setFails(fails);
        paramRsg.setSuccesss(successs);
      }
      //参数验证完成,设置结果状态.
      rtRsg.setStatus((srcRsg.getStatus()) && (waterRsg.getStatus()) && (backIconRsg.getStatus()) && (backTextRsg.getStatus()) && (paramRsg.getStatus()));
      if (srcRsg.getStatus()) {
        if (waterRsg.getStatus() && water.getShow()) {
          ImagePutils.pressImage(this.src.getResource(), this.water.getResource(), this.src.getResource(), this.water.getXlt(), this.water.getYlt(), this.water.getOpacity(), this.water.getRate(), this.water.getHasLoop());
        }
        if (backIconRsg.getStatus() && backIcon.getShow()) {
          ImagePutils.pressImage(this.src.getResource(), this.backIcon.getResource(), this.src.getResource(), this.backIcon.getXlt(), this.backIcon.getYlt(), this.backIcon.getOpacity(), this.backIcon.getRate(), this.backIcon.getHasLoop());
        }
        if (backTextRsg.getStatus() && backText.getShow()) {
          ImagePutils.pressImage(this.src.getResource(), this.backText.getResource(), this.src.getResource(), this.backText.getXlt(), this.backText.getYlt(), this.backText.getOpacity(), this.backText.getRate(), this.backText.getHasLoop());
        }
        if (paramRsg.getStatus()) {
          for (Water<String> pm : param) {
            if (pm.getShow()) {
              ImagePutils.pressImage(this.src.getResource(), pm.getResource(), this.src.getResource(), pm.getXlt(), pm.getYlt(), pm.getOpacity(), pm.getRate(), false);
            }
          }
        }
      }

      rtRsg.getFails().addAll(srcRsg.getFails());
      rtRsg.getFails().addAll(waterRsg.getFails());
      rtRsg.getFails().addAll(backIconRsg.getFails());
      rtRsg.getFails().addAll(backTextRsg.getFails());
      rtRsg.getFails().addAll(paramRsg.getFails());
      rtRsg.getSuccesss().addAll(srcRsg.getFails());
      rtRsg.getSuccesss().addAll(waterRsg.getFails());
      rtRsg.getSuccesss().addAll(backIconRsg.getFails());
      rtRsg.getSuccesss().addAll(backTextRsg.getFails());
      rtRsg.getSuccesss().addAll(paramRsg.getFails());
      return rtRsg;
    }

    public WaterSrc getSrc() {
      if (src == null) {
        src = new WaterSrc();
      }
        return src;
    }

    public void setSrc(WaterSrc src) {
        this.src = src;
    }

    public WaterText getWater() {
      if (water == null) {
        water = new WaterText();
      }
        return water;
    }

    public void setWater(WaterText water) {
        this.water = water;
    }

    public WaterText getBackIcon() {
      if (backIcon == null) {
        backIcon = new WaterText();
      }
        return backIcon;
    }

    public void setBackIcon(WaterText backIcon) {
        this.backIcon = backIcon;
    }

    public WaterText getBackText() {
      if (backText == null) {
        backText = new WaterText();
      }
        return backText;
    }

    public void setBackText(WaterText backText) {
        this.backText = backText;
    }

    public List<Water<String>> getParam() {
      if (param == null) {
        param = Lists.newArrayList();
      }
        return param;
    }

    public void setParam(List<Water<String>> param) {
        this.param = param;
    }
}

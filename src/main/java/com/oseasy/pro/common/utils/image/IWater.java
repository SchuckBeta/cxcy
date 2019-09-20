package com.oseasy.pro.common.utils.image;

/**
 * Created by Administrator on 2017/10/26 0026.
 */
public interface IWater<T> extends IWaterValidate<T>{
    public Double getWidth();//获取宽度
    public Double getHeight();//获取高度
    public Integer getXlt();//获取左上角X坐标
    public Integer getYlt();//获取左上角Y坐标
    public Float getOpacity();//获取透明度
    public Float getRate();//获取角度
    public T getResource();//获取资源
}

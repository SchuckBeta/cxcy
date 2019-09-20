package com.oseasy.util.common.utils;

/**
 * Created by zhangzheng on 2017/3/16.
 */
public class FloatUtils {
    /**
     * 除法四舍五入 获得一位float
     * @param total
     * @param number
     * @return
     */
    public static double division(double total, int number) {
        double a = total/(double) number;
        double result = (double)(Math.round(a*10))/10;
        return result;
    }
    /**
     * 除法四舍五入 获得一位float
     * @param total
     * @param number
     * @return
     */
    public static float division(float total, int number) {
        float  a=total/(float) number;
        float   result   =   (float)(Math.round(a*10))/10;
        return result;
    }

    public static float divisionFloat(float total,float divisor) {
        float  a = total/ divisor;
        float  result = (float)(Math.round(a*10))/10;
        return result;
    }

    /**
     * 四舍五入保留一位float
     */
    public static float getOnePoint(float f) {
        float  result = (float)(Math.round(f*10))/10;
        return result;
    }
    /**
     * 获取float 的整数部分
     * @param f
     * @return
     */
    public static int getInt(float f) {
        int i=0;
        i=(int)f;
        return i;
    }

    /**
     * 获取1位float 的小数部分
     * @param f
     * @return
     */
    public static int getPoint(float f) {
        int i=(int)f;
        float res= f-i;
        int point=(int)(res*10);
        return point;
    }

    public static void main(String[] args) {
        float f=1.45f;
        int a=1;
        int b=3;

        System.out.println(getOnePoint( 0.56f));
    }



}

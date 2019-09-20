/**
 * .
 */

package com.oseasy.dr.modules.dr.manager;

import java.util.Calendar;

import Net.PC15.Connector.E_ControllerType;

/**
 * .
 * @author chenhao
 *
 */
public class DrConfig{
    public final static int TIMEOUT = 5000;//超时时长
    public final static int DET_PROT = 8000;//端口
    public final static String DET_DR_NO = "0,1,2,3,";//默认设备门号
    public final static String DET_PSW = "FFFFFFFF";//默认密码
    public final static Integer DET_TYPE = E_ControllerType.FC8800.getValue();//默认设备类型
    public final static int DET_CARD_EXPIRE_MAX = 65535;//有效次数，永久
    public final static int DET_CARD_EXPIRE_MIN = 0;//有效次数，0
    public final static String DET_CARD_EXPIRE_MAX_KEY = "expireMax";//有效次数，永久
    public final static String DET_CARD_EXPIRE_MIN_KEY = "expireMin";//有效次数，永久
    public final static String DET_CARD_EXPIRE_KEY = "expireDefault";//有效次数，永久
    public final static int DET_INDEX = 0;//每次获取数据的长度
    public final static int DET_SIZE = 300;//每次获取数据的长度

    /**
     * 开卡默认期限.
     * @return Calendar
     */
    public static Calendar getExpiry(){
        Calendar cdar = Calendar.getInstance();
        cdar.set(2020, 12, 29);
        return cdar;
    }
}
